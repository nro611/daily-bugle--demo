package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Publicist;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PhonebookDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j(topic = "PublicistRepository")
public class PublicistRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PublicistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PublicistListDto> getPublicists() {
        try {
            return jdbcTemplate.query(PublicistQuery.GET_PUBLICISTS, (resultSet, i) -> {
                PublicistListDto publicist = new PublicistListDto();
                publicist.setId(resultSet.getInt("id"));
                publicist.setName(resultSet.getString("name"));
                publicist.setAddress(resultSet.getString("address"));
                publicist.setEmail(resultSet.getString("email"));
                publicist.setPhone(resultSet.getString("phone"));
                return publicist;
            });
        }
        catch (DataAccessException exception) {
            logger.error(exception.getMessage());
            return null;
        }
    }

    public Publicist getPublicist(int id) {
        try {
            return jdbcTemplate.queryForObject(PublicistQuery.GET_PUBLICIST, (resultSet, i) -> {
                Publicist publicist = new Publicist();
                publicist.setId(resultSet.getInt("id"));
                publicist.setName(resultSet.getString("name"));
                publicist.setAddress(resultSet.getString("address"));
                publicist.setEmail(resultSet.getString("email"));
                publicist.setPhone(resultSet.getString("phone"));
                publicist.setArticles(getArticlesByPublicist(id));
                return publicist;
            }, id);
        } catch (EmptyResultDataAccessException exception) {
            logger.error(exception.getMessage());
            return null;
        }
    }

    // gets all articles by a publicist in a descending order, used in getPublicist for articleList
    private List<ArticleListDto> getArticlesByPublicist(int id) {
        List<ArticleListDto> articles = new ArrayList<>();
        try {
            articles = jdbcTemplate.query(PublicistQuery.GET_ARTICLES_BY_PUBLICIST, (resultSet, i) -> {
                ArticleListDto article = new ArticleListDto();
                article.setId(resultSet.getInt("id"));
                article.setPublicistName(resultSet.getString("name"));
                article.setTitle(resultSet.getString("title"));
                article.setSynopsys(resultSet.getString("synopsys"));
                article.setAvgRating(resultSet.getDouble("avg_rating"));
                article.setNumOfRatings(resultSet.getInt("number_of_ratings"));
                article.setNumOfComments(resultSet.getInt("number_of_comments"));
                return article;
            }, id);
        } catch (DataAccessException exception) {
            logger.error(exception.getMessage());
        }
        return articles;
    }

    public boolean savePublicist(PublicistDto data, LocalDateTime now) {
        try {
            int rowsAffected = jdbcTemplate.update(PublicistQuery.SAVE_PUBLICIST,
                    data.getName(),
                    data.getAddress(),
                    data.getEmail(),
                    data.getPhone(),
                    now
            );
            return rowsAffected == 1;
        } catch (DataAccessException exception) {
            logger.error(exception.getMessage());
            return false;
        }
    }

    public boolean updatePublicist(int id, PublicistDto data, LocalDateTime now) {
        try {
            int rowsAffected = jdbcTemplate.update(PublicistQuery.UPDATE_PUBLICIST,
                    data.getName(),
                    data.getAddress(),
                    data.getEmail(),
                    data.getPhone(),
                    now,
                    id
            );
            return rowsAffected == 1;
        } catch (DataAccessException exception) {
            logger.error(exception.getMessage());
            return false;
        }
    }

    public boolean deletePublicist(int id, LocalDateTime now) {
        try {
            int rowsAffected = jdbcTemplate.update(PublicistQuery.DELETE_PUBLICIST, now, id);
            return rowsAffected == 1;
        } catch (DataAccessException exception) {
            logger.error(exception.getMessage());
            return false;
        }
    }

    public List<PhonebookDto> getPhonebook() {
        List<PhonebookDto> phonebook = new ArrayList<>();
        try {
            phonebook = jdbcTemplate.query(PublicistQuery.GET_PHONEBOOK, (resultSet, i) -> {
                PhonebookDto tempPhonebook = new PhonebookDto();
                tempPhonebook.setName(resultSet.getString("name"));
                tempPhonebook.setPhone(resultSet.getString("phone"));
                return tempPhonebook;
            });
        }
        catch (DataAccessException exception) {
            logger.error(exception.getMessage());
        }
        return phonebook;
    }
}
