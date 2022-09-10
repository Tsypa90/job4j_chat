package ru.job4j.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "room")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Person> persons;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "room_messages", joinColumns = @JoinColumn(name = "room_id"), inverseJoinColumns = @JoinColumn(name = "messages_id"))
    private List<Message> messages;

    public void addPerson(Person person) {
        persons.add(person);
    }

    public void deletePerson(Person person) {
        persons.remove(person);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void deleteMessage(Message message) {
        messages.remove(message);
    }
}
