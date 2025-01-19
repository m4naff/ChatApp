package com.example.chatapp.auth;

import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Class that contains Web Security Expressions used by Spring Security
 * (e.g. in annotations like {@link org.springframework.security.access.prepost.PreAuthorize} and {@link org.springframework.security.access.prepost.PostAuthorize}).
 */
@Component
@RequiredArgsConstructor
public class WebSecurity {

    private final UserRepository userRepository;

    public boolean hasChatAuthority(Authentication authentication, String chatId) {
        return authentication.getAuthorities().contains(new ChatAuthority(chatId));
    }

    public boolean addChatAuthority(Authentication authentication, Chat chat) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.user();

        user.addAuthority(new ChatAuthority(chat.getId()));
        userRepository.save(user).subscribe();

        return true;
    }

    public boolean removeChatAuthority(Authentication authentication, String chatId) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.user();

        if (user.removeAuthority(new ChatAuthority(chatId))) {
            userRepository.save(user).subscribe();
        }

        return true;
    }



}
