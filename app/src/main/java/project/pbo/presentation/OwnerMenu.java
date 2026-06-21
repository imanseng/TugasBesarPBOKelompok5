package project.pbo.presentation;

import java.util.Scanner;
import project.pbo.domain.Owner;

public class OwnerMenu {
    private char pilihanMenu;
    private final Owner owner;
    private final LaporanUI laporanUI;

    public OwnerMenu(Owner owner, LaporanUI laporanUI) {
        this.owner = owner;
        this.laporanUI = laporanUI;
        this.pilihanMenu = ' ';
    }

    public void prosesMenu() {
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("=== MENU OWNER ===");
            System.out.println("Selamat Datang, " + owner.getUsername());
            System.out.println("Silahkan pilih menu:");
            System.out.println("1. Lihat Laporan Pendapatan dan Riwayat Transaksi");
            System.out.println("0. Logout");
            System.out.println("\nPilihan Anda > ");

            String userInput = input.nextLine();
            if (userInput.isEmpty()) {
                pilihanMenu = ' ';
            } else {
                pilihanMenu = userInput.charAt(0);
            }

            switch (pilihanMenu) {
                case '1':
                    laporanUI.riwayatTransaksi(input);
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