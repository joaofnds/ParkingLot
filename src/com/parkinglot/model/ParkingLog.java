package com.parkinglot.model;

import java.text.SimpleDateFormat;

public class ParkingLog {
	private boolean mEntering;
	private boolean mSucceed;
	private int mCarChassis;
	private int mParkingSpaceID;
	private long mTime;

	public ParkingLog(boolean Entering, boolean Succeed, int CarChassis, int ParkingSpaceID, long time) {
		this.mEntering = Entering;
		this.mSucceed = Succeed;
		this.mCarChassis = CarChassis;
		this.mParkingSpaceID = ParkingSpaceID;
		this.mTime = time;
	}

	public boolean isEntering() {
		return mEntering;
	}

	public boolean succeed() {
		return mSucceed;
	}

	public int getCarChassis() {
		return mCarChassis;
	}

	public int getParkingSpaceID() {
		return mParkingSpaceID;
	}

	public long getTime() {
		return mTime;
	}

	@Override
	public String toString() {
		return java.lang.String.format("%s,%s,%s,%s,%s",
				isEntering() ? "entrada" : "saída",
				succeed() ? "sucesso" : "falha",
				Integer.toString(getCarChassis()),
				Integer.toString(getParkingSpaceID()),
				Long.toString(getTime()) );
	}

	public String pretty() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return java.lang.String.format("Operação: %s" +
				"\tResultado: %s%n" +
				"\tNúmero de chassi: %d%n" +
				"\tID da vaga: %d%n" +
				"\tHorário: %s%n",
				isEntering() ? "Entrada" : "Saída",
				succeed() ? "Sucesso" : "Falha",
				getCarChassis(),
				getParkingSpaceID(),
				sdf.format(getTime())
		);
	}

}
