package ru.job4j.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/")
    public List<Message> findAllByPersonId(@RequestParam int id) {
        return service.findAllByPersonId(id);
    }

    @PutMapping("/")
    public ResponseEntity<Void> updateMessage(@RequestBody Message message) {
        service.updateMessage(message);
        return ResponseEntity.ok().build();
    }
}
