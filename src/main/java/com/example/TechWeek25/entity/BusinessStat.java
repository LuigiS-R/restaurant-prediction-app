package com.example.TechWeek25.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "business_stats")
public class BusinessStat {
    @EmbeddedId
    private BusinessStatId id;
    private Integer count;
}
