package com.heisenberg.logeasy.backend.dal;

import com.heisenberg.logeasy.backend.model.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class HostDAL {

    private final MongoTemplate mongoTemplate ;

    @Autowired
    public HostDAL(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Host getHostById(String hostId){
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where("_id").is(hostId)) ;
            Host host = this.mongoTemplate.findOne(query, Host.class) ;
            return host ;
        }
        catch(Exception e){
            e.printStackTrace();
            return null ;
        }
    }

    public Host getHostByAddress(String hostAddress){
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where("hostAddress").is(hostAddress)) ;
            Host host = this.mongoTemplate.findOne(query, Host.class) ;
            return host ;
        }
        catch (Exception e){
            e.printStackTrace();
            return null ;
        }
    }

    public boolean saveHost(Host host){
        try{
            this.mongoTemplate.save(host) ;
            return true ;
        }
        catch(Exception e){
            e.printStackTrace();
            return false ;
        }
    }


}
