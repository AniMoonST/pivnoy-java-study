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

    @BeforeEach
    public void setUp() {
        // Создаем тестового автора и сохраняем его в репозитории
        Author author = new Author();
        author.setFullName("Test Author");
        author.setAvgRating(4.5);
        author.setBooks(new ArrayList<>());

        // Сохраняем автора в репозитории
        authorRepository.save(author);

        // Устанавливаем authorService, который будет использовать реальный репозиторий
        authorService = new AuthorServiceImpl(authorRepository);
    }

    @Test
    public void testCreate() {
        // Создаем AuthorDto вместо Author
        AuthorDto authorDto = AuthorDto.builder()
                .fullName("Test Author")
                .avgRating(4.5)
                .build();

        // Вызываем метод create с AuthorDto
        authorService.create(authorDto);

        // Проверяем, что метод save был вызван один раз с объектом Author
        ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository, times(1)).save(authorCaptor.capture());

        // Проверяем, что сохраненный автор имеет правильные данные
        Author savedAuthor = authorCaptor.getValue();
        assertEquals("Test Author", savedAuthor.getFullName());
        assertEquals(4.5, savedAuthor.getAvgRating());
    }

    @Test
    public void testDelete() {
        Integer authorId = 1;

        authorService.delete(authorId);

        verify(authorRepository, times(1)).deleteById(authorId); // Проверяем, что метод deleteById был вызван один раз
    }

    @Test
    public void testAddBook() {
        Integer authorId = 1;
        Book book = new Book();
        book.setId(1);
        book.setArticle("Test Article");
        book.setGenre("Fiction");
        book.setRating(4.5);
        book.setTags("tag1, tag2");

        // Создаем автора и добавляем его в репозиторий
        Author author = new Author();
        author.setId(authorId);
        author.setFullName("Test Author");
        author.setAvgRating(4.5);
        author.setBooks(new ArrayList<>());

        when(authorRepository.findById(authorId)).thenReturn(java.util.Optional.of(author));

        authorService.addBook(authorId, book);

        ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository, times(1)).save(authorCaptor.capture()); // Проверяем, что метод save был вызван один раз

        Author capturedAuthor = authorCaptor.getValue();
        assertNotNull(capturedAuthor);
        assertTrue(capturedAuthor.getBooks().contains(book)); // Проверяем, что книга добавлена к автору
        assertEquals(book.getAuthor(), capturedAuthor); // Проверяем, что книга ссылается на автора
    }

    @Test
    public void testAddBooks() {
        Integer authorId = 1;
        List<Book> books = new ArrayList<>();
        Book book1 = new Book();
        book1.setId(1);
        book1.setArticle("Test Article 1");
        book1.setGenre("Fiction");
        book1.setRating(4.0);
        book1.setTags("tag1, tag2");
        books.add(book1);
        Book book2 = new Book();
        book2.setId(2);
        book2.setArticle("Test Article 2");
        book2.setGenre("Non-Fiction");
        book2.setRating(4.5);
        book2.setTags("tag3, tag4");
        books.add(book2);

        // Создаем автора и добавляем его в репозиторий
        Author author = new Author();
        author.setId(authorId);
        author.setFullName("Test Author");
        author.setAvgRating(4.5);
        author.setBooks(new ArrayList<>());

        when(authorRepository.findById(authorId)).thenReturn(java.util.Optional.of(author));

        authorService.addBooks(authorId, books);

        ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository, times(1)).save(authorCaptor.capture()); // Проверяем, что метод save был вызван один раз

        Author capturedAuthor = authorCaptor.getValue();
        assertNotNull(capturedAuthor);
        assertTrue(capturedAuthor.getBooks().containsAll(books)); // Проверяем, что все книги добавлены к автору
        for (Book book : books) {
            assertEquals(capturedAuthor, book.getAuthor()); // Проверяем, что каждая книга ссылается на автора
        }
    }

    @Test
    public void testGetTopAuthorsByTag() {
        String tag = "Fiction";
        int count = 5;
    }
}
