package com.solo.ecommerce.repository;

import com.solo.ecommerce.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Repository> {

}
