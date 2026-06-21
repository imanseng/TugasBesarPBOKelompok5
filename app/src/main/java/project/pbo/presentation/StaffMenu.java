package project.pbo.presentation;

import java.util.Scanner;
import project.pbo.domain.Staff;

public class StaffMenu {
    private char pilihanMenu;
    private final Staff staff;

    private final PelangganUI pelangganUI;
    private final KendaraanUI kendaraanUI;
    private final TransaksiUI transaksiUI;

    public StaffMenu(Staff staff, PelangganUI pelangganUI, KendaraanUI kendaraanUI, TransaksiUI transaksiUI) {
        this.staff = staff;
        this.pelangganUI = pelangganUI;
        this.kendaraanUI = kendaraanUI;
        this.transaksiUI = transaksiUI;
        this.pilihanMenu = ' ';
    }

    public void prosesMenu() {
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("=== MENU STAFF ===");
            System.out.println("Selamat Datang, " + staff.getUsername());
            System.out.println("Silahkan pilih menu:");
            System.out.println("1. Daftar Pelanggan Baru");
            System.out.println("2. Cari Data Pelanggan");
            System.out.println("3. Cek Kendaraan Tersedia");
            System.out.println("4. Proses Peminjaman (Sewa)");
            System.out.println("5. Proses Pengambilan");
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
                    pelangganUI.daftarPelangganBaru(input);
                    break;
                case '2':
                    pelangganUI.cariDataPelanggan(input);
                    break;
                case '3':
                    kendaraanUI.cekKendaraanTersedia(input);
                    break;
                case '4':
                    transaksiUI.prosesPeminjaman(input);
                    break;
                case '5':
                    transaksiUI.prosesPengembalian(input);
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