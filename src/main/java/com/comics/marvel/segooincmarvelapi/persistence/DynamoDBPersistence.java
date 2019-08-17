package com.comics.marvel.segooincmarvelapi.persistence;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class DynamoDBPersistence implements Persistence{

    @Autowired
    private DynamoDB dynamoDB;

    @Override
    public void saveComicBooks(Map comics) {

        try {
            Table table = dynamoDB.getTable("marvel-comic-books");

            List<Map> results = (List<Map>) ((Map) comics.get("data")).get("results");
            System.out.println(results.get(0));
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map removeNullValues(Map map) {

        return map;
    }
}
