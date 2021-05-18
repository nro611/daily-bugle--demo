package hu.progmasters.ujratervezes.week16.dailybugle.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ArticleQuery {
    GET_ALL("SELECT article.id, title, synopsys, publicist.name FROM article " +
            "JOIN publicist ON publicist.id = publicist_id"),
    GET_FRESH("SELECT article.id, title, synopsys, publicist.name FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "ORDER BY created_at DESC " +
            "LIMIT 10"),
    GET_TOP("SELECT article.id, title, synopsys, publicist.name FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "ORDER BY avg_rating DESC " +
            "LIMIT 10"),
    GET_TOP_FRESH("SELECT article.id, title, synopsys, publicist.name FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "WHERE sysdate() < ADDDATE(article.created_at, INTERVAL 3 DAY) " +
            "ORDER BY avg_rating DESC " +
            "LIMIT 10"),
    GET_ID("SELECT article.id, publicist_id, title, synopsys, text, publicist.name FROM article " +
            "JOIN publicist ON publicist.id = publicist_id " +
            "WHERE article.id = ?"),
    UPDATE("UPDATE article SET " +
            "title = ?, " +
            "synopsys = ?, " +
            "text = ?, " +
            "modified_at = ? " +
            "WHERE id = ?"),
    DELETE("UPDATE article SET " +
            "status = 0, " +
            "modified_at = ? " +
            "WHERE id = ?"),
    SAVE("INSERT INTO article (publicist_id, title, synopsys, text, created_at) " +
            "VALUES (?, ?, ?, ?, ?)");

    private final String sqlQuery;

}
