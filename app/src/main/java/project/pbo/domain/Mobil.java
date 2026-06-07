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

    //Adit (feat/return-logic) : diubah ini agar sesuai dengan aturan denda (Mobil 50k/hari & Motor 20k/hari)
    @Override
    public double hitungDenda(int hariTerlambat) {
        if (hariTerlambat <= 0) {
            return 0;
        }
        return hariTerlambat * 50000;
    }

    @Override
    public void inputSpesifik(Scanner scanner) {
        do {
            System.out.print("Masukkan jumlah pintu: ");
            try {
                this.jumlahPintu = Integer.parseInt(scanner.nextLine().trim());
                if (this.jumlahPintu <= 0 || this.jumlahPintu > 4) {
                    System.out.println("Jumlah pintu harus diantara 1 sampai 4!");
                }
            } catch (NumberFormatException e) {
                System.out.println("[PERINGATAN] Jumlah pintu harus berupa angka.");
                this.jumlahPintu = 0;
            }
        } while (this.jumlahPintu <= 0 || this.jumlahPintu > 4);
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo(); // Mencetak Plat, Merk, Harga, Status dari kelas induk
        System.out.println("Jumlah Pintu: " + this.jumlahPintu + " Pintu");
    }

}
