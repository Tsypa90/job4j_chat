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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    @NonNull
    private final RoomRepository roomRepository;
    @NonNull
    private final PersonRepository personRepository;
    @NonNull
    private final MessageRepository messageRepository;


    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Optional<Room> findRoomById(int roomId) {
        return roomRepository.findById(roomId);
    }

    public void postMessage(Message message, int roomId, int personId) {
        Room room = findRoom(roomId);
        Person person = findPerson(personId);
        message.setPerson(person);
        room.addMessage(message);
        roomRepository.save(room);
    }

    public void patchMessage(Message message) throws InvocationTargetException, IllegalAccessException {
        var current = messageRepository.findById(message.getId());
        if (current.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message to update not found");
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
                var newValue = getMethod.invoke(message);
                if (newValue != null) {
                    setMethod.invoke(current.get(), newValue);
                }
            }
        }
        messageRepository.save(current.get());
    }

    public void enterRoom(int roomId, int personId) {
        Room room = findRoom(roomId);
        Person person = findPerson(personId);
        room.addPerson(person);
        roomRepository.save(room);
    }

    public void exitRoom(int roomId, int personId) {
        Room room = findRoom(roomId);
        Person person = findPerson(personId);
        room.deletePerson(person);
        roomRepository.save(room);
    }

    public void deleteMessage(int roomId, int messageId) {
        Room room = findRoom(roomId);
        Message message = findMessage(messageId);
        room.deleteMessage(message);
        roomRepository.save(room);
        messageRepository.deleteById(messageId);
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
