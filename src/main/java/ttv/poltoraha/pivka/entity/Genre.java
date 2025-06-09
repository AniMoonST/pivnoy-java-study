package ttv.poltoraha.pivka.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

// Энтити - это привязка класса к конкретной табличке в БД
@Entity(name="genre")
@Data
@ToString
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer genre_id;
    private String genreName;
    @OneToMany(mappedBy="genre")
    @ToString.Exclude
    private List<Book> books;
}