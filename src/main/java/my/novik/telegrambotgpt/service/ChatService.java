package my.novik.telegrambotgpt.service;

import lombok.RequiredArgsConstructor;
import my.novik.telegrambotgpt.model.Chat;
import my.novik.telegrambotgpt.repository.ChatRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public Chat addChat(Chat chat) {
        return chatRepository.save(chat);
    }

}
