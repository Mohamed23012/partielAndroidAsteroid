package com.example.partielandroidasteroid;

public class Asteroide {
    String id;
    String nom;
    double magnitude;
    double distance;

    public Asteroide(String id, String nom,
                     double magnitude, double distance) {
        this.id = id;
        this.nom = nom;
        this.magnitude = magnitude;
        this.distance = distance;
    }
}