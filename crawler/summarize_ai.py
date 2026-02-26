import json
import os
import time
from google import genai
from google.genai import types

# [1. 설정]
GEMINI_API_KEY = "AIzaSyBTOqZ6MaHBGOK5Cn5Cet-4e0pGrNbyGTY".strip()  # 주의: API키는 노출되지 않게 관리하세요
client = genai.Client(api_key=GEMINI_API_KEY)
MODEL_NAME = 'gemini-2.0-flash'  # 최신 정식 모델 사용 권장

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_DIR = os.path.join(BASE_DIR, "data")
RAW_FILE = os.path.join(DATA_DIR, "raw_cases.json")
AI_FILE = os.path.join(DATA_DIR, "ai_cases.json")


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
    "header": {{ "category": "카테고리", "result": "판결결과(예: 상고기각/무죄확정)" }},
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


def summarize_case(case):
    if not case: return None

    # 수집한 상세 필드들을 하나로 합쳐서 AI에게 전달
    content = case.get('content', {})
    input_text = f"""
    사건명: {case.get('title')}
    판결유형: {case.get('metadata', {}).get('verdictType')}
    판시사항: {content.get('holdings')}
    판결요지: {content.get('summary')}
    판례본문: {content.get('fullText', '')[:3000]} 
    """

    prompt = PROMPT_TEMPLATE.format(input_data=input_text)

    try:
        response = client.models.generate_content(
            model=MODEL_NAME,
            contents=prompt,
            config=types.GenerateContentConfig(
                response_mime_type='application/json',
                temperature=0.3
            )
        )
        # JSON 문자열을 파이썬 딕셔너리로 변환
        return json.loads(response.text)
    except Exception as e:
        err_msg = str(e).lower()
        if "429" in err_msg or "quota" in err_msg:
            return "QUOTA_EXCEEDED"
        print(f"❌ API 에러 (ID: {case.get('id', 'unknown')}): {e}")
        return None


def run_final():
    if not os.path.exists(RAW_FILE):
        print("❌ 원본 데이터가 없습니다.")
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

    print(f"🚀 [Gemini] 법률 서비스용 최적화 요약 시작")

    for case in raw_cases:
        case_id = str(case.get('id', ''))
        if not case_id or case_id in done_ids: continue

        print(f"⚡ 분석 중 (ID {case_id}): {case.get('title', '')[:20]}...")

        result = summarize_case(case)

        if result == "QUOTA_EXCEEDED":
            print("\n🛑 할당량 초과! 나중에 다시 실행하세요.")
            break

        if result:
            # 원본 핵심 정보와 AI 요약본 결합
            result.update({
                "id": case_id,
                "originInfo": {
                    "court": case.get('court'),
                    "date": case.get('date'),
                    "caseNumber": case.get('caseNumber')
                }
            })
            ai_results.append(result)

            # 중간 저장 (실패 대비)
            with open(AI_FILE, "w", encoding="utf-8") as f:
                json.dump(ai_results, f, ensure_ascii=False, indent=2)

            print(f"✅ 요약 완료: {result['display']['title']}")
            # 속도 제한(Rate Limit) 방지
            time.sleep(5)


if __name__ == "__main__":
    run_final()