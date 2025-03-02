package com.solo.ecommerce.repository;

import com.solo.ecommerce.model.Cart;
import com.solo.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    void deleteByUser(User user);

    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product WHERE c.user = :user")
    Optional<Cart> findByUserWithProducts(@Param("user") User user);


}
