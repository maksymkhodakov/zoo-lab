# Zoo Lab
This project is considered to be a combination of different technologies and approaches in modern software development.

# Table of contents
* [Google drive](#google-drive)
* [Postman](#postman)
* [Swagger](#swagger)
* [Installation](#installation)
* [Containerization](#containerization)
* [Run without docker](#runwithoutdocker)
* [Start using](#start-using)

---
### Google Drive
This Google Drive is used to store files such as: SEO analytics and Postman collections: [link](https://drive.google.com/drive/folders/1fBpfP7I5tSagGTB_iIcAeFuXVp8mpLth?usp=sharing)

---
### Postman
For automatic testing of WebAPI is recommended to use Postman.
To recreate a collection in your Postman app, use [this](https://drive.google.com/file/d/1wjwz37bV4NKeEBbILof0P9leJ5weAzTh/view?usp=drive_link) json file import collection with all appropriate automatic API tests.

---
### Swagger
Swagger is used for dev testing purposes. Link could be found in application.properties

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
---
### Start using
Despite using application via containerization, you can always try to use Azure:  
Azure link: [click here](https://zoo-lab.azurewebsites.net)  
#### NB! In order not to spend subscription money, the app via Azure can be turned off.  
## NB Keep in mind that Elasticsearch is disabled on prod environment