package com.Cloud.CrowdOracle.services;

import com.Cloud.CrowdOracle.dtos.CrowdDataDTO;
import com.Cloud.CrowdOracle.entity.CrowdData;
import com.Cloud.CrowdOracle.repository.CrowdDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CrowdDataService {

    private final CrowdDataRepository crowdDataRepository;

    @Autowired
    public CrowdDataService(CrowdDataRepository crowdDataRepository) {
        this.crowdDataRepository = crowdDataRepository;
    }

    /**
     * Save crowd data from DTO
     */
    public CrowdData saveCrowdData(CrowdDataDTO crowdDataDTO) {
        // Convert DTO to Entity
        CrowdData crowdData = new CrowdData(
            crowdDataDTO.getTemperatureCelsius(),
            crowdDataDTO.getTotalPeopleCount()
        );

        // Save and return the saved entity
        return crowdDataRepository.save(crowdData);
    }

    /**
     * Save multiple crowd data entries in a batch
     */
    public List<CrowdData> saveMultipleCrowdData(List<CrowdDataDTO> crowdDataDTOs) {
        List<CrowdData> crowdDataList = crowdDataDTOs.stream()
            .map(dto -> new CrowdData(dto.getTemperatureCelsius(), dto.getTotalPeopleCount()))
            .toList();

        return crowdDataRepository.saveAll(crowdDataList);
    }

    /**
     * Get all crowd data records ordered by entry time (most recent first)
     */
    @Transactional(readOnly = true)
    public List<CrowdData> getAllCrowdData() {
        return crowdDataRepository.findAllByOrderByEntryTimeDesc();
    }

    /**
     * Get crowd data by ID
     */
    @Transactional(readOnly = true)
    public Optional<CrowdData> getCrowdDataById(Long id) {
        return crowdDataRepository.findById(id);
    }

    /**
     * Get crowd data by temperature range
     */
    @Transactional(readOnly = true)
    public List<CrowdData> getCrowdDataByTemperatureRange(Double minTemp, Double maxTemp) {
        return crowdDataRepository.findByTemperatureCelsiusBetween(minTemp, maxTemp);
    }

    /**
     * Get crowd data by people count range
     */
    @Transactional(readOnly = true)
    public List<CrowdData> getCrowdDataByPeopleCountRange(Integer minCount, Integer maxCount) {
        return crowdDataRepository.findByTotalPeopleCountBetween(minCount, maxCount);
    }

    /**
     * Get crowd data within a specific time range
     */
    @Transactional(readOnly = true)
    public List<CrowdData> getCrowdDataByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return crowdDataRepository.findByEntryTimeBetween(startTime, endTime);
    }

    /**
     * Get records where people count is greater than specified value
     */
    @Transactional(readOnly = true)
    public List<CrowdData> getHighCrowdData(Integer threshold) {
        return crowdDataRepository.findByTotalPeopleCountGreaterThan(threshold);
    }

    /**
     * Get records where temperature is greater than specified value
     */
    @Transactional(readOnly = true)
    public List<CrowdData> getHighTemperatureData(Double threshold) {
        return crowdDataRepository.findByTemperatureCelsiusGreaterThan(threshold);
    }

    /**
     * Get average temperature in a time range
     */
    @Transactional(readOnly = true)
    public Double getAverageTemperature(LocalDateTime startTime, LocalDateTime endTime) {
        return crowdDataRepository.getAverageTemperatureInTimeRange(startTime, endTime);
    }

    /**
     * Get total people count in a time range
     */
    @Transactional(readOnly = true)
    public Long getTotalPeopleCount(LocalDateTime startTime, LocalDateTime endTime) {
        return crowdDataRepository.getTotalPeopleCountInTimeRange(startTime, endTime);
    }

    /**
     * Get record count in a time range
     */
    @Transactional(readOnly = true)
    public Long getRecordCount(LocalDateTime startTime, LocalDateTime endTime) {
        return crowdDataRepository.getRecordsCountInTimeRange(startTime, endTime);
    }

    /**
     * Delete crowd data by ID
     */
    public boolean deleteCrowdData(Long id) {
        if (crowdDataRepository.existsById(id)) {
            crowdDataRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Delete all crowd data (use with caution)
     */
    public void deleteAllCrowdData() {
        crowdDataRepository.deleteAll();
    }
}
