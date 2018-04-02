package com.bov.mt.service.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service("mongoService")
public class MongoService {

    @Autowired
    private MongoTemplate template;


}
