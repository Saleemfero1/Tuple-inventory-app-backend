package com.nextuple.Inventory.management.controller.test;

import com.nextuple.Inventory.management.controller.AuthenticationController;
import com.nextuple.Inventory.management.dto.AuthResponseDTO;
import com.nextuple.Inventory.management.dto.LogInDTO;
import com.nextuple.Inventory.management.model.Role;
import com.nextuple.Inventory.management.model.UserEntity;
import com.nextuple.Inventory.management.repository.RoleRepository;
import com.nextuple.Inventory.management.repository.UserRepository;
import com.nextuple.Inventory.management.security.JwtGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtGenerator jwtGenerator;

    @InjectMocks
    private AuthenticationController authenticationController;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testRegister() {
        // Mock data
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setPassword("password");

        // Mock the repository methods
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(new Role("001","user"));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Call the controller method
        ResponseEntity<String> response = authenticationController.register(userEntity);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("registered successfully.!", response.getBody());
        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(roleRepository, times(1)).findByName("USER");
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }



    @Test
    public void testLogin() {
        // Mock data
        LogInDTO logInDto = new LogInDTO();
        logInDto.setUsername("testuser");
        logInDto.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = "testToken";

        // Mock the authentication manager and token generation
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtGenerator.generateToken(authentication)).thenReturn(token);

        // Call the controller method
        ResponseEntity<AuthResponseDTO> response = authenticationController.login(logInDto);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody().getAccessToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtGenerator, times(1)).generateToken(authentication);
    }

    @Test
    public void testGetUserEntity() {
        // Mock data
        String username = "testuser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);

        // Mock the repository method
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // Call the controller method
        ResponseEntity<UserEntity> response = authenticationController.getUserEntity(username);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userEntity, response.getBody());
        verify(userRepository, times(1)).findByUsername(username);
    }

}
