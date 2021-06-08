package hu.progmasters.ujratervezes.week16.dailybugle.service;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Reader;
import hu.progmasters.ujratervezes.week16.dailybugle.repository.ReaderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
   
   @Autowired
   private Clock clock;
   
   
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
   void getReader() {
   }
   
   @Test
   void saveReader() {
   }
   
   @Test
   void updateReader() {
   }
   
   @Test
   void deleteReader() {
   }
}