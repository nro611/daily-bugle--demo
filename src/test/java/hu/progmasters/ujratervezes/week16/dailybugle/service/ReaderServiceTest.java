package hu.progmasters.ujratervezes.week16.dailybugle.service;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Reader;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderProfileDto;
import hu.progmasters.ujratervezes.week16.dailybugle.repository.ReaderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {
   
   private ReaderService readerService;
   
   @Mock
   private ReaderRepository repositoryMock;
   
   private Clock clock = Clock.systemUTC();
   
   
   
   @BeforeEach
   void setUp() {
      readerService = new ReaderService(repositoryMock, clock);
   }
   
   @Test
   @DisplayName("Get all readers in list")
   void getReaders() {
      Reader reader = new Reader();
      reader.setId(1);
      reader.setUserName("John");
      reader.setEmail("john@mail.com");
      reader.setCommentCount(2);
      
      when(repositoryMock.getReaders()).thenReturn(List.of(reader));
      
      List<Reader> readers = readerService.getReaders();
      
      Reader resultReader = readers.get(0);
      
      assertEquals(1, readers.size());
      assertEquals(1, resultReader.getId());
      assertEquals("John", resultReader.getUserName());
      assertEquals("john@mail.com", resultReader.getEmail());
      assertEquals(2, resultReader.getCommentCount());
      
      verify(repositoryMock, times(1)).getReaders();
      verifyNoMoreInteractions(repositoryMock);
   }
   
   @Test
   @DisplayName("Get reader by ID")
   void getReader() {
      ReaderProfileDto reader = new ReaderProfileDto();
      reader.setName("John");
      reader.setEmail("john@mail.com");
      
      when(repositoryMock.getReader(Mockito.anyInt())).thenReturn(reader);
      
      ReaderProfileDto resultReader = readerService.getReader(2);
      
      assertEquals("John", resultReader.getName());
      assertEquals("john@mail.com", resultReader.getEmail());
   
      verify(repositoryMock, times(1)).getReader(Mockito.anyInt());
      verifyNoMoreInteractions(repositoryMock);
   
   }
   
   @Test
   @DisplayName("Save reader successful")
   void saveReaderSuccess() {
      when(repositoryMock.saveReader(Mockito.any(), Mockito.any())).thenReturn(true);
      
      assertTrue(readerService.saveReader(new ReaderDto()));
      
      verify(repositoryMock, times(1)).saveReader(Mockito.any(), Mockito.any());
      verifyNoMoreInteractions(repositoryMock);
   }
   
   @Test
   @DisplayName("Save reader fail")
   void saveReaderFail() {
      when(repositoryMock.saveReader(Mockito.any(), Mockito.any())).thenReturn(false);
      
      assertFalse(readerService.saveReader(new ReaderDto()));
      
      verify(repositoryMock, times(1)).saveReader(Mockito.any(), Mockito.any());
      verifyNoMoreInteractions(repositoryMock);
   }
   
   @Test
   @DisplayName("Update reader successful")
   void updateReaderSuccess() {
      when(repositoryMock.updateReader(Mockito.any(), Mockito.anyInt(), Mockito.any())).thenReturn(true);
      
      assertTrue(readerService.updateReader(new ReaderDto(), 2));
      
      verify(repositoryMock, times(1)).updateReader(Mockito.any(), Mockito.anyInt(), Mockito.any());
      verifyNoMoreInteractions(repositoryMock);
   }
   
   @Test
   @DisplayName("Update reader fail")
   void updateReaderFail() {
      when(repositoryMock.updateReader(Mockito.any(), Mockito.anyInt(), Mockito.any())).thenReturn(false);
      
      assertFalse(readerService.updateReader(new ReaderDto(), 2));
      
      verify(repositoryMock, times(1)).updateReader(Mockito.any(), Mockito.anyInt(), Mockito.any());
      verifyNoMoreInteractions(repositoryMock);
   }
   
   @Test
   @DisplayName("Delete reader successful")
   void deleteReaderSuccess() {
      when(repositoryMock.deleteReader(Mockito.anyInt(), Mockito.any())).thenReturn(true);
      
      assertTrue(readerService.deleteReader(2));
      
      verify(repositoryMock, times(1)).deleteReader(Mockito.anyInt(), Mockito.any());
      verifyNoMoreInteractions(repositoryMock);
   }
   
   @Test
   @DisplayName("Delete reader fail")
   void deleteReaderFail() {
      when(repositoryMock.deleteReader(Mockito.anyInt(), Mockito.any())).thenReturn(false);
      
      assertFalse(readerService.deleteReader(2));
      
      verify(repositoryMock, times(1)).deleteReader(Mockito.anyInt(), Mockito.any());
      verifyNoMoreInteractions(repositoryMock);
   }
}