package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Publicist;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PhonebookDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PublicistRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PublicistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PublicistListDto> getPublicists() {
        String sql = "SELECT * from publicist WHERE status = 1";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            PublicistListDto publicist = new PublicistListDto();
            publicist.setId(resultSet.getInt("id"));
            publicist.setName(resultSet.getString("name"));
            publicist.setAddress(resultSet.getString("address"));
            publicist.setEmail(resultSet.getString("email"));
            publicist.setPhone(resultSet.getString("phone"));
            return publicist;
        });
    }

    public Publicist getPublicist(int id) {
        String sql = "SELECT * FROM publicist WHERE id = ? AND status = 1";
        try {
            return jdbcTemplate.queryForObject(sql, (resultSet, i) -> {
                Publicist publicist = new Publicist();
                publicist.setId(resultSet.getInt("id"));
                publicist.setName(resultSet.getString("name"));
                publicist.setAddress(resultSet.getString("address"));
                publicist.setEmail(resultSet.getString("email"));
                publicist.setPhone(resultSet.getString("phone"));
                publicist.setArticles(getArticlesByPublicist(id));
                return publicist;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // gets all articles by a publicist in a descending order, used in getPublicist for articleList
    private List<ArticleListDto> getArticlesByPublicist(int id) {
        List<ArticleListDto> articles = new ArrayList<>();
        String sql = "SELECT a.id, p.name, a.title, a.synopsys, " +
                "IFNULL(CAST(AVG(r.article_rating) AS DECIMAL(10,2)),0.00) AS avg_rating," +
                "IFNULL(COUNT(r.article_rating),0) AS number_of_ratings " +
                "FROM article a " +
                "JOIN publicist p ON p.id = a.publicist_id " +
                "LEFT JOIN rating r ON r.article_id = a.id " +
                "WHERE p.id = ? AND a.status = 1 AND (a.deployed_at <= CURRENT_TIMESTAMP OR a.deployed_at IS NULL)" +
                "GROUP BY a.id " +
                "ORDER BY a.created_at DESC;";
        try {
            articles = jdbcTemplate.query(sql, (resultSet, i) -> {
                ArticleListDto article = new ArticleListDto();
                article.setId(resultSet.getInt("id"));
                article.setPublicistName(resultSet.getString("name"));
                article.setTitle(resultSet.getString("title"));
                article.setSynopsys(resultSet.getString("synopsys"));
                article.setAvgRating(resultSet.getDouble("avg_rating"));
                article.setNumOfRatings(resultSet.getInt("number_of_ratings"));
                return article;
            }, id);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return articles;

    }

    public boolean savePublicist(PublicistDto data, LocalDateTime now) {
        String sql = "INSERT INTO publicist (name, address, email, phone, created_at) VALUES (?, ?, ?, ?, ?);";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    data.getName(),
                    data.getAddress(),
                    data.getEmail(),
                    data.getPhone(),
                    now
            );
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean updatePublicist(int id, PublicistDto data, LocalDateTime now) {
        String sql = "UPDATE publicist SET " +
                "name = ?, " +
                "address = ?, " +
                "email = ?, " +
                "phone = ?, " +
                "modified_at = ?" +
                "WHERE id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    data.getName(),
                    data.getAddress(),
                    data.getEmail(),
                    data.getPhone(),
                    now,
                    id
            );
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean deletePublicist(int id, LocalDateTime now) {
        String sql = "UPDATE publicist SET " +
                "name = 'Névtelen Szerző', " +
                "address = NULL, " +
                "email = NULL, " +
                "phone = NULL, " +
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

    public List<PhonebookDto> getPhonebook() {
        String sql = "SELECT name, phone FROM publicist WHERE status = 1";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            PhonebookDto phonebook = new PhonebookDto();
            phonebook.setName(resultSet.getString("name"));
            phonebook.setPhone(resultSet.getString("phone"));
            return phonebook;
        });

    }
}
