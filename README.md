# Zoo Lab
This project is considered to be a combination of different technologies and approaches in modern software development.

# Table of contents
* [Installation](#installation)
* [Containerization](#containerization)
* [Run without docker](#runwithoutdocker)
* [Start using](#start-using)
* [Google drive](#google-drive)

---
### Google Drive
This Google Drive is used to store all videos regarding all phases: [link](https://drive.google.com/drive/folders/1fBpfP7I5tSagGTB_iIcAeFuXVp8mpLth?usp=sharing)

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