package project.pbo;

import project.pbo.domain.Admin;
import project.pbo.domain.Staff;
import project.pbo.domain.Owner;
import project.pbo.domain.Pengguna;
import project.pbo.service.AuthService;
import project.pbo.presentation.AdminMenu;
import project.pbo.presentation.StaffMenu;

import java.util.Scanner;

public class App {
    private static final Scanner input = new Scanner(System.in);
    private static final AuthService authService = new AuthService();

    public static void main(String[] args) {
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
                AdminMenu adminMenu = new AdminMenu(admin);
                adminMenu.prosesMenu(); 
                break;
            case "staff":
                Staff staff = new Staff(pengguna.getUsername(), pengguna.getPassword(), pengguna.getRole());
                StaffMenu staffMenu = new StaffMenu(staff);
                staffMenu.prosesMenu();
                break;
            case "owner":
                Owner owner = new Owner(pengguna.getUsername(), pengguna.getPassword(), pengguna.getRole());
                owner.prosesMenu();
                break;
            default:
                System.out.println("[ERROR] Role tidak dikenali: " + pengguna.getRole());
        }
    }
}
