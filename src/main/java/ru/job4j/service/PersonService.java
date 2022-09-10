package ru.job4j.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.exception.PersonNameNotUniqueException;
import ru.job4j.repository.PersonRepository;
import ru.job4j.repository.RoleRepository;

import java.util.List;
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

    public void deletePerson(int id) throws IllegalArgumentException {
        var person = personRepository.findById(id);
        if (person.isEmpty()) {
            throw new IllegalArgumentException("No found person with id:" + id);
        }
        personRepository.deleteById(id);
    }
}
