import requests
import json
import os
import time

# [설정]
OC = "yh9035926"
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_DIR = os.path.join(BASE_DIR, "data")
RAW_FILE = os.path.join(DATA_DIR, "raw_cases.json")

# [세분화된 카테고리 맵]
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

def collect_precedents(max_per_keyword=3):
    if not os.path.exists(DATA_DIR): os.makedirs(DATA_DIR)

    all_data = []
    if os.path.exists(RAW_FILE):
        try:
            with open(RAW_FILE, "r", encoding="utf-8") as f:
                all_data = json.load(f)
        except: pass

    existing_ids = {str(c['id']) for c in all_data}
    print(f"🚀 API 명세 기반 수집 시작 (현재 {len(existing_ids)}건 보유)")

    for category, keywords in CATEGORY_MAP.items():
        for kw in keywords:
            print(f"🔎 검색 중: [{category}] {kw}")
            params = {"OC": OC, "target": "prec", "type": "JSON", "query": kw, "display": max_per_keyword}

            try:
                res = requests.get("https://www.law.go.kr/DRF/lawSearch.do", params=params).json()
                items = res.get("PrecSearch", {}).get("prec", [])
                if isinstance(items, dict): items = [items]
                if not items: continue

                for item in items:
                    cid = str(item.get("판례일련번호"))
                    if cid in existing_ids: continue

                    # 상세 정보 조회 (명세서의 response field 매칭)
                    detail_url = f"http://www.law.go.kr/DRF/lawService.do?OC={OC}&target=prec&ID={cid}&type=JSON"
                    d_res = requests.get(detail_url).json()
                    d = d_res.get("PrecService", {})

                    if not d.get("사건명"): continue

                    # 명세서 필드 기반 데이터 구조화
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
                            "holdings": d.get("판시사항"),      # 판시사항
                            "summary": d.get("판결요지"),       # 판결요지
                            "references": d.get("참조조문"),    # 참조조문
                            "refPrecedents": d.get("참조판례"), # 참조판례
                            "fullText": d.get("판례내용")      # 판례내용
                        }
                    }

                    all_data.append(case_entry)
                    existing_ids.add(cid)
                    print(f"   [저장] {d.get('사건번호')}")
                    time.sleep(0.1)

            except Exception as e:
                print(f"⚠️ 에러: {e}")

    with open(RAW_FILE, "w", encoding="utf-8") as f:
        json.dump(all_data, f, ensure_ascii=False, indent=2)
    print(f"\n✅ 완료! 총 {len(all_data)}건 저장됨.")

if __name__ == "__main__":
    collect_precedents(max_per_keyword=2)