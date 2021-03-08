package se.artcomputer.edu.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

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
                .map(m -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(m.getId()))
                        .data(m.getMessage())
                        .build());
    }
}

