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
        return jdbcTemplate.query(ReaderQuery.GET_READERS, (resultSet, i) -> {
            Reader reader = new Reader();
            reader.setId(resultSet.getInt("id"));
            reader.setUserName(resultSet.getString("username"));
            reader.setEmail(resultSet.getString("email"));
            reader.setCommentCount(resultSet.getInt("number_of_comments"));
            return reader;
        });
    }

    public ReaderProfileDto getReader(int id) {
        ReaderProfileDto readerProfile = null;
        try {
            readerProfile = jdbcTemplate.queryForObject(ReaderQuery.GET_READER, (resultSet, i) -> {
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
        try {
            commentedArticles = jdbcTemplate.query(ReaderQuery.GET_COMMENT_ARTICLES, (resultSet, i) -> {
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
        ratedArticles = jdbcTemplate.query(ReaderQuery.GET_RATED_ARTICLES, (resultSet, i) -> {
            ReaderRatedArticleDto readerRatedArticleDto = new ReaderRatedArticleDto();
            readerRatedArticleDto.setArticleId(resultSet.getInt("article_id"));
            readerRatedArticleDto.setArticleTitle(resultSet.getString("title"));
            readerRatedArticleDto.setRatingGiven(resultSet.getInt("article_rating"));
            return readerRatedArticleDto;
        }, id);
        return ratedArticles;
    }

    public boolean saveReader(ReaderDto data, LocalDateTime now) {
        try {
            int rowsAffected = jdbcTemplate.update(ReaderQuery.SAVE_READER,
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
        try {
            int rowsAffected = jdbcTemplate.update(ReaderQuery.UPDATE_READER,
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
        try {
            int rowsAffected = jdbcTemplate.update(ReaderQuery.DELETE_READER, now, id);
            return rowsAffected == 1;
        } catch (DataAccessException exception) {
            logger.error(exception.getMessage());
            return false;
        }
    }
}
