package com.Cloud.CrowdOracle.repository;

import com.Cloud.CrowdOracle.entity.CrowdData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CrowdDataRepository extends JpaRepository<CrowdData, Long> {

    // Find all records ordered by entry time (most recent first)
    List<CrowdData> findAllByOrderByEntryTimeDesc();

    // Find records by temperature range
    List<CrowdData> findByTemperatureCelsiusBetween(Double minTemp, Double maxTemp);

    // Find records by people count range
    List<CrowdData> findByTotalPeopleCountBetween(Integer minCount, Integer maxCount);

    // Find records within a specific time range
    List<CrowdData> findByEntryTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    // Find records where people count is greater than specified value
    List<CrowdData> findByTotalPeopleCountGreaterThan(Integer count);

    // Find records where temperature is greater than specified value
    List<CrowdData> findByTemperatureCelsiusGreaterThan(Double temperature);

    // Custom query to get average temperature
    @Query("SELECT AVG(c.temperatureCelsius) FROM CrowdData c WHERE c.entryTime BETWEEN :startTime AND :endTime")
    Double getAverageTemperatureInTimeRange(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    // Custom query to get total people count in time range
    @Query("SELECT SUM(c.totalPeopleCount) FROM CrowdData c WHERE c.entryTime BETWEEN :startTime AND :endTime")
    Long getTotalPeopleCountInTimeRange(@Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);

    // Custom query to get records count in time range
    @Query("SELECT COUNT(c) FROM CrowdData c WHERE c.entryTime BETWEEN :startTime AND :endTime")
    Long getRecordsCountInTimeRange(@Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime);
}
