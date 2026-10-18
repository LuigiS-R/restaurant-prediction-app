package com.example.TechWeek25.repository;

import com.example.TechWeek25.entity.SeoulSubwayStation;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeoulSubwayStationRepo extends JpaRepository<SeoulSubwayStation, String>{
    @Query(value = "SELECT count(*) FROM seoul_subway_station WHERE ST_DWithin(seoul_subway_station.location::geography, :location ::geography, :distance) AND seoul_subway_station.dong_code = :dong", nativeQuery = true)
    Integer countByLocationWithin(@Param("location") Point location, @Param("distance") int distance, @Param("dong") String dongCode);

    @Query(value = """
    SELECT ST_DistanceSphere(sss.location, :location) 
    FROM seoul_subway_station sss 
    ORDER BY sss.location <-> :location 
    LIMIT 1
    """, nativeQuery = true)
    Double getClosestDistance(@Param("location") Point location);
}
