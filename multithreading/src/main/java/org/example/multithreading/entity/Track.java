package org.example.multithreading.entity;

public class Track {

    private final int id;
    private Train currentTrain;

    public Track(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isOccupied() {
        return currentTrain != null;
    }

    public void occupy(Train train) {
        if (train != null) {
            this.currentTrain = train;
        }
    }

    public void release() {
        this.currentTrain = null;
    }

    public Train getCurrentTrain() {
        return currentTrain;
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
        Track that = (Track) obj;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("Track{id=%d, isOccupied=%b}", id, isOccupied());
    }
}