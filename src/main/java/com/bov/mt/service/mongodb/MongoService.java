package com.bov.mt.service.mongodb;

import com.bov.mt.exception.mongo.MongoTableException;
import com.bov.mt.utils.page.Page;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mongoService")
public class MongoService {

    @Autowired
    private MongoTemplate template;

    //获取所有的
    public List<JSONObject> queryAll(String tableName){
       return template.findAll(JSONObject.class,tableName);
    }

    //分页获取信息
    public <T> List<T> findByPage(Page page, Query query ,Class<T> clazz , String tableName){
        if (null == tableName || "".equalsIgnoreCase(tableName)) {
            throw new MongoTableException("mongo document can not be null or blank");
        }
        query.skip(page.getOffset()).limit(page.getEveryPage());
        return template.find(query,clazz,tableName);
    }


    public int count(Query query , String tableName){
        Long count = template.count(query,tableName);
        return count.intValue();
    }

    public int count(String tableName){
        return count(new Query(),tableName);
    }

}
