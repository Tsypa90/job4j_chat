package ru.job4j.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class.getSimpleName());

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            NullPointerException.class})
    public void illAndNullExceptionHandler(Exception e, HttpServletResponse res) throws IOException {
        exceptionHandler(e, res, "Some fields wrong or empty");
        LOGGER.error(e.getMessage());
    }

    @ExceptionHandler({
            EmptyResultDataAccessException.class,
            NoSuchElementException.class,
    })
    public void noFindExceptionHandler(Exception e, HttpServletResponse res) throws IOException {
       exceptionHandler(e, res, "Entity not find");
    }

    private void exceptionHandler(Exception e, HttpServletResponse res, String message) throws IOException {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json");
        res.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", message);
            put("detail", e.getMessage());
        }}));
        LOGGER.error(e.getMessage());
    }
}
