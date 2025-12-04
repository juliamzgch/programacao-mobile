package com.example.faunaeflora.model;

public class Flower {
    private int photo;
    private String name;
    private String seedingMonth;
    private String seedingWarnings;

    public Flower(int photo, String name, String seedingMonth, String seedingWarnings) {
        setPhoto(photo);
        setName(name);
        setSeedingMonth(seedingMonth);
        setSeedingWarnings(seedingWarnings);
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeedingMonth() {
        return seedingMonth;
    }

    public void setSeedingMonth(String seedingMonth) {
        this.seedingMonth = seedingMonth;
    }

    public String getSeedingWarnings() {
        return seedingWarnings;
    }

    public void setSeedingWarnings(String seedingWarnings) {
        this.seedingWarnings = seedingWarnings;
    }
}
