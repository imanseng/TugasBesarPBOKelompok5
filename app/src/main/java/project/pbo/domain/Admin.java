package project.pbo.domain;
import java.util.Scanner;

public class Admin extends Pengguna {
    private char pilihanMenu;

    // IMAN - Perbaikan Konstruktor, memasukkan data ke superclass Pengguna
    public Admin(String username, String password, String role) {
        super(username, password, role);
        this.pilihanMenu = ' ';
    }

    // IMAN - Menjalankan Menu
    public void prosesMenu() {
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("=== MENU ADMIN ===");
            System.out.println("\nSelamat Datang, " + super.getUsername()); 
            System.out.println("Silahkan pilih menu:");
            System.out.println("1. Tambah Kendaraan Baru");
            System.out.println("2. Lihat Semua Kendaraan");
            System.out.println("3. Hapus Kendaraan");
            System.out.println("0. Logout");
            System.out.print("\nPilihan Anda > ");
            
            // IMAN - Memasukkan pilihan menu
            pilihanMenu = input.nextLine().charAt(0);

            // IMAN - Menampilkan menu sesuai pilihan
            switch (pilihanMenu) {
                case '1':
                    // Memanggil fungsi input kendaraan baru
                    break;
                case '2':
                    System.out.println("Fitur lihat kendaraan (Monic/Robby)."); // UBAH DARI SINI
                    break;
                case '3':
                    System.out.println("Fitur hapus kendaraan (Monic/Robby)."); // UBAH DARI SINI
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
}
