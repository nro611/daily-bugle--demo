package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Comment;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j(topic = "CommentRepository")
public class CommentRepository {

   private final JdbcTemplate jdbcTemplate;
   
   @Autowired
   public CommentRepository(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }
   
   public boolean saveComment(CommentDto data) {
      try {
         int rowsAffected = jdbcTemplate.update(CommentQuery.SAVE_COMMENT,
                 data.getReaderId(),
                 data.getCommentText(),
                 data.getArticleId(),
                 data.getCreatedAt()
         );
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public Comment getComment(int commentId) {
      Comment comment;
      try {
         comment = jdbcTemplate.queryForObject(CommentQuery.GET_COMMENT, (rs, row) -> {
            Comment tempComment = new Comment();
            tempComment.setId(rs.getInt("id"));
            tempComment.setCommentAuthor(rs.getString("username"));
            tempComment.setCommentText(rs.getString("comment_text"));
            tempComment.setArticleId(rs.getInt("article_id"));
            return tempComment;
         }, commentId);
      }
      catch (DataAccessException exception) {
         comment = null;
         logger.error(exception.getMessage());
      }
      return comment;
   }
   
   public List<Comment> getArticleComments(int articleId) {
/*
        String sql = "SELECT comment.id, reader.username, comment.comment_text, comment.article_id " +
                "FROM article " +
                "LEFT JOIN comment ON comment.id = article.comment_id " +
                "JOIN reader ON comment.reader_id = reader.id " +
                "WHERE article.id=?";
*/
      List<Comment> comments;
      try {
         comments = jdbcTemplate.query(CommentQuery.GET_ARTICLE_COMMENTS, (rs, row) -> {
            Comment comment = new Comment();
            comment.setId(rs.getInt("id"));
            comment.setCommentAuthor(rs.getString("username"));
            comment.setCommentText(rs.getString("comment_text"));
            comment.setArticleId(rs.getInt("article_id"));
            return comment;
         }, articleId);
      }
      catch (DataAccessException exception) {
         comments = null;
         logger.error(exception.getMessage());
      }
      return comments;
   }
}
