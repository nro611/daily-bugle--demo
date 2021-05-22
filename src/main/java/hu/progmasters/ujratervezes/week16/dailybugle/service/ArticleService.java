package hu.progmasters.ujratervezes.week16.dailybugle.service;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Article;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleImportPathDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleModifyDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleRatingDto;
import hu.progmasters.ujratervezes.week16.dailybugle.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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

    public List<ArticleListDto> getFreshArticles() {
        return articleRepository.getFreshArticles();
    }

    public List<ArticleListDto> getTopArticles() {
        return articleRepository.getTopArticles();
    }

    public List<ArticleListDto> getTopFreshArticles() {
        return articleRepository.getTopFreshArticles();
    }

    public Article getArticle(int id) {
        return articleRepository.getArticle(id);
    }

    public boolean updateArticle(ArticleModifyDto data, int id) {
        return articleRepository.updateArticle(data, id, LocalDateTime.now(clock));
    }

    public boolean saveArticle(ArticleImportPathDto articleImportPathDto) {
        int dC = 1; // deployCounter
        boolean saveSuccessful = false;
        LocalDateTime deployTime = null;
        List<String> lines;
        Path path = Path.of(articleImportPathDto.getPath());

        try {
            lines = Files.readAllLines(path);
        } catch (IOException ioException) {
            return saveSuccessful;
        }

        // Checks if the 0th index of the List (first line of the text file) is a String that can be parsed
        // as a LocalDateTime
        // Said String should appear in the following format:
        // YYYY-MM-DDThh:mm:ss
        // e.g: 2007-12-03T10:15:30
        try {
            deployTime = LocalDateTime.parse(lines.get(0));
        } catch (DateTimeParseException | IndexOutOfBoundsException e) {
            dC = 0;
        }

        if (lines.size() < 4 + dC) return saveSuccessful;

        saveSuccessful = isSaveSuccessful(lines, dC, deployTime);

        return saveSuccessful;
    }

    private boolean isSaveSuccessful(List<String> lines, int dC, LocalDateTime deployTime) {
        boolean saveSuccessful;
        Integer publicist_id = Integer.parseInt(lines.get(dC));
        String title = lines.get(dC + 1);
        String synopsys = lines.get(dC + 2);
        String text = String.join("\n", lines.subList(dC + 3, lines.size()));

        if (deployTime != null) {
            saveSuccessful = articleRepository.saveArticleWithDeployTime(
                    publicist_id,
                    title,
                    synopsys,
                    text,
                    LocalDateTime.now(clock),
                    deployTime
            );
        } else {
            saveSuccessful = articleRepository.saveArticle(publicist_id, title, synopsys, text, LocalDateTime.now(clock));
        }
        return saveSuccessful;
    }

    public boolean saveRating(ArticleRatingDto data, int articleId) {
        int rating = data.getRating();
        if (rating >= 1 && rating <= 5) {
            if (articleRepository.getRatingWithUserAndArticle(data.getReaderId(), articleId) != null) {
                return articleRepository.updateRating(data.getReaderId(), articleId, rating, LocalDateTime.now(clock));
            } else {
                return articleRepository.saveRating(data.getReaderId(), articleId, rating, LocalDateTime.now(clock));
            }
        } else {
            return false;
        }

    }

    public boolean deleteArticle(int id) {
        return articleRepository.deleteArticle(id, LocalDateTime.now(clock));
    }


}
