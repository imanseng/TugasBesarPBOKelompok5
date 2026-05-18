package project.pbo;

import project.pbo.domain.Admin;
import project.pbo.domain.Staff;
import project.pbo.domain.Owner;
import project.pbo.domain.Pengguna;
import project.pbo.repository.PenggunaRepository;

import java.util.Scanner;

// MONIC - feat/role-menu
public class Main {

    private static final int MAKS_PERCOBAAN = 3;
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        // Setelah logout, kembali ke layar login otomatis
        while (true) {
            prosesLogin();
        }
    }

    private static void prosesLogin() {
        System.out.println("========================================");
        System.out.println("  SELAMAT DATANG DI RENTAL KENDARAAN");
        System.out.println("         KELOMPOK 5 - PBO 2026");
        System.out.println("========================================");

        PenggunaRepository repo = new PenggunaRepository();
        int sisaPercobaan = MAKS_PERCOBAAN;

        while (sisaPercobaan > 0) {
            System.out.print("Username : ");
            String username = input.nextLine().trim();

            System.out.print("Password : ");
            String password = input.nextLine().trim();

            Pengguna pengguna = repo.cariPengguna(username);

            if (pengguna != null && pengguna.cekLogin(username, password)) {
                String role = pengguna.getRole().toUpperCase();
                System.out.println("[SUKSES] Login berhasil sebagai " + role + ".");
                System.out.print("Tekan ENTER untuk masuk ke Dashboard...");
                input.nextLine();

                // Routing ke menu sesuai role
                arahkanKeMenu(pengguna);
                return;

            } else {
                sisaPercobaan--;
                if (sisaPercobaan > 0) {
                    System.out.println("[GAGAL] Username atau password salah. "
                            + "Sisa percobaan: " + sisaPercobaan);
                } else {
                    System.out.println("[GAGAL] Batas percobaan login (3x) telah habis.");
                    System.out.println("Aplikasi dihentikan.");
                    input.close();
                    System.exit(0);
                }
            }
        }
    }

    // Switch-case routing ke menu sesuai role
    private static void arahkanKeMenu(Pengguna pengguna) {
        String role = pengguna.getRole().toLowerCase();

        switch (role) {
            case "admin":
                new Admin(pengguna.getUsername(), pengguna.getPassword(), pengguna.getRole());
                break;
            case "staff":
                new Staff(pengguna.getUsername(), pengguna.getPassword(), pengguna.getRole());
                break;
            case "owner":
                new Owner(pengguna.getUsername(), pengguna.getPassword(), pengguna.getRole());
                break;
            default:
                System.out.println("[ERROR] Role tidak dikenali: " + pengguna.getRole());
        }
    }
}