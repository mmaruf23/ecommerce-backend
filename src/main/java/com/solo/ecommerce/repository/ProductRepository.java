package com.solo.ecommerce.repository;

import com.solo.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStockGreaterThan(int stock);
    List<Product> findByStockLessThanEqual(int stock);
    Product findByName(String name);
}
