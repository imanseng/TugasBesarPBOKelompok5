package project.pbo.domain;

import java.util.Scanner;

public class Admin extends User {
    private char pilihanMenu;
    private String username;
    public Admin (String username){
        username = getUsername();
        pilihanMenu = ' ';
    }

    Scanner input = new Scanner(System.in);

    private void tampilkanMenu (){
        do {
            System.out.println("Selamat Datang, " + username + "!\nSilahkan pilih menu:\n1. Tambah Kendaraan Baru\n2. Lihat Semua Kendaraan\n3. Hapus Kendaraan\n0. Logout");
            System.out.println("\nPilihan Anda > ");
            pilihanMenu = input.nextLine().charAt(0);
        } while (pilihanMenu == '0');
    }
}
