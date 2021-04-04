package com.heisenberg.logeasy.data.dal;

import com.heisenberg.logeasy.data.entity.Aggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public class AggregatorDAL {
    private final MongoTemplate mongoTemplate ;

    @Autowired
    public AggregatorDAL(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Stream<Aggregator> getAll(){
        return mongoTemplate.findAll(Aggregator.class).stream() ;
    }
    public Aggregator getAggregatorById(String id){
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where("_id").is(id)) ;
            Aggregator aggregator = mongoTemplate.findOne(query, Aggregator.class) ;
            return aggregator ;
        }
        catch(Exception e){
            e.printStackTrace();
            return null ;
        }
    }

    public Aggregator getAggregatorByUrl(String url){
        try{
            Query query = new Query() ;
            query.addCriteria(Criteria.where("aggregatorUrl").is(url)) ;
            Aggregator aggregator = mongoTemplate.findOne(query, Aggregator.class) ;
            return aggregator ;
        }
        catch(Exception e){
            e.printStackTrace();
            return null ;
        }
    }

    public boolean saveAggregator(Aggregator aggregator){
        try{
            this.mongoTemplate.save(aggregator) ;
            return true ;
        }
        catch(Exception e){
            e.printStackTrace();
            return false ;
        }
    }
}
