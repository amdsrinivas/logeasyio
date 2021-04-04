package com.heisenberg.logeasy.aggregator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heisenberg.logeasy.aggregator.dal.AggregatorDAL;
import com.heisenberg.logeasy.aggregator.model.AggregatorStatus;
import com.heisenberg.logeasy.aggregator.model.JobStatus;
import com.heisenberg.logeasy.aggregator.model.LogDetails;
import com.heisenberg.logeasy.aggregator.runner.JobRunner;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
public class AggregatorController {

    @Value("${aggregator.id}")
    private String aggregatorId ;

    @Autowired
    private AggregatorDAL aggregatorDAL ;

    @PostMapping("/status")
    public AggregatorStatus getStatus() throws JsonProcessingException {
        // Make request to aggregator DB using the aggregator ID. Get running threads.
        // Return the status.
        AggregatorStatus status = aggregatorDAL.getStatus(aggregatorId) ;
        return status ;
    }

    @PostMapping("/runJob")
    public String runJob(
//            @RequestParam(value = "logId") String logId,
//            @RequestParam(value = "logPath") String logPath,
//            @RequestParam(value = "logSchema")List<String> logSchema,
//            @RequestParam(value = "logFields")List<Integer> logFields
            @RequestBody Map<String, Object> request
            ){
        try{
            System.out.println("Job request received");
            String logId = (String) request.get("logId") ;
            String logPath = (String) request.get("logPath") ;
            List<String> logSchema = (List<String>) request.get("logSchema") ;
            List<Integer> logFields = (List<Integer>) request.get("logFields") ;

            String jobId = UUID.randomUUID().toString() ;
            new Thread(new JobRunner(jobId, aggregatorId, new LogDetails(
                    logId,
                    logPath,
                    logSchema,
                    logFields
            ),
                    aggregatorDAL)).start();

            return new JSONObject().put("status", "submitted").put("jobId", jobId).toString() ;
        }
        catch (Exception e){
            e.printStackTrace();
            return new JSONObject().put("status", "not submitted").toString() ;
        }

    }
}
