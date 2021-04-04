package com.heisenberg.logeasy.aggregator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Document(collection = "logPathStatus")
public class LogPathStatus {

    @Id
    private String logPath ;
    private long lastIngestedTime ;
    private HashMap<String, Integer> directoryListing ;

    public LogPathStatus(String logPath, long lastIngestedTime, HashMap<String, Integer> directoryListing) {
        this.logPath = logPath;
        this.lastIngestedTime = lastIngestedTime;
        this.directoryListing = directoryListing ;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public long getLastIngestedTime() {
        return lastIngestedTime;
    }

    public void setLastIngestedTime(long lastIngestedTime) {
        this.lastIngestedTime = lastIngestedTime;
    }

    public HashMap<String, Integer> getDirectoryListing() {
        return directoryListing;
    }

    public void setDirectoryListing(HashMap<String, Integer> directoryListing) {
        this.directoryListing = directoryListing;
    }
}
