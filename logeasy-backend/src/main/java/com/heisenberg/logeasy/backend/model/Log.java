package com.heisenberg.logeasy.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "loggers")
public class Log {

    @Id
    private String logId ;
    private String hostId ;
    private String logPath ;
    private List<String> logSchema ;
    private List<Integer> logFields ;

    public Log(String logId, String hostId, String logPath, List<String> logSchema, List<Integer> logFields) {
        this.logId = logId;
        this.hostId = hostId;
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

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
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
