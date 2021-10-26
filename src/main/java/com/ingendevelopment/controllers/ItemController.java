package com.ingendevelopment.controllers;

import com.ingendevelopment.model.persistence.Item;
import com.ingendevelopment.model.persistence.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@Slf4j
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

	final Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);

		if (items == null || items.isEmpty()) {
			logger.error("Failed to find items with the name '" + name + "'.");
			return ResponseEntity.notFound().build();
		} else {
			logger.info("Found at least one items with the name '" + name + "'.");
			return ResponseEntity.ok(items);
		}
	}
}
