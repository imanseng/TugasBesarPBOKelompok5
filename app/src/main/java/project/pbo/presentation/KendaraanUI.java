package project.pbo.presentation;

import project.pbo.domain.Kendaraan;
import project.pbo.domain.Mobil;
import project.pbo.domain.Motor;
import project.pbo.service.KendaraanService;

import java.util.List;
import java.util.Scanner;

public class KendaraanUI {
    private final KendaraanService kendaraanService = new KendaraanService();

    public void tambahKendaraan(Scanner input) {
        int jenis = -1;

        String platNomor = "";
        double hargaSewa = 0;

        while (true) {
            System.out.println("=== TAMBAH KENDARAAN ===");
            System.out.print("\nMasukkan Plat Nomor Kendaraan: ");
            platNomor = input.nextLine().trim().toUpperCase();

            if (kendaraanService.isPlatNomorTerdaftar(platNomor)) {
                System.out.println("[GAGAL] Plat Nomor " + platNomor + " sudah terdaftar di sistem!");
                continue;
            }

            while (true) {
                System.out.print("Masukkan Harga Sewa per Hari: ");
                try {
                    hargaSewa = Double.parseDouble(input.nextLine().trim());
                    if (hargaSewa <= 0) {
                        System.out.println("[PERINGATAN] Harga sewa harus lebih dari 0.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("[PERINGATAN] Harga sewa harus berupa angka.");
                }
            }

            while (true) {
                System.out.println("Silahkan pilih jenis kendaraan:");
                System.out.println("1. Mobil");
                System.out.println("2. Motor");
                System.out.println("0. Batalkan proses");
                System.out.print("\nPilihan Anda > ");

                try {
                    jenis = Integer.parseInt(input.nextLine().trim());
                    if (jenis < 0 || jenis > 2) {
                        System.out.println("Pilihan tidak valid!");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("[PERINGATAN] Pilihan harus berupa angka.");
                }
            }

            break;
        }

        if (jenis == 0) {
            System.out.println("Proses penambahan dibatalkan.");
            return;
        }

        Kendaraan kendaraanBaru = null;

        if (jenis == 1) {
            int jumlahPintu = 0;
            do {
                System.out.print("Masukkan jumlah pintu: ");
                try {
                    jumlahPintu = Integer.parseInt(input.nextLine().trim());
                    if (jumlahPintu <= 0 || jumlahPintu > 4) {
                        System.out.println("Jumlah pintu harus diantara 1 sampai 4!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[PERINGATAN] Jumlah pintu harus berupa angka.");
                    jumlahPintu = 0;
                }
            } while (jumlahPintu <= 0 || jumlahPintu > 4);
            
            kendaraanBaru = new Mobil(platNomor, hargaSewa, "Mobil", jumlahPintu);
            System.out.println("[DEBUG LOG] Objek mobil berhasil dibuat dengan aman.");

        } else if (jenis == 2) {
            String jenisTransmisi = "";
            while (true) {
                System.out.print("Masukkan Info Tambahan (Manual/Matic): ");
                String transmisi = input.nextLine().trim();

                if (transmisi.equalsIgnoreCase("Manual") || transmisi.equalsIgnoreCase("Matic")) {
                    jenisTransmisi = transmisi.substring(0, 1).toUpperCase() + transmisi.substring(1).toLowerCase();
                    break;
                }
                System.out.println("[PERINGATAN] Pilihan tidak valid! Harap ketik 'Manual' atau 'Matic'.");
            }
            kendaraanBaru = new Motor(platNomor, hargaSewa, "Motor", jenisTransmisi);
            System.out.println("[DEBUG LOG] Objek motor berhasil dibuat dengan aman.");
        }

        if (kendaraanBaru != null) {
            kendaraanService.tambahKendaraan(kendaraanBaru);
            System.out.println("[SUKSES] Data kendaraan berhasil disimpan ke json. Status default: TERSEDIA.");
        }
    }

    public void lihatDaftarKendaraan(Scanner input){
        List<Kendaraan> listKendaraan = kendaraanService.getAllKendaraan();

        System.out.println("\n============================================================");
        System.out.println("                   DAFTAR SELURUH KENDARAAN");
        System.out.println("============================================================");

        if (listKendaraan == null || listKendaraan.isEmpty()) {
            System.out.println("Data kendaraan masih kosong.");
        } else {
            System.out.println("| Plat Nomor | Jenis | Harga/Hari | Info Tambahan | Status        |");
            System.out.println("--------------------------------------------------------------------------------");

            for (Kendaraan k : listKendaraan) {
                String platNomor = k.getPlatNomor();
                String jenis = k.getJenisKendaraan();
                String harga = "Rp " + String.format("%,.0f", k.getHargaSewaPerHari());
                String status = k.getStatus();
                String infoTambahan = k.getInfoTambahan();

                System.out.printf("| %-10s | %-5s | %-10s | %-13s | %-13s |%n", 
                                    platNomor, jenis, harga, infoTambahan, status);
            }
        }
        System.out.println("\nTekan ENTER untuk kembali ke menu utama...");
        input.nextLine(); 
    }
    
    public void hapusKendaraan(Scanner input) {
        List<Kendaraan> listKendaraan = kendaraanService.getAllKendaraan();

        System.out.println("\n========================================");
        System.out.println("         MENU HAPUS KENDARAAN           ");
        System.out.println("========================================");

        if (listKendaraan.isEmpty()) {
            System.out.println("  Data kendaraan masih kosong. Tidak ada yang bisa dihapus.");
            System.out.println("\nTekan ENTER untuk kembali ke menu utama...");
            input.nextLine();
            return;
        }

        while (true) {
            System.out.print("\nMasukkan Plat Nomor yang ingin dihapus (ketik 0 untuk kembali): ");
            String platNomor = input.nextLine().trim().toUpperCase();

            if (platNomor.equals("0")) {
                System.out.println("Kembali ke menu utama.");
                return;
            }

            if (platNomor.isEmpty()) {
                System.out.println("[!] Plat Nomor tidak boleh kosong!");
                continue;
            }

            Kendaraan kendaraanDitemukan = kendaraanService.cariKendaraanByPlat(platNomor);

            if (kendaraanDitemukan == null) {
                System.out.println("[ERROR] Kendaraan dengan plat nomor " + platNomor + " tidak ditemukan di sistem!");
                continue;
            }

            System.out.println("\n--- Detail Kendaraan Ditemukan ---");
            tampilkanDetailKendaraan(kendaraanDitemukan);
            System.out.println("----------------------------------");

            if (kendaraanDitemukan.getStatus().equalsIgnoreCase("SEDANG DISEWA")) {
                System.out.println("[GAGAL] Kendaraan " + platNomor
                        + " masih berstatus SEDANG DISEWA, data tidak dapat dihapus!");
                continue;
            }

            kendaraanService.hapusKendaraan(kendaraanDitemukan);
            System.out.println("\n[SUKSES] Kendaraan " + platNomor + " berhasil dihapus dari sistem.");
            System.out.println("\nTekan ENTER untuk kembali ke menu utama...");
            input.nextLine();
            return;
        }
    }

    public void cekKendaraanTersedia(Scanner input) {
        System.out.println("=== DAFTAR KENDARAAN YANG TERSEDIA ===");
        List<Kendaraan> listKendaraanTersedia = kendaraanService.getKendaraanTersedia();

        if (listKendaraanTersedia.isEmpty()) {
            System.out.println("Tidak ada kendaraan yang tersedia saat ini.");
        } else {
            for (Kendaraan kendaraan : listKendaraanTersedia) {
                System.out.println("======================================");
                System.out.println("Plat Nomor      :" + kendaraan.getPlatNomor());
                System.out.println("Jenis           :" + kendaraan.getJenisKendaraan());
                System.out.println("Harga sewa/hari :" + kendaraan.getHargaSewaPerHari());
                System.out.println("Status          :" + kendaraan.getStatus());
                System.out.println("======================================");
            }
        }
        System.out.println("Tekan Enter untuk Kembali ke Menu");
        input.nextLine();
    }

    public void tampilkanDetailKendaraan(Kendaraan k) {
        System.out.println("Plat Nomor: " + k.getPlatNomor());
        System.out.println("Merk Kendaraan: " + k.getJenisKendaraan());
        System.out.println("Harga Sewa/Hari: Rp " + k.getHargaSewaPerHari());
        System.out.println("Info Tambahan: " + k.getInfoTambahan());
    }
}
