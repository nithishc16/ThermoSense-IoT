package com.Cloud.CrowdOracle.controller;

import com.Cloud.CrowdOracle.dtos.CrowdDataDTO;
import com.Cloud.CrowdOracle.entity.CrowdData;
import com.Cloud.CrowdOracle.services.CrowdDataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/crowd-data")
@CrossOrigin(origins = "*") // Allow CORS for frontend integration
public class CrowdDataController {

    private final CrowdDataService crowdDataService;

    @Autowired
    public CrowdDataController(CrowdDataService crowdDataService) {
        this.crowdDataService = crowdDataService;
    }

    /**
     * POST endpoint to save single crowd data entry
     * Usage: POST /api/crowd-data
     * Body: {"temperatureCelsius": 25.5, "totalPeopleCount": 150}
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveCrowdData(@Valid @RequestBody CrowdDataDTO crowdDataDTO) {
        try {
            CrowdData savedData = crowdDataService.saveCrowdData(crowdDataDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Crowd data saved successfully");
            response.put("data", savedData);
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error saving crowd data: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * POST endpoint to save multiple crowd data entries
     * Usage: POST /api/crowd-data/batch
     * Body: [{"temperatureCelsius": 25.5, "totalPeopleCount": 150}, {"temperatureCelsius": 26.0, "totalPeopleCount": 200}]
     */
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> saveMultipleCrowdData(@Valid @RequestBody List<CrowdDataDTO> crowdDataDTOs) {
        try {
            if (crowdDataDTOs == null || crowdDataDTOs.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "No data provided");
                errorResponse.put("timestamp", LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            List<CrowdData> savedData = crowdDataService.saveMultipleCrowdData(crowdDataDTOs);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Crowd data batch saved successfully");
            response.put("data", savedData);
            response.put("recordsSaved", savedData.size());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error saving crowd data batch: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET endpoint to retrieve all crowd data
     * Usage: GET /api/crowd-data
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCrowdData() {
        try {
            List<CrowdData> allData = crowdDataService.getAllCrowdData();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Crowd data retrieved successfully");
            response.put("data", allData);
            response.put("totalRecords", allData.size());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving crowd data: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET endpoint to retrieve crowd data by ID
     * Usage: GET /api/crowd-data/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCrowdDataById(@PathVariable Long id) {
        try {
            Optional<CrowdData> crowdData = crowdDataService.getCrowdDataById(id);

            Map<String, Object> response = new HashMap<>();
            if (crowdData.isPresent()) {
                response.put("success", true);
                response.put("message", "Crowd data found");
                response.put("data", crowdData.get());
            } else {
                response.put("success", false);
                response.put("message", "Crowd data not found with ID: " + id);
            }
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving crowd data: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET endpoint to retrieve crowd data by temperature range
     * Usage: GET /api/crowd-data/temperature?min=20&max=30
     */
    @GetMapping("/temperature")
    public ResponseEntity<Map<String, Object>> getCrowdDataByTemperatureRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        try {
            List<CrowdData> data = crowdDataService.getCrowdDataByTemperatureRange(min, max);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Crowd data retrieved by temperature range");
            response.put("data", data);
            response.put("totalRecords", data.size());
            response.put("temperatureRange", Map.of("min", min, "max", max));
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving crowd data by temperature: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET endpoint to retrieve crowd data by people count range
     * Usage: GET /api/crowd-data/people?min=100&max=500
     */
    @GetMapping("/people")
    public ResponseEntity<Map<String, Object>> getCrowdDataByPeopleCountRange(
            @RequestParam Integer min,
            @RequestParam Integer max) {
        try {
            List<CrowdData> data = crowdDataService.getCrowdDataByPeopleCountRange(min, max);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Crowd data retrieved by people count range");
            response.put("data", data);
            response.put("totalRecords", data.size());
            response.put("peopleCountRange", Map.of("min", min, "max", max));
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving crowd data by people count: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET endpoint to retrieve the latest crowd data entry
     * Usage: GET /api/crowd-data/latest
     */
    @GetMapping("/latest")
    public ResponseEntity<Map<String, Object>> getLatestCrowdData() {
        try {
            List<CrowdData> allData = crowdDataService.getAllCrowdData();
            
            Map<String, Object> response = new HashMap<>();
            if (!allData.isEmpty()) {
                CrowdData latest = allData.get(0); // Already sorted by entry time desc
                response.put("success", true);
                response.put("message", "Latest crowd data retrieved");
                response.put("data", latest);
                response.put("currentPeopleCount", latest.getTotalPeopleCount());
                response.put("currentTemperature", latest.getTemperatureCelsius());
            } else {
                response.put("success", true);
                response.put("message", "No crowd data available");
                response.put("data", null);
                response.put("currentPeopleCount", 0);
                response.put("currentTemperature", null);
            }
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving latest crowd data: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET endpoint to get statistics summary
     * Usage: GET /api/crowd-data/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCrowdDataStats() {
        try {
            List<CrowdData> allData = crowdDataService.getAllCrowdData();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Statistics retrieved successfully");
            response.put("totalRecords", allData.size());
            
            if (!allData.isEmpty()) {
                // Calculate statistics
                double avgPeopleCount = allData.stream()
                    .mapToInt(CrowdData::getTotalPeopleCount)
                    .average()
                    .orElse(0);
                
                int maxPeopleCount = allData.stream()
                    .mapToInt(CrowdData::getTotalPeopleCount)
                    .max()
                    .orElse(0);
                
                int minPeopleCount = allData.stream()
                    .mapToInt(CrowdData::getTotalPeopleCount)
                    .min()
                    .orElse(0);
                
                double avgTemperature = allData.stream()
                    .mapToDouble(CrowdData::getTemperatureCelsius)
                    .average()
                    .orElse(0);
                
                response.put("averagePeopleCount", Math.round(avgPeopleCount * 100.0) / 100.0);
                response.put("maxPeopleCount", maxPeopleCount);
                response.put("minPeopleCount", minPeopleCount);
                response.put("averageTemperature", Math.round(avgTemperature * 100.0) / 100.0);
                response.put("latestEntry", allData.get(0));
            }
            
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving statistics: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
