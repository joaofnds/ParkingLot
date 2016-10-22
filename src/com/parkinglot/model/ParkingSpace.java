package com.parkinglot.model;

public class ParkingSpace {
    private int id;
    private double weight;
    private double height;
    private double length;
    private double width;
    private boolean occupied;
    Car parkedCar;

    public ParkingSpace(int id, double weight, double height, double length, double width) {
        this.id = id;
        this.weight = weight;
        this.height = height;
        this.length = length;
        this.width = width;
    }

    public int getId() {
        return id;
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

    public boolean isOccupied() {
        return occupied;
    }

    public Car getParkedCar() {
        return parkedCar;
    }

    public void setParkedCar(Car parkedCar) {
        this.parkedCar = parkedCar;
    }
}
