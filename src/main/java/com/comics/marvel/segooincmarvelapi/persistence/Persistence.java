package com.comics.marvel.segooincmarvelapi.persistence;

import com.comics.marvel.segooincmarvelapi.consume.clients.comics.model.ComicTitle;

public interface Persistence {
    void saveComicBooks(ComicTitle[] comics);
}
