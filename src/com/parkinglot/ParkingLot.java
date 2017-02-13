package com.parkinglot;

import com.parkinglot.model.ParkingLog;
import com.parkinglot.model.ParkingSpace;
import com.parkinglot.model.Car;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ParkingLot {
	private List<ParkingSpace> mParkingSpaces;
	private List<Car> mCars;
	private List<ParkingLog> mParkingLogs;
	private Map<String, String> mMenu; // Option, Description
	private Map<Integer, Integer> mParkingRelations; // Chassis, Parking space ID
	private BufferedReader mReader;
	private Scanner mScanner;

	public ParkingLot() {
		this.mCars = new ArrayList<Car>();
		this.mParkingSpaces = new ArrayList<ParkingSpace>();
		this.mParkingLogs = new ArrayList<ParkingLog>();
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
		this.mScanner = new Scanner(System.in);
	}

	private void addCar(Car car) { this.mCars.add(car); }

	private void addParkingSpace(ParkingSpace parkingSpace) { this.mParkingSpaces.add(parkingSpace); }

	private void addParkingRelation(int chassis, int parkingSpaceID) {
		this.mParkingRelations.put(chassis, parkingSpaceID);
	}

	private void addParkingLog(ParkingLog log) {
		mParkingLogs.add(log);
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
			System.out.printf("Problema ao carregar '%s' %n", filename);
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
			System.out.printf("Erro ao importar o arquivo '%s' %n", filename);
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
			System.out.printf("Problema ao carregar o arquivo '%s' %n", filename);
			ioe.printStackTrace();;
		}
	}

	private void importLogsFrom(String filename) {
		try (
				FileInputStream fis = new FileInputStream(filename);
				BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		) {
			String line;
			while ( (line = reader.readLine()) != null ) {
				String[] args = line.split(",");
				this.addParkingLog(
						new ParkingLog(
								args[0].equals("entrada"),
								args[1].equals("sucesso"),
								Integer.parseInt(args[2]),
								Integer.parseInt(args[3]),
								Long.parseLong(args[4])
						)
				);
			}
		} catch ( IOException ioe ) {
			System.out.printf("Erro ao importar o arquivo '%s' %n", filename);
			ioe.printStackTrace();
		}
	}

	private void exportLogsTo(String filename) {
		try (
				FileOutputStream fos = new FileOutputStream(filename);
				PrintWriter writer = new PrintWriter(fos);
		) {
			for ( ParkingLog log : mParkingLogs ) {
				writer.println(log);
			}
		} catch (IOException ioe) {
			System.out.printf("Problema ao carregar o arquivo '%s'%n", filename);
			ioe.printStackTrace();
		}
	}

	private void importRelationsFrom(String filename) {
		try (
				FileInputStream fis = new FileInputStream(filename);
				BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		) {
			String line;
			while ( (line = reader.readLine()) != null ) {
				String[] args = line.split(",");
				addParkingRelation(
						Integer.parseInt(args[0]),
						Integer.parseInt(args[1])
				);
			}
		} catch ( IOException ioe ) {
			System.out.printf("Erro ao importar o arquivo '%s'%n", filename);
			ioe.printStackTrace();
		}
	}

	private void exportRelationsTo(String filename) {
		try (
				FileOutputStream fos = new FileOutputStream(filename);
				PrintWriter writer = new PrintWriter(fos);
		) {
			for (Object o : mParkingRelations.entrySet()) {
				Map.Entry rel = (Map.Entry) o;
				writer.println(rel.getKey() + "," + rel.getValue());
			}
		} catch ( IOException ioe ) {
			System.out.printf("Problema ao carregar o arquivo '%s'%n", filename);
			ioe.printStackTrace();
		}
	}

	private String promptAction() throws IOException {
		System.out.println("-------------------------- MENU --------------------------");
		for (Map.Entry<String, String> option : mMenu.entrySet()) {
			System.out.printf("%s - %s%n",
					option.getKey(),
					option.getValue());
		}
		System.out.println("----------------------------------------------------------");
		System.out.print("O que você deseja fazer: ");
		String choice = mReader.readLine();
		return choice.trim().toLowerCase();
	}

	private void promptOpen() {

		System.out.println("Recuperando arquivos de simulação...");
		importCarsFrom("VEICULOS.txt");
		importParkingSpacesFrom("VAGAS.txt");
		System.out.println("Recuperação de arquivos concluída com sucesso!");

		String choice = "";
		do {
			try {
				System.out.println("Deseja iniciar uma nova simulação? (y/n)");
				choice = mReader.readLine().trim().toLowerCase();
			} catch ( IOException ioe ) {
				System.out.printf("Problema com a entrada '%s'%n", choice);
				ioe.printStackTrace();
			}
		} while( !choice.equals("y") && !choice.equals("n") );

		if ( choice == "y" ) {
			System.out.println("Carros disponíveis para a nova simulação:");
			showCars(false);
			showFreeParkingSpaces();
		} else {
			System.out.println("Carregando a última simulação...");
			importRelationsFrom("RELATIONS.txt");
			importLogsFrom("LOGS.txt");
		}

	}

	private String promptLeave() {
		showCars(true);

		int chassis = promptCarChassis();

		Car car = findCarByChassis(chassis);
		if ( car == null )
			return "null,null";

		ParkingSpace ps = findParkingRelationByCar(car);
		if ( ps == null )
			return chassis + ",null";

		return chassis + "," + ps.getId();
	}

	private int promptCarChassis() {
		System.out.print("Informe o número de chassi do carro: ");
		return mScanner.nextInt();
	}

	private int promptParkingSpaceID() {
		System.out.print("Informe o ID da vaga: ");
		return mScanner.nextInt();
	}

	private String promptParking() {
		int chassis = promptCarChassis();
		int psID = promptParkingSpaceID();
		return chassis + "," + psID;
	}

	private void promptSearch() {
		int chassis = promptCarChassis();
		Car car = findCarByChassis(chassis);
		if ( car == null ) {
			System.out.printf("Não foi possível encontrar o veículo com o número de chassi '%d'%n", chassis);
			return;
		}
		ParkingSpace bestPS = findParkingSpaceByCar(car);
		if ( bestPS == null )
			System.out.printf("Não foi possível encontrar uma vaga para o veículo com o número de chassi '%d'%n", chassis);
		else
			System.out.printf("A melhor vaga para seu veículo é:%n%s", bestPS.pretty());
	}

	private ParkingSpace findParkingSpaceByID(int id) {
		for ( ParkingSpace ps : this.mParkingSpaces ) {
			if ( ps.getId() == id ) return ps;
		}
		return null;
	}

	private ParkingSpace findParkingSpaceByCar(Car car) {
		if ( !isParked(car.getChassis()) )
			for ( ParkingSpace ps : mParkingSpaces )
				if ( isParkable(car.getChassis(), ps.getId()) )
					return ps;
		return null;
	}

	private ParkingSpace findParkingRelationByCar(Car car) {
		if ( car == null ) return null;
		int psID = mParkingRelations.get(car.getChassis());
		return findParkingSpaceByID(psID);
	}

	private Car findCarByChassis(int chassis) {
		for ( Car car : this.mCars ) {
			if ( car.getChassis() == chassis )  return car;
		}
		return null;
	}

	private boolean isParkable(int chassis, int parkingSpaceID) {
		Car car = findCarByChassis(chassis);

		if (car == null) {
			System.out.printf("Não foi possível encontrar um verículo com o número de chassi '%d'.%n", chassis);
			return false;
		}

		ParkingSpace parkingSpace = findParkingSpaceByID(parkingSpaceID);

		if ( parkingSpace == null ) {
			System.out.printf("Não foi possível encontrar a vaga de ID '%d'.%n", parkingSpaceID);
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

	private void showCars(boolean parked) {
		if ( parked )
			System.out.println("----------------------- Lista de Carros estacionados ---------------------------");
		else
			System.out.println("----------------------- Lista de Carros não estacionados -----------------------");
		for ( Car car : this.mCars ) {
			if ( this.mCars.indexOf(car) > 10 ) break;
			if ( isParked(car.getChassis()) == parked ) {
				String[] carInfo = car.toString().split(",");
				System.out.println(car.pretty());
			}
		}
		System.out.println("--------------------------------------------------------------------------------");
	}

	private void showFreeParkingSpaces() {
		System.out.println("-------------------------- Lista de vagas desocupadas --------------------------");
		for ( ParkingSpace ps : this.mParkingSpaces ) {
			if ( this.mParkingSpaces.indexOf(ps) > 10 ) break;
			if ( !isOccupied(ps.getId()) ) {
				String[] psInfo = ps.toString().split(",");
				System.out.println(ps.pretty());
								}
		}
		System.out.println("--------------------------------------------------------------------------------");
	}

	private boolean parkCar() {
		if ( mCars.isEmpty() || mParkingSpaces.isEmpty() ) {
			System.out.println("Abra o programa primeiro!");
			return false;
		}

		showCars(false);
		showFreeParkingSpaces();

		String[] response = promptParking().split(",");
		int chassis = Integer.parseInt(response[0]);
		int psID = Integer.parseInt(response[1]);

		if ( !isParkable(chassis, psID)  || isParked(chassis) ) {
			mParkingLogs.add(new ParkingLog(true, false, chassis, psID, new Date().getTime()));
			return false;
		}

		this.addParkingRelation(chassis, psID);
		mParkingLogs.add(new ParkingLog(true, true, chassis, psID, new Date().getTime()));
		return true;
	}

	private boolean unparkCar() {
		if ( mCars.isEmpty() || mParkingSpaces.isEmpty() ) {
			System.out.println("Abra o programa primeiro!");
			return false;
		}

		showCars(true);

		String[] response = promptLeave().split(",");

		if( response[0].equals("null") ) {
			System.out.println("Veículo não encontrado!%n");
			return false;
		}

		if ( response[1].equals("null") ) {
			System.out.printf("Não há nenhuma vaga associada ao veículo de chassi '%s'!%n", response[0]);
			return false;
		}

		int chassis = Integer.parseInt(response[0]);
		int psID = Integer.parseInt(response[1]);
		if ( isParked(chassis) ) {
			mParkingRelations.remove(chassis);
			mParkingLogs.add(new ParkingLog(false, true, chassis, psID, new Date().getTime()));
			return true;
		} else {
			mParkingLogs.add(new ParkingLog(false, false, chassis, psID, new Date().getTime()));
			return false;
		}
	}

	private void generateReport (String filename) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
		String currentDate = sdf.format(new Date().getTime());

		try (
				FileOutputStream fos = new FileOutputStream(filename);
				PrintWriter writer = new PrintWriter(fos);
		) {
			writer.printf("Relatório do dia %s%n===%n%n%n", currentDate);
			reportAmountPerPS(writer, sdf, currentDate);
			reportAmountPerCatergory(writer, sdf, currentDate);
			reportAmountPerSpecs(writer, sdf, currentDate);
			System.out.printf("Relatório gerado e salvo em '%s'%n", filename);
		} catch ( IOException ioe ) {
			System.out.printf("Não foi possível abrir o arquivo '%s'%n", filename);
			ioe.printStackTrace();
		}
	}

	private void reportAmountPerPS(PrintWriter writer, SimpleDateFormat sdf, String date) {
	//	Para cada vaga, a quantidade total de veículos que estacionaram nela hoje e o total de tentativas
	//	falhas de estacionar um veículo nela.

		writer.printf("### Relação da quantidade de carros estacionados por vaga%n");
		writer.printf("| ID da vaga | Carros estacionados | Tentativas Falhas |%n");
		writer.printf("| ---------: | :-----------------: | :---------------: |%n");
		for ( ParkingSpace ps : mParkingSpaces ) {
			int successParks = 0;
			int failedParks = 0;
			for ( ParkingLog parkingLog : mParkingLogs ) {
				if ( parkingLog.getParkingSpaceID() == ps.getId() &&
						parkingLog.isEntering() &&
						date.equals(sdf.format( parkingLog.getTime() )) ) {
					if ( parkingLog.succeed() )
						successParks += 1;
					else if ( !parkingLog.succeed() )
						failedParks += 1;
				}
			}
			writer.printf("| %d | %d | %d |%n", ps.getId(), successParks, failedParks);
		}
		writer.printf("%n%n%n");
	}

	private void reportAmountPerCatergory(PrintWriter writer, SimpleDateFormat sdf, String date) {
	// O total de veículos estacionados hoje, longos, curtos, pesados, leves, altos, baixos, largos, estreitos.
	// Um veículo é pesado se tiver peso igual ou superior a 2500 kg. Um veículo é longo se tiver
	// comprimento igual ou superior a 2,5 metros. Um veículo é largo se tiver largura maior ou igual a 1,6
	// metros. Um veículo é alto se tiver altura maior ou igual a 1,7 metros.
		int longVehicles = 0;
		int shortVehicles = 0;
		int heavy = 0;
		int light = 0;
		int tall = 0;
		int low = 0;
		int wide = 0;
		int narrow = 0;
		int grandTotal = 0;

		Set<Car> totalCarsToday = new HashSet<Car>();

		for ( ParkingLog parkingLog : mParkingLogs ) {

			Car car = findCarByChassis(parkingLog.getCarChassis());

			if ( car == null ) continue; // If car doesn't exists
			if ( !totalCarsToday.add(car) )  continue; // If car has already parked today

			if ( parkingLog.isEntering() && parkingLog.succeed() &&  date.equals(sdf.format(new Date(parkingLog.getTime()) )) )
			{
				grandTotal += 1;

				if ( car.getLength() >= 2.50 )
					longVehicles += 1;
				else
					shortVehicles += 1;

				if ( car.getWeight() >= 2500.00 )
					heavy += 1;
				else
					light += 1;

				if ( car.getHeight() >= 1.70 )
					tall += 1;
				else
					low += 1;

				if ( car.getWidth() >= 1.60 )
					wide += 1;
				else
					narrow += 1;
			}
		}
		writer.printf("### Relação da quantidade de carros estacionados por categoria%n");
		writer.printf("| Total | Longos | Curtos | Pesados | Leves | Altos | Baixos | Largos | Estreitos |%n");
		writer.printf("| ----: | :----: | :----: | :-----: | :---: | :---: | :----: | :----: | :-------: |%n");
		writer.printf("| %d |  %d |  %d |  %d |  %d |  %d |  %d |  %d |  %d |%n",   grandTotal,
																					longVehicles,
																					shortVehicles,
																					heavy,
																					light,
																					tall,
																					low,
																					wide,
																					narrow
		);
	}

	private void reportAmountPerSpecs(PrintWriter writer, SimpleDateFormat sdf, String date) {
	// A lista de veículos que já estacionaram hoje, em ordem decrescente de peso, altura, comprimento e
	// largura, nesta ordem. Ou seja, se dois veículos, a e b, pesarem 3500 kg, mas a tem 1,6 m de altura e
	// b tem 2 m de altura, b deve aparecer antes que a.

		Set<Car> totalCarsToday = new HashSet<Car>();

		for ( ParkingLog parkingLog : mParkingLogs ) {

			Car car = findCarByChassis(parkingLog.getCarChassis());
			if ( car == null ) continue; // If car doesn't exists

			if ( parkingLog.isEntering() && parkingLog.succeed() &&  date.equals(sdf.format(new Date(parkingLog.getTime()) )) )
			{
				totalCarsToday.add(car);
			}
		}

		List<Car> carList = new ArrayList<Car>(totalCarsToday);
		carList.sort(new Comparator<Car>() {
			@Override
			public int compare(Car o1, Car o2) {
				return o1.compareTo(o2);
			}
		});

		writer.printf("### Lista de veículos que já estacionaram hoje, em ordem decrescente de peso, altura, comprimento e largura%n");
		writer.printf("| Chassí do veículo | Peso | Altura | Comprimento | Largura |%n");
		writer.printf("| :---------------: | :--: | :----: |  :--------: |  :----: |%n");
		for (Car c : carList) {
			writer.printf("| %d | %.2f | %.2f | %.2f | %.2f |%n",
					c.getChassis(),
					c.getWeight(),
					c.getHeight(),
					c.getLength(),
					c.getWidth());
		}
	}

	public void run() {
		String choice = "";
		do {
			try {
				choice = promptAction();
				switch (choice) {
					case "abrir":
						promptOpen();
						break;
					case "entrar":
						if( parkCar() )
							System.out.println("Carro estacionado com sucesso!");
						else
							System.out.println("Não foi possível estacionar o carro!");
						break;
					case "pesquisar":
						promptSearch();
						break;
					case "sair":
						unparkCar();
						break;
					case "salvar":
						exportRelationsTo("RELATIONS.txt");
						exportLogsTo("LOGS.txt");
						System.out.println("Arquivos salvos com sucesso!");
						break;
					case "relatorios":
						generateReport("REPORT.md");
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
