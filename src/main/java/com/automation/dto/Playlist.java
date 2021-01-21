package com.automation.dto;

import com.google.gson.annotations.SerializedName;

public class Playlist {

    private String name;
    @SerializedName("public")
    private boolean isPublic;

    public Playlist(String name) {
        this.name = name;
        this.isPublic = true;
    }

    public String getName() {
        return name;
    }
}