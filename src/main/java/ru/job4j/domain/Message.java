package ru.job4j.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.job4j.validation.OperationOnValidation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@Entity
@Table(name = "message")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Positive(message = "Id is mandatory", groups = {OperationOnValidation.OnUpdate.class, OperationOnValidation.OnPatch.class})
    private int id;
    @NotBlank(message = "Body is mandatory", groups = {
            OperationOnValidation.OnUpdate.class, OperationOnValidation.OnCreate.class})
    private String body;
    @ManyToOne
    private Person person;
}
