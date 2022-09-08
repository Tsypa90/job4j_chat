package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.service.RoomService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    @NonNull
    private RoomService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class.getName());
    private final ObjectMapper objectMapper;

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
        try {
            service.postMessage(message, roomId, personId);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Id of room or person not found");
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roomId}/enter")
    public ResponseEntity<Void> enterRoom(@PathVariable int roomId, @RequestParam int personId) {
        if (personId == 0 || roomId == 0) {
            throw new IllegalArgumentException("id cannot be empty");
        }
        try {
            service.enterRoom(roomId, personId);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Id of room or person not found");
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roomId}/exit")
    public ResponseEntity<Void> exitRoom(@PathVariable int roomId, @RequestParam int personId) {
        if (personId == 0 || roomId == 0) {
            throw new IllegalArgumentException("id cannot be empty");
        }
        try {
            service.exitRoom(roomId, personId);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Id of room or person not found");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}/message")
    public ResponseEntity<Void> deleteMessage(@PathVariable int roomId, @RequestParam int messageId) {
        try {
            service.deleteMessage(roomId, messageId);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Id of room not found");
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("Id of message not found", e.getExpectedSize());
        }
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({IllegalArgumentException.class,
            EmptyResultDataAccessException.class,
            NoSuchElementException.class})
    public void exceptionHandler(Exception e, HttpServletResponse res) throws IOException {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json");
        res.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        LOGGER.error(e.getLocalizedMessage());
    }
}
