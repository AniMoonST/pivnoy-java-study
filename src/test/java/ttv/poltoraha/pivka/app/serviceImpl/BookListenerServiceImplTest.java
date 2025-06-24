package ttv.poltoraha.pivka.app.serviceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import ttv.poltoraha.pivka.repository.BookRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "book_topic" })
@DirtiesContext
public class BookListenerServiceImplTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testBookListener() throws InterruptedException {
        // Отправляем 20 тестовых сообщений в Kafka
        for (int i = 0; i < 20; i++) {
            kafkaTemplate.send("book_topic", "{\"title\":\"Test Book Title " + i + "\"}");
        }

        // Ждем, пока сообщения будут обработаны
        Thread.sleep(2000);

        // Проверяем, что книга была сохранена в БД
        assertThat(bookRepository.count()).isEqualTo(15); // Проверяем, что сохранено 15 записей
    }
}