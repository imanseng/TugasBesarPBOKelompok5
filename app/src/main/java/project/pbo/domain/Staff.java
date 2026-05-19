package project.pbo.domain;

import project.pbo.repository.KendaraanRepository;
import project.pbo.repository.PelangganRepository;
import project.pbo.domain.Pelanggan;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Staff extends Pengguna {
    private char pilihanMenu;
    private String nik;
    public String namaPelanggan;
    private String noTelp;

    private PelangganRepository pelangganRepo = new PelangganRepository();
    private KendaraanRepository kendaraanRepo = new KendaraanRepository();

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
                    System.out.println("Fitur cari data pelanggan."); // UBAH/HAPUS NANTI
                    break;
                case '3':
                    cekKendaraanTersedia(); 
                    break;
                case '4':
                    System.out.println("Fitur peminjaman (sewa)."); // UBAH/HAPUS NANTI
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
    public void daftarPelangganBaru () {
        Scanner input = new Scanner(System.in);
        System.out.println("=== MENU PENDAFTARAN PELANGGAN ===");
        while (true){
            System.out.println("Masukkan Nomor KTP: ");
            nik = input.nextLine();
            if (!validasiNomorKtp(nik)){
                continue;
            }
            if (validasiDataNomorKTP(nik)){
                System.out.println("Pelanggan dengan KTP tersebut sudah terdaftar!");
            }
            break;
        }
        System.out.println("Masukkan Nama Lengkap: ");
        namaPelanggan = input.nextLine();
        System.out.println("Masukkan No Telepon: ");
        noTelp = input.nextLine();

    }

    public boolean validasiNomorKtp(String nik){
        if (nik.trim().isEmpty()){
            System.out.println("Nomor KTP tidak boleh kosong!");
            return false;
        }
        if (!nik.matches("\\d{16}")){
            System.out.println("Nomor KTP harus diisi 16 angka!");
            return false;
        }
        return true;
    }
    public boolean validasiDataNomorKTP(String nik){
        List <Pelanggan> listPelanggan = pelangganRepo.loadAll();
        for (Pelanggan list : listPelanggan){
            if (list.getNik().equals(nik)){
                return true;
            }
        }
        return false;
    }
     
    public void cariDataPelanggan () {}

    //Task Fatin - Cek Kendaraan Tersedia
    public void cekKendaraanTersedia () { //tugas Fatin
        Scanner input = new Scanner(System.in);

        //mengambil semua data yang ada di kendaraan.json
        List<Kendaraan> listKendaraan = kendaraanRepo.loadAll();
        System.out.println("=== DAFTAR KENDARAAN YANG TERSEDIA ===");
        //membuat list baru, untuk kendaraan yang statusnya tersedia
        List<Kendaraan> listKendaraanTersedia = new ArrayList<>();

        //loop kendaraan, untuk memfilter yang statusnya tersedia saja
        for (Kendaraan kendaraan : listKendaraan){
            if (kendaraan.getStatus().equalsIgnoreCase("Tersedia")){
                listKendaraanTersedia.add(kendaraan); //masukkan ke list yang tersedia 
                //untuk yang sedang disewa akan otomatis dilewati alias tak masuk ke dalam list
            }
        } 

        //cek apakah ada kendaraan yang tersedia
        if(listKendaraanTersedia.isEmpty()){
            System.out.println("Tidak ada kendaraan yang tersedia saat ini.");
        } else {
            for (Kendaraan kendaraan : listKendaraanTersedia){
            System.out.println("======================================");
            //tampilaka kendaraan yang tersedia 
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

    public void prosesPeminjaman () {}
    public void prosesPengembalian () {}
}
