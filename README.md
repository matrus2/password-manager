[![Build Status](https://travis-ci.org/matrus2/password-manager.svg?branch=master)](https://travis-ci.org/matrus2/password-manager) [![Coverage Status](https://coveralls.io/repos/github/matrus2/password-manager/badge.svg?branch=master)](https://coveralls.io/github/matrus2/password-manager?branch=master)

##Passwords Manager

####Usage instructions:

1. Ensure that MongoDB server is running locally on port 27018;
2. Inject sample data to DB by invoking:
    ```$java
    mongorestore --port 27018  dump
    ```
3. Download and install JCE Policy:

    ```
    http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
    ```
4. Run application:
    ```$xslt
    mvn spring-boot:run  
    ```
    