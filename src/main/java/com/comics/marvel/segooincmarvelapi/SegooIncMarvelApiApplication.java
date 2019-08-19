package com.comics.marvel.segooincmarvelapi;

import com.comics.marvel.segooincmarvelapi.consume.clients.comics.ComicDataDownloader;
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
    private ComicDataDownloader comics;

    public static void main(String[] args) {
        SpringApplication.run(SegooIncMarvelApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        comics.downloadTitleInformation("2019-08-10","2019-08-19");
        //persistence.saveComicBooks(comics.downloadComics());
    }
}
