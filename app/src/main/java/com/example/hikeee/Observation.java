package com.example.hikeee;
import java.io.Serializable;
public class Observation implements Serializable {
    private long id;
    private long hikeId;
    private String Content;
    private String time;
    private String weather;
    private String trailConditions;

    public Observation(long id, long hikeId, String Content, String time, String weather, String trailConditions) {
        this.id = id;
        this.hikeId = hikeId;
        this.Content = Content;
        this.time = time;
        this.weather = weather;
        this.trailConditions = trailConditions;
    }

    //getter and setter
    public long getId() {
        return id;
    }

    public long getHikeId() {
        return hikeId;
    }



    public String getTime() {
        return time;
    }

    public String getWeather() {
        return weather;
    }

    public String getTrailCondition() {
        return trailConditions;
    }


    public String getContent() {
        return Content; }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTrailCondition(String trailCondition) {
        this.trailConditions = trailCondition;
    }
}