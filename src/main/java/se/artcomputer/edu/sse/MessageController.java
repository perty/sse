package se.artcomputer.edu.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("messages")
public class MessageController {
    private static final Logger LOG = LoggerFactory.getLogger(MessageController.class);


    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getMessages(@RequestHeader HttpHeaders headers) {
        List<String> lastIds = headers.get("Last-Event-ID");
        LOG.info("Get messages: {} ", lastIds);
        String lastId = lastIds == null ? "0" : lastIds.get(0);
        return messageService.getMessages(lastId);
    }

    @GetMapping(path = "slow", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Message> getMessagesSlowly() {
        return messageService.slow();
    }

    @GetMapping(path = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Message> generate() {
        return messageService.generate();
    }
}