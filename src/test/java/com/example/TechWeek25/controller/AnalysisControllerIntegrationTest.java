package com.example.TechWeek25.controller;

import com.example.TechWeek25.dto.Coordinates;
import com.example.TechWeek25.dto.ErrorResponse;
import com.example.TechWeek25.dto.PredictionData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
class AnalysisControllerIntegrationTest {
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
          DockerImageName.parse("my_postgis_snapshot")
                  .asCompatibleSubstituteFor("postgres"))
          .withInitScript("TestDataSeedingScript.sql");

  @Autowired
  AnalysisController analysisController;
  @Autowired
  JdbcTemplate jdbcTemplate;

  @BeforeEach
  public void verifyData(){
    Integer count = jdbcTemplate.queryForObject(
            "SELECT count(*) FROM neighborhoods", Integer.class);

    Assertions.assertThat(count).isGreaterThan(0);
  }

  @Test
  public void analysisController_getNeighborhoodData_returnsNeighborhoodData(){
    Object neighborhoodData = analysisController.getNeighborhoodData(new Coordinates("35.1573174", "129.0594829")).getBody();
    Assertions.assertThat(neighborhoodData).isInstanceOf(PredictionData.class);
    Assertions.assertThat(((PredictionData)neighborhoodData).getDongName()).isEqualTo("부전2동");
    Assertions.assertThat(((PredictionData)neighborhoodData).getGeneralCategoryCounts()).isNotNull();
  }

  @ParameterizedTest
  @CsvSource({
          "aaa, aaa",
          "35.1573, aaa",
          ",",
          "45. 1 , ",
          "12.01, 12.09"
  })
  public void analysisController_getNeighborhoodData_returnsErrorResponse(String longitude, String latitude){
    Object neighborhoodData = analysisController.getNeighborhoodData(new Coordinates(longitude, latitude)).getBody();
    Assertions.assertThat(neighborhoodData).isInstanceOf(ErrorResponse.class);
  }
}