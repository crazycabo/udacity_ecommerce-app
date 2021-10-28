package com.ingendevelopment.controllers;

import com.ingendevelopment.logging.SplunkLogger;
import com.ingendevelopment.model.persistence.Cart;
import com.ingendevelopment.model.persistence.Item;
import com.ingendevelopment.model.persistence.User;
import com.ingendevelopment.model.persistence.repositories.CartRepository;
import com.ingendevelopment.model.persistence.repositories.ItemRepository;
import com.ingendevelopment.model.persistence.repositories.UserRepository;
import com.ingendevelopment.model.requests.ModifyCartRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
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
public class CartControllerTests {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartController cartController;

    @Mock
    private SplunkLogger logger;

    User user = User.builder()
            .id(1L)
            .username("Akira")
            .password("a1b2c3d4")
            .cart(new Cart())
            .build();

    Item itemA = Item.builder()
            .id(1L)
            .name("漫画")
            .description("Manga")
            .price(BigDecimal.valueOf(50))
            .build();

    Cart cart = Cart.builder()
            .id(1L)
            .user(new User())
            .total(BigDecimal.valueOf(1500))
            .items(Collections.singletonList(new Item()))
            .build();

    @Test
    public void addItemsToCart() {
        when(userRepository.findByUsername("Akira")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(itemA));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(3);
        request.setUsername("Akira");

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("漫画", Objects.requireNonNull(response.getBody()).getItems().get(2).getName());
    }

    @Test
    public void removeItemFromCart() {
        List<Item> items = new ArrayList<>();
        items.add(itemA);
        items.add(itemA);

        cart.setItems(items);
        user.setCart(cart);

        when(userRepository.findByUsername("Akira")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(itemA));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("Akira");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getItems().size());
    }
}
