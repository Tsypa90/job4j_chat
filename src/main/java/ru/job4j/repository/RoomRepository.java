package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.domain.Room;

import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    Room findById(int id);

    List<Room> findAll();
}
