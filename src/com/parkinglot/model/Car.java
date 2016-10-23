package com.parkinglot.model;

public class Car {
    private String mModel;
    private int mChassis;
    private double mWeight;
    private double mHeight;
    private double mLength;
    private double mWidth;

    public Car(String model, int chassis, double weight, double height, double length, double width) {
        this.mModel = model;
        this.mChassis = chassis;
        this.mWeight = weight;
        this.mHeight = height;
        this.mLength = length;
        this.mWidth = width;
    }

    public String getModel() {
        return mModel;
    }

    public int getChassis() {
        return mChassis;
    }

    public double getWeight() {
        return mWeight;
    }

    public double getHeight() {
        return mHeight;
    }

    public double getLength() {
        return mLength;
    }

    public double getWidth() {
        return mWidth;
    }

    @Override
    public String toString() {
        return this.getModel() + "," +
                Integer.toString(this.getChassis()) + "," +
                Double.toString(this.getWeight()) + "," +
                Double.toString(this.getHeight()) + "," +
                Double.toString(this.getLength()) + "," +
                Double.toString(this.getWidth());
    }
}
