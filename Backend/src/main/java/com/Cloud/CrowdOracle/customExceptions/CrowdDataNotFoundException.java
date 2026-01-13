package com.Cloud.CrowdOracle.customExceptions;

public class CrowdDataNotFoundException extends RuntimeException {

    public CrowdDataNotFoundException(String message) {
        super(message);
    }

    public CrowdDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrowdDataNotFoundException(Long id) {
        super("Crowd data not found with ID: " + id);
    }
}
