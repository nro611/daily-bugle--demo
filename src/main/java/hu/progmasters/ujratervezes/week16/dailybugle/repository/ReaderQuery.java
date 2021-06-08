package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import org.springframework.stereotype.Component;

@Component
public class ReaderQuery {
    public static final String GET_READERS = "SELECT " +
            "r.id, " +
            "r.username, " +
            "r.email, " +
            "IFNULL(cn.comment_count,0) AS number_of_comments " +
            "FROM reader r " +
            "LEFT JOIN (" +
            "   SELECT c.reader_id, COUNT(*) AS comment_count " +
            "   FROM comment c " +
            "   GROUP BY c.reader_id " +
            "           ) AS cn ON reader_id = r.id " +
            "WHERE r.status = 1";
    public static final String GET_READER = "SELECT r.username, r.email " +
            "FROM reader r " +
            "WHERE r.id = ?";
    public static final String GET_COMMENT_ARTICLES = "SELECT a.id AS article_id, a.title, COUNT(c.id) AS comment_count " +
            "FROM article a " +
            "LEFT JOIN comment c ON a.id = c.article_id " +
            "WHERE c.reader_id = ? " +
            "GROUP BY a.title";
    public static final String GET_RATED_ARTICLES = "SELECT a.id AS article_id, a.title, r.article_rating " +
            "FROM article a  " +
            "LEFT JOIN rating r ON a.id = r.article_id " +
            "WHERE r.reader_id = ?";
    public static final String SAVE_READER = "INSERT INTO reader (username, email, created_at) VALUES (?, ?, ?)";
    public static final String UPDATE_READER = "UPDATE reader SET " +
            "username = ?, " +
            "email = ?, " +
            "modified_at = ? " +
            "WHERE id = ?";
    public static final String DELETE_READER = "UPDATE reader SET " +
            "status = 0, " +
            "modified_at = ? " +
            "WHERE id = ?";

    private ReaderQuery() {
    }
}
