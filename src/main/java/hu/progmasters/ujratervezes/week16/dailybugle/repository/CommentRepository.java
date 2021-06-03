package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Comment;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean saveComment(CommentDto data) { // this is the old version new query needed
        String sql = "INSERT INTO comment (reader_id, comment_text, article_id, created_at) " +
                "VALUES (?, ?, ?, ?)";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    data.getReaderId(),
                    data.getCommentText(),
                    data.getArticleId(),
                    data.getCreatedAt()
            );
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }
    
    public Comment getComment(int commentId) {
        String sql = "SELECT comment.id, reader.username, comment.comment_text, comment.article_id " +
                "FROM comment " +
                "JOIN reader ON comment.reader_id = reader.id" +
                "WHERE comment.id=?";
        Comment comment;
        try {
            comment = jdbcTemplate.queryForObject(sql, (rs, row) -> {
                Comment tempComment = new Comment();
                tempComment.setId(rs.getInt("comment.id"));
                tempComment.setCommentAuthor(rs.getString("reader.username"));
                tempComment.setCommentText(rs.getString("comment.comment_text"));
                tempComment.setArticleId(rs.getInt("comment.article_id"));
                return tempComment;
            }, commentId);
        } catch (DataAccessException exception) {
            comment = null;
        }
        return comment;
    }
    
    public List<Comment> getArticleComments(int articleId) {
        String sql = "SELECT comment.id, reader.username, comment.comment_text, comment.article_id " +
                "FROM article " +
                "LEFT JOIN comment ON comment.id = article.comment_id " +
                "JOIN reader ON comment.reader_id = reader.id" +
                "WHERE article.id=?";
        List<Comment> comments;
        try {
            comments = jdbcTemplate.query(sql, (rs, row) -> {
                Comment comment = new Comment();
                comment.setId(rs.getInt("comment.id"));
                comment.setCommentAuthor(rs.getString("reader.username"));
                comment.setCommentText(rs.getString("comment.comment_text"));
                comment.setArticleId(rs.getInt("comment.article_id"));
                return comment;
            }, articleId);
        } catch (DataAccessException exception) {
            comments = null;
        }
        return comments;
    }
}
