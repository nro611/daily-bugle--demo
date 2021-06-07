package hu.progmasters.ujratervezes.week16.dailybugle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class InitTables implements CommandLineRunner {
   
   private final JdbcTemplate jdbcTemplate;
   private final CreateTables createTables;
   
   @Autowired
   public InitTables(JdbcTemplate jdbcTemplate, CreateTables createTables) {
      this.jdbcTemplate = jdbcTemplate;
      this.createTables = createTables;
   }
   
   public static void main(String[] args) {
      SpringApplication.run(InitTables.class);
   }
   
   @Override
   public void run(String... args) {
      createTables.createAllTables();
      fillTablesWithDummyData();
   }
   
   private void fillTablesWithDummyData() {
      fillPublicistWithDummyData();
      fillArticleWithDummyData();
      fillReaderWithDummyData();
      fillCommentWithDummyData();
      fillRatingWithDummyData();
      fillKeywordWithDummyData();
      fillArticleKeywordWithDummyData();
   }
   
   private void fillPublicistWithDummyData() {
      String sql = "INSERT INTO publicist (name, address, email, phone, created_at) " +
              "VALUES " +
              "('Kovács Aladár', 'Veszprém, Aradi utca 2', 'aladar@gmail.com', '+36 30/374 8838', '2012-12-12T12:13:01') ," +
              "('Tóth Béla', 'Budapest, Fehérvári út 11', 'bela@hotmail.com', '+36 30/736 1211', '2012-06-01T23:12:55') ," +
              "('Kiss Mariann', 'Kecskemét, Arany János utca 23', 'cecil@gmail.com', '+36 30/122 7644', '2019-12-30T06:00:01') ," +
              "('Farkas Barna', 'Debrecen, Diófa utca 212', 'dezso@gmail.com', '+36 30/600 1234', '1999-11-21T16:55:00') ," +
              "('Tamás Zsombor', 'Eger, Nagyhegyi út 1', 'endre@gmail.com', '+36 30/001 5555', '2021-01-03T20:10:11') ," +
              "('Szekeres Renáta', 'Kaposvár, Akác utca 88', 'feri@hotmail.com', '+36 30/089 6534', '2020-02-11T21:21:21');";
      jdbcTemplate.update(sql);
   }
   
   private void fillArticleWithDummyData() {
      String sql = "INSERT INTO article (publicist_id, title, synopsys, text, created_at) " +
              "VALUES " +
              "('1', 'Legjobb fogyasztóteák', 'Hogyan segíteheti a tea a fogyást?', 'Citromfű tea: Serkenti a máj működését, emésztésjavító, puffadásgátló hatása is van, emellett felgyorsítja az anyagcsere-folyamataidat.', '2013-01-13T12:56:41')," +
              "('1', 'Remek hírek Ausztráliából', 'Tasmán ördög született', ' A Re:Wild és a WildArk közreműködésével megvalósított projekt most ünnepelhet: hét tasman ördög bébi született nemrégiben! Mivel erszényesekről van szó, a kicsinyek egyelőre valóban igen kicsinyek, s anyjuk erszényében laknak még egy ideig', '2013-04-01T05:58:17')," +
              "('2', 'A világ egyik legfélelmetesebb útja', 'Miért a Kishtwar Kailash az egyik legfélelmetesebb út?', 'A Himalájában kanyargó Kishtwar Kailash hegyi utat a világ egyik legfélelmetesebb útjának tartják. India Dzsammu és Kasmír államában található. Kishtwar-t és a 6451 méter magas Kishtwar Kailash alaptáborát köti össze. A keskeny út egyik oldalát magas sziklák, a másikat sok helyen több száz méter mély szakadékok szegélyezik.', '2013-11-01T00:09:30')," +
              "('2', 'Vörösre változott a Jangce', 'Még keresik az okot az érdekes eseményre', 'Kína leghosszabb folyója egy-egy szakaszon váratlanul paradicsomlé színűre változott. A szakemberek értetlenül állnak a jelenség előtt. Elsőként a délnyugaton fekvő Csungking városának lakói észlelték, hogy az „arany vízi útként” is emlegetett folyó hírnevén igencsak tekintélyes csorba esett. A beszámolók szerint a víz nemcsak Csungking közelében piroslik.', '2013-11-13T15:45:59')," +
              "('3', 'A Honda 1000 árvíztől sérült autót zúzott be Thaiföldön', 'Súlyos árvízkárok okán született döntés', 'A japán autógyártó Honda úgy döntött, hogy bezúzza azt az 1000 autót, amik a pusztító árvizek során rongálódtak meg Thaiföldön. A Honda így akarja biztosítani a vásárlókat, hogy a katasztrófában tönkrement autókat soha nem fogják eladni. Az autók többsége közepes méretű városi szedán, valamint ferdehátú Jazz és Brio autók. Az központi Ayutthaya tartományban található Honda telepen fog zajlani a selejtezési folyamat, mely várhatóan egy hónapon át fog tartani. Az autók Ayutthaya ipari részén, a Ronja ipari parkban készültek, 100 kilométerre Bangkoktól. A gyár egyike volt azoknak az épületeknek, amiket a legerősebb áradások öntöttek el', '2020-01-02T12:54:10')," +
              "('4', 'Sós tavak a Marson', 'A négy, sósvízű víztest növeli annak esélyét, hogy mikrobiális élet lehet a vörös bolygón.', 'Sósvízű tavak hálózatát fedezték fel a Mars felszíne alatt a déli-sarkvidéknél egy nagy földalatti tó mellett. Olasz kutatók, akik a Nature Astronomy című tudományos lapban számoltak be felfedezésükről, két évvel ezelőtt azonosították a nagyobbik tavat, amelyről szintén kibővítették ismereteiket. Az Európai Űrügynökség (ESA) Mars Express szondájának radarja által gyűjtött adatok segítségével több száz mérfölddel kiterjesztették a kutatási területet.', '2000-05-07T20:17:38')," +
              "('4', 'Solar Orbiter Napközelben', 'Először közelíti meg a Napot a Solar Orbiter európai űrszonda', 'Először halad el a Naphoz közel, mintegy 77 millió kilométeres távolságban a Solar Orbiter (SolO) európai űrszonda. A misszió feladata, hogy új információkkal segítse a Nap dinamikájának megértését. A perihélion, vagyis röppályájának a Naphoz legközelebb eső része a szondát a Vénusz és a Merkúr között vezeti el. A következő években ennél is közelebb kerül, akár 43 millió kilométerre is megközelíti a csillagot.', '2004-02-29T12:07:50')," +
              "('4', 'Mesterséges meteorzápor', 'Mesterséges meteorzáport hoz létre egy japán űrvállalat', 'A világon elsőként hoz létre mesterséges meteorzáport egy japán űrvállalat, amely egy apró műhold segítségével fogja előidézni a kutatási és szórakoztató célú látványos égi jelenséget. A tokiói székhelyű ALE Co. a tervek szerint január közepén indítja útnak a 60 centiméter széles, 80 centiméter magas és csaknem 65 kilogrammos műholdat a délnyugat-japán Kagosima prefektúrabeli Ucsinoura Űrközpontból.', '2021-06-07T13:00:52')," +
              "('4', 'Repülő Uber', 'Repülő taxit fejleszt az Uber a NASA közreműködésével', 'Pilóta nélküli repülő taxit fejleszt az Uber, mely az amerikai űrkutatási hivatallal együttműködve akarja kiépíteni új szolgáltatását. Az Uber szerint a Los Angeles-i repülőtér és a Staples Center sportcsarnok közötti másfélórás autóutat kevesebb mint 30 perc alatt lehetne megtenni a repülő és a hagyományos autók kombinációjával.', '2007-12-24T09:39:39')," +
              "('5', 'Hihetetlen erdőpusztulás', 'Hat másodpercenként tűnt el egy focipályányi érintetlen őserdő tavaly a Földön', 'Hat másodpercenként egy labdarúgópályányi őserdő tűnt el a Földön tavaly, a harmadik legtöbb a 2000-es évek kezdete óta – közölte kedden a World Resources Institute. Az intézet szerint 2019-ben 11,9 millió hektár trópusi esőerdő vált fakitermelés vagy tűzvész áldozatául, közte 3,8 millió hektárnyi érintetlen, az élővilág sokfélesége szempontjából különleges jelentőségű erdő. Csak 2016-ban és 2017-ben pusztult el a tavalyinál több érintetlen erdőség.', '2021-05-30T10:07:00')," +
              "('6', 'Sáskajárás Afrikában', 'Az utóbbi 25 év legpusztítóbb sáskajárása van Afrikában', 'Az utóbbi 25 év legszörnyűbb sáskajárása sújtja Kelet-Afrikát. Attól lehet tartani, hogy miután a sáskák elvonulnak, nyomukban éhínség következik. Az egyiptomi vándorsáskák (amelyeket egyszerűen sivatagi sáskáknak is neveznek) észak felől támadják Kenyát, Etiópiát, Szomáliát, Szudánt és Ugandát, és inváziójuk megállíthatatlannak látszik.', '2021-06-017T09:45:17')," +
              "('6', 'A halál jeges ujja', 'A megdöbbentő természeti jelenség a tengerekben', 'A kutatók már több mint 50 éve ismerik azt a különleges, tengerekben illetve óceánokban kialakuló jelenséget, amit sokszor csak >>a halál jeges ujjaként<< emlegetnek. Mivel rendkívül nehéz megörökíteni a sós vízben kialakuló, pusztító jégcsapot (angol nevén: brinicle), ezért először csak 2011-ben tudták lefilmezni. A halálos jégcsap lassan ereszkedik lefele az óceán felszínétől, egészen az aljáig, miközben gyakorlatilag mindent csapdába ejt, amihez hozzáér, és szépen lassan megfagyasztja az útjába kerülő élőlényeket. Amikor tehát eléri az óceán fenekét, nem áll meg, hanem tovább terjed. Percek alatt alatt akár több méteres területet elboríthat, eközben sorban pusztítja el az élőlényeket.', '2021-06-07T10:10:56');";
      jdbcTemplate.execute(sql);
   }
   
   private void fillReaderWithDummyData() {
      String sql = "INSERT INTO reader (username, email, created_at) " +
              "VALUES " +
              "('albinoCat', 'albinocat@gmail.com', '2021-01-11T15:55:38'), " +
              "('Béka_herceg', 'bbird@hotmail.com', '2020-07-11T16:40:13'), " +
              "('cop123', 'cop@gmail.com', '2019-07-11T06:45:50'), " +
              "('Chessmaster2000', 'debbie12@gmail.com', '2018-07-11T06:11:17'), " +
              "('elephantsAreCool', 'elephantFromHawaii@gmail.com', '2017-07-11T12:06:09');";
      jdbcTemplate.execute(sql);
   }
   
   private void fillCommentWithDummyData() {
      String sql = "INSERT INTO comment (reader_id, comment_text, article_id, created_at) " +
              "VALUES " +
              "('1', 'Ezt kipróbálom!', '1', '2021-03-03T12:02:12'), " +
              "('2', 'Szuper hír', '2', '2021-03-04T23:41:56'), " +
              "('3', 'Never thought about that this way', '3', '2021-03-10T05:50:11'), " +
              "('4', 'Szörnyű. Nekem is hondám van', '5', '2021-03-11T01:01:12'), " +
              "('5', 'Micsoda felfedezés', '6', '2021-03-11T16:54:25'), " +
              "('1', 'Milyen közel tud menni anélkül, hogy tönkremenne?', '7', '2021-03-12T13:20:44'), " +
              "('2', 'Ez aztán a jó ötlet..', '8', '2021-03-13T08:58:12'), " +
              "('3', 'Erre kiváncsi leszek!', '9', '2021-03-14T12:45:16'), " +
              "('4', 'Vigyázzunk a Földre!', '10', '2021-03-15T23:35:23'), " +
              "('5', 'Remélem erre nem jönnek a sáskák :(', '11', '2021-03-16T11:11:12'), " +
              "('1', 'Mi a helyzet a fekete teával?', '1', '2021-03-16T00:12:50'), " +
              "('2', 'Én inkább sportolok!', '1', '2021-03-16T12:14:15'), " +
              "('3', 'A tea egészséges!', '1', '2021-03-17T20:45:30'), " +
              "('4', 'Jó cikk, csak kicsit rövid, érdekelne több részlet', '1', '2021-03-18T10:20:30'), " +
              "('1', 'Az rengeteg autó', '5', '2021-03-19T10:21:32'), " +
              "('1', 'Korrekt döntés', '5', '2021-03-21T22:10:01'), " +
              "('2', 'Ezaz! Köszi az infót!', '2', '2021-03-22T16:12:12'), " +
              "('3', 'Én amúgy is félek a magasban, úgyhogy ezt kihagyom :D', '3', '2021-03-22T18:49:29'), " +
              "('4', 'Veszélyesnek hangzik', '9', '2021-03-23T06:45:51'), " +
              "('5', 'Félelmetes lehet', '11', '2021-03-24T12:12:12'), " +
              "('1', 'Ez mikor lesz kész?', '9', '2021-03-27T15:34:28');";
      jdbcTemplate.execute(sql);
      
   }
   
   private void fillRatingWithDummyData() {
      String sql = "INSERT INTO rating (reader_id, article_id, article_rating, created_at) " +
              "VALUES " +
              "('5', '1', 1, '2021-03-01T12:23:34'), " +
              "('4', '1', 2, '2021-03-02T23:15:37'), " +
              "('4', '3', '3', '2021-03-03T21:38:21'), " +
              "('3', '3', '4', '2021-03-04T10:50:34'), " +
              "('2', '5', '5', '2021-03-05T23:23:23'), " +
              "('2', '5', '2', '2021-03-06T11:20:45'), " +
              "('2', '7', '3', '2021-07-07T10:12:10'), " +
              "('3', '7', '4', '2021-03-08T17:28:00'), " +
              "('1', '9', '5', '2021-03-09T23:23:11'), " +
              "('1', '9', '1', '2021-02-10T12:11:12'), " +
              "('1', '11', '3', '2021-03-11T06:00:02'), " +
              "('5', '11', '4', '2020-03-12T23:17:46'), " +
              "('3', '11', '5', '2021-12-13T01:23:47'), " +
              "('4', '11', '1', '2021-03-13T11:17:43'), " +
              "('4', '11', '2', '2021-03-11T10:19:12'), " +
              "('4', '11', '4', '2021-11-14T12:12:12'), " +
              "('4', '1', '1', '2021-03-15T21:21:21'), " +
              "('4', '5', '1', '2021-01-16T10:10:46'), " +
              "('4', '4', '1', '2021-05-16T12:05:48'), " +
              "('4', '6', '5', '2021-06-18T14:45:39'), " +
              "('4', '12', '2', '2021-06-07T21:59:15'), " +
              "('4', '12', '5', '2021-06-07T22:59:15'), " +
              "('4', '8', '2', '2021-09-22T06:59:59');";
      jdbcTemplate.execute(sql);
      
   }
   
   private void fillKeywordWithDummyData() {
      String sql = "INSERT INTO keyword (keyword_name) " +
              "VALUES " +
              "('tea'), " +
              "('tasmán ördög'), " +
              "('magasság'), " +
              "('jangce'), " +
              "('ázsia'), " +
              "('dráma'), " +
              "('tudomány'), " +
              "('sáska'), " +
              "('állatok'), " +
              "('víz'), " +
              "('űr'), " +
              "('repülő'), " +
              "('tűz'), " +
              "('katasztrófa'), " +
              "('érdekesség'), " +
              "('autó'), " +
              "('természet'), " +
              "('élet');";
      jdbcTemplate.execute(sql);
      
   }
   
   private void fillArticleKeywordWithDummyData() {
      String sql = "INSERT INTO article_keyword (article_id, keyword_id) " +
              "VALUES " +
              "(1,1), " +
              "(1,17), " +
              "(2,2), " +
              "(2,9), " +
              "(2,15), " +
              "(2,17), " +
              "(2,18), " +
              "(3,3), " +
              "(3,5), " +
              "(3,15), " +
              "(4,4), " +
              "(4,5), " +
              "(4,10), " +
              "(4,17), " +
              "(5,5), " +
              "(5,6), " +
              "(5,10), " +
              "(5,14), " +
              "(5,16), " +
              "(5,17), " +
              "(6,10), " +
              "(6,11), " +
              "(6,15), " +
              "(6,18), " +
              "(7,11), " +
              "(7,13), " +
              "(8,5), " +
              "(8,7), " +
              "(8,11), " +
              "(8,15), " +
              "(9,7), " +
              "(9,12), " +
              "(9,16), " +
              "(10,6), " +
              "(10,13), " +
              "(10,14), " +
              "(10,17), " +
              "(11,9), " +
              "(11,8), " +
              "(11,14), " +
              "(11,12), " +
              "(11,17), " +
              "(12,7), " +
              "(12,10), " +
              "(12,15), " +
              "(12,17);";
      jdbcTemplate.execute(sql);
   }
   
}
