package hu.progmasters.ujratervezes.week16.dailybugle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommentDto {
    private String commentAuthor;
    private String commentText;
    private LocalDateTime time;
}