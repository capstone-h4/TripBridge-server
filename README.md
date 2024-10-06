<div align=center>

# 사용자 맞춤형 웹사이트, Trip Bridge
</div>

### 프로젝트 개요
**당신만의 여행을 즐길 수 있는, [Trip Bridge]** <br>
같은 장소를 여행 하더라도 사람들은 각각의 목표와 계획에 의해 다른 여행을 합니다. 하지만 여행 계획 정보를 얻고자 검색을 하면 대부분
유명한 관광지에 대한 정보들만 가득합니다. 이에 사용자가 보다 다양한 선택지를 가지고 여행을 할 수 있도록 서비스를 기획을 하였습니다.

<br>

### 프로젝트 진행 기간
2024.02.12 ~ 2024.05.31 (기획 및 디자인, 개발) <br>
2024.09.01 ~ 2024.09.20 (추가 기능 개발 및 리팩토링)

<br>

### 팀원
#### Front-End
- 백주희(팀장) : 프론트 구현(React), 프론트 배포
- 황서현 : UI 디자인, 프론트 구현(React)

#### Back-End
- 박현지 : DB 설계 및 구축, Rest API 개발, 서버 구축, 서비스 배포
- 윤현수 : DB 설계 및 구축, Rest API 개발, AWS S3 관리

<br><br>

### 백엔드 기술 스택
[![stackticon](https://firebasestorage.googleapis.com/v0/b/stackticon-81399.appspot.com/o/images%2F1728227673059?alt=media&token=ddf706fc-d675-4ee5-9ded-347d6ae177e3)](https://github.com/msdio/stackticon)
- 언어 및 프레임워크<br>
  Java 17, Spring Boot 3.2.4, Spring Data JPA, Spring Security


- 데이터베이스<br>
  MySQL (AWS RDS)


- 서버 및 배포<br>
  AWS EC2 (서버 호스팅), AWS S3 (파일 저장), AWS Route 53 (도메인 관리)


- 버전 관리 및 기타 도구<br>
  Git (GitHub를 통해 버전 관리), Postman (API 테스트), KaKao API (카카오 맵), 공공데이터 포털 Tour API, OpenAI API(ChatGPT 서비스)


<br><br>



### ERD
<img width="1119" alt="trip_erd" src="https://github.com/user-attachments/assets/ee3562f4-b884-451a-83d4-824202992907">
<br><br>


### 적용 기술 정리

#### 공공 데이터 이용

#### Kakao Maps API

#### OpenAI 서비스





<br><br>

### 사용자 플로우

[//]: # (본 서비스는 여행자들을 위해 각각의 취향을 반영)

[//]: # (공공 데이터 포털의 공공 데이터를 이용하여 방대한 양의 장소 데이터를 제공하고,)

[//]: # (사용자는 취향에 맞춰 이 장소들을 조합하여 여행 계획을 세울 수 있습니다.)

[//]: # (여행지를 저장하고 지도&#40;Kakao Maps API&#41;에서 여행지의 자세한 위치, 여행 추천 동선 등을 확인할 수 있으며,)

[//]: # (챗봇 인터페이스에 기반한 OpenAI 서비스&#40;ChatGPT&#41;를 이용하여 여행지에 관한 상세 정보와 주변 관광지 또한 추천 받을 수 있습니다. <br>)



<br><br>


