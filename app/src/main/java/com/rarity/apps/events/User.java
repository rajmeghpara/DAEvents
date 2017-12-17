package com.rarity.apps.events;

public class User {

    private String id, name, phone, batch, admin="";

    public User(){}

    public User(String name, String id, String phone, String batch){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.batch = batch;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getBatch() {
        return batch;
    }

    public String isAdmin() {
        return admin;
    }
}
