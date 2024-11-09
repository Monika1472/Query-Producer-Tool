package com.Springpro.Devtools.Service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoDBService {
    @Getter
    private String customMongoConnectionString;
    @Getter
    private String customMongoDBName;

    public void setCustomMongoConnectionString(String customMongoConnectionString){
        this.customMongoConnectionString=customMongoConnectionString;
    }
    public void setCustomMongoDBName(String customMongoDBName){
        this.customMongoDBName=customMongoDBName;
    }
}
