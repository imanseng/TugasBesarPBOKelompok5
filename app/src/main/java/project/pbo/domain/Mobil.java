package project.pbo.domain;

public class Mobil extends Kendaraan {
    private int jumlahPintu;

    // Konstruktor mengalirkan data ke superclass
    public Mobil(String platNomor, double hargaSewaPerHari, String jenisKendaraan, int jumlahPintu) {
        super(platNomor, hargaSewaPerHari, jenisKendaraan);
        this.jumlahPintu = jumlahPintu;
    }

    public int getJumlahPintu() {
        return jumlahPintu;
    }

    // Adit (feat/return-logic) : diubah ini agar sesuai dengan aturan denda (Mobil
    // 50k/hari & Motor 20k/hari)
    @Override
    public double hitungDenda(int hariTerlambat) {
        if (hariTerlambat <= 0) {
            return 0;
        }
        return hariTerlambat * 50000;
    }
}