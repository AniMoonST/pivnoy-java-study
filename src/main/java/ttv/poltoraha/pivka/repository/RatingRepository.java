package ttv.poltoraha.pivka.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Quote;
import ttv.poltoraha.pivka.entity.Rating;
import ttv.poltoraha.pivka.entity.Reader;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Integer> {
    Optional<Rating> findByQuoteAndReader(Quote quote, Reader reader);
    // Метод для получения всех оценок по цитате
    List<Rating> findByQuote(Quote quote);

    // Метод для получения всех оценок по книге
    List<Rating> findByQuote_Book(Book book);
}
