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
    department_id INTEGER REFERENCES department(id),
    is_active INTEGER
);
```
### Table: `employee & it's detail`

```sql
CREATE TABLE IF NOT EXISTS employee (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(250),
    last_name VARCHAR(250),
    middle_name VARCHAR(250),
    gender VARCHAR(10),
    email_id VARCHAR(50),
    emp_code VARCHAR(50),
    role_id INTEGER REFERENCES grade_and_role(id),
    joined_on DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active INTEGER
);

CREATE TABLE IF NOT EXISTS employee_personal_detail (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    emp_id INTEGER REFERENCES employee(id),
    emp_date_of_birth DATE,
    personal_email_id VARCHAR(50),
    father_name VARCHAR(50),
    father_dob DATE,
    father_occupation VARCHAR(50),
    mother_name VARCHAR(50),
    mother_dob DATE,
    mother_occupation VARCHAR(50),
    is_married INTEGER,
    marriage_date DATE,
    spouse_name VARCHAR(50),
    spoouse_dob DATE,
    spouse_occupation VARCHAR(50),
    children_count INTEGER,
    child_first_name VARCHAR(50),
    child_first_dob DATE,
    child_second_name VARCHAR(50),
    child_second_dob DATE,
    child_third_name VARCHAR(50),
    child_third_dob DATE,
    updated_at TIMESTAMP,
    is_active INTEGER
);

CREATE TABLE IF NOT EXISTS employee_address (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    emp_id INTEGER REFERENCES employee(id),
    country_code VARCHAR(50),
    phone_number VARCHAR(50),
    alternate_number VARCHAR(50),
    emergency_contact_name VARCHAR(50),
    emergency_contact_relation VARCHAR(50),
    emergency_contact_number VARCHAR(50),
    house_number VARCHAR(250),
    street VARCHAR(250),
    city VARCHAR(250),
    state VARCHAR(250),
    country VARCHAR(50),
    postal_code VARCHAR(50),
    updated_at TIMESTAMP,
    is_active INTEGER
);

CREATE TABLE IF NOT EXISTS employee_current_salary (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    emp_id INTEGER REFERENCES employee(id), 
    ctc_salary INTEGER,
    is_active INTEGER
);

CREATE TABLE IF NOT EXISTS employee_banking_detail (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    emp_id INTEGER REFERENCES employee(id), 
    adhaar_number VARCHAR(50),
    pan_number VARCHAR(50),
    uan_number VARCHAR(50),
    pf_number VARCHAR(50),
    work_days INTEGER,
    bank_name VARCHAR(250),
    account_number VARCHAR(50),
    ifsc_code VARCHAR(50),
    branch VARCHAR(50),
    is_active INTEGER
);

CREATE TABLE IF NOT EXISTS employee_created_salary (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    emp_id INTEGER REFERENCES employee(id),
    banking_id INTEGER REFERENCES employee_banking_detail(id),
    basic_salary INTEGER,
    house_rent_allowance INTEGER,
    special_allowance INTEGER,
    pf_amount INTEGER,
    income_tax INTEGER,
    gross_earning INTEGER,
    net_pay INTEGER,
    created_date TIMESTAMP,
    is_active INTEGER
);

CREATE TABLE IF NOT EXISTS employee_attendance (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    emp_id INTEGER REFERENCES employee(id), 
    is_present INTEGER,
    created_date TIMESTAMP,
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