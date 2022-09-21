package ru.job4j.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.validation.OperationOnValidation;
import ru.job4j.domain.Message;
import ru.job4j.service.MessageService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Validated
public class MessageController {
    @NonNull
    private final MessageService service;

    @GetMapping
    public List<Message> findAllByPersonId(@RequestParam @Positive(message = "Id is not positive") int id) {
        return service.findAllByPersonId(id);
    }

    @PostMapping
    public ResponseEntity<Void> postMessage(@Validated(OperationOnValidation.OnCreate.class) @RequestBody Message message,
                                            @RequestParam @Positive(message = "Id is not positive") int roomId,
                                            @RequestParam @Positive(message = "Id is not positive") int personId) {
        service.postMessage(message, roomId, personId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Void> patch(@Validated(OperationOnValidation.OnPatch.class) @RequestBody Message message) {
        service.patchMessage(message);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateMessage(@Validated(OperationOnValidation.OnUpdate.class) @RequestBody Message message) {
        service.updateMessage(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@RequestParam @Positive(message = "Id is not positive") int roomId,
                                              @PathVariable @Positive(message = "Id is not positive") int messageId) {
        service.deleteMessage(roomId, messageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
