package project.pbo.domain;

import java.util.Scanner;

public class Owner extends Pengguna {
    private char pilihanMenu;
    // IMAN - Perbaikan Konstruktor
    public Owner(String username, String password, String role) {
        super(username, password, role);
        this.pilihanMenu = ' ';
        prosesMenu();
    }

    // BELUM DI IMPLEMENT
    public void prosesMenu() {
         Scanner input = new Scanner(System.in);
        do {
            System.out.println("=== MENU STAFF ===");
            System.out.println("Selamat Datang, " + super.getUsername());
            System.out.println("Silahkan pilih menu:");
            System.out.println("1. Daftar Pelanggan Baru");
            System.out.println("0. Logout");
            System.out.println("\nPilihan Anda > ");

            pilihanMenu = input.nextLine().charAt(0);

            switch (pilihanMenu) {
                case '1':
                    System.out.println("Fitur lihat laporan pendapatan dan riwayat");
                    break;
                case '0':
                    System.out.println("Logout berhasil.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        } while (pilihanMenu != '0');

    }
    public void riwayatTransaksi() {}
}
