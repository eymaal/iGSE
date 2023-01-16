# iGSE

This project serves as backend for CW2 - CO7102 - Mobile and Web Applications.
The project was created using Java and SpringBoot, with MySQL serving the data.

## Default Configuration
- MySQL Port number: ```3306```
- Backend Port Number: ```8080```
- Database name: ```igse```

# To Start Server
1. Make sure database credentials area updated in application.properties file.
[```src/main/resources/application.properties```]
1. Ensure that database is created. (default: igse)
**[Tables and Initial records will be created and inserted automatically]**
1. Run the following command to clean and build dependencies.
```mvn clean install```
1. Run the following command to start server.
```mvn spring-boot::run```

