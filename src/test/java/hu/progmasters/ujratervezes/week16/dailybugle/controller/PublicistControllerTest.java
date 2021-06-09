package hu.progmasters.ujratervezes.week16.dailybugle.controller;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Publicist;
import hu.progmasters.ujratervezes.week16.dailybugle.domain.Reader;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderDto;
import hu.progmasters.ujratervezes.week16.dailybugle.service.PublicistService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicistControllerTest {
   
   PublicistController publicistController;
   
   @Mock
   PublicistService publicistServiceMock;
   
   @BeforeEach
   void init() {
      publicistController = new PublicistController(publicistServiceMock);
   }
   
   @Test
   @DisplayName("Get publicist successful status OK")
   void getPublicists_successful() {
   
      when(publicistServiceMock.getPublicists()).thenReturn(List.of(new PublicistListDto()));
   
      ResponseEntity<List<PublicistListDto>> responseEntity = publicistController.getPublicists();
   
      assertEquals(200, responseEntity.getStatusCodeValue());
   
   }
   
   @Test
   @DisplayName("Get publicist by ID status OK")
   void getPublicist_successful() {
   
      when(publicistServiceMock.getPublicist(Mockito.anyInt())).thenReturn(new Publicist());
   
      ResponseEntity<Publicist> responseEntity = publicistController.getPublicist(2);
   
      assertEquals(200, responseEntity.getStatusCodeValue());
   
   }
   
   @Test
   @DisplayName("Save publicist successful status created")
   void savePublicist_successful() {
   
      when(publicistServiceMock.savePublicist(Mockito.any())).thenReturn(true);
   
      ResponseEntity<Void> responseEntity = publicistController.savePublicist(new PublicistDto());
   
      assertEquals(201, responseEntity.getStatusCodeValue());
   
   }
   
   @Test
   @DisplayName("Update publicist successful status OK")
   void updatePublicist_successful() {
   
      when(publicistServiceMock.updatePublicist(Mockito.anyInt(), Mockito.any())).thenReturn(true);
   
      ResponseEntity<Void> responseEntity = publicistController.updatePublicist(2, new PublicistDto());
   
      assertEquals(200, responseEntity.getStatusCodeValue());
   
   }
   
   @Test
   @DisplayName("Delete publicist successful status OK")
   void deletePublicist_successful() {
   
      when(publicistServiceMock.deletePublicist(Mockito.anyInt())).thenReturn(true);
   
      ResponseEntity<Void> responseEntity = publicistController.deletePublicist(2);
   
      assertEquals(200, responseEntity.getStatusCodeValue());
   
   }
   
   @Test
   void getPhonebook() {
   
      when(publicistServiceMock.getPhonebook()).thenReturn(true);
   
      ResponseEntity<Void> responseEntity = publicistController.getPhonebook();
   
      assertEquals(200, responseEntity.getStatusCodeValue());
   
   }
}