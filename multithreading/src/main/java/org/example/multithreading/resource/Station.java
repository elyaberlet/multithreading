package org.example.multithreading.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.multithreading.entity.Track;
import org.example.multithreading.entity.Train;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Station {
    private static final Logger logger = LogManager.getLogger(Station.class);

    private final Track[] tracks;

    private int wagonsOnStation;
    private final int warehouseCapacity;

    private final Lock tracksLock = new ReentrantLock(true);
    private final Condition trackAvailable = tracksLock.newCondition();

    private final Lock warehouseLock = new ReentrantLock(true);
    private final Condition warehouseSpaceAvailable = warehouseLock.newCondition();
    private final Condition wagonsAvailable = warehouseLock.newCondition();

    private Station(int tracksCount, int warehouseCapacity) {
        this.tracks = new Track[tracksCount];
        for (int i = 0; i < tracksCount; i++) {
            this.tracks[i] = new Track(i + 1);
        }
        this.wagonsOnStation = 0;
        this.warehouseCapacity = warehouseCapacity;
    }

    public Track acquireTrack(Train train) throws InterruptedException {
        tracksLock.lock();
        try {
            while (true) {
                for (Track track : tracks) {
                    if (!track.isOccupied()) {
                        track.occupy(train);
                        logger.info("Train {} occupied Track {}.", train.getId(), track.getId());
                        return track;
                    }
                }

                logger.debug("Train {} is waiting for a free track.", train.getId());
                trackAvailable.await();
            }
        } finally {
            tracksLock.unlock();
        }
    }

    public void releaseTrack(Track track) {
        tracksLock.lock();
        try {
            track.release();
            logger.info("Train released Track {}.", track.getId());
            trackAvailable.signal();
        } finally {
            tracksLock.unlock();
        }
    }

    public void unloadWagons(int count) throws InterruptedException {
        warehouseLock.lock();  // Захватываем замок склада
        try {
            while (wagonsOnStation + count > warehouseCapacity) {
                logger.debug("Warehouse full ({}/{}). Train is waiting to unload {} wagons.",
                        wagonsOnStation, warehouseCapacity, count);
                warehouseSpaceAvailable.await();
            }

            wagonsOnStation += count;
            logger.info("Unloaded {} wagons to warehouse. Total on station: {}/{}.",
                    count, wagonsOnStation, warehouseCapacity);

            wagonsAvailable.signalAll();
        } finally {
            warehouseLock.unlock();
        }
    }

    public void loadWagons(int count) throws InterruptedException {
        warehouseLock.lock();
        try {
            while (wagonsOnStation < count) {
                logger.debug("Not enough wagons on station ({}/{}). Train is waiting to load {} wagons.",
                        wagonsOnStation, warehouseCapacity, count);
                wagonsAvailable.await();
            }

            wagonsOnStation -= count;
            logger.info("Loaded {} wagons from warehouse. Total on station: {}/{}.",
                    count, wagonsOnStation, warehouseCapacity);

            warehouseSpaceAvailable.signalAll();
        } finally {
            warehouseLock.unlock();
        }
    }

    private static class StationHolder {
        private static Station INSTANCE;

        static void init(int tracksCount, int warehouseCapacity) {
            if (INSTANCE == null) {
                INSTANCE = new Station(tracksCount, warehouseCapacity);
            }
        }
    }
        public static void init(int tracksCount, int warehouseCapacity) {
            StationHolder.init(tracksCount, warehouseCapacity);
        }

        public static Station getInstance() {
            if (StationHolder.INSTANCE == null) {
                throw new IllegalStateException("Station не инициализирован!");
            }
            return StationHolder.INSTANCE;
        }
    }
