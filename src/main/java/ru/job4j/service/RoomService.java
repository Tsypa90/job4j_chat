package ru.job4j.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.repository.MessageRepository;
import ru.job4j.repository.PersonRepository;
import ru.job4j.repository.RoomRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
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
        Optional<Room> room = roomRepository.findById(roomId);
        message.setPerson(personRepository.findById(personId).get());
        room.get().addMessage(message);
        roomRepository.save(room.get());
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
        Optional<Room> room = roomRepository.findById(roomId);
        room.get().addPerson(personRepository.findById(personId).get());
        roomRepository.save(room.get());
    }

    public void exitRoom(int roomId, int personId) {
        Optional<Room> room = roomRepository.findById(roomId);
        room.get().deletePerson(personRepository.findById(personId).get());
        roomRepository.save(room.get());
    }

    public void deleteMessage(int roomId, int messageId) {
        Optional<Room> room = roomRepository.findById(roomId);
        room.get().deleteMessage(messageRepository.findAllById(messageId));
        roomRepository.save(room.get());
        messageRepository.deleteById(messageId);
    }
}
