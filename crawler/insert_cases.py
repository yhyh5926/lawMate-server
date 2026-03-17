import json
import os
import oracledb

# [1. DB 접속 설정]
DB_CONFIG = {
    "user": "lawMate",
    "password": "1234",
    "dsn": "localhost:1521/xe"  # IP:포트/서비스네임
}

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
# 파일 경로가 프로젝트 구조에 맞는지 확인하세요 (data 폴더 안의 ai_cases.json)
AI_FILE = os.path.join(BASE_DIR, "data", "ai_cases.json")


def insert_to_oracle():
    # 1. AI 요약된 파일 읽기
    if not os.path.exists(AI_FILE):
        print(f"❌ 파일이 없습니다: {AI_FILE}")
        return

    try:
        with open(AI_FILE, "r", encoding="utf-8") as f:
            ai_cases = json.load(f)
    except Exception as e:
        print(f"❌ JSON 읽기 에러: {e}")
        return

    connection = None
    try:
        # 2. 오라클 연결 (Thin 모드로 동작 - 별도 클라이언트 설치 불필요)
        connection = oracledb.connect(
            user=DB_CONFIG["user"],
            password=DB_CONFIG["password"],
            dsn=DB_CONFIG["dsn"]
        )
        cursor = connection.cursor()
        print("✅ Oracle DB 연결 성공!")

        success_count = 0
        skip_count = 0

        # 3. 데이터 루프 및 INSERT
        for case in ai_cases:
            case_no = case['originInfo']['caseNumber']

            # 중복 체크 (사건번호 기준)
            cursor.execute("SELECT COUNT(*) FROM TB_PRECEDENT WHERE CASE_NO = :1", [case_no])
            if cursor.fetchone()[0] > 0:
                print(f"⏭️ 건너뜀 (이미 존재): {case_no}")
                skip_count += 1
                continue

            # 데이터 가공
            keyword_csv = ",".join(case.get('tags', []))
            # content 객체 전체를 JSON 문자열로 변환 (CLOB 저장용)
            ai_summary_json = json.dumps(case.get('content', {}), ensure_ascii=False)

            # SQL 쿼리 (가독성과 안정성을 위해 Named Parameter 사용)
            sql = """
                INSERT INTO TB_PRECEDENT (
                    PREC_ID, CASE_NO, COURT, JUDGE_DATE, CASE_TYPE, 
                    TITLE, ONE_LINE, JUDGMENT, AI_SUMMARY, KEYWORD_CSV, CREATED_AT
                ) VALUES (
                    SEQ_PRECEDENT.NEXTVAL, :case_no, :court, TO_DATE(:judge_date, 'YYYYMMDD'), :case_type, 
                    :title, :one_line, :judgment, :ai_summary, :keyword_csv, SYSDATE
                )
            """

            # 딕셔너리 형태로 파라미터 전달 (매핑 오류 방지)
            params = {
                "case_no": case_no,
                "court": case['originInfo']['court'],
                "judge_date": case['originInfo']['date'],
                "case_type": case['header']['category'],
                "title": case['display']['title'],
                "one_line": case['display']['oneLine'],
                "judgment": case['header']['result'],
                "ai_summary": ai_summary_json,  # CLOB 컬럼에 바인딩
                "keyword_csv": keyword_csv
            }

            cursor.execute(sql, params)
            success_count += 1
            print(f"📥 저장 완료 ({success_count}): {case['display']['title']}")

        # 4. 트랜잭션 커밋
        connection.commit()
        print(f"\n✨ 작업 완료! (성공: {success_count}, 건너뜀: {skip_count})")

    except oracledb.Error as e:
        print(f"❌ Oracle 에러: {e}")
        if connection:
            connection.rollback()
    finally:
        if connection:
            connection.close()


if __name__ == "__main__":
    insert_to_oracle()