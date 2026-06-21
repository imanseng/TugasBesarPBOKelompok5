package project.pbo.service;

import java.util.List;
import project.pbo.domain.Kendaraan;
import project.pbo.domain.Transaksi;
import project.pbo.repository.TransaksiRepository;
import project.pbo.infrastructure.database.PostgreSqlDatabaseConnection;

public class TransaksiService {
    // Membuat objek repository transaksi secara langsung dan terisolasi dengan koneksi PostgreSQL
    private final TransaksiRepository transaksiRepo = new TransaksiRepository(new PostgreSqlDatabaseConnection());
    private final KendaraanService kendaraanService = new KendaraanService();

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
        if (transaksi.isDelivery()) {
            String zonaKirim = transaksi.getZonaKirim();
            if ("A".equalsIgnoreCase(zonaKirim))
                return 150000;
            else if ("B".equalsIgnoreCase(zonaKirim))
                return 100000;
            else if ("C".equalsIgnoreCase(zonaKirim))
                return 50000;
        }
        return 0;
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