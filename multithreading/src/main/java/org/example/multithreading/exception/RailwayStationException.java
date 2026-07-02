package org.example.multithreading.exception;

public class RailwayStationException extends Exception {
    public RailwayStationException(String message) {
        super(message);
    }

    public RailwayStationException(String message, Throwable cause) {
        super(message, cause);
    }
}
