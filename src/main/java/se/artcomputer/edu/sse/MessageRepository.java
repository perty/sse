package se.artcomputer.edu.sse;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MessageRepository extends ReactiveCrudRepository<MessageEntity, Long> {
    Flux<MessageEntity> findByIdGreaterThan(long last);
}
