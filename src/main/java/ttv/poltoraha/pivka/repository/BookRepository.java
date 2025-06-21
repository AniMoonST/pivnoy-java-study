package ttv.poltoraha.pivka.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    @Query(value = "SELECT b.* FROM books b " +
            "JOIN authors a ON b.author_id = a.id " +
            "WHERE a.last_name = :lastName " +
            "AND a.rating = (SELECT MAX(a2.rating) FROM authors a2 WHERE a2.last_name = :lastName)",
            nativeQuery = true)
    List<Book> findBooksByAuthorWithHighestRating(String lastName);

    @Query("SELECT b FROM book b WHERE b.tags LIKE %:tag% ORDER BY b.rating DESC")
    List<Book> findTop3BooksByTag(@Param("tag") String tag);

    @Query("SELECT b FROM book b WHERE b.tags LIKE %:tag% ORDER BY b.rating DESC")
    List<Book> findTop2BooksByTag(@Param("tag") String tag);
}
