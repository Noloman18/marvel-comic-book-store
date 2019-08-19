package com.comics.marvel.segooincmarvelapi.consume.clients.comics.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class ComicTitle extends LinkedHashMap {

    public ComicTitle() {
    }

    public ComicTitle(Map map) {
        for (Object key : map.keySet()) {
            this.put(key, map.get(key));
        }
    }

    public String getImagePath() {
        Map thumbNail = (Map) this.get("thumbnail");
        if (thumbNail != null && thumbNail.containsKey("path")) {
            return String.format("%s/portrait_uncanny.jpg", thumbNail.get("path"));
        }

        return null;
    }
}
