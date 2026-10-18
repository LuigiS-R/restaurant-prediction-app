package com.example.TechWeek25.repository;

import com.example.TechWeek25.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.locationtech.jts.geom.Point;

import java.util.List;

@Repository
public interface BusinessRepo extends JpaRepository<Business, String> {
    @Query(value = "SELECT DISTINCT category FROM business WHERE category IS NOT NULL", nativeQuery = true)
    List<String> findDistinctCategories();

    @Query(value = "SELECT count(*) FROM business WHERE ST_DWithin(business.location::geography, :location ::geography, :distance) AND business.dong_code = :dong", nativeQuery = true)
    Integer countByLocationWithin(@Param("location") Point location, @Param("distance") int distance, @Param("dong") String dongCode);

    @Query(value = "SELECT count(*) FROM business WHERE ST_DWithin(business.location::geography, :location ::geography, :distance) AND business.dong_code = :dong AND business.category = :category", nativeQuery = true)
    Integer countByLocationAndCategoryWithin(@Param("location") Point location, @Param("distance") int distance, @Param("dong") String dongCode, @Param("category") String category);
}
