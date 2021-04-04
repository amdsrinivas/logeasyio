package com.heisenberg.logeasy.backend.schema;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "aggregatorRegistration")
public class AggregatorRegistration {

    @Id
    private String aggregatorId ;
    private String hostName ;
    private String hostAddress ;
    private String aggregatorUrl ;

    public AggregatorRegistration(String aggregatorId, String hostName, String hostAddress, String aggregatorUrl) {
        this.aggregatorId = aggregatorId;
        this.hostName = hostName;
        this.hostAddress = hostAddress;
        this.aggregatorUrl = aggregatorUrl;
    }

    public String getAggregatorId() {
        return aggregatorId;
    }

    public void setAggregatorId(String aggregatorId) {
        this.aggregatorId = aggregatorId;
    }

    public String getAggregatorUrl() {
        return aggregatorUrl;
    }

    public void setAggregatorUrl(String aggregatorUrl) {
        this.aggregatorUrl = aggregatorUrl;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }
}
