package com.parkinglot.model;

import java.util.Date;

public class ParkingLog {
    private boolean mEntering;
    private boolean mSucceed;
    private int mCarChassis;
    private int mParkingSpaceID;
    private long mTime;

    public ParkingLog(boolean mEntering, boolean mSucceed, int mCarChassis, int mParkingSpaceID) {
        this.mEntering = mEntering;
        this.mSucceed = mSucceed;
        this.mCarChassis = mCarChassis;
        this.mParkingSpaceID = mParkingSpaceID;
        this.mTime = new Date().getTime();
    }

    public boolean getOperation() {
        return mEntering;
    }

    public boolean getStatus() {
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
        return java.lang.String.format("%s,%s,%s,%s,%s%n",
                getOperation() ? "entrada" : "saída",
                getStatus() ? "sucesso" : "falha",
                Integer.toString(getCarChassis()),
                Integer.toString(getParkingSpaceID()),
                Long.toString(getTime()) );
    }
}
