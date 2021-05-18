package hu.progmasters.ujratervezes.week16.dailybugle.controller;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Publicist;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistCreateUpdateData;
import hu.progmasters.ujratervezes.week16.dailybugle.service.PublicistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicist")
public class PublicistController {

    private final PublicistService publicistService;

    @Autowired
    public PublicistController(PublicistService publicistService) {
        this.publicistService = publicistService;
    }

    @GetMapping
    public ResponseEntity<List<Publicist>> getPublicists() {
        return new ResponseEntity<>(publicistService.getPublicists(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> savePublicist(@RequestBody PublicistCreateUpdateData data) {
        boolean saveSuccesful = publicistService.savePublicist(data);
        if (saveSuccesful) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publicist> getPublicist(@PathVariable int id) {
        Publicist publicist = publicistService.getPublicist(id);
        if (publicist != null) {
            return new ResponseEntity<>(publicist, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePublicist(@PathVariable int id, @RequestBody PublicistCreateUpdateData data) {
        boolean updateSuccessful = publicistService.updatePublicist(id, data);
        if (updateSuccessful) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublicist(@PathVariable int id) {
        boolean deleteSuccessful = publicistService.deletePublicist(id);
        if (deleteSuccessful) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/phonebook")
    public ResponseEntity<Void> getPhonenook() {
        if (publicistService.getPhonebook()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
