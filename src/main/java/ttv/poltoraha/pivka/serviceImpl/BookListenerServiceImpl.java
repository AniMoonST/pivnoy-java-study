package ttv.poltoraha.pivka.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.repository.BookRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class BookListenerServiceImpl {

    private final BookRepository bookRepository; // Ваш репозиторий для работы с БД
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private int recordCount = 0;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Для десериализации JSON

    public BookListenerServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        startRateLimiter();
    }

    @KafkaListener(topics = "book_topic", groupId = "book_group")
    public void listen(String message) {
        try {
            // Десериализация JSON в объект Book
            Book newBook = objectMapper.readValue(message, Book.class);
            synchronized (this) {
                recordCount++;
                if (recordCount <= 15) {
                    bookRepository.save(newBook); // Сохраняем книгу в БД
                }
            }
        } catch (Exception e) {
            // Обработка ошибок десериализации
            e.printStackTrace();
        }
    }

    private void startRateLimiter() {
        executorService.scheduleAtFixedRate(() -> {
            synchronized (this) {
                recordCount = 0; // Сбрасываем счетчик
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}