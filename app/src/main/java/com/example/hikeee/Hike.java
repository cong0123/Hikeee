package com.example.hikeee;

import java.io.Serializable;
import java.util.List;

public class Hike implements Serializable {
    private long id;

    private String name;
    private String location;
    private String date;
    private String length;
    private String difficulty;
    private String description;
    private String parkingArea;
    private String rating;
    private String restingSpots;
    private List<Observation> observations;
    public Hike(String name, String location, String date, String length, String difficulty, String description, String parkingArea, String rating, String restingSpots) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
        this.parkingArea = parkingArea;
        this.rating = rating;
        this.restingSpots = restingSpots;
    }

    public List<Observation> getObservations() {
        return observations;
    }
    public void addObservation(Observation observation) {
        observations.add(observation);
    }
    // Getters and setters for other attributes...
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    //
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    //
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
    //

    public String getDifficulty(){
        return  difficulty;
    }
    public  void  setDifficulty(String difficulty){
        this.difficulty = difficulty;
    }
    //
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    //... (other getters and setters)
    public String getParkingArea() {
        return parkingArea;
    }

    public void setParkingArea(String parkingArea) {
        this.parkingArea = parkingArea;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRestingSpots() {
        return restingSpots;
    }

    public void setRestingSpots(String restingSpots) {
        this.restingSpots = restingSpots;
    }

    // Implement Serializable
    private static final long serialVersionUID = 1L;

    public String getFieldValue(int index) {
        switch (index) {
            case 0: return getName();
            case 1: return getLocation();
            case 2: return getDate();
            case 3: return getLength();
            case 4: return getDifficulty();
            case 5: return getDescription();
            case 6: return getParkingArea();
            case 7: return getRating();
            case 8: return getRestingSpots();
            default: return null;
        }
    }

    public void setFieldValue(int index, String value) {
        switch (index) {
            case 0: setName(value); break;
            case 1: setLocation(value); break;
            case 2: setDate(value); break;
            case 3: setLength(value); break;
            case 4: setDifficulty(value); break;
            case 5: setDescription(value); break;
            case 6: setParkingArea(value); break;
            case 7: setRating(value); break;
            case 8: setRestingSpots(value); break;
        }
    }
}

