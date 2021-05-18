package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.configuration.ArticleQuery;
import hu.progmasters.ujratervezes.week16.dailybugle.domain.Article;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleModifyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ArticleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ArticleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ArticleListDto> getArticles() {
        return jdbcTemplate.query(ArticleQuery.GET_ALL.getSqlQuery(), (resultSet, i) -> {
            ArticleListDto article = new ArticleListDto();
            article.setId(resultSet.getInt("id"));
            article.setPublicistName(resultSet.getString("name"));
            article.setTitle(resultSet.getString("title"));
            article.setSynopsys(resultSet.getString("synopsys"));
            article.setAvgRating(resultSet.getDouble("avg_rating"));
            article.setNumOfRatings(resultSet.getInt("number_of_ratings"));
            return article;
        });
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
            return article;
        }, id);
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


}
