package hu.progmasters.ujratervezes.week16.dailybugle.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReaderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReaderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
