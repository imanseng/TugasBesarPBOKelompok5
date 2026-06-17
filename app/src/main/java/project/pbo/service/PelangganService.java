package project.pbo.service;

import java.util.List;

import project.pbo.domain.Pelanggan;
import project.pbo.repository.PelangganRepository;

public class PelangganService {
    // Pindah fitur validasiNomorKtp dan validasiDataNomorKTP dari StaffMenu ke sini
    private final PelangganRepository pelangganRepo = new PelangganRepository();

    public boolean validasiNomorKtp(String nik) {
        if (nik.trim().isEmpty()) {
            System.out.println("Nomor KTP tidak boleh kosong!");
            return false;
        }
        if (!nik.matches("\\d{16}")) {
            System.out.println("Nomor KTP harus diisi 16 angka!");
            return false;
        }
        return true;
    }

    public boolean validasiDataNomorKTP(String nik) {
        List<Pelanggan> listPelanggan = pelangganRepo.loadAll();
        for (Pelanggan list : listPelanggan) {
            if (list.getNik().equals(nik)) {
                return true;
            }
        }
        return false;
    }

    // Pindah fitur cari pelanggan (cariByNik) dari StaffMenu ke sini
    public Pelanggan cariByNik(String nik) {
        List<Pelanggan> listPelanggan = pelangganRepo.loadAll();
        for (Pelanggan pelanggan : listPelanggan) {
            if (pelanggan.getNik().equals(nik)) {
                return pelanggan;
            }
        }
        return null;
    }
}
