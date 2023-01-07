# API List 
### 기능에 의해 저장된 정보를 조회, 등록, 수정 삭제하는 api 구현(Restful API)
#### 시간대별 가입자수 / 매출액/ 결제금액 


시간대별 (/api/hourly)
- 가입자수 (/new-user)
- 이탈 유저  (/churn-user)
- 매출  (/pay-amount)
- 판매금액  (/cost)
- 비용 (/sales-amount)

일별 (/api/daily)
- 가입자수 (/new-user)
- 이탈 유저  (/churn-user)
- 매출  (/pay-amount)
- 판매금액  (/cost)
- 비용 (/sales-amount)

누적 집계 (/api/summary)
[집계데이터 : 가입자, 이탈자, 매출, 비용, 판매금액의 평균, 합계, 최대, 최소]
- 시간별
  - 시간대별 집게데이터 (평균/ 합계/ )
  - 특정 시간대의 집계데이터 총 
    
- 일별 
    - 일별 집게데이터 (평균/ 합계/ )
    - 특정 일의 집계데이터 총
