package com.heisenberg.logeasy.backend.controller;

import com.heisenberg.logeasy.backend.dal.AggregatorDAL;
import com.heisenberg.logeasy.backend.dal.HostDAL;
import com.heisenberg.logeasy.backend.dal.LogDAL;
import com.heisenberg.logeasy.backend.model.Aggregator;
import com.heisenberg.logeasy.backend.model.Host;
import com.heisenberg.logeasy.backend.model.Log;
import com.heisenberg.logeasy.backend.schema.AggregatorResponse;
import com.heisenberg.logeasy.backend.schema.HostResponse;
import com.heisenberg.logeasy.backend.utils.Constants;
import com.heisenberg.logeasy.backend.utils.RegistrationUtils;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class RegistrationController {

    private final RegistrationUtils registrationUtils ;

    private final HostDAL hostDAL ;

    private final AggregatorDAL aggregatorDAL ;

    private final LogDAL logDAL ;

    public RegistrationController(RegistrationUtils registrationUtils, HostDAL hostDAL, AggregatorDAL aggregatorDAL, LogDAL logDAL) {
        this.registrationUtils = registrationUtils;
        this.hostDAL = hostDAL;
        this.aggregatorDAL = aggregatorDAL;
        this.logDAL = logDAL;
    }

    @PostMapping("/register/log")
    public String registerLog(
//            @RequestBody String hostId,
//            @RequestBody String logPath,
//            @RequestBody List<String> logFields,
//            @RequestBody List<Integer> ingestedFields
              @RequestBody Map<String, Object> request
            ){
        String hostId = (String) request.get("hostId") ;
        String logPath = (String) request.get("logPath") ;
        List<String> logFields = (List<String>) request.get("logFields");
        List<Integer> ingestedFields = (List<Integer>) request.get("ingestedFields");
        Log logger = logDAL.getLogger("logPath", logPath) ;
        if( logger == null){
            // TODO : Create new Log object with the parameters and save to DB. Return the logID as part of response.
            logger = new Log(registrationUtils.getId(),
                    hostId,
                    logPath,
                    logFields,
                    ingestedFields) ;
            if(logDAL.save(logger)){
                return new JSONObject().
                        put("logId", logger.getLogId()).
                        put("status", Constants.SUCCESS).
                        put("responseMessage", "Logger registered successfully.").toString() ;
            }
            else{
                return new JSONObject().
                        put("logId" , "").
                        put("status" , Constants.FAILED).
                        put("responseMessage", "Logger failed to register. Contact Admin.").toString() ;
            }
        }
        else{
            return new JSONObject().put("logId" , logger.getLogId()).
                    put("status" ,Constants.FAILED).
                    put("responseMessage", "Provided log path already registered.").toString() ;
        }
    }

    @PostMapping("/register/aggregator")
    public AggregatorResponse registerAggregator(
//            @RequestParam(value = "hostId") String hostId,
//            @RequestParam(value = "aggregatorUrl") String aggregatorUrl,
//            @RequestParam(value = "maxThreads") Integer maxThreads
            @RequestBody Map<String, Object> request
    ){
        String hostId = (String) request.get("hostId") ;
        String aggregatorUrl = (String) request.get("aggregatorUrl") ;
        Integer maxThreads = (Integer) request.get("maxThreads") ;

        Host host = hostDAL.getHostById(hostId) ;
        if( host == null){
            return new AggregatorResponse(null,
                    Constants.FAILED,
                    "Host not registered. Please register host first") ;
        }
        Aggregator aggregator = aggregatorDAL.getAggregatorByUrl(aggregatorUrl) ;
        if(aggregator != null){
            return new AggregatorResponse(aggregator.getAggregatorId(),
                    Constants.FAILED,
                    "Aggregator already registered and will be used for log ingestion.") ;
        }
        else{
            aggregator = new Aggregator(
                    registrationUtils.getId(),
                    host.getHostId(),
                    aggregatorUrl,
                    maxThreads
            ) ;

            if(aggregatorDAL.saveAggregator(aggregator)){
                return new AggregatorResponse(aggregator.getAggregatorId(), Constants.SUCCESS, "Aggregator registered.");
            }
            else {
                return new AggregatorResponse(aggregator.getAggregatorId(), Constants.FAILED, "Aggregator registration failed. Contact Admin.") ;
            }
        }
    }

    @PostMapping("/register/host")
    public HostResponse registerHost(
//            @RequestParam(value = "hostName") String hostName,
//            @RequestParam(value = "hostAddress") String hostAddress
            @RequestBody Map<String, Object> request
    ){
        String hostName = (String) request.get("hostName") ;
        String hostAddress = (String) request.get("hostAddress") ;

        Host host = hostDAL.getHostByAddress(hostAddress) ;
        if(host == null){
            host = new Host(registrationUtils.getId(),hostName, hostAddress) ;
            if(hostDAL.saveHost(host)){
                return new HostResponse(host.getHostId(), Constants.SUCCESS, "Host registered.") ;
            }
            else{
                return new HostResponse(host.getHostId(), Constants.FAILED, "Host registration failed. Contact Admin.") ;
            }
        }
        return new HostResponse(host.getHostId(), Constants.FAILED, "Host already registered.") ;
    }

    @PostMapping("/test")
    public String test(){
        RestTemplate restTemplate = new RestTemplate() ;

        Map<String, Object> requestBody = new HashMap<>() ;
        requestBody.put("logId", "test1") ;
        requestBody.put("logPath", "E:/test-logs-1") ;
        requestBody.put("logSchema", Arrays.asList("test", "test1", "test2", "test3")) ;
        requestBody.put("logFields", Arrays.asList(0,2)) ;

        return restTemplate.postForObject("http://localhost:8080/runJob", requestBody, Map.class).toString() ;
    }

}
