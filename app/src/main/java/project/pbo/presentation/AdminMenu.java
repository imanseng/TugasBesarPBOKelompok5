package project.pbo.presentation;

import project.pbo.domain.Kendaraan;
import project.pbo.service.KendaraanService;

import java.util.List;
import java.util.Scanner;

// Presentation layer untuk fitur Admin: Lihat & Hapus Kendaraan
// (feat/list-vehicle & feat/delete-vehicle)
public class AdminMenu {

    private final KendaraanService kendaraanService = new KendaraanService();

    // Menu 2 Admin: Lihat seluruh daftar kendaraan (feat/list-vehicle)
    public void lihatDaftarKendaraan() {
        List<Kendaraan> listKendaraan = kendaraanService.getDaftarKendaraan();

        System.out.println("\n===================================================================================");
        System.out.println("                       DAFTAR SELURUH KENDARAAN                                   ");
        System.out.println("===================================================================================");

        if (listKendaraan.isEmpty()) {
            System.out.println("  Data kendaraan masih kosong.");
        } else {
            System.out.printf("| %-12s | %-5s | %-13s | %-14s | %-13s |%n",
                    "Plat Nomor", "Jenis", "Harga/Hari", "Info Tambahan", "Status");
            System.out.println("-----------------------------------------------------------------------------------");

            for (Kendaraan k : listKendaraan) {
                String infoTambahan = kendaraanService.getInfoTambahan(k);

                System.out.printf("| %-12s | %-5s | Rp %,-10.0f | %-14s | %-13s |%n",
                        k.getPlatNomor(),
                        k.getJenisKendaraan(),
                        k.getHargaSewaPerHari(),
                        infoTambahan,
                        k.getStatus());
            }
            System.out.println("===================================================================================");
            System.out.println("  Total kendaraan terdaftar: " + listKendaraan.size());
        }

        System.out.println("\nTekan ENTER untuk kembali ke menu utama...");
        new Scanner(System.in).nextLine();
    }

    //  Menu 3 Admin: Hapus kendaraan (feat/delete-vehicle)
    public void hapusKendaraan() {
        Scanner input = new Scanner(System.in);
        List<Kendaraan> listKendaraan = kendaraanService.getDaftarKendaraan();

        System.out.println("\n========================================");
        System.out.println("         MENU HAPUS KENDARAAN           ");
        System.out.println("========================================");

        // Cek dulu apakah data ada
        if (listKendaraan.isEmpty()) {
            System.out.println("  Data kendaraan masih kosong. Tidak ada yang bisa dihapus.");
            System.out.println("\nTekan ENTER untuk kembali ke menu utama...");
            input.nextLine();
            return;
        }

        while (true) {
            System.out.print("\nMasukkan Plat Nomor yang ingin dihapus (ketik 0 untuk kembali): ");
            String platNomor = input.nextLine().trim().toUpperCase();

            // Opsi kembali
            if (platNomor.equals("0")) {
                System.out.println("Kembali ke menu utama.");
                return;
            }

            if (platNomor.isEmpty()) {
                System.out.println("[!] Plat Nomor tidak boleh kosong!");
                continue;
            }

            // Cek dulu kendaraannya sebelum hapus, untuk tampilkan infonya
            Kendaraan kendaraan = kendaraanService.cariKendaraanByPlat(platNomor);

            if (kendaraan != null) {
                System.out.println("\n--- Detail Kendaraan Ditemukan ---");
                kendaraan.tampilkanInfo();
                System.out.println("----------------------------------");
            }

            // Jalankan logika hapus dari service
            int hasil = kendaraanService.hapusKendaraan(platNomor);

            if (hasil == 1) {
                System.out.println("[ERROR] Kendaraan dengan plat nomor " + platNomor
                        + " tidak ditemukan di sistem!");
                // Loop lagi, minta input plat lain

            } else if (hasil == 2) {
                System.out.println("[GAGAL] Kendaraan " + platNomor
                        + " masih berstatus SEDANG DISEWA, data tidak dapat dihapus!");
                // Loop lagi, minta input plat lain

            } else {
                // hasil == 0 → sukses
                System.out.println("\n[SUKSES] Kendaraan " + platNomor + " berhasil dihapus dari sistem.");
                System.out.println("\nTekan ENTER untuk kembali ke menu utama...");
                input.nextLine();
                return;
            }
        }
    }
}