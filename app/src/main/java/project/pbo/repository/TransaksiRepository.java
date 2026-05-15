package project.pbo.repository;
import com.google.gson.Gson;//untuk penggunaan Gson
import java.util.List;//import List

import project.pbo.domain.Transaksi;//import class "Transaksi" dari "Transaksi.java"
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import project.pbo.domain.Transaksi;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//Logika Gson (tugas = Java object ke JSON, dan sebaliknya)
public class TransaksiRepository {
    private static final String FILE_PATH = "app/data/transaksi.json";

    private final Gson gson;

    public TransaksiRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public List<Transaksi> findAll() {
        File file = new File(FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Transaksi>>() {}.getType();
            List<Transaksi> transaksiList = gson.fromJson(reader, listType);

            if (transaksiList == null) {
                return new ArrayList<>();
            }

            return transaksiList;
        } catch (IOException e) {
            throw new RuntimeException("Gagal membaca data transaksi", e);
        }
    }

    public void saveAll(List<Transaksi> transaksiList) {
        File file = new File(FILE_PATH);
        File parentFolder = file.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(transaksiList, writer);
        } catch (IOException e) {
            throw new RuntimeException("Gagal menyimpan data transaksi", e);
        }
    }

    public void tambah(Transaksi transaksi) {
        List<Transaksi> transaksiList = findAll();
        transaksiList.add(transaksi);
        saveAll(transaksiList);
    }
}