# Zoo Lab
This project is considered to be a combination of different technologies and approaches in modern software development.

# Table of contents
* [Installation](#installation)
* [Containerization](#containerization)
* [Run without docker](#runwithoutdocker)

---
### Installation
```
1. Installl docker
2. Clone project
3. Install MariaDB
```
---
### Containerization 
To run this using docker we can use a command
```
docker-compose up -d --build 
```
---
App will run on port 8080.

### Run without docker
To run this without docker we can set the properties:
```
DB_HOST = localhost
Active profile = dev
```
Or run maven command:
```
mvn spring-boot:run -DDB_HOST=localhost -Dspring.profiles.active=dev
```
