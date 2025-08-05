# Pahana Edu Billing System

## Overview
The Pahana Edu Billing System is a web application built using Java Servlets and JSP, deployed on an Apache Tomcat server. It is designed to demonstrate a simple servlet and JSP integration.

## Project Structure
The project follows the standard Maven directory structure:

```
Pahana Edu Billing System/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/pahanaedu/
│   │   │       └── HelloServlet.java
│   │   ├── resources/
│   │   └── webapp/
│   │       ├── index.jsp
│   │       └── WEB-INF/
│   │           └── web.xml
└── target/
    ├── PahanaEdu.war
    └── ...
```

## Key Files

### `HelloServlet.java`
This servlet handles HTTP GET requests and responds with a simple HTML message.

### `index.jsp`
A JSP file that serves as the landing page of the application. It includes a link to test the servlet.

### `web.xml`
The deployment descriptor that maps the servlet to the `/hello` URL pattern.

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- Apache Maven
- Apache Tomcat 11.0.9

## Build and Deployment

1. **Build the Project**
   ```bash
   mvn clean package
   ```
   This will generate a WAR file in the `target/` directory.

2. **Deploy to Tomcat**
   - Copy the `PahanaEdu.war` file from the `target/` directory to the `webapps/` directory of your Tomcat installation.
   - Start the Tomcat server.

3. **Access the Application**
   - Visit `http://localhost:8080/PahanaEdu_war_exploded/` for the JSP page.
   - Visit `http://localhost:8080/PahanaEdu_war_exploded/hello` to test the servlet.

## Troubleshooting

### 404 Error on `/PahanaEdu`
Ensure that the correct URL is being used. The application is deployed under the context path `PahanaEdu_war_exploded` by default. Use the following URLs:
- JSP: `http://localhost:8080/PahanaEdu_war_exploded/`
- Servlet: `http://localhost:8080/PahanaEdu_war_exploded/hello`

### Changing the Context Path
To change the context path:
1. Rename the WAR file to your desired context path (e.g., `PahanaEdu.war`).
2. Redeploy the WAR file to Tomcat.

## License
This project is licensed under the MIT License.
