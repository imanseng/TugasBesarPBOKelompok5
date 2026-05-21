package project.pbo.domain;

public class Transaksi {
    private String idTransaksi;
    private String nikPelanggan;
    private String platNomor;
    private int durasiHari;
    private double totalBayar;
    private String status;
    private boolean isDelivery;
    private String zonaKirim;

    // default constructor
    public Transaksi() {
    }

    // parametrized constructor
    // IMAN - Perbaikan konstruktor sesuai dengan kebutuhan kelompok (penambahan isDelivery dan zonaKirim)
    public Transaksi(String id, String nik, String plat, int durasi, double total, String status, boolean isDelivery, String zonaKirim) {
        this.idTransaksi = id;
        this.nikPelanggan = nik;
        this.platNomor = plat;
        this.durasiHari = durasi;
        this.totalBayar = total;
        this.status = status;
        this.isDelivery = isDelivery; // Kelompok 5
        this.zonaKirim = zonaKirim;   // Kelompok 5
    }

    // getters and setters
    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getNikPelanggan() {
        return nikPelanggan;
    }

    public void setNikPelanggan(String nikPelanggan) {
        this.nikPelanggan = nikPelanggan;
    }

    public String getPlatNomor() {
        return platNomor;
    }

    public void setPlatNomor(String platNomor) {
        this.platNomor = platNomor;
    }

    public int getDurasiHari() {
        return durasiHari;
    }

    public void setDurasiHari(int durasiHari) {
        this.durasiHari = durasiHari;
    }

    public double getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(double totalBayar) {
        this.totalBayar = totalBayar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDelivery() {
        return isDelivery;
    }

    public void setDelivery(boolean delivery) {
        isDelivery = delivery;
    }

    public String getZonaKirim() {
        return zonaKirim;
    }

    public void setZonaKirim(String zonaKirim) {
        this.zonaKirim = zonaKirim;
    }

    // Method tampilkan struk peminjaman
    public void tampilkanStruk(String namaPelanggan, String jenisKendaraan) {
        System.out.println("\n--- STRUK PEMINJAMAN SEMENTARA ---");
        System.out.println("ID Transaksi   : " + this.idTransaksi);
        System.out.println("Nama Pelanggan : " + namaPelanggan);
        System.out.println("Kendaraan      : " + jenisKendaraan + " (" + this.platNomor + ")");
        System.out.println("Durasi Sewa    : " + this.durasiHari + " Hari");
        System.out.println("Estimasi Biaya : Rp " + String.format("%,.0f", this.totalBayar));
        System.out.println("----------------------------------");
        System.out.println("[SUKSES] Transaksi berhasil dicatat. Status kendaraan berubah menjadi SEDANG DISEWA.");
    }
}
