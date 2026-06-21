package project.pbo;

import project.pbo.domain.Admin;
import project.pbo.domain.Staff;
import project.pbo.infrastructure.database.DatabaseConnection;
import project.pbo.infrastructure.database.PostgreSqlDatabaseConnection;
import project.pbo.domain.Owner;
import project.pbo.domain.Pengguna;
import project.pbo.service.AuthService;
import project.pbo.presentation.AdminMenu;
import project.pbo.presentation.StaffMenu;
import project.pbo.repository.PenggunaRepository;
import project.pbo.presentation.OwnerMenu;
import project.pbo.presentation.KendaraanUI;
import project.pbo.presentation.PelangganUI;
import project.pbo.presentation.TransaksiUI;
import project.pbo.presentation.LaporanUI;
import project.pbo.repository.KendaraanRepository;
import project.pbo.repository.PelangganRepository;
import project.pbo.repository.TransaksiRepository;
import project.pbo.service.KendaraanService;
import project.pbo.service.PelangganService;
import project.pbo.service.TransaksiService;
import project.pbo.service.LaporanService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    private static final Scanner input = new Scanner(System.in);
    // Inisialisasi antarmuka koneksi utama ke database PostgreSQL
    private static final DatabaseConnection databaseConnection =
        new PostgreSqlDatabaseConnection();

    // Penerapan Dependency Injection: Koneksi DB ke PenggunaRepository dan meneruskannya ke AuthService
    private static final AuthService authService =
        new AuthService(new PenggunaRepository(databaseConnection));

    // INJEKSI DEPENDENSI (DIP) - Composition Root
    // 1. Repositories
    private static final KendaraanRepository kendaraanRepo = new KendaraanRepository(databaseConnection);
    private static final PelangganRepository pelangganRepo = new PelangganRepository(databaseConnection);
    private static final TransaksiRepository transaksiRepo = new TransaksiRepository(databaseConnection);

    // 2. Services
    private static final KendaraanService kendaraanService = new KendaraanService(kendaraanRepo);
    private static final PelangganService pelangganService = new PelangganService(pelangganRepo);
    private static final TransaksiService transaksiService = new TransaksiService(transaksiRepo, kendaraanService);
    private static final LaporanService laporanService = new LaporanService(transaksiRepo);

    // 3. UIs
    private static final KendaraanUI kendaraanUI = new KendaraanUI(kendaraanService);
    private static final PelangganUI pelangganUI = new PelangganUI(pelangganService);
    private static final TransaksiUI transaksiUI = new TransaksiUI(transaksiService, kendaraanService, pelangganService);
    private static final LaporanUI laporanUI = new LaporanUI(laporanService);

    public static void main(String[] args) {

        try (Connection connection = databaseConnection.connect()) {
            System.out.println("[SUKSES] Terhubung ke PostgreSQL.");
        } catch (SQLException e) {
            System.out.println("[GAGAL] Koneksi PostgreSQL: " + e.getMessage());
            return;
        }


        // Alur berputar terus: Setelah logout otomatis kembali ke gerbang login awal (Monic)
        while (true) {
            prosesOtentikasiSistem();
        }
    }

    private static void prosesOtentikasiSistem() {
        System.out.println("========================================");
        System.out.println("  SELAMAT DATANG DI RENTAL KENDARAAN");
        System.out.println("         KELOMPOK 5 - PBO 2026");
        System.out.println("========================================");

        int percobaan = 0;
        int maksPercobaan = 3;

        // Logika batasan 3x percobaan (Neila)
        while (percobaan < maksPercobaan) {
            System.out.print("Username: > ");
            String username = input.nextLine().trim();

            System.out.print("Password: > ");
            String password = input.nextLine().trim();

            // Memanggil service otentikasi data akun.json milik Neila
            Pengguna pengguna = authService.login(username, password);

            if (pengguna != null) {
                System.out.println("[SUKSES] Login berhasil sebagai " + pengguna.getRole().toUpperCase());
                System.out.print("Tekan ENTER untuk masuk ke Dashboard...");
                input.nextLine();

                // Alur routing menu berdasarkan hak akses (Monic)
                arahkanKeDashboardRole(pengguna);
                return; // Keluar dari gerbang otentikasi karena sudah masuk menu utama
            } else {
                percobaan++;
                System.out.println("[GAGAL] Login gagal! Periksa kembali kredensial Anda.");
                System.out.println("Sisa percobaan Anda: " + (maksPercobaan - percobaan));
                System.out.println("----------------------------------------");
            }
        }

        System.out.println("[BLOCKED] Batas percobaan login (3x) telah habis. Sistem dihentikan.");
        input.close();
        System.exit(0);
    }

    // Mengarahkan objek konkrit ke logic perulangan menu masing-masing (Iman Fixed)
    private static void arahkanKeDashboardRole(Pengguna pengguna) {
        String role = pengguna.getRole().toLowerCase();

        switch (role) {
            case "admin":
                Admin admin = new Admin(pengguna.getUsername(), pengguna.getPassword(), pengguna.getRole());
                AdminMenu adminMenu = new AdminMenu(admin, kendaraanUI);
                adminMenu.prosesMenu(); 
                break;
            case "staff":
                Staff staff = new Staff(pengguna.getUsername(), pengguna.getPassword(), pengguna.getRole());
                StaffMenu staffMenu = new StaffMenu(staff, pelangganUI, kendaraanUI, transaksiUI);
                staffMenu.prosesMenu();
                break;
            case "owner":
                Owner owner = new Owner(pengguna.getUsername(), pengguna.getPassword(), pengguna.getRole());
                OwnerMenu ownerMenu = new OwnerMenu(owner, laporanUI);
                ownerMenu.prosesMenu();
                break;
            default:
                System.out.println("[ERROR] Role tidak dikenali: " + pengguna.getRole());
        }
    }
}
