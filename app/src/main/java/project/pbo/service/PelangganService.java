package project.pbo.service;

import java.util.List;
import project.pbo.domain.Pelanggan;
import project.pbo.repository.PelangganRepository;


public class PelangganService {
    private final PelangganRepository pelangganRepo;

    public PelangganService(PelangganRepository pelangganRepo) {
        this.pelangganRepo = pelangganRepo;
    }

    public void validasiNomorKtp(String nik) {
        if (nik.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor KTP tidak boleh kosong!");
        }
        if (!nik.matches("\\d{16}")) {
            throw new IllegalArgumentException("Nomor KTP harus diisi 16 angka!");
        }
    }

    public boolean validasiDataNomorKTP(String nik) {
        List<Pelanggan> listPelanggan = pelangganRepo.getAll();
        for (Pelanggan list : listPelanggan) {
            if (list.getNik().equals(nik)) {
                return true;
            }
        }
        return false;
    }

    public Pelanggan cariByNik(String nik) {
        List<Pelanggan> listPelanggan = pelangganRepo.getAll();
        for (Pelanggan pelanggan : listPelanggan) {
            if (pelanggan.getNik().equals(nik)) {
                return pelanggan;
            }
        }
        return null;
    }

    public void tambahPelanggan(String nik, String namaPelanggan, String noTelp) {
        Pelanggan pelangganBaru = new Pelanggan(nik, namaPelanggan, noTelp);
        pelangganRepo.save(pelangganBaru);
    }
}