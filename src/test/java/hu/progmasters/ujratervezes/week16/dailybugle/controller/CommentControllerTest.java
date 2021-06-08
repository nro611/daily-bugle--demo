package hu.progmasters.ujratervezes.week16.dailybugle.controller;

import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentDto;
import hu.progmasters.ujratervezes.week16.dailybugle.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {
   
   CommentController commentController;
   
   @Mock
   CommentService commentServiceMock;
   
   @BeforeEach
   void setUp() {
      commentController = new CommentController(commentServiceMock);
   }
   
   @Test
   @DisplayName("Successful save comment get created")
   void saveComment_successful_created() {
      
      MockHttpServletRequest request = new MockHttpServletRequest();
      RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
      
      when(commentServiceMock.saveComment(Mockito.any(), Mockito.anyInt())).thenReturn(true);
   
      ResponseEntity<Void> responseEntity = commentController.saveComment(new CommentDto(), 2);
      
      assertEquals(201, responseEntity.getStatusCodeValue());
   
   }
   
   @Test
   @DisplayName("Fail save comment get bad request")
   void saveComment_fail_badRequest() {
      
      MockHttpServletRequest request = new MockHttpServletRequest();
      RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
      
      when(commentServiceMock.saveComment(Mockito.any(), Mockito.anyInt())).thenReturn(false);
   
      ResponseEntity<Void> responseEntity = commentController.saveComment(new CommentDto(), 2);
      
      assertEquals(400, responseEntity.getStatusCodeValue());
   
   }
}