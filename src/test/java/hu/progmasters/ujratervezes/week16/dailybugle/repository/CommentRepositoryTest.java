package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.CreateTables;
import hu.progmasters.ujratervezes.week16.dailybugle.domain.Comment;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentDto;
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
class CommentRepositoryTest {
   
   @Autowired
   private JdbcTemplate jdbcTemplate;
   private CreateTables createTables;
   private DummyData dummyData;
   
   private CommentRepository repository;
   
   private final LocalDateTime CREATED_AT = LocalDateTime.of(2021, 11, 13, 10, 11);
   
   @BeforeEach
   void setUp() {
      
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
   void testSaveComment() {
      repository.saveComment(new CommentDto(4, "comment", 2, CREATED_AT));
      String sql = "SELECT comment.id, reader.username, comment.comment_text, comment.article_id " +
              "FROM comment " +
              "JOIN reader ON comment.reader_id = reader.id " +
              "WHERE comment.comment_text='comment'";
      Comment comment = jdbcTemplate.queryForObject(sql, (rs, row) -> {
         Comment tempComment = new Comment();
         tempComment.setId(rs.getInt("id"));
         tempComment.setCommentText(rs.getString("comment_text"));
         tempComment.setCommentAuthor(rs.getString("username"));
         tempComment.setArticleId(rs.getInt("article_id"));
         return tempComment;
      });
      assert comment != null;
      assertEquals("comment", comment.getCommentText());
      assertEquals("Chessmaster2000", comment.getCommentAuthor());
      assertEquals(2, comment.getArticleId());
   }

   @Test
   void testGetComment() {
      Comment comment = repository.getComment(1);
      assertEquals("Ezt kipróbálom!", comment.getCommentText());
      assertEquals("albinoCat", comment.getCommentAuthor());
      assertEquals(1, comment.getArticleId());
   }

   @Test
   void testGetArticleComments() {
      List<Comment> comments = repository.getArticleComments(10);
      assertEquals(1, comments.size());
      Comment comment = comments.get(0);
      assertEquals("Vigyázzunk a Földre!", comment.getCommentText());
      assertEquals("Chessmaster2000", comment.getCommentAuthor());
      assertEquals(10, comment.getArticleId());

   }
   
}