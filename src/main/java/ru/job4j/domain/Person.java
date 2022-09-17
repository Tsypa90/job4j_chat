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
@Table(name = "person")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Positive(message = "Id is mandatory", groups = {
            Operation.OnUpdate.class, Operation.OnPatch.class})
    private int id;
    @NotBlank(message = "Name is mandatory", groups = {
            Operation.OnUpdate.class, Operation.OnCreate.class})
    private String name;
    @NotBlank(message = "Password is mandatory", groups = {
            Operation.OnUpdate.class, Operation.OnCreate.class})
    @Size(min = 3, message = "Invalid password. Password length must be more then 3 chars",
            groups = {Operation.OnUpdate.class, Operation.OnCreate.class})
    private String password;
    @OneToOne
    private Role role;
}
