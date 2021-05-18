package hu.progmasters.ujratervezes.week16.dailybugle.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ArticleQuery {
    // Get every article in a list with average rating (2 decimals) and number of ratings
    GET_ALL("SELECT article.id, article.title, article.synopsys, publicist.name, " +
            "CAST(AVG(rating.article_rating) AS DECIMAL(10,2)) AS avg_rating, " +
            "COUNT(rating.article_rating) AS number_of_ratings " +
            "FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "JOIN rating ON rating.article_id = article.id " +
            "GROUP BY article.id"),
    // Get article by id
    GET_ID("SELECT article.id, publicist_id, title, synopsys, text, publicist.name, " +
            "CAST(AVG(rating.article_rating) AS DECIMAL(10,2)) AS avg_rating, " +
            "COUNT(rating.article_rating) AS number_of_ratings " +
            "FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "JOIN rating ON rating.article_id = article.id " +
            "WHERE article.id = ? " +
            "GROUP BY article.id"),
    // Get the 10 latest articles
    GET_FRESH("SELECT article.id, title, synopsys, publicist.name FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "ORDER BY created_at DESC " +
            "LIMIT 10"),
    // Get the 10 best rated articles
    GET_TOP("SELECT article.id, title, synopsys, publicist.name FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "ORDER BY avg_rating DESC " +
            "LIMIT 10"),
    // Get the 10 best rated articles that are 3 days old at most
    GET_TOP_FRESH("SELECT article.id, title, synopsys, publicist.name FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "WHERE sysdate() < ADDDATE(article.created_at, INTERVAL 3 DAY) " +
            "ORDER BY avg_rating DESC " +
            "LIMIT 10"),
    // Update article
    UPDATE("UPDATE article SET " +
            "title = ?, " +
            "synopsys = ?, " +
            "text = ?, " +
            "modified_at = ? " +
            "WHERE id = ?"),
    // Update article status to 0 <inactive>
    DELETE("UPDATE article SET " +
            "status = 0, " +
            "modified_at = ? " +
            "WHERE id = ?"),
    // Import article into db
    SAVE_ARTICLE("INSERT INTO article (publicist_id, title, synopsys, text, created_at) " +
            "VALUES (?, ?, ?, ?, ?)"),
    SAVE_RATING("INSERT INTO rating (article_id, article_rating) VALUES (?, ?)");

    private final String sqlQuery;

}
