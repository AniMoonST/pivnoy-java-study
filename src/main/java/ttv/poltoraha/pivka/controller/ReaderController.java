package ttv.poltoraha.pivka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ttv.poltoraha.pivka.entity.Reader;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.serviceImpl.ReaderServiceForNewReaders;
import ttv.poltoraha.pivka.serviceImpl.ReaderServiceImpl;

import java.util.Optional;

@RestController
@RequestMapping("/reader")
@RequiredArgsConstructor
public class ReaderController {
    private final ReaderServiceImpl readerService;
    private final ReaderServiceForNewReaders readerServiceForNewReaders;
    private final ReaderRepository readerRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username) {
        // Поиск пользователя по имени
        Optional<Reader> optionalReader = readerRepository.findByUsername(username);

        if (optionalReader.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }

        Reader reader = optionalReader.get();

        // Проверяем, новый ли пользователь
        if (reader.isNew()) {
            // Предлагаем обновить пароль и используем сервис для новых пользователей
            return ResponseEntity.ok("Пожалуйста, обновите ваш пароль.");
        } else {
            // Используем сервис для старых пользователей
            return ResponseEntity.ok("Добро пожаловать обратно, " + reader.getUsername() + "!");
        }
    }
    // RequestParam - это штука в запросе, которая выглядит так: localhost:8080/reader/create?username=value?password=value
    // Очевидно, что передавать пароли таким способом - небезопасно :)
    @PostMapping("/create")
    public ResponseEntity<?> createReader(@RequestParam String username, @RequestParam String password) {
        // Проверка на наличие имени пользователя и пароля
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Имя пользователя и пароль обязательны");
        }

        // Используем сервис для создания нового пользователя
        readerServiceForNewReaders.createReader(username, password);
        return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь успешно создан. Пожалуйста, обновите ваш пароль.");
    }
    @PostMapping("/addQuote")
    public ResponseEntity<?> addQuote(@RequestParam String username, @RequestParam Integer book_id, @RequestParam String text) {
        // Создание цитаты
        Optional<Reader> optionalReader = readerRepository.findByUsername(username);
        if (optionalReader.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }

        Reader reader = optionalReader.get();

        if (reader.isNew()) {
            return ResponseEntity.badRequest().body("Новые пользователи должны обновить пароль перед созданием цитат.");
        }

        readerService.createQuote(username, book_id, text);
        return ResponseEntity.ok("Цитата успешно создана.");
    }

    @PostMapping("/addFinishedBook")
    public ResponseEntity<?> addFinishedBook(@RequestParam String username, @RequestParam Integer book_id) {
        // Добавление завершенной книги
        Optional<Reader> optionalReader = readerRepository.findByUsername(username);
        if (optionalReader.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }

        Reader reader = optionalReader.get();
        readerService.addFinishedBook(username, book_id);
        return ResponseEntity.ok("Книга успешно добавлена в завершенные.");
    }
}
