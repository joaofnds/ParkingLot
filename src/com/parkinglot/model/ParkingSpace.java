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

	@Override
	public String toString() {
		return  Integer.toString(this.getId()) + "," +
				Double.toString(this.getWeight()) + "," +
				Double.toString(this.getHeight()) + "," +
				Double.toString(this.getLength()) + "," +
				Double.toString(this.getWidth());
	}

	public String pretty() {
		return java.lang.String.format("Vaga ID: %s%n" +
						"\tPeso: %.1f%n" +
						"\tAltura: %.1f%n" +
						"\tComprimento: %.1f%n" +
						"\tLargura: %.1f%n",
							getId(),
							getWeight(),
							getHeight(),
							getLength(),
							getWidth());
	}
}
