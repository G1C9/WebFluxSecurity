package com.example.webfluxproject.restservice;

import com.example.webfluxproject.entity.UserEntity;
import com.example.webfluxproject.entity.UserRole;
import com.example.webfluxproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Mono<UserEntity> getById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<UserEntity> getByName(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<UserEntity> registerUser(UserEntity user) {
        return userRepository.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.USER)
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u ->
            log.info("In registerUser - user: {} created", u)
        );
    }

}
