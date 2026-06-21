package project.pbo.service;

import java.util.ArrayList;
import java.util.List;
import project.pbo.domain.Kendaraan;
import project.pbo.repository.KendaraanRepository;


public class KendaraanService {
    private final KendaraanRepository kendaraanRepo;

    public KendaraanService(KendaraanRepository kendaraanRepo) {
        this.kendaraanRepo = kendaraanRepo;
    }

    public List<Kendaraan> getKendaraanTersedia() {
        List<Kendaraan> listKendaraan = kendaraanRepo.getAll();
        List<Kendaraan> listKendaraanTersedia = new ArrayList<>();

        for (Kendaraan kendaraan : listKendaraan) {
            if (kendaraan.getStatus().equalsIgnoreCase("TERSEDIA")) {
                listKendaraanTersedia.add(kendaraan);
            }
        }
        return listKendaraanTersedia;
    }

    public boolean isPlatNomorTerdaftar(String platNomor) {
        List<Kendaraan> listKendaraan = kendaraanRepo.getAll();
        for (Kendaraan k : listKendaraan) {
            if (k.getPlatNomor().equalsIgnoreCase(platNomor)) {
                return true;
            }
        }
        return false;
    }

    // Kini kita hanya memanggil save, bukan memuat list JSON
    public void tambahKendaraan(Kendaraan kendaraanBaru) {
        kendaraanRepo.save(kendaraanBaru);
    }

    public Kendaraan cariKendaraanByPlat(String platNomor) {
        List<Kendaraan> listKendaraan = kendaraanRepo.getAll();
        for (Kendaraan k : listKendaraan) {
            if (k.getPlatNomor().equalsIgnoreCase(platNomor)) {
                return k;
            }
        }
        return null;
    }

    // Panggil fungsi delete SQL yang dibuat
    public void hapusKendaraan(Kendaraan kendaraan) {
        kendaraanRepo.delete(kendaraan.getPlatNomor());
    }

    public List<Kendaraan> getAllKendaraan() {
        return kendaraanRepo.getAll();
    }

    // Panggil perintah UPDATE SQL yang dibuat
    public void updateStatusKendaraan(Kendaraan kendaraan, String statusBaru) {
        kendaraanRepo.updateStatus(kendaraan.getPlatNomor(), statusBaru);
    }
}