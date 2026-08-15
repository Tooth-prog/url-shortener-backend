# Shortify — Full-Stack URL Shortener & Analytics Platform 🚀

A production-grade URL shortening service and interactive analytics dashboard built with **Spring Boot 3**, **Spring Security**, **JWT & Refresh Tokens**, **PostgreSQL**, **Redis Caching**, and **React + Vite**.

![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen.svg)
![React](https://img.shields.io/badge/React-18-61dafb.svg)
![Docker](https://img.shields.io/badge/Docker-Supported-2496ed.svg)

---

## ✨ Features

* ⚡ **High-Performance Redirects**: Sub-millisecond URL lookups using Redis in-memory caching with dynamic TTL link expiration.
* 📊 **Asynchronous Click Analytics**: Non-blocking click tracking using Spring `@Async` thread pools to preserve instant redirect speeds.
* 🔐 **Stateless JWT Authentication**: Secure user registration & login with short-lived access tokens and automated background refresh token rotation.
* 👥 **Role-Based Access Control**: Personal link management dashboard for users + restricted Admin Control Panel for system user management (`ROLE_ADMIN`).
* 🏷️ **Custom Aliases & Expiration**: Create custom short codes and flexible expiration durations (1 day to 1 year).
* 🎨 **Interactive Glassmorphic Dashboard**: Modern React UI with analytics summary metrics, 1-click copy/open actions, link inspector modals, and status badges.
* 🐳 **Dockerized Full-Stack Deployment**: Multi-stage Docker container builds for both backend API and frontend static asset serving via Nginx.

---

## 🏗️ System Architecture

```text
               ┌────────────────────────┐
               │    React + Vite UI     │
               │ (Port 5173 / Nginx)    │
               └───────────┬────────────┘
                           │ HTTP REST / JWT Bearer
                           ▼
               ┌────────────────────────┐
               │  Spring Boot Backend   │
               │      (Port 8080)       │
               └────┬──────────────┬────┘
                    │              │
       Cache Lookups│              │ Persistence
                    ▼              ▼
           ┌────────────────┐ ┌────────────────┐
           │ Redis 7 Cache  │ │ PostgreSQL 15  │
           │  (Port 6379)   │ │  (Port 5432)   │
           └────────────────┘ └────────────────┘
```

---

## 🛠️ Tech Stack

### Backend
- **Java 21 / Spring Boot 3**
- **Spring Security** (JWT Authentication & Role-based Filter Chain)
- **Hibernate / JPA & PostgreSQL**
- **Spring Data Redis** (Caching & TTL management)
- **Swagger / OpenAPI 3** (`/swagger-ui.html`)
- **Lombok & Jakarta Validation**

### Frontend
- **React 18 & Vite**
- **Axios** (Configured with request/response interceptors for 401 token auto-refresh)
- **React Router DOM v6** (Protected & Admin-only routes)
- **Lucide React Icons**
- **Vanilla Glassmorphism CSS**

---

## 📁 Project Structure

```text
Urlshortener/
├── src/main/java/com/piyush/Urlshortener/
│   ├── config/            # Security & CORS configuration
│   ├── controller/        # Auth, URL, User, and Admin REST endpoints
│   ├── dto/               # Request/Response data transfer objects
│   ├── entity/            # JPA Entities (User, Url, RefreshToken, Role)
│   ├── exception/         # Global Exception Handler & Custom exceptions
│   ├── repository/       # Data Access repositories
│   ├── scheduler/       # Cron job for expired link cleanup
│   ├── security/          # JWT authentication filters
│   └── service/           # Business logic & Redis caching
├── frontend/              # React + Vite UI App
│   ├── src/
│   │   ├── components/    # Navbar & UI elements
│   │   ├── context/       # AuthContext for session management
│   │   ├── pages/         # Landing, Login, Register, Dashboard, Admin pages
│   │   ├── routes/        # Public & Protected routes
│   │   └── services/      # Axios API client & endpoints
│   ├── Dockerfile         # Multi-stage Nginx build
│   └── nginx.conf         # SPA routing & API reverse proxy
├── docker-compose.yml     # Full-stack Docker service orchestration
└── Dockerfile             # Backend Spring Boot container definition
```

---

## 🚀 Getting Started

### Prerequisites
- [Docker](https://www.docker.com/) & Docker Compose installed, OR
- Local environment with Java 21, Node.js 20+, PostgreSQL, and Redis.

---

### Environment Setup

1. Copy `.env.example` to `.env` in the project root:
   ```env
   POSTGRES_DB=your_db_name
   POSTGRES_USER=your_db_user
   POSTGRES_PASSWORD=your_db_password
   SPRING_PROFILE=docker
   ```

2. Copy `frontend/.env.example` to `frontend/.env`:
   ```env
   VITE_API_BASE_URL=http://localhost:8080
   ```

---

### Running with Docker Compose (Recommended)

1. Build backend package & start containers:
   ```bash
   ./mvnw package -DskipTests
   docker-compose up --build
   ```

2. Access services:
   - **Frontend App**: `http://localhost:5173`
   - **Backend API**: `http://localhost:8080`
   - **Swagger Docs**: `http://localhost:8080/swagger-ui.html`

---

### Running Locally without Docker

1. **Start PostgreSQL & Redis** services locally on default ports (`5432` and `6379`).
2. **Run Backend**:
   ```bash
   ./mvnw spring-boot:run
   ```
3. **Run Frontend**:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

---

## 📑 API Endpoints

| HTTP Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Public | Register new user account (`username`, `email`, `password`) |
| `POST` | `/api/auth/login` | Public | Login & receive JWT access + refresh tokens |
| `POST` | `/api/auth/refresh` | Public | Exchange refresh token for new access token |
| `POST` | `/api/v1/shorten` | Public / Auth | Create short URL with optional custom code & expiry |
| `GET` | `/api/v1/{shortCode}` | Public | 302 Redirect & async increment click count |
| `GET` | `/api/v1/analytics/{shortCode}` | Public / Auth | Retrieve click statistics & link metadata |
| `GET` | `/api/user/urls` | Authenticated | Fetch current user's link registry |
| `GET` | `/api/admin/users` | Admin | Retrieve system user registry (`ROLE_ADMIN`) |

---

## 🔒 Security Best Practices

- All database passwords, tokens, and secrets are configured via environment variables and excluded from version control (`.gitignore`).
- Stateless JWT authentication with short-lived access tokens and token refresh rotation.
- CORS policies enabled securely to support cross-origin requests.

---

## 📄 License

Distributed under the MIT License.
