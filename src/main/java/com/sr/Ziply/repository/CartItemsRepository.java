package com.sr.Ziply.repository;

import com.sr.Ziply.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItems,Long> {
}
