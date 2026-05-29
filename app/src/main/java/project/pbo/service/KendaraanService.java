package project.pbo.service;

import project.pbo.domain.Kendaraan;
import project.pbo.domain.Mobil;
import project.pbo.domain.Motor;
import project.pbo.repository.KendaraanRepository;

import java.util.List;

//  Service untuk logika bisnis Manajemen Kendaraan (feat/list-vehicle & feat/delete-vehicle)
public class KendaraanService {

    private final KendaraanRepository repo = new KendaraanRepository();

    //  Mengambil seluruh daftar kendaraan dari repository
    public List<Kendaraan> getDaftarKendaraan() {
        return repo.loadAll();
    }

    // Mencari kendaraan berdasarkan plat nomor, return null jika tidak ditemukan
    public Kendaraan cariKendaraanByPlat(String platNomor) {
        List<Kendaraan> listKendaraan = repo.loadAll();
        for (Kendaraan k : listKendaraan) {
            if (k.getPlatNomor().equalsIgnoreCase(platNomor)) {
                return k;
            }
        }
        return null;
    }

    // Menghapus kendaraan berdasarkan plat nomor
    // Return: 0 = sukses, 1 = tidak ditemukan, 2 = sedang disewa
    public int hapusKendaraan(String platNomor) {
        List<Kendaraan> listKendaraan = repo.loadAll();

        Kendaraan target = null;
        for (Kendaraan k : listKendaraan) {
            if (k.getPlatNomor().equalsIgnoreCase(platNomor)) {
                target = k;
                break;
            }
        }

        // Kendaraan tidak ditemukan di sistem
        if (target == null) {
            return 1;
        }

        // Kendaraan sedang disewa, tidak bisa dihapus
        if (target.getStatus().equalsIgnoreCase("SEDANG DISEWA")) {
            return 2;
        }

        // Kendaraan tersedia, hapus dan simpan ulang
        listKendaraan.remove(target);
        repo.saveAll(listKendaraan);
        return 0;
    }

    public String getInfoTambahan(Kendaraan k) {
        if (k instanceof Mobil) {
            return ((Mobil) k).getJumlahPintu() + " Pintu";
        } else if (k instanceof Motor) {
            return ((Motor) k).getJenisTransmisi();
        }
        return "-";
    }
}