package com.heisenberg.logeasy.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hosts")
public class Host {

    @Id
    public String hostId ;
    public String hostName ;
    public String hostAddress ;

    public Host(String hostId, String hostName, String hostAddress) {
        this.hostId = hostId;
        this.hostName = hostName;
        this.hostAddress = hostAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }
}
