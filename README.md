# This is the Secure Branch. It fixes every vulnerability from the `insecure` branch and adds CSRF protection, session management, security headers, login lockout, and logging and monitoring.

# SAP_Project
Secure Application Programming Project.

# Notio
Notio is a small study board web application that is built for my SAP project.It is a simple application that allows students to register, log in, read and search study posts. They can also write their own posts and edit them. An administrator can delete any post.

The project has three different branches on purpose,
1: A working baseline of the application.
2: A deliberatly insecure version that displays some common web vulnerabilities.
3: A secure version that fixes these vulnerabilities., this can be merged into main after its working.

# Features
Register an account as a student (username, email, password)
Log in and Log out
View all posts, newest first
Search posts by title or body
Create a post
Students can edit thier own posts
Admins are allowed to delete any posts

# Technology used
Language: Java v25
Framework: Spring Boot 4.1.2 (With Spring MVC, Spring Security and Validation)
Templates: Thymeleaf
Database: SQLite, accessed with Spring JdbcClient
Password Storage: PBKDF2-HMAC-SHA256, 16-byte salt, 600,000 iterations
Build: Maven

# Prerequisites
JDK 25. Check with java -version.
Git
You don't need to install Maven: the project includes the Maven wrapper (mvnw, mvnw.cmd).

# Setup and run
git clone https://github.com/Cr1874/SAP_Project.git
cd SAP_Project
git checkout main          # or: insecure 
.\mvnw spring-boot:run

When the console shows Tomcat started on port 8080, open http://localhost:8080

# Test accounts

These accounts are synthetic and are created automatically every time the app starts. They are for local testing only.

Username	Password	Role
alan	AlanPass123!	USER
james	JamesPass123!	USER
admin	AdminPass123!	ADMIN

# Vulnerabilities and fixes
V1: SQL injection
V2: Reflected XSS
V3: Stored XSS
V4: DOM-Based XSS
V5: Sensitive Data Exposure

# Licence
MIT. See LICENSE.

# Author
Connor Reid