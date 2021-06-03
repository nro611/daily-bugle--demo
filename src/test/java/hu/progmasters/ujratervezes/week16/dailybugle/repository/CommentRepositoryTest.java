package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.CreateTables;
import hu.progmasters.ujratervezes.week16.dailybugle.domain.Comment;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
class CommentRepositoryTest {
   
   @Autowired
   private JdbcTemplate jdbcTemplate;
   private CreateTables createTables;
   private DummyData dummyData;
   
   private CommentRepository repository;
   
   private final LocalDateTime CREATED_AT = LocalDateTime.of(2021, 11, 13, 10, 11);
   
   @BeforeEach
   void setUp() {
      
      // This should work with @Autowired
      dummyData = new DummyData(jdbcTemplate);
      createTables = new CreateTables(jdbcTemplate);
      createTables.createAllTables();
      dummyData.fillTablesWithDummyData();
      repository = new CommentRepository(jdbcTemplate);
   }
   
   
   @AfterEach
   void tearDown() {
      createTables.dropTables();
   }
   
   @Test
   void saveComment() {
      repository.saveComment(new CommentDto(4, "comment", 2, CREATED_AT));
      String sql = "SELECT * FROM comment WHERE reader_id=4 AND article_id=2";
      Comment comment = jdbcTemplate.queryForObject(sql, (rs, row) -> {
         Comment tempComment = new Comment();
         tempComment.setId(rs.getInt("id"));
         tempComment.setCommentText(rs.getString("comment_text"));
         return tempComment;
      });
      assertEquals("comment", comment.getCommentText());
   }
   
   @Test
   void temp() {
      System.out.println("helloo lefut");
   }
   
}