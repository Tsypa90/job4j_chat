package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.Operation;
import ru.job4j.domain.Person;
import ru.job4j.exception.GlobalExceptionHandler;
import ru.job4j.exception.PersonNameNotUniqueException;
import ru.job4j.service.PersonService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("person")
@RequiredArgsConstructor
@Validated
public class PersonController {
    @NonNull
    private final PersonService service;
    @NonNull
    private final BCryptPasswordEncoder encoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class.getSimpleName());

    private final ObjectMapper objectMapper;

    @GetMapping
    public List<Person> findAll() {
        return service.findAll();
    }

    @PostMapping("/sign-up")
    public void signUp(@Validated(Operation.OnCreate.class) @RequestBody Person person) throws
            PersonNameNotUniqueException {
        person.setPassword(encoder.encode(person.getPassword()));
        service.savePerson(person);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findDyId(@PathVariable @Positive(message = "Id is not positive") int id) {
        var person = service.findPersonById(id);
        return new ResponseEntity<>(
                person.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found")),
                HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> updatePerson(@Validated(Operation.OnUpdate.class) @RequestBody Person person) throws
            PersonNameNotUniqueException {
        person.setPassword(encoder.encode(person.getPassword()));
        service.savePerson(person);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> patch(@Validated(Operation.OnPatch.class) @RequestBody Person person) throws
            PersonNameNotUniqueException, InvocationTargetException, IllegalAccessException {
        if (person.getPassword() != null) {
            person.setPassword(encoder.encode(person.getPassword()));
        }
        service.patch(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable @Positive(message = "Id is not positive") int id) {
        service.deletePerson(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({PersonNameNotUniqueException.class})
    public void exceptionHandler(Exception e, HttpServletResponse res) throws IOException {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json");
        res.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", "person name not unique");
            put("detail", e.getMessage());
        }}));
        LOGGER.error(e.getMessage());
    }
}
