package ru.job4j.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Message;
import ru.job4j.repository.MessageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    @NonNull
    private final MessageRepository messageRepository;

    public List<Message> findAllByPersonId(int id) {
        return messageRepository.findAllByPersonId(id);
    }

    public void updateMessage(Message message) {
        Message messageDb = messageRepository.findById(message.getId()).get();
        messageDb.setBody(message.getBody());
        messageRepository.save(messageDb);
    }

    public void deleteMessage(int id) {
        messageRepository.deleteById(id);
    }
}
