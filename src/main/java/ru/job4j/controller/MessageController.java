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
import ru.job4j.domain.Message;
import ru.job4j.service.MessageService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    @NonNull
    private final MessageService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class.getName());
    private final ObjectMapper objectMapper;

    @GetMapping("/")
    public List<Message> findAllByPersonId(@RequestParam int id) {
        return service.findAllByPersonId(id);
    }

    @PutMapping("/")
    public ResponseEntity<Void> updateMessage(@RequestBody Message message) {
        if (message.getBody() == null) {
            throw new NullPointerException("Message mustn't be empty");
        }
        if (message.getId() == 0) {
            throw new IllegalArgumentException("Id cannot be empty");
        }
        service.updateMessage(message);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
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
