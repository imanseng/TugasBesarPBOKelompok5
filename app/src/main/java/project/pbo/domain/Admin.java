package project.pbo.domain;

public class Admin extends Pengguna {

    // IMAN Perbaikan Konstruktor, meneruskan data ke superclass Pengguna
    public Admin(String username, String password, String role) {
        super(username, password, role);
    }
}
