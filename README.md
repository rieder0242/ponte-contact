# Leírás

##install

adatbázis létrehozás

elérés beállítása
    liquibase.properties
    src\main\resources\application.properties

schama létrehozés
    mvn liquibase:update

fordítás
    mvn clean compile assembly:single -DskipTests

image létrehozás
    docker build -t ponte-contact .

## futtatás

indítás
    docker run -p 8080:8080 ponte-contact

böngészőben
    http://localhost:8080/contact

beléps 
    user
    alma

# Backend

# Felület