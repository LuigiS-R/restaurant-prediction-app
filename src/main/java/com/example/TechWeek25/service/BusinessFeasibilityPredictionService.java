package com.example.TechWeek25.service;

import jakarta.annotation.PostConstruct;
import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoost;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
public class BusinessFeasibilityPredictionService {

    private Booster booster;

    @PostConstruct
    public void loadModel() throws Exception {
        // Ensure the json file is in src/main/resources
        String modelPath = getClass().getResource("/bizvision_model.json").getPath();
        this.booster = XGBoost.loadModel(modelPath);
    }

    public float predictSuccess(float restaurantArea, int businessType, int restaurants500m,
                                int competition500m, int transportHubs500m, double closestHubDist,
                                double pricePerSqm, double marketShare) throws Exception {

        // Order MUST match your file:
        // [area, type, rest_500, comp_500, trans_500, close_trans, price, market]
        float[] features = new float[] {
                restaurantArea,
                (float) businessType, // Cast categorical/ints to float
                (float) restaurants500m,
                (float) competition500m,
                (float) transportHubs500m,
                (float)closestHubDist,
                (float)pricePerSqm,
                (float)marketShare
        };

        // Create a 1-row, 8-column matrix
        DMatrix inputMatrix = new DMatrix(features, 1, 8, Float.NaN);

        // Output is a 2D array (rows x classes)
        float[][] prediction = booster.predict(inputMatrix);

        // Returning the probability of Class 1 (Success)
        return prediction[0][0];
    }
}
