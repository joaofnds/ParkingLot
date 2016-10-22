package com.parkinglot;

import com.parkinglot.model.ParkingSpace;
import com.parkinglot.model.Car;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private List<ParkingSpace> mParkingSpaces;
    private List<Car> mCars;
    private Map<String, String> mMenu;
    private BufferedReader mReader;

    public ParkingLot() {
        this.mCars = new ArrayList<Car>();
        this.mParkingSpaces = new ArrayList<ParkingSpace>();
        this.mMenu = new LinkedHashMap<String, String>();
        mMenu.put("Abrir", "Iniciar ou continuar uma simulção");
        mMenu.put("Entrar", "Aloca uma vaga no estacionamento");
        mMenu.put("Pesquisar", "Encontra a vaga mais adequada ao veículo");
        mMenu.put("Sair", "Desaloca vaga do veículo");
        mMenu.put("Salvar", "Salva o estado atual do estacionamento");
        mMenu.put("Relatórios", "Gera relatórios sobre o estacionamento");
        mMenu.put("Fim", "Encerra a aplicação");
        this.mReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void addCar(Car car) { this.mCars.add(car); }

    public void addParkingSpace(ParkingSpace parkingSpace) { this.mParkingSpaces.add(parkingSpace); }

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
            for (Car car : this.mCars) {
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
    		for (ParkingSpace ps : this.mParkingSpaces) {
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
