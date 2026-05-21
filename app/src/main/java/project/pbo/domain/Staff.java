package project.pbo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import project.pbo.repository.KendaraanRepository;
import project.pbo.repository.PelangganRepository;
import project.pbo.repository.TransaksiRepository;

public class Staff extends Pengguna {
    private char pilihanMenu;
    private String nik;
    public String namaPelanggan;
    private String noTelp;

    private PelangganRepository pelangganRepo = new PelangganRepository();
    private KendaraanRepository kendaraanRepo = new KendaraanRepository();
    private TransaksiRepository transaksiRepo = new TransaksiRepository();

    // IMAN - Perbaikan konstruktor
    public Staff(String username, String password, String role) {
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
            System.out.println("2. Cari Data Pelanggan");
            System.out.println("3. Cek Kendaraan Tersedia");
            System.out.println("Proses Peminjaman (Sewa)");
            System.out.println("5. Proses Pengambilan");
            System.out.println("0. Logout");
            System.out.println("\nPilihan Anda > ");

            pilihanMenu = input.nextLine().charAt(0);

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
                    System.out.println("Fitur pengembalian."); // UBAH/HAPUS NANTI
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
        System.out.println("=== MENU PENDAFTARAN PELANGGAN ===");
        while (true) {
            System.out.println("Masukkan Nomor KTP: ");
            nik = input.nextLine();
            if (!validasiNomorKtp(nik)) {
                continue;
            }
            if (validasiDataNomorKTP(nik)) {
                System.out.println("Pelanggan dengan KTP tersebut sudah terdaftar!");
            }
            break;
        }
        System.out.println("Masukkan Nama Lengkap: ");
        namaPelanggan = input.nextLine();
        System.out.println("Masukkan No Telepon: ");
        noTelp = input.nextLine();

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

    // task Fatin - cari data pelanggan
    public void cariDataPelanggan() {
        Scanner input = new Scanner(System.in);
        System.out.println("=== MENU PENCARIAN PELANGGAN ===");
        System.out.println("================================");
        System.out.println("= Ketik 0 untuk Kembali ke Menu =");
        System.out.println("================================");
        System.out.println("Masukkan Nomor KTP Pelanggan: ");

        String nikCari = input.nextLine();
        if (nikCari.equals("0")) {
            return; // kembali ke menu utama
        }

        List<Pelanggan> listPelanggan = pelangganRepo.loadAll();
        Pelanggan pelangganDiTemukan = null; // jika pelanggan belum ditemukan

        for (Pelanggan pelanggan : listPelanggan) {
            if (pelanggan.getNik().equals(nikCari)) {
                pelangganDiTemukan = pelanggan;
                break; // keluar dari loop jika sudah ditemukan
            }
        }

        if (pelangganDiTemukan != null) {
            System.out.println("================================");
            System.out.println("DATA PELANGGAN DITEMUKAN!");
            System.out.println("================================");
            pelangganDiTemukan.tampilkanInfo();// tampilkan data pelanggan yang ditemukan

        } else {
            System.out.println("================================");
            System.out.println("MAAF DATA PELANGGAN TIDAK DITEMUKAN!");
            System.out.println("================================");
        }

        System.out.println("Tekan Enter untuk Kembali ke Menu");
        input.nextLine();

    }

    // Task Fatin - Cek Kendaraan Tersedia
    public void cekKendaraanTersedia() { // tugas Fatin
        Scanner input = new Scanner(System.in);

        // mengambil semua data yang ada di kendaraan.json
        List<Kendaraan> listKendaraan = kendaraanRepo.loadAll();
        System.out.println("=== DAFTAR KENDARAAN YANG TERSEDIA ===");
        // membuat list baru, untuk kendaraan yang statusnya tersedia
        List<Kendaraan> listKendaraanTersedia = new ArrayList<>();

        // loop kendaraan, untuk memfilter yang statusnya tersedia saja
        for (Kendaraan kendaraan : listKendaraan) {
            if (kendaraan.getStatus().equalsIgnoreCase("Tersedia")) {
                listKendaraanTersedia.add(kendaraan); // masukkan ke list yang tersedia
                // untuk yang sedang disewa akan otomatis dilewati alias tak masuk ke dalam list
            }
        }

        // cek apakah ada kendaraan yang tersedia
        if (listKendaraanTersedia.isEmpty()) {
            System.out.println("Tidak ada kendaraan yang tersedia saat ini.");
        } else {
            for (Kendaraan kendaraan : listKendaraanTersedia) {
                System.out.println("======================================");
                // tampilaka kendaraan yang tersedia
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

        // Input nomor KTP
        System.out.println("Masukkan Nomor KTP Pelanggan: ");
        String nikInput = input.nextLine().trim(); // menghilangkan spasi di awal dan akhir untuk mencegah kesalahan
                                                   // input

        if (nikInput.equals("0")) {
            return; // kembali ke menu
        }

        // Validasi KTP terdaftar
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

        // Input plat nomor
        System.out.println("Masukkan Plat Nomor Kendaraan: ");
        String platNomorInput = input.nextLine().trim().toUpperCase();

        if (platNomorInput.equals("0")) {
            return; // kembali ke menu
        }

        // Validasi kendaraan tersedia
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

        // Input durasi sewa
        System.out.println("Masukkan Durasi Sewa (dalam hari): ");
        int durasi = input.nextInt();
        input.nextLine();

        if (durasi <= 0) {
            System.out.println("[GAGAL] Durasi sewa tidak valid!");
            System.out.println("Tekan Enter untuk Kembali ke Menu");
            input.nextLine();
            return;
        }

         // IMAN - IMPLEMENTASI AC 1 (Opsi Pengantaran)
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
                break; // Keluar dari perulangan, peminjaman normal di kantor
            } else if (opsi.equals("2")) {
                isDelivery = true;
                break;
            }
            System.out.println("[PERINGATAN] Pilihan tidak valid! Masukkan angka 1 atau 2.");
        }

        // IMAN - IMPLEMENTASI AC 2 & AC 3 (Pilihan Zona & Biaya Otomatis)
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

        // IMAN - Perbaikan Rumus Akumulasi Biaya Kirim (AC 4)
        double totalBayar = (durasi * kendaraanDitemukan.getHargaSewaPerHari()) + biayaKirim;

        // Generate ID Transaksi
        List<Transaksi> listTransaksi = transaksiRepo.findAll();
        String idTransaksi = generateIdTransaksi(listTransaksi);

        // Buat objek transaksi
        // IMAN - Integrasi variabel kelompok 5 ke dalam konstruktor Transaksi (AC 4)
        Transaksi transaksiBaru = new Transaksi(idTransaksi, nikInput, platNomorInput, durasi, totalBayar, "AKTIF", isDelivery, zonaKirim);


        // Update status
        kendaraanDitemukan.setStatus("SEDANG DISEWA");

        // Simpan ke JSON
        transaksiRepo.tambah(transaksiBaru);
        kendaraanRepo.saveAll(listKendaraan);

        // Tampilkan struk
        System.out.println("\nMemproses transaksi...");
        transaksiBaru.tampilkanStruk(pelangganDitemukan.getNamaPelanggan(), kendaraanDitemukan.getJenisKendaraan());

        System.out.println("Tekan Enter untuk Kembali ke Menu");
        input.nextLine();
    }

    // Generator ID transaksi unik
    private String generateIdTransaksi(List<Transaksi> listTransaksi) {
        int maxNum = 0;

        for (Transaksi t : listTransaksi) {
            if (t.getIdTransaksi() != null && t.getIdTransaksi().startsWith("TRX-")) {
                try {
                    String numberStr = t.getIdTransaksi().substring(4); // Mengambil angka setelah "TRX-"
                    int number = Integer.parseInt(numberStr);
                    if (number > maxNum) {
                        maxNum = number;
                    }
                } catch (NumberFormatException e) {
                    // Skip jika format ID transaksi tidak valid
                }
            }
        }
        return String.format("TRX-%03d", maxNum + 1);
    }

    public void prosesPengembalian() {
    }
}
