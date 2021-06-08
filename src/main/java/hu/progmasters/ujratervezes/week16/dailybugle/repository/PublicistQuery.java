package hu.progmasters.ujratervezes.week16.dailybugle.repository;

public class PublicistQuery {

    public static final String GET_PUBLICISTS = "SELECT * from publicist WHERE status = 1";
    public static final String GET_PUBLICIST = "SELECT * FROM publicist WHERE id = ? AND status = 1";
    public static final String GET_ARTICLES_BY_PUBLICIST = "SELECT a.id, p.name, a.title, a.synopsys, " +
            "IFNULL(CAST(AVG(r.article_rating) AS DECIMAL(10,2)),0.00) AS avg_rating," +
            "IFNULL(COUNT(r.article_rating),0) AS number_of_ratings, " +
            "IFNULL(c.CommentCount,0) AS number_of_comments " +
            "FROM article a " +
            "JOIN publicist p ON p.id = a.publicist_id " +
            "LEFT JOIN (" +
            "  SELECT comment.article_id, COUNT(*) as CommentCount " +
            "  FROM comment " +
            "  WHERE comment.status = 1 " +
            "  GROUP BY article_id " +
            ") c ON a.id = c.article_id " +
            "LEFT JOIN rating r ON r.article_id = a.id " +
            "WHERE p.id = ? AND a.status = 1 AND (a.deployed_at <= CURRENT_TIMESTAMP OR a.deployed_at IS NULL)" +
            "GROUP BY a.id " +
            "ORDER BY a.created_at DESC;";
    public static final String SAVE_PUBLICIST = "INSERT INTO publicist (name, address, email, phone, created_at) " +
            "VALUES (?, ?, ?, ?, ?);";
    public static final String UPDATE_PUBLICIST = "UPDATE publicist SET " +
            "name = ?, " +
            "address = ?, " +
            "email = ?, " +
            "phone = ?, " +
            "modified_at = ?, " +
            "status = 1 " +
            "WHERE id = ?";
    public static final String DELETE_PUBLICIST = "UPDATE publicist SET " +
            "name = 'Névtelen Szerző', " +
            "address = NULL, " +
            "email = NULL, " +
            "phone = NULL, " +
            "status = 0, " +
            "modified_at = ? " +
            "WHERE id = ?";
    public static final String GET_PHONEBOOK = "SELECT name, phone FROM publicist WHERE status = 1";

    private PublicistQuery() {
    }
}
