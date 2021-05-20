package hu.progmasters.ujratervezes.week16.dailybugle.domain;

import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Article {
    private int id;
    private int publicistId;
    private String publicistName;
    private String title;
    private String synopsys;
    private String text;
    private double avgRating;
    private int numOfRatings;
    private List<CommentDto> comments;
}
