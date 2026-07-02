package org.example.multithreading.entity;

import org.example.multithreading.resource.Station;
import org.example.multithreading.state.BaseTrainState;
import org.example.multithreading.state.impl.DepartedState;
import org.example.multithreading.state.impl.ArrivedState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

public class Train implements Callable<String> {

    private static final Logger logger = LogManager.getLogger(Train.class);

    private final int id;
    private final int wagonsToUnload;
    private final int wagonsToLoad;
    private final Station station;
    private BaseTrainState state;
    private Track currentTrack;

    public Train(int id, int wagonsToUnload, int wagonsToLoad, Station station) {
        this.id = id;
        this.wagonsToUnload = wagonsToUnload;
        this.wagonsToLoad = wagonsToLoad;
        this.station = station;
        this.state = new ArrivedState();
    }

    public int getId() {
        return id;
    }

    public int getWagonsToUnload() {
        return wagonsToUnload;
    }

    public int getWagonsToLoad() {
        return wagonsToLoad;
    }

    public Station getStation() {
        return station;
    }

    public void setState(BaseTrainState state) {
        if (state != null) {
            this.state = state;
        }
    }

    public BaseTrainState getState() {
        return state;
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Track track) {
        this.currentTrack = track;
    }

    @Override
    public String call() throws Exception {
        long startTime = System.currentTimeMillis();

        while (!(state instanceof DepartedState)) {
            state.handle(this);
        }

        state.handle(this);

        long endTime = System.currentTimeMillis();
        long durationInSeconds = (endTime - startTime) / 1000;

        String report = String.format(
                "Train %d has departed. Unloaded: %d wagons, Loaded: %d wagons. Time spent: %ds.",
                id, wagonsToUnload, wagonsToLoad, durationInSeconds
        );

        logger.info(report);

        return report;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Train that = (Train) obj;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("Train{id=%d, toUnload=%d, toLoad=%d}",
                id, wagonsToUnload, wagonsToLoad);
    }
}