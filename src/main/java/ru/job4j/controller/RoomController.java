package ru.job4j.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.service.RoomService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    @NonNull
    private RoomService service;

    @GetMapping("/")
    public List<Room> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findRoomById(@PathVariable int id) {
        return new ResponseEntity<>(
                service.findRoomById(id).
                        orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Room not found with id:" + id)), HttpStatus.OK);
    }

    @PutMapping("/{roomId}/message")
    public ResponseEntity<Void> postMessage(@RequestBody Message message,
                                            @PathVariable int roomId,
                                            @RequestParam int personId) {

        if (message.getBody() == null) {
            throw new NullPointerException("Message mustn't be empty");
        }
        service.postMessage(message, roomId, personId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/message/")
    public ResponseEntity<Void> patch(@RequestBody Message message) throws
            InvocationTargetException, IllegalAccessException {
        if (message.getId() == 0) {
            throw new IllegalArgumentException("Id cannot be empty");
        }
        service.patchMessage(message);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{roomId}/enter")
    public ResponseEntity<Void> enterRoom(@PathVariable int roomId, @RequestParam int personId) {
        if (personId == 0 || roomId == 0) {
            throw new IllegalArgumentException("id cannot be empty");
        }
        service.enterRoom(roomId, personId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{roomId}/exit")
    public ResponseEntity<Void> exitRoom(@PathVariable int roomId, @RequestParam int personId) {
        if (personId == 0 || roomId == 0) {
            throw new IllegalArgumentException("id cannot be empty");
        }
        service.exitRoom(roomId, personId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{roomId}/message")
    public ResponseEntity<Void> deleteMessage(@PathVariable int roomId, @RequestParam int messageId) {
        if (messageId == 0 || roomId == 0) {
            throw new IllegalArgumentException("id cannot be empty");
        }
        service.deleteMessage(roomId, messageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
