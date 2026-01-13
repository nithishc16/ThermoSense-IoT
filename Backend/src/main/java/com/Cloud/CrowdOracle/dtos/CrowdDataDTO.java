package com.Cloud.CrowdOracle.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

public class CrowdDataDTO {

    @NotNull(message = "Temperature in Celsius cannot be null")
    @DecimalMin(value = "-273.15", message = "Temperature cannot be below absolute zero")
    private Double temperatureCelsius;

    @NotNull(message = "Total people count cannot be null")
    @Min(value = 0, message = "Total people count cannot be negative")
    private Integer totalPeopleCount;

    // Default constructor
    public CrowdDataDTO() {}

    // Constructor with parameters
    public CrowdDataDTO(Double temperatureCelsius, Integer totalPeopleCount) {
        this.temperatureCelsius = temperatureCelsius;
        this.totalPeopleCount = totalPeopleCount;
    }

    // Getters and Setters
    public Double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(Double temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    public Integer getTotalPeopleCount() {
        return totalPeopleCount;
    }

    public void setTotalPeopleCount(Integer totalPeopleCount) {
        this.totalPeopleCount = totalPeopleCount;
    }

    @Override
    public String toString() {
        return "CrowdDataDTO{" +
                "temperatureCelsius=" + temperatureCelsius +
                ", totalPeopleCount=" + totalPeopleCount +
                '}';
    }
}
