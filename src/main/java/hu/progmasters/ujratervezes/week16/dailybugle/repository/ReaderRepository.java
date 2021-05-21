package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Reader;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReaderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReaderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reader> getReaders() {
        String sql = "SELECT " +
                "r.id, " +
                "r.username, " +
                "r.email, " +
                "IFNULL(cn.comment_count,0) AS number_of_comments " +
                "FROM reader r " +
                "LEFT JOIN (" +
                "   SELECT c.reader_id, COUNT(*) AS comment_count " +
                "   FROM comment c " +
                "   GROUP BY c.reader_id " +
                "           ) AS cn ON reader_id = r.id " +
                "WHERE r.status = 1";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            Reader reader = new Reader();
            reader.setId(resultSet.getInt("id"));
            reader.setUserName(resultSet.getString("username"));
            reader.setEmail(resultSet.getString("email"));
            reader.setCommentCount(resultSet.getInt("number_of_comments"));
            return reader;
        });
    }

    public boolean saveReader(ReaderDto data, LocalDateTime now) {
        String sql = "INSERT INTO reader (username, email, created_at) VALUES (?, ?, ?)";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    data.getUserName(),
                    data.getEmail(),
                    now
            );
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean updateReader(ReaderDto data, int id, LocalDateTime now) {
        String sql = "UPDATE reader SET " +
                "username = ?, " +
                "email = ?, " +
                "modified_at = ? " +
                "WHERE id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    data.getUserName(),
                    data.getEmail(),
                    now,
                    id
            );
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean deleteReader(int id, LocalDateTime now) {
        String sql = "UPDATE reader SET " +
                "status = 0, " +
                "modified_at = ? " +
                "WHERE id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, now, id);
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }
}
