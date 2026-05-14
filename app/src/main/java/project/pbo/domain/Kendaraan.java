package project.pbo.domain;

import java.util.Scanner;

public class Kendaraan {
    private String platNomor;
    private double hargaSewaPerHari;
    private String jenisKendaraan;
    private String status;

    public Kendaraan(String platNomor, double hargaSewaPerHari, String jenisKendaraan) {
        this.platNomor = platNomor;
        this.hargaSewaPerHari = hargaSewaPerHari;
        this.jenisKendaraan = jenisKendaraan;
        this.status = "TERSEDIA";
    }

    public String getPlatNomor() { return platNomor; }
    public double getHargaSewaPerHari() { return hargaSewaPerHari; }
    public String getJenisKendaraan() { return jenisKendaraan; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double hitungDenda(int hariTerlambat) {
        return hariTerlambat * hargaSewaPerHari;
    }

    public void inputSpesifik(Scanner scanner) {}

    public void tampilkanInfo() {
        System.out.println("Plat Nomor: " + this.platNomor);
        System.out.println("Merk Kendaraan: " + this.jenisKendaraan); // Atribut merk/jenis induk
        System.out.println("Harga Sewa/Hari: Rp " + this.hargaSewaPerHari);
        System.out.println("Status: " + this.status);
    }

}
