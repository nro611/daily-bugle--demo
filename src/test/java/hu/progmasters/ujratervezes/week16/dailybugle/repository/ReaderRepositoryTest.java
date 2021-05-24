package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Reader;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderProfileDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
class ReaderRepositoryTest {
   
   @Autowired
   private JdbcTemplate jdbcTemplate;
   
   private ReaderRepository repository;
   
   private final LocalDateTime CREATED_AT = LocalDateTime.of(2021, 11, 13, 10, 11);
   private final LocalDateTime MODIFIED_AT = LocalDateTime.of(2021, 12, 13, 10, 11);
   
   
   @BeforeEach
   void setUp() {
      createReaderTable();
      createCommentTable();
      createArticleTable();
      createRatingTable();
      repository = new ReaderRepository(jdbcTemplate);
   }
   
   
   @AfterEach
   void tearDown() {
      jdbcTemplate.execute("DROP TABLE IF EXISTS reader;");
      jdbcTemplate.execute("DROP TABLE IF EXISTS comment");
      jdbcTemplate.execute("DROP TABLE IF EXISTS article");
      jdbcTemplate.execute("DROP TABLE IF EXISTS rating");
   }
   
   @Test
   void getReaders() {
      putReader();
      List<Reader> readerList = repository.getReaders();
      
      assertEquals(1, readerList.size());
      assertEquals(1, readerList.get(0).getId());
      assertEquals("Joe", readerList.get(0).getUserName());
      assertEquals("joe@mail.com", readerList.get(0).getEmail());
   }
   
   @Test
   void getReader() {
      putReader();
      ReaderProfileDto readerProfileDto = repository.getReader(1);
      
      assertEquals("Joe", readerProfileDto.getName());
      assertEquals("joe@mail.com", readerProfileDto.getEmail());
      assertEquals(0, readerProfileDto.getCommentedArticles().size());
      assertEquals(0, readerProfileDto.getRatedArticles().size());
   }
   
   @Test
   void saveReader() {
      ReaderDto readerDto = new ReaderDto();
      readerDto.setUserName("Joe");
      readerDto.setEmail("joe@mail.com");
      repository.saveReader(readerDto, CREATED_AT);
      ReaderProfileDto readerProfileDto = repository.getReader(1);
      
      assertEquals("Joe", readerProfileDto.getName());
      assertEquals("joe@mail.com", readerProfileDto.getEmail());
      assertEquals(0, readerProfileDto.getCommentedArticles().size());
      assertEquals(0, readerProfileDto.getRatedArticles().size());
   }
   
   @Test
   void updateReader() {
      putReader();
      ReaderDto readerDto = new ReaderDto();
      readerDto.setUserName("Joe Doe");
      readerDto.setEmail("joe@mail.hu");
      repository.updateReader(readerDto, 1, MODIFIED_AT);
      ReaderProfileDto readerProfileDto = repository.getReader(1);
      
      assertEquals("Joe Doe", readerProfileDto.getName());
      assertEquals("joe@mail.hu", readerProfileDto.getEmail());
      assertEquals(0, readerProfileDto.getCommentedArticles().size());
      assertEquals(0, readerProfileDto.getRatedArticles().size());
   }
   
   void createReaderTable() {
      String sql = "CREATE TABLE reader(" +
              "id int primary key auto_increment," +
              "username varchar(200)," +
              "email varchar(200)," +
              "created_at datetime," +
              "modified_at datetime," +
              "status tinyint(1));";
      
      jdbcTemplate.execute(sql);
   }
   
   void createCommentTable() {
      String sql = "CREATE TABLE comment(" +
              "id int primary key auto_increment," +
              "reader_id int," +
              "comment_text varchar(200)," +
              "article_id int," +
              "created_at datetime," +
              "modified_at datetime," +
              "status tinyint(1));";
      
      jdbcTemplate.execute(sql);
      
   }
   
   void createRatingTable() {
      String sql = "CREATE TABLE rating(" +
              "reader_id int," +
              "article_id int," +
              "article_rating tinyint," +
              "created_at datetime," +
              "modified_at datetime);";
      
      jdbcTemplate.execute(sql);
      
   }
   
   void createArticleTable() {
      String sql = "CREATE TABLE article(" +
              "id int primary key auto_increment," +
              "publicist_id int," +
              "title varchar(200)," +
              "synopsys varchar(200)," +
              "text varchar(200)," +
              "created_at datetime," +
              "modified_at datetime," +
              "deployed_at datetime," +
              "status tinyint(1));";
      
      jdbcTemplate.execute(sql);
      
   }
   
   void putReader() {
      String sql = "INSERT INTO reader(username, email, created_at, modified_at, status) VALUES(?, ?, ?, ?, ?);";
      jdbcTemplate.update(sql, "Joe", "joe@mail.com", CREATED_AT, MODIFIED_AT, 1);
   }
}
