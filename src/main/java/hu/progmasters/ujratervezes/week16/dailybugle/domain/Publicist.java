package hu.progmasters.ujratervezes.week16.dailybugle.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Publicist {
    private int id;
    private String name;
    private String address;
    private String email;
    private String phone;
}
