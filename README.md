
# Pahana Edu Billing System

## Overview

Pahana Edu Billing System is a Java-based web application for educational billing management. It uses Jakarta EE Servlets (6.0), JSP, and MySQL for data persistence, and is deployed on Apache Tomcat 11.0.9. The project demonstrates a real-world, session-based authentication and billing workflow.

## Project Structure

The project follows the Maven standard layout, with additional organization for MVC and DAO patterns:

```text
Pahana Edu Billing System/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/pahanaedu/
│       │       ├── controller/         # Servlets (LoginServlet, LogoutServlet, DashboardServlet)
│       │       ├── dao/                # Data Access Objects (UserDAO, BillDAO, etc.)
│       │       ├── service/            # Business logic (AuthService)
│       │       ├── filter/             # Servlet filters (AuthenticationFilter)
│       │       ├── helpers/            # Utility classes (DatabaseHelper)
│       │       ├── model/              # Data models/POJOs (User, etc.)
│       │       └── util/               # Utilities (PasswordUtil)
│       ├── resources/
│       │   ├── .env.example            # Example environment file (copy to .env and fill in credentials)
│       │   └── .env                    # (NOT in version control; required for DB connection)
│       └── webapp/
│           ├── index.jsp
│           ├── login.jsp
│           ├── dashboard.jsp
│           ├── css/
│           │   ├── common.css
│           │   ├── login.css
│           │   └── dashboard.css
│           └── WEB-INF/
│               └── web.xml
├── database/
│   ├── schema.sql
│   └── seeds.sql
└── target/
    ├── PahanaEdu.war
    └── ...
```

## Key Files

- `LoginServlet.java`, `LogoutServlet.java`, `DashboardServlet.java`: Main servlet controllers
- `UserDAO.java`, `BillDAO.java`, etc.: Data access logic
- `AuthService.java`: Authentication logic
- `AuthenticationFilter.java`: Session-based authentication filter
- `DatabaseHelper.java`: Singleton DB connection manager
- `PasswordUtil.java`: Password handling (plain text for debugging)
- `login.jsp`, `dashboard.jsp`, `index.jsp`: Main JSP pages
- `.env.example`: Example environment file (copy to `.env` and fill in credentials)

## Prerequisites

- Java Development Kit (JDK) 17 or higher (tested with OpenJDK 24)
- Apache Maven
- MySQL Server 8.0+
- Apache Tomcat 11.0.9 (Jakarta EE 10 compatible)

## Build and Deployment

### 1. Environment Setup (REQUIRED)

- Copy `src/main/resources/.env.example` to `src/main/resources/.env` and fill in your MySQL credentials:

  ```
  DB_URL=jdbc:mysql://localhost:3306/pahana_edu
  DB_USER=your_username
  DB_PASSWORD=your_password
  ```

### 2. Database Setup

- Create the database and tables:

  ```sql
  CREATE DATABASE pahana_edu;
  ```

- Load schema and seed data:

  ```bash
  mysql -u [username] -p pahana_edu < database/schema.sql
  mysql -u [username] -p pahana_edu < database/seeds.sql
  ```

### 3. Build the Project

- Always run clean before package:

  ```bash
  mvn clean package
  ```

- Output: `target/PahanaEdu.war`

### 4. Deploy to Tomcat

- Copy `target/PahanaEdu.war` to Tomcat's `webapps/` directory
- Start Tomcat server
- Application will be available at:
  - `http://localhost:8080/PahanaEdu/` (WAR deployment)
  - or `http://localhost:8080/PahanaEdu_war_exploded/` (IDE exploded deployment)

## Environment Variable Setup

**IMPORTANT:** The `.env` file must be placed in `src/main/resources/.env` (not the project root). This is required for the application to start and connect to the database.

An example file is provided as `src/main/resources/.env.example`. Copy it to `.env` and fill in your credentials. The `.env` file is excluded from version control.

## Troubleshooting

- **404 on deployment:** Check the context path. In IDEs, it may deploy as `/PahanaEdu_war_exploded/`.
- **Database connection failed:** Ensure MySQL is running and `.env` is present in `src/main/resources/`.
- **Session timeout:** Default is 30 minutes (see `web.xml`).
- **Maven not found:** Install Maven separately (no wrapper provided).
- **Java version mismatch:** Use JDK 17+ for Jakarta EE 10 compatibility.

## License

MIT License
