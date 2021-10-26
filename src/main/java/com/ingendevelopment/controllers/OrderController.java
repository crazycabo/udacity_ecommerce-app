package com.ingendevelopment.controllers;

import com.ingendevelopment.model.persistence.User;
import com.ingendevelopment.model.persistence.UserOrder;
import com.ingendevelopment.model.persistence.repositories.OrderRepository;
import com.ingendevelopment.model.persistence.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);

		if(user == null) {
			logger.error("User with username '" + username + "' not found.");
			return ResponseEntity.notFound().build();
		}

		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);

		logger.info("Order ID '" + order.getId() + "' submitted successfully");

		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);

		if(user == null) {
			logger.error("User with username '" + username + "' not found.");
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
