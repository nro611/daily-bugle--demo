package hu.progmasters.ujratervezes.week16.dailybugle.service;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Article;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleRatingDto;
import hu.progmasters.ujratervezes.week16.dailybugle.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
   
   private ArticleService articleService;
   
   @Mock
   private ArticleRepository repositoryMock;
   
   private Clock clock = Clock.systemUTC();
   
   @BeforeEach
   void setUp() {
      articleService = new ArticleService(repositoryMock, clock);
   }
   
   public ArticleListDto getArticleListDto() {
      ArticleListDto dto = new ArticleListDto();
      dto.setId(1);
      dto.setTitle("title");
      dto.setPublicistName("publicist");
      dto.setNumOfComments(3);
      return dto;
   }
   
   @Test
   @DisplayName("Get articles to list")
   void getArticles() {
      when(repositoryMock.getArticles()).thenReturn(List.of(getArticleListDto()));
      
      List<ArticleListDto> articleDtos = articleService.getArticles();
      
      assertEquals(1, articleDtos.size());
      assertEquals("title", articleDtos.get(0).getTitle());
   
      verify(repositoryMock, times(1)).getArticles();
      verifyNoMoreInteractions(repositoryMock);
   
   }
   
   @Test
   @DisplayName("Get fresh articles to list")
   void getFreshArticles() {
      when(repositoryMock.getFreshArticles()).thenReturn(List.of(getArticleListDto()));
   
      List<ArticleListDto> articleDtos = articleService.getFreshArticles();
   
      assertEquals(1, articleDtos.size());
      assertEquals("title", articleDtos.get(0).getTitle());
   
      verify(repositoryMock, times(1)).getFreshArticles();
      verifyNoMoreInteractions(repositoryMock);
   
   }
   
   @Test
   @DisplayName("Get top articles")
   void getTopArticles() {
      when(repositoryMock.getTopArticles()).thenReturn(List.of(getArticleListDto()));
   
      List<ArticleListDto> articleDtos = articleService.getTopArticles();
   
      assertEquals(1, articleDtos.size());
      assertEquals("title", articleDtos.get(0).getTitle());
   
      verify(repositoryMock, times(1)).getTopArticles();
      verifyNoMoreInteractions(repositoryMock);
   
   }
   
   @Test
   @DisplayName("Get top fresh articles")
   void getTopFreshArticles() {
      when(repositoryMock.getTopFreshArticles()).thenReturn(List.of(getArticleListDto()));
   
      List<ArticleListDto> articleDtos = articleService.getTopFreshArticles();
   
      assertEquals(1, articleDtos.size());
      assertEquals("title", articleDtos.get(0).getTitle());
   
      verify(repositoryMock, times(1)).getTopFreshArticles();
      verifyNoMoreInteractions(repositoryMock);
   
   }
   
   @Test
   @DisplayName("Get article by ID")
   void getArticle() {
      Article articleToReturn = new Article();
      articleToReturn.setId(3);
      articleToReturn.setTitle("title");
      
      when(repositoryMock.getArticle(Mockito.anyInt())).thenReturn(articleToReturn);
   
      Article article = articleService.getArticle(3);
   
      assertEquals(3, article.getId());
      assertEquals("title", article.getTitle());
   
      verify(repositoryMock, times(1)).getArticle(Mockito.anyInt());
      verifyNoMoreInteractions(repositoryMock);
   
   }
   
   @Test
   @DisplayName("Save rating with valid data successful")
   void saveRating_validRating_successful() {
      when(repositoryMock.getRatingWithUserAndArticle(Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);
      when(repositoryMock.saveRating(anyInt(), anyInt(), anyInt(), any())).thenReturn(true);
   
      ArticleRatingDto rating = new ArticleRatingDto();
      rating.setRating(4);
      assertTrue(articleService.saveRating(rating, 2));
   
      verify(repositoryMock, times(1)).saveRating(anyInt(), anyInt(), anyInt(), any());
      verifyNoMoreInteractions(repositoryMock);
   
   
   }
   
   @Test
   @DisplayName("Delete article successful")
   void deleteArticle_successful() {
      when(repositoryMock.deleteArticle(anyInt(), any())).thenReturn(true);
      
      assertTrue(articleService.deleteArticle(2));
   
      verify(repositoryMock, times(1)).deleteArticle(Mockito.anyInt(), Mockito.any());
      verifyNoMoreInteractions(repositoryMock);
   
   }
}