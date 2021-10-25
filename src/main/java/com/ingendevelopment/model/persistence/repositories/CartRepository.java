package com.ingendevelopment.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ingendevelopment.model.persistence.Cart;
import com.ingendevelopment.model.persistence.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
