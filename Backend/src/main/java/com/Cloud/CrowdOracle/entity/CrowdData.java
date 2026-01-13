package com.Cloud.CrowdOracle.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "crowd_data")
public class CrowdData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "temperature_celsius", nullable = false)
    private Double temperatureCelsius;

    @Column(name = "total_people_count", nullable = false)
    private Integer totalPeopleCount;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    // Default constructor
    public CrowdData() {}

    // Constructor without id and entryTime (for creating new entries)
    public CrowdData(Double temperatureCelsius, Integer totalPeopleCount) {
        this.temperatureCelsius = temperatureCelsius;
        this.totalPeopleCount = totalPeopleCount;
        this.entryTime = LocalDateTime.now(); // Set current time when creating
    }

    // Full constructor
    public CrowdData(Long id, Double temperatureCelsius, Integer totalPeopleCount, LocalDateTime entryTime) {
        this.id = id;
        this.temperatureCelsius = temperatureCelsius;
        this.totalPeopleCount = totalPeopleCount;
        this.entryTime = entryTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    @Override
    public String toString() {
        return "CrowdData{" +
                "id=" + id +
                ", temperatureCelsius=" + temperatureCelsius +
                ", totalPeopleCount=" + totalPeopleCount +
                ", entryTime=" + entryTime +
                '}';
    }
}
