package project.pbo.presentation;

import java.util.List;
import java.util.Scanner;

import project.pbo.domain.Owner;
import project.pbo.domain.Transaksi;
import project.pbo.service.LaporanService;

public class OwnerMenu {
    private char pilihanMenu;
    private final Owner owner;
    private final LaporanService laporanService = new LaporanService();

    public OwnerMenu(Owner owner) {
        this.owner = owner;
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
                    riwayatTransaksi();
                    break;
                case '0':
                    System.out.println("Logout berhasil.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        } while (pilihanMenu != '0');

    }

    public void riwayatTransaksi() {
        Scanner input = new Scanner(System.in);
        List<Transaksi> riwayat = laporanService.getRiwayatTransaksi();

        System.out.println("\n=== LAPORAN PENDAPATAN DAN RIWAYAT TRANSAKSI ===");

        if (riwayat.isEmpty()) {
            System.out.println("Belum ada transaksi.");
        } else {
            for (Transaksi transaksi : riwayat) {
                System.out.println("----------------------------------");
                System.out.println("ID Transaksi : " + transaksi.getIdTransaksi());
                System.out.println("NIK          : " + transaksi.getNikPelanggan());
                System.out.println("Plat Nomor   : " + transaksi.getPlatNomor());
                System.out.println("Durasi       : " + transaksi.getDurasiHari() + " Hari");
                System.out.println("Total Bayar  : Rp " + String.format("%,.0f", transaksi.getTotalBayar()));
                System.out.println("Status       : " + transaksi.getStatus());
            }
            System.out.println("----------------------------------");
        }

        System.out.println("Total Pendapatan dari Transaksi Selesai: Rp "
                + String.format("%,.0f", laporanService.hitungTotalPendapatan()));

        System.out.println("\nTekan ENTER untuk kembali ke menu utama...");
        input.nextLine();
    }
}
