package project.pbo.service;

import project.pbo.domain.Transaksi;
import project.pbo.repository.TransaksiRepository;
import project.pbo.infrastructure.database.PostgreSqlDatabaseConnection;
import java.util.List;

// Fungsi rekap untuk Owner: Menjumlahkan semua totalBayar dari transaksi yang sudah selesai.
public class LaporanService {
    private final TransaksiRepository transaksiRepository;

    public LaporanService() {
        // Membuat objek repository transaksi secara langsung dan terisolasi dengan koneksi PostgreSQL
        this.transaksiRepository = new TransaksiRepository(new PostgreSqlDatabaseConnection());
    }

    public LaporanService(TransaksiRepository transaksiRepository) {
        this.transaksiRepository = transaksiRepository;
    }

    public double hitungTotalPendapatan() {
        List<Transaksi> transaksiList = transaksiRepository.findAll();
        double totalPendapatan = 0;

        for (Transaksi transaksi : transaksiList) {
            if ("SELESAI".equalsIgnoreCase(transaksi.getStatus())) {
                totalPendapatan += transaksi.getTotalBayar();
            }
        }

        return totalPendapatan;
    }

    public List<Transaksi> getRiwayatTransaksi() {
        return transaksiRepository.findAll();
    }
}