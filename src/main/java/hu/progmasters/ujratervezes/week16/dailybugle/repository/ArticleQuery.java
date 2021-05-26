package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ArticleQuery {
    // Get every article in a list with average rating (2 decimals) and number of ratings and comments
    GET_ALL_ARTICLE("SELECT article.id, " +
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
    GET_ARTICLE_ID("SELECT article.id, publicist_id, title, synopsys, text, publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(10,2)),0) AS avg_rating, " +
            "IFNULL(COUNT(rating.article_rating),0) AS number_of_ratings " +
            "FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "LEFT JOIN rating ON rating.article_id = article.id " +
            "WHERE article.id = ? AND article.status = 1 " +
            "GROUP BY article.id"),
    // Get the 10 latest articles
    GET_FRESH_ARTICLE("SELECT article.id, article.title, article.synopsys, publicist.name, " +
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
            "WHERE article.status = 1 AND (article.deployed_at <= CURRENT_TIMESTAMP OR article.deployed_at IS NULL)" +
            "GROUP BY article.id " +
            "ORDER BY article.created_at DESC " +
            "LIMIT 10"),
    // Get the 10 best rated articles
    GET_TOP_ARTICLE("SELECT article.id, article.title, article.synopsys, publicist.name, " +
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
            "WHERE article.status = 1 AND (article.deployed_at <= CURRENT_TIMESTAMP OR article.deployed_at IS NULL)" +
            "GROUP BY article.id " +
            "ORDER BY avg_rating DESC " +
            "LIMIT 10"),
    // Get the 10 best rated articles that are 3 days old at most
    GET_TOP_FRESH_ARTICLE("SELECT article.id, article.title, article.synopsys, publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(10,2)),0) AS avg_rating, " +
            "IFNULL(COUNT(rating.article_rating),0) AS number_of_ratings," +
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
            "WHERE sysdate() < ADDDATE(article.created_at, INTERVAL 3 DAY) AND " +
            "article.status = 1 AND (article.deployed_at <= CURRENT_TIMESTAMP OR article.deployed_at IS NULL)" +
            "GROUP BY article.id " +
            "ORDER BY avg_rating DESC " +
            "LIMIT 10"),
    // Update article
    UPDATE_ARTICLE("UPDATE article SET " +
            "title = ?, " +
            "synopsys = ?, " +
            "text = ?, " +
            "modified_at = ? " +
            "WHERE id = ?"),
    // Update article status to 0 <inactive>
    DELETE_ARTICLE("UPDATE article SET " +
            "status = 0, " +
            "modified_at = ? " +
            "WHERE id = ?"),
    // Import article into db
    SAVE_ARTICLE("INSERT INTO article (publicist_id, title, synopsys, text, created_at, deployed_at) " +
            "VALUES (?, ?, ?, ?, ?, ?)"),
    // TODO------------------------
    // insert keyword into keyword
    SAVE_KEYWORD(""),
    // insert article_id, keyword_id into article_keyword
    SAVE_ARTICLE_KEYWORD(""),
    //------------------------------
    GET_RATING_USER_ARTICLE("SELECT reader_id, article_rating " +
            "FROM rating " +
            "WHERE reader_id = ? AND article_id = ?"),
    SAVE_RATING("INSERT INTO rating (reader_id, article_id, article_rating, created_at) " +
            "VALUES (?, ?, ?, ?)"),
    UPDATE_RATING("UPDATE rating SET " +
            "article_rating = ?, " +
            "modified_at = ? " +
            "WHERE reader_id = ? AND article_id = ?"),
    GET_COMMENTS_FOR_ARTICLE_ID("SELECT r.username, c.comment_text, c.created_at " +
            "FROM comment c " +
            "JOIN reader r ON r.id = c.reader_id " +
            "WHERE c.article_id = ? " +
            "ORDER BY c.created_at DESC");

    private final String sqlQuery;

}
