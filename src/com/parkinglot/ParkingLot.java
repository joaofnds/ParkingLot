package com.parkinglot;

import com.parkinglot.model.ParkingSpace;
import com.parkinglot.model.Car;

import java.io.*;
import java.util.*;

public class ParkingLot {
    private List<ParkingSpace> mParkingSpaces;
    private List<Car> mCars;
    private Map<String, String> mMenu; // Option, Description
    private Map<Integer, Integer> mParkingRelations; // Chassis, Parking space ID
    private BufferedReader mReader;

    public ParkingLot() {
        this.mCars = new ArrayList<Car>();
        this.mParkingSpaces = new ArrayList<ParkingSpace>();
        this.mMenu = new LinkedHashMap<String, String>();
        this.mParkingRelations = new HashMap<Integer, Integer>();
        mMenu.put("Abrir", "Iniciar ou continuar uma simulção");
        mMenu.put("Entrar", "Aloca uma vaga no estacionamento");
        mMenu.put("Pesquisar", "Encontra a vaga mais adequada ao veículo");
        mMenu.put("Sair", "Desaloca vaga do veículo");
        mMenu.put("Salvar", "Salva o estado atual do estacionamento");
        mMenu.put("Relatórios", "Gera relatórios sobre o estacionamento");
        mMenu.put("Fim", "Encerra a aplicação");
        this.mReader = new BufferedReader(new InputStreamReader(System.in));
    }

    private void addCar(Car car) { this.mCars.add(car); }

    private void addParkingSpace(ParkingSpace parkingSpace) { this.mParkingSpaces.add(parkingSpace); }

    private void addParkingRelation(int chassis, int parkingSpaceID) {
        this.mParkingRelations.put(chassis, parkingSpaceID);
    }

    private void importCarsFrom(String filename) {
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

    private void exportCarsTo(String filename) {
        try (
                FileOutputStream fos = new FileOutputStream(filename);
                PrintWriter writer = new PrintWriter(fos);
        ) {
            for (Car car : this.mCars) {
                writer.println(car);
            }
        } catch (IOException ioe) {
            System.out.printf("Problem saving %s%n", filename);
            ioe.printStackTrace();
        }
    }

    private void importParkingSpacesFrom(String filename) {
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

    private void exportParkingSpacesTo(String filename) {
    	try (
    		FileOutputStream fos = new FileOutputStream(filename);
    		PrintWriter writer = new PrintWriter(fos);
    	) {
    		for (ParkingSpace ps : this.mParkingSpaces) {
    			writer.println(ps);
    		}
    	} catch (IOException ioe) {
            System.out.printf("Problems loading %s %n", filename);
            ioe.printStackTrace();;
        }
    }

    private String promptAction() throws IOException {
        System.out.println("Bem vindo ao estacionamento. Estas são as ações que você pode tomar: ");
        for (Map.Entry<String, String> option : mMenu.entrySet()) {
            System.out.printf("%s - %s%n",
                    option.getKey(),
                    option.getValue());
        }
        System.out.print("O que você deseja fazer: ");
        String choice = mReader.readLine();
        return choice.trim().toLowerCase();
    }

    private ParkingSpace findParkingSpaceByID(int id) {
        for ( ParkingSpace ps : this.mParkingSpaces ) {
            if ( ps.getId() == id ) return ps;
        }
        return null;
    }

    private Car findCarByChassis(int chassis) {
        for ( Car car : this.mCars ) {
            if ( car.getChassis() == chassis )  return car;
        }
        return null;
    }

    private boolean isParkable(int chassis, int parkingSpaceID) {
        Car car = findCarByChassis(chassis);
        ParkingSpace parkingSpace = findParkingSpaceByID(parkingSpaceID);

        if (car.equals(null)) {
            System.out.printf("Não foi possível encontrar um carro com o número de chassi '%d'.", chassis);
            return false;
        }

        return car.getWeight() <= parkingSpace.getWeight() &&
               car.getHeight() <= parkingSpace.getHeight() &&
               car.getLength() <= parkingSpace.getLength() &&
               car.getWidth() <= parkingSpace.getWidth();

    }

    private boolean isParked(int chassis) {
        return this.mParkingRelations.containsKey(chassis);
    }

    private boolean isOccupied(int parkingSpaceID) {
        return this.mParkingRelations.containsValue(parkingSpaceID);
    }

    private void showUnparkedCars() {
        for ( Car car : this.mCars ) {
            if ( !isParked(car.getChassis()))
                System.out.println(car);
        }
    }

    private void showFreeParkingSpaces() {
        for ( ParkingSpace ps : this.mParkingSpaces ) {
            if ( !isOccupied(ps.getId()) )
                System.out.println(ps);
        }
    }

    private boolean parkCar(int chassis, int parkingSpaceID) {
        if ( !isParkable(chassis, parkingSpaceID)  || isParked(chassis) ) return false;

        this.addParkingRelation(chassis, parkingSpaceID);

        return true;
    }

    public void run() { // TODO: implement menu functionality
        String choice = "";
        do {
            try {
                choice = promptAction();
                switch (choice) {
                    case "abrir":
                        break;
                    case "entrar":
                        break;
                    case "pesquisar":
                        break;
                    case "sair":
                        break;
                    case "salvar":
                        break;
                    case "relatorios":
                        break;
                    case "fim":
                        System.out.println("Obrigado pela preferência!");
                        break;
                    default:
                        System.out.printf("Opção inválida: '%s'. Tente novamente. %n%n%n", choice);
                }
            } catch (IOException ioe) {
                System.out.println("Problema com a entrada do usuário. Contate o desenvolvedor!");
                ioe.printStackTrace();
            }
        } while(!choice.equals("fim") );
    }
}
