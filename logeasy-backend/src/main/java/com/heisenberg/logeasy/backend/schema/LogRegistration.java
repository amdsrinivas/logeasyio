package com.heisenberg.logeasy.backend.schema;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "logRegistration")
public class LogRegistration {

    @Id
    private String logId ;
    private String hostId ;
    private String logPath ;
    private List<String> logFields ;
    private List<String> ingestedFields ;

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

    public List<String> getLogFields() {
        return logFields;
    }

    public void setLogFields(List<String> logFields) {
        this.logFields = logFields;
    }

    public List<String> getIngestedFields() {
        return ingestedFields;
    }

    public void setIngestedFields(List<String> ingestedFields) {
        this.ingestedFields = ingestedFields;
    }
}
