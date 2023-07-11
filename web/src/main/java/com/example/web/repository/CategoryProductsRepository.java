package com.example.web.repository;

import com.example.web.models.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryProductsRepository extends JpaRepository<ProductCategoryEntity, Integer> {
}
