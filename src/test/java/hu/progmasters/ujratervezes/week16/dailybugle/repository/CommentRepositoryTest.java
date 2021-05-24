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

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class CommentRepositoryTest {
   
   @Autowired
   private JdbcTemplate jdbcTemplate;
   
   private CommentRepository repository;
   
   private final LocalDateTime CREATED_AT = LocalDateTime.of(2021, 11, 13, 10, 11);
   
   @BeforeEach
   void setUp() {
      createReaderTable();
      createCommentTable();
      createArticleTable();
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
   
}