package hu.progmasters.ujratervezes.week16.dailybugle.service;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Article;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleImportPath;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleModifyDto;
import hu.progmasters.ujratervezes.week16.dailybugle.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final Clock clock;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, Clock clock) {
        this.articleRepository = articleRepository;
        this.clock = clock;
    }

    public List<ArticleListDto> getArticles() {
        return articleRepository.getArticles();
    }

    public Article getArticle(int id) {
        return articleRepository.getArticle(id);
    }

    public boolean updateArticle(ArticleModifyDto data, int id) {
        return articleRepository.updateArticle(data, id, LocalDateTime.now(clock));
    }

    public boolean deleteArticle(int id) {
        return articleRepository.deleteArticle(id, LocalDateTime.now(clock));
    }

    public boolean saveArticle(ArticleImportPath articleImportPath) {
        boolean saveSuccessful = false;
        List<String> lines = new ArrayList<>();
        Path path = Path.of(articleImportPath.getPath());

        try {
            lines = Files.readAllLines(path);
        } catch (IOException ioException) {
            return saveSuccessful;
        }

        Integer publicist_id = Integer.parseInt(lines.get(0));
        String title = lines.get(1);
        String synopsys = lines.get(2);
        String text = String.join("\n", lines.subList(3, lines.size()));

        saveSuccessful = articleRepository.saveArticle(publicist_id, title, synopsys, text, LocalDateTime.now(clock));

        return saveSuccessful;
    }
}
