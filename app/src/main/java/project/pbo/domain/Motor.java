package project.pbo.domain;

import java.util.Scanner;

public class Motor extends Kendaraan {
    private String jenisTransmisi;

    public Motor(String platNomor, double hargaSewaPerHari, String jenisKendaraan, String jenisTransmisi) {
        super(platNomor, hargaSewaPerHari, jenisKendaraan);
        this.jenisTransmisi = jenisTransmisi;
    }

    public String getJenisTransmisi() {
        return jenisTransmisi;
    }

    public double hitungDenda(int hariTerlambat) {
        return super.hitungDenda(hariTerlambat);
    }

    // OVERRIDE METHOD: Mengambil input transmisi khusus objek motor itu sendiri
    @Override
    public void inputSpesifik(Scanner scanner) {
        while (true) {
            System.out.print("Masukkan Info Tambahan (Manual/Matic): ");
            String transmisi = scanner.nextLine().trim();
            
            // Validasi input (Abaikan huruf besar/kecil)
            if (transmisi.equalsIgnoreCase("Manual") || transmisi.equalsIgnoreCase("Matic")) {
                // Format agar huruf pertama kapital (contoh: "Matic")
                this.jenisTransmisi = transmisi.substring(0, 1).toUpperCase() + transmisi.substring(1).toLowerCase();
                break; // Keluar dari looping jika input valid
            }
            System.out.println("[PERINGATAN] Pilihan tidak valid! Harap ketik 'Manual' atau 'Matic'.");
        }
    }

    public void tampilkanInfo() {
        super.tampilkanInfo(); // Mencetak Plat, Merk, Harga, Status dari kelas induk
        System.out.println("Jenis Transmisi: " + this.jenisTransmisi);
    }
}
