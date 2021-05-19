package hu.progmasters.ujratervezes.week16.dailybugle.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ArticleQuery {
    // Get every article in a list with average rating (2 decimals) and number of ratings
    GET_ALL("SELECT article.id, article.title, article.synopsys, publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(10,2)),0) AS avg_rating, " +
            "IFNULL(COUNT(rating.article_rating),0) AS number_of_ratings " +
            "FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "JOIN rating ON rating.article_id = article.id " +
            "GROUP BY article.id"),

    /*
        Kommentek számának hozzáadása a RShez
    //  TODO
            SELECT a.id, a.title, a.synopsys, p.name,
            IFNULL(CAST(AVG(r.article_rating) AS DECIMAL(10,2)),0) AS avg_rating,
            IFNULL(COUNT(r.article_rating),0) AS number_of_ratings,
            -- IFNULL(COUNT(CASE WHEN c.status = 1 THEN 1 END),0) AS number_of_comments
            FROM article a
            JOIN publicist p ON p.id = a.publicist_id
            LEFT JOIN rating r ON r.article_id = a.id
            -- LEFT JOIN comment c ON c.article_id = a.id
            GROUP BY a.id;

        Ez lenne az ötlet, de a kikommentelt sorokkal nem jó számok jönnek a number_of_comments-re.
        Megszorozza számot az adott article-höz tartozó commentek számával, és ezt adja vissza
        comment számként is...

     */
    // Get article by id
    GET_ID("SELECT article.id, publicist_id, title, synopsys, text, publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(10,2)),0) AS avg_rating, " +
            "IFNULL(COUNT(rating.article_rating),0) AS number_of_ratings " +
            "FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "JOIN rating ON rating.article_id = article.id " +
            "WHERE article.id = ? " +
            "GROUP BY article.id"),
    // Get the 10 latest articles
    GET_FRESH("SELECT article.id, article.title, article.synopsys, publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(10,2)),0) AS avg_rating, " +
            "IFNULL(COUNT(rating.article_rating),0) AS number_of_ratings " +
            "FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "JOIN rating ON rating.article_id = article.id " +
            "GROUP BY article.id " +
            "ORDER BY article.created_at DESC " +
            "LIMIT 10"),
    // Get the 10 best rated articles
    GET_TOP("SELECT article.id, article.title, article.synopsys, publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(10,2)),0) AS avg_rating, " +
            "IFNULL(COUNT(rating.article_rating),0) AS number_of_ratings " +
            "FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "JOIN rating ON rating.article_id = article.id " +
            "GROUP BY article.id " +
            "ORDER BY avg_rating DESC " +
            "LIMIT 10"),
    // Get the 10 best rated articles that are 3 days old at most
    GET_TOP_FRESH("SELECT article.id, article.title, article.synopsys, publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(10,2)),0) AS avg_rating, " +
            "IFNULL(COUNT(rating.article_rating),0) AS number_of_ratings " +
            "FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "JOIN rating ON rating.article_id = article.id " +
            "WHERE sysdate() < ADDDATE(article.created_at, INTERVAL 3 DAY) " +
            "GROUP BY article.id " +
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
