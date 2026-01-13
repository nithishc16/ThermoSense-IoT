package com.Cloud.CrowdOracle.customExceptions;

public class InvalidCrowdDataException extends RuntimeException {

    public InvalidCrowdDataException(String message) {
        super(message);
    }

    public InvalidCrowdDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCrowdDataException(Double temperature, Integer peopleCount) {
        super("Invalid crowd data - Temperature: " + temperature + ", People Count: " + peopleCount);
    }
}
