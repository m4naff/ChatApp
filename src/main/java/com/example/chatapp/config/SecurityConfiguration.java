package com.example.chatapp.config;

import com.example.chatapp.auth.UserPrincipal;
import com.example.chatapp.auth.jwt.JwtAuthenticationConverter;
import com.example.chatapp.auth.jwt.JwtAuthenticationSuccessHandler;
import com.example.chatapp.auth.jwt.JwtAuthorizationManager;
import com.example.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;

import java.util.List;

/**
 * Configuration class for Spring Security.
 */
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserRepository userRepository;

    @Value("${chat.https.enabled:false}")
    private boolean httpsEnabled;

    @Value("${chat.cors.allowedOrigin:http://localhost:8080}")
    private String allowedOrigin;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .cors().and()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/signup").permitAll()
                        .anyExchange().authenticated())
                .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(jwtAuthorizationFilter(), SecurityWebFiltersOrder.AUTHORIZATION);

        if (httpsEnabled) {
            http
                    .redirectToHttps().and()
                    .headers(header -> header
                            .hsts(hsts -> hsts.includeSubdomains(false)));
        }

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(allowedOrigin);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.addExposedHeader(HttpHeaders.AUTHORIZATION);
        configuration.applyPermitDefaultValues();

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return username -> userRepository
                .findByUsername(username)
                .map(UserPrincipal::new);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private WebFilter jwtAuthenticationFilter() {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());
        authenticationManager.setPasswordEncoder(passwordEncoder());

        var jwtAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);
        jwtAuthenticationFilter.setServerAuthenticationConverter(new JwtAuthenticationConverter());
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
        jwtAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/auth/signin"));

        return jwtAuthenticationFilter;
    }

    private WebFilter jwtAuthorizationFilter() {
        var jwtAuthorizationFilter = new AuthenticationWebFilter(new JwtAuthorizationManager(userDetailsService()));
        jwtAuthorizationFilter.setServerAuthenticationConverter(new JwtAuthenticationConverter());
        jwtAuthorizationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.anyExchange());

        return jwtAuthorizationFilter;
    }

}
