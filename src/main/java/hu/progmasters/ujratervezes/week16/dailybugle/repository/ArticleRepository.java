package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.configuration.ArticleQuery;
import hu.progmasters.ujratervezes.week16.dailybugle.domain.Article;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleModifyDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentCreateUpdateData;
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
        return jdbcTemplate.query(ArticleQuery.GET_ALL.getSqlQuery(), mapper);
    }

    public List<ArticleListDto> getFreshArticles() {
        return jdbcTemplate.query(ArticleQuery.GET_FRESH.getSqlQuery(), mapper);
    }

    public List<ArticleListDto> getTopArticles() {
        return jdbcTemplate.query(ArticleQuery.GET_TOP.getSqlQuery(), mapper);
    }

    public List<ArticleListDto> getTopFreshArticles() {
        return jdbcTemplate.query(ArticleQuery.GET_TOP_FRESH.getSqlQuery(), mapper);
    }

    public Article getArticle(int id) {
        return jdbcTemplate.queryForObject(ArticleQuery.GET_ID.getSqlQuery(), (resultSet, i) -> {
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
    }

    private List<CommentCreateUpdateData> getCommentsForArticle(int id) {
        List<CommentCreateUpdateData> comments = new ArrayList<>();
        comments = jdbcTemplate.query(ArticleQuery.GET_COMMENTS_FOR_ARTICLE_ID.getSqlQuery(), (resultSet, i) -> {
            CommentCreateUpdateData comment = new CommentCreateUpdateData();
            comment.setCommentAuthor(resultSet.getString("username"));
            comment.setCommentText(resultSet.getString("comment_text"));
            comment.setTime(resultSet.getTimestamp("created_at").toLocalDateTime());
            return comment;
        }, id);
        return comments;
    }

    public boolean saveRating(int rating, int id) {
        try {
            int rowsAffected = jdbcTemplate.update(ArticleQuery.SAVE_RATING.getSqlQuery(), id, rating);
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean updateArticle(ArticleModifyDto data, int id, LocalDateTime now) {
        try {
            int rowsAffected = jdbcTemplate.update(ArticleQuery.UPDATE.getSqlQuery(),
                    data.getTitle(),
                    data.getSynopsys(),
                    data.getText(),
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
            int rowsAffected = jdbcTemplate.update(ArticleQuery.DELETE.getSqlQuery(), now, id);
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean saveArticle(Integer publicist_id, String title, String synopsys, String text, LocalDateTime now) {
        try {
            int rowsAffected = jdbcTemplate.update(ArticleQuery.SAVE_ARTICLE.getSqlQuery(),
                    publicist_id,
                    title,
                    synopsys,
                    text,
                    now
            );
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
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
