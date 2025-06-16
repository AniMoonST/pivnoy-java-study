package ttv.poltoraha.pivka.app.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.repository.AuthorRepository;
import ttv.poltoraha.pivka.service.AuthorService;
import ttv.poltoraha.pivka.serviceImpl.AuthorServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Используйте H2 вместо реальной БД
@Transactional // Обеспечивает откат транзакций после каждого теста
public class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository authorRepository; // Предполагается, что у вас есть репозиторий для работы с авторами

    private Author author;

    @BeforeEach
    public void setUp() {
        // Создаем автора и сохраняем его в репозитории
        author = new Author();
        author.setFullName("Test Author");
        author.setAvgRating(4.5);
        author.setBooks(new ArrayList<>());
        author = authorRepository.save(author); // Сохраняем автора
    }

    @Test
    public void testCreate() {
        // Создаем AuthorDto вместо Author
        AuthorDto authorDto = AuthorDto.builder()
                .fullName("Test Author")
                .avgRating(4.5)
                .build();

        // Получаем количество авторов до вызова метода create
        List<Author> authorsBefore = (List<Author>) authorRepository.findAll();

        // Вызываем метод create с AuthorDto
        authorService.create(authorDto);

        // Получаем количество авторов после вызова метода create
        List<Author> authorsAfter = (List<Author>) authorRepository.findAll();

        // Проверяем, что количество авторов увеличилось на 1
        assertEquals(authorsBefore.size() + 1, authorsAfter.size());

        // Проверяем, что сохраненный автор имеет правильные данные
        Author savedAuthor = authorsAfter.get(authorsAfter.size() - 1); // Получаем последнего сохраненного автора
        assertEquals("Test Author", savedAuthor.getFullName());
        assertEquals(4.5, savedAuthor.getAvgRating());
    }

    @Test
    public void testDelete() {
        Integer authorId = 1;

        // Сначала создаем автора, чтобы он существовал в репозитории
        Author author = new Author();
        author.setId(authorId);
        author.setFullName("Test Author");
        author.setAvgRating(4.5);
        authorRepository.save(author); // Сохраняем автора в репозитории

        // Вызываем метод delete
        authorService.delete(authorId);

        // Проверяем, что автор был удален
        assertFalse(authorRepository.existsById(authorId)); // Проверяем, что автор больше не существует
    }

    @Test
    public void testAddBook() {
        // Создаем книгу
        Book book = new Book();
        book.setArticle("Test Article");
        book.setGenre("Fiction");
        book.setRating(4.0);
        book.setTags("tag1, tag2");
        book.setAuthor(author); // Устанавливаем автора

        // Получаем текущее количество книг у автора
        int initialBookCount = author.getBooks().size();

        // Добавляем книгу
        authorService.addBook(author.getId(), book);

        // Проверяем, что книга была добавлена
        Author updatedAuthor = authorRepository.findById(author.getId()).orElseThrow();
        assertEquals(initialBookCount + 1, updatedAuthor.getBooks().size());
        assertTrue(updatedAuthor.getBooks().contains(book)); // Проверяем, что книга добавлена к автору
        assertEquals(updatedAuthor, book.getAuthor()); // Проверяем, что книга ссылается на автора
    }

    @Test
    public void testAddBooks() {
        List<Book> books = new ArrayList<>();

        Book book1 = new Book();
        book1.setId(1);
        book1.setArticle("Test Article 1");
        book1.setGenre("Fiction");
        book1.setRating(4.0);
        book1.setTags("tag1, tag2");
        book1.setAuthor(author); // Устанавливаем автора
        books.add(book1);

        Book book2 = new Book();
        book2.setId(2);
        book2.setArticle("Test Article 2");
        book2.setGenre("Non-Fiction");
        book2.setRating(4.5);
        book2.setTags("tag3, tag4");
        book2.setAuthor(author); // Устанавливаем автора
        books.add(book2);

        // Получаем текущее количество книг у автора
        int initialBookCount = author.getBooks().size();

        // Добавляем книги
        authorService.addBooks(author.getId(), books);

        // Проверяем, что книги были добавлены
        Author updatedAuthor = authorRepository.findById(author.getId()).orElseThrow();
        assertEquals(initialBookCount + books.size(), updatedAuthor.getBooks().size()); // Проверяем, что количество книг увеличилось
        assertTrue(updatedAuthor.getBooks().containsAll(books)); // Проверяем, что все книги добавлены к автору

        for (Book book : books) {
            assertEquals(updatedAuthor, book.getAuthor()); // Проверяем, что каждая книга ссылается на автора
        }
    }

    @Test
    public void testGetTopAuthorsByTag() {
        String tag = "Fiction";
        int count = 5;
    }
}
