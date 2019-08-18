package com.comics.marvel.segooincmarvelapi.persistence;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DynamoDBPersistence implements Persistence{

    @Autowired
    private DynamoDB dynamoDB;

    @Override
    public void saveComicBooks(Map comics) {

        try {
            Table table = dynamoDB.getTable("marvel-comic-books");

            List<Map> results = (List<Map>) ((Map) comics.get("data")).get("results");

            if (results!=null) {
                for (Map result:results) {
                    result = removeNullValues(result);
                    Item item = new Item();

                    for (Object key:result.keySet()) {
                        item.with((String) key,result.get(key));
                   }

                    table.putItem(item);
                    System.out.printf("Successfully saved %s\n",result.get("title"));
                }
            }
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map removeNullValues(Map map) {
        List<String> list = (List<String>) map.keySet().stream().map(key->key.toString()).collect(Collectors.toList());
        for (String key:list) {
            Object item = map.get(key);
            if (item==null) {
                map.remove(key);
                continue;
            }
            if (item instanceof String && StringUtils.isEmpty((String)item)) {
                map.remove(key);
                continue;
            }
            if (item instanceof Map) {
                removeNullValues((Map) item);
            }
        }

        return map;
    }
}
