# 📰 News Recommend Backend

뉴스 이슈 추천 웹사이트의 백엔드 서버입니다.  
사용자의 관심사를 기반으로 뉴스 이슈를 선별하고 추천해주는 기능을 제공합니다.

## 🚀 기술 스택

- **Java 21**
- **Spring Boot**
- **JPAL**
- **Postgresql**
- **Gradle**
- **JWT + OAuth2 인증**
- **AWS Lightsail 배포**
- **Jenkins CI/CD**
- **HTTPS + Let's Encrypt**

## 📁 주요 기능

- 회원가입 및 로그인 (JWT 기반 인증)
- 카테고리 별 뉴스 이슈 수집 및 추천
-  이슈 검색
- 마이페이지 및 사용자 설정 관리
- 북마크 

## 🛠️ 개발 환경 설정

1. Java 21 설치
2. `application.yml` 또는 `application.properties` 설정  
   (DB 정보, JWT 시크릿, OAuth 설정 등)
3. Postgresql 실행 및 초기 테이블 생성
4. `./gradlew bootRun`으로 서버 실행

## 📦 배포

- AWS Lightsail Nginx + Spring Boot 운영
- HTTPS 인증서: Certbot + Let's Encrypt
- CI/CD: Jenkins로 자동 배포

## 👥 팀 소개

- **Backend**: 공해성, 이현경, 허소영, 박건상
- **Frontend**: [news-recommend frontend](https://github.com/news-recommend/frontend)

---

> 이 프로젝트는 이슈 별 뉴스를 제공하기 위해 개발되었습니다.
> Notion: [Notion](https://deluxe-bougon-906.notion.site/1e46238d0c1e801ba5dac455e5bd4a69?pvs=74)
> Figma: [Figma 주소](https://www.figma.com/design/iPoaggg7seTt5STcylTnPl/%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8?node-id=0-1&t=MwaMTJlK2cO697Mh-1)
> 배포 URL: [배포 주소](https://www.figma.com/design/iPoaggg7seTt5STcylTnPl/%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8?node-id=0-1&t=MwaMTJlK2cO697Mh-1)

