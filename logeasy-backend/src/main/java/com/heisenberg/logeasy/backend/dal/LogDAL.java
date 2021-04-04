package com.heisenberg.logeasy.backend.dal;

import com.heisenberg.logeasy.backend.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogDAL {
    private final MongoTemplate mongoTemplate ;

    @Autowired
    public LogDAL(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Log getLogger(String columnName, String columnValue){
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where(columnName).is(columnValue)) ;
            Log logger = mongoTemplate.findOne(query, Log.class) ;
            return logger ;
        }
        catch(Exception e){
            e.printStackTrace();
            return null ;
        }
    }


    public boolean save(Log logger) {
        try{
            this.mongoTemplate.save(logger) ;
            return true ;
        }
        catch (Exception e){
            e.printStackTrace();
            return false ;
        }
    }

    public List<Log> getAll(){
        return mongoTemplate.findAll(Log.class) ;
    }
}
