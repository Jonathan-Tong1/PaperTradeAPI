# Spring Boot API with JWT Authentication, OAuth2 Login, and Two-Factor Authentication

## 🚀 Use Case

This project demonstrates a secure Spring Boot API that integrates JWT authentication, OAuth2 login with Google and GitHub, and two-factor authentication (2FA). It allows users to authenticate through either username/password or third-party OAuth2 providers (Google and GitHub). For enhanced security, two-factor authentication (2FA) is enabled using GitHub's TOTP system.

### Features:
- **JWT Authentication**: Token-based authentication for API security.
- **OAuth2 Login**: Social login integration with Google and GitHub.
- **Two-Factor Authentication (2FA)**: Provides an extra layer of security with TOTP-based 2FA.
- **MySQL Database**: Stores user credentials, OAuth login details, and 2FA settings.

---

## 🛠 Technologies Used

- **Spring Boot**: The framework used to develop the backend API.
- **Spring Security**: Manages authentication and authorization logic.
- **JWT (JSON Web Token)**: Provides stateless, token-based authentication for secure API access.
- **OAuth2**: Enables social login integrations with Google and GitHub.
- **Two-Factor Authentication (2FA)**: Uses TOTP (Time-based One-Time Password) for added security.
- **MySQL**: Database for storing user data, including credentials, OAuth2 information, and 2FA configurations.
- **Spring Data JPA**: Manages data access between the application and MySQL database.
- **Google Authenticator / GitHub 2FA**: Used for generating TOTP tokens for 2FA.

---
