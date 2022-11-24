package com.example.myspring.domain;

import lombok.Data;

@Data
public class PostInfo {
    private int uid;
    private String sportType;
    private String time;
    private String location;
    private String level;
    private int numNeeded;

    public PostInfo(int uid, String sportType, String time, String location, String level, int numNeeded) {
        this.uid = uid;
        this.sportType = sportType;
        this.time = time;
        this.location = location;
        this.level = level;
        this.numNeeded = numNeeded;
    }
}
