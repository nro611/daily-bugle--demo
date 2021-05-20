package hu.progmasters.ujratervezes.week16.dailybugle.controller;

import hu.progmasters.ujratervezes.week16.dailybugle.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reader")
public class ReaderController {

    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }
/*

    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReader(@PathVariable int id) {
        Reader reader = readerService.getReader(id);
        if (reader != null) {
            return new ResponseEntity<>(reader, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Void> saveReader(@RequestBody ReaderCreateUpdateData data) {
        boolean saveSuccessful = readerService.saveReader(data);
        if (saveSuccessful) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
*/

}
