package hu.progmasters.ujratervezes.week16.dailybugle.domain;

import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Publicist {
    private int id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private List<ArticleListDto> articles;
}
