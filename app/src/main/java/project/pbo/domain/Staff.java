package project.pbo.domain;

import java.util.Scanner;

public class Staff extends Pengguna {
    private char pilihanMenu;
    // IMAN - Perbaikan konstruktor
    public Staff(String username, String password, String role) {
        super(username, password, role);
        this.pilihanMenu = ' ';
    }

    // BELUM DI IMPLEMENT
    public void prosesMenu() {}
    public void daftarPelangganBaru () {}
    public void cariDataPelanggan () {}
    public void cekKendaraanTersedia () {}
    public void prosesPeminjaman () {}
    public void prosesPengembalian () {}
}
