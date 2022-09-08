package ru.job4j.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.repository.MessageRepository;
import ru.job4j.repository.PersonRepository;
import ru.job4j.repository.RoomRepository;

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
