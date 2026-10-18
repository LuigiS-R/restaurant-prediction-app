package com.example.TechWeek25.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "categories")
public class Category {
    @Id
    private String categoryId;
    private String categoryName;
    private String categoryType;
}
