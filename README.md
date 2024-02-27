### 0. 🍚 실시간 캠퍼스 식당 정보 서비스 - "밥zip"
점심시간만 되면 평소 한가하던 학식당도, 늦은 오후에 가도 여유롭게 사 마시던 카페도 웨이팅 한 경험을 한 적 있으신가요?

또는 내가 가고 싶던 식당에 갔지만 갑자기 당일 휴업으로 발걸음을 돌린 적이 있지 않으신가요?

저희도 하루에 학식당, 학관 카페 , 도서관 카페에서 많은 웨이팅으로 허탕을 친 경험으로 "밥zip" 서비스를 기획했습니다. 

<h4>학교 내 식당 뿐만 아니라 대학가 근처 음식점의 실시간 혼잡도 안내 기능을 비롯하여 리뷰, 식당 최신 업데이트 정보, 메뉴 추천 등의 기능을 포함한 서비스입니다.</h4>

<br>
<img width=700 src="https://github.com/BapZip/Backend/assets/101572960/b1fa599a-09ac-4f90-94df-854793c4d3f1">

<br>
<br>

### 1. 💻 Developer
</br>
<div align="center">
<table>
  <tr>
    <td align="center"><a href="https://github.com/RhoSeungA"><br /><p><b>노승아</b></p></a><small>🐸 Back-End Developer</small></td>
    <td align="center"><a href=""><br /><p><b>우성원</b></p></a><small>🐻 Back-End Developer</small></td>
    <td align="center"><a href=""><br /><p><b>박성훈</b></p></a><small>🐶 Back-End Developer</small></td>
    <td align="center"><a href=""><br /><p><b>최현지</b></p></a><small>🐰 Back-End Developer</small></td>
  </tr> 
</table>
</div>
<br>


### 2. 🔨 개발 환경
* Java 17
* Gradle
* Spring Boot (v3.1.7)
* Github actions
* AWS EC2
* AWS S3
* AWS ElasticBeanstalk
* AWS RDS
* MySQL
* Swagger
<br><br>

### 3. ☁️ 아키텍쳐
<br>
<img width=800 src="https://github.com/BapZip/Backend/assets/101572960/37714a40-c3f0-4f5b-a5b6-ba4e22540b5a">

<br>

### 4. 🌲 프로젝트 구조
<br>

```bash
├── main
     ├── java
         ├── com
             ├── example
                 ├── BapZip
                     ├── apiPayload
                         ├── code
                         ├── exception
                             ├── handler
                     ├── config
                     ├── domain
                         ├── chat
                         ├── common
                         ├── enums
                         ├── mapping
                     ├── repository
                     ├── security
                     ├── service
                         ├── ChatService
                         ├── CongestionService
                         ├── CouponService
                         ├── MailService
                         ├── MypageService
                         ├── PointService
                         ├── ReviewService
                         ├── S3Service
                         ├── SchoolService
                         ├── StoreService
                         ├── UserService
                     ├── web
                         ├── controller
                         ├── dto
                     ├── BapZipApplication.java
     ├── resources
         ├── application.yml

```

<br/>


