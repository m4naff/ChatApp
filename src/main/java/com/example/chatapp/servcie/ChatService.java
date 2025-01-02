package com.example.chatapp.servcie;

import com.example.chatapp.model.Chat;
import com.example.chatapp.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Service layer for {@link Chat}.
 */
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ReactiveMongoOperations mongoOperations;

    public Flux<Chat> findAllChatsForCurrentUser() {
        return SecurityUtils
    }



}
