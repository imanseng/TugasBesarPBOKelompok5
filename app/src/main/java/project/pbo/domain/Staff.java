package project.pbo.domain;

public class Staff extends Pengguna {

    // IMAN - Perbaikan konstruktor
    public Staff(String username, String password, String role) {
        super(username, password, role);
    }
}
