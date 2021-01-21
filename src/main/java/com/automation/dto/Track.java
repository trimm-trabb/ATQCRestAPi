package com.automation.dto;

public class Track {

    private String uri;
    private int[] position;

    public Track(String uri, int[] position) {
        this.uri = uri;
        this.position = position;
    }
}