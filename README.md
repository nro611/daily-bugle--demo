Hírharsona projekt

A Daily Bugle (magyarul Hírharsona néven jelenik meg) fel szeretné újítani a háttérrendszereit, és a fejlesztéssel a ti csapatotokat bízták meg. Az authentikáció és jogosultsági szintek ellenőrzése nem része az első körös fejlesztésnek, így bár adódnak ezek a kérdések (ki vehet fel/módosíthat/törölhet bizonyos adatokat a szerverről), egyelőre ezekkel nem kell foglalkozni.
Általános infók
A feladot kezdjétek a leírás elolvasásával és tervezéssel. Találjátok ki az alapvető adatbázis-szerkezetet és az url-eket, és aztán haladjatok az implementálással lépésenként.
Próbáljatok minél több “leszállítható” részt megcsinálni, azaz inkább legyen meg a hét végéig több részfeladat teljesen, mint sok részfeladatnak csak mondjuk az adatbázis része.
A nem egymásra épülő részfeladatokat tetszőleges sorrendben valósíthatjátok meg, nem kell betartani a lenti sorrendet.

A feladat alapvetően Spring Boot webalkalmazásnak lett tervezve, és azt is javasoljuk, hogy így valósítsátok meg. Ha csapatszinten úgy döntötök, hogy ettől eltérnétek, és a java alap nyelvi eszközeivel valósítjátok meg a dolgokat, arra is van lehetőségetek, DE ez legyen akkor csapat szintű döntés, és még a tervezés elején döntsétek el, illetve számítsatok arra, hogy a feladatokat bonyolultabb lesz megoldani, azaz lehet, hogy kevesebb részfeladatra lesz időtök a héten. A három rétegű alkalmazás megközelítést ebben az esetben is érdemes lehet követni, a presentation layer-ben levő controller osztályokat ebben az esetben le tudjátok cserélni egy konzolos menüre (Scannerrel beolvasás, SOUT-tal kiírás), illetve a service és repository osztályokat meg tudjátok hajtani tesztekkel is (ez lehet időtakarékosabb, de kevésbé látványos megoldás).

A feladat nyílt végű, ha esetleg minden részfeladattal készen vagytok, akkor tovább lehet bővíteni, ehhez létrehoztam egy “ötletláda” dokumentumot, ahová bedobhatjátok a további fejlesztési ötleteiteket, és így a csapatok egymásnak is tudnak segíteni a gyakorlásban.
https://docs.google.com/document/d/1F079hLjzZGksvvj73FormM6Xr44P5FUflBNercEweEI/edit?usp=sharing

Ne felejtsetek el gyakran commitolni, update-elni és push-olni, hogy elkerüljétek a merge conflictokat!

Részfeladatok

Általános feladatok

Minden “létrehozás” jellegű feladat esetén tárolni kell a létrehozás időpontját, amit a szerveren kell beállítani.
Minden “módosítás” vagy “törlés” esetén tárolni kell az utolsó módosítás időpontját (csak az utolsót, és a törlés ebből a szempontból módosításnak minősül).
Minden “törlés” esetén ne történjen valódi törlés az adatbázisból, csak státusz legyen állítva. Ha ezen felül kell még valami változtatás törlés esetén (például újságíró nevének megváltoztatása), akkor arra a feladat külön ki fog térni.

Újságírók

A Hírharsona szeretné eltárolni a nála dolgozó cikkírókat. Minden újságírónak kapnia kell egy belső használatú azonosító számot, emellé eltároljuk róluk a nevüket, címüket, email címüket, telefonszámukat.
A rendszerben lennie kell lehetőségnek a szerzők listázására, ebben az esetben egy újságíróról elegendő a személyes adatait és elérhetőségeit listázni.
Emellett el lehet kérni egy újságíró profilját is az azonosító alapján.
Az újságírók adatait természetesen lehet módosítani is, és újságírót lehet törölni is.
Törlés esetén az újságíró elérhetőséget (cím, email, telefon) törölni kell az adatbázisból, a neve pedig legyen módosítva arra, hogy “Névtelen szerző”.

Telefonkönyv

A főszerkesztőnek szüksége van az újságírók telefonszámaira, kiexportálva egy fájlba. A beérkező kérésre az adatbázisból legyen feltöltve az újságírók neve és telefonszáma, és legyen kiírva egy phonebook.csv fájlba, CSV formátumban (egy újságíró kerül egy sorba, a név és a telefonszám mezők pontosvesszővel legyenek elválasztva).
A fájlt NEM kell visszaküldeni a HTTP kérésre válaszban, elegendő létrehozni, a kérésre csak válaszoljatok egy 200 OK-t!

Cikkek

A rendszerben lehet cikkeket rögzíteni. Minden cikknek van azonosítója, szerzője, címe, rövid szinopszisa és teljes szövege.
A cikkeket lehet listázni, ebben az esetben az ID, szerző, cím és rövid szinopszis jelenik meg egy cikkről.
Azonosító alapján el lehet kérni egy konkrét cikket, ebben az esetben az összes adatát megkapjuk, a teljes szövegével együtt és a szerző nevével (nem csak a szerző ID-val).
Cikket lehet módosítani és törölni, az alap feltételekkel.

Az újságírók profiljára kerüljön be az összes általuk írt cikk (a listázásnál használt változatban, azaz a teljes szöveg nélkül), időrendben csökkenő sorrendben.

Cikk importálása

Legyen lehetőség cikket fájlból importálni. A HTTP kérés törzsében ekkor azt az elérési útvonalat kapja meg a szerver, ahol megtalálja a fájlt a gépen (nem, ez nem teljesen életszerű, de egy fájl feltöltése HTTP kérésben nem triviális feladat), a megadott fájlt nyissa meg, és töltse be a cikk adatait az alábbiak alapján:
az első sorban szerepel a szerző id-ja (ha nincs ilyen szerző az adatbázisban, akkor hiba)
a második sorban a cikk címe
a harmadik sorban a rövid szinopszis
minden ezutáni szöveg pedig a cikk tartalma (értelemszerűen akár több sornyi szöveg is lehet már itt)
Ha sikeres volt a fájl beolvasása és a cikk mentése, akkor adjon vissza a kérésre 200 OK-t, ha nem volt sikeres, akkor 400 Bad Requestet! Magát a cikket vagy a fájlt NEM kell visszaküldeni válaszban a kérésre.

Értékelés

Minden cikket lehet értékelni 1-től 5-ig egy egész számmal. Az értékelés nincs felhasználóhoz kötve (egyelőre), egy cikkre akármennyi értékelés érkezhet.
A cikkeknél minden helyen (teljes listázás, szerző cikkei, teljes cikk ID alapján) jelenjen meg az átlagos értékelésük két tizedesjegyre kerekítve, illetve hogy hány értékelést kaptak.

Címlap (legfrissebb 10 cikk, legjobban értékelt 10 cikk)

A Hírharsona szeretné a weboldalának címlapján különböző feltételek alapján leszűrve megjeleníteni kiemelt cikkeket:
legfrissebb 10 cikk
legjobban értékelt 10 cikk
legjobban értékelt 10 cikk, ami nem régebbi 3 napnál

Kommentelés

A cikkekhez van lehetőség kommentet írni, egy kommentről eltároljuk a szerzőjét (egyelőre egy sima Stringben), a tartalmát, hogy melyik cikkhez tartozik, a létrehozás időpontját (szerveren beállítva) és egy azonosítót.
A cikkeknél listázás esetén jelenjen meg, hogy hány komment érkezett rájuk, teljes cikk megjelenítésénél pedig maguk a kommentek, időrendben csökkenő sorrendben.
Olvasói regisztráció 
Legyen lehetőség olvasói regisztrációra a rendszerben. Egy olvasóról eltároljuk az emailcímét és a megjelenített felhasználónevét, illetve kap egy belső azonosítót is.

Az értékelés legyen módosítva, egy cikk értékelése mostantól legyen felhasználóhoz kötve. Egy olvasó egy cikket csak egyszer értékelhet. Ha új értékelést küld be, akkor az módosítsa az eddigi értéket.

A kommentelés legyen módosítva, a komment szerzőjének már ne egy String legyen eltárolva, hanem egy olvasó azonosítója.

Olvasókat lehet listázni, ebben az esetben csak az ID-juk, az emailjük és a nevük kerül listázásra, illetve az összes kommentjük száma.
Olvasói profil
Legyen lehetőség ID alapján elkérni egy olvasó profilját, itt az email és név mellett szerepeljen:
minden cikk ID-ja és címe, amit értékelt az olvasó, és az adott értékelés
minden cikk ID-ja és címe, ami alá kommentelt az olvasó, és a hozzá tartozó kommentek száma
Időzített cikkek
Legyen lehetőség a cikkekhez megadni egy “élesítés időpontja” mezőt.
Az összes cikk megjelenítése kivételével, tehát a címlap funkciókban és a szerzők profiloldalánál csak olyan cikkek jelenjenek meg, amiknél az élesítés időpontja üresen lett hagyva, vagy már múltbeli az időpont.
Kulcsszó rendszer
Egyszerűbb változat: minden cikknek meg lehet adni egy előre rögzített listából egy kategóriát (például közélet, tudományos, bulvár, politika, stb). A cikkek listázásánál és a teljes cikk megjelenítésénél is adjuk vissza a kategóriát is.
Emellett lehet a cikkekre kategória alapján is keresni, ilyenkor adjuk vissza az összes olyan cikk listáját, ami az adott kategóriához tartozik.

Bonyolultabb változat: a cikkekhez kulcssszavakat lehet rögzíteni. A kulcsszavak nincsenek meghatározva előre, és minden cikkhez többet is fel lehet venni. A kulcsszavakat a cikkek rögzítésekor és módosításkor is be lehet állítani.
A kulcsszavakat a cikkek listázásánál és a teljes cikk megjelenítésénél is adjuk vissza a válaszban. Emellett legyen lehetőség kulcsszó alapján listázni a cikkeket.

Módosítások története (nehéz!)

A rendszerben történt módosítások és törlések dátumából nem csak a legutolsót akarjuk eltárolni, hanem az összes módosítást.
Figyelem! Nem triviális feladat, több új adatbázistáblát is létre kell hozni hozzá, és át kell gondolni minden folyamat lépéseit.

