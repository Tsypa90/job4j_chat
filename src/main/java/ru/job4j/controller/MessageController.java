package ru.job4j.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    @NonNull
    private final MessageService service;

    @GetMapping
    public List<Message> findAllByPersonId(@RequestParam int id) {
        return service.findAllByPersonId(id);
    }

    @PostMapping
    public ResponseEntity<Void> postMessage(@RequestBody Message message,
                                            @RequestParam int roomId,
                                            @RequestParam int personId) {
        if (personId == 0 || roomId == 0) {
            throw new IllegalArgumentException("id cannot be empty");
        }
        if (message.getBody() == null) {
            throw new NullPointerException("Message mustn't be empty");
        }
        service.postMessage(message, roomId, personId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Void> patch(@RequestBody Message message) {
        checkMessage(message);
        service.patchMessage(message);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateMessage(@RequestBody Message message) {
        checkMessage(message);
        service.updateMessage(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@RequestParam int roomId, @PathVariable int messageId) {
        if (messageId == 0 || roomId == 0) {
            throw new IllegalArgumentException("id cannot be empty");
        }
        service.deleteMessage(roomId, messageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void checkMessage(Message message) {
        if (message.getBody() == null) {
            throw new NullPointerException("Message mustn't be empty");
        }
        if (message.getId() == 0) {
            throw new IllegalArgumentException("Id cannot be empty");
        }
    }
}
