package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Publicist;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PhonebookDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistListDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@JdbcTest
class PublicistRepositoryTest {
   
   @Autowired
   private JdbcTemplate jdbcTemplate;
   
   private PublicistRepository repository;
   
   private final LocalDateTime CREATED_AT = LocalDateTime.of(2021, 11, 13, 10, 11);
   private final LocalDateTime MODIFIED_AT = LocalDateTime.of(2021, 12, 13, 10, 11);
   
   
   @BeforeEach
   void setUp() {
      String sql = "CREATE TABLE publicist(" +
              "id int primary key auto_increment," +
              "name varchar(200)," +
              "address varchar(200)," +
              "email varchar(200)," +
              "phone varchar(30)," +
              "status tinyint DEFAULT 1," +
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
   void testGetPublicists() {
      putPublicist();
      List<PublicistListDto> publicistList = repository.getPublicists();

      Assertions.assertEquals(1, publicistList.size());
      Assertions.assertEquals("Joe", publicistList.get(0).getName());
      Assertions.assertEquals("Budapest", publicistList.get(0).getAddress());
      Assertions.assertEquals("joe@mail.com", publicistList.get(0).getEmail());
      Assertions.assertEquals("phone", publicistList.get(0).getPhone());
   }

   @Test
   void testGetPublicist() {
      putPublicist();
      Publicist publicist = repository.getPublicist(1);

      assertEquals(1, publicist.getId());
      assertEquals("Joe", publicist.getName());
      assertEquals("Budapest", publicist.getAddress());
      assertEquals("joe@mail.com", publicist.getEmail());
      assertEquals("phone", publicist.getPhone());
   }

   @Test
   void testSavePublicist() {
      repository.savePublicist(getPublicistDto(), CREATED_AT);
      Publicist publicist = repository.getPublicist(1);

      assertEquals(1, publicist.getId());
      assertEquals("Joe", publicist.getName());
      assertEquals("Budapest", publicist.getAddress());
      assertEquals("joe@mail.com", publicist.getEmail());
      assertEquals("phone", publicist.getPhone());
   }

   @Test
   void testUpdatePublicist() {
      putPublicist();
      repository.updatePublicist(1, getUpdatedPublicistDto(), MODIFIED_AT);
      Publicist publicist = repository.getPublicist(1);

      assertEquals(1, publicist.getId());
      assertEquals("Joe Doe", publicist.getName());
      assertEquals("London", publicist.getAddress());
      assertEquals("joe@mail.com", publicist.getEmail());
      assertEquals("phone doe", publicist.getPhone());
   }

   @Test
   void testDeletePublicist() {
      putPublicist();
      repository.deletePublicist(1, MODIFIED_AT);
      Publicist publicist = getDeletedPublicist(1);

      assertEquals(1, publicist.getId());
      assertEquals("Névtelen Szerző", publicist.getName());
      assertNull(publicist.getAddress());
      assertNull(publicist.getEmail());
      assertNull(publicist.getPhone());
   }

   @Test
   void testGetPhonebook() {
      putPublicist();
      List<PhonebookDto> phoneBook = repository.getPhonebook();

      assertEquals(1, phoneBook.size());
      assertEquals("Joe", phoneBook.get(0).getName());
      assertEquals("phone", phoneBook.get(0).getPhone());

   }
   
   void putPublicist() {
      String sql = "INSERT INTO publicist(name, address, email, phone, created_at, modified_at)" +
              "VALUES(?, ?, ?, ?, ?, ?)";
      jdbcTemplate.update(sql, "Joe", "Budapest", "joe@mail.com", "phone", CREATED_AT, CREATED_AT);
   }
   
   PublicistDto getPublicistDto() {
      PublicistDto publicistDto = new PublicistDto();
      publicistDto.setName("Joe");
      publicistDto.setAddress("Budapest");
      publicistDto.setEmail("joe@mail.com");
      publicistDto.setPhone("phone");
      
      return publicistDto;
   }
   PublicistDto getUpdatedPublicistDto() {
      PublicistDto publicistDto = new PublicistDto();
      publicistDto.setName("Joe Doe");
      publicistDto.setAddress("London");
      publicistDto.setEmail("joe@mail.com");
      publicistDto.setPhone("phone doe");
      
      return publicistDto;
   }
   
   public Publicist getDeletedPublicist(int id) {
      String sql = "SELECT * FROM publicist WHERE id = ?";
      try {
         return jdbcTemplate.queryForObject(sql, (resultSet, i) -> {
            Publicist publicist = new Publicist();
            publicist.setId(resultSet.getInt("id"));
            publicist.setName(resultSet.getString("name"));
            publicist.setAddress(resultSet.getString("address"));
            publicist.setEmail(resultSet.getString("email"));
            publicist.setPhone(resultSet.getString("phone"));
            return publicist;
         }, id);
      } catch (EmptyResultDataAccessException e) {
         return null;
      }
   }
   
}