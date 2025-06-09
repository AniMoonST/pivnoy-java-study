package ttv.poltoraha.pivka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// Контроллеры - это классы для создания внешних http ручек. Чтобы к нам могли прийти по http, например, через постман
// Или если у приложухи есть веб-морда, каждое действие пользователя - это http запросы
@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    @PostMapping("/create")
    public void createAuthor(@RequestBody AuthorDto authorDto) {
        logger.info("Received request to create author: {}", authorDto);
        authorService.create(authorDto);
        logger.info("Successfully created author: {}", authorDto);
    }

    @PostMapping("/delete")
    public void deleteAuthorById(@RequestParam Integer id) {
        logger.info("Received request to delete author with id: {}", id);
        authorService.delete(id);
        logger.info("Successfully deleted author with id: {}", id);
    }

    @PostMapping("/add/books")
    public void addBooksToAuthor(@RequestParam Integer id, @RequestBody List<Book> books) {
        logger.info("Received request to add books to author with id: {}", id);
        logger.info("Books to add: {}", books);
        authorService.addBooks(id, books);
        logger.info("Successfully added books to author with id: {}", id);
    }
}