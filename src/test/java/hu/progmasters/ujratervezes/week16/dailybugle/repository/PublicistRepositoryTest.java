package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Publicist;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistListDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class PublicistRepositoryTest {
   
   @Autowired
   private JdbcTemplate jdbcTemplate;
   
   private PublicistRepository repository;
   
   private LocalDateTime CREATEDAT = LocalDateTime.of(2021, 12, 13, 10, 11);
   
   
   @BeforeEach
   void setUp() {
      String sql = "CREATE TABLE publicist(" +
              "id int primary key auto_increment," +
              "name varchar(200)," +
              "address varchar(200)," +
              "email varchar(200)," +
              "phone varchar(30)," +
              "status tinyint(1)," +
              "created_at datetime," +
              "modified_at datetime);";
      
      repository = new PublicistRepository(jdbcTemplate);
      jdbcTemplate.execute(sql);
   }
   
   @AfterEach
   void tearDown() {
      jdbcTemplate.execute("DROP TABLE publicist");
   }
   
   @Test
   void getPublicists() {
      putPublicist();
      List<PublicistListDto> publicistList = repository.getPublicists();
   
      Assertions.assertEquals(1, publicistList.size());
      Assertions.assertEquals("Joe", publicistList.get(0).getName());
      Assertions.assertEquals("Budapest", publicistList.get(0).getAddress());
      Assertions.assertEquals("joe@mail.com", publicistList.get(0).getEmail());
      Assertions.assertEquals("phone", publicistList.get(0).getPhone());
   }
   
   @Test
   void getPublicist() {
      putPublicist();
      Publicist publicist = repository.getPublicist(1);
      
      assertEquals(1, publicist.getId());
      assertEquals("Joe", publicist.getName());
      assertEquals("Budapest", publicist.getAddress());
      assertEquals("joe@mail.com", publicist.getEmail());
      assertEquals("phone", publicist.getPhone());
   }
   
   @Test
   void savePublicist() {
      repository.savePublicist(getPublicistDto(), CREATEDAT);
      Publicist publicist = repository.getPublicist(1);
   
      assertEquals(1, publicist.getId());
      assertEquals("Joe", publicist.getName());
      assertEquals("Budapest", publicist.getAddress());
      assertEquals("joe@mail.com", publicist.getEmail());
      assertEquals("phone", publicist.getPhone());
   }
   
   @Test
   void updatePublicist() {
   }
   
   @Test
   void deletePublicist() {
   }
   
   @Test
   void getPhonebook() {
   }
   
   void putPublicist() {
      String sql = "INSERT INTO publicist(name, address, email, phone, status, created_at, modified_at)" +
              "VALUES(?, ?, ?, ?, ?, ?, ?)";
      jdbcTemplate.update(sql, "Joe", "Budapest", "joe@mail.com", "phone", 1, CREATEDAT, CREATEDAT);
   }
   
   PublicistDto getPublicistDto() {
      PublicistDto publicistDto = new PublicistDto();
      publicistDto.setName("Joe");
      publicistDto.setAddress("Budapest");
      publicistDto.setEmail("joe@mail.com");
      publicistDto.setPhone("phone");
      
      return publicistDto;
   }
}