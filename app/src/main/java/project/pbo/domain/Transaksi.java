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

    // parametrized constructor
    // IMAN - Perbaikan konstruktor sesuai dengan kebutuhan kelompok (penambahan
    // isDelivery dan zonaKirim)
    public Transaksi(String id, String nik, String plat, int durasi, double total, String status, boolean isDelivery,
            String zonaKirim) {
        this.idTransaksi = id;
        this.nikPelanggan = nik;
        this.platNomor = plat;
        this.durasiHari = durasi;
        this.totalBayar = total;
        this.status = status;
        this.isDelivery = isDelivery; // Kelompok 5
        this.zonaKirim = zonaKirim; // Kelompok 5
    }

    // getters and setters
    public String getIdTransaksi() {
        return idTransaksi;
    }

    public String getNikPelanggan() {
        return nikPelanggan;
    }

    public String getPlatNomor() {
        return platNomor;
    }

    public int getDurasiHari() {
        return durasiHari;
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

    public String getZonaKirim() {
        return zonaKirim;
    }
}