package hu.progmasters.ujratervezes.week16.dailybugle.controller;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Article;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleImportPath;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleModifyDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ArticleRating;
import hu.progmasters.ujratervezes.week16.dailybugle.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return new ResponseEntity<>(articleService.getArticles(), HttpStatus.OK);
    }

    @GetMapping("/fresh")
    public ResponseEntity<List<ArticleListDto>> getFreshArticles() {
        // TODO: legfrissebb 10 cikk
        return new ResponseEntity<>(articleService.getFreshArticles(), HttpStatus.OK);
    }

    @GetMapping("/top")
    public ResponseEntity<List<ArticleListDto>> getTopArticles() {
        // TODO: Legjobban értékelt 10 cikk
        return null;
    }

    @GetMapping("/top_fresh")
    public ResponseEntity<List<ArticleListDto>> getTopFreshArticles() {
        // TODO: Legjobban értékelt 10 cikk, ami nem régebbi 3 napnál
        return null;
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
    public ResponseEntity<Void> saveArticle(@RequestBody ArticleImportPath articleImportPath) {
        boolean saveSuccessful = articleService.saveArticle(articleImportPath);
        if (saveSuccessful) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/rating/{id}")
    public ResponseEntity<Void> saveRating(@RequestBody ArticleRating data, @PathVariable int id) {
        boolean rateSuccessful = articleService.saveRating(data, id);
        if (rateSuccessful) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateArticle(@RequestBody ArticleModifyDto data, @PathVariable int id) {
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
