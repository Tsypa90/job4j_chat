package ru.job4j.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.repository.MessageRepository;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MessageService {
    @NonNull
    private final MessageRepository messageRepository;
    @NonNull
    private final PersonRepository personRepository;

    public List<Message> findAllByPersonId(int id) {
        Person person = personRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("No person with id:" + id));
        return messageRepository.findAllByPersonId(person.getId());
    }

    public void updateMessage(Message message) {
        Message messageDb = messageRepository
                .findById(message.getId())
                .orElseThrow(() -> new NoSuchElementException("No message with id:" + message.getId()));
        messageDb.setBody(message.getBody());
        messageRepository.save(messageDb);
    }
}
