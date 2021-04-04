package com.heisenberg.logeasy.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hosts")
public class Host{

    @Id
    private String hostId;
    private String hostName;
    private String hostAddress;

    public String getHostId() {
        return hostId;
    }
    public void setHostId(String hostId) {
        this.hostId = hostId;
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
