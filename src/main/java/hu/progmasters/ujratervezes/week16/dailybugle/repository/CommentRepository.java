package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentCreateUpdateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean saveComment(CommentCreateUpdateData data, int articleId, LocalDateTime now) {
        String sql = "INSERT INTO comment (comment_author, comment_text, article_id, created_at) " +
                "VALUES (?, ?, ?, ?)";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    data.getCommentAuthor(),
                    data.getCommentText(),
                    articleId,
                    now
            );
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }
}
