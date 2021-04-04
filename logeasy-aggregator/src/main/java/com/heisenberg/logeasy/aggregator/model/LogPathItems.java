package com.heisenberg.logeasy.aggregator.model;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "logs_ingested")
public class LogPathItems {

    @Id
    private String logPath ;
    private List<Map<String,String>> logsList ;

    @PersistenceConstructor
    public LogPathItems(String logPath, List<Map<String,String>> logsList) {
        this.logPath = logPath;
        this.logsList = logsList;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public List<Map<String,String>> getLogsList() {
        return logsList;
    }

    public void setLogsList(List<Map<String,String>> logsList) {
        this.logsList = logsList;
    }

    public void updateItems(Map<String, String> newItem){
        this.logsList.add(newItem) ;
    }
}
