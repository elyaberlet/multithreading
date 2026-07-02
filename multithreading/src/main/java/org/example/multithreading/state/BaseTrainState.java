package org.example.multithreading.state;

import org.example.multithreading.entity.Train;

public interface BaseTrainState {
    void handle(Train train);
}
