package ttv.poltoraha.pivka.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Rating;
import ttv.poltoraha.pivka.service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecomendationController {

    private final RecommendationService recommendationService;

    /**
     * Рекомендует авторов на основе прочитанных книг пользователя.
     *
     * @param username - уникальный идентификатор читателя, его логин
     * @return список авторов для рекомендации
     */
    @GetMapping("/authors")
    public ResponseEntity<List<Author>> recommendAuthors(@RequestParam String username) {
        List<Author> authors = recommendationService.recommendAuthor(username);
        if (authors == null || authors.isEmpty()) {
            return ResponseEntity.noContent().build(); // Возвращаем 204, если нет авторов
        }
        return ResponseEntity.ok(authors);
    }

    /**
     * Рекомендует книги на основе прочитанных книг пользователя.
     *
     * @param username - уникальный идентификатор читателя, его логин
     * @return список книг для рекомендации
     */
    @GetMapping("/books")
    public ResponseEntity<List<Book>> recommendBooks(@RequestParam String username) {
        List<Book> books = recommendationService.recommendBook(username);
        if (books == null || books.isEmpty()) {
            return ResponseEntity.noContent().build(); // Возвращаем 204, если нет книг
        }
        return ResponseEntity.ok(books);
    }

    /**
     * Рекомендует цитаты для определённой книги.
     *
     * @param bookId - идентификатор книги
     * @return список подходящих цитат для книги
     */
    @GetMapping("/quotes/{bookId}")
    public ResponseEntity<List<Rating>> recommendQuotesByBook(@PathVariable Integer bookId) {
        List<Rating> quotes = recommendationService.recommendQuoteByBook(bookId);
        if (quotes == null || quotes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Возвращаем 204, если нет цитат
        }
        return ResponseEntity.ok(quotes);
    }
}
