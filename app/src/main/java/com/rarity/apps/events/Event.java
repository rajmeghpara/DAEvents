package com.rarity.apps.events;

import java.io.Serializable;

public class Event implements Serializable{

    private String name, startTime, endTime, club, description, contact, registrationLink, venue;
    private int likes = 0;

    public Event(){}

    public Event(String name, String startTime, String endTime, String club, String description, String contact, String registrationLink, String venue) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.club = club;
        this.description = description;
        this.contact = contact;
        this.registrationLink = registrationLink;
        this.venue = venue;
    }

    public String getName() {
        return name;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getClub() {
        return club;
    }

    public String getDescription() {
        return description;
    }

    public String getContact() {
        return contact;
    }

    public String getRegistrationLink() {
        return registrationLink;
    }

    public String getVenue() {
        return venue;
    }

    public int getLikes() {
        return likes;
    }

    public void addLike(){
        likes++;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
