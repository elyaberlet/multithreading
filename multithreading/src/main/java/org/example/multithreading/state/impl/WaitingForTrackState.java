package org.example.multithreading.state.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.multithreading.entity.Track;
import org.example.multithreading.entity.Train;
import org.example.multithreading.resource.Station;
import org.example.multithreading.state.BaseTrainState;

public class WaitingForTrackState implements BaseTrainState {

    private static final Logger logger = LogManager.getLogger(WaitingForTrackState.class);

    @Override
    public void handle(Train train) {
        Station station = train.getStation();

        try {
            logger.debug("Train {} is looking for a free track.", train.getId());

            Track track = station.acquireTrack(train);

            train.setCurrentTrack(track);

            logger.info("Train {} found Track {}.", train.getId(), track.getId());

            if (train.getWagonsToUnload() > 0) {
                train.setState(new UnloadingState());
            } else if (train.getWagonsToLoad() > 0) {
                train.setState(new LoadingState());
            } else {
                train.setState(new DepartedState());
            }

        } catch (InterruptedException e) {
            logger.error("Train {} was interrupted while waiting for track.", train.getId());
            Thread.currentThread().interrupt();
        }
    }
}