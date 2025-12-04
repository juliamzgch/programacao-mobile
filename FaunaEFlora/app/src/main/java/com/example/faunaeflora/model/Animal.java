package com.example.faunaeflora.model;

public class Animal {
    private int photo;
    private String name;
    private String habitat;

    public Animal(int photo, String name, String habitat) {
        setPhoto(photo);
        setName(name);
        setHabitat(habitat);
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

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }
}


