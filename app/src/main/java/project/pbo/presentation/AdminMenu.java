package project.pbo.presentation;

import project.pbo.domain.Admin;
import java.util.Scanner;

public class AdminMenu {
    private char pilihanMenu;
    private final Admin admin;
    private final KendaraanUI kendaraanUI;

    public AdminMenu(Admin admin, KendaraanUI kendaraanUI) {
        this.admin = admin;
        this.kendaraanUI = kendaraanUI;
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
                    kendaraanUI.tambahKendaraan(input);
                    break;
                case '2':
                    kendaraanUI.lihatDaftarKendaraan(input);
                    break;
                case '3':
                    kendaraanUI.hapusKendaraan(input);
                    break;
                case '0':
                    System.out.println("Logout berhasil.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        } while (pilihanMenu != '0');
    }
}
