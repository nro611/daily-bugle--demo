package hu.progmasters.ujratervezes.week16.dailybugle.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Reader {
    private int id;
    private String userName;
    private String email;
}
