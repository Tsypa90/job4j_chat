package ru.job4j.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Person;
import ru.job4j.exception.PersonNameNotUniqueException;
import ru.job4j.repository.PersonRepository;
import ru.job4j.repository.RoleRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {
    @NonNull
    private final PersonRepository personRepository;
    private static final String USER = "USER";
    @NonNull
    private final RoleRepository roleRepository;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findByUserName(String name) {
        return personRepository.findPersonByName(name);
    }

    public Optional<Person> findPersonById(int id) {
        return personRepository.findById(id);
    }

    public Person savePerson(Person person) throws PersonNameNotUniqueException {
        var namePerson = personRepository.findPersonByName(person.getName());
        if (namePerson != null) {
            throw new PersonNameNotUniqueException("This name already exists");
        }
        person.setRole(roleRepository.findAllByName(USER));
        return personRepository.save(person);
    }

    public void patch(Person person) throws InvocationTargetException, IllegalAccessException {
        var current = personRepository.findById(person.getId());
        if (current.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person to update not found");
        }
        var methods = current.get().getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method: methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Impossible invoke set method from object : " + current + ", Check set and get pairs.");
                }
                var newValue = getMethod.invoke(person);
                if (newValue != null) {
                    setMethod.invoke(current.get(), newValue);
                }
            }
        }
        personRepository.save(current.get());
    }

    public void deletePerson(int id) {
        Person person = personRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("No found person with id:" + id));
        personRepository.deleteById(person.getId());
    }
}
