package com.emovid.ege;

enum SpotEgeType {
    POLICE,
    AMBULANCE,
    FIRE,
    SAR,
    PERSONAL
}

class SpotEge {
    double latitude;
    double longitude;
    SpotEgeType type;
    String name;
    String phone;

    public SpotEge(double latitude, double longitude, SpotEgeType type, String name, String phone) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.type = type;
        this.phone = phone;
    }

    public boolean isType(SpotEgeType type) {
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

    public SpotEgeType getType() {
        return type;
    }

    public void setType(SpotEgeType type) {
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
