import json
import os
import time
from google import genai
from google.genai import types

# [1. 설정]
# 이전에 확인된 유효한 API 키입니다.
GEMINI_API_KEY = "AIzaSyARv8GiCsl48VnI8wjaaL93w_CQr_U5jd8".strip()
client = genai.Client(api_key=GEMINI_API_KEY)

# 리스트 확인 결과 가장 최신인 'gemini-2.5-flash'를 사용합니다.
# 라이브러리 특성상 접두사(models/) 없이 ID만 입력합니다.
MODEL_NAME = 'gemini-2.5-flash'

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_DIR = os.path.join(BASE_DIR, "data")
RAW_FILE = os.path.join(DATA_DIR, "raw_cases.json")
AI_FILE = os.path.join(DATA_DIR, "ai_cases.json")

# 출력 디렉토리 생성
os.makedirs(DATA_DIR, exist_ok=True)

PROMPT_TEMPLATE = """
당신은 일반인을 위한 법률 서비스의 판례 분석 전문가입니다. 
제공된 판례 데이터를 바탕으로 사용자가 이해하기 쉬운 구조화된 JSON으로 변환하세요.

[분석 지침]
1. 제목: 판례의 핵심 내용을 흥미롭고 직관적인 질문 형식으로 작성하세요.
2. 한줄요약: 판결의 결론을 가장 쉬운 문장으로 요약하세요.
3. 스토리: 사건이 어떻게 시작되었는지(start), 어떤 점이 문제였는지(issue), 법원의 최종 판단은 무엇인지(court)를 이야기하듯 작성하세요.
4. 논리: 법원이 왜 그런 결정을 내렸는지 핵심 근거를 최대 3개의 리스트로 정리하세요.
5. 팁: 비슷한 상황에 처한 일반인에게 주는 실질적인 조언(tip)을 포함하세요.

[입력 데이터]
{input_data}

[응답 포맷]
반드시 아래 JSON 구조만 응답하세요:
{{ "id": "번호",
    "header": {{ "category": "카테고리", "result": "판결결과" }},
    "display": {{ "title": "제목", "oneLine": "한줄요약" }},
    "content": {{
        "story": {{ "start": "사건발생", "issue": "법적쟁점", "court": "판결취지" }},
        "logic": ["논리1", "논리2", "논리3"],
        "tip": "전문가 조언"
    }},
    "tags": ["태그1", "태그2"],
    "originInfo": {{ "court": "법원종류", "date": "날짜", "caseNumber": "사건번호" }}
}}
"""


def summarize_case(case, retry_count=0):
    if not case: return None

    content = case.get('content', {})

    # 무료 버전 안정성을 위해 본문 길이를 2000자로 제한
    full_text = content.get('fullText', '')
    if not full_text: full_text = content.get('summary', '내용 없음')

    input_text = f"""
    사건명: {case.get('title')}
    판시사항: {content.get('holdings')}
    판결요지: {content.get('summary')}
    판례본문: {full_text[:2000]} 
    """

    prompt = PROMPT_TEMPLATE.format(input_data=input_text)

    try:
        response = client.models.generate_content(
            model=MODEL_NAME,
            contents=prompt,
            config=types.GenerateContentConfig(
                response_mime_type='application/json',
                temperature=0.2
            )
        )
        return json.loads(response.text)

    except Exception as e:
        err_msg = str(e).lower()

        # 할당량 초과(429) 시 1분 대기 후 재시도
        if "429" in err_msg or "quota" in err_msg:
            if retry_count < 2:
                print(f"\n⚠️ 할당량 초과. 60초 대기 후 다시 시도합니다...")
                time.sleep(60)
                return summarize_case(case, retry_count + 1)
            return "QUOTA_EXCEEDED"

        # 인증 및 권한 에러
        if "401" in err_msg or "403" in err_msg or "key" in err_msg:
            return "AUTH_ERROR"

        print(f"❌ API 에러 (ID: {case.get('id', 'unknown')}): {e}")
        return None


def run_final():
    if not os.path.exists(RAW_FILE):
        print(f"❌ 원본 파일({RAW_FILE})이 없습니다.")
        return

    with open(RAW_FILE, "r", encoding="utf-8") as f:
        raw_cases = json.load(f)

    ai_results = []
    if os.path.exists(AI_FILE):
        try:
            with open(AI_FILE, "r", encoding="utf-8") as f:
                ai_results = json.load(f)
        except:
            pass

    done_ids = {str(item['id']) for item in ai_results}
    todo_count = len(raw_cases) - len(done_ids)

    print(f"🚀 [Gemini] 요약 프로세스 시작 (대상: {todo_count}건)")

    for case in raw_cases:
        case_id = str(case.get('id', ''))
        if not case_id or case_id in done_ids:
            continue

        print(f"⚡ 분석 중 (ID {case_id}): {case.get('title', '')[:15]}...", end=" ", flush=True)

        result = summarize_case(case)

        if result == "QUOTA_EXCEEDED":
            print("\n🛑 일일 할당량을 초과했습니다. 나중에 다시 실행하세요.")
            break

        if result == "AUTH_ERROR":
            print("\n🛑 API 키에 문제가 있습니다. 키 상태를 확인하세요.")
            break

        if result:
            # 원본 메타데이터 유지
            result.update({
                "id": case_id,
                "originInfo": {
                    "court": case.get('court'),
                    "date": case.get('date'),
                    "caseNumber": case.get('caseNumber')
                }
            })
            ai_results.append(result)

            # 건별 실시간 저장
            with open(AI_FILE, "w", encoding="utf-8") as f:
                json.dump(ai_results, f, ensure_ascii=False, indent=2)

            print(f"✅ 완료")

            # 무료 버전 안정성을 위해 15초 대기 (분당 약 4건)
            time.sleep(15)
        else:
            print("⏭️ 건너뜀")


if __name__ == "__main__":
    run_final()