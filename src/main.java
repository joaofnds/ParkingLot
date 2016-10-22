import com.parkinglot.ParkingLot;

public class main {
    public static void main(String[] args) {
        String carsFileName = "VEICULOS.txt";
        String parkingSpacesFileName = "VAGAS.txt";
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.importCarsFrom(carsFileName);
        parkingLot.importParkingSpacesFrom(parkingSpacesFileName);
        parkingLot.exportCarsTo(carsFileName);
        parkingLot.exportParkingSpacesTo(parkingSpacesFileName);
    }
}
