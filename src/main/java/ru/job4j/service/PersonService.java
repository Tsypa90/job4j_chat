package ru.job4j.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
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

    public Optional<Person> findPersonById(int id) {
        return personRepository.findById(id);
    }

    public Person savePerson(Person person) {
        Optional<Person> rsl = personRepository.findById(person.getId());
        person.setRole(roleRepository.findAllByName(USER));
        if (rsl.isPresent()) {
            return personRepository.save(person);
        }
        return personRepository.save(person);
    }

    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }
}
