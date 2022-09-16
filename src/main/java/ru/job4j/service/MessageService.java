package ru.job4j.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;
import ru.job4j.repository.MessageRepository;
import ru.job4j.repository.PersonRepository;
import ru.job4j.repository.RoomRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MessageService {
    @NonNull
    private final MessageRepository messageRepository;
    @NonNull
    private final PersonRepository personRepository;
    @NonNull
    private final RoomRepository roomRepository;

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

    public void postMessage(Message message, int roomId, int personId) {
        Room room = findRoom(roomId);
        Person person = findPerson(personId);
        message.setPerson(person);
        room.addMessage(message);
        roomRepository.save(room);
    }

    public void patchMessage(Message message) {
        var current = messageRepository
                .findById(message.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message to update not found"));
        var newValue = message.getBody();
        if (newValue != null) {
            current.setBody(newValue);
        }
        messageRepository.save(current);
    }

    public void deleteMessage(int roomId, int messageId) {
        var current = findMessage(messageId);
        var room = findRoom(roomId);
        room.deleteMessage(current);
        roomRepository.save(room);
    }

    private Room findRoom(int id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No room with id:" + id));
    }

    private Message findMessage(int id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No message with id:" + id));
    }

    private Person findPerson(int id) {
        return personRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("No person with id:" + id));
    }
}
