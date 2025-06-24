package ttv.poltoraha.pivka.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity(name="quote")
@Data
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "reader_username")
    private Reader reader;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    private String text;
    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    public void addRating(Rating rating) {
        ratings.add(rating);
        rating.setQuote(this);
    }

    public void removeRating(Rating rating) {
        ratings.remove(rating);
        rating.setQuote(null);
    }
}
