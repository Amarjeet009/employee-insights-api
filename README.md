## 📘 Employee Management Database Schema

This document outlines the structure of the employee management system built using PostgreSQL.
***
### Run Docker Image
```jsunicoderegexp
docker image

```
```jsunicoderegexp
docker ps
```
```jsunicoderegexp
docker run --name app-postgres -e POSTGRES_PASSWORD=password -p 5433:5432 -d postgres
```
```jsunicoderegexp
docker stop app-postgres
```
```jsunicoderegexp
docker start app-postgres
```
```jsunicoderegexp
docker rm app-postgres  (if need to remove)
```
***



### Table: `department`

```sql
CREATE TABLE IF NOT EXISTS department (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    department_name VARCHAR(250),
    is_active INTEGER
);

INSERT INTO department (department_name, is_active) VALUES
('Human Resource', 1),
('IT Tech', 1),
('IT Support', 1);
```
### Table: `grade_and_role`

```sql
CREATE TABLE IF NOT EXISTS grade_and_role (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    role_name VARCHAR(250),
    grade INTEGER,
    salary_range VARCHAR(500),
    is_active INTEGER
);
```
### Table: `employee`

```sql
CREATE TABLE IF NOT EXISTS employee (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(250),
    last_name VARCHAR(250),
    age INTEGER,
    role_id INTEGER REFERENCES grade_and_role(id),
    department_id INTEGER REFERENCES department(id),
    phone_number VARCHAR(250),
    joined_on DATE,
    address VARCHAR(250),
    date_of_birth DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

```
### Table: `employee_salary`

```sql
CREATE TABLE IF NOT EXISTS employee_salary (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    emp_id INTEGER REFERENCES employee(id), 
    salary INTEGER,
    is_active INTEGER
);
```
### Table: `employee_attendance`

```sql
CREATE TABLE IF NOT EXISTS employee_attendance (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    emp_id INTEGER REFERENCES employee(id), 
    salary INTEGER,
    is_active INTEGER
);

```

### `To build your project using Gradle from the command line, you can run:`
```jsunicoderegexp
./gradlew build
```
### `If you're on Windows, use:`
```jsunicoderegexp
gradlew.bat build
```
### `To clean the build before compiling:`
```jsunicoderegexp
./gradlew clean build
```
### `To skip tests:`
```jsunicoderegexp
./gradlew build -x test
```
### `To run your app (if using the application plugin):`
```jsunicoderegexp
./gradlew run
```

### `Docker build`
```jsunicoderegexp
docker build -t employee-insights-api .
```
### `Run Docker build`
```jsunicoderegexp
docker run -p 8084:8080 employee-insights-api
```