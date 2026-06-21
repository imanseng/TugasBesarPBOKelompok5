package project.pbo.domain;

public abstract class Kendaraan {
    private String platNomor;
    private double hargaSewaPerHari;
    private String jenisKendaraan;
    private String status;

    public Kendaraan(String platNomor, double hargaSewaPerHari, String jenisKendaraan) {
        this.platNomor = platNomor;
        this.hargaSewaPerHari = hargaSewaPerHari;
        this.jenisKendaraan = jenisKendaraan;
        this.status = "TERSEDIA";
    }

    public String getPlatNomor() {
        return platNomor;
    }

    public double getHargaSewaPerHari() {
        return hargaSewaPerHari;
    }

    public String getJenisKendaraan() {
        return jenisKendaraan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double hitungDenda(int hariTerlambat) {
        return hariTerlambat * hargaSewaPerHari;
    }

    public abstract String getInfoTambahan();
}