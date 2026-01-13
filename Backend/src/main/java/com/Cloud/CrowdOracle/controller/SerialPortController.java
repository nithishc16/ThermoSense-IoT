package com.Cloud.CrowdOracle.controller;

import com.Cloud.CrowdOracle.services.SerialCommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/serial")
@ConditionalOnBean(SerialCommunicationService.class)
public class SerialPortController {

    private final SerialCommunicationService serialCommunicationService;

    @Autowired
    public SerialPortController(SerialCommunicationService serialCommunicationService) {
        this.serialCommunicationService = serialCommunicationService;
    }

    /**
     * Get the current status of the serial connection
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("connected", serialCommunicationService.isConnected());
        status.put("portName", serialCommunicationService.getPortName());
        status.put("availablePorts", serialCommunicationService.getAvailablePorts());
        return ResponseEntity.ok(status);
    }

    /**
     * Reconnect to the serial port
     */
    @PostMapping("/reconnect")
    public ResponseEntity<Map<String, Object>> reconnect() {
        boolean success = serialCommunicationService.reconnect();
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("connected", serialCommunicationService.isConnected());
        response.put("portName", serialCommunicationService.getPortName());
        
        if (success) {
            response.put("message", "Successfully reconnected to serial port");
        } else {
            response.put("message", "Failed to reconnect to serial port");
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * List all available serial ports on the system
     */
    @GetMapping("/ports")
    public ResponseEntity<String[]> getAvailablePorts() {
        return ResponseEntity.ok(serialCommunicationService.getAvailablePorts());
    }
}
