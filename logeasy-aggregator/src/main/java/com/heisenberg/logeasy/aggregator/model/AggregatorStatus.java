package com.heisenberg.logeasy.aggregator.model;


public class AggregatorStatus {

    private int runningThreads ;

    public AggregatorStatus(int runningThreads) {
        this.runningThreads = runningThreads;
    }

    public int getRunningThreads() {
        return runningThreads;
    }

    public void setRunningThreads(int runningThreads) {
        this.runningThreads = runningThreads;
    }
}
