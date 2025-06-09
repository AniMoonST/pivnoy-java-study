package ttv.poltoraha.pivka.serviceImpl;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.repository.AuthorRepository;
import ttv.poltoraha.pivka.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// Имплементации интерфейсов с бизнес-логикой
@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    // todo как будто надо насрать всякими мапперами
    @Override
    public void create(AuthorDto authorDto) {
        logger.info("Received request to create author: {}", authorDto);
        // Проверяем, существует ли автор с таким же именем
        if (authorRepository.existsByFullName(authorDto.getFullName())) {
            logger.error("Author with name = {} already exists", authorDto.getFullName());
            throw new EntityExistsException(String.format("Author with name = %s already exists", authorDto.getFullName()));
        }

        // Преобразуем AuthorDto в Author
        Author author = new Author();
        author.setFullName(authorDto.getFullName());
        author.setAvgRating(authorDto.getAvgRating()); // Если avgRating не нужен, можно убрать эту строку

        // Сохраняем автора в репозитории
        logger.info("Saving author to database: {}", author);
        authorRepository.save(author);
        logger.info("Successfully created author: {}", author);
    }

    @Override
    public void delete(Integer id) {
        logger.info("Received request to delete author with id: {}", id);
        authorRepository.deleteById(id);
        logger.info("Successfully deleted author with id: {}", id);
    }

    @Override
    public void addBooks(Integer id, List<Book> books) {
        logger.info("Received request to add books to author with id: {}", id);
        val author = getOrThrow(id);

        logger.info("Adding books to author: {}", author);
        author.getBooks().addAll(books);
        logger.info("Successfully added books to author with id: {}", id);
    }

    @Override
    public void addBook(Integer id, Book book) {
        logger.info("Received request to add a book to author with id: {}", id);
        val author = getOrThrow(id);

        logger.info("Adding book to author: {}", author);
        author.getBooks().add(book);
        logger.info("Successfully added book to author with id: {}", id);
    }

    @Override
    public List<Author> getTopAuthorsByTag(String tag, int count) {
        logger.info("Received request to get top authors by tag: {} with count: {}", tag, count);
        Pageable pageable = PageRequest.of(0, count);
        val authors = authorRepository.findTopAuthorsByTag(tag);
        logger.info("Successfully retrieved top authors by tag: {}", tag);
        return authorRepository.findTopAuthorsByTag(tag, pageable);
    }

    private Author getOrThrow(Integer id) {
        val optionalAuthor = authorRepository.findById(id);
        val author = optionalAuthor.orElse(null);

        if (author == null) {
            logger.error("Author with id = {} not found", id);
            throw new RuntimeException("Author with id = " + id + " not found");
        }

        return author;
    }
}
