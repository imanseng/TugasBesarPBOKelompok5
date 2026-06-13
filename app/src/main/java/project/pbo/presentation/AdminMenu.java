package project.pbo.presentation;

import project.pbo.domain.Admin;
import project.pbo.domain.Kendaraan;
import project.pbo.domain.Mobil;
import project.pbo.domain.Motor;
import project.pbo.repository.KendaraanRepository;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private char pilihanMenu;
    private final KendaraanRepository repo = new KendaraanRepository();
    private final Admin admin;

    public AdminMenu(Admin admin) {
        this.admin = admin;
        this.pilihanMenu = ' ';
    }

    public void prosesMenu() {
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("=== MENU ADMIN ===");
            System.out.println("Selamat Datang, " + admin.getUsername());
            System.out.println("Silahkan pilih menu:");
            System.out.println("1. Tambah Kendaraan Baru");
            System.out.println("2. Lihat Semua Kendaraan");
            System.out.println("3. Hapus Kendaraan");
            System.out.println("0. Logout");
            System.out.print("\nPilihan Anda > ");

            String userInput = input.nextLine();
            if (userInput.isEmpty()) {
                pilihanMenu = ' ';
            } else {
                pilihanMenu = userInput.charAt(0);
            }

            switch (pilihanMenu) {
                case '1':
                    tambahKendaraan();
                    break;
                case '2':
                    lihatDaftarKendaraan();
                    break;
                case '3':
                    hapusKendaraan();
                    break;
                case '0':
                    System.out.println("Logout berhasil.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        } while (pilihanMenu != '0');
    }

    public void tambahKendaraan() {
        Scanner input = new Scanner(System.in);
        int jenis = -1;

        String platNomor = "";
        double hargaSewa = 0;

        List<Kendaraan> listKendaraan = repo.loadAll();

        while (true) {
            System.out.println("=== TAMBAH KENDARAAN ===");
            System.out.print("\nMasukkan Plat Nomor Kendaraan: ");
            platNomor = input.nextLine().trim().toUpperCase();

            boolean isDuplikat = false;
            for (Kendaraan k : listKendaraan) {
                if (k.getPlatNomor().equalsIgnoreCase(platNomor)) {
                    isDuplikat = true;
                    break;
                }
            }

            if (isDuplikat) {
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
            kendaraanBaru = new Mobil(platNomor, hargaSewa, "Mobil", 0);
            kendaraanBaru.inputSpesifik(input);
            System.out.println("[DEBUG LOG] Objek mobil berhasil dibuat dengan aman.");

        } else if (jenis == 2) {
            kendaraanBaru = new Motor(platNomor, hargaSewa, "Motor", "");
            kendaraanBaru.inputSpesifik(input);
            System.out.println("[DEBUG LOG] Objek motor berhasil dibuat dengan aman.");
        }

        if (kendaraanBaru != null) {
            listKendaraan.add(kendaraanBaru);
            repo.saveAll(listKendaraan);
            System.out.println("[SUKSES] Data kendaraan berhasil disimpan ke json. Status default: TERSEDIA.");
        }
    }

    public void lihatDaftarKendaraan(){
        List<Kendaraan> listKendaraan = repo.loadAll();
        Scanner input = new Scanner(System.in);

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
                String infoTambahan = "-";

                if (k instanceof Mobil) {
                    Mobil mobil = (Mobil) k;
                    infoTambahan = mobil.getJumlahPintu() + " pintu";
                } else if (k instanceof Motor) {
                    Motor motor = (Motor) k;
                    infoTambahan = motor.getJenisTransmisi();
                }

                System.out.printf("| %-10s | %-5s | %-10s | %-13s | %-13s |%n", 
                                    platNomor, jenis, harga, infoTambahan, status);
            }
        }
        System.out.println("\nTekan ENTER untuk kembali ke menu utama...");
        input.nextLine(); 
    }
    
    public void hapusKendaraan() {
        Scanner input = new Scanner(System.in);
        List<Kendaraan> listKendaraan = repo.loadAll();

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

            Kendaraan kendaraanDitemukan = null;
            for (Kendaraan k : listKendaraan) {
                if (k.getPlatNomor().equalsIgnoreCase(platNomor)) {
                    kendaraanDitemukan = k;
                    break;
                }
            }

            if (kendaraanDitemukan == null) {
                System.out.println("[ERROR] Kendaraan dengan plat nomor " + platNomor + " tidak ditemukan di sistem!");
                continue;
            }

            System.out.println("\n--- Detail Kendaraan Ditemukan ---");
            kendaraanDitemukan.tampilkanInfo();
            System.out.println("----------------------------------");

            if (kendaraanDitemukan.getStatus().equalsIgnoreCase("SEDANG DISEWA")) {
                System.out.println("[GAGAL] Kendaraan " + platNomor
                        + " masih berstatus SEDANG DISEWA, data tidak dapat dihapus!");
                continue;
            }

            listKendaraan.remove(kendaraanDitemukan);
            repo.saveAll(listKendaraan);
            System.out.println("\n[SUKSES] Kendaraan " + platNomor + " berhasil dihapus dari sistem.");
            System.out.println("\nTekan ENTER untuk kembali ke menu utama...");
            input.nextLine();
            return;
        }
    }
}
