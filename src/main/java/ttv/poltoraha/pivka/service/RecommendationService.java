package ttv.poltoraha.pivka.service;

import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Rating;

import java.util.List;

public interface RecommendationService {
    public List<Author> recommendAuthor(String username);
    public List<Book> recommendBook(String username);
    public List<Rating> recommendQuoteByBook(Integer book_id);
}
