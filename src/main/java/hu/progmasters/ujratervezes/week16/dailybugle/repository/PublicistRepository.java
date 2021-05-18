package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Publicist;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PhonebookDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistCreateUpdateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PublicistRepository {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public PublicistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Publicist> getPublicists() {
        String sql = "SELECT * from publicist";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            Publicist publicist = new Publicist();
            publicist.setId(resultSet.getInt("id"));
            publicist.setName(resultSet.getString("name"));
            publicist.setAddress(resultSet.getString("address"));
            publicist.setEmail(resultSet.getString("email"));
            publicist.setPhone(resultSet.getString("phone"));
            return publicist;
        });
    }

    public boolean savePublicist(PublicistCreateUpdateData data, LocalDateTime now) {
        String sql = "INSERT INTO publicist (name, address, email, phone, created_at) VALUES (?, ?, ?, ?, ?);";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    data.getName(),
                    data.getAddress(),
                    data.getEmail(),
                    data.getPhone(),
                    now
            );
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public Publicist getPublicist(int id) {
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

    public boolean updatePublicist(int id, PublicistCreateUpdateData data, LocalDateTime now) {
        String sql = "UPDATE publicist SET " +
                "name = ?, " +
                "address = ?, " +
                "email = ?, " +
                "phone = ?, " +
                "modified_at = ?" +
                "WHERE id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    data.getName(),
                    data.getAddress(),
                    data.getEmail(),
                    data.getPhone(),
                    now,
                    id
            );
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean deletePublicist(int id, LocalDateTime now) {
        String sql = "UPDATE publicist SET " +
                "address = NULL, " +
                "email = NULL, " +
                "phone = NULL, " +
                "status = 0, " +
                "modified_at = ? " +
                "WHERE id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, now, id);
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public List<PhonebookDto> getPhonebook() {
        String sql = "SELECT name, phone FROM publicist";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            PhonebookDto phonebook = new PhonebookDto();
            phonebook.setName(resultSet.getString("name"));
            phonebook.setPhone(resultSet.getString("phone"));
            return phonebook;
        });

    }
}
