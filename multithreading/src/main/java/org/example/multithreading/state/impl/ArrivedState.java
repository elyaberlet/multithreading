package org.example.multithreading.state.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.multithreading.entity.Train;
import org.example.multithreading.state.BaseTrainState;

public class ArrivedState implements BaseTrainState {
    private static final Logger logger = LogManager.getLogger(ArrivedState.class);

    @Override
    public void handle(Train train) {
        logger.info("Train {} has arrived at the station.", train.getId());
        train.setState(new WaitingForTrackState());
    }
}
