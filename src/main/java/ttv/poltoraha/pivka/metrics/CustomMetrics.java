package ttv.poltoraha.pivka.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

// Как правило все имеющиеся метрики создаются в отдельном классе.
@Component
public class CustomMetrics {
    private final Counter requestCounter;
    private final Timer dbTimer;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.requestCounter = meterRegistry.counter("author_requests_count");
        this.dbTimer = meterRegistry.timer("author_db_timer");
    }

    public void incrementRequestCounter() {
        requestCounter.increment();
    }

    public Timer.Sample startDbTimer() {
        return Timer.start();
    }

    public void stopDbTimer(Timer.Sample sample) {
        sample.stop(dbTimer);
    }
}
