package com.heisenberg.logeasy.backend.schema;

public class AggregatorResponse {
    private String aggregatorId ;
    private String status ;
    private String responseMessage ;

    public AggregatorResponse(String aggregatorId, String status, String responseMessage) {
        this.aggregatorId = aggregatorId;
        this.status = status;
        this.responseMessage = responseMessage;
    }

    public String getAggregatorId() {
        return aggregatorId;
    }

    public void setAggregatorId(String aggregatorId) {
        this.aggregatorId = aggregatorId;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
