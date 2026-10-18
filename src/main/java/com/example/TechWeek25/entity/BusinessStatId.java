package com.example.TechWeek25.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class BusinessStatId {
    private String dongCode;
    private String categoryId;
}
