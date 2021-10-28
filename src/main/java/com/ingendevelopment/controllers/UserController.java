package com.ingendevelopment.controllers;

import com.ingendevelopment.logging.SplunkLogger;
import com.ingendevelopment.model.persistence.Cart;
import com.ingendevelopment.model.persistence.User;
import com.ingendevelopment.model.persistence.repositories.CartRepository;
import com.ingendevelopment.model.persistence.repositories.UserRepository;
import com.ingendevelopment.model.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private SplunkLogger logger;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			logger.logMessage("Username '" + username + "' not found.",
					this.getClass().getName(),
					SplunkLogger.Severity.ERROR);

			return ResponseEntity.notFound().build();
		} else {
			logger.logMessage("Username '" + user.getUsername() + "' with ID '" + user.getId() + "' found.",
					this.getClass().getName(),
					SplunkLogger.Severity.INFO);

			return ResponseEntity.ok(user);
		}
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());

		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);

		if (createUserRequest.getPassword().length() < 8) {
			logger.logMessage("Password for user ID '" + user.getId() + "' does not meet minimum length requirement.",
					this.getClass().getName(),
					SplunkLogger.Severity.ERROR);

			return ResponseEntity.badRequest().build();
		} else if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			logger.logMessage("Password input and confirm input do not match.",
					this.getClass().getName(),
					SplunkLogger.Severity.ERROR);

			return ResponseEntity.badRequest().build();
		} else {
			if (userRepository.findByUsername(user.getUsername()) != null) {
				logger.logMessage("Username '" + user.getUsername() + "' already exists. Please choose another.",
						this.getClass().getName(),
						SplunkLogger.Severity.ERROR);

				return ResponseEntity.status(409).build();
			}

			user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
			userRepository.save(user);

			logger.logMessage("User ID '" + user.getId() + "' created successfully.",
					this.getClass().getName(),
					SplunkLogger.Severity.INFO);
		}

		return ResponseEntity.ok(user);
	}
}
