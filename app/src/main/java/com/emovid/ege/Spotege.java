package com.emovid.ege;

enum SpotegeType {
    POLICE,
    AMBULANCE,
    FIRE,
    SAR,
    PERSONAL
}

class Spotege {
    double latitude;
    double longitude;
    SpotegeType type;
    String name;
    String phone;

    public Spotege(double latitude, double longitude, SpotegeType type, String name, String phone) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.type = type;
        this.phone = phone;
    }

    public boolean isType(SpotegeType type) {
        return this.type == type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public SpotegeType getType() {
        return type;
    }

    public void setType(SpotegeType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
