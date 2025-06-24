package ttv.poltoraha.pivka.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Quote;
import ttv.poltoraha.pivka.entity.Reader;

import java.util.List;

@Repository
public interface QuoteRepository extends CrudRepository<Quote, Integer> {
    // Метод для поиска цитат по читателю
    List<Quote> findByReader(Reader reader);

    // Метод для поиска цитат по книге
    List<Quote> findByBook(Book book);

    // Метод для поиска цитат по тексту (например, по частичному совпадению)
    List<Quote> findByTextContaining(String text);
}
