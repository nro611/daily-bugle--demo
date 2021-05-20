package hu.progmasters.ujratervezes.week16.dailybugle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArticleRating {
    private int readerId;
    private int rating;
}
