package hu.progmasters.ujratervezes.week16.dailybugle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReaderProfileDto {

    private String name;
    private String email;
    private List<ReaderRatedArticleDto> ratedArticles;
    private List<ReaderCommentedArticleDto> commentedArticles;
}
