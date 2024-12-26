# 🌱 JPA FETCH JOIN 및 QueryDSL 실습 프로젝트

> **JPA FETCH JOIN 및 QueryDSL을 활용한 실습 프로젝트**  
> N+1 문제를 해결하기 위한 JPA FETCH JOIN과 QueryDSL 기반 FETCH JOIN 구현을 학습합니다.  
> 공유되는 코드는 한국폴리텍대학 서울강서캠퍼스 빅데이터과 수업에서 사용된 코드입니다.

---

### 📚 **작성자**
- **한국폴리텍대학 서울강서캠퍼스 빅데이터과**  
- **이협건 교수**  
- ✉️ [hglee67@kopo.ac.kr](mailto:hglee67@kopo.ac.kr)  
- 🔗 [빅데이터학과 입학 상담 오픈채팅방](https://open.kakao.com/o/gEd0JIad)

---

## 🚀 주요 실습 내용

1. **N+1 문제를 해결하기 위한 JPA FETCH JOIN 구현**
2. **QueryDSL을 활용한 JPA FETCH JOIN 구현**

---

## 🛠️ 주요 적용 프레임워크

- **Spring Boot Frameworks**
- **Spring Data JPA**
- **QueryDSL**

---

## 📩 문의 및 입학 상담

- 📧 **이메일**: [hglee67@kopo.ac.kr](mailto:hglee67@kopo.ac.kr)  
- 💬 **입학 상담 오픈채팅방**: [바로가기](https://open.kakao.com/o/gEd0JIad)

---

## 💡 **우리 학과 소개**
- 한국폴리텍대학 서울강서캠퍼스 빅데이터과는 **클라우드 컴퓨팅, 인공지능, 빅데이터 기술**을 활용하여 소프트웨어 개발자를 양성하는 학과입니다.  
- 학과에 대한 더 자세한 정보는 [학과 홈페이지](https://www.kopo.ac.kr/kangseo/content.do?menu=1547)를 참고하세요.

---

## 📦 **설치 및 실행 방법**

### 1. 레포지토리 클론
- 아래 명령어를 실행하여 레포지토리를 클론합니다.

```bash
git clone https://github.com/Hyeopgeon-Lee/SpringJPAFetchJoin
cd SpringJPAFetchJoin
```

2. 데이터베이스 설정
application.yml 또는 application.properties 파일에서 데이터베이스 연결 정보를 설정합니다.

```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/your_database
    username: your_username
    password: your_password
```

### 3. 의존성 설치 및 빌드
- Gradle을 사용하여 의존성을 설치하고 애플리케이션을 빌드합니다.

```bash
./gradlew build
```

### 4. 애플리케이션 실행
- 아래 명령어를 실행하여 애플리케이션을 시작합니다.

```bash
./gradlew bootRun
```


