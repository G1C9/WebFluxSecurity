package com.example.webfluxproject.controller;

import com.example.webfluxproject.dto.rest.AuthRq;
import com.example.webfluxproject.dto.rest.AuthRs;
import com.example.webfluxproject.dto.rest.UserDto;
import com.example.webfluxproject.entity.UserEntity;
import com.example.webfluxproject.mapper.UserMapper;
import com.example.webfluxproject.dto.security.CustomPrincipal;
import com.example.webfluxproject.security.SecurityService;
import com.example.webfluxproject.restservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthControllerV1 {

    private final SecurityService securityService;

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto userDto) {
        UserEntity user = userMapper.map(userDto);
        return userService.registerUser(user)
                .map(userMapper::map);
    }

    @PostMapping("/login")
    public Mono<AuthRs> login(@RequestBody AuthRq authRq) {
        return securityService.authenticate(authRq.getUsername(), authRq.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthRs.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getById(customPrincipal.getId())
                .map(userMapper::map);
    }

}
