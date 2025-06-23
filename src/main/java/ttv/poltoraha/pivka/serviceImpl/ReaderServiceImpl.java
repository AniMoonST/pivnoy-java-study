package ttv.poltoraha.pivka.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Quote;
import ttv.poltoraha.pivka.entity.Reader;
import ttv.poltoraha.pivka.entity.Reading;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.service.ReaderService;
import util.MyUtility;

@Service
@RequiredArgsConstructor
@Transactional
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository quoteRepository; // Репозиторий для работы с читателями
    private final BookRepository bookRepository; // Репозиторий для работы с книгами

    @Override
    public void createQuote(String username, Integer bookId, String text) {
        // Находим читателя по имени пользователя
        Reader reader = quoteRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Reader with username = " + username + " was not found"));

        // Проверяем, является ли читатель новым
        if (reader.isNew()) {
            throw new IllegalStateException("New users must update their password before creating quotes.");
        }

        // Находим книгу по ID
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id = " + bookId + " was not found"));

        // Создаем новую цитату
        Quote newQuote = new Quote();
        newQuote.setBook(book);
        newQuote.setText(text);
        newQuote.setReader(reader);

        // Добавляем цитату в список читателя
        reader.addQuote(newQuote);

        // Сохраняем читателя, что также сохранит цитаты благодаря каскадированию
        quoteRepository.save(reader);
    }

    @Override
    public void addFinishedBook(String username, Integer bookId) {
        // Находим читателя по имени пользователя
        Reader reader = quoteRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Reader with username = " + username + " was not found"));

        // Находим книгу по ID
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id = " + bookId + " was not found"));

        // Создаем новое чтение
        Reading reading = new Reading();
        reading.setReader(reader);
        reading.setBook(book);

        // Добавляем чтение в список читателя
        reader.addReading(reading);

        // Сохраняем читателя, что также сохранит чтения благодаря каскадированию
        quoteRepository.save(reader);
    }

    @Override
    public void createReader(String username, String password) {
        // Создаем нового читателя
        Reader reader = new Reader();
        reader.setUsername(username);
        reader.setPassword(password);
        reader.setIsNew(true); // Устанавливаем флаг для новых пользователей

        // Сохраняем нового читателя
        quoteRepository.save(reader);
    }
}
