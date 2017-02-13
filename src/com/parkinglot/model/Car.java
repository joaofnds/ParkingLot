package com.parkinglot.model;

public class Car implements Comparable<Car>{
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

	public String pretty() {
		return  java.lang.String.format("Nome: %s%n" +
											"\tNúmero de chassi: %d%n" +
											"\tPeso: %.1f%n" +
											"\tAltura: %.1f%n" +
											"\tComprimento: %.1f%n" +
											"\tlargura: %.1f%n",
											getModel(),
											getChassis(),
											getWeight(),
											getHeight(),
											getLength(),
											getWidth());
	}

	// Ordenar por ordem decrescente de peso, altura, comprimento e largura
	@Override
	public int compareTo(Car o) {
		if (o == null) {
			return 0;
		}

		if (this.getWeight() == o.getWeight()) {
			if (this.getHeight() == o.getHeight()) {
				if (this.getLength() == o.getLength()) {
					if (this.getWidth() == o.getWidth()) {
						return 0;
					} else {
						return this.getWidth() > o.getWidth() ? -1 : 1;
					}
				} else {
					return this.getLength() > o.getLength() ? -1 : 1;
				}
			} else {
				return this.getHeight() > o.getHeight() ? -1 : 1;
			}
		} else {
			return this.getWeight() > o.getWeight() ? -1 : 1;
		}
	}
}
