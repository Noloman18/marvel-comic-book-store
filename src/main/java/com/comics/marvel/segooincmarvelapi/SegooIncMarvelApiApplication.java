package com.comics.marvel.segooincmarvelapi;

import com.comics.marvel.segooincmarvelapi.consume.clients.comics.MarvelDataRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@SpringBootApplication
@EnableScheduling
public class SegooIncMarvelApiApplication {

    @Autowired
    private MarvelDataRetriever marvelDataRetriever;

    @Scheduled(fixedDelay = 1000*60*60L)
    public void scheduledJob() {
        marvelDataRetriever.retrieveData();
    }

    public static void main(String[] args) {
        SpringApplication.run(SegooIncMarvelApiApplication.class, args);
    }
}
