package com.heisenberg.logeasy.aggregator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "aggregator_threads")
public class AggregatorThreads {

    @Id
    private String aggregatorId ;
    private int currentThreads ;

    public AggregatorThreads(String aggregatorId, int currentThreads) {
        this.aggregatorId = aggregatorId;
        this.currentThreads = currentThreads;
    }

    public String getAggregatorId() {
        return aggregatorId;
    }

    public void setAggregatorId(String aggregatorId) {
        this.aggregatorId = aggregatorId;
    }

    public int getCurrentThreads() {
        return currentThreads;
    }

    public void setCurrentThreads(int currentThreads) {
        this.currentThreads = currentThreads;
    }
}
