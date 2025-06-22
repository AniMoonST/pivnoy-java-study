package ttv.poltoraha.pivka.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity(name="reader")
@Data
@ToString
@DiscriminatorValue("READER")
public class Reader extends MyUser {

    // Тут хороший пример зачем вообще юзать каскад. В текущем виде мы сохраняем quotes через класс ReaderService и вызов
    // репозитория readerRepository. Если не установить каскад тип, то цитата просто не будет создана в бд
    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quote> quotes = new ArrayList<>();
    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reading> readings = new ArrayList<>();

    private boolean isNew; // Флаг для определения новых пользователей

    public void addQuote(Quote quote) {
        quotes.add(quote);
        quote.setReader(this);
    }

    public void removeQuote(Quote quote) {
        quotes.remove(quote);
        quote.setReader(null);
    }

    public void addReading(Reading reading) {
        readings.add(reading);
        reading.setReader(this);
    }

    public void removeReading(Reading reading) {
        readings.remove(reading);
        reading.setReader(null);
    }

    // Метод для установки флага isNew
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
}
