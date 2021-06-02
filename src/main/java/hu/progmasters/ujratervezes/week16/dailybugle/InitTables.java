package hu.progmasters.ujratervezes.week16.dailybugle;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class InitTables implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public InitTables(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(InitTables.class);
    }

    @Override
    public void run(String... args) throws Exception {
        dropTables();
        createPublicist();
        createArticle();
        createReader();
        createComment();
        createRating();
        createKeyword();
        createArticleKeyword();
        fillTablesWithDummyData();
    }

    private void dropTables() {
        setForeignKeyCheck0();
        dropPublicist();
        dropArticle();
        dropReader();
        dropComment();
        dropRating();
        dropKeyword();
        dropArticleKeyword();
        setForeignKeyCheck1();
    }

    private void createPublicist() {
        String sql = "CREATE TABLE IF NOT EXISTS `publicist` (" +
                "  `id` int NOT NULL AUTO_INCREMENT," +
                "  `name` varchar(50) DEFAULT NULL," +
                "  `address` varchar(100) DEFAULT NULL," +
                "  `email` varchar(50) DEFAULT NULL," +
                "  `phone` varchar(20) DEFAULT NULL," +
                "  `status` tinyint(1) DEFAULT '1'," +
                "  `created_at` datetime DEFAULT NULL," +
                "  `modified_at` datetime DEFAULT NULL," +
                "  PRIMARY KEY (`id`)" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void createArticle() {
        String sql = "CREATE TABLE IF NOT EXISTS `article` (" +
                "  `id` int NOT NULL AUTO_INCREMENT," +
                "  `publicist_id` int NOT NULL," +
                "  `title` varchar(45) DEFAULT NULL," +
                "  `synopsys` varchar(145) DEFAULT NULL," +
                "  `text` varchar(500) DEFAULT NULL," +
                "  `status` tinyint DEFAULT '1'," +
                "  `created_at` datetime DEFAULT NULL," +
                "  `modified_at` datetime DEFAULT NULL," +
                "  `deployed_at` datetime DEFAULT NULL," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `title_UNIQUE` (`title`)," +
                "  KEY `publicist_id_idx` (`publicist_id`)," +
                "  CONSTRAINT `publicist_id` FOREIGN KEY (`publicist_id`) REFERENCES `publicist` (`id`)" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void createReader() {
        String sql = "CREATE TABLE IF NOT EXISTS `reader` (" +
                "  `id` int NOT NULL AUTO_INCREMENT," +
                "  `username` varchar(45) DEFAULT NULL," +
                "  `email` varchar(45) DEFAULT NULL," +
                "  `created_at` datetime DEFAULT NULL," +
                "  `modified_at` datetime DEFAULT NULL," +
                "  `status` tinyint DEFAULT '1'," +
                "  PRIMARY KEY (`id`)" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void createComment() {
        String sql = "CREATE TABLE IF NOT EXISTS `comment` (" +
                "  `id` int NOT NULL AUTO_INCREMENT," +
                "  `reader_id` int DEFAULT NULL," +
                "  `comment_text` varchar(100) DEFAULT NULL," +
                "  `article_id` int DEFAULT NULL," +
                "  `created_at` datetime DEFAULT NULL," +
                "  `modified_at` datetime DEFAULT NULL," +
                "  `status` tinyint DEFAULT '1'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `article_idx` (`article_id`)," +
                "  KEY `reader_idx` (`reader_id`)," +
                "  CONSTRAINT `article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)," +
                "  CONSTRAINT `reader` FOREIGN KEY (`reader_id`) REFERENCES `reader` (`id`)" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void createRating() {
        String sql = "CREATE TABLE IF NOT EXISTS `rating` (" +
                "  `reader_id` int DEFAULT NULL," +
                "  `article_id` int DEFAULT NULL," +
                "  `article_rating` tinyint DEFAULT NULL," +
                "  `created_at` datetime DEFAULT NULL," +
                "  `modified_at` datetime DEFAULT NULL," +
                "  KEY `id_idx` (`article_id`)," +
                "  KEY `reader_idx` (`reader_id`)," +
                "  CONSTRAINT `articleFK` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)," +
                "  CONSTRAINT `rating_reader_FK` FOREIGN KEY (`reader_id`) REFERENCES `reader` (`id`)" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void createKeyword() {
        String sql = "CREATE TABLE IF NOT EXISTS `keyword` (" +
                "  `id` int NOT NULL AUTO_INCREMENT," +
                "  `keyword_name` varchar(45) DEFAULT NULL," +
                "  PRIMARY KEY (`id`)" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void createArticleKeyword() {
        String sql = "CREATE TABLE IF NOT EXISTS `article_keyword` (" +
                "  `article_id` int DEFAULT NULL," +
                "  `keyword_id` int DEFAULT NULL," +
                "  KEY `keyword key_idx` (`keyword_id`)," +
                "  KEY `article key` (`article_id`)," +
                "  CONSTRAINT `article key` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE," +
                "  CONSTRAINT `keyword key` FOREIGN KEY (`keyword_id`) REFERENCES `keyword` (`id`) ON DELETE CASCADE" +
                ");";
        jdbcTemplate.execute(sql);
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
                "('Asztalos Aladár', 'Abós, Autó utca 2', 'aladar@a.hu', '+36 30/374 8838', '2012-12-12') ," +
                "('Boltos Béla', 'Budapest, Borozó út 11', 'bela@b.hu', '+36 30/736 1211', '2012-06-01') ," +
                "('Cincogi Cecil', 'Csorgó, Cuha utca 23', 'cecil@c.hu', '+36 30/122 7644', '2019-12-30') ," +
                "('Deres Dezső', 'Debrecen, Dolgos utca 212', 'dezso@d.hu', '+36 30/600 1234', '1999-11-21') ," +
                "('Elemi Endre', 'Eger, Egér út 1', 'endre@e.hu', '+36 30/001 5555', '2021-01-03') ," +
                "('Foltos Ferenc', 'Fót, Fehér utca 88', 'feri@f.hu', '+36 30/089 6534', '2020-02-11');";
        jdbcTemplate.update(sql);
    }

    private void fillArticleWithDummyData() {
        String sql = "INSERT INTO article (publicist_id, title, synopsys, text, created_at) " +
                "VALUES " +
                "('1', 'First title', 'About my first title', 'This is my first article, yay!', '2013-01-13')," +
                "('1', 'Aprils fools', 'How I got fooled', 'I gave an unknown man $20 to buy me a Sprite, but he never came back', '2013-04-01')," +
                "('2', 'Interesting', 'Thinking about things', 'I saw a thing that was interesting. Constantly thinking about it', '2013-11-01')," +
                "('2', 'Light', 'I am unable see', 'So I looked into the sun for about an hour, and now I am unable see. Wonder how long this will last..', '2013-11-13')," +
                "('3', '2020', 'Positive outlooks', 'This year is gonna be the greatest believe me. That chinese thing will blow over in a week!', '2020-01-02')," +
                "('4', 'Long cats', 'Why I like them', 'I like long cats because sometimes when you lift them they are still on the ground!', '2000-05-07')," +
                "('4', 'Leap years', 'Usually this does not happen', 'Only every fourth year. Ha! ', '2004-02-29')," +
                "('4', 'Cosmos', 'Why chocolate is dark', 'It is pretty obvious, everything is originated from space, so is chocolate. Space is dark. Bamm!', '2003-10-30')," +
                "('4', 'Christmas', 'A tale for little ones', 'Santa is running everywhere really fast. Drops a cookie and cries. Then runs some more. FIN', '2007-12-24')," +
                "('5', 'Life story part 1', 'First part of my long life story', 'I was born as a baby when I was young. I am 30 now. To be continued..', '2021-01-03')," +
                "('6', 'Washing machines', 'How to clean them', 'Maybe use a washing-machine-washing machine, I have no idea, I never wash. Wait you should??', '2020-03-24')," +
                "('6', 'Washing machines 2', 'Why I bought one', 'Since you HAVE to wash your clothes from time to time I was forced to buy one:(.', '2021-07-11');";
        jdbcTemplate.execute(sql);

    }

    private void fillReaderWithDummyData() {
        String sql = "INSERT INTO reader (username, email, created_at) " +
                "VALUES " +
                "('albinoCat', 'albinocat@a.hu', '2021-07-11'), " +
                "('birdWithABeard', 'bbird@b.hu', '2020-07-11'), " +
                "('cop123', 'cop@c.hu', '2019-07-11'), " +
                "('durianForKing', 'debbie12@d.hu', '2018-07-11'), " +
                "('elephantsAreCool', 'elephantFromHawaii@e.hu', '2017-07-11');";
        jdbcTemplate.execute(sql);

    }

    private void fillCommentWithDummyData() {
        String sql = "INSERT INTO comment (reader_id, comment_text, article_id, created_at) " +
                "VALUES " +
                "('1', 'Nice Article!', '1', '2021-03-03'), " +
                "('2', 'I like it', '2', '2021-03-04'), " +
                "('3', 'Never thought about that this way', '3', '2021-03-10'), " +
                "('4', 'lol', '5', '2021-03-11'), " +
                "('5', 'I will try this', '6', '2021-03-11'), " +
                "('1', 'kek', '7', '2021-03-12'), " +
                "('2', 'Just no', '8', '2021-03-13'), " +
                "('3', 'Whatever', '9', '2021-03-14'), " +
                "('4', 'Hell yeah!', '10', '2021-03-15'), " +
                "('5', 'This is exactly my life', '11', '2021-03-16'), " +
                "('1', 'Interesting, thanks', '1', '2021-03-16'), " +
                "('2', 'Anyone plays pokemon?', '1', '2021-03-16'), " +
                "('3', 'That might be important later', '1', '2021-03-17'), " +
                "('4', 'Like new shoes!', '1', '2021-03-18'), " +
                "('1', 'How to comment?', '5', '2021-03-19'), " +
                "('1', 'How to delete comment?', '5', '2021-03-21'), " +
                "('2', 'What about giraffes?', '2', '2021-03-22'), " +
                "('3', 'I am great', '3', '2021-03-22'), " +
                "('4', 'Have a lovely day!', '9', '2021-03-23'), " +
                "('5', 'I would love to go a zoo!', '11', '2021-03-24'), " +
                "('1', 'What does this mean for us?', '9', '2021-03-27');";
        jdbcTemplate.execute(sql);

    }

    private void fillRatingWithDummyData() {
        String sql = "INSERT INTO rating (reader_id, article_id, article_rating, created_at) " +
                "VALUES " +
                "('5', '1', 1, '2021-03-01'), " +
                "('4', '1', 2, '2021-03-02'), " +
                "('4', '3', '3', '2021-03-03'), " +
                "('3', '3', '4', '2021-03-04'), " +
                "('2', '5', '5', '2021-03-05'), " +
                "('2', '5', '2', '2021-03-06'), " +
                "('2', '7', '3', '2021-03-07'), " +
                "('3', '7', '4', '2021-03-08'), " +
                "('1', '9', '5', '2021-03-09'), " +
                "('1', '9', '1', '2021-03-10'), " +
                "('1', '11', '3', '2021-03-11'), " +
                "('5', '11', '4', '2021-03-12'), " +
                "('3', '11', '5', '2021-03-13'), " +
                "('4', '11', '1', '2021-03-13'), " +
                "('4', '11', '2', '2021-03-11'), " +
                "('4', '11', '4', '2021-03-14'), " +
                "('4', '1', '1', '2021-03-15'), " +
                "('4', '5', '1', '2021-03-16'), " +
                "('4', '4', '1', '2021-03-16'), " +
                "('4', '6', '5', '2021-03-18'), " +
                "('4', '12', '2', '2021-03-27'), " +
                "('4', '8', '2', '2021-03-22');";
        jdbcTemplate.execute(sql);

    }

    private void fillKeywordWithDummyData() {
        String sql = "INSERT INTO keyword (keyword_name) " +
                "VALUES " +
                "('modern'), " +
                "('régi'), " +
                "('vicces'), " +
                "('zenés'), " +
                "('politika'), " +
                "('dráma'), " +
                "('vígjáték'), " +
                "('háború'), " +
                "('állatok'), " +
                "('sejtkutatás'), " +
                "('étel'), " +
                "('jármű'), " +
                "('sport'), " +
                "('humor'), " +
                "('kérdés'), " +
                "('lecke'), " +
                "('sakk'), " +
                "('lélektan');";
        jdbcTemplate.execute(sql);

    }

    private void fillArticleKeywordWithDummyData() {
        String sql = "INSERT INTO article_keyword (article_id, keyword_id) " +
                "VALUES " +
                "(1,1), " +
                "(1,2), " +
                "(1,3), " +
                "(1,4), " +
                "(1,5), " +
                "(2,6), " +
                "(2,7), " +
                "(3,8), " +
                "(4,9), " +
                "(4,10), " +
                "(4,11), " +
                "(5,12), " +
                "(5,13), " +
                "(6,14), " +
                "(6,15), " +
                "(8,16), " +
                "(8,17), " +
                "(8,18), " +
                "(9,17), " +
                "(10,8), " +
                "(10,15), " +
                "(10,1), " +
                "(10,5), " +
                "(11,6), " +
                "(12,4), " +
                "(12,7);";
        jdbcTemplate.execute(sql);
    }

    private void dropPublicist() {
        String sql = "DROP TABLE IF EXISTS publicist;";
        jdbcTemplate.execute(sql);
    }

    private void dropArticle() {
        String sql = "DROP TABLE IF EXISTS article;";
        jdbcTemplate.execute(sql);
    }

    private void dropReader() {
        String sql = "DROP TABLE IF EXISTS reader;";
        jdbcTemplate.execute(sql);
    }

    private void dropComment() {
        String sql = "DROP TABLE IF EXISTS comment;";
        jdbcTemplate.execute(sql);
    }

    private void dropRating() {
        String sql = "DROP TABLE IF EXISTS rating;";
        jdbcTemplate.execute(sql);
    }

    private void dropKeyword() {
        String sql = "DROP TABLE IF EXISTS keyword;";
        jdbcTemplate.execute(sql);
    }

    private void dropArticleKeyword() {
        String sql = "DROP TABLE IF EXISTS article_keyword;";
        jdbcTemplate.execute(sql);
    }

    private void setForeignKeyCheck0() {
        String sql = "SET FOREIGN_KEY_CHECKS = 0;";
        jdbcTemplate.execute(sql);
    }

    private void setForeignKeyCheck1() {
        String sql = "SET FOREIGN_KEY_CHECKS = 1;";
        jdbcTemplate.execute(sql);
    }

}
