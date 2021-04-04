package com.heisenberg.logeasy.aggregator.dal;

import com.heisenberg.logeasy.aggregator.model.*;
import com.heisenberg.logeasy.aggregator.utils.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AggregatorDAL {
    private MongoTemplate mongoTemplate ;

    @Autowired
    public AggregatorDAL(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public AggregatorStatus getStatus(String aggregatorId){
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where("_id").is(aggregatorId)) ;

            if( mongoTemplate.exists(query, AggregatorThreads.class)){
                AggregatorThreads aggregatorThreads = mongoTemplate.findOne(query, AggregatorThreads.class) ;

                return new AggregatorStatus(aggregatorThreads.getCurrentThreads()) ;
            }
            else{
                initiateAggregatorStatus(aggregatorId, 0) ;
                return new AggregatorStatus(0) ;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null ;
        }
    }

    public boolean incrementThreads(String aggregatorId){
        try{
            Query findAggregatorQuery = new Query() ;
            findAggregatorQuery.addCriteria(Criteria.where("_id").is(aggregatorId)) ;

            if( mongoTemplate.exists(findAggregatorQuery, AggregatorThreads.class)) {
                AggregatorThreads aggregatorThreads = mongoTemplate.findOne(findAggregatorQuery, AggregatorThreads.class) ;
                aggregatorThreads.setCurrentThreads(aggregatorThreads.getCurrentThreads()+1);
                mongoTemplate.save(aggregatorThreads) ;

            }
            else{
                initiateAggregatorStatus(aggregatorId, 1) ;
            }
            return true ;
        }
        catch (Exception e){
            e.printStackTrace();
            return false ;
        }
    }

    public boolean decrementThreads(String aggregatorId){
        try{
            Query findAggregatorQuery = new Query() ;
            findAggregatorQuery.addCriteria(Criteria.where("_id").is(aggregatorId)) ;

            AggregatorThreads aggregatorThreads = mongoTemplate.findOne(findAggregatorQuery, AggregatorThreads.class) ;
            aggregatorThreads.setCurrentThreads(aggregatorThreads.getCurrentThreads()-1);
            mongoTemplate.save(aggregatorThreads) ;
            return true ;
        }
        catch (Exception e){
            e.printStackTrace();
            return false ;
        }
    }

    public boolean initiateAggregatorStatus(String aggregatorId, int numThreads){
        try{
            this.mongoTemplate.save(new AggregatorThreads(aggregatorId, numThreads)) ;
            return true ;
        } catch (Exception e){
            e.printStackTrace();
            return false ;
        }
    }

    public boolean setJobStatus(String jobId){
        try{
            this.mongoTemplate.save(new JobStatus(jobId, Constants.JOB_STARTED)) ;
            return true ;
        }
        catch (Exception e){
            e.printStackTrace();
            return false ;
        }
    }

    public boolean setJobStatus(String jobId, String status){
        try {
            this.mongoTemplate.save(new JobStatus(jobId, status)) ;
            return true ;
        }
        catch (Exception e){
            e.printStackTrace();
            return false ;
        }
    }

    public long getLastIngestedTime(String logPath){
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where("_id").is(logPath)) ;

            if(mongoTemplate.exists(query, LogPathStatus.class)){
                LogPathStatus status = mongoTemplate.findOne(query, LogPathStatus.class) ;
                return status.getLastIngestedTime() ;
            }
            else{
                return 0 ;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return -1 ;
        }

    }

    public boolean setLastIngestedTime(String logPath, long time){
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where("_id").is(logPath)) ;

            if(mongoTemplate.exists(query, LogPathStatus.class)){
                LogPathStatus status = mongoTemplate.findOne(query, LogPathStatus.class) ;
                status.setLastIngestedTime(time);
                mongoTemplate.save(status) ;
            }
            else{
                mongoTemplate.save(new LogPathStatus(logPath, time, null)) ;
            }
            return true ;
        }
        catch (Exception e){
            e.printStackTrace();
            return false ;
        }
    }

    public HashMap<String, Integer> getDirectoryListing(String logPath){
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where("_id").is(logPath)) ;

            if(mongoTemplate.exists(query, LogPathStatus.class)){
                LogPathStatus status = mongoTemplate.findOne(query, LogPathStatus.class) ;
                return status.getDirectoryListing() ;
            }
            else {
                return null ;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null ;
        }
    }

    public boolean setDirectoryListing(String logPath, HashMap<String, Integer> directoryListing){
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where("_id").is(logPath)) ;

            if(mongoTemplate.exists(query, LogPathStatus.class)){
                //System.out.println("Found logPathStatus");
                LogPathStatus status = mongoTemplate.findOne(query, LogPathStatus.class) ;
                status.setDirectoryListing(directoryListing);
                mongoTemplate.save(status) ;
                return true;
            }
            else {
                mongoTemplate.save(new LogPathStatus(logPath, 0, directoryListing)) ;
                return true ;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false ;
        }
    }

    public boolean saveLogItem(String logPath, Map<String, String> logItemJson) {
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where("_id").is(logPath)) ;
            LogPathItems items ;
            if(mongoTemplate.exists(query, LogPathItems.class)){
                //System.out.println("LogPathItems found in DB");
                items = mongoTemplate.findOne(query, LogPathItems.class) ;
                //System.out.println(items.getLogsList().toString());
                items.updateItems(logItemJson);
                //System.out.println(items.getLogsList().toString());
            }
            else{
                //System.out.println("LogPathItems not found in DB");
                items = new LogPathItems(logPath, Arrays.asList(logItemJson)) ;
            }
            mongoTemplate.save(items) ;
            return true ;
        }
        catch (Exception e){
            e.printStackTrace();
            return false ;
        }
    }
}
