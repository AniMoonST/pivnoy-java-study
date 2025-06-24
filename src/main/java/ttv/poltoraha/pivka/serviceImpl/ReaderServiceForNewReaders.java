package ttv.poltoraha.pivka.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.entity.Reader;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.service.ReaderService;

@Service
@RequiredArgsConstructor
@Transactional
public class ReaderServiceForNewReaders implements ReaderService {
    private final ReaderRepository readerRepository;

    @Override
    public void createQuote(String username, Integer bookId, String text) {
        // Логика для новых пользователей
        System.out.println("Пожалуйста, обновите ваш пароль.");
        // Здесь можно добавить логику для создания цитаты, если это необходимо
    }

    @Override
    public void addFinishedBook(String username, Integer bookId) {
        // Логика для новых пользователей
    }

    @Override
    public void createReader(String username, String password) {
        Reader reader = new Reader();
        reader.setUsername(username);
        reader.setPassword(password);
        reader.setIsNew(true); // Устанавливаем флаг для новых пользователей

        readerRepository.save(reader);
    }
}