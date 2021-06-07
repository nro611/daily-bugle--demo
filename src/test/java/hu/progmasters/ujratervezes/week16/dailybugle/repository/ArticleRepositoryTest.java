package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.CreateTables;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class ArticleRepositoryTest {
   
   @Autowired
   private JdbcTemplate jdbcTemplate;
   private CreateTables createTables;
   private DummyData dummyData;
   
   private ArticleRepository repository;
   private ArticleListMapper mapper = new ArticleListMapper();
   
   private final LocalDateTime CREATED_AT = LocalDateTime.of(2021, 11, 13, 10, 11);
   
   @BeforeEach
   void setUp() {
      
      dummyData = new DummyData(jdbcTemplate);
      createTables = new CreateTables(jdbcTemplate);
      createTables.createAllTables();
      dummyData.fillTablesWithDummyData();
      repository = new ArticleRepository(jdbcTemplate, mapper);
   }
   
   
   @AfterEach
   void tearDown() {
      createTables.dropTables();
   }
   
   @Test
   void getArticles() {
      repository.getArticles();
   }
   
   @Test
   void getTopArticles() {
   }
   
   @Test
   void getTopFreshArticles() {
   }
   
   @Test
   void getArticle() {
   }
   
   @Test
   void saveRating() {
   }
   
   @Test
   void updateRating() {
   }
   
   @Test
   void getRatingWithUserAndArticle() {
   }
   
   @Test
   void updateArticle() {
   }
   
   @Test
   void deleteArticle() {
   }
   
   @Test
   void saveArticle() {
   }
   
   @Test
   void getKeywords() {
   }
   
   @Test
   void saveKeyword() {
   }
   
   @Test
   void getKeywordIds() {
   }
   
   @Test
   void getArticleId() {
   }
   
   @Test
   void saveArticleKeyword() {
   }
   
   @Test
   void getKeywordsForArticle() {
   }
   
   @Test
   void removeKeywords() {
   }
   
   @Test
   void removeKeyword() {
   }
   
   @Test
   void removeArticleKeywords() {
   }
   
   @Test
   void containsKeyword() {
   }
}