# Customer-Management-Service

This is a mini project using java spring boot.
* jdk 11
* postgreSQL

# Database Configuration
Create a new role with username = postgresqldb and password = postgre123.
Then run the DDL command to create a new table.
* or change the datasource configuration at application.properties as yours, then add the new table.

# DDL create table
CREATE TABLE customer(
nik varchar(20) primary key,
full_name varchar(50),
address varchar(150),
phone_number varchar(12),
account_number varchar(12),
balance numeric,
status varchar(10)
);

CREATE TABLE management(colname SERIAL);

CREATE TABLE transaction_history(
id SERIAL,                               
date date,
account_number varchar(12),
amount numeric,
transaction_type varchar(2)
);

# Run the Application 
*mvn clean install
*mvn spring-boot:run

# Path URL

http://localhost:8081/

# API Documentation

http://localhost:8081/swagger-ui.html#/customer-web-controller

![screenswagger.png](/screenswagger.png)