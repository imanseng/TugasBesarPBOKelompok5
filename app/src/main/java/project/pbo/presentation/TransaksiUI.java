package project.pbo.presentation;

import project.pbo.domain.Kendaraan;
import project.pbo.domain.Pelanggan;
import project.pbo.domain.Transaksi;
import project.pbo.service.KendaraanService;
import project.pbo.service.PelangganService;
import project.pbo.service.TransaksiService;

import java.util.Scanner;

public class TransaksiUI {
    private final TransaksiService transaksiService;
    private final KendaraanService kendaraanService;
    private final PelangganService pelangganService;

    public TransaksiUI(TransaksiService transaksiService, KendaraanService kendaraanService, PelangganService pelangganService) {
        this.transaksiService = transaksiService;
        this.kendaraanService = kendaraanService;
        this.pelangganService = pelangganService;
    }

    public void prosesPeminjaman(Scanner input) {
        System.out.println("=== MENU PEMINJAMAN KENDARAAN ===");
        System.out.println("================================");
        System.out.println("= Ketik 0 untuk Kembali ke Menu =");
        System.out.println("================================");

        System.out.println("Masukkan Nomor KTP Pelanggan: ");
        String nikInput = input.nextLine().trim();

        if (nikInput.equals("0")) {
            return;
        }

        Pelanggan pelangganDitemukan = pelangganService.cariByNik(nikInput);

        if (pelangganDitemukan == null) {
            System.out.println("[GAGAL] Nomor KTP tidak terdaftar!");
            System.out.println("Tekan Enter untuk Kembali ke Menu");
            input.nextLine();
            return;
        }

        System.out.println("Masukkan Plat Nomor Kendaraan: ");
        String platNomorInput = input.nextLine().trim().toUpperCase();

        if (platNomorInput.equals("0")) {
            return;
        }

        Kendaraan kendaraanDitemukan = kendaraanService.cariKendaraanByPlat(platNomorInput);

        if (kendaraanDitemukan != null && !kendaraanDitemukan.getStatus().equalsIgnoreCase("TERSEDIA")) {
            System.out.println("[GAGAL] Kendaraan sedang disewa!");
            return;
        }

        if (kendaraanDitemukan == null) {
            System.out.println("[GAGAL] Plat Nomor Kendaraan tidak terdaftar!");
            System.out.println("Tekan Enter untuk Kembali ke Menu");
            input.nextLine();
            return;
        }

        int durasi = inputDurasiSewa(input);
        if (durasi <= 0) {
            System.out.println("[GAGAL] Durasi sewa tidak valid!");
            System.out.println("Tekan Enter untuk Kembali ke Menu");
            input.nextLine();
            return;
        }

        boolean isDelivery = false;
        String zonaKirim = "-";
        double biayaKirim = 0;

        while (true) {
            System.out.println("\nPilih Opsi Layanan Pengambilan Kendaraan:");
            System.out.println("1. Ambil di Kantor (Gratis)");
            System.out.println("2. Antar ke Lokasi Pelanggan (Ada Biaya Tambahan Zona)");
            System.out.print("Pilihan Layanan > ");
            String opsi = input.nextLine().trim();

            if (opsi.equals("1")) {
                break;
            } else if (opsi.equals("2")) {
                isDelivery = true;
                break;
            }
            System.out.println("[PERINGATAN] Pilihan tidak valid! Masukkan angka 1 atau 2.");
        }

        if (isDelivery) {
            zonaKirim = pilihZonaKirim(input);
            biayaKirim = transaksiService.hitungBiayaKirimBerdasarkanZona(zonaKirim);
        }

        Transaksi transaksiBaru = transaksiService.prosesPeminjaman(nikInput, kendaraanDitemukan, durasi, isDelivery, zonaKirim, biayaKirim);

        System.out.println("\nMemproses transaksi...");
        tampilkanStrukPeminjaman(transaksiBaru, pelangganDitemukan.getNamaPelanggan(), kendaraanDitemukan.getJenisKendaraan());

        System.out.println("Tekan Enter untuk Kembali ke Menu");
        input.nextLine();
    }

    public void prosesPengembalian(Scanner input) {
        System.out.println("=== MENU PENGEMBALIAN KENDARAAN ===");
        System.out.println("===================================");
        System.out.println("= Ketik 0 untuk Kembali ke Menu    =");
        System.out.println("===================================");
        System.out.print("Masukkan ID Transaksi atau Plat Nomor Kendaraan: ");
        String keyword = input.nextLine().trim();

        if (keyword.equals("0")) {
            return;
        }

        Transaksi transaksiDitemukan = transaksiService.cariTransaksiAktif(keyword);

        if (transaksiDitemukan == null) {
            System.out.println("[GAGAL] Transaksi aktif tidak ditemukan.");
            System.out.println("Tekan Enter untuk Kembali ke Menu");
            input.nextLine();
            return;
        }

        Kendaraan kendaraanDitemukan = kendaraanService.cariKendaraanByPlat(transaksiDitemukan.getPlatNomor());

        if (kendaraanDitemukan == null) {
            System.out.println("[GAGAL] Data kendaraan untuk transaksi ini tidak ditemukan.");
            System.out.println("Tekan Enter untuk Kembali ke Menu");
            input.nextLine();
            return;
        }
        
        int hariTerlambat;
        while (true) {
            System.out.print("Masukkan hari keterlambatan pengembalian (0 jika tepat waktu): ");
            try {
                hariTerlambat = Integer.parseInt(input.nextLine().trim());
                if (hariTerlambat < 0) {
                    System.out.println("[PERINGATAN] Hari keterlambatan tidak boleh negatif.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("[PERINGATAN] Input harus berupa angka.");
            }
        }

        double biayaDasar = transaksiService.hitungBiayaDasar(transaksiDitemukan, kendaraanDitemukan);
        double biayaKirim = transaksiService.hitungBiayaKirim(transaksiDitemukan);
        double denda = kendaraanDitemukan.hitungDenda(hariTerlambat);
        double totalBayar = biayaDasar + biayaKirim + denda;

        transaksiService.selesaikanPengembalian(transaksiDitemukan, kendaraanDitemukan, totalBayar);

        System.out.println("\n=== STRUK TAGIHAN AKHIR ===");
        System.out.println("ID Transaksi       : " + transaksiDitemukan.getIdTransaksi());
        System.out.println("Plat Nomor         : " + transaksiDitemukan.getPlatNomor());
        System.out.println("Jenis Kendaraan    : " + kendaraanDitemukan.getJenisKendaraan());
        System.out.println("Durasi Sewa        : " + transaksiDitemukan.getDurasiHari() + " Hari");
        System.out.println("Harga Sewa / Hari  : Rp " + kendaraanDitemukan.getHargaSewaPerHari());
        System.out.println("Biaya Dasar        : Rp " + biayaDasar);
        System.out.println("Biaya Delivery     : Rp " + biayaKirim);
        System.out.println("Hari Terlambat     : " + hariTerlambat + " Hari");
        System.out.println("Denda              : Rp " + denda);
        System.out.println("Total Bayar        : Rp " + totalBayar);
        System.out.println("==========================");
        System.out.println("[SUKSES] Pengembalian berhasil. Status kendaraan berubah menjadi Tersedia.");

        System.out.println("Tekan Enter untuk Kembali ke Menu");
        input.nextLine();
    }

    private void tampilkanStrukPeminjaman(Transaksi t, String namaPelanggan, String jenisKendaraan) {
        System.out.println("\n--- STRUK PEMINJAMAN SEMENTARA ---");
        System.out.println("ID Transaksi   : " + t.getIdTransaksi());
        System.out.println("Nama Pelanggan : " + namaPelanggan);
        System.out.println("Kendaraan      : " + jenisKendaraan + " (" + t.getPlatNomor() + ")");
        System.out.println("Durasi Sewa    : " + t.getDurasiHari() + " Hari");
        System.out.println("Estimasi Biaya : Rp " + String.format("%,.0f", t.getTotalBayar()));
        System.out.println("----------------------------------");
        System.out.println("[SUKSES] Transaksi berhasil dicatat. Status kendaraan berubah menjadi SEDANG DISEWA.");
    }

    private int inputDurasiSewa(Scanner input) {
        while (true) {
            System.out.println("Masukkan Durasi Sewa (dalam hari): ");
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[PERINGATAN] Durasi sewa harus berupa angka.");
            }
        }
    }

    private String pilihZonaKirim(Scanner input) {
        while (true) {
            System.out.println("\nPilih Zona Pengantaran Kendaraan:");
            System.out.println("A. Zona A (Biaya Tambahan: Rp 150.000)");
            System.out.println("B. Zona B (Biaya Tambahan: Rp 100.000)");
            System.out.println("C. Zona C (Biaya Tambahan: Rp  50.000)");
            System.out.print("Pilih Zona (A/B/C) > ");
            String zona = input.nextLine().trim().toUpperCase();
            if (zona.equals("A") || zona.equals("B") || zona.equals("C")) {
                return zona;
            }
            System.out.println("[PERINGATAN] Zona tidak valid! Harap pilih antara A, B, atau C.");
        }
    }
}
