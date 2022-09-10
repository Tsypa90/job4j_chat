package ru.job4j.exception;

public class PersonNameNotUniqueException extends Exception {
    public PersonNameNotUniqueException(String message) {
        super(message);
    }
}
