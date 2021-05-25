package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Comment;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

@JdbcTest
class CommentRepositoryTest {
   
   @Autowired
   private JdbcTemplate jdbcTemplate;
   
   private CommentRepository repository;
   
   private final LocalDateTime CREATED_AT = LocalDateTime.of(2021, 11, 13, 10, 11);
   
   @BeforeEach
   void setUp() {
      createReaderTable();
      createArticleTable();
      createCommentTable();
      //createRatingTable();
      repository = new CommentRepository(jdbcTemplate);
   }
   
   
   @AfterEach
   void tearDown() {
      jdbcTemplate.execute("DROP TABLE IF EXISTS reader;");
      jdbcTemplate.execute("DROP TABLE IF EXISTS comment");
      jdbcTemplate.execute("DROP TABLE IF EXISTS article");
      //jdbcTemplate.execute("DROP TABLE IF EXISTS rating");
   }
   
   @Test
   void saveComment() {
      repository.saveComment(new CommentDto("Joe Doe", "comment", CREATED_AT, 1));
      String sql = "SELECT * FROM comment";
      Comment comment = jdbcTemplate.queryForObject(sql, (rs, row) -> {
         Comment tempComment = new Comment();
         tempComment.setId(rs.getInt("id"));
         return tempComment;
         //TODO - need to modify CommentRepository.saveComment table changed!
      });
   }
   void createReaderTable() {
      String sql = "CREATE TABLE reader(" +
              "id INT NOT NULL AUTO_INCREMENT," +
              "username VARCHAR(200)," +
              "email varchar(200)," +
              "created_at datetime," +
              "modified_at datetime," +
              "status TINYINT DEFAULT 1, " +
              "PRIMARY KEY (id));";
      
      jdbcTemplate.execute(sql);
   }
   
   void createCommentTable() {
      String sql = "CREATE TABLE comment(" +
              "id INT NOT NULL AUTO_INCREMENT," +
              "reader_id INT," +
              "comment_text VARCHAR(200)," +
              "article_id INT," +
              "created_at DATETIME," +
              "modified_at DATETIME," +
              "status TINYINT DEFAULT 1," +
              "PRIMARY KEY (id)," +
              "FOREIGN KEY (reader_id) REFERENCES reader (id)," +
              "FOREIGN KEY (article_id) REFERENCES article (id));";
      
      jdbcTemplate.execute(sql);
      
   }
   
   void createRatingTable() {
      String sql = "CREATE TABLE rating(" +
              "reader_id INT," +
              "article_id INT," +
              "article_rating TINYINT," +
              "created_at DATETIME," +
              "modified_at DATETIME " +
              "FOREIGN KEY (article_id) REFERENCES article (id)," +
              "FOREIGN KEY (reader_id) REFERENCES reader (id)" +
              ");";
      
      jdbcTemplate.execute(sql);
      
   }
   
   void createArticleTable() {
      String sql = "CREATE TABLE article(" +
              "id INT NOT NULL AUTO_INCREMENT," +
              "publicist_id INT," +
              "title VARCHAR(200)," +
              "synopsys VARCHAR(200)," +
              "text VARCHAR(200)," +
              "created_at DATETIME," +
              "modified_at DATETIME," +
              "deployed_at DATETIME," +
              "status TINYINT DEFAULT 1" +
              "PRIMARY KEY (id)," +
              "FOREIGN KEY (publicist_id) REFERENCES publicist (id)" +
              ");";
      
      jdbcTemplate.execute(sql);
      
   }
   
}