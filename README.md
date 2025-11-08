![header](https://capsule-render.vercel.app/api?type=rect&color=auto&height=250&section=header&text=OneJo%20Note&fontSize=70)

> Vue.js + Spring Boot 기반 풀스택 노트 애플리케이션 (팀 스터디)

<br>

## :open_book: 목차
- [프로젝트 소개](#프로젝트-소개)
- [👤 저의 기여](#-저의-기여)
- [🛠️ 기술 스택](#-기술-스택)
- [📁 프로젝트 구조](#-프로젝트-구조)

<br>

<a id="프로젝트-소개"></a>
## 📋 프로젝트 소개

**원조 노트(onejo\_note)**는 Vue.js와 Spring Boot를 사용한 풀스택 웹 애플리케이션 개발 **팀 스터디** 프로젝트입니다.

프론트엔드는 Vue.js 3, 백엔드는 Spring Boot(Java 21)로 구성되어 있으며, 사용자 인증을 기반으로 한 개인 노트를 작성하고 관리하는 CRUD 기능을 제공합니다.

### 🚀 주요 기능
- **인증:** 회원가입 및 JWT 토큰을 이용한 로그인
- **노트 CRUD:** 개인 노트 생성, 조회, 수정, 삭제

<br>

<a id="-저의-기여"></a>
## 👤 저의 기여

저는 이 풀스택 스터디 프로젝트에서 **백엔드 개발**을 담당했습니다.

* **노트 CRUD API 구현:** `NoteController`와 `NoteService`를 중심으로, 사용자가 노트를 **작성, 조회, 수정, 삭제**하는 데 필요한 **전반적인 REST API를 구현**했습니다.
* **데이터베이스 연동:** Spring Data JPA와 `NoteRepository`를 사용하여 API 로직과 MySQL 데이터베이스를 연동했습니다.

<br>

<a id="-기술-스택"></a>
## 🛠️ 기술 스택

| 구분 | 기술 |
|------|------|
| **Frontend** | ![Vue.js](https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vue.js&logoColor=4FC08D) ![Vite](https://img.shields.io/badge/Vite-B73BFE?style=for-the-badge&logo=vite&logoColor=FFD62E) ![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white) |
| **Backend** | ![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white) |
| **Database** | ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white) (Spring Data JPA) |
| **Build Tool** | ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white) (Backend) |

<br>

<a id="-프로젝트-구조"></a>
## 📁 프로젝트 구조
```bash
onejo_note/
├── note-app/           # Frontend (Vue.js)
│   ├── src/
│   │   ├── views/
│   │   │   ├── HomeView.vue
│   │   │   ├── LoginView.vue
│   │   │   ├── RegisterView.vue
│   │   │   └── NoteView.vue
│   │   ├── stores/     # (auth.ts)
│   │   ├── services/   # (apiService.ts)
│   │   ├── router/
│   │   └── App.vue
│   └── package.json
└── note-lication/      # Backend (Spring Boot)
    ├── src/main/java/com/project/notelication/
    │   ├── controller/
    │   │   ├── AuthController.java   # 인증 API
    │   │   └── NoteController.java   # 노트 CRUD API
    │   ├── service/
    │   │   ├── NoteService.java      # 노트 비즈니스 로직
    │   │   ├── UserService.java
    │   │   └── UserDetailsServiceImpl.java
    │   ├── repository/
    │   │   ├── NoteRepository.java   # JPA 리포지토리
    │   │   └── UserRepository.java
    │   ├── model/                  # JPA 엔티티
    │   │   ├── Note.java
    │   │   └── User.java
    │   ├── config/                 # SecurityConfig
    │   └── filter/                 # JwtRequestFilter
    └── build.gradle                # Gradle 빌드 스크립트
