package hu.progmasters.ujratervezes.week16.dailybugle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentWithoutIdDto {
    private String commentAuthor;
    private String commentText;
    private LocalDateTime time;
}
