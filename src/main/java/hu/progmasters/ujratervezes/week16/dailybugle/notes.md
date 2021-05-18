## Általános:
 1. Minden `létrehozás` jellegű feladat esetén tárolni kell a `létrehozás időpontját`
 2. Minden `módosítás` vagy `törlés` esetén tárolni kell az `utolsó módosítás időpontját`
 3. Törlés esetén ne legyen valódi törlés, csak időpont, és státusz átállítás
 
 ## Újságírók
    -id INT PRIMARY KEY AUTO_INCREMENT
    -name VARCHAR(50)
    -address VARCHAR(100)
    -email VARCHAR(50)
    -phone VARCHAR(20)
 
 1. Szerzők listázása
    - személyes adatok + elérhetőség (cím, email, telefon)
 2. Profil id alapján
    - összes általuk írt cikk listázásnál használt változatban, időrendben csökkenő sorrendben.
 3. Adatmódosítás
 4. Törlés
    - elérhetőség (cím, email, telefon) törlése, név -> "Névtelen szerző"
    
 ## Telefonkönyv
 Újságírók telefonszáma kiexportálva egy fileba (CSV)  
 Beérkező kérésre adatbázisból legyen kiírva `phonebook.csv` filba ->  
 - név
 - telefonszám
 - ";"  
   
 CSV-t nem visszaküldeni HTTP kérésre a válaszban -> létrehozni + 200 OK!
 
 ## Cikkek
 
 1. Rögzítés  
 
        -id INT PRIMARY KEY AUTO_INCREMENT
        -publicist_id INT FK
        -title VARCHAR(50)
        -synopsys VARCHAR(150)
        -text VARCHAR(500)
 2. Listázás  

        -id INT PRIMARY KEY AUTO_INCREMENT
        -publicist VARCHAR(50)
        -title VARCHAR(50)
        -synopsys VARCHAR(150)
 3. Lekérés id alapján  
 
        -szerző neve
        -minden más
 
## Cikk importálása
Importálás fileból  
HTTP kérés törzsében elérési útvonalat kapja meg a gépen  
Nyissa meg és jelenítse meg a cikk adatait a következő módon:  
- 1 sor:  szerző id, ha nincs db-ben akkor hiba  
- 2 sor:  cikk címe  
- 3 sor:  rövid szinopszis
- Végén:   teljes szöveg  

Sikeres beolvasás, és cikk mentése -> 200 OK
Sikertelen -> 400 Bad Request

Cikket/Filet nem kell visszaküldeni a kérésre

## Értékelés
1-5 egész szám  
Nincs userhez kötve  
Akármennyi értékelés jöhet
Teljes listázás, szerző cikkei, teljes cikk ID alapján -> jelenjen meg az átlagos értékelés 2 tizedesjegyre + értékelések száma  

## Címlap
Szűrések:  
- Legfrissebb 10 cikk
- Legjobban értékelt 10 cikk  
- Legjobban értékelt 10 cikk, ami nem régebbi 3 napnál

## Kommentelés
Cikkhez lehet komment

    -id INT PRIMARY KEY AUTO_INCREMENT
    -author VARCHAR(50)
    -text VARCHAR(150)
    -article_id FK
    -create_time

Cikk listázásnál -> hány komment  
Teljes cikk megjelenítésénél -> összes komment, időrendi sorrendben csökkenő  

# OLVASD EL A LEÍRÁST A REGTŐL, FOLYTATÓDIK
## Olvasói regisztráció
Olvasóról eltároljuk  

    -id INT PRIMARY KEY AUTO_INCREMENT
    -email VARCHAR(50)
    -username VARCHAR(50)
    

