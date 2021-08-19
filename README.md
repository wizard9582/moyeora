## 프로젝트명 : 모여라 MOYEORA   

# 목차   
1. [프로젝트 소개](#프로젝트-소개)   
2. [프로젝트 명세](#프로젝트-명세)    
    - [배포 환경](#배포-환경)   
    - [개발 환경](#개발-환경)   
    - [Design Resources](#Design-Resources)   
    - [핵심라이브러리](#핵심라이브러리)   

# 프로젝트 소개   
MOYEORA 프로젝트는 Makes You Re-active 라는 슬로건을 가진, 코로나로 인한 비대면 시대에 필요한 영상회의 및 게임 웹 사이트입니다.   
webRTC kurento를 이용한 영상회의와 이를 응용한 마피아 게임을 사람들과 즐길 수 있습니다.   
   
   


# 프로젝트 명세   

### 팀원 역할   
노진혁 - frontend 게임로직 외 기능, 디자인 구현   
박상욱 - frontend Websocket, 게임로직 구현   
이재상 - backend 게임로직 외 API 구현, AWS 배포   
이진영 - backend 게임로직 API, frontend 디자인 구현   
최성훈 - backend 게임로직 외 API 구현    

### 배포 환경   
URL : [MOYEORA](https://i5a306.p.ssafy.io/) - [https://i5a306.p.ssafy.io/]   
배포 여부 : O   
접속 가능 : 접속 가능   
HTTPS 적용 : O   




### 개발 환경   
   
Front-end   
Framework : Vue.js   
지원 환경 : Web   
담당자 : 노진혁, 박상욱   



Back-end   
Framework : Spring boot   
Database : MySQL   
담당자 : 이재상, 이진영, 최성훈   



Design   
Framework : Element Plus   
Design Tool 사용 : X   
담당자 : 노진혁, 이진영   



### Design Resources   
외부 템플릿 또는 에셋   
[Anton Vlasov - Our Summer] - ucc 배경음악으로 사용   
-그 외 무료 이미지(PowerPoint 기본 아이콘, [Flaticon](https://www.flaticon.com/)의 무료 이미지), 사운드 소스, 폰트(배달의 민족 주아체) 사용   





자체 제작 산출물 (필요시 이미지 또는 설명 첨부)   
LOGO   
web site 화면에서 보이는 메인 로고, favicon logo   
![img-logo](/uploads/e78f05acbe3b97c833352e97afc30013/img-logo.png)
![favicon-32x32](/uploads/c0de2f6a05ff94bf44597f194c379f15/favicon-32x32.png)   

CardView    
회의방 목록에서 보이는 이미지    
![img-mafia](/uploads/fb25113034f0bfe73ce5593c65c94e06/img-mafia.png)
![img-video](/uploads/e83b5668c6b411252cf47b569a569a55/img-video.png)    
마피아 게임 방 관련 이미지 (투표카운트, 사망상태, 배경이미지, 룰 설명 이미지)    
![favicon-white](/uploads/61d602921e530d1b16fe197f1419eaeb/favicon-white.png)
![img-dead](/uploads/233e9351a3a0b6434b67e888ac031690/img-dead.png)   
![img-mafia-rule](/uploads/3a030497fa17576987d3b876cf275678/img-mafia-rule.png)    
    


### 핵심 라이브러리    
Kurento,WebStomp,JWT,JPA 등 기본 라이브러리 사용    
