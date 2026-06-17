package project.pbo.service;

import java.util.List;

import project.pbo.domain.Kendaraan;
import project.pbo.domain.Transaksi;
import project.pbo.repository.TransaksiRepository;

public class TransaksiService {
    private final TransaksiRepository transaksiRepo = new TransaksiRepository();
    private final KendaraanService kendaraanService = new KendaraanService();

    // Pindah fitur generateIdTransaksi dari StaffMenu ke sini
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

    public List<Transaksi> getAllTransaksi() {
        return transaksiRepo.findAll();
    }

    // Pindah fitur buat transaksi peminjaman dari StaffMenu ke sini
    public Transaksi prosesPeminjaman(String nikPelanggan, Kendaraan kendaraan, int durasi, boolean isDelivery, String zonaKirim, double biayaKirim) {
        double totalBayar = (durasi * kendaraan.getHargaSewaPerHari()) + biayaKirim;
        String idTransaksi = generateIdTransaksi();

        Transaksi transaksiBaru = new Transaksi(idTransaksi, nikPelanggan, kendaraan.getPlatNomor(), durasi, totalBayar, "AKTIF", isDelivery, zonaKirim);

        transaksiRepo.tambah(transaksiBaru);
        kendaraanService.updateStatusKendaraan(kendaraan, "SEDANG DISEWA");

        return transaksiBaru;
    }

    // Pindah fitur perhitungan dan proses pengembalian dari StaffMenu ke sini
    public double hitungBiayaDasar(Transaksi transaksi, Kendaraan kendaraan) {
        return kendaraan.getHargaSewaPerHari() * transaksi.getDurasiHari();
    }

    public double hitungBiayaKirim(Transaksi transaksi) {
        if (transaksi.isDelivery()) {
            String zonaKirim = transaksi.getZonaKirim();
            if ("A".equalsIgnoreCase(zonaKirim)) return 150000;
            else if ("B".equalsIgnoreCase(zonaKirim)) return 100000;
            else if ("C".equalsIgnoreCase(zonaKirim)) return 50000;
        }
        return 0;
    }

    public void selesaikanPengembalian(Transaksi transaksi, Kendaraan kendaraan, double totalBayar) {
        List<Transaksi> listTransaksi = transaksiRepo.findAll();
        for (Transaksi t : listTransaksi) {
            if (t.getIdTransaksi().equals(transaksi.getIdTransaksi())) {
                t.setTotalBayar(totalBayar);
                t.setStatus("SELESAI");
                break;
            }
        }
        transaksiRepo.saveAll(listTransaksi);

        transaksi.setTotalBayar(totalBayar);
        transaksi.setStatus("SELESAI");
        
        kendaraanService.updateStatusKendaraan(kendaraan, "Tersedia");
    }

    // Pindah fitur pencarian transaksi aktif dari StaffMenu ke sini
    public Transaksi cariTransaksiAktif(String keyword) {
        List<Transaksi> listTransaksi = transaksiRepo.findAll();
        for (Transaksi transaksi : listTransaksi) {
            boolean idCocok = transaksi.getIdTransaksi() != null && transaksi.getIdTransaksi().equalsIgnoreCase(keyword);
            boolean platCocok = transaksi.getPlatNomor() != null && transaksi.getPlatNomor().equalsIgnoreCase(keyword);

            if ((idCocok || platCocok) && "AKTIF".equalsIgnoreCase(transaksi.getStatus())) {
                return transaksi;
            }
        }
        return null;
    }
}

