package project.pbo.service;

import java.util.ArrayList;
import java.util.List;

import project.pbo.domain.Kendaraan;
import project.pbo.repository.KendaraanRepository;

public class KendaraanService {
    private final KendaraanRepository kendaraanRepo = new KendaraanRepository();

    // Pindah fitur filter kendaraan tersedia dari StaffMenu ke sini
    public List<Kendaraan> getKendaraanTersedia() {
        List<Kendaraan> listKendaraan = kendaraanRepo.loadAll();
        List<Kendaraan> listKendaraanTersedia = new ArrayList<>();

        for (Kendaraan kendaraan : listKendaraan) {
            if (kendaraan.getStatus().equalsIgnoreCase("Tersedia")) {
                listKendaraanTersedia.add(kendaraan);
            }
        }
        return listKendaraanTersedia;
    }

    // Pindah fitur validasi plat nomor duplikat dari AdminMenu ke sini
    public boolean isPlatNomorTerdaftar(String platNomor) {
        List<Kendaraan> listKendaraan = kendaraanRepo.loadAll();
        for (Kendaraan k : listKendaraan) {
            if (k.getPlatNomor().equalsIgnoreCase(platNomor)) {
                return true;
            }
        }
        return false;
    }

    // Pindah fitur simpan kendaraan baru dari AdminMenu ke sini
    public void tambahKendaraan(Kendaraan kendaraanBaru) {
        List<Kendaraan> listKendaraan = kendaraanRepo.loadAll();
        listKendaraan.add(kendaraanBaru);
        kendaraanRepo.saveAll(listKendaraan);
    }

    // Pindah fitur cari kendaraan by plat nomor dari AdminMenu ke sini
    public Kendaraan cariKendaraanByPlat(String platNomor) {
        List<Kendaraan> listKendaraan = kendaraanRepo.loadAll();
        for (Kendaraan k : listKendaraan) {
            if (k.getPlatNomor().equalsIgnoreCase(platNomor)) {
                return k;
            }
        }
        return null;
    }

    // Pindah fitur hapus kendaraan dari AdminMenu ke sini
    public void hapusKendaraan(Kendaraan kendaraan) {
        List<Kendaraan> listKendaraan = kendaraanRepo.loadAll();
        listKendaraan.removeIf(k -> k.getPlatNomor().equalsIgnoreCase(kendaraan.getPlatNomor()));
        kendaraanRepo.saveAll(listKendaraan);
    }

    public List<Kendaraan> getAllKendaraan() {
        return kendaraanRepo.loadAll();
    }

    public void updateStatusKendaraan(Kendaraan kendaraan, String statusBaru) {
        List<Kendaraan> listKendaraan = kendaraanRepo.loadAll();
        for (Kendaraan k : listKendaraan) {
            if (k.getPlatNomor().equalsIgnoreCase(kendaraan.getPlatNomor())) {
                k.setStatus(statusBaru);
                break;
            }
        }
        kendaraanRepo.saveAll(listKendaraan);
    }
}

