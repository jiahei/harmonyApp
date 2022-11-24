package com.example.myharmonyapplication_api4.beans;

public class Item {
    private int uid;
    private String sportType;
    private String time;
    private String location;
    private String level;
    private int numNeeded;

    public Item() {
    }

    public Item(int uid, String sportType, String time, String location, String level, int numNeeded) {
        this.uid = uid;
        this.sportType = sportType;
        this.time = time;
        this.location = location;
        this.level = level;
        this.numNeeded = numNeeded;
    }

    @Override
    public String toString() {
        return "Item{" +
                "uid=" + uid +
                ", sportType='" + sportType + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", level='" + level + '\'' +
                ", numNeeded='" + numNeeded + '\'' +
                '}';
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getNumNeeded() {
        return numNeeded;
    }

    public void setNumNeeded(int numNeeded) {
        this.numNeeded = numNeeded;
    }
}
