package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
                "           ) AS cn ON reader_id = r.id";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            Reader reader = new Reader();
            reader.setId(resultSet.getInt("id"));
            reader.setUserName(resultSet.getString("username"));
            reader.setEmail(resultSet.getString("email"));
            reader.setCommentCount(resultSet.getInt("number_of_comments"));
            return reader;
        });
    }
}
