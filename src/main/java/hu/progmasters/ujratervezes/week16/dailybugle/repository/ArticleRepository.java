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
         return jdbcTemplate.query(ArticleQuery.GET_ARTICLES, mapper);
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return null;
      }
   }
   
   public List<ArticleListDto> getFreshArticles() {
      try {
         return jdbcTemplate.query(ArticleQuery.GET_FRESH_ARTICLES, mapper);
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return null;
      }
   }
   
   public List<ArticleListDto> getTopArticles() {
      try {
         return jdbcTemplate.query(ArticleQuery.GET_TOP_ARTICLES, mapper);
      }
      catch (DataAccessException exception) {
          logger.error(exception.getMessage());
          return null;
      }
   }
   
   public List<ArticleListDto> getTopFreshArticles() {
      try {
         return jdbcTemplate.query(ArticleQuery.GET_TOP_FRESH_ARTICLES, mapper);
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return null;
      }
   }
   
   public Article getArticle(int id) {
      try {
         return jdbcTemplate.queryForObject(ArticleQuery.GET_ARTICLE, (resultSet, i) -> {
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
      return jdbcTemplate.query(ArticleQuery.GET_COMMENTS_FOR_ARTICLE_ID, (resultSet, i) -> {
         CommentWithoutIdDto comment = new CommentWithoutIdDto();
         comment.setCommentAuthor(resultSet.getString("username"));
         comment.setCommentText(resultSet.getString("comment_text"));
         comment.setTime(resultSet.getTimestamp("created_at").toLocalDateTime());
         return comment;
      }, id);
   }
   
   public boolean saveRating(int readerId, int articleId, int rating, LocalDateTime now) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.SAVE_RATING, readerId, articleId, rating, now);
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public boolean updateRating(int readerId, int articleId, int rating, LocalDateTime now) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.UPDATE_RATING, rating, now, readerId, articleId);
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }

   public ArticleRatingDto getRatingWithUserAndArticle(int readerId, int articleId) {
      ArticleRatingDto articleDto = null;
      try {
         List<ArticleRatingDto> articleRatingDtos = jdbcTemplate.query(ArticleQuery.GET_RATING_USER_ARTICLE, (resultSet, i) -> {
            ArticleRatingDto rating = new ArticleRatingDto();
            rating.setReaderId(resultSet.getInt("reader_id"));
            rating.setRating(resultSet.getInt("article_rating"));
            return rating;
         }, readerId, articleId);
         if (!articleRatingDtos.isEmpty()) {
            articleDto = articleRatingDtos.get(0);
         }
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
      }
      return articleDto;
   }
   
   public boolean updateArticle(ArticleDto data, int id, LocalDateTime now) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.UPDATE_ARTICLE,
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
         int rowsAffected = jdbcTemplate.update(ArticleQuery.DELETE_ARTICLE, now, id);
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public boolean saveArticle(ArticleDto data, LocalDateTime now) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.SAVE_ARTICLE,
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
         return jdbcTemplate.queryForList(ArticleQuery.GET_KEYWORDS, String.class);
      } catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return new ArrayList<>();
      }
   }
   
   public boolean saveKeyword(String keyword) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.SAVE_KEYWORD, keyword);
         return rowsAffected == 1;
      } catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         throw new TableDoesNotExistException("Table not found.", exception);
      }
   }
   
   public List<Integer> getKeywordIds(String inSql, List<String> keywordsInDto) {
      List<Integer> keywordIds = new ArrayList<>();
      try {
         keywordIds = jdbcTemplate.queryForList(
                 String.format(ArticleQuery.GET_KEYWORD_IDS, inSql),
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
      try {
         return jdbcTemplate.queryForObject(ArticleQuery.GET_ARTICLE_ID, Integer.class, title);
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return 0;
      }
   }
   
   public boolean saveArticleKeyword(int articleId, int keywordId) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.SAVE_ARTICLE_KEYWORD, articleId, keywordId);
         return rowsAffected == 1;
      }
      catch (DataAccessException exception) {
         throw new TableDoesNotExistException("Table not found.", exception);
      }
   }
   
   public List<String> getKeywordsForArticle(int article_id) {
      try {
         return jdbcTemplate.queryForList(
                 ArticleQuery.GET_KEYWORDS_FOR_ARTICE_ID,
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
         int rowsAffected = jdbcTemplate.update(ArticleQuery.DELETE_KEYWORDS_ARTICLEID, id);
         return rowsAffected == numberToBeRemoved;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
   
   public boolean removeKeyword(int keyword) {
      try {
         int rowsAffected = jdbcTemplate.update(ArticleQuery.REMOVE_KEYWORD, keyword);
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
                 String.format(ArticleQuery.REMOVE_ARTICLE_KEYWORDS, inSql),
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
      try {
         Integer articleId = jdbcTemplate.queryForObject(ArticleQuery.CONTAINS_KEYWORD, Integer.class, keywordId);
         return articleId != null;
      }
      catch (DataAccessException exception) {
         logger.error(exception.getMessage());
         return false;
      }
   }
}