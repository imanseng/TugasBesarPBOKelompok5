package project.pbo.domain;

import java.util.Scanner;

public class Mobil extends Kendaraan {
    private int jumlahPintu;

    // Konstruktor mengalirkan data ke superclass
    public Mobil(String platNomor, double hargaSewaPerHari, String jenisKendaraan, int jumlahPintu) {
        super(platNomor, hargaSewaPerHari, jenisKendaraan);
        this.jumlahPintu = jumlahPintu;
    }

    public int getJumlahPintu() {
        return jumlahPintu;
    }

    public double hitungDenda(int hariTerlambat) {
        return super.hitungDenda(hariTerlambat);
    }

    @Override
    public void inputSpesifik(Scanner scanner) {
        do {
            System.out.print("Masukkan jumlah pintu: ");
            this.jumlahPintu = scanner.nextInt();
            if (this.jumlahPintu <= 0 || this.jumlahPintu > 4) {
                System.out.println("Jumlah pintu harus diantara 1 sampai 4!");
            }
        } while (this.jumlahPintu <= 0 || this.jumlahPintu > 4);
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo(); // Mencetak Plat, Merk, Harga, Status dari kelas induk
        System.out.println("Jumlah Pintu: " + this.jumlahPintu + " Pintu");
    }

}
