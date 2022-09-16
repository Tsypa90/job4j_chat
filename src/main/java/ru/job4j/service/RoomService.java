package ru.job4j.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;
import ru.job4j.repository.PersonRepository;
import ru.job4j.repository.RoomRepository;

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

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Optional<Room> findRoomById(int roomId) {
        return roomRepository.findById(roomId);
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

    private Room findRoom(int id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No room with id:" + id));
    }

    private Person findPerson(int id) {
        return personRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("No person with id:" + id));
    }
}
