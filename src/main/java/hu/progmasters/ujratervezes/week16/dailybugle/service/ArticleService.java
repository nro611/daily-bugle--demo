package hu.progmasters.ujratervezes.week16.dailybugle.service;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Article;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleImportPathDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleRatingDto;
import hu.progmasters.ujratervezes.week16.dailybugle.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

    // article_keyword table FKs need to be set to CASCADE ON DELETE!
    public boolean updateArticle(ArticleDto data, int id) {
        boolean updateSuccessful = articleRepository.updateArticle(data, id, LocalDateTime.now(clock));
        if (!updateSuccessful) return false;

        List<String> prevKeywords = articleRepository.getKeywordsForArticle(id);
        List<String> newKeywords = data.getKeywords();
        List<String> keywordsToSave = new ArrayList<>();

        if (newKeywords == null || newKeywords.size() == 0) {
            if (!articleRepository.removeKeywords(id, prevKeywords.size())) {
                return false;
            }
        } else {
            newKeywords.forEach(keyword -> {
                if (!prevKeywords.contains(keyword)) {
                    keywordsToSave.add(keyword);
                } else {
                    prevKeywords.remove(keyword);
                }
            });
            for (String keyword : prevKeywords) {
                if (!articleRepository.removeKeyword(keyword)) {
                    return false;
                }
            }
            updateSuccessful = isKeywordSaveSuccessful(keywordsToSave, id);
        }
        return updateSuccessful;
    }

    /**
     * Saves an article, stores the returning boolean in a variable. <p>
     * If saving was successful checks if @param data has keywords. If yes:  <p>
     * - Gets article id  <p>
     * - Gets existing keywords  <p>
     * - Gets keywords from @param data, converts them to lowercase and only keeps unique  <p>
     * - Saves new keywords in db  <p>
     * - Gets each keyword's (from @param data) id from db  <p>
     * - Saves keywords and article id in db
     *
     * @param data int publicistId,
     *             String title,
     *             String synopsys,
     *             String text,
     *             LocalDateTime deployTime,
     *             List<String> keywords
     * @return boolean depending if saving the article and the keywords was successful
     */
    public boolean saveArticle(ArticleDto data) {
        boolean saveSuccessful = articleRepository.saveArticle(data, LocalDateTime.now(clock));
        if (saveSuccessful && data.getKeywords() != null && data.getKeywords().size() > 0) {
            saveSuccessful = isKeywordSaveSuccessful(data.getKeywords(), articleRepository.getArticleId(data.getTitle()));
        }
        return saveSuccessful;
    }

    // article_keyword table FKs set to cascade upon delete/update
    // set article title to unique and remove publicistid param from articleId
    private boolean isKeywordSaveSuccessful(List<String> keywords, int articleId) {
        boolean saveSuccessful = true;
        List<String> keywordsInDb = articleRepository.getKeywords();
        List<String> keywordsInDto = keywords
                .stream()
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());

        for (String keyword : keywordsInDto) {
            if (!keywordsInDb.contains(keyword)) {
                if (!articleRepository.saveKeyword(keyword)) {
                    saveSuccessful = false;
                }
            }
        }
        List<Integer> keywordIdsToSave = getKeywordIds(keywordsInDto);
        for (Integer keywordId : keywordIdsToSave) {
            if (articleId == 0 || !articleRepository.saveArticleKeyword(articleId, keywordId)) {
                saveSuccessful = false;
            }
        }
        return saveSuccessful;
    }


    /**
     * inSql creates a String "(?,?,?,?,...)" with as many "?"s as many keywords are in the @param keywordsInDto,
     * seperated with ",". Used in sql query in repository.
     * This way query works with Lists of any size.
     *
     * @param keywordsInDto list of keywords which ids' are needed
     * @return an Integer list containing the ids for the keywords
     */
    private List<Integer> getKeywordIds(List<String> keywordsInDto) {
        String inSql = String.join(",", Collections.nCopies(keywordsInDto.size(), "?"));
        return articleRepository.getKeywordIds(inSql, keywordsInDto);
    }


    /**
     * Checks if the 0th index of the List (first line of the text file) is a String that can be parsed
     * as a LocalDateTime <p>
     * Said String should appear in the following format: <p>
     * YYYY-MM-DDThh:mm:ss <p>
     * e.g: 2007-12-03T10:15:30
     *
     * @param articleImportPathDto contains the path to the import file on the local computer
     * @return true or false depending on the success of the import and save
     */
    public boolean importArticle(ArticleImportPathDto articleImportPathDto) {
        int deployCounter = 1;
        List<String> lines = new ArrayList<>();
        boolean saveSuccessful = false;
        LocalDateTime deployTime = null;


        try {
            Path path = Path.of(articleImportPathDto.getPath());
            lines = Files.readAllLines(path);
            removeBlankLines(lines);
            deployTime = LocalDateTime.parse(lines.get(0));
        } catch (DateTimeParseException | IndexOutOfBoundsException e) {
            deployCounter = 0;
        } catch (IOException | InvalidPathException ignored) {
        }

        if (lines.size() >= 4 + deployCounter) {
            saveSuccessful = isSaveSuccessful(lines, deployCounter, deployTime);
        }

        return saveSuccessful;
    }

    private void removeBlankLines(List<String> lines) {
        Iterator<String> iterator = lines.iterator();
        String line;
        while (iterator.hasNext()) {
            line = iterator.next();
            if (line.isBlank()) {
                iterator.remove();
            }
        }
    }

    private boolean isSaveSuccessful(List<String> lines, int deployCounter, LocalDateTime deployTime) {
        boolean saveSuccessful;
        ArticleDto articleDto = new ArticleDto();
        articleDto.setPublicistId(Integer.parseInt(lines.get(deployCounter)));
        articleDto.setTitle(lines.get(deployCounter + 1));
        articleDto.setSynopsys(lines.get(deployCounter + 2));
        articleDto.setText(String.join("\n", lines.subList(deployCounter + 3, lines.size())));
        articleDto.setDeployTime(deployTime);


        saveSuccessful = articleRepository.saveArticle(articleDto, LocalDateTime.now(clock));

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
