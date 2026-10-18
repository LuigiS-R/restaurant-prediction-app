package com.example.TechWeek25.repository;

import com.example.TechWeek25.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, String>{
    public Optional<Category> findByCategoryId(String categoryId);
}
