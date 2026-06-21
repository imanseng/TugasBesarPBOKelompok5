package project.pbo.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import project.pbo.domain.Kendaraan;
import project.pbo.domain.Transaksi;
import project.pbo.repository.TransaksiRepository;
import project.pbo.infrastructure.database.PostgreSqlDatabaseConnection;

public class TransaksiService {
    // Membuat objek repository transaksi secara langsung dan terisolasi dengan koneksi PostgreSQL
    private final TransaksiRepository transaksiRepo = new TransaksiRepository(new PostgreSqlDatabaseConnection());
    private final KendaraanService kendaraanService = new KendaraanService();

    // Map tarif zona agar mematuhi OCP (terbuka untuk ditambah, tanpa harus mengedit if-else method)
    private static final Map<String, Double> TARIF_ZONA = new HashMap<>();

    static {
        TARIF_ZONA.put("A", 150000.0);
        TARIF_ZONA.put("B", 100000.0);
        TARIF_ZONA.put("C", 50000.0);
    }

    public String generateIdTransaksi() {
        List<Transaksi> listTransaksi = transaksiRepo.findAll();
        int maxNum = 0;

        for (Transaksi t : listTransaksi) {
            if (t.getIdTransaksi() != null && t.getIdTransaksi().startsWith("TRX-")) {
                try {
                    String numberStr = t.getIdTransaksi().substring(4);
                    int number = Integer.parseInt(numberStr);
                    if (number > maxNum) {
                        maxNum = number;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        return String.format("TRX-%03d", maxNum + 1);
    }

    public Transaksi prosesPeminjaman(String nikPelanggan, Kendaraan kendaraan, int durasi, boolean isDelivery,
            String zonaKirim, double biayaKirim) {
        double totalBayar = (durasi * kendaraan.getHargaSewaPerHari()) + biayaKirim;
        String idTransaksi = generateIdTransaksi();

        Transaksi transaksiBaru = new Transaksi(idTransaksi, nikPelanggan, kendaraan.getPlatNomor(), durasi, totalBayar,
                "AKTIF", isDelivery, zonaKirim);

        // Melakukan INSERT transaksi baru ke database dan UPDATE status kendaraan menjadi "SEDANG DISEWA"
        transaksiRepo.tambah(transaksiBaru);
        kendaraanService.updateStatusKendaraan(kendaraan, "SEDANG DISEWA");

        return transaksiBaru;
    }

    public double hitungBiayaDasar(Transaksi transaksi, Kendaraan kendaraan) {
        return kendaraan.getHargaSewaPerHari() * transaksi.getDurasiHari();
    }

    public double hitungBiayaKirim(Transaksi transaksi) {
        if (!transaksi.isDelivery()) {
            return 0; // Tidak ada biaya tambahan jika diambil di kantor
        }
        return hitungBiayaKirimBerdasarkanZona(transaksi.getZonaKirim());
    }

    public double hitungBiayaKirimBerdasarkanZona(String zonaKirim) {
        if (zonaKirim == null) return 0;
        return TARIF_ZONA.getOrDefault(zonaKirim.toUpperCase(), 0.0);
    }

    // Langsung update database menggunakan repository
    public void selesaikanPengembalian(Transaksi transaksi, Kendaraan kendaraan, double totalBayar) {
        transaksi.setTotalBayar(totalBayar);
        transaksi.setStatus("SELESAI");
        transaksiRepo.update(transaksi);
        
        kendaraanService.updateStatusKendaraan(kendaraan, "TERSEDIA");
    }

    public Transaksi cariTransaksiAktif(String keyword) {
        List<Transaksi> listTransaksi = transaksiRepo.findAll();
        for (Transaksi transaksi : listTransaksi) {
            boolean idCocok = transaksi.getIdTransaksi() != null
                    && transaksi.getIdTransaksi().equalsIgnoreCase(keyword);
            boolean platCocok = transaksi.getPlatNomor() != null && transaksi.getPlatNomor().equalsIgnoreCase(keyword);

            if ((idCocok || platCocok) && "AKTIF".equalsIgnoreCase(transaksi.getStatus())) {
                return transaksi;
            }
        }
        return null;
    }
}