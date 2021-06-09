package hu.progmasters.ujratervezes.week16.dailybugle.controller;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Reader;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderProfileDto;
import hu.progmasters.ujratervezes.week16.dailybugle.service.ReaderService;
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
class ReaderControllerTest {
   
   ReaderController readerController;
   
   @Mock
   ReaderService readerServiceMock;
   
   @BeforeEach
   void setUp() {
      readerController = new ReaderController(readerServiceMock);
   }
   
   @Test
   @DisplayName("Get readers successful status OK")
   void getReaders_successful() {
   
      when(readerServiceMock.getReaders()).thenReturn(List.of(new Reader()));
   
      ResponseEntity<List<Reader>> responseEntity = readerController.getReaders();
   
      assertEquals(200, responseEntity.getStatusCodeValue());
   
   }
   
   @Test
   @DisplayName("Get reader by ID status OK")
   void getReader_successful() {
      
      when(readerServiceMock.getReader(Mockito.anyInt())).thenReturn(new ReaderProfileDto());
   
      ResponseEntity<ReaderProfileDto> responseEntity = readerController.getReader(3);
   
      assertEquals(200, responseEntity.getStatusCodeValue());
   
   
   }
   
   @Test
   @DisplayName("Save reader successful status created")
   void saveReader_successful_created() {
      
      when(readerServiceMock.saveReader(Mockito.any())).thenReturn(true);
   
      ResponseEntity<Void> responseEntity = readerController.saveReader(new ReaderDto());
   
      assertEquals(201, responseEntity.getStatusCodeValue());
      
   }
   
   @Test
   @DisplayName("Update reader successful status OK")
   void updateReader_successful() {
   
      when(readerServiceMock.updateReader(Mockito.any(), Mockito.anyInt())).thenReturn(true);
   
      ResponseEntity<Void> responseEntity = readerController.updateReader(new ReaderDto(), 2);
   
      assertEquals(200, responseEntity.getStatusCodeValue());
   
   }
   
   @Test
   @DisplayName("Delete reader successful status OK")
   void deleteReader_successful() {
   
      when(readerServiceMock.deleteReader(Mockito.anyInt())).thenReturn(true);
   
      ResponseEntity<Void> responseEntity = readerController.deleteReader(2);
   
      assertEquals(200, responseEntity.getStatusCodeValue());
   
   }
}