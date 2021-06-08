package hu.progmasters.ujratervezes.week16.dailybugle.service;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Publicist;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.repository.PublicistRepository;
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
class PublicistServiceTest {
   
   private PublicistService publicistService;
   
   @Mock
   private PublicistRepository repositoryMock;
   
   private Clock clock = Clock.systemUTC();
   
   @BeforeEach
   void setUp() {
      publicistService = new PublicistService(repositoryMock, clock);
   }
   
   @Test
   @DisplayName("Get all publicist in list")
   void getPublicists() {
      PublicistListDto publicistDto = new PublicistListDto();
      publicistDto.setName("John");
      publicistDto.setEmail("john@mail.com");
      publicistDto.setPhone("phone");
      publicistDto.setAddress("address");
      publicistDto.setId(2);
      
      when(repositoryMock.getPublicists()).thenReturn(List.of(publicistDto));
      
      List<PublicistListDto> publicists = publicistService.getPublicists();
      PublicistListDto publicist = publicists.get(0);
      
      assertEquals(1, publicists.size());
      assertEquals("John", publicist.getName());
      assertEquals("john@mail.com", publicist.getEmail());
      assertEquals("phone", publicist.getPhone());
      assertEquals("address", publicist.getAddress());
      assertEquals(2, publicist.getId());
   
      verify(repositoryMock, times(1)).getPublicists();
      verifyNoMoreInteractions(repositoryMock);
   
   }
   
   @Test
   @DisplayName("Save publicist successful")
   void savePublicist() {
      when(repositoryMock.savePublicist(Mockito.any(), Mockito.any())).thenReturn(true);
      
      assertTrue(publicistService.savePublicist(new PublicistDto()));
      
      verify(repositoryMock, times(1)).savePublicist(Mockito.any(), Mockito.any());
      verifyNoMoreInteractions(repositoryMock);
   }
   
   @Test
   @DisplayName("Get publicist by ID")
   void getPublicist() {
      Publicist publicist = new Publicist();
      publicist.setId(2);
      publicist.setName("John");
      publicist.setEmail("john@mail.com");
      publicist.setPhone("phone");
      
      when(repositoryMock.getPublicist(Mockito.anyInt())).thenReturn(publicist);
      
      Publicist resultPublicist = publicistService.getPublicist(2);
      
      assertEquals(2, resultPublicist.getId());
      assertEquals("John", resultPublicist.getName());
      assertEquals("john@mail.com", resultPublicist.getEmail());
      assertEquals("phone", resultPublicist.getPhone());
      
      verify(repositoryMock, times(1)).getPublicist(Mockito.anyInt());
      verifyNoMoreInteractions(repositoryMock);
   
   }
   
   @Test
   @DisplayName("Update publicist successful")
   void updatePublicist() {
      when(repositoryMock.updatePublicist(Mockito.anyInt(), Mockito.any(), Mockito.any())).thenReturn(true);
      
      assertTrue(publicistService.updatePublicist(1, new PublicistDto()));
   
      verify(repositoryMock, times(1)).updatePublicist(Mockito.anyInt(), Mockito.any(), Mockito.any());
      verifyNoMoreInteractions(repositoryMock);
   
   }
   
   @Test
   @DisplayName("Delete publicist successful")
   void deletePublicist() {
      when(repositoryMock.deletePublicist(Mockito.anyInt(), Mockito.any())).thenReturn(true);
      
      assertTrue(publicistService.deletePublicist(1));
   
      verify(repositoryMock, times(1)).deletePublicist(Mockito.anyInt(), Mockito.any());
      verifyNoMoreInteractions(repositoryMock);
   
   }
   
}