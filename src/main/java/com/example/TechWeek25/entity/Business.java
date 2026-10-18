package com.example.TechWeek25.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.locationtech.jts.geom.Geometry;

@Entity
public class Business {
    @Id
    private String administrativeId;
    private String dongCode;
    private String category;
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Geometry location;
}
