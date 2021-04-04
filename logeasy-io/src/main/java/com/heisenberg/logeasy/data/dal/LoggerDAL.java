package com.heisenberg.logeasy.data.dal;

import com.heisenberg.logeasy.data.entity.Host;
import com.heisenberg.logeasy.data.entity.IngestedLogs;
import com.heisenberg.logeasy.data.entity.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class LoggerDAL {
    private final MongoTemplate mongoTemplate ;

    @Autowired
    public LoggerDAL(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Stream<Log> getAll(){
        return mongoTemplate.findAll(Log.class).stream() ;
    }

    public Log getLoggerById(String id){
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where("_id").is(id)) ; // change back to _id
            Log log = this.mongoTemplate.findOne(query, Log.class) ;
            return log ;
        }
        catch(Exception e){
            e.printStackTrace();
            return null ;
        }
    }

    public Log getLoggerByPath(String path){
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where("logPath").is(path)) ;
            Log log = this.mongoTemplate.findOne(query, Log.class) ;
            return log ;
        }
        catch(Exception e){
            e.printStackTrace();
            return null ;
        }
    }

    public boolean saveLog(Log log){
        try{
            this.mongoTemplate.save(log) ;
            return true ;
        }
        catch(Exception e){
            e.printStackTrace();
            return false ;
        }
    }

    public List<String> getAllLoggers(){
        List<String> loggerIds ;
        try{
            Query query = new Query() ;
            query.fields().include("_id") ;

            loggerIds = mongoTemplate.find(query, IngestedLogs.class).
                    stream().
                    filter(ingestedLog -> ingestedLog != null).
                    map(ingestedLog -> ingestedLog.getLogPath()).
                    collect(Collectors.toList());
            return loggerIds ;
        }
        catch (Exception e){
            e.printStackTrace();
            return null ;
        }
    }
    public IngestedLogs getLoggerDataByPath(String path){
        try{
            return mongoTemplate.findById(path, IngestedLogs.class) ;
        }
        catch (Exception e){
            e.printStackTrace();
            return null ;
        }
    }
}
