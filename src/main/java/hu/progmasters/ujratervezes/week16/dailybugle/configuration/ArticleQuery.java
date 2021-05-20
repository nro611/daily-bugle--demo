package hu.progmasters.ujratervezes.week16.dailybugle.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ArticleQuery {
    // Get every article in a list with average rating (2 decimals) and number of ratings and comments
    GET_ALL("SELECT article.id, " +
            "article.title, " +
            "article.synopsys, " +
            "publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(10,2)),0) AS avg_rating, " +
            "IFNULL(COUNT(rating.article_rating),0) AS number_of_ratings, " +
            "IFNULL(c.CommentCount,0) AS number_of_comments " +
            "FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "LEFT JOIN rating ON rating.article_id = article.id " +
            "LEFT JOIN (" +
            "  SELECT article_id, COUNT(*) as CommentCount " +
            "  FROM comment " +
            "  WHERE comment.status = 1 " +
            "  GROUP BY article_id " +
            ") c ON article.id = c.article_id " +
            "WHERE article.status = 1 " +
            "GROUP BY article.id;"),
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
    SAVE_RATING("INSERT INTO rating (article_id, article_rating) VALUES (?, ?)"),
    GET_COMMENTS_FOR_ARTICLE_ID("SELECT r.username, c.comment_text, c.created_at " +
            "FROM comment c " +
            "JOIN reader r ON r.id = c.reader_id " +
            "WHERE c.article_id = ? " +
            "ORDER BY c.created_at DESC");

    private final String sqlQuery;

}
