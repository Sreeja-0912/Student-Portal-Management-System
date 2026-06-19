# ER Diagram

```mermaid
erDiagram
  ROLES ||--o{ USERS : has
  USERS ||--o| STUDENTS : linked_to
  STUDENTS ||--o{ STUDENT_COURSES : enrolls
  COURSES ||--o{ STUDENT_COURSES : contains
  STUDENTS ||--o{ ATTENDANCE : has
  COURSES ||--o{ ATTENDANCE : tracks
  STUDENTS ||--o{ MARKS : receives
  COURSES ||--o{ MARKS : grades
  COURSES ||--o{ STUDY_MATERIALS : owns

  ROLES {
    bigint id PK
    varchar name UK
  }
  USERS {
    bigint id PK
    varchar username UK
    varchar password
    varchar email UK
    bigint role_id FK
    boolean active
    boolean deleted
  }
  STUDENTS {
    bigint id PK
    varchar roll_number UK
    varchar first_name
    varchar last_name
    varchar email UK
    varchar phone
    varchar department
    int semester
    bigint user_id FK
    boolean deleted
  }
  COURSES {
    bigint id PK
    varchar course_code UK
    varchar course_name
    int credits
    varchar faculty_name
    boolean deleted
  }
  STUDENT_COURSES {
    bigint id PK
    bigint student_id FK
    bigint course_id FK
    boolean deleted
  }
  ATTENDANCE {
    bigint id PK
    bigint student_id FK
    bigint course_id FK
    decimal attendance_percentage
    boolean deleted
  }
  MARKS {
    bigint id PK
    bigint student_id FK
    bigint course_id FK
    decimal score
    boolean deleted
  }
  ANNOUNCEMENTS {
    bigint id PK
    varchar title
    varchar description
    varchar created_by_name
    boolean deleted
  }
  STUDY_MATERIALS {
    bigint id PK
    bigint course_id FK
    varchar title
    varchar file_url
    boolean deleted
  }
  AUDIT_LOGS {
    bigint id PK
    varchar action
    varchar username
    boolean deleted
  }
```

Unique constraints:

- `roles.name`
- `users.username`, `users.email`
- `students.roll_number`, `students.email`
- `courses.course_code`
- `student_courses(student_id, course_id)`
- `attendance(student_id, course_id)`
- `marks(student_id, course_id)`
