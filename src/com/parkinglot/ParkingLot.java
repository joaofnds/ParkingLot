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


	/**
	 * Add a Car to mCars list
	 * @param car Car to be added
	 */
	private void addCar(Car car) { this.mCars.add(car); }

	/**
	 * Add a ParkingSpace to mParkingSpaces list
	 * @param parkingSpace ParkingSpace to be added
	 */
	private void addParkingSpace(ParkingSpace parkingSpace) { this.mParkingSpaces.add(parkingSpace); }

	/**
	 * Associate a Car chassis to a ParkingSpace ID
	 * @param chassis Car chassis number
	 * @param parkingSpaceID ParkingSpace ID
	 */
	private void addParkingRelation(int chassis, int parkingSpaceID) {
		this.mParkingRelations.put(chassis, parkingSpaceID);
	}

	/**
	 * Add a ParkingLog to mParkingLogs list
	 * @param log Log to be added
	 */
	private void addParkingLog(ParkingLog log) {
		mParkingLogs.add(log);
	}

	/**
	 * Import Cars to mCars list, from a file
	 * @param filename path to file
	 */
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

	/**
	 * Export Cars from mCars list to a file
	 * @param filename path to file
	 */
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

	/**
	 * Import ParkingSpaces to mParkingSpaces, from a file
	 * @param filename path to file
	 */
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

	/**
	 * Export ParkingSpaces from mParkingSpaces to a file
	 * @param filename path to file
	 */
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

	/**
	 * Import ParkingLogs to mParkingLogs list, from a file
	 * @param filename path to file
	 */
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

	/**
	 * Export ParkingLogs from mParkingLogs to a file
	 * @param filename path to file
	 */
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

	/**
	 * Import ParkingRelations to mParkingRelations, from a file
	 * @param filename path to file
	 */
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

	/**
	 * Export ParkingRelations from mParkingRelations to a file
	 * @param filename path to file
	 */
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

	/**
	 * Prompt user actions
	 * @return User input
	 * @throws IOException Throws IOException in case of reading failure
	 */
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

	/**
	 * Prompt user for last simulation continuation
	 */
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

		if (choice.equals("y")) {
			System.out.println("Carros disponíveis para a nova simulação:");
			showCars(false);
			showFreeParkingSpaces();
		} else {
			System.out.println("Carregando a última simulação...");
			importRelationsFrom("RELATIONS.txt");
			importLogsFrom("LOGS.txt");
		}

	}

	/**
	 * Prompt user to select a car to un-park
	 * @return CSV string width 'car chassis number','parking space id'
	 */
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

	/**
	 * Prompt user for a Car chassis number
	 * @return User input
	 */
	private int promptCarChassis() {
		System.out.print("Informe o número de chassi do carro: ");
		return mScanner.nextInt();
	}

	/**
	 * Prompt user for a ParkingSpace ID number
	 * @return User input
	 */
	private int promptParkingSpaceID() {
		System.out.print("Informe o ID da vaga: ");
		return mScanner.nextInt();
	}

	/**
	 * Prompt user for a Car chassis number to be parked at a given ParkingSpace ID
	 * @return CSV string with 'Car chassis number', 'ParkingSpace ID'
	 */
	private String promptParking() {
		int chassis = promptCarChassis();
		int psID = promptParkingSpaceID();
		return chassis + "," + psID;
	}

	/**
	 * Prompt user from a Car chassis number and shows the best ParkingSpace for that vehicle
	 */
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

	/**
	 * Find a ParkingSpace by his ID number
	 * @param id ParkingSpace ID to be searched
	 * @return ParkingSpace if found, null if not found
	 */
	private ParkingSpace findParkingSpaceByID(int id) {
		for ( ParkingSpace ps : this.mParkingSpaces ) {
			if ( ps.getId() == id ) return ps;
		}
		return null;
	}

	/**
	 * Find a ParkingSpace compatible with a given Car
	 * @param car Car to be parked
	 * @return ParkingSpace if found, null if not found
	 */
	private ParkingSpace findParkingSpaceByCar(Car car) {
		if ( !isParked(car.getChassis()) )
			for ( ParkingSpace ps : mParkingSpaces )
				if ( isParkable(car.getChassis(), ps.getId()) )
					return ps;
		return null;
	}

	/**
	 * Finds a ParkingSpace that contains a giver Car
	 * @param car Car parked at ParkingSpace
	 * @return ParkingSpace if found, null if not found
	 */
	private ParkingSpace findParkingRelationByCar(Car car) {
		if ( car == null ) return null;
		int psID = mParkingRelations.get(car.getChassis());
		return findParkingSpaceByID(psID);
	}

	/**
	 * Find a Car by it's chassis number
	 * @param chassis Car chassis number
	 * @return Car if found, null if not found
	 */
	private Car findCarByChassis(int chassis) {
		for ( Car car : this.mCars ) {
			if ( car.getChassis() == chassis )  return car;
		}
		return null;
	}

	/**
	 * Checks if a given Car can park at a given ParkingSpace
	 * @param chassis Car chassis number
	 * @param parkingSpaceID ParkingSpace ID
	 * @return true if compatible, false if not compatible
	 */
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

	/**
	 * Checks if a Car it's currently parked
	 * @param chassis Car chassis number
	 * @return true if it's currently parked, false if it's not currently parked
	 */
	private boolean isParked(int chassis) {
		return this.mParkingRelations.containsKey(chassis);
	}

	/**
	 * Checks if a ParkingSpace it's currently occupied
	 * @param parkingSpaceID ParkingSpace ID
	 * @return true if it's currently occupied, false if it's not currently occupied
	 */
	private boolean isOccupied(int parkingSpaceID) {
		return this.mParkingRelations.containsValue(parkingSpaceID);
	}

	/**
	 * Print's Car stored in mCars list based on their parking state
	 * @param parked true if you want to show parked cars, false if you want to show un-parked cars
	 */
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

	/**
	 * Print's to the user all unoccupied ParkingSpaces
	 */
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

	/**
	 * Tries to park a car
	 * @return true if succeeds, false if doesn't succeeds
	 */
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

	/**
	 * Tries to un-park a car
	 * @return true if succeeds, false if doesn't succeeds
	 */
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

	/**
	 * Generates Parking Lot reports and writes them to a given file path
	 * @param filename path to file
	 */
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

	/**
	 * Generates a report about the amount of cars that tried to park at every single ParkingSpace, showing succeeded and failed attempts
	 * @param writer PrintWriter to write the report
	 * @param sdf SimpleDateFormatter to format dates
	 * @param date Date of the report
	 */
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

	/**
	 * Generates a report abouts the amount of cars that parked at a given date, categorizing them by length, weight, height and width.
	 * @param writer PrintWriter to write the report
	 * @param sdf SimpleDateFormatter to format dates
	 * @param date Date of the report
	 */
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
		writer.printf("| %d |  %d |  %d |  %d |  %d |  %d |  %d |  %d |  %d |%n%n%n",   grandTotal,
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

	/**
	 * Generates a report about the amount of cars that parked today sorted descendingly by weight, height, length and width
	 * @param writer PrintWrite to write the report
	 * @param sdf SimpleDateFormatter to format dates
	 * @param date Date of the report
	 */
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
		writer.printf("%n%n%n");
	}

	/**
	 * Runs the ParkingLot code using a Menu system
	 */
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
