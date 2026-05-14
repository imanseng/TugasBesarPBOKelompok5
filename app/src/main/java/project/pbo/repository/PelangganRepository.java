package project.pbo.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import project.pbo.domain.Pelanggan;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PelangganRepository {
    private static final String FILE_PATH = "app/data/pelanggan.json";
    private final Gson gson;

    public PelangganRepository() {
        // Build Gson dengan format cetak rapi (Pretty Printing)
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    // Fungsi untuk membaca (LOAD) seluruh data pelanggan
    public List<Pelanggan> loadAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>(); // Kembalikan list kosong jika file belum ada
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Pelanggan>>(){}.getType();
            List<Pelanggan> data = gson.fromJson(reader, listType);
            return data != null ? data : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal membaca file database pelanggan.");
            return new ArrayList<>();
        }
    }

    // Fungsi untuk menyimpan (SAVE) satu pelanggan baru
    public void save(Pelanggan baru) {
        List<Pelanggan> list = loadAll();
        list.add(baru);
        saveAll(list);
    }

    // Fungsi internal untuk menulis seluruh list ke file JSON
    private void saveAll(List<Pelanggan> list) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(list, writer);
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menyimpan data pelanggan ke JSON.");
        }
    }
}