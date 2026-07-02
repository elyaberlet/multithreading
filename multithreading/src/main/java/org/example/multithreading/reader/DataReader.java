package org.example.multithreading.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.multithreading.exception.RailwayStationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class DataReader {
    private static final Logger logger = LogManager.getLogger(DataReader.class);

    public List<String> readAllLines(String filePath) throws RailwayStationException {

        Path path = Path.of(filePath);

        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            logger.error("Error reading file", e);
            throw new RailwayStationException("Failed to read file", e);
        }
    }
}
