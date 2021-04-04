package com.heisenberg.logeasy.aggregator.model;

import java.util.List;

public class LogDetails {
    private String logId ;
    private String logPath ;
    private List<String> logSchema ;
    private List<Integer> logFields ;

    public LogDetails(String logId, String logPath, List<String> logSchema, List<Integer> logFields) {
        this.logId = logId;
        this.logPath = logPath;
        this.logSchema = logSchema;
        this.logFields = logFields;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public List<String> getLogSchema() {
        return logSchema;
    }

    public void setLogSchema(List<String> logSchema) {
        this.logSchema = logSchema;
    }

    public List<Integer> getLogFields() {
        return logFields;
    }

    public void setLogFields(List<Integer> logFields) {
        this.logFields = logFields;
    }
}
