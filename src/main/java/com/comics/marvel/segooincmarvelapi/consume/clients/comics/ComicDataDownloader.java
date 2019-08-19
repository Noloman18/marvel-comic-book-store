package com.comics.marvel.segooincmarvelapi.consume.clients.comics;

import com.comics.marvel.segooincmarvelapi.consume.clients.comics.model.ComicTitle;

public interface ComicDataDownloader {
    ComicTitle[] downloadTitleInformation(String beginRange, String endRange);
}
