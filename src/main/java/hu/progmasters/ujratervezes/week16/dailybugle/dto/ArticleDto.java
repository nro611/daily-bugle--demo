package hu.progmasters.ujratervezes.week16.dailybugle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {
    private int publicistId;
    private String title;
    private String synopsys;
    private String text;
    private LocalDateTime deployTime;
    private List<String> keyWords;
}
