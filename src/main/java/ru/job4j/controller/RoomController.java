package ru.job4j.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Room;
import ru.job4j.service.RoomService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
@Validated
public class RoomController {
    @NonNull
    private RoomService service;

    @GetMapping
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

    @PutMapping("/{roomId}/enter")
    public ResponseEntity<Void> enterRoom(@PathVariable @Positive(message = "Id is not positive") int roomId,
                                          @RequestParam @Positive(message = "Id is not positive") int personId) {
        service.enterRoom(roomId, personId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{roomId}/exit")
    public ResponseEntity<Void> exitRoom(@PathVariable @Positive(message = "Id is not positive") int roomId,
                                         @RequestParam @Positive(message = "Id is not positive") int personId) {
        service.exitRoom(roomId, personId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
