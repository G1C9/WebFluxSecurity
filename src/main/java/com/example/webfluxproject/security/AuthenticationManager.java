package com.example.webfluxproject.security;

import com.example.webfluxproject.dto.security.CustomPrincipal;
import com.example.webfluxproject.entity.UserEntity;
import com.example.webfluxproject.exception.UnauthorizedException;
import com.example.webfluxproject.restservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserService userService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getById(principal.getId())
                .filter(UserEntity::getEnabled)
                .switchIfEmpty(Mono.error(new UnauthorizedException("User disabled")))
                .map(user -> authentication);
    }
}
