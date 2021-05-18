package hu.progmasters.ujratervezes.week16.dailybugle.configuration;

import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ArticleListMapper implements RowMapper<ArticleListDto> {

    @Override
    public ArticleListDto mapRow(ResultSet resultSet, int i) throws SQLException {
        ArticleListDto article = new ArticleListDto();
        article.setId(resultSet.getInt("id"));
        article.setPublicistName(resultSet.getString("name"));
        article.setTitle(resultSet.getString("title"));
        article.setSynopsys(resultSet.getString("synopsys"));
        article.setAvgRating(resultSet.getDouble("avg_rating"));
        article.setNumOfRatings(resultSet.getInt("number_of_ratings"));
        return article;
    }
}
