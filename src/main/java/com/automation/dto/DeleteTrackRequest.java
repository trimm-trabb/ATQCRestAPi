package com.automation.dto;

public class DeleteTrackRequest {

    private Track[] tracks;

    public DeleteTrackRequest(Track[] tracks) {
        this.tracks = tracks;
    }
}