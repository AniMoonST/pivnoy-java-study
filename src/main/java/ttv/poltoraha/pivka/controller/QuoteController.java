package ttv.poltoraha.pivka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ttv.poltoraha.pivka.serviceImpl.RatingServiceImpl;

@RestController
@RequestMapping("/quotes")
public class QuoteController {
    @Autowired
    private RatingServiceImpl ratingServiceImpl;

    @PostMapping("/{quoteId}/rate")
    public ResponseEntity<String> rateQuote(@PathVariable Integer quoteId, @RequestParam String username, @RequestParam int value) {
        ratingServiceImpl.rateQuote(username, quoteId, value);
        return ResponseEntity.ok("Rating submitted successfully.");
    }
}
