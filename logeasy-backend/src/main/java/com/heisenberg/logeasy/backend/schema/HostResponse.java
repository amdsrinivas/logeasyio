package com.heisenberg.logeasy.backend.schema;

public class HostResponse {
    private String hostId ;
    private String status ;
    private String responseMessage ;

    public HostResponse(String hostId, String status, String responseMessage) {
        this.hostId = hostId;
        this.status = status;
        this.responseMessage = responseMessage;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
