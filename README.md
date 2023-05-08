
# Nineleaps-Expense-Management-Project

The Nineleaps Expense Management project is aimed at automating the expenses reimbursement process for the company's employees and finance team. Currently, the reimbursement process is managed using an external tool, and approvals are done manually through email, leading to inefficiencies and delays. To address this challenge, the project will create a mobile platform that enables employees to create and manage expense reports, view their expenses history, and submit reports for approval. The platform will also include features such as employee login, approval management, and reimbursement management. By implementing this project, Nineleaps can enhance its operational efficiency and improve collaboration and networking among its employees.

## Getting Started

To get started with the Nineleaps Expense Management Project, you need to follow these steps:

## Prerequisites

Before you start with the project, make sure you have the following prerequisites installed on your system:

- Java version 8 or higher.
- MySQL Database version 8.32 or higher.
- Spring Tool Suite version 2.7 or higher.

## Installation

To install and run the project, you need to follow these steps:

Clone the repository from GitHub:

```bash
git clone https://github.com/parthbhayana/Nineleaps-Expense-Management-Project.git
```

Create a MySQL database and update the **'application.properties'** file with your database credentials:

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/expense_management_db
spring.datasource.username=<your_database_username>
spring.datasource.password=<your_database_password>
```
Build and run the project using Maven:
```bash
mvn spring-boot:run
```

## Features

The Nineleaps Expense Management Project has the following features:

- Employee Login: Employees can log in to the mobile application using their work email ID (SSO).

- Expense Report Management: Employees can create and manage their expense reports with a title, description, and list of expenses. They can view all their reports, delete and edit them before submitting them to their reporting manager for approval.

- Expense Management: Employees can create and manage their expenses with a merchant name, description, date, attachment/s, amount, tag report, category, etc. They can view all their expenses, delete and edit them before submitting them to their report.

- PDF Generation: The application generates a PDF format of the submitted expense report for the approval manager.

- Manager Approval/Rejection: Managers can view all the submitted reports and approve or reject them with optional comments. The employee is notified via email about the decision.

- Finance Admin Reimbursement Management: Finance admin can view all the approved reports, mark them as reimbursed, and add comments. They can also manage the categories and generate an Excel summary of all pending reimbursement reports sent to a specific finance email ID.


## Tech Stack

- Spring Boot (2.7.10)
- MySQL Database (8.0.32)
- Java 8





## Dependencies

- **'spring-boot-starter-data-jpa'**: Provides a starter set of dependencies for using Spring Data JPA including Hibernate, Spring ORM, and Spring Data JDBC.

- **'spring-boot-starter-web'**: Provides a starter set of dependencies for building web applications using Spring MVC.

- **'spring-boot-devtool'**: Provides development-time tools to help with automatic restarts and hot-swapping of code changes.

- **'mysql-connector-j'**: Provides a JDBC driver for connecting to a MySQL database.

- **'springfox-boot-starter'**: Provides a starter set of dependencies for using SpringFox to generate Swagger documentation for RESTful APIs.

- **'spring-boot-starter-test'**: Provides a starter set of dependencies for testing Spring Boot applications using JUnit, Mockito, and other testing frameworks.

- **'openpdf'**: Provides a library for creating and manipulating PDF documents.

- **'lombok'**: Provides annotations to simplify Java code, such as @Getter and @Setter for generating getters and setters, and @Data for generating getters, setters, equals, hashCode, and toString methods.

- **'jjwt'**: Provides a Java library for creating and verifying JSON Web Tokens (JWT).

- **'java-jwt'**: Provides a Java library for creating and verifying JWTs, which includes support for JWKs (JSON Web Keys) and JWSs (JSON Web Signatures).






