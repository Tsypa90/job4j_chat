package ru.job4j.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.job4j.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "message")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Positive(message = "Id is mandatory", groups = {Operation.OnUpdate.class, Operation.OnPatch.class})
    private int id;
    @NotBlank(message = "Body is mandatory", groups = {
            Operation.OnUpdate.class, Operation.OnCreate.class})
    private String body;
    @ManyToOne
    private Person person;
}
