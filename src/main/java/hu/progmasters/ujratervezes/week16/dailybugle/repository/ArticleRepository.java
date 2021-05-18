package hu.progmasters.ujratervezes.week16.dailybugle.repository;

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
        String sql = "SELECT article.id, title, synopsys, publicist.name FROM article " +
                "JOIN publicist ON publicist.id = publicist_id";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            ArticleListDto article = new ArticleListDto();
            article.setId(resultSet.getInt("id"));
            article.setPublicistName(resultSet.getString("name"));
            article.setTitle(resultSet.getString("title"));
            article.setSynopsys(resultSet.getString("synopsys"));
            return article;
        });
    }

    public Article getArticle(int id) {
        String sql = "SELECT article.id, publicist_id, title, synopsys, text, publicist.name FROM article " +
                "JOIN publicist ON publicist.id = publicist_id";
        return jdbcTemplate.queryForObject(sql, (resultSet, i) -> {
            Article article = new Article();
            article.setId(resultSet.getInt("id"));
            article.setPublicistId(resultSet.getInt("publicist_id"));
            article.setPublicistName(resultSet.getString("name"));
            article.setTitle(resultSet.getString("title"));
            article.setSynopsys(resultSet.getString("synopsys"));
            article.setText(resultSet.getString("text"));
            return article;
        }, id);
    }

    public boolean updateArticle(ArticleModifyDto data, int id, LocalDateTime now) {
        String sql = "UPDATE article SET " +
                "title = ?, " +
                "synopsys = ?, " +
                "text = ?, " +
                "modified_at = ? " +
                "WHERE id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
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
        String sql = "UPDATE article SET " +
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


    public boolean saveArticle(Integer publicist_id, String title, String synopsys, String text, LocalDateTime now) {
        String sql = "INSERT INTO article (publicist_id, title, synopsys, text, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
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
