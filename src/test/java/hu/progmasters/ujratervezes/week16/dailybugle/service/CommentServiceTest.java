package hu.progmasters.ujratervezes.week16.dailybugle.service;

import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentDto;
import hu.progmasters.ujratervezes.week16.dailybugle.repository.CommentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
   
   private CommentService commentService;
   
   @Mock
   private CommentRepository repositoryMock;
   
   private Clock clock = Clock.systemUTC();
   
   @BeforeEach
   void setUp() {
      commentService = new CommentService(repositoryMock, clock);
   }
   
   @Test
   @DisplayName("Save comment successful")
   void saveComment() {
      when(repositoryMock.saveComment(Mockito.any())).thenReturn(true);
      
      assertTrue(commentService.saveComment(new CommentDto(), 2));
      
      verify(repositoryMock, times(1)).saveComment(Mockito.any());
      verifyNoMoreInteractions(repositoryMock);
   }
}