package com.parkinglot;

import com.parkinglot.model.ParkingSpace;
import com.parkinglot.model.Car;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private List<ParkingSpace> parkingSpaces;
    private List<Car> cars;

    public ParkingLot() {
        this.cars = new ArrayList<Car>();
        this.parkingSpaces = new ArrayList<ParkingSpace>();
    }

    public void addCar(Car car) { this.cars.add(car); }

    public void addParkingSpace(ParkingSpace parkingSpace) { this.parkingSpaces.add(parkingSpace); }

    public void importCarsFrom(String filename) {
        try (
                FileInputStream fis = new FileInputStream(filename);
                BufferedReader reader = new BufferedReader( new InputStreamReader(fis) );
        ) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                String[] args = line.split(",");
                this.addCar(
                        new Car(
                                args[0],
                                Integer.parseInt(args[1]),
                                Double.parseDouble(args[2]),
                                Double.parseDouble(args[3]),
                                Double.parseDouble(args[4]),
                                Double.parseDouble(args[5])
                        )
                );
            }
        } catch (IOException ioe) {
            System.out.printf("Problems loading %s %n", filename);
            ioe.printStackTrace();
        }
    }

    public void exportCarsTo(String filename) {
        try (
                FileOutputStream fos = new FileOutputStream(filename);
                PrintWriter writer = new PrintWriter(fos);
        ) {
            for (Car car : this.cars) {
                writer.printf("%s,%d,%f,%f,%f,%f%n",
                        car.getModel(),
                        car.getChassis(),
                        car.getWeight(),
                        car.getHeight(),
                        car.getLength(),
                        car.getWidth()
                );
            }
        } catch (IOException ioe) {
            System.out.printf("Problem saving %s%n", filename);
            ioe.printStackTrace();
        }
    }

    public void importParkingSpacesFrom(String filename) {
        try (
                FileInputStream fis = new FileInputStream(filename);
                BufferedReader reader = new BufferedReader( new InputStreamReader(fis) );
        ) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                String[] args = line.split(",");
                this.addParkingSpace(
                        new ParkingSpace(
                                Integer.parseInt(args[0]),
                                Double.parseDouble(args[1]),
                                Double.parseDouble(args[2]),
                                Double.parseDouble(args[3]),
                                Double.parseDouble(args[4])
                        )
                );
            }
        } catch (IOException ioe) {
            System.out.printf("Problems loading %s %n", filename);
            ioe.printStackTrace();
        }
    }

    public void exportParkingSpacesTo(String filename) {
    	try (
    		FileOutputStream fos = new FileOutputStream(filename);
    		PrintWriter write = new PrintWriter(fos);
    	) {
    		for (ParkingSpace ps : this.parkingSpaces) {
    			write.printf("%d,%f,%f,%f,%f%n",
    				ps.getId(),
                    ps.getWeight(),
                    ps.getHeight(),
                    ps.getLength(),
                    ps.getWidth()
                );
    		}
    	} catch (IOException ioe) {
            System.out.printf("Problems loading %s %n", filename);
            ioe.printStackTrace();;
        }
    }
}
