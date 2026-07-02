package org.example.multithreading;

import org.example.multithreading.entity.Train;
import org.example.multithreading.reader.DataReader;
import org.example.multithreading.resource.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting railway station system");

        DataReader reader = new DataReader();
        String filePath = "data/config.txt";

        try {
            List<String> lines = reader.readAllLines(filePath);

            if (lines.size() > 1) {
                String firstLine = lines.getFirst();
                String[] stationParams = firstLine.split(" ");
                int tracksCount = Integer.parseInt(stationParams[0]);
                int warehouseCapacity = Integer.parseInt(stationParams[1]);

                Station.init(tracksCount, warehouseCapacity);
                Station station = Station.getInstance();

                ExecutorService executorService = Executors.newCachedThreadPool();
                int totalLines = lines.size();

                for (int i = 1; i < totalLines; i++) {
                    String line = lines.get(i);
                    String[] trainParams = line.split(" ");

                    int trainId = Integer.parseInt(trainParams[0]);
                    int wagonsToUnload = Integer.parseInt(trainParams[1]);
                    int wagonsToLoad = Integer.parseInt(trainParams[2]);

                    Train train = new Train(trainId, wagonsToUnload, wagonsToLoad, station);
                    executorService.submit(train);
                }

                executorService.shutdown();
                boolean isTerminated = executorService.awaitTermination(1, TimeUnit.MINUTES);

                if (isTerminated) {
                    logger.info("Railway station closed");
                } else {
                    logger.warn("Station closed forcibly (timeout)");
                }

            } else {
                logger.error("Config file is empty or missing trains");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Main thread was interrupted", e);
        } catch (Exception e) {
            logger.error("Critical error during initialization", e);
        }
    }
}