# Leírás

Ez a Ponte.hu számára készített tesztfeladat.

# Telepítés

1. Töltse le ezt a repository-t.

2. Hozzon létre egy PostgreSQL adatbázist.

3. Állítsa be a PostgreSQL elérését a két fájban:
`liquibase.properties`, `src\main\resources\application.properties`

4. Hozza létre az adatbázis struktúráját, illetve a hozzá tartozó adatokat a következő utasítással:
```
    mvn liquibase:update
```
5. Fordítsa le a projektet a következő utasítással:
```
    mvn clean compile assembly:single -DskipTests
```
6. Hozza létre a konténer image-et a következzőképpen:
```
    docker build -t ponte-contact .
```

# Futtatás

1. Indítsa el az alkalmazást
```
    docker run -p 8080:8080 ponte-contact
```
2. Látogassa meg a böngészőjében a következő címet:
    http://localhost:8080/contact

3. Belépéshez használja az `user` felhasználónevet, illetve az  `alma` jelszót.
4. Kellemes adatböngészést kívánok! :)

# Megvalósítás
Alepvetően a backend pozicióra jelentkeztem, de fullstack-es gyakorlatomra támaszkodva a frontend felületet is kialakítottam. Azonban a UI-t nem a kiírásnak megfelelő framework-ök egyikében készítettem el, hanem Vanilia JavaScript-ben.

# Megvalósított szorgalmi feladatok
* Formai ellenőrzés pl. e-mail címekre és telefonszámokra, mind szerver, mind kliens oldalon.
* Lapozható listaoldal helyett lefelé scroll-ozva dinamikusan töltődik be a tartalom.
* A kontaktok minden adatukkal együtt törölhetők a listából.
* Unit test-et készítettem.
* Az adatbázis-struktúrát és az adatokat Liquibase-sel menedzselem.
* Spring Security-vel lehet belépni.
