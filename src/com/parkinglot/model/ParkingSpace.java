package com.parkinglot.model;

public class ParkingSpace {
    private int mId;
    private double mWeight;
    private double mHeight;
    private double mLength;
    private double mWidth;

    public ParkingSpace(int id, double weight, double height, double length, double width) {
        this.mId = id;
        this.mWeight = weight;
        this.mHeight = height;
        this.mLength = length;
        this.mWidth = width;
    }

    public int getId() {
        return mId;
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

}
