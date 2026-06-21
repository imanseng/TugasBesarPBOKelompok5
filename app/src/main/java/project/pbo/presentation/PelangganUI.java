package project.pbo.presentation;

import java.util.Scanner;
import project.pbo.domain.Pelanggan;
import project.pbo.service.PelangganService;

public class PelangganUI {
    private final PelangganService pelangganService = new PelangganService();

    public void daftarPelangganBaru(Scanner input) {
        String nik = "";
        String namaPelanggan = "";
        String noTelp = "";

        System.out.println("=== MENU PENDAFTARAN PELANGGAN ===");
        while (true) {
            System.out.println("Masukkan Nomor KTP: ");
            nik = input.nextLine();
            try {
                pelangganService.validasiNomorKtp(nik);
            } catch (IllegalArgumentException e) {
                System.out.println("[PERINGATAN] " + e.getMessage());
                continue;
            }
            if (pelangganService.validasiDataNomorKTP(nik)) {
                System.out.println("Pelanggan dengan KTP tersebut sudah terdaftar!");
                continue;
            }
            break;
        }
        while (true) {
            System.out.println("Masukkan Nama Lengkap: ");
            namaPelanggan = input.nextLine();
            if (namaPelanggan.matches("^[a-zA-Z\\s]+$")) {
                break;
            } else {
                System.out.println("[PERINGATAN] Nama lengkap hanya boleh berisi huruf!");
            }
        }

        while (true) {
            System.out.println("Masukkan No Telepon: ");
            noTelp = input.nextLine();
            if (noTelp.matches("^[0-9]+$")) {
                break;
            } else {
                System.out.println("[PERINGATAN] Nomor telepon hanya boleh berisi angka!");
            }
        }

        pelangganService.tambahPelanggan(nik, namaPelanggan, noTelp);
        System.out.println("[SUKSES] Data pelanggan berhasil disimpan ke JSON.");
    }

    public void cariDataPelanggan(Scanner input) {
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
            tampilkanInfoPelanggan(pelangganDiTemukan);
        } else {
            System.out.println("================================");
            System.out.println("MAAF DATA PELANGGAN TIDAK DITEMUKAN!");
            System.out.println("================================");
        }

        System.out.println("Tekan Enter untuk Kembali ke Menu");
        input.nextLine();
    }

    public void tampilkanInfoPelanggan(Pelanggan p) {
        System.out.println("Nama Lengkap\t: " + p.getNamaPelanggan());
        System.out.println("Nomor KTP\t: " + p.getNik());
        System.out.println("No Telepon\t: " + p.getNoTelp());
    }
}
