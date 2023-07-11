package com.example.web.controllers;

import com.example.web.repository.CategoryProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CategoryProductsController {

    private final CategoryProductsRepository categoryRepository;

    @Autowired
    public CategoryProductsController(CategoryProductsRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
