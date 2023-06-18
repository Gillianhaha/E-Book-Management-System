## E-Book Management System
This is a system to manage user and books.

### Tech
* Frontend: Swing
* Backend: Spring MVC
* Database: MySQL

### How to start the service
* Start MySQL server.
  ```
  mysql.server start
  ```
* Start MySQL prompt.
  ```
  mysql -u root
  ```
* Create a database named `db1` or drop first if there is already one.
  ```
  DROP DATABASE db1;
  CREATE DATABASE db1;
  ```
* Finally, start the service
  ```
  mvn spring-boot:run
  ```