package se.artcomputer.edu.sse;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.stream.Stream;

public interface MessageRepository extends ReactiveCrudRepository<MessageEntity, Long> {
    Flux<MessageEntity> findByIdGreaterThan(long last);
}
