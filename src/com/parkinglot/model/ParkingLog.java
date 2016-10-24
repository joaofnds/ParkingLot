package com.parkinglot.model;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        return java.lang.String.format("%s,%s,%s,%s,%s",
                getOperation() ? "entrada" : "saída",
                getStatus() ? "sucesso" : "falha",
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
                getOperation() ? "Entrada" : "Saída",
                getStatus() ? "Sucesso" : "Falha",
                getCarChassis(),
                getParkingSpaceID(),
                sdf.format(getTime())
        );
    }

}
