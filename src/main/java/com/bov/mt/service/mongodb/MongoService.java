package com.bov.mt.service.mongodb;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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

}
