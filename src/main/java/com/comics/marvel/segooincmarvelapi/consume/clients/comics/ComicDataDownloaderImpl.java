package com.comics.marvel.segooincmarvelapi.consume.clients.comics;

import com.comics.marvel.segooincmarvelapi.consume.clients.comics.model.ComicTitle;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ComicDataDownloaderImpl implements ComicDataDownloader {

    @Value("#{environment.MARVEL_PUBLIC}")
    private String marvelPublicKey;

    @Value("#{environment.MARVEL_PRIVATE}")
    private String marvelPrivateKey;

    @Override
    public ComicTitle[] downloadTitleInformation(String beginRange, String endRange) {
        List<Map> result = new ArrayList<>();
        int offset = 0;
        do {
            String host = "https://gateway.marvel.com";
            String[] timestampHash = generateTimestampAndHash();
            Client client = Client.create();

            ClientResponse response =
                    client.resource(host)
                            .path("v1/public/comics")
                            .queryParam("dateRange", String.format("%s,%s", beginRange, endRange))
                            .queryParam("ts", timestampHash[0])
                            .queryParam("apikey", marvelPublicKey)
                            .queryParam("offset", String.valueOf(offset))
                            .queryParam("hash", timestampHash[1])
                            .get(ClientResponse.class);

            String responseStr = response.getEntity(String.class);
            Map map = new Gson().fromJson(responseStr, Map.class);

            Map dataMap = ((Map) map.get("data"));

            Double count = (Double) dataMap.get("count");

            if (count == null)
                break;

            List<Map> list = (List<Map>) dataMap.get("results");

            result.addAll(list);

            System.out.printf("Downloaded comic titles from between %d and %d\r\n", offset, (int) (offset + count));

            offset += 20;
            if (count < 20)
                break;
        } while (true);

        System.out.printf("Finished downloading comic titles for range [%s,%s]\n", beginRange, endRange);

        return result.stream().map(item -> new ComicTitle(item)).collect(Collectors.toList()).toArray(new ComicTitle[0]);
    }

    private String[] generateTimestampAndHash() {
        String ts = String.valueOf(new Date().getTime());
        String key = ts + marvelPrivateKey + marvelPublicKey;
        return new String[]{ts, DigestUtils.md5DigestAsHex(key.getBytes())};
    }
}
