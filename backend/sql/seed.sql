-- Seed sample data for main page

-- CASES sample (last 10 days)
INSERT INTO cases (case_type, title, status, created_at) VALUES
('부동산', '임대차 보증금 반환', 'OPEN', SYSTIMESTAMP - INTERVAL '0' DAY);
INSERT INTO cases (case_type, title, status, created_at) VALUES
('형사', '명예훼손 고소', 'OPEN', SYSTIMESTAMP - INTERVAL '1' DAY);
INSERT INTO cases (case_type, title, status, created_at) VALUES
('손해배상', '교통사고 손해배상', 'OPEN', SYSTIMESTAMP - INTERVAL '2' DAY);
INSERT INTO cases (case_type, title, status, created_at) VALUES
('가사', '양육권 분쟁', 'OPEN', SYSTIMESTAMP - INTERVAL '3' DAY);
INSERT INTO cases (case_type, title, status, created_at) VALUES
('노동', '부당해고 구제', 'OPEN', SYSTIMESTAMP - INTERVAL '4' DAY);
INSERT INTO cases (case_type, title, status, created_at) VALUES
('부동산', '전세 사기 피해', 'OPEN', SYSTIMESTAMP - INTERVAL '5' DAY);
INSERT INTO cases (case_type, title, status, created_at) VALUES
('형사', '사기/횡령', 'OPEN', SYSTIMESTAMP - INTERVAL '6' DAY);
INSERT INTO cases (case_type, title, status, created_at) VALUES
('손해배상', '의료 과실 손해배상', 'OPEN', SYSTIMESTAMP - INTERVAL '7' DAY);

-- PRECEDENTS sample
INSERT INTO precedents (category, title, summary, keywords, decided_at)
VALUES (
  '부동산',
  '임대차계약 해지 및 보증금 반환 관련 판례',
  '임대차계약 해지 요건과 임차인의 보증금 반환 청구 범위에 관한 판단을 정리한 판례 요약입니다.',
  '임대차,보증금,계약해지,부동산',
  DATE ''2024-11-12''
);

INSERT INTO precedents (category, title, summary, keywords, decided_at)
VALUES (
  '손해배상',
  '교통사고 과실비율 및 위자료 산정 기준 판례',
  '교통사고 손해배상에서 과실비율과 치료비, 위자료 산정 요소를 종합적으로 고려한 판례 요약입니다.',
  '교통사고,손해배상,과실비율,위자료',
  DATE ''2024-09-03''
);

INSERT INTO precedents (category, title, summary, keywords, decided_at)
VALUES (
  '형사',
  '명예훼손에서 사실 적시와 의견 표현의 구별 판례',
  '사실 적시와 의견 표명의 경계를 판단하고 위법성 조각 사유를 검토한 판례 요약입니다.',
  '명예훼손,사실적시,의견표현,위법성조각',
  DATE ''2023-06-18''
);

COMMIT;
