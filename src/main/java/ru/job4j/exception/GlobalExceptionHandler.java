package ru.job4j.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class.getSimpleName());

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler({NullPointerException.class})
    public void exceptionHandler(Exception e, HttpServletResponse res) throws IOException {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json");
        res.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", "Some fields empty");
            put("detail", e.getMessage());
        }}));
        LOGGER.error(e.getMessage());
    }
}