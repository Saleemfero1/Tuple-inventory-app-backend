package com.nextuple.Inventory.management.controller;

import com.nextuple.Inventory.management.dto.AuthResponseDTO;
import com.nextuple.Inventory.management.dto.LogInDTO;
import com.nextuple.Inventory.management.model.Role;
import com.nextuple.Inventory.management.model.UserEntity;
import com.nextuple.Inventory.management.repository.OrganizationRepository;
import com.nextuple.Inventory.management.repository.RoleRepository;
import com.nextuple.Inventory.management.repository.UserRepository;
import com.nextuple.Inventory.management.security.JwtGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@CrossOrigin("*")
@RequestMapping("/api/auth")
@RestController
@Slf4j
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private OrganizationRepository organizationRepository;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<String>register(@RequestBody UserEntity userEntity){
        log.info("Registration request received for user: {}", userEntity.getUsername());
        if(userRepository.existsByUsername(userEntity.getUsername())){
            log.warn("Username '{}' is already taken", userEntity.getUsername());
            return new ResponseEntity<>("username is taken!", HttpStatus.BAD_REQUEST);
        } else if (userRepository.existsByUserEmail(userEntity.getUserEmail())) {
            log.warn("Email '{}' already exists", userEntity.getUserEmail());
            return new ResponseEntity<>("Email already exist!", HttpStatus.BAD_REQUEST);
        }

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        Role roles = roleRepository.findByName("USER");
        userEntity.setRoles(Collections.singletonList(roles));
        userEntity.setOrganizationId("ORG001");
        userRepository.save(userEntity);
        log.info("User '{}' registered successfully", userEntity.getUsername());
        return new ResponseEntity<>("registered successfully.!",HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO>login(@RequestBody LogInDTO logInDto){
        log.info("Login request received for username: {}", logInDto.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logInDto.getUsername(),logInDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        log.info("User '{}' logged in successfully", logInDto.getUsername());

        return new ResponseEntity<>(new AuthResponseDTO(token),HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<UserEntity>getUserEntity(@PathVariable("username") String username){
        log.info("Request received to retrieve user entity for username: {}", username);
        Optional<UserEntity> user =  userRepository.findByUsername(username);
        return user.map(userEntity -> new ResponseEntity<>(userEntity, HttpStatus.OK)).orElse(null);
    }

}
