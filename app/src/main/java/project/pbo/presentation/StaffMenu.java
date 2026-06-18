package project.pbo.presentation;

import java.util.List;
import java.util.Scanner;

import project.pbo.domain.Kendaraan;
import project.pbo.domain.Pelanggan;
import project.pbo.domain.Staff;
import project.pbo.domain.Transaksi;
import project.pbo.service.KendaraanService;
import project.pbo.service.PelangganService;
import project.pbo.service.TransaksiService;

public class StaffMenu {
    private char pilihanMenu;
    private final Staff staff;

    private final PelangganService pelangganService = new PelangganService();
    private final KendaraanService kendaraanService = new KendaraanService();
    private final TransaksiService transaksiService = new TransaksiService();

    public StaffMenu(Staff staff) {
        this.staff = staff;
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
                    daftarPelangganBaru();
                    break;
                case '2':
                    cariDataPelanggan();
                    break;
                case '3':
                    cekKendaraanTersedia();
                    break;
                case '4':
                    prosesPeminjaman();
                    break;
                case '5':
                    prosesPengembalian();
                    break;
                case '0':
                    System.out.println("Logout berhasil.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        } while (pilihanMenu != '0');
    }

    public void daftarPelangganBaru() {
        Scanner input = new Scanner(System.in);
        String nik = "";
        String namaPelanggan = "";
        String noTelp = "";

        System.out.println("=== MENU PENDAFTARAN PELANGGAN ===");
        while (true) {
            System.out.println("Masukkan Nomor KTP: ");
            nik = input.nextLine();
            if (!pelangganService.validasiNomorKtp(nik)) {
                continue;
            }
            if (pelangganService.validasiDataNomorKTP(nik)) {
                System.out.println("Pelanggan dengan KTP tersebut sudah terdaftar!");
                continue;
            }
            break;
        }
        System.out.println("Masukkan Nama Lengkap: ");
        namaPelanggan = input.nextLine();
        System.out.println("Masukkan No Telepon: ");
        noTelp = input.nextLine();

        pelangganService.tambahPelanggan(nik, namaPelanggan, noTelp);
        System.out.println("[SUKSES] Data pelanggan berhasil disimpan ke JSON.");
    }

    public void cariDataPelanggan() {
        Scanner input = new Scanner(System.in);
        System.out.println("=== MENU PENCARIAN PELANGGAN ===");
        System.out.println("================================");
        System.out.println("= Ketik 0 untuk Kembali ke Menu =");
        System.out.println("================================");
        System.out.println("Masukkan Nomor KTP Pelanggan: ");

        String nikCari = input.nextLine();
        if (nikCari.equals("0")) {
            return;
        }

        Pelanggan pelangganDiTemukan = pelangganService.cariByNik(nikCari);

        if (pelangganDiTemukan != null) {
            System.out.println("================================");
            System.out.println("DATA PELANGGAN DITEMUKAN!");
            System.out.println("================================");
            pelangganDiTemukan.tampilkanInfo();

        } else {
            System.out.println("================================");
            System.out.println("MAAF DATA PELANGGAN TIDAK DITEMUKAN!");
            System.out.println("================================");
        }

        System.out.println("Tekan Enter untuk Kembali ke Menu");
        input.nextLine();

    }

    public void cekKendaraanTersedia() {
        Scanner input = new Scanner(System.in);

        System.out.println("=== DAFTAR KENDARAAN YANG TERSEDIA ===");
        List<Kendaraan> listKendaraanTersedia = kendaraanService.getKendaraanTersedia();

        if (listKendaraanTersedia.isEmpty()) {
            System.out.println("Tidak ada kendaraan yang tersedia saat ini.");
        } else {
            for (Kendaraan kendaraan : listKendaraanTersedia) {
                System.out.println("======================================");
                System.out.println("Plat Nomor      :" + kendaraan.getPlatNomor());
                System.out.println("Jenis           :" + kendaraan.getJenisKendaraan());
                System.out.println("Harga sewa/hari :" + kendaraan.getHargaSewaPerHari());
                System.out.println("Status          :" + kendaraan.getStatus());
                System.out.println("======================================");
            }
        }
        System.out.println("Tekan Enter untuk Kembali ke Menu");
        input.nextLine();
    }

    public void prosesPeminjaman() {
        Scanner input = new Scanner(System.in);

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

        int durasi;
        while (true) {
            System.out.println("Masukkan Durasi Sewa (dalam hari): ");
            try {
                durasi = Integer.parseInt(input.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("[PERINGATAN] Durasi sewa harus berupa angka.");
            }
        }

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
            while (true) {
                System.out.println("\nPilih Zona Pengantaran Kendaraan:");
                System.out.println("A. Zona A (Biaya Tambahan: Rp 150.000)");
                System.out.println("B. Zona B (Biaya Tambahan: Rp 100.000)");
                System.out.println("C. Zona C (Biaya Tambahan: Rp  50.000)");
                System.out.print("Pilih Zona (A/B/C) > ");
                zonaKirim = input.nextLine().trim().toUpperCase();

                if (zonaKirim.equals("A")) {
                    biayaKirim = 150000;
                    break;
                } else if (zonaKirim.equals("B")) {
                    biayaKirim = 100000;
                    break;
                } else if (zonaKirim.equals("C")) {
                    biayaKirim = 50000;
                    break;
                }
                System.out.println("[PERINGATAN] Zona tidak valid! Harap pilih antara A, B, atau C.");
            }
        }

        Transaksi transaksiBaru = transaksiService.prosesPeminjaman(nikInput, kendaraanDitemukan, durasi, isDelivery, zonaKirim, biayaKirim);

        System.out.println("\nMemproses transaksi...");
        transaksiBaru.tampilkanStruk(pelangganDitemukan.getNamaPelanggan(), kendaraanDitemukan.getJenisKendaraan());

        System.out.println("Tekan Enter untuk Kembali ke Menu");
        input.nextLine();
    }



    public void prosesPengembalian() {
        Scanner input = new Scanner(System.in);

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
}
