package com.nextuple.Inventory.management.security.test;

import com.nextuple.Inventory.management.model.Role;
import com.nextuple.Inventory.management.model.UserEntity;
import com.nextuple.Inventory.management.repository.UserRepository;
import com.nextuple.Inventory.management.security.CustomUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Before
    public void setUp() {
        // Mock the userRepository.findByUsername() method
        when(userRepository.findByUsername("name")).thenReturn(java.util.Optional.of(new UserEntity("001", "name","email@gmail.com","pwd@123","ORG001")));
    }

    @Test
    public void testLoadUserByUsername_existingUser() {
        // Test case for an existing user
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("name");
        assertEquals("name", userDetails.getUsername());
        assertEquals("pwd@123", userDetails.getPassword());

    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_nonExistingUser() {
        // Test case for a non-existing user
        customUserDetailsService.loadUserByUsername("nonExistingUser");
    }
}
