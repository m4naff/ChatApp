package com.example.chatapp.auth.jwt;

import com.example.chatapp.utils.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Adds an Authorization HTTP header with a token to the response.
 *
 */
public class JwtAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();

        String token = JwtUtils.createToken(authentication);
        exchange.getResponse().getHeaders().setBearerAuth(token);

        return webFilterExchange.getChain().filter(exchange);
    }
}
