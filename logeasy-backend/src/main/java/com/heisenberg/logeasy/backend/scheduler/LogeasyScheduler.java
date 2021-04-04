package com.heisenberg.logeasy.backend.scheduler;

import com.heisenberg.logeasy.backend.dal.AggregatorDAL;
import com.heisenberg.logeasy.backend.dal.LogDAL;
import com.heisenberg.logeasy.backend.model.Aggregator;
import com.heisenberg.logeasy.backend.model.Log;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class LogeasyScheduler {
    private final AggregatorDAL aggregatorDAL ;
    private final RestTemplate restTemplate ;
    private final LogDAL logDAL ;

    @Autowired
    public LogeasyScheduler(AggregatorDAL aggregatorDAL, RestTemplateBuilder restTemplateBuilder, LogDAL logDAL) {
        this.aggregatorDAL = aggregatorDAL ;
        this.restTemplate = restTemplateBuilder.build();
        this.logDAL = logDAL;
    }

    //@Scheduled(cron = "*/5 * * * *")
    public void checkAggregatorHeartbeat(){
        ResponseEntity<JSONObject> response ;
        String aggregatorUrl ;
        String statusEndpoint = "/status" ;
        for(Aggregator aggregator : aggregatorDAL.getAll()){
            aggregatorUrl = aggregator.getAggregatorUrl() ;
            response = restTemplate.getForEntity(aggregatorUrl + statusEndpoint, JSONObject.class) ;
            if(response.getStatusCode() == HttpStatus.OK){
                // TODO : Update the aggregator heartbeat time.
            }
            else{
                // TODO : Update the aggregator heartbeat not found.
            }
        }
    }

    //@Scheduled(cron = "*/20 * * * *")
    public void runAggregation(){
        /*
        Steps :
        1. get all loggers.
        2. get the aggregators available for each logger based on hostId.
        3. Check the status of each aggregator and submit the job to the first aggregator that can accommodate the job.
        4. If all aggregators are busy, simply wait for the next run of the scheduler call.
         */
        String hostId ;
        List<Aggregator> availableAggregators ;
        for(Log logger : logDAL.getAll()){
            hostId = logger.getHostId() ;
            availableAggregators = aggregatorDAL.getAll(hostId) ;

            if(availableAggregators == null){
                System.out.println("No aggregators registered for the host : " + hostId);
            }
            else{
                for(Aggregator aggregator : availableAggregators){
                    JSONObject status = getStatus(aggregator) ;
                    if(status != null && status.getInt("runningThreads") < aggregator.getMaxThreads()){
                        // TODO : make Rest call to aggregator with job details.
                        break ;
                    }
                    else{
                       System.out.println("Aggregator Busy/unavailable : " + aggregator.getAggregatorId());
                    }
                }
            }
        }
    }

    public JSONObject getStatus(Aggregator aggregator){
        String aggregatorUrl = aggregator.getAggregatorUrl() ;
        String statusEndpoint = "/status" ;
        ResponseEntity<JSONObject> response = restTemplate.getForEntity(aggregatorUrl + statusEndpoint, JSONObject.class) ;

        if(response.getStatusCode() == HttpStatus.OK){
            // TODO : Update the aggregator heartbeat time.
            return response.getBody() ;
        }
        else{
            // TODO : Update the aggregator heartbeat not found.
            return  null ;
        }
    }
}
