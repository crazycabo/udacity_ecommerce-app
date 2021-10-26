package com.ingendevelopment.controllers;

import com.ingendevelopment.model.persistence.Cart;
import com.ingendevelopment.model.persistence.Item;
import com.ingendevelopment.model.persistence.User;
import com.ingendevelopment.model.persistence.repositories.CartRepository;
import com.ingendevelopment.model.persistence.repositories.ItemRepository;
import com.ingendevelopment.model.persistence.repositories.UserRepository;
import com.ingendevelopment.model.requests.ModifyCartRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
@Slf4j
public class CartController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;

	final Logger logger = LoggerFactory.getLogger(CartController.class);
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
		User user = userRepository.findByUsername(request.getUsername());

		if(user == null) {
			logger.info("User with username '" + request.getUsername() + "' not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Optional<Item> item = itemRepository.findById(request.getItemId());

		if(!item.isPresent()) {
			logger.info("Item with ID '" + request.getItemId() + "' not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get()));

		cartRepository.save(cart);

		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		User user = userRepository.findByUsername(request.getUsername());

		if(user == null) {
			logger.error("User with username '" + request.getUsername() + "' not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Optional<Item> item = itemRepository.findById(request.getItemId());

		if(!item.isPresent()) {
			logger.error("Item with ID '" + request.getItemId() + "' not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));

		cartRepository.save(cart);

		return ResponseEntity.ok(cart);
	}
}
