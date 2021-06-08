package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Article;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleRatingDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentWithoutIdDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j(topic = "ArticleRepository")
public class ArticleRepository {
   
   private final JdbcTemplate jdbcTemplate;
   private final ArticleListMapper mapper;
   
   
   @Autowired
   public ArticleRepository(JdbcTemplate jdbcTemplate, ArticleListMapper mapper) {
      this.jdbcTemplate = jdbcTemplate;
      this.mapper = mapper;
   }
   
   public List<ArticleListDto> getArticles() {
      try {
         return jdbcTemplate.query(ArticleQuery.GET_ALL_ARTICLE.getSqlQuery(), mapper);
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return null;
      }
   }
   
   public List<ArticleListDto> getFreshArticles() {
      try {
         return jdbcTemplate.query(ArticleQuery.GET_FRESH_ARTICLE.getSqlQuery(), mapper);
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return null;
      }
   }
   
   public List<ArticleListDto> getTopArticles() {
      try {
         return jdbcTemplate.query(ArticleQuery.GET_TOP_ARTICLE.getSqlQuery(), mapper);
      }
      catch (DataAccessException exception) {
          logger.error(exception.getMessage());
          return null;
      }
   }
   
   public List<ArticleListDto> getTopFreshArticles() {
      try {
         return jdbcTemplate.query(ArticleQuery.GET_TOP_FRESH_ARTICLE.getSqlQuery(), mapper);
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return null;
      }
   }
   
   public Article getArticle(int id) {
      try {
         return jdbcTemplate.queryForObject(ArticleQuery.GET_ARTICLE_ID.getSqlQuery(), (resultSet, i) -> {
            Article article = new Article();
            article.setId(resultSet.getInt("id"));
            article.setPublicistId(resultSet.getInt("publicist_id"));
            article.setPublicistName(resultSet.getString("name"));
            article.setTitle(resultSet.getString("title"));
            article.setSynopsys(resultSet.getString("synopsys"));
            article.setText(resultSet.getString("text"));
            article.setAvgRating(resultSet.getDouble("avg_rating"));
            article.setNumOfRatings(resultSet.getInt("number_of_ratings"));
            article.setAvgRating(resultSet.getDouble("avg_rating"));
            article.setNumOfRatings(resultSet.getInt("number_of_ratings"));
            article.setComments(getCommentsForArticle(id));
            return article;
         }, id);
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return null;
      }
   }
   
   private List<CommentWithoutIdDto> getCommentsForArticle(int id) {
      return jdbcTemplate.query(ArticleQuery.GET_COMMENTS_FOR_ARTICLE_ID.getSqlQuery(), (resultSet, i) -> {
         CommentWithoutIdDto comment = new CommentWithoutIdDto();
         comment.setCommentAuthor(resultSet.getString("username"));
         comment.setCommentText(resultSet.getString("comment_text"));
         comment.setTime(resultSet.getTimestamp("created_at").toLocalDateTime());
         return comment;
      }, id);
   }
   
   public boolean saveRating(int readerId, int articleId, int rating, LocalDateTime now) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.SAVE_RATING.getSqlQuery(), readerId, articleId, rating, now);
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public boolean updateRating(int readerId, int articleId, int rating, LocalDateTime now) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.UPDATE_RATING.getSqlQuery(), rating, now, readerId, articleId);
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public ArticleRatingDto getRatingWithUserAndArticle(int readerId, int articleId) {
      try {
         return jdbcTemplate.queryForObject(ArticleQuery.GET_RATING_USER_ARTICLE.getSqlQuery(), (resultSet, i) -> {
            ArticleRatingDto rating = new ArticleRatingDto();
            rating.setReaderId(resultSet.getInt("reader_id"));
            rating.setRating(resultSet.getInt("article_rating"));
            return rating;
         }, readerId, articleId);
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return null;
      }
   }
   
   public boolean updateArticle(ArticleDto data, int id, LocalDateTime now) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.UPDATE_ARTICLE.getSqlQuery(),
                 data.getTitle(),
                 data.getSynopsys(),
                 data.getText(),
                 data.getDeployTime(),
                 now,
                 id
         );
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public boolean deleteArticle(int id, LocalDateTime now) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.DELETE_ARTICLE.getSqlQuery(), now, id);
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public boolean saveArticle(ArticleDto data, LocalDateTime now) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.SAVE_ARTICLE.getSqlQuery(),
                 data.getPublicistId(),
                 data.getTitle(),
                 data.getSynopsys(),
                 data.getText(),
                 now,
                 data.getDeployTime()
         );
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public List<String> getKeywords() {
      try {
         return jdbcTemplate.queryForList("SELECT keyword_name FROM keyword", String.class);
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return new ArrayList<>();
      }
   }
   
   public boolean saveKeyword(String keyword) {
      try {
         String sql = "INSERT INTO keyword (keyword_name) VALUES (?)";
         int rowsAffected = jdbcTemplate.update(sql, keyword);
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public List<Integer> getKeywordIds(String inSql, List<String> keywordsInDto) {
      List<Integer> keywordIds = new ArrayList<>();
      try {
         keywordIds = jdbcTemplate.queryForList(
                 String.format("SELECT id FROM keyword WHERE keyword_name IN (%s)", inSql),
                 Integer.class,
                 keywordsInDto.toArray()
         );
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
      }
      return keywordIds;
   }
   
   public Integer getArticleId(String title) {
      String sql = "SELECT id FROM article WHERE title = ?";
      try {
         return jdbcTemplate.queryForObject(sql, Integer.class, title);
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return 0;
      }
   }
   
   public boolean saveArticleKeyword(int articleId, int keywordId) {
      String sql = "INSERT INTO article_keyword (article_id, keyword_id) VALUES (?,?)";
      try {
         int rowsAffected = jdbcTemplate.update(sql, articleId, keywordId);
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public List<String> getKeywordsForArticle(int article_id) {
      try {
         return jdbcTemplate.queryForList(
                 ArticleQuery.GET_KEYWORDS_FOR_ARTICE_ID.getSqlQuery(),
                 String.class,
                 article_id
         );
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return new ArrayList<>();
      }
   }
   
   public boolean removeKeywords(int id, int numberToBeRemoved) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.DELETE_KEYWORDS_ARTICLEID.getSqlQuery(), id);
         return rowsAffected == numberToBeRemoved;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public boolean removeKeyword(int keyword) {
      String sql = "DELETE FROM keyword WHERE id = ?";
      try {
         int rowsAffected = jdbcTemplate.update(sql, keyword);
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public boolean removeArticleKeywords(int articleId, String inSql, List<Integer> idsToRemove) {
      try {
         idsToRemove.add(0, articleId);
         int rowsAffected = jdbcTemplate.update(
                 String.format("DELETE FROM article_keyword WHERE article_id = ? AND keyword_id IN (%s)", inSql),
                 idsToRemove.toArray()
         );
         return rowsAffected == idsToRemove.size() - 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
      }
      return false;
   }
   
   public boolean containsKeyword(Integer keywordId) {
      String sql = "SELECT article_id FROM article_keyword WHERE keyword_id = ? LIMIT 1";
      try {
         Integer articleId = jdbcTemplate.queryForObject(sql, Integer.class, keywordId);
         return articleId != null;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
}