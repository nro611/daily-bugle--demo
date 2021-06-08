package hu.progmasters.ujratervezes.week16.dailybugle.repository;

public class ArticleQuery {
    // Get every article in a list with average rating (2 decimals) and number of ratings and comments
    public static final String GET_ARTICLES = "SELECT article.id, " +
            "article.title, " +
            "article.synopsys, " +
            "publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(3,2)),0) AS avg_rating, " +
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
            "GROUP BY article.id;";
    // Get article by id
    public static final String GET_ARTICLE = "SELECT article.id, publicist_id, title, synopsys, text, publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(3,2)),0) AS avg_rating, " +
            "IFNULL(COUNT(rating.article_rating),0) AS number_of_ratings " +
            "FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "LEFT JOIN rating ON rating.article_id = article.id " +
            "WHERE article.id = ? AND article.status = 1 " +
            "GROUP BY article.id";
    // Get the 10 latest articles
    public static final String GET_FRESH_ARTICLES = "SELECT article.id, article.title, article.synopsys, publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(3,2)),0) AS avg_rating, " +
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
            "LIMIT 10";
    // Get the 10 best rated articles
    public static final String GET_TOP_ARTICLES = "SELECT article.id, article.title, article.synopsys, publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(3,2)),0) AS avg_rating, " +
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
            "LIMIT 10";
    // Get the 10 best rated articles that are 3 days old at most
    public static final String GET_TOP_FRESH_ARTICLES = "SELECT article.id, article.title, article.synopsys, publicist.name, " +
            "IFNULL(CAST(AVG(rating.article_rating) AS DECIMAL(3,2)),0) AS avg_rating, " +
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
            "LIMIT 10";
    // Update article
    public static final String UPDATE_ARTICLE = "UPDATE article SET " +
            "title = ?, " +
            "synopsys = ?, " +
            "text = ?, " +
            "deployed_at = ?, " +
            "modified_at = ? " +
            "WHERE id = ?";
    // Update article status to 0 <inactive>
    public static final String DELETE_ARTICLE = "UPDATE article SET " +
            "status = 0, " +
            "modified_at = ? " +
            "WHERE id = ?";
    public static final String SAVE_ARTICLE = "INSERT INTO article (publicist_id, title, synopsys, text, created_at, deployed_at) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    public static final String GET_RATING_USER_ARTICLE = "SELECT reader_id, article_rating " +
            "FROM rating " +
            "WHERE reader_id = ? AND article_id = ?";
    public static final String SAVE_RATING = "INSERT INTO rating (reader_id, article_id, article_rating, created_at) " +
            "VALUES (?, ?, ?, ?)";
    public static final String UPDATE_RATING = "UPDATE rating SET " +
            "article_rating = ?, " +
            "modified_at = ? " +
            "WHERE reader_id = ? AND article_id = ?";
    public static final String GET_COMMENTS_FOR_ARTICLE_ID = "SELECT r.username, c.comment_text, c.created_at " +
            "FROM comment c " +
            "JOIN reader r ON r.id = c.reader_id " +
            "WHERE c.article_id = ? " +
            "ORDER BY c.created_at DESC";
    public static final String GET_KEYWORDS_FOR_ARTICE_ID = "SELECT k.keyword_name FROM keyword k " +
            "JOIN article_keyword ak ON ak.keyword_id = k.id " +
            "WHERE ak.article_id = ? " +
            "ORDER BY k.keyword_name ASC";
    public static final String DELETE_KEYWORDS_ARTICLEID = "DELETE keyword FROM keyword " +
            "JOIN article_keyword ak ON ak.keyword_id = keyword.id " +
            "WHERE article_id = ?";
    public static final String GET_KEYWORDS = "SELECT keyword_name FROM keyword";
    public static final String SAVE_KEYWORD = "INSERT INTO keyword (keyword_name) VALUES (?)";
    public static final String GET_ARTICLE_ID = "SELECT id FROM article WHERE title = ?";
    public static final String SAVE_ARTICLE_KEYWORD = "INSERT INTO article_keyword (article_id, keyword_id) " +
            "VALUES (?,?)";
    public static final String REMOVE_KEYWORD = "DELETE FROM keyword WHERE id = ?";
    public static final String CONTAINS_KEYWORD = "SELECT article_id FROM article_keyword WHERE keyword_id = ? LIMIT 1";
    public static final String GET_KEYWORD_IDS = "SELECT id FROM keyword WHERE keyword_name IN (%s)";
    public static final String REMOVE_ARTICLE_KEYWORDS = "DELETE FROM article_keyword WHERE article_id = ?" +
            " AND keyword_id IN (%s)";

    private ArticleQuery() {
    }


}
