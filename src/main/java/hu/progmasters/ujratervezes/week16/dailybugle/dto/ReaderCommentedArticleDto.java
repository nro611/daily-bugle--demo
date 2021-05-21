package hu.progmasters.ujratervezes.week16.dailybugle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReaderCommentedArticleDto {

    private int articleId;
    private String articleTitle;
    private int commentCount;
}
