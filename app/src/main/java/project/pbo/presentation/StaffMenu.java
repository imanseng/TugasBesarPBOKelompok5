package project.pbo.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import project.pbo.domain.Kendaraan;
import project.pbo.domain.Pelanggan;
import project.pbo.domain.Staff;
import project.pbo.domain.Transaksi;
import project.pbo.repository.KendaraanRepository;
import project.pbo.repository.PelangganRepository;
import project.pbo.repository.TransaksiRepository;

public class StaffMenu {
    private char pilihanMenu;
    private final Staff staff;

    private final PelangganRepository pelangganRepo = new PelangganRepository();
    private final KendaraanRepository kendaraanRepo = new KendaraanRepository();
    private final TransaksiRepository transaksiRepo = new TransaksiRepository();

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
            if (!validasiNomorKtp(nik)) {
                continue;
            }
            if (validasiDataNomorKTP(nik)) {
                System.out.println("Pelanggan dengan KTP tersebut sudah terdaftar!");
                continue;
            }
            break;
        }
        System.out.println("Masukkan Nama Lengkap: ");
        namaPelanggan = input.nextLine();
        System.out.println("Masukkan No Telepon: ");
        noTelp = input.nextLine();

        Pelanggan pelangganBaru = new Pelanggan(nik, namaPelanggan, noTelp);
        pelangganRepo.save(pelangganBaru);
        System.out.println("[SUKSES] Data pelanggan berhasil disimpan ke JSON.");
    }

    public boolean validasiNomorKtp(String nik) {
        if (nik.trim().isEmpty()) {
            System.out.println("Nomor KTP tidak boleh kosong!");
            return false;
        }
        if (!nik.matches("\\d{16}")) {
            System.out.println("Nomor KTP harus diisi 16 angka!");
            return false;
        }
        return true;
    }

    public boolean validasiDataNomorKTP(String nik) {
        List<Pelanggan> listPelanggan = pelangganRepo.loadAll();
        for (Pelanggan list : listPelanggan) {
            if (list.getNik().equals(nik)) {
                return true;
            }
        }
        return false;
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

        List<Pelanggan> listPelanggan = pelangganRepo.loadAll();
        Pelanggan pelangganDiTemukan = null;

        for (Pelanggan pelanggan : listPelanggan) {
            if (pelanggan.getNik().equals(nikCari)) {
                pelangganDiTemukan = pelanggan;
                break;
            }
        }

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

        List<Kendaraan> listKendaraan = kendaraanRepo.loadAll();
        System.out.println("=== DAFTAR KENDARAAN YANG TERSEDIA ===");
        List<Kendaraan> listKendaraanTersedia = new ArrayList<>();

        for (Kendaraan kendaraan : listKendaraan) {
            if (kendaraan.getStatus().equalsIgnoreCase("Tersedia")) {
                listKendaraanTersedia.add(kendaraan);
            }
        }

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

        List<Pelanggan> listPelanggan = pelangganRepo.loadAll();
        Pelanggan pelangganDitemukan = null;

        for (Pelanggan pelanggan : listPelanggan) {
            if (pelanggan.getNik().equals(nikInput)) {
                pelangganDitemukan = pelanggan;
                break;
            }
        }

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

        List<Kendaraan> listKendaraan = kendaraanRepo.loadAll();
        Kendaraan kendaraanDitemukan = null;

        for (int i = 0; i < listKendaraan.size(); i++) {
            Kendaraan k = listKendaraan.get(i);
            if (k.getPlatNomor().equalsIgnoreCase(platNomorInput)) {
                if (k.getStatus().equalsIgnoreCase("TERSEDIA")) {
                    kendaraanDitemukan = k;
                } else {
                    System.out.println("[GAGAL] Kendaraan sedang disewa!");
                    return;
                }
                break;
            }
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

        double totalBayar = (durasi * kendaraanDitemukan.getHargaSewaPerHari()) + biayaKirim;

        List<Transaksi> listTransaksi = transaksiRepo.findAll();
        String idTransaksi = generateIdTransaksi(listTransaksi);

        Transaksi transaksiBaru = new Transaksi(idTransaksi, nikInput, platNomorInput, durasi, totalBayar, "AKTIF", isDelivery, zonaKirim);

        kendaraanDitemukan.setStatus("SEDANG DISEWA");

        transaksiRepo.tambah(transaksiBaru);
        kendaraanRepo.saveAll(listKendaraan);

        System.out.println("\nMemproses transaksi...");
        transaksiBaru.tampilkanStruk(pelangganDitemukan.getNamaPelanggan(), kendaraanDitemukan.getJenisKendaraan());

        System.out.println("Tekan Enter untuk Kembali ke Menu");
        input.nextLine();
    }

    private String generateIdTransaksi(List<Transaksi> listTransaksi) {
        int maxNum = 0;

        for (Transaksi t : listTransaksi) {
            if (t.getIdTransaksi() != null && t.getIdTransaksi().startsWith("TRX-")) {
                try {
                    String numberStr = t.getIdTransaksi().substring(4);
                    int number = Integer.parseInt(numberStr);
                    if (number > maxNum) {
                        maxNum = number;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        return String.format("TRX-%03d", maxNum + 1);
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

        List<Transaksi> listTransaksi = transaksiRepo.findAll();
        Transaksi transaksiDitemukan = null;
        
        for (Transaksi transaksi : listTransaksi) {
            boolean idCocok = transaksi.getIdTransaksi() != null && transaksi.getIdTransaksi().equalsIgnoreCase(keyword);
            boolean platCocok = transaksi.getPlatNomor() != null && transaksi.getPlatNomor().equalsIgnoreCase(keyword);

            if ((idCocok || platCocok) && "AKTIF".equalsIgnoreCase(transaksi.getStatus())) {
                transaksiDitemukan = transaksi;
                break;
            }
        }

        if (transaksiDitemukan == null) {
            System.out.println("[GAGAL] Transaksi aktif tidak ditemukan.");
            System.out.println("Tekan Enter untuk Kembali ke Menu");
            input.nextLine();
            return;
        }

        List<Kendaraan> listKendaraan = kendaraanRepo.loadAll();
        Kendaraan kendaraanDitemukan = null;
        for (Kendaraan kendaraan : listKendaraan) {
            if (kendaraan.getPlatNomor().equalsIgnoreCase(transaksiDitemukan.getPlatNomor())) {
                kendaraanDitemukan = kendaraan;
                break;
            }
        }

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

        double biayaDasar = kendaraanDitemukan.getHargaSewaPerHari() * transaksiDitemukan.getDurasiHari();

        double biayaKirim = 0;
        if (transaksiDitemukan.isDelivery()) {
            String zonaKirim = transaksiDitemukan.getZonaKirim();
            if ("A".equalsIgnoreCase(zonaKirim)) {
                biayaKirim = 150000;
            } else if ("B".equalsIgnoreCase(zonaKirim)) {
                biayaKirim = 100000;
            } else if ("C".equalsIgnoreCase(zonaKirim)) {
                biayaKirim = 50000;
            }
        }

        double denda = kendaraanDitemukan.hitungDenda(hariTerlambat);
        double totalBayar = biayaDasar + biayaKirim + denda;

        transaksiDitemukan.setTotalBayar(totalBayar);
        transaksiDitemukan.setStatus("SELESAI");
        kendaraanDitemukan.setStatus("Tersedia");

        transaksiRepo.saveAll(listTransaksi);
        kendaraanRepo.saveAll(listKendaraan);

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
