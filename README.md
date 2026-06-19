# Student Portal Management System

A full stack Student Portal Management System for managing academic operations with role-based authentication, dashboards, reports, soft deletes, audit logs and API documentation.

## Objective

Build a production-ready academic/student portal where administrators can manage students, courses, enrollments, marks, attendance, announcements, study materials and audit logs; faculty can manage academic operations and reports; students can view their own academic profile and resources.

## Technology Stack

### Backend

- Java 21
- Spring Boot 3.3
- Spring Security 6 with JWT bearer authentication
- Spring Data JPA and Hibernate
- MySQL 8
- Maven 3.9
- Swagger/OpenAPI 3 using springdoc
- MapStruct, Lombok and Jakarta Bean Validation
- JUnit 5, Mockito, MockMvc and H2 for testing
- Spring Boot Actuator health endpoint

### Frontend

- Angular 20 with standalone components
- Angular Material 20
- TypeScript 5.5
- Reactive Forms
- Angular Router with lazy-loaded pages
- Functional guards and interceptors
- Angular Signals
- RxJS 7.8
- SCSS
- Nginx for Docker production serving

### DevOps

- Docker and Docker Compose
- GitHub Actions CI/CD
- Docker Hub image push
- Render deploy-hook steps

## Features Implemented

- JWT login and registration
- Default seeded admin account
- Role-based access control: `ADMIN`, `FACULTY`, `STUDENT`
- Student CRUD with search, pagination and soft delete
- Course CRUD with search, pagination and soft delete
- Student-course enrollment and unenrollment
- Attendance recording, update, soft delete and defaulter report below 75%
- Marks recording, update, soft delete, automatic grade calculation and rankings
- Announcements CRUD
- Study materials CRUD with course mapping and external file URLs
- Dashboard summary, top students, low attendance and at-risk students
- Reports for rankings, attendance, course performance, department performance, pass/fail, attendance bands and monthly enrollment
- Admin-only audit logs
- Base entity auditing with created/updated metadata
- Swagger UI
- Postman collection with sample requests and responses
- Dockerized backend, frontend and MySQL
- CI/CD workflow scaffold

## Project Structure

```text
student-portal/
├── backend/                         Spring Boot API
├── frontend/                        Angular app
├── docs/                            Architecture and ER diagram
├── postman/                         Postman collection
├── .github/workflows/ci-cd.yml      CI/CD pipeline
├── docker-compose.yml               MySQL + backend + frontend
├── .env.example                     Environment template
├── .gitignore
└── README.md
```

## Default Login

```text
Username: admin
Password: Admin@123
Role: ADMIN
```

## Environment Variables

Copy `.env.example` to `.env` for Docker Compose.

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=student_portal
DB_USERNAME=portaluser
DB_PASSWORD=portalpass
JWT_SECRET=c3R1ZGVudC1wb3J0YWwtZGV2ZWxvcG1lbnQtc2VjcmV0LWtleS0zMi1wbHVz
JWT_EXPIRATION=86400000
CORS_ORIGINS=http://localhost:4200,http://localhost:80
```

`JWT_SECRET` must be a Base64-encoded HMAC secret. Replace the development fallback before production deployment.

## Run the Application

### Option A: Manual Development Run

Start MySQL and create the database:

```sql
CREATE DATABASE student_portal;
CREATE USER 'portaluser'@'%' IDENTIFIED BY 'portalpass';
GRANT ALL PRIVILEGES ON student_portal.* TO 'portaluser'@'%';
FLUSH PRIVILEGES;
```

Run backend:

```bash
cd backend
mvn spring-boot:run
```

Backend runs at:

```text
http://localhost:8080
```

Run frontend:

```bash
cd frontend
npm install --legacy-peer-deps
npm start
```

Frontend runs at:

```text
http://localhost:4200
```

### Option B: Docker Compose

```bash
cp .env.example .env
docker compose up --build -d
```

Services:

```text
MySQL:    localhost:3306
Backend:  http://localhost:8080
Frontend: http://localhost:80
```

## Swagger

Open:

```text
http://localhost:8080/swagger-ui.html
```

Steps:

1. Run `POST /api/auth/login` with admin credentials.
2. Copy the JWT token.
3. Click **Authorize**.
4. Paste `Bearer <token>`.
5. Test secured endpoints.

## Testing

Backend tests:

```bash
cd backend
mvn test -Dspring.profiles.active=test
```

Included tests:

- `StudentServiceTest`
- `AttendanceServiceTest`
- `MarksServiceTest`
- `AuthServiceTest`
- `AuthControllerTest`
- `StudentControllerTest`

## Business Rules

### Grade Calculation

| Score | Grade |
|---:|:---|
| >= 90 | A+ |
| >= 80 | A |
| >= 70 | B+ |
| >= 60 | B |
| >= 50 | C |
| >= 40 | D |
| < 40 | F |

### Attendance Bands

| Attendance | Band |
|---:|:---|
| >= 90% | Excellent |
| 75% to 89% | Good |
| 50% to 74% | Average |
| < 50% | Poor |

### Thresholds

- Attendance defaulter: below 75%
- Pass: score >= 40
- Fail: score < 40
- At-risk: low attendance and/or low marks

## HTTP Status Codes Used

| Status | Meaning | Typical Scenario |
|---:|---|---|
| 200 OK | Successful read/update | GET and PUT endpoints |
| 201 Created | Resource created | POST create endpoints |
| 204 No Content | Resource soft deleted | DELETE endpoints |
| 400 Bad Request | Validation or malformed input failed | Missing fields, invalid email, invalid percentage |
| 401 Unauthorized | Missing/invalid JWT or bad credentials | Login failure or no token |
| 403 Forbidden | Authenticated user lacks required role | Student attempting admin endpoint |
| 404 Not Found | Resource does not exist or is soft deleted | Invalid student/course ID |
| 409 Conflict | Duplicate unique resource | Duplicate username, roll number, course code, enrollment |
| 500 Internal Server Error | Unexpected server error | Unhandled server/runtime issue |

## Postman REST API Requests and Responses

Import this collection:

```text
postman/Student Portal API.postman_collection.json
```

Collection variables:

```text
baseUrl = http://localhost:8080
token   = auto-filled after successful Login request
```

### Authentication

| Method | Endpoint | Body | Success Response | Possible Errors |
|---|---|---|---|---|
| POST | `/api/auth/login` | `{"username":"admin","password":"Admin@123"}` | `200 {"token":"<jwt>","username":"admin","email":"admin@studentportal.local","role":"ADMIN"}` | 400, 401 |
| POST | `/api/auth/register` | `{"username":"student1","email":"student1@example.com","password":"Student@123","role":"STUDENT"}` | `201 {"token":"<jwt>","username":"student1","role":"STUDENT"}` | 400, 409 |

### Students

| Method | Endpoint | Body | Response |
|---|---|---|---|
| GET | `/api/students?keyword=&page=0&size=10` | - | `200 Page<StudentResponse>` |
| POST | `/api/students` | `{"rollNumber":"R001","firstName":"Asha","lastName":"K","email":"asha@example.com","phone":"9999999999","department":"CSE","semester":3,"userId":null}` | `201 StudentResponse` |
| GET | `/api/students/{id}` | - | `200 StudentResponse`, `404 ApiError` |
| PUT | `/api/students/{id}` | Same as create body | `200 StudentResponse` |
| DELETE | `/api/students/{id}` | - | `204 No Content` |
| GET | `/api/students/me` | - | `200 StudentResponse` |

### Courses

| Method | Endpoint | Body | Response |
|---|---|---|---|
| GET | `/api/courses?page=0&size=10` | - | `200 Page<CourseResponse>` |
| POST | `/api/courses` | `{"courseCode":"CS201","courseName":"Data Structures","credits":4,"facultyName":"Prof. Mehta"}` | `201 CourseResponse` |
| GET | `/api/courses/{id}` | - | `200 CourseResponse`, `404 ApiError` |
| PUT | `/api/courses/{id}` | Same as create body | `200 CourseResponse` |
| DELETE | `/api/courses/{id}` | - | `204 No Content` |

### Enrollment

| Method | Endpoint | Body | Response |
|---|---|---|---|
| POST | `/api/student-courses` | `{"studentId":1,"courseId":1}` | `201 EnrollmentResponse` |
| GET | `/api/student-courses/student/{studentId}` | - | `200 EnrollmentResponse[]` |
| GET | `/api/student-courses/course/{courseId}` | - | `200 EnrollmentResponse[]` |
| DELETE | `/api/student-courses/student/{sId}/course/{cId}` | - | `204 No Content` |

### Attendance

| Method | Endpoint | Body | Response |
|---|---|---|---|
| POST | `/api/attendance` | `{"studentId":1,"courseId":1,"attendancePercentage":82.5}` | `201 AttendanceResponse` |
| PUT | `/api/attendance/{id}` | Same as create body | `200 AttendanceResponse` |
| DELETE | `/api/attendance/{id}` | - | `204 No Content` |
| GET | `/api/attendance/student/{id}` | - | `200 AttendanceResponse[]` |
| GET | `/api/attendance/defaulters?page=0&size=10` | - | `200 Page<AttendanceResponse>` |

### Marks

| Method | Endpoint | Body | Response |
|---|---|---|---|
| POST | `/api/marks` | `{"studentId":1,"courseId":1,"score":91.0}` | `201 MarksResponse` with grade and pass/fail |
| PUT | `/api/marks/{id}` | Same as create body | `200 MarksResponse` |
| DELETE | `/api/marks/{id}` | - | `204 No Content` |
| GET | `/api/marks/student/{id}` | - | `200 MarksResponse[]` |
| GET | `/api/marks/rankings?page=0&size=10` | - | `200 Page<RankingResponse>` |

### Announcements

| Method | Endpoint | Body | Response |
|---|---|---|---|
| GET | `/api/announcements?page=0&size=10` | - | `200 Page<AnnouncementResponse>` |
| POST | `/api/announcements` | `{"title":"Exam Schedule","description":"Midterm starts Monday"}` | `201 AnnouncementResponse` |
| GET | `/api/announcements/{id}` | - | `200 AnnouncementResponse` |
| PUT | `/api/announcements/{id}` | Same as create body | `200 AnnouncementResponse` |
| DELETE | `/api/announcements/{id}` | - | `204 No Content` |

### Study Materials

| Method | Endpoint | Body | Response |
|---|---|---|---|
| GET | `/api/study-materials?page=0&size=10` | - | `200 Page<StudyMaterialResponse>` |
| POST | `/api/study-materials` | `{"courseId":1,"title":"Unit 1 Notes","fileUrl":"https://example.com/unit1.pdf"}` | `201 StudyMaterialResponse` |
| GET | `/api/study-materials/course/{id}` | - | `200 StudyMaterialResponse[]` |
| PUT | `/api/study-materials/{id}` | Same as create body | `200 StudyMaterialResponse` |
| DELETE | `/api/study-materials/{id}` | - | `204 No Content` |

### Dashboard and Reports

| Method | Endpoint | Response |
|---|---|---|
| GET | `/api/dashboard/summary` | `200 SummaryResponse` |
| GET | `/api/dashboard/top-students?limit=5` | `200 RankingResponse[]` |
| GET | `/api/dashboard/low-attendance` | `200 AttendanceResponse[]` |
| GET | `/api/dashboard/at-risk-students` | `200 AtRiskStudentResponse[]` |
| GET | `/api/reports/student-rankings?page=0&size=10` | `200 Page<RankingResponse>` |
| GET | `/api/reports/attendance-defaulters?page=0&size=10` | `200 Page<AttendanceResponse>` |
| GET | `/api/reports/course-performance` | `200 CoursePerformanceResponse[]` |
| GET | `/api/reports/department-performance` | `200 DepartmentPerformanceResponse[]` |
| GET | `/api/reports/pass-fail-statistics` | `200 PassFailStatisticsResponse` |
| GET | `/api/reports/attendance-statistics` | `200 BandStatisticResponse[]` |
| GET | `/api/reports/monthly-enrollment?year=2026` | `200 MonthlyEnrollmentResponse[]` |

### Audit and Health

| Method | Endpoint | Response |
|---|---|---|
| GET | `/api/audit-logs?page=0&size=10` | `200 Page<AuditLogResponse>` |
| GET | `/actuator/health` | `200 {"status":"UP"}` |
| GET | `/swagger-ui.html` | Swagger UI page |

## Sample Error Response

```json
{
  "timestamp": "2026-06-17T10:15:30",
  "status": 409,
  "error": "Conflict",
  "message": "Course code already exists",
  "path": "/api/courses",
  "details": []
}
```

## Deployment Notes

- Build backend Docker image from `backend/Dockerfile`.
- Build frontend Docker image from `frontend/Dockerfile`.
- Configure Render services with the same environment variables from `.env.example`.
- Set GitHub secrets listed in `.github/workflows/ci-cd.yml` before enabling deployment jobs.

## Security Notes

- Replace the development JWT secret in production.
- Use HTTPS in production.
- Restrict CORS origins to deployed frontend domains.
- Avoid storing production database credentials in Git.
- Default admin password should be changed after first deployment.
