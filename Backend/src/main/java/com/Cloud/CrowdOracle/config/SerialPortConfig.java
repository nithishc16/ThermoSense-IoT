package com.Cloud.CrowdOracle.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "serial")
public class SerialPortConfig {

    private String portName = "COM3";
    private int baudrate = 9600;
    private boolean enabled = true;

    // Getters and Setters
    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // Nested class for port configuration
    public static class Port {
        private String name = "COM3";
        private int baudrate = 9600;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBaudrate() {
            return baudrate;
        }

        public void setBaudrate(int baudrate) {
            this.baudrate = baudrate;
        }
    }

    private Port port = new Port();

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }
}
