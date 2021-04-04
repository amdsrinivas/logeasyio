package com.heisenberg.logeasy.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "aggregators")
public class Aggregator{

    @Id
    private String aggregatorId ;
    private String hostId ;
    private String aggregatorUrl ;
    private Integer maxThreads ;

    public Aggregator() {
    }

    @PersistenceConstructor
    public Aggregator(String aggregatorId, String hostId, String aggregatorUrl, Integer maxThreads) {
        this.aggregatorId = aggregatorId;
        this.hostId = hostId;
        this.aggregatorUrl = aggregatorUrl;
        this.maxThreads = maxThreads;
    }

    public Aggregator(String aggregatorId, String hostId, String aggregatorUrl, String maxThreads) {
        this.aggregatorId = aggregatorId;
        this.hostId = hostId;
        this.aggregatorUrl = aggregatorUrl;
        this.setMaxThreads(maxThreads);
    }

    public String getAggregatorId() {
        return aggregatorId;
    }

    public void setAggregatorId(String aggregatorId) {
        this.aggregatorId = aggregatorId;
    }

    public Integer getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(Integer maxThreads) {
        this.maxThreads = maxThreads;
    }

    public void setMaxThreads(String maxThreads) {
        this.maxThreads = Integer.parseInt(maxThreads);
    }

    public String getHostId() {
        return hostId;
    }
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
    public String getAggregatorUrl() {
        return aggregatorUrl;
    }
    public void setAggregatorUrl(String aggregatorUrl) {
        this.aggregatorUrl = aggregatorUrl;
    }

}
