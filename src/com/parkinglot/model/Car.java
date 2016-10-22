package com.parkinglot.model;

public class Car {
    private String model;
    private int chassis;
    private double weight;
    private double height;
    private double length;
    private double width;

    public Car(String model, int chassis, double weight, double height, double length, double width) {
        this.model = model;
        this.chassis = chassis;
        this.weight = weight;
        this.height = height;
        this.length = length;
        this.width = width;
    }

    public String getModel() {
        return model;
    }

    public int getChassis() {
        return chassis;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }
}
