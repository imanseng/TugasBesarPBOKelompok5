package project.pbo.domain;

import java.util.Scanner;

public class Owner extends Pengguna {
    private char pilihanMenu;
    // IMAN - Perbaikan Konstruktor
    public Owner(String username, String password, String role) {
        super(username, password, role);
        this.pilihanMenu = ' ';
    }

    // BELUM DI IMPLEMENT
    public void prosesMenu() {}
    public void riwayatTransaksi() {}
}
