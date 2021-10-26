package com.ingendevelopment.controllers;

import com.ingendevelopment.model.persistence.Cart;
import com.ingendevelopment.model.persistence.User;
import com.ingendevelopment.model.persistence.repositories.CartRepository;
import com.ingendevelopment.model.persistence.repositories.UserRepository;
import com.ingendevelopment.model.requests.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Brian Smith on 10/25/21.
 * Description:
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController userController;

    User user = User.builder()
            .id(1L)
            .username("alpha")
            .password("a1b2c3d4")
            .cart(new Cart())
            .build();

    @Test
    public void createValidUser() {
        when(bCryptPasswordEncoder.encode("password")).thenReturn("a1b2c3d4");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("alpha");
        request.setPassword("password");
        request.setConfirmPassword("password");

        ResponseEntity<User> response = userController.createUser(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("alpha", Objects.requireNonNull(response.getBody()).getUsername());
        assertEquals("a1b2c3d4", Objects.requireNonNull(response.getBody()).getPassword());
    }

    @Test
    public void findUserById() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));

        ResponseEntity<User> response = userController.findById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("alpha", Objects.requireNonNull(response.getBody()).getUsername());
        assertEquals("a1b2c3d4", Objects.requireNonNull(response.getBody()).getPassword());
    }

    @Test
    public void findUserByUsername() {
        when(userRepository.findByUsername("alpha")).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("alpha");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("alpha", Objects.requireNonNull(response.getBody()).getUsername());
        assertEquals("a1b2c3d4", Objects.requireNonNull(response.getBody()).getPassword());
    }
}
