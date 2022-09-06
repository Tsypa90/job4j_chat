package ru.job4j.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.service.RoomService;

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
        return new ResponseEntity<>(service.findRoomById(id), HttpStatus.OK);
    }

    @PutMapping("/{roomId}/message")
    public ResponseEntity<Void> postMessage(@RequestBody Message message, @PathVariable int roomId, @RequestParam int personId) {
        service.postMessage(message, roomId, personId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roomId}/enter")
    public ResponseEntity<Void> enterRoom(@PathVariable int roomId, @RequestParam int personId) {
        service.enterRoom(roomId, personId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roomId}/exit")
    public ResponseEntity<Void> exitRoom(@PathVariable int roomId, @RequestParam int personId) {
        service.exitRoom(roomId, personId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}/message")
    public ResponseEntity<Void> deleteMessage(@PathVariable int roomId, @RequestParam int messageId) {
        service.deleteMessage(roomId, messageId);
        return ResponseEntity.ok().build();
    }
}
