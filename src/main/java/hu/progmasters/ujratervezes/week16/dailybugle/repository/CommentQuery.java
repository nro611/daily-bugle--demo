package hu.progmasters.ujratervezes.week16.dailybugle.repository;

public class CommentQuery {
    public static final String SAVE_COMMENT =
            "INSERT INTO comment (reader_id, comment_text, article_id, created_at) " +
                    "VALUES (?, ?, ?, ?)";
    public static final String GET_COMMENT =
            "SELECT comment.id, reader.username, comment.comment_text, comment.article_id " +
                    "FROM comment " +
                    "JOIN reader ON comment.reader_id = reader.id " +
                    "WHERE comment.id=?";
    public static final String GET_ARTICLE_COMMENTS =
            "SELECT comment.id, reader.username, comment.comment_text, comment.article_id " +
                    "FROM comment " +
                    "JOIN reader ON comment.reader_id = reader.id " +
                    "WHERE comment.article_id=?";

    private CommentQuery() {
    }

}
