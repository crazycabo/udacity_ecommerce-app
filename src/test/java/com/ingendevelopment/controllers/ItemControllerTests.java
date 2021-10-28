package com.ingendevelopment.controllers;

import com.ingendevelopment.logging.SplunkLogger;
import com.ingendevelopment.model.persistence.Item;
import com.ingendevelopment.model.persistence.repositories.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
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
public class ItemControllerTests {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    @Mock
    private SplunkLogger logger;

    Item itemA = Item.builder()
            .id(1L)
            .name("ピザの日本語")
            .description("Japanese pizza")
            .price(BigDecimal.valueOf(100))
            .build();

    Item itemB = Item.builder()
            .id(2L)
            .name("院筆")
            .description("Pencil")
            .price(BigDecimal.valueOf(5000))
            .build();

    @Test
    public void getItems() {
        List<Item> items = Arrays.asList(itemA, itemB);

        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ピザの日本語", Objects.requireNonNull(Objects.requireNonNull(response.getBody()).get(0)).getName());
        assertEquals("院筆", Objects.requireNonNull(Objects.requireNonNull(response.getBody()).get(1)).getName());
    }

    @Test
    public void getItemById() {
        when(itemRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(itemB));

        ResponseEntity<Item> response = itemController.getItemById(2L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(5000), Objects.requireNonNull(Objects.requireNonNull(response.getBody())).getPrice());
    }

    @Test
    public void getItemsByName() {
        List<Item> items = Collections.singletonList(itemB);

        when(itemRepository.findByName("院筆")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("院筆");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("院筆", Objects.requireNonNull(Objects.requireNonNull(response.getBody()).get(0)).getName());
    }
}
