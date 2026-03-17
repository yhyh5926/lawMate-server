import requests
import json
import os
import time

# [설정]
OC = "yh9035926"
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_DIR = os.path.join(BASE_DIR, "data")
RAW_FILE = os.path.join(DATA_DIR, "raw_cases.json")

CATEGORY_MAP = {
    "교통형사": ["음주운전", "도주치상", "뺑소니", "무면허운전", "어린이보호구역"],
    "교통민사": ["자동차손해배상", "과실비율", "보험금", "위자료산정"],
    "부동산임대차": ["전세사기", "보증금반환", "권리금", "명도소송", "계약갱신"],
    "부동산매매": ["계약금배액배상", "이중매매", "하자담보책임", "소유권이전등기"],
    "형사재산": ["보이스피싱", "투자사기", "업무상횡령", "업무상배임", "보험사기"],
    "형사강력": ["특수폭행", "주거침입", "협박", "감금", "스토킹범죄"],
    "가사상속": ["재판상이혼", "양육비심판", "상속재산분할", "유류분반환"],
    "근로산재": ["부당해고", "임금체불", "직장내괴롭힘", "산업재해"]
}


def collect_daily_limit(target_new_count=10):
    if not os.path.exists(DATA_DIR): os.makedirs(DATA_DIR)

    all_data = []
    if os.path.exists(RAW_FILE):
        try:
            with open(RAW_FILE, "r", encoding="utf-8") as f:
                all_data = json.load(f)
        except: pass

    existing_ids = {str(c['id']) for c in all_data}
    new_collected = 0
    print(f"🚀 데일리 수집 시작 (보유: {len(existing_ids)}건 / 목표 신규: {target_new_count}건)")

    for category, keywords in CATEGORY_MAP.items():
        if new_collected >= target_new_count: break  # 목표 달성 시 카테고리 순회 중단

        for kw in keywords:
            if new_collected >= target_new_count: break
            
            print(f"🔎 검색 중: [{category}] {kw}")
            # display를 20 정도로 높여서 기존 중복 건너뛰고 새 데이터를 찾을 확률을 높임
            params = {"OC": OC, "target": "prec", "type": "JSON", "query": kw, "display": 20}

            try:
                res = requests.get("https://www.law.go.kr/DRF/lawSearch.do", params=params).json()
                items = res.get("PrecSearch", {}).get("prec", [])
                if isinstance(items, dict): items = [items]
                if not items: continue

                for item in items:
                    if new_collected >= target_new_count: break
                    
                    cid = str(item.get("판례일련번호"))
                    if cid in existing_ids: continue  # 이미 있는 건 무시

                    # 상세 정보 조회
                    detail_url = f"http://www.law.go.kr/DRF/lawService.do?OC={OC}&target=prec&ID={cid}&type=JSON"
                    d_res = requests.get(detail_url).json()
                    d = d_res.get("PrecService", {})

                    if not d.get("사건명"): continue

                    case_entry = {
                        "id": cid,
                        "title": d.get("사건명"),
                        "caseNumber": d.get("사건번호"),
                        "date": d.get("선고일자"),
                        "court": d.get("법원명"),
                        "category": category,
                        "keyword": kw,
                        "metadata": {
                            "courtCode": d.get("법원종류코드"),
                            "caseType": d.get("사건종류명"),
                            "verdictType": d.get("판결유형")
                        },
                        "content": {
                            "holdings": d.get("판시사항"),
                            "summary": d.get("판결요지"),
                            "references": d.get("참조조문"),
                            "refPrecedents": d.get("참조판례"),
                            "fullText": d.get("판례내용")
                        }
                    }

                    all_data.append(case_entry)
                    existing_ids.add(cid)
                    new_collected += 1
                    print(f"   ✨ [신규저장 {new_collected}/{target_new_count}] {d.get('사건번호')}")
                    time.sleep(0.2)

            except Exception as e:
                print(f"⚠️ 에러: {e}")

    # 최종 저장
    with open(RAW_FILE, "w", encoding="utf-8") as f:
        json.dump(all_data, f, ensure_ascii=False, indent=2)
    print(f"\n✅ 오늘 수집 완료! (총 보유 건수: {len(all_data)}건)")


if __name__ == "__main__":
    # 매일 10개씩만 추가하려면 target_new_count를 10으로 설정
    collect_daily_limit(target_new_count=10)
