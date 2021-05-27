package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Article;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleRatingDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ArticleListMapper mapper;


    @Autowired
    public ArticleRepository(JdbcTemplate jdbcTemplate, ArticleListMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    public List<ArticleListDto> getArticles() {
        return jdbcTemplate.query(ArticleQuery.GET_ALL_ARTICLE.getSqlQuery(), mapper);
    }

    public List<ArticleListDto> getFreshArticles() {
        return jdbcTemplate.query(ArticleQuery.GET_FRESH_ARTICLE.getSqlQuery(), mapper);
    }

    public List<ArticleListDto> getTopArticles() {
        return jdbcTemplate.query(ArticleQuery.GET_TOP_ARTICLE.getSqlQuery(), mapper);
    }

    public List<ArticleListDto> getTopFreshArticles() {
        return jdbcTemplate.query(ArticleQuery.GET_TOP_FRESH_ARTICLE.getSqlQuery(), mapper);
    }

    public Article getArticle(int id) {
        try {
            return jdbcTemplate.queryForObject(ArticleQuery.GET_ARTICLE_ID.getSqlQuery(), (resultSet, i) -> {
                Article article = new Article();
                article.setId(resultSet.getInt("id"));
                article.setPublicistId(resultSet.getInt("publicist_id"));
                article.setPublicistName(resultSet.getString("name"));
                article.setTitle(resultSet.getString("title"));
                article.setSynopsys(resultSet.getString("synopsys"));
                article.setText(resultSet.getString("text"));
                article.setAvgRating(resultSet.getDouble("avg_rating"));
                article.setNumOfRatings(resultSet.getInt("number_of_ratings"));
                article.setComments(getCommentsForArticle(id));
                return article;
            }, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    private List<CommentDto> getCommentsForArticle(int id) {
        return jdbcTemplate.query(ArticleQuery.GET_COMMENTS_FOR_ARTICLE_ID.getSqlQuery(), (resultSet, i) -> {
            CommentDto comment = new CommentDto();
            comment.setCommentAuthor(resultSet.getString("username"));
            comment.setCommentText(resultSet.getString("comment_text"));
            comment.setTime(resultSet.getTimestamp("created_at").toLocalDateTime());
            return comment;
        }, id);
    }

    public boolean saveRating(int readerId, int articleId, int rating, LocalDateTime now) {
        try {
            int rowsAffected = jdbcTemplate.update(ArticleQuery.SAVE_RATING.getSqlQuery(), readerId, articleId, rating, now);
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean updateRating(int readerId, int articleId, int rating, LocalDateTime now) {
        try {
            int rowsAffected = jdbcTemplate.update(ArticleQuery.UPDATE_RATING.getSqlQuery(), rating, now, readerId, articleId);
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public ArticleRatingDto getRatingWithUserAndArticle(int readerId, int articleId) {
        try {
            return jdbcTemplate.queryForObject(ArticleQuery.GET_RATING_USER_ARTICLE.getSqlQuery(), (resultSet, i) -> {
                ArticleRatingDto rating = new ArticleRatingDto();
                rating.setReaderId(resultSet.getInt("reader_id"));
                rating.setRating(resultSet.getInt("article_rating"));
                return rating;
            }, readerId, articleId);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public boolean updateArticle(ArticleDto data, int id, LocalDateTime now) {
        try {
            int rowsAffected = jdbcTemplate.update(ArticleQuery.UPDATE_ARTICLE.getSqlQuery(),
                    data.getTitle(),
                    data.getSynopsys(),
                    data.getText(),
                    data.getDeployTime(),
                    now,
                    id
            );
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean deleteArticle(int id, LocalDateTime now) {
        try {
            int rowsAffected = jdbcTemplate.update(ArticleQuery.DELETE_ARTICLE.getSqlQuery(), now, id);
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean saveArticle(ArticleDto data, LocalDateTime now) {
        try {
            int rowsAffected = jdbcTemplate.update(ArticleQuery.SAVE_ARTICLE.getSqlQuery(),
                    data.getPublicistId(),
                    data.getTitle(),
                    data.getSynopsys(),
                    data.getText(),
                    now,
                    data.getDeployTime()
            );
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public List<String> getKeywords() {
        try {
            return jdbcTemplate.queryForList("SELECT keyword_name FROM keyword", String.class);
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }
    }

    public boolean saveKeyword(String keyword) {
        try {
            String sql = "INSERT INTO keyword (keyword_name) VALUES (?)";
            int rowsAffected = jdbcTemplate.update(sql, keyword);
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public List<Integer> getKeywordIds(String inSql, List<String> keywordsInDto) {
        List<Integer> keywordIds = new ArrayList<>();
        try {
            keywordIds = jdbcTemplate.queryForList(
                    String.format("SELECT id FROM keyword WHERE keyword_name IN (%s)", inSql),
                    Integer.class,
                    keywordsInDto.toArray()
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return keywordIds;
    }

    public Integer getArticleId(String title) {
        String sql = "SELECT id FROM article WHERE title = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, title);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    public boolean saveArticleKeyword(int articleId, int keywordId) {
        String sql = "INSERT INTO article_keyword (article_id, keyword_id) VALUES (?,?)";
        try {
            int rowsAffected = jdbcTemplate.update(sql, articleId, keywordId);
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public List<String> getKeywordsForArticle(int id) {
        // TODO
        return null;
    }

    public void removeKeywords(int id) {
        // TODO
    }

    public void removeKeyword(String keyword) {
        // TODO
    }

    @Component
    private static class ArticleListMapper implements RowMapper<ArticleListDto> {

        @Override
        public ArticleListDto mapRow(ResultSet resultSet, int i) throws SQLException {
            ArticleListDto article = new ArticleListDto();
            article.setId(resultSet.getInt("id"));
            article.setPublicistName(resultSet.getString("name"));
            article.setTitle(resultSet.getString("title"));
            article.setSynopsys(resultSet.getString("synopsys"));
            article.setAvgRating(resultSet.getDouble("avg_rating"));
            article.setNumOfRatings(resultSet.getInt("number_of_ratings"));
            article.setNumOfComments(resultSet.getInt("number_of_comments"));
            return article;
        }
    }
}
