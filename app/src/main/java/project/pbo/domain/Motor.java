package project.pbo.domain;

public class Motor extends Kendaraan {
    private String jenisTransmisi;

    public Motor(String platNomor, double hargaSewaPerHari, String jenisKendaraan, String jenisTransmisi) {
        super(platNomor, hargaSewaPerHari, jenisKendaraan);
        this.jenisTransmisi = jenisTransmisi;
    }

    public String getJenisTransmisi() {
        return jenisTransmisi;
    }

    // Adit (feat/return-logic) : diubah ini agar sesuai dengan aturan denda (Mobil
    // 50k/hari & Motor 20k/hari)
    @Override
    public double hitungDenda(int hariTerlambat) {
        if (hariTerlambat <= 0) {
            return 0;
        }
        return hariTerlambat * 20000;
    }
}