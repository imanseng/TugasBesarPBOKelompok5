package project.pbo.domain;

public class Owner extends Pengguna {
    // IMAN - Perbaikan Konstruktor
    public Owner(String username, String password, String role) {
        super(username, password, role);
    }
}