package hu.progmasters.ujratervezes.week16.dailybugle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReaderRatedArticleDto {

    private int articleId;
    private String articleTitle;
    private int ratingGiven;
}
