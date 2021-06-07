package hu.progmasters.ujratervezes.week16.dailybugle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

//@Component
@SpringBootApplication
public class CreateTables {

   private JdbcTemplate jdbcTemplate;

   @Autowired
   public CreateTables(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   public void createAllTables() {
      dropTables();
      createPublicist();
      createArticle();
      createReader();
      createComment();
      createRating();
      createKeyword();
      createArticleKeyword();
   }
   
   public void dropTables() {
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
   
   public void createPublicist() {
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
   
   public void createArticle() {
      String sql = "CREATE TABLE IF NOT EXISTS `article` (" +
              "  `id` int NOT NULL AUTO_INCREMENT," +
              "  `publicist_id` int NOT NULL," +
              "  `title` varchar(200) DEFAULT NULL," +
              "  `synopsys` varchar(500) DEFAULT NULL," +
              "  `text` varchar(1500) DEFAULT NULL," +
              "  `status` tinyint DEFAULT '1'," +
              "  `created_at` datetime DEFAULT NULL," +
              "  `modified_at` datetime DEFAULT NULL," +
              "  `deployed_at` datetime DEFAULT NULL," +
              "  PRIMARY KEY (`id`)," +
              "  UNIQUE KEY `title_UNIQUE` (`title`)," +
//              "  KEY `publicist_id_idx` (`publicist_id`)," +
              "  CONSTRAINT `publicist_id` FOREIGN KEY (`publicist_id`) REFERENCES `publicist` (`id`)" +
              ");";
      jdbcTemplate.execute(sql);
   }
   
   public void createReader() {
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
   
   public void createComment() {
      String sql = "CREATE TABLE IF NOT EXISTS `comment` (" +
              "  `id` int NOT NULL AUTO_INCREMENT," +
              "  `reader_id` int DEFAULT NULL," +
              "  `comment_text` varchar(100) DEFAULT NULL," +
              "  `article_id` int DEFAULT NULL," +
              "  `created_at` datetime DEFAULT NULL," +
              "  `modified_at` datetime DEFAULT NULL," +
              "  `status` tinyint DEFAULT '1'," +
              "  PRIMARY KEY (`id`)," +
/*
              "  KEY `article_idx` (`article_id`)," +
              "  KEY `reader_idx` (`reader_id`)," +
*/
              "  CONSTRAINT `article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)," +
              "  CONSTRAINT `reader` FOREIGN KEY (`reader_id`) REFERENCES `reader` (`id`)" +
              ");";
      jdbcTemplate.execute(sql);
   }
   
   public void createRating() {
      String sql = "CREATE TABLE IF NOT EXISTS `rating` (" +
              "  `reader_id` int DEFAULT NULL," +
              "  `article_id` int DEFAULT NULL," +
              "  `article_rating` tinyint DEFAULT NULL," +
              "  `created_at` datetime DEFAULT NULL," +
              "  `modified_at` datetime DEFAULT NULL," +
/*
              "  KEY `id_idx` (`article_id`)," +
              "  KEY `reader_idx` (`reader_id`)," +
*/
              "  CONSTRAINT `articleFK` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)," +
              "  CONSTRAINT `rating_reader_FK` FOREIGN KEY (`reader_id`) REFERENCES `reader` (`id`)" +
              ");";
      jdbcTemplate.execute(sql);
   }
   
   public void createKeyword() {
      String sql = "CREATE TABLE IF NOT EXISTS `keyword` (" +
              "  `id` int NOT NULL AUTO_INCREMENT," +
              "  `keyword_name` varchar(45) DEFAULT NULL," +
              "  PRIMARY KEY (`id`)" +
              ");";
      jdbcTemplate.execute(sql);
   }
   
   public void createArticleKeyword() {
      String sql = "CREATE TABLE IF NOT EXISTS `article_keyword` (" +
              "  `article_id` int DEFAULT NULL," +
              "  `keyword_id` int DEFAULT NULL," +
/*
              "  KEY `keyword key_idx` (`keyword_id`)," +
              "  KEY `article key` (`article_id`)," +
*/
              "  CONSTRAINT `article key` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE," +
              "  CONSTRAINT `keyword key` FOREIGN KEY (`keyword_id`) REFERENCES `keyword` (`id`) ON DELETE CASCADE" +
              ");";
      jdbcTemplate.execute(sql);
   }
   
   
   public void dropPublicist() {
      String sql = "DROP TABLE IF EXISTS publicist;";
      jdbcTemplate.execute(sql);
   }
   
   public void dropArticle() {
      String sql = "DROP TABLE IF EXISTS article;";
      jdbcTemplate.execute(sql);
   }
   
   public void dropReader() {
      String sql = "DROP TABLE IF EXISTS reader;";
      jdbcTemplate.execute(sql);
   }
   
   public void dropComment() {
      String sql = "DROP TABLE IF EXISTS comment;";
      jdbcTemplate.execute(sql);
   }
   
   public void dropRating() {
      String sql = "DROP TABLE IF EXISTS rating;";
      jdbcTemplate.execute(sql);
   }
   
   public void dropKeyword() {
      String sql = "DROP TABLE IF EXISTS keyword;";
      jdbcTemplate.execute(sql);
   }
   
   public void dropArticleKeyword() {
      String sql = "DROP TABLE IF EXISTS article_keyword;";
      jdbcTemplate.execute(sql);
   }
   
   public void setForeignKeyCheck0() {
      String sql = "SET FOREIGN_KEY_CHECKS = 0;";
      jdbcTemplate.execute(sql);
   }
   
   public void setForeignKeyCheck1() {
      String sql = "SET FOREIGN_KEY_CHECKS = 1;";
      jdbcTemplate.execute(sql);
   }
   
}
