package project.pbo.service;
import project.pbo.domain.Transaksi;
import project.pbo.repository.TransaksiRepository;
import java.util.List;

// Fungsi rekap untuk Owner: Menjumlahkan semua totalBayar dari transaksi yang sudah selesai.
public class LaporanService {
private final TransaksiRepository transaksiRepository;

    public LaporanService() {
        this.transaksiRepository = new TransaksiRepository();
    }

    public LaporanService(TransaksiRepository transaksiRepository) {
        this.transaksiRepository = transaksiRepository;
    }

    public double hitungTotalPendapatan() {
        List<Transaksi> transaksiList = transaksiRepository.findAll();
        // ambil semua transaksi
        // pilih yang statusnya SELESAI
        // jumlahkan totalBayar
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
