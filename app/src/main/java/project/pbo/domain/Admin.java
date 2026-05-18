package project.pbo.domain;

import project.pbo.repository.KendaraanRepository; // Ditambahkan untuk akses JSON
import java.util.List; // Ditambahkan untuk List database
import java.util.Scanner;

public class Admin extends Pengguna {
    private char pilihanMenu;
    private final KendaraanRepository repo = new KendaraanRepository(); // Instansiasi repositori data

    // IMAN Perbaikan Konstruktor, meneruskan data ke superclass Pengguna
    public Admin(String username, String password, String role) {
        super(username, password, role);
        this.pilihanMenu = ' ';
    }

    // IMAN - Menjalankan Menu
    public void prosesMenu() {
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("=== MENU ADMIN ===");
            System.out.println("Selamat Datang, " + super.getUsername());
            System.out.println("Silahkan pilih menu:");
            System.out.println("1. Tambah Kendaraan Baru");
            System.out.println("2. Lihat Semua Kendaraan");
            System.out.println("3. Hapus Kendaraan");
            System.out.println("0. Logout");
            System.out.print("\nPilihan Anda > ");

            // - IMAN Memasukkan pilihan menu
            pilihanMenu = input.nextLine().charAt(0);

            // IMAN - Mengeksekusi menu sesuai pilihan
            switch (pilihanMenu) {
                case '1':
                    tambahKendaraan();
                    break;
                case '2':
                    lihatDaftarKendaraan();
                    break;
                case '3':
                    System.out.println("Fitur hapus kendaraan (Monic/Robby)."); // UBAH/HAPUS NANTI
                    break;
                case '0':
                    System.out.println("Logout berhasil.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        } while (pilihanMenu != '0');
        input.close();
    }

    // IMAN - Menambah Kendaraan (feat/add-vehicle)
    public void tambahKendaraan() {
        Scanner input = new Scanner(System.in);
        int jenis = -1;

        String platNomor = "";
        double hargaSewa = 0;

        // Membaca list database dari file JSON untuk validasi plat unik
        List<Kendaraan> listKendaraan = repo.loadAll();

        // Input wajib Plat Nomor, Harga Sewa per Hari, dan Jenis Kendaraan (Mobil/Motor).
        do {
            System.out.println("=== TAMBAH KENDARAAN ===");
            System.out.print("\nMasukkan Plat Nomor Kendaraan: ");
            platNomor = input.nextLine().trim().toUpperCase();

            // Validasi Keunikan Plat Nomor (Menolak Data Duplikat)
            boolean isDuplikat = false;
            for (Kendaraan k : listKendaraan) {
                if (k.getPlatNomor().equalsIgnoreCase(platNomor)) {
                    isDuplikat = true;
                    break;
                }
            }

            if (isDuplikat) {
                System.out.println("[GAGAL] Plat Nomor " + platNomor + " sudah terdaftar di sistem!");
                continue; // Melempar alur kembali ke input plat nomor teratas
            }

            System.out.print("Masukkan Harga Sewa per Hari: ");
            hargaSewa = input.nextDouble();
            input.nextLine(); // Pembersih buffer scanner

            System.out.println("Silahkan pilih jenis kendaraan:");
            System.out.println("1. Mobil");
            System.out.println("2. Motor");
            System.out.println("0. Batalkan proses");
            System.out.print("\nPilihan Anda > ");
            jenis = input.nextInt();
            input.nextLine(); // Pembersih buffer scanner

            if (jenis < 0 || jenis > 2) {
                System.out.println("Pilihan tidak valid!");
            }
        } while (jenis < 0 || jenis > 2);

        // Catatan: Baris input.close() dihapus agar stream console tidak mati

        if (jenis == 0) {
            System.out.println("Proses penambahan dibatalkan.");
            return;
        }

        Kendaraan kendaraanBaru = null;

        if (jenis == 1) {
            // Instansiasi objek Mobil dengan nilai default pintu = 0 di awal
            kendaraanBaru = new Mobil(platNomor, hargaSewa, "Mobil", 0);
            // Mengisi jumlah pintu langsung via method milik Mobil
            kendaraanBaru.inputSpesifik(input);
            System.out.println("[DEBUG LOG] Objek mobil berhasil dibuat dengan aman.");
            
        } else if (jenis == 2) {
            // Instansiasi objek Motor dengan nilai default transmisi = "" di awal
            kendaraanBaru = new Motor(platNomor, hargaSewa, "Motor", "");
            // Mengisi jenis transmisi langsung via method milik Motor
            kendaraanBaru.inputSpesifik(input);
            System.out.println("[DEBUG LOG] Objek motor berhasil dibuat dengan aman.");
        }

        // Menyimpan objek baru ke database JSON secara permanen
        if (kendaraanBaru != null) {
            listKendaraan.add(kendaraanBaru);
            repo.saveAll(listKendaraan);
            System.out.println("[SUKSES] Data kendaraan berhasil disimpan ke json. Status default: TERSEDIA.");
        }
    }

    // ROBBY - Menu Melihat Daftar Kendaraan (feat/list-vehicle)
    public void lihatDaftarKendaraan(){

        // List data kendaraan
        List<Kendaraan> listKendaraan = repo.loadAll();
        Scanner input = new Scanner(System.in);

        System.out.println("\n============================================================");
        System.out.println("                   DAFTAR SELURUH KENDARAAN");
        System.out.println("============================================================");

        // Cek apakah data kosong
        if (listKendaraan == null || listKendaraan.isEmpty()) {
            System.out.println("Data kendaraan masih kosong.");
        } else {
            // Header tabel
            System.out.println("| Plat Nomor | Jenis | Harga/Hari | Info Tambahan | Status        |");
            System.out.println("--------------------------------------------------------------------------------");

            // Isi tabel
            for (Kendaraan k : listKendaraan) {
                // Catatan: Output Merek belum ditambahkan karena kendaraan belum memiliki atribut merek
                String platNomor = k.getPlatNomor();
                String jenis = k.getJenisKendaraan();
                String harga = "Rp " + String.format("%,.0f", k.getHargaSewaPerHari());
                String status = k.getStatus();
                String infoTambahan = "-";

                // Cek tipe kendaraan untuk info tambahan
                if (k instanceof Mobil) {
                    Mobil mobil = (Mobil) k;
                    infoTambahan = mobil.getJumlahPintu() + " pintu"; // Menampilkan jumlah pintu mobil
                    // Catatan: pada UI yang diharapkan terdapat info tambahan berupa transmisi, namun mobil tidak memiliki atribut transmisi
                } else if (k instanceof Motor) {
                    Motor motor = (Motor) k;
                    infoTambahan = motor.getJenisTransmisi(); // Menampilkan jenis transmisi motor
                }

                // Format baris tabel
                System.out.printf("| %-10s | %-5s | %-10s | %-13s | %-13s |%n", 
                                    platNomor, jenis, harga, infoTambahan, status);
            }
        }
        System.out.println("\nTekan ENTER untuk kembali ke menu utama...");
        input.nextLine(); 
    }
}
