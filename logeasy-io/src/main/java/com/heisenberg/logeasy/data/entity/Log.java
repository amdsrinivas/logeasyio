package com.heisenberg.logeasy.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "loggers")
public class Log {

    @Id
    private String logId ;
    private String hostId ;
    private String logPath ;
    private List<String> logSchema ;
    private List<Integer> logFields ;

    public Log() {
    }

    @PersistenceConstructor
    public Log(String logId, String hostId, String logPath, List<String> logSchema, List<Integer> logFields) {
        this.logId = logId;
        this.hostId = hostId;
        this.logPath = logPath;
        this.logSchema = logSchema;
        this.logFields = logFields;
    }

    public Log(String logId, String hostId, String logPath, String logSchema, String logFields){
        this.logId = logId;
        this.hostId = hostId;
        this.logPath = logPath;
        this.setLogSchema(logSchema);
        this.setLogFields(logFields);
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
    public void setLogSchema(String logSchema) { this.logSchema = Arrays.asList(logSchema.split(",")) ;}

    public List<Integer> getLogFields() {
        return logFields;
    }

    public void setLogFields(List<Integer> logFields) {
        this.logFields = logFields;
    }
    public void setLogFields(String logFields) {
        this.logFields = Arrays.asList(logFields.split(",")).
                stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}

