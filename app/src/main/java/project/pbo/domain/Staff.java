package project.pbo.domain;

import java.util.Scanner;

public class Staff extends Pengguna {
    private char pilihanMenu;
    private String username;
    public Staff (String username){
        username = getUsername();
        pilihanMenu = ' ';
    }

    Scanner input = new Scanner(System.in);

    private void tampilkanMenu (){
        do {
            System.out.println("Selamat Datang, " + username + "!\nSilahkan pilih menu:\n1. Daftar Pelanggan Baru\n2. Cari Data Pelanggan\n3. Cek Kendaraan Tersedia\n4. Proses Peminjaman (Sewa)\n5. Proses Pengembalian\n0. Logout");
            System.out.println("\nPilihan Anda > ");
            pilihanMenu = input.nextLine().charAt(0);
        } while (pilihanMenu == '0');
    }
}
