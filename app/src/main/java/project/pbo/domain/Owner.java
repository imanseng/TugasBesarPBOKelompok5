package project.pbo.domain;

import java.util.Scanner;

public class Owner extends User {
    private char pilihanMenu;
    private String username;
    public Owner (String username){
        username = getUsername();
        pilihanMenu = ' ';
    }

    Scanner input = new Scanner(System.in);

    private void tampilkanMenu (){
        do {
            System.out.println("Selamat Datang, " + username + "!\nSilahkan pilih menu:\n1. Lihat Laporan Pendapatan & Riwayat\n0. Logout");
            System.out.println("\nPilihan Anda > ");
            pilihanMenu = input.nextLine().charAt(0);
        } while (pilihanMenu == '0');
    }
}
