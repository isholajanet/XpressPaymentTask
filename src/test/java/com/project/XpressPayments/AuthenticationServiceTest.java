package com.project.XpressPayments;


import com.project.XpressPayments.auth.AuthenticationResponse;
import com.project.XpressPayments.auth.AuthenticationService;
import com.project.XpressPayments.auth.RegisterRequest;
import com.project.XpressPayments.config.JwtService;
import com.project.XpressPayments.model.Role;
import com.project.XpressPayments.model.User;
import com.project.XpressPayments.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@Slf4j
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp(){
        authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtService, authenticationManager);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUserSuccessfully(){

        RegisterRequest request = RegisterRequest.builder()
                .email("ronke@gmail.com")
                .username("ronke")
                .password("ronke12")
                .build();

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword())
                .role(Role.USER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("216638995");

        AuthenticationResponse response = authenticationService.createUser(request);
        log.info("Response: " + response);
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));

    }
}
