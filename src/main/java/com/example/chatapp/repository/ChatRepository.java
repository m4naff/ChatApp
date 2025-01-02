package com.example.chatapp.repository;

import com.example.chatapp.model.Chat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Repository for {@link Chat}.
 *
 */
@Repository
public interface ChatRepository extends ReactiveCrudRepository<Chat, String> {

    Flux<Chat> findAllByMembers(String userId);

}
