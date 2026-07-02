package org.example.multithreading.state.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.multithreading.entity.Train;
import org.example.multithreading.resource.Station;
import org.example.multithreading.state.BaseTrainState;

import java.util.concurrent.TimeUnit;

public class LoadingState implements BaseTrainState {

    private static final Logger logger = LogManager.getLogger(LoadingState.class);

    @Override
    public void handle(Train train) {
        Station station = train.getStation();
        int wagonsToLoad = train.getWagonsToLoad();

        try {
            logger.info("Train {} is loading {} wagons from warehouse.",
                    train.getId(), wagonsToLoad);

            station.loadWagons(wagonsToLoad);

            TimeUnit.MILLISECONDS.sleep(wagonsToLoad * 100L);

            logger.info("Train {} finished loading.", train.getId());

            train.setState(new DepartedState());

        } catch (InterruptedException e) {
            logger.error("Train {} was interrupted while loading.", train.getId());
            Thread.currentThread().interrupt();
        }
    }
}