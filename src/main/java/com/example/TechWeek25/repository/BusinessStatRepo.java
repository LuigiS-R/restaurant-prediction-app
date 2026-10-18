package com.example.TechWeek25.repository;

import com.example.TechWeek25.dto.CategoryCount;
import com.example.TechWeek25.entity.BusinessStat;
import com.example.TechWeek25.entity.BusinessStatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessStatRepo extends JpaRepository<BusinessStat, BusinessStatId>{
    @Query("SELECT bs.count as count, c.categoryName as categoryName FROM BusinessStat as bs JOIN Category" +
            " as c ON bs.id.categoryId = c.categoryId WHERE bs.id.dongCode = :dongCode and c.categoryType = :categoryType")
    List<CategoryCount> findBusinessCounts(@Param("categoryType") String categoryType, @Param(("dongCode")) String dongCode);
}
