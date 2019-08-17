package com.comics.marvel.segooincmarvelapi.consume.clients.comics;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.Map;

@Component
public class ComicsImpl implements Comics{

    @Value("#{environment.MARVEL_PUBLIC}")
    private String marvelPublicKey;

    @Value("#{environment.MARVEL_PRIVATE}")
    private String marvelPrivateKey;

    @Override
    public Map downloadComics() {
        String host = "https://gateway.marvel.com";
        String ts = String.valueOf(new Date().getTime());
        String key = ts+marvelPrivateKey+marvelPublicKey;
        String hash = DigestUtils.md5DigestAsHex(key.getBytes());

        Client client = Client.create();

        ClientResponse response =
                client.resource(host).path("v1/public/comics").queryParam("ts",ts).queryParam("apikey",marvelPublicKey).queryParam("hash",hash).get(ClientResponse.class);
        String responseStr = response.getEntity(String.class);
        return new Gson().fromJson(responseStr,Map.class);
    }
}
