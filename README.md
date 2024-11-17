# token-service
[서비스에서 권한 확인을 위한 JWT를 관리하는 API 입니다]


## 📂 디렉토리 구조
```
.
├── src/
│   ├── main/
│   │   ├── java/                 
│   │   │   └── com/
│   │   │       └── jle_official/
│   │   │           └── token_service/  # 프로젝트 패키지
│   │   │               ├── member/   
│   │   │               ├── token/      
│   │   │               └── ProjectApplication.java  # 메인 애플리케이션 클래스
│   │   └── resources/
│   │       ├── application.yml  # Spring Boot 환경 설정 파일
│   │       └── application-*.yml  # 다른 프로파일 설정 파일 (dev, prod 등)
│   │ 
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── project/  # 테스트 클래스 디렉토리
│                       ├── controller/   
│                       ├── service/     
│                       ├── repository/ 
│                       └── ProjectApplicationTests.java
├── build.gradle           
├── gradlew               
├── gradlew.bat              
├── settings.gradle           
├── .gitignore                
├── README.md                  
└── Dockerfile                 
```

## 🔄 주요 기능
1. 토큰 발급

2. 토큰 재발급

3. 토큰 저장 및 삭제

## ⚒️ 기술 스택
