package ttv.poltoraha.pivka.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ttv.poltoraha.pivka.entity.Quote;
import ttv.poltoraha.pivka.entity.Rating;
import ttv.poltoraha.pivka.entity.Reader;
import ttv.poltoraha.pivka.repository.QuoteRepository;
import ttv.poltoraha.pivka.repository.RatingRepository;
import ttv.poltoraha.pivka.repository.ReaderRepository;

import java.util.Optional;

@Service
public class RatingServiceImpl {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private ReaderRepository readerRepository;

    public void rateQuote(String username, Integer quoteId, int value) {
        Reader reader = readerRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Reader not found"));
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(() -> new RuntimeException("Quote not found"));

        // Проверка, существует ли уже оценка
        Optional<Rating> existingRating = ratingRepository.findByQuoteAndReader(quote, reader);
        if (existingRating.isPresent()) {
            throw new RuntimeException("You have already rated this quote.");
        }

        Rating rating = new Rating();
        rating.setQuote(quote);
        rating.setReader(reader);
        rating.setValue(value);

        ratingRepository.save(rating);
    }
}