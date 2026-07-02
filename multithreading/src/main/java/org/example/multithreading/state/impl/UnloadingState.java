package org.example.multithreading.state.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.multithreading.entity.Train;
import org.example.multithreading.resource.Station;
import org.example.multithreading.state.BaseTrainState;

import java.util.concurrent.TimeUnit;

public class UnloadingState implements BaseTrainState {

    private static final Logger logger = LogManager.getLogger(UnloadingState.class);

    @Override
    public void handle(Train train) {
        Station station = train.getStation();
        int wagonsToUnload = train.getWagonsToUnload();

        try {
            logger.info("Train {} is unloading {} wagons to warehouse.",
                    train.getId(), wagonsToUnload);

            station.unloadWagons(wagonsToUnload);

            TimeUnit.MILLISECONDS.sleep(wagonsToUnload * 100L);

            logger.info("Train {} finished unloading.", train.getId());

            if (train.getWagonsToLoad() > 0) {
                train.setState(new LoadingState());
            } else {
                train.setState(new DepartedState());
            }

        } catch (InterruptedException e) {
            logger.error("Train {} was interrupted while unloading.", train.getId());
            Thread.currentThread().interrupt();
        }
    }
}