package se.artcomputer.edu.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class MessageService {
    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Flux<ServerSentEvent<String>> getMessages(String lastId) {
        int last = Integer.parseInt(lastId);
        return messageRepository.findByIdGreaterThan(last)
                .map(m -> ServerSentEvent.<String>builder()
                        .id(String.valueOf(m.getId()))
                        .data(m.getMessage())
                        .build());
    }

    public Flux<Message> slow() {
        return messageRepository.findAll()
                .delayElements(Duration.ofMillis(100))
                .map(this::toDto);
    }

    public Flux<Message> generate() {
        return Flux.range(0, 1000).flatMap(
                this::create
        );
    }

    private Mono<Message> create(Integer n) {
        LOG.info("Create {}", n);
        MessageEntity entity = new MessageEntity();
        entity.setMessage("This is generated " + n);
        return messageRepository.save(entity).map(this::toDto);
    }

    private Message toDto(MessageEntity messageEntity) {
        Message message = new Message();
        message.id = messageEntity.getId();
        message.message = messageEntity.getMessage();
        return message;
    }
}

