<div align=center>

# 사용자 맞춤형 웹서비스, Trip Bridge

![Image](https://github.com/user-attachments/assets/b7aba67e-eaa5-486d-83c7-e5865a429406)
</div>

- 배포 URL : https://www.tripbridge.co.kr/
- 테스트 ID : test@test.com
- 테스트 PW : 12341234

<br>

## 프로젝트 소개
**당신만의 여행을 즐길 수 있는, [Trip Bridge]** <br>
같은 장소를 여행 하더라도 사람들은 각각의 목표와 계획에 의해 다른 여행을 합니다. 하지만 여행 계획 정보를 얻고자 검색을 하면 대부분
유명한 관광지에 대한 정보들만 가득합니다. 이에 사용자가 보다 다양한 선택지를 가지고 여행을 할 수 있도록 서비스를 기획하였습니다.

<br>



## 프로젝트 진행 기간
2024.02.12 ~ 2024.05.31 (기획 및 디자인, 개발) <br>
2024.09.01 ~ 2024.09.20 (추가 기능 개발 및 리팩토링)

<br>

## 팀원 구성 & 역할 분담
### Front-End
- 백주희(팀장) : 프론트 구현(React), 프론트 배포
- 황서현 : UI 디자인, 프론트 구현(React)

### Back-End
- 박현지 : DB 설계 및 구축, Rest API 개발, 인프라 설계 및 구축
- 윤현수 : DB 설계 및 구축, Rest API 개발, AWS S3 관리

<br><br>

## 주요 기능 & 동작 시연
<details>
<summary>여행지 추천</summary>
  
<br>
  
![Image](https://github.com/user-attachments/assets/4515fbdb-6dbf-48bb-833a-14a59154bb57)

- 공공데이터 포털의 한국관광공사 국문 관광정보 서비스 데이터를 이용하여 다양한 여행지를 제공
- 원하는 지역과 관광타입을 선택하면 실시간으로 공공데이터 API를 호출하여 다양한 여행지를 추천
</details>

<br>

<details>
  <summary>스크랩 관리 & 동선 추천</summary> 

  <br>

![Image](https://github.com/user-attachments/assets/51923310-98ed-4434-a365-b1c179b0b1d8)

- 추천 여행지와 직접 검색을 통한 장소 스크랩
- 스크랩 목록에서 자유롭게 원하는 장소들을 선택하여 최적 동선 추천받기
- 추천 받은 동선은 동선 관리에 저장하여 관리
</details>

<br>

<details>
  <summary>챗봇</summary>

  <br>

![Image](https://github.com/user-attachments/assets/2a8a6560-5db3-4d8d-8f30-b24760920106)

- 기본적으로 제공되는 장소 정보나 추천 동선 외에 추가적인 정보 제공
- 챗봇 인터페이스를 바탕으로 여행 기간에 따른 일정 추천, 주변 관광지 추천, 이동 수단과 예상 비용 안내 제공
  

</details>

<br>


## 서비스 아키텍처
<img width="942" alt="스크린샷 2025-03-17 오후 9 43 37" src="https://github.com/user-attachments/assets/06d75d57-0575-4605-a4c5-45dcb0328165" />

<br><br>


## 백엔드 기술 스택
### BE
![Image](https://github.com/user-attachments/assets/03571a91-6dc6-4f85-968f-25046fe435b0)

### Infra
![Image](https://github.com/user-attachments/assets/b2ef712f-05e8-40bc-96f3-b0d8eaf85fbc)

- 언어 및 프레임워크<br>
  Java 17, Spring Boot 3.2.4, Spring Data JPA, Spring Security


- 데이터베이스<br>
  MySQL (AWS RDS)


- 인프라<br>
  AWS EC2 (서버 호스팅), AWS S3 (파일 저장), AWS Route 53 (도메인 관리)


- 버전 관리 및 기타 도구<br>
  Git (GitHub를 통해 버전 관리), Postman (API 테스트), KaKao API (카카오 맵), 공공데이터 포털 Tour API, OpenAI API(ChatGPT 서비스)


<br><br>



## ERD
<img width="1119" alt="trip_erd" src="https://github.com/user-attachments/assets/ee3562f4-b884-451a-83d4-824202992907">
<br><br>
<br>


## 적용 기술과 활용
### 공공 데이터
공공데이터는 정부나 공공 기관의 사업이나 연구의 운영 과정에서 수집된 다양한 데이터입니다.
공공데이터 포털은 공공기관이 생성 또는 취득하여 관리하고 있는 공공데이터를 한 곳에서 제공하는 통합 창구로,
포털에서는 모두가 쉽고 편리하게 공공데이터를 이용할 수 있도록 파일데이터, Open API, 시각화 등 다양한 방식으로 제공하고 있습니다.

본 서비스는 사용자의 취향을 반영하여 최대한 다양한 관광지를 추천하고자 공공데이터 포털의 **한국관광공사 국문 관광정보 서비스**를 이용하여 사용자에게 제공하였습니다.

### Kakao Maps API
Kakao Maps API는 웹사이트와 모바일 애플리케이션에서 지도를 이용한 서비스를 제작할 수 있도록 다양한 기능을 제공합니다.

Maps API의 내장된 기능과 TSP를 해결하기 위한 알고리즘을 사용하여 사용자에게 여행 동선 추천 기능을 구현하였습니다.
Maps API의 내장된 마커를 이용하여 지도 상에서 사용자는 장소의 위치와 명칭을 보다 편리하게 확인 가능합니다.

### OpenAI 서비스
OpenAI의 API는 OpenAI의 기계학습모델에 액세스 하는 데에 사용할 수 있는 도구입니다.
대화 전문 인공지능 챗봇 ChatGPT의 GPT-3.5 Turbo 모델을 이용하여 여행 동선과 관련된 서비스 이용중, 사용자가 추가적인 정보를 필요로 때 실시간으로 요청을 보내고 응답을 받아오도록 하였습니다.

<br>
