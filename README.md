  Spring Security Practice Project

Overview
This project is a practice application built with Spring Security to demonstrate and understand various security features in a Spring-based application. It covers key concepts such as authentication, authorization, and securing REST endpoints.

Features
User Authentication: Basic and advanced authentication methods.
Role-Based Authorization: Restrict access based on user roles.
Custom Login Page: Create and style a custom login page.
JWT Authentication: Secure REST APIs with JSON Web Tokens.
OAuth2 Integration: Implement OAuth2 for third-party login.
CSRF Protection: Enable and configure Cross-Site Request Forgery protection.
Demo

Getting Started
Prerequisites
JDK 11 or later
Maven or Gradle
An IDE like IntelliJ IDEA or Eclipse
Clone the Repository
bash
Copy code
git clone https://github.com/MINtesla/SpringSecurity.git
cd SpringSecurity.
Build and Run
Navigate to the project directory:

cd SpringSecurity.
Build the project using Maven:

Maven Build:
mvn clean install

RUN Project
mvn spring-boot:run
Access the application:

Open your browser and go to http://localhost:8080.

Usage
Authentication
To test authentication, visit the login page at http://localhost:8080/login.
