package org.example.multithreading.state.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.multithreading.entity.Track;
import org.example.multithreading.entity.Train;
import org.example.multithreading.resource.Station;
import org.example.multithreading.state.BaseTrainState;

public class DepartedState implements BaseTrainState {

    private static final Logger logger = LogManager.getLogger(DepartedState.class);

    @Override
    public void handle(Train train) {
        Station station = train.getStation();
        Track track = train.getCurrentTrack();

        station.releaseTrack(track);
        logger.info("Train {} released Track {} and departed from station.",
                train.getId(), track.getId());
    }
}