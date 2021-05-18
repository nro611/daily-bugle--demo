package hu.progmasters.ujratervezes.week16.dailybugle.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
