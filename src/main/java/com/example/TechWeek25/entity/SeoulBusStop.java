package com.example.TechWeek25.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.locationtech.jts.geom.Geometry;

@Entity
public class SeoulBusStop {
    @Id
    private String busStopId;
    private String dongCode;
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Geometry location;
}
