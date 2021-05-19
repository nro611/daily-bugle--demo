package hu.progmasters.ujratervezes.week16.dailybugle.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Comment {

    private int id;
    private String commentAuthor;
    private String commentText;
    private int articleId;

}
