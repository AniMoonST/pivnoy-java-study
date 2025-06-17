package ttv.poltoraha.pivka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.metrics.CustomMetrics;
import ttv.poltoraha.pivka.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Counter;

import java.util.List;

// Контроллеры - это классы для создания внешних http ручек. Чтобы к нам могли прийти по http, например, через постман
// Или если у приложухи есть веб-морда, каждое действие пользователя - это http запросы
@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;
    private final Logger logger = LoggerFactory.getLogger(AuthorController.class);
    private final CustomMetrics customMetrics;

    public AuthorController(AuthorService authorService, CustomMetrics customMetrics) {
        this.authorService = authorService;
        this.customMetrics = customMetrics;
    }

    @PostMapping("/create")
    public void createAuthor(@RequestBody AuthorDto authorDto) {
        logger.info("Received request to create author: {}", authorDto);
        customMetrics.incrementRequestCounter(); // Увеличиваем счетчик запросов

        Timer.Sample sample = customMetrics.startDbTimer(); // Начинаем измерение времени
        try {
            authorService.create(authorDto); // Выполняем действие
            logger.info("Successfully created author: {}", authorDto);
        } finally {
            customMetrics.stopDbTimer(sample); // Останавливаем таймер
        }
    }

    @PostMapping("/delete")
    public void deleteAuthorById(@RequestParam Integer id) {
        logger.info("Received request to delete author with id: {}", id);
        customMetrics.incrementRequestCounter(); // Увеличиваем счетчик запросов

        Timer.Sample sample = customMetrics.startDbTimer(); // Начинаем измерение времени
        try {
            authorService.delete(id); // Выполняем действие
            logger.info("Successfully deleted author with id: {}", id);
        } finally {
            customMetrics.stopDbTimer(sample); // Останавливаем таймер
        }
    }

    @PostMapping("/add/books")
    public void addBooksToAuthor(@RequestParam Integer id, @RequestBody List<Book> books) {
        logger.info("Received request to add books to author with id: {}", id);
        logger.info("Books to add: {}", books);
        customMetrics.incrementRequestCounter(); // Увеличиваем счетчик запросов

        Timer.Sample sample = customMetrics.startDbTimer(); // Начинаем измерение времени
        try {
            authorService.addBooks(id, books); // Выполняем действие
            logger.info("Successfully added books to author with id: {}", id);
        } finally {
            customMetrics.stopDbTimer(sample); // Останавливаем таймер
        }
    }
}