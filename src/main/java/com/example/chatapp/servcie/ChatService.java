package com.example.chatapp.servcie;

import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.ChatRepository;
import com.example.chatapp.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Service layer for {@link Chat}.
 */
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ReactiveMongoOperations mongoOperations;

    public Flux<Chat> findAllChatsForCurrentUser() {
        return SecurityUtils.getPrincipal()
                .flatMapMany(principal -> chatRepository.findAllByMembers(principal.getId()));
    }

    @PostAuthorize("@webSecurity.addChatAuthority(authentication, #chat)")
    public Mono<Chat> saveChat(Chat chat) {
        Set<User> members = Objects.requireNonNullElseGet(chat.getMembers(), HashSet::new);

        return SecurityUtils.getPrincipal()
                .doOnNext(principal -> members.add(principal.user()))
                .doOnNext(principal -> chat.setMembers(members))
                .flatMap(principal -> chatRepository.save(chat));
    }



}
