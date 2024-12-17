package com.example.chatapp.repository;

import com.example.chatapp.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Spring Data Repository for {@link User}.
 *
 */
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {

    Mono<User> findByUsername(String username);

}
