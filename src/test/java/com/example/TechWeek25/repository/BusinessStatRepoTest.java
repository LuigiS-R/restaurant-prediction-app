package com.example.TechWeek25.repository;


import com.example.TechWeek25.dto.CategoryCount;
import com.example.TechWeek25.entity.BusinessStat;
import com.example.TechWeek25.entity.BusinessStatId;
import com.example.TechWeek25.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BusinessStatRepoTest {
    @Autowired
    BusinessStatRepo businessStatRepo;
    @Autowired
    CategoryRepo categoryRepo;

    final Category categoryRow = new Category();
    final BusinessStat businessStatRow = new BusinessStat();
    final BusinessStatId businessStatId = new BusinessStatId();

    @BeforeEach
    public void Data_Initialization(){
        categoryRow.setCategoryId("cp2_bnu_56");
        categoryRow.setCategoryName("음식점 및 주점업");
        categoryRow.setCategoryType("specific");

        businessStatId.setCategoryId("cp2_bnu_56");
        businessStatId.setDongCode("11010530");

        businessStatRow.setCount(887);
        businessStatRow.setId(businessStatId);

        //Arrange
        businessStatRepo.save(businessStatRow);
        categoryRepo.save(categoryRow);
    }

    @Test
    public void BusinessStatRepo_FindBusinessCount_ReturnsCategoryCountMapping() {

        //Act
        List<CategoryCount> categoryCounts = businessStatRepo.findBusinessCounts("specific", "11010530");

        //Assert
        org.assertj.core.api.Assertions.assertThat(categoryCounts).isNotEmpty();
        org.assertj.core.api.Assertions.assertThat(categoryCounts.size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(categoryCounts.getFirst().getCount()).isEqualTo(887);
        org.assertj.core.api.Assertions.assertThat(categoryCounts.getFirst().getCategoryName()).isEqualTo("음식점 및 주점업");
    }

    @Test
    public void BusinessStatRepo_FindBusinessCount_ReturnsEmptyCategoryCountMapping(){
        //Act
        List<CategoryCount> categoryCounts = businessStatRepo.findBusinessCounts("specific", "invalid Dong Code");
        org.assertj.core.api.Assertions.assertThat(categoryCounts).isEmpty();

        categoryCounts = businessStatRepo.findBusinessCounts("invalid category name", "11010530");
        org.assertj.core.api.Assertions.assertThat(categoryCounts).isEmpty();

    }

}