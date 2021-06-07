package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Reader;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderCommentedArticleDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderProfileDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderRatedArticleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j(topic = "ReaderRepository")
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
        List<Reader> readers = null;
        try {
            readers = jdbcTemplate.query(sql, (resultSet, i) -> {
                Reader reader = new Reader();
                reader.setId(resultSet.getInt("id"));
                reader.setUserName(resultSet.getString("username"));
                reader.setEmail(resultSet.getString("email"));
                reader.setCommentCount(resultSet.getInt("number_of_comments"));
                return reader;
            });
        }
        catch (DataAccessException exception) {
            logger.error(exception.getMessage());
        }
        return readers;
    }

    public ReaderProfileDto getReader(int id) {
        String sql = "SELECT r.username, r.email " +
                "FROM reader r " +
                "WHERE r.id = ?";
        ReaderProfileDto readerProfile = null;
        try {
            readerProfile = jdbcTemplate.queryForObject(sql, (resultSet, i) -> {
                ReaderProfileDto reader = new ReaderProfileDto();
                reader.setName(resultSet.getString("username"));
                reader.setEmail(resultSet.getString("email"));
                reader.setCommentedArticles(getCommentedArticles(id));
                reader.setRatedArticles(getRatedArticles(id));
                return reader;
            }, id);
        }
        catch (DataAccessException exception) {
            logger.error(exception.getMessage());
        }
        return readerProfile;
    }

    // minden cikk ID-ja és címe, ami alá kommentelt az olvasó, és a hozzá tartozó kommentek száma
    private List<ReaderCommentedArticleDto> getCommentedArticles(int id) {
        List<ReaderCommentedArticleDto> commentedArticles = new ArrayList<>();
        String sql = "SELECT a.id AS article_id, a.title, COUNT(c.id) AS comment_count " +
                "FROM article a " +
                "LEFT JOIN comment c ON a.id = c.article_id " +
                "WHERE c.reader_id = ? " +
                "GROUP BY a.title";
        try {
            commentedArticles = jdbcTemplate.query(sql, (resultSet, i) -> {
                ReaderCommentedArticleDto readerCommentedArticle = new ReaderCommentedArticleDto();
                readerCommentedArticle.setArticleId(resultSet.getInt("article_id"));
                readerCommentedArticle.setArticleTitle(resultSet.getString("title"));
                readerCommentedArticle.setCommentCount(resultSet.getInt("comment_count"));
                return readerCommentedArticle;
            }, id);
        } catch (DataAccessException exception) {
            logger.error(exception.getMessage());
        }
        return commentedArticles;
    }

    // minden cikk ID-ja és címe, amit értékelt az olvasó, és az adott értékelés
    private List<ReaderRatedArticleDto> getRatedArticles(int id) {
        List<ReaderRatedArticleDto> ratedArticles;
        String sql = "SELECT a.id AS article_id, a.title, r.article_rating " +
                "FROM article a  " +
                "LEFT JOIN rating r ON a.id = r.article_id " +
                "WHERE r.reader_id = ?";
        ratedArticles = jdbcTemplate.query(sql, (resultSet, i) -> {
            ReaderRatedArticleDto readerRatedArticleDto = new ReaderRatedArticleDto();
            readerRatedArticleDto.setArticleId(resultSet.getInt("article_id"));
            readerRatedArticleDto.setArticleTitle(resultSet.getString("title"));
            readerRatedArticleDto.setRatingGiven(resultSet.getInt("article_rating"));
            return readerRatedArticleDto;
        }, id);
        return ratedArticles;
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
        } catch (DataAccessException exception) {
            logger.error(exception.getMessage());
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
        } catch (DataAccessException exception) {
            logger.error(exception.getMessage());
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
        } catch (DataAccessException exception) {
            logger.error(exception.getMessage());
            return false;
        }
    }
}
