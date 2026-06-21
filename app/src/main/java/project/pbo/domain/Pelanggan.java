package project.pbo.domain;

public class Pelanggan {
    private String nik;
    private String namaPelanggan;
    private String noTelp;

    public Pelanggan(String nik, String namaPelanggan, String noTelp) {
        this.nik = nik;
        this.namaPelanggan = namaPelanggan;
        this.noTelp = noTelp;
    }

    public String getNik() {
        return nik;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public String getNoTelp() {
        return noTelp;
    }
}