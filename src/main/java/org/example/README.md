# Library Management System

## Overview

This project is a library management system that includes functionalities for managing users, books, authors, categories, posts, and comments. It provides features for adding, updating, deleting, and listing users and books, along with importing and exporting data using Excel. The system also includes API documentation with Swagger, authentication with Spring Security and JWT, and application logging with Logback.

## Features

### User Management
- **Add, Update, Delete Users**: Allows for the management of user records.
- **List Users**: Provides pagination and search capabilities to list users.
- **Export/Import Users**: Allows exporting and importing user data via Excel files.

### Book Management
- **Add, Update, Delete Books**: Manage book records in the library.
- **Borrow/Return Books**: Allows users to borrow and return books.

### Additional Features
- **Swagger Integration**: API documentation and testing using Swagger.
- **Spring Security + JWT**: Authentication and authorization using Spring Security with JWT.
- **Logback Logging**: Application logging configured with Logback.

### Entity Management
- **Books**: Manage book details including title, author, genre, and more.
- **Authors**: Manage author details.
- **Categories**: Manage book categories.
- **Posts**: Manage posts related to the library.
- **Comments**: Manage comments on posts.

### JPA Relationships
- **@OneToMany**: One entity has a collection of another entity.
- **@ManyToMany**: Many entities are related to many other entities.
- **@OneToOne**: One entity is related to exactly one other entity.
- **@ManyToOne**: Many entities are related to one entity.

## Getting Started


## Setup

### SQL databases are used here

### Create SQL databases

-- Create users table
CREATE TABLE users (
id INT AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(255) NOT NULL UNIQUE,
telephone VARCHAR(20),
email VARCHAR(255) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL
);

-- Create books table
CREATE TABLE books (
id INT AUTO_INCREMENT PRIMARY KEY,
title VARCHAR(255) NOT NULL,
author_id INT,
category_id INT,
FOREIGN KEY (author_id) REFERENCES authors(id),
FOREIGN KEY (category_id) REFERENCES categories(id)
);

### Insert sample data

INSERT INTO users (username, telephone, email, password) VALUES
('test', '0123321312', 'test@gmail.com', 'hashed-password'),
('library-admin', '1234567890', 'admin@library.com', 'hashed-password');

INSERT INTO books (title, author_id, category_id) VALUES
('The Great Gatsby', 1, 1),
('To Kill a Mockingbird', 2, 2);

## Every component

### Admin

An admin account is created with the username 'library-admin' and password 'password123'. He is able to access the users api (retrieve/create/delete/update) and the Excel api (export/import)

### Excel

The Excel files has all the attributes of user except password for safety issues.

### Swagger

It is used to be able to see the API documentation at this website :
http://localhost:8080/swagger-ui.html

### Logback

It is used in order to see the log application in the console

### Spring Security + JWT login

It is used in order to have a secure application with encrypted password.

### Books

Any book can be borrowed by only one person, who can only borrow 5 at a time.

