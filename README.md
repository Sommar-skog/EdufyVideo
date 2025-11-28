# EdufyVideo
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)  
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)

## 🎬 Overview
EdufyVideo manages video clips and video playlists within the Edufy platform.  
The service can retrieve videos, create new content, and operates as part of a larger microservice ecosystem communicating via Docker Compose.  
Other related services are linked below.

---

## 🧩 Related projects

### Organization
- [EdufyProjects](https://github.com/EudfyProjects)

### Connections
- [Edufy-infra](https://github.com/EudfyProjects/Edufy-infra) – Docker-compose + init.db
- [EdufyEurekaServer](https://github.com/Sommar-skog/EdufyEurekaServer) – Service discovery
- [Gateway](https://github.com/SaraSnail/EdufyGateway) – Entry point for all requests
- [EdufyUser](https://github.com/Jamtgard/EdufyUser) – User handling service
- [EdufyKeycloak](https://github.com/Sommar-skog/EdufyKeycloak) – Keycloak pipeline for auth

### Media connections
- [EdufyCreator](https://github.com/Sommar-skog/EdufyCreator) – Creators
- [EdufyGenre](https://github.com/a-westerberg/EdufyGenre) – Genres
- [EdufyThumb](https://github.com/a-westerberg/EdufyThumb) – Thumbs up/down records
- [EdufyUtility](https://github.com/a-westerberg/EdufyUtility) – Placeholder for algorithms

### Other Media services
- [EdufyMusic](https://github.com/Jamtgard/EdufyMusic)
- [EdufyPod](https://github.com/SaraSnail/EdufyPod)

---

## 🚀 Tech Stack

- **Language:** Java 21
- **Build Tool:** Maven
- **Framework:** Spring Boot 3.5.7
    - Spring Web
    - Spring Data JPA
    - Spring Security
    - Eureka Client
    - Spring Cloud Loadbalancer
- **Databases:**
    - MySQL 8.0 (Docker)
    - H2 (Development)
- **Security:**
    - OAuth2 Resource Server
- **Testing:**
    - Mockito
    - JUnit 5


---

## 🏁 Getting Started

### Prerequisites
- Java 21
- Maven
- Docker
- Postman
- Keycloak

---

### 🔌 Ports

#### Connections
- **Eureka:** `8761`
- **Gateway:** `4545`
- **MySQL:** `3307`
- **User:** `8686`
- **Keycloak:** `8080`

#### Media connections
- **Creator:** `8787`
- **Genre:** `8585`
- **Thumb:** `8484`
- **Utility:** `8888`

#### Media services
- **Video:** `8383`
- **Music:** `8181`
- **Pod:** `8282`

---

## 🔒 Authentication & Roles

Edufy Video uses **OAuth2 + Keycloak** for authentication and authorization.

### User Roles
- **edufy_realm_admin** – Admin access across all microservices
- **video_admin** – Create and manage video content
- **video_user** – Retrieve videos and playlists
- **microservice_access** – Internal service-to-service communication

>_Note: These are not "real" users/admin. They are placeholders for production and used under development._


| Role                | Username            | Password |
|---------------------|---------------------|----------|
| video_admin         | video_admin         | admin    |
| edufy_realm_admin   | edufy_realm_admin   | admin    |
| video_user          | video_user          | video    |
| microservice_access | –                   | –        |

> Note: Unauthenticated requests will receive a `401 Unauthorized` response.

> `microservice_access` is a role that clients uses between each other to authorize access


---

## 📚 API Endpoints

### Admin – Roles `video_admin` & `edufy_realm_admin`
* **POST** `/video/videoclip` – Create a new video
* **POST** `/video/playlis` – Create a new playlist
* **POST** `/playlist/{playlistid}/videoclips/add` - Add videoclip to playlist


---

### Client – Role `microservice_access`
* **GET** `/video/user-history/{userId}` – Get user's watch history

---

### Common – Any authenticated user
* **GET** `/video/videoclip/{id}}` – Get video by ID
* **GET** `/video/videoplaylist/{id}` – Get playlist by ID
* **GET** `/video/video-all` – List all videos
* **GET** `/video/playlist-all` – List all playlists
* **GET** `/video/videography-creator/{creatorId}` – All videos made by a creator


---

### User – Role `video_user`
* **GET** `/video/video-title` – Search videos by title
* **GET** `/video/playlist-title` – Search playlists by title
* **GET** `/video/videos-genre/{genreId}` – Get videos by genre
* **GET** `/video/play/{videoId}` – "Play" video, returns video URL

---

## 🐳 Docker
- This service runs via `docker-compose.yml` found in **Edufy-infra**.
- Docker network: `edufy-network`.

---

## 🛢️ MySQL Database

| Name               | User | Pass | Database |
|--------------------|------|------|----------|
| edufy_mysql        | assa | assa | main     |
| edufy_video_db     | assa | assa | video    |

- **Version:** 8.0
- **SQL files:**
    - Global init file is located in Edufy-infra
    - Video service uses `data.sql` (dev) + `import.sql` (prod)
- **Default port:** `3306` → mapped as `3307:3306`

- **Connection Example :**
  ```
    spring.datasource.url=jdbc:mysql://edufy-mysql:3306/edufy_pod_db
    spring.datasource.username=assa
    spring.datasource.password=assa
    spring.jpa.hibernate.ddl-auto=update
  ```

> _README made by [Sommar-skog](https://github.com/Sommar-skog)_



