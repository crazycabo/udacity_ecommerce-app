package com.ingendevelopment.controllers;

import com.ingendevelopment.logging.SplunkLogger;
import com.ingendevelopment.model.persistence.Cart;
import com.ingendevelopment.model.persistence.Item;
import com.ingendevelopment.model.persistence.User;
import com.ingendevelopment.model.persistence.UserOrder;
import com.ingendevelopment.model.persistence.repositories.OrderRepository;
import com.ingendevelopment.model.persistence.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Brian Smith on 10/26/21.
 * Description:
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderController orderController;

    @Mock
    private SplunkLogger logger;

    Item itemA = Item.builder()
            .id(1L)
            .name("漫画")
            .description("Manga")
            .price(BigDecimal.valueOf(50))
            .build();

    Cart cart = Cart.builder()
            .id(1L)
            .user(new User())
            .total(BigDecimal.valueOf(50))
            .items(Collections.singletonList(itemA))
            .build();

    User user = User.builder()
            .id(1L)
            .username("Akira")
            .password("a1b2c3d4")
            .cart(cart)
            .build();

    UserOrder order = UserOrder.builder()
            .id(null)
            .user(user)
            .total(BigDecimal.valueOf(50))
            .items(Collections.singletonList(itemA))
            .build();

    @Test
    public void submitOrder() {
        cart.setUser(user);

        when(userRepository.findByUsername("Akira")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("Akira");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(itemA, Objects.requireNonNull(response.getBody()).getItems().get(0));
    }

    @Test
    public void getOrderForUser() {
        when(userRepository.findByUsername("Akira")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(order));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Akira");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(itemA, Objects.requireNonNull(response.getBody()).get(0).getItems().get(0));
    }
}
