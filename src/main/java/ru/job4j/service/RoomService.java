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

    public Room findRoomById(int roomId) {
        return roomRepository.findById(roomId);
    }

    public void postMessage(Message message, int roomId, int personId) {
        Room room = roomRepository.findById(roomId);
        message.setPerson(personRepository.findById(personId).get());
        room.addMessage(message);
        roomRepository.save(room);
    }

    public void enterRoom(int roomId, int personId) {
        Room room = roomRepository.findById(roomId);
        room.addPerson(personRepository.findById(personId).get());
        roomRepository.save(room);
    }

    public void exitRoom(int roomId, int personId) {
        Room room = roomRepository.findById(roomId);
        room.deletePerson(personRepository.findById(personId).get());
        roomRepository.save(room);
    }

    public void deleteMessage(int roomId, int messageId) {
        Room room = roomRepository.findById(roomId);
        room.deleteMessage(messageRepository.findAllById(messageId));
        roomRepository.save(room);
        messageRepository.deleteById(messageId);
    }
}
