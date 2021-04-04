package com.heisenberg.logeasy.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;

@Document(collection = "logs_ingested")
public class IngestedLogs {
    @Id
    private String logPath ;
    private List<HashMap<String, String>> logsList ;

    public IngestedLogs(String logPath, List<HashMap<String, String>> logsList) {
        this.logPath = logPath;
        this.logsList = logsList;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public List<HashMap<String, String>> getLogsList() {
        return logsList;
    }

    public void setLogsList(List<HashMap<String, String>> logsList) {
        this.logsList = logsList;
    }
}
