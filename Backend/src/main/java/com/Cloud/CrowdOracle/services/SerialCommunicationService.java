package com.Cloud.CrowdOracle.services;

import com.Cloud.CrowdOracle.config.SerialPortConfig;
import com.Cloud.CrowdOracle.dtos.CrowdDataDTO;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@ConditionalOnProperty(name = "serial.enabled", havingValue = "true", matchIfMissing = false)
public class SerialCommunicationService {

    private static final Logger logger = LoggerFactory.getLogger(SerialCommunicationService.class);

    private final SerialPortConfig serialPortConfig;
    private final CrowdDataService crowdDataService;
    private final ObjectMapper objectMapper;

    private SerialPort comPort;
    private StringBuilder dataBuffer = new StringBuilder();

    @Autowired
    public SerialCommunicationService(SerialPortConfig serialPortConfig, 
                                       CrowdDataService crowdDataService) {
        this.serialPortConfig = serialPortConfig;
        this.crowdDataService = crowdDataService;
        this.objectMapper = new ObjectMapper();
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing Serial Communication Service...");
        
        String portName = serialPortConfig.getPort().getName();
        int baudRate = serialPortConfig.getPort().getBaudrate();
        
        logger.info("Attempting to connect to port: {} with baud rate: {}", portName, baudRate);
        
        // List available ports for debugging
        listAvailablePorts();
        
        // Try to find and open the configured port
        comPort = SerialPort.getCommPort(portName);
        
        if (comPort == null) {
            logger.error("Serial port {} not found!", portName);
            return;
        }
        
        // Configure port settings
        comPort.setBaudRate(baudRate);
        comPort.setNumDataBits(8);
        comPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        comPort.setParity(SerialPort.NO_PARITY);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        
        // Open the port
        if (comPort.openPort()) {
            logger.info("Successfully opened serial port: {}", portName);
            startListening();
        } else {
            logger.error("Failed to open serial port: {}. Check if another application is using it.", portName);
        }
    }

    private void listAvailablePorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        logger.info("Available serial ports:");
        for (SerialPort port : ports) {
            logger.info("  - {} ({})", port.getSystemPortName(), port.getDescriptivePortName());
        }
        if (ports.length == 0) {
            logger.warn("No serial ports found on this system!");
        }
    }

    private void startListening() {
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
                    return;
                }

                byte[] newData = event.getReceivedData();
                String receivedString = new String(newData);
                
                // Buffer the data (Arduino sends line by line)
                dataBuffer.append(receivedString);
                
                // Process complete lines (ending with newline)
                String bufferContent = dataBuffer.toString();
                int newlineIndex;
                
                while ((newlineIndex = bufferContent.indexOf('\n')) != -1) {
                    String completeLine = bufferContent.substring(0, newlineIndex).trim();
                    bufferContent = bufferContent.substring(newlineIndex + 1);
                    
                    if (!completeLine.isEmpty()) {
                        processSerialData(completeLine);
                    }
                }
                
                // Keep remaining incomplete data in buffer
                dataBuffer = new StringBuilder(bufferContent);
            }
        });
        
        logger.info("Serial port listener started. Waiting for data from Arduino...");
    }

    private void processSerialData(String jsonData) {
        logger.debug("Received data: {}", jsonData);
        
        try {
            // Check if it's an error message from Arduino
            if (jsonData.contains("\"error\"")) {
                JsonNode errorNode = objectMapper.readTree(jsonData);
                String errorMessage = errorNode.get("error").asText();
                logger.warn("Arduino sensor error: {}", errorMessage);
                return;
            }
            
            // Parse the JSON data
            JsonNode rootNode = objectMapper.readTree(jsonData);
            
            Double temperatureCelsius = rootNode.get("temperatureCelsius").asDouble();
            Integer totalPeopleCount = rootNode.get("totalPeopleCount").asInt();
            
            // Create DTO and save to database
            CrowdDataDTO crowdDataDTO = new CrowdDataDTO(temperatureCelsius, totalPeopleCount);
            
            var savedData = crowdDataService.saveCrowdData(crowdDataDTO);
            logger.info("Saved sensor data - ID: {}, Temperature: {}°C, People Count: {}", 
                       savedData.getId(), temperatureCelsius, totalPeopleCount);
            
        } catch (Exception e) {
            logger.error("Failed to parse/save serial data: {} - Error: {}", jsonData, e.getMessage());
        }
    }

    @PreDestroy
    public void cleanup() {
        if (comPort != null && comPort.isOpen()) {
            comPort.removeDataListener();
            comPort.closePort();
            logger.info("Serial port closed successfully.");
        }
    }

    /**
     * Get the connection status of the serial port
     */
    public boolean isConnected() {
        return comPort != null && comPort.isOpen();
    }

    /**
     * Get the name of the connected port
     */
    public String getPortName() {
        return comPort != null ? comPort.getSystemPortName() : "Not connected";
    }

    /**
     * Manually reconnect to the serial port
     */
    public boolean reconnect() {
        cleanup();
        init();
        return isConnected();
    }

    /**
     * Get list of available ports as String array
     */
    public String[] getAvailablePorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        String[] portNames = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            portNames[i] = ports[i].getSystemPortName() + " - " + ports[i].getDescriptivePortName();
        }
        return portNames;
    }
}
