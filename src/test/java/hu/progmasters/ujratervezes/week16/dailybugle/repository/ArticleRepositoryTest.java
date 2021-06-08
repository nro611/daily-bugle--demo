package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.CreateTables;
import hu.progmasters.ujratervezes.week16.dailybugle.domain.Article;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
   @DisplayName("Get articles to list")
   void testGetArticles() {
      List<ArticleListDto> articles = repository.getArticles();

      assertEquals(12, articles.size());
   }

   @Test
   @DisplayName("Get fresh articles to list")
   void testGetFreshArticles() {
      List<ArticleListDto> articles = repository.getFreshArticles();

      assertEquals(10, articles.size());

   }

   @Test
   @DisplayName("Get top articles to list")
   void testGetTopArticles() {
      List<ArticleListDto> articles = repository.getTopArticles();

      assertEquals(10, articles.size());

   }
   
/*

   ADDDATE method broke h2database

   @Test
   @DisplayName("Get top fresh articles")
   void getTopFreshArticles() {
      List<ArticleListDto> articles = repository.getTopFreshArticles();
   
      assertEquals(12, articles.size());
   
   }
*/

   @Test
   @DisplayName("Get article by ID")
   void testGetArticle() {
      Article article = repository.getArticle(1);

      assertEquals("Legjobb fogyasztóteák", article.getTitle());
      assertEquals(1, article.getPublicistId());
      assertEquals("Kovács Aladár", article.getPublicistName());

   }

   @Test
   @DisplayName("Update article by ID")
   void testUpdateArticle() {
      ArticleDto articleDto = new ArticleDto();
      articleDto.setTitle("title");
      repository.updateArticle(articleDto, 1, CREATED_AT);
      Article article = repository.getArticle(1);

      assertEquals("title", article.getTitle());

   }

   @Test
   void testSaveArticle() {
   }

   @Test
   void testGetKeywords() {
   }

   @Test
   void testSaveKeyword() {
   }

   @Test
   void testGetKeywordIds() {
   }

   @Test
   void testGetArticleId() {
   }

   @Test
   void testSaveArticleKeyword() {
   }

   @Test
   void testGetKeywordsForArticle() {
   }

   @Test
   void testRemoveKeywords() {
   }

   @Test
   void testRemoveKeyword() {
   }

   @Test
   void testRemoveArticleKeywords() {
   }

   @Test
   void testContainsKeyword() {
   }
}