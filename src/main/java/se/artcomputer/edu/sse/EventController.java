package se.artcomputer.edu.sse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class EventController {

    private static final List<String> WORDS = Arrays.asList("The quick brown fox jumps over the lazy dog.".split(" "));

    @GetMapping(path = "/words", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getWords() {
        return Flux
                .zip(Flux.fromIterable(WORDS), Flux.interval(Duration.ofSeconds(1)))
                .map(Tuple2::getT1);
    }

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> createConnectionAndSendEvents() {
        return Flux.just("Alpha", "Omega");
    }

    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> events() {
        return Flux.interval(Duration.ofSeconds(1)).map(s -> readValue());
    }

    private String readValue() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}