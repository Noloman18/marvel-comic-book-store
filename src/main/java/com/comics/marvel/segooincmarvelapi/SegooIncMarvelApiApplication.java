package com.comics.marvel.segooincmarvelapi;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.comics.marvel.segooincmarvelapi.consume.clients.comics.Comics;
import com.comics.marvel.segooincmarvelapi.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SegooIncMarvelApiApplication implements CommandLineRunner {

    @Autowired
    private Persistence persistence;

    @Autowired
    private Comics comics;

    public static void main(String[] args) {
        SpringApplication.run(SegooIncMarvelApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        comics.downloadComics();
        //persistence.saveComicBooks(comics.downloadComics());
    }
}
