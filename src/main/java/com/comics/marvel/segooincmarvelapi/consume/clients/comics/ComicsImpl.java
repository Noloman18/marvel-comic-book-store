package com.comics.marvel.segooincmarvelapi.consume.clients.comics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class ComicsImpl implements Comics{

    @Value("#{environment.MARVEL_PUBLIC}")
    private String marvelPublicKey;

    @Value("#{environment.MARVEL_PRIVATE}")
    private String marvelPrivateKey;

    @Override
    public Map[] downloadComics() {
        String host = "https://gateway.marvel.com";
        String[] timestampHash = generateTimestampAndHash();
        Client client = Client.create();
        ClientResponse response =
                client.resource(host).path("v1/public/comics").queryParam("dateRange","2019-07-01,2019-08-19").queryParam("ts",timestampHash[0]).queryParam("apikey",marvelPublicKey).queryParam("hash",timestampHash[1]).get(ClientResponse.class);
        String responseStr = response.getEntity(String.class);
        Map map = new Gson().fromJson(responseStr,Map.class);

        List<Map> list = (List<Map>) ((Map) map.get("data")).get("results");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        System.out.println(gson.toJson(list.get(0)));

        return list.toArray(new Map[0]);
    }

    private String[] generateTimestampAndHash() {
        String ts = String.valueOf(new Date().getTime());
        String key = ts+marvelPrivateKey+marvelPublicKey;
        return new String[]{ts,DigestUtils.md5DigestAsHex(key.getBytes())};
    }
}
