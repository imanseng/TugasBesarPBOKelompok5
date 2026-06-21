package project.pbo.presentation;

import java.util.List;
import java.util.Scanner;

import project.pbo.domain.Transaksi;
import project.pbo.service.LaporanService;

public class LaporanUI {
    private final LaporanService laporanService;

    public LaporanUI(LaporanService laporanService) {
        this.laporanService = laporanService;
    }

    public void riwayatTransaksi(Scanner input) {
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
