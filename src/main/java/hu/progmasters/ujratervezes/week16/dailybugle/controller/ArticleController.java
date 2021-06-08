package hu.progmasters.ujratervezes.week16.dailybugle.controller;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Article;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleImportPathDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleRatingDto;
import hu.progmasters.ujratervezes.week16.dailybugle.service.ArticleService;
import hu.progmasters.ujratervezes.week16.dailybugle.service.TableDoesNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleListDto>> getArticles() {
        List<ArticleListDto> articles = articleService.getArticles();
        if (articles != null) {
            return new ResponseEntity<>(articles, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/fresh")
    public ResponseEntity<List<ArticleListDto>> getFreshArticles() {
        List<ArticleListDto> articles = articleService.getFreshArticles();
        if (articles != null) {
            return new ResponseEntity<>(articles, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/top")
    public ResponseEntity<List<ArticleListDto>> getTopArticles() {
        List<ArticleListDto> articles = articleService.getTopArticles();
        if (articles != null) {
            return new ResponseEntity<>(articles, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/top_fresh")
    public ResponseEntity<List<ArticleListDto>> getTopFreshArticles() {
        List<ArticleListDto> articles = articleService.getTopFreshArticles();
        if (articles != null) {
            return new ResponseEntity<>(articles, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable int id) {
        Article article = articleService.getArticle(id);
        if (article != null) {
            return new ResponseEntity<>(article, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<String> saveArticle(@RequestBody ArticleDto data) {
        boolean saveSuccessful;
        try {
            saveSuccessful = articleService.saveArticle(data);
        } catch (TableDoesNotExistException e) {
            logger.error(e.getMessage(), e.getCause());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (saveSuccessful) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importArticle(@RequestBody ArticleImportPathDto articleImportPathDto) {
        boolean saveSuccessful = articleService.importArticle(articleImportPathDto);
        if (saveSuccessful) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/rating/{id}")
    public ResponseEntity<Void> saveRating(@RequestBody ArticleRatingDto data, @PathVariable int id) {
        boolean rateSuccessful = articleService.saveRating(data, id);
        if (rateSuccessful) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateArticle(@RequestBody ArticleDto data, @PathVariable int id) {
        boolean updateSuccessful = articleService.updateArticle(data, id);
        if (updateSuccessful) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable int id) {
        boolean deleteSuccessful = (articleService.deleteArticle(id));
        if (deleteSuccessful) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
