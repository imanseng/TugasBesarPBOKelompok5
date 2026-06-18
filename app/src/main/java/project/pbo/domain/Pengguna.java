package project.pbo.domain;

public class Pengguna {
    private String username;
    private String password;
    private String role;

    // IMAN - Menambah Constructor Pengguna
    public Pengguna(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // IMAN - Menambah Method cekLogin, apakah username dan password sesuai
    public boolean cekLogin(String username, String password) {
        if (this.username.equals(username) && this.password.equals(password)) {
            return true;
        } else {
            return false;
        }
    }
}