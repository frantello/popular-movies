package com.example.android.popularmovies.model;

/**
 * Video.
 */

public class Video {

    public static final String RESULTS = "results";

    public static final String SITE_YOUTUBE = "YouTube";

    public static final String TYPE_TRAILER = "Trailer";

    public static final String ID = "id";

    public static final String KEY = "key";

    public static final String NAME = "name";

    public static final String SITE = "site";

    public static final String SIZE = "size";

    public static final String TYPE = "type";

    private String id;

    private String key;

    private String name;

    private String site;

    private int size;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
