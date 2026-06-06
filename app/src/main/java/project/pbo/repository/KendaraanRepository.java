package project.pbo.repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import project.pbo.domain.Kendaraan;
import project.pbo.domain.Mobil;
import project.pbo.domain.Motor;

public class KendaraanRepository {
    private static final String FILE_PATH = "app/data/kendaraan.json";
    private static final String STATUS_TERSEDIA = "TERSEDIA";
    private static final String STATUS_DISEWA = "SEDANG DISEWA";

    private final Gson gson;

    public KendaraanRepository() {
        // Custom Serializer & Deserializer untuk menangani Polimorfisme Mobil/Motor di JSON
        JsonSerializer<Kendaraan> serializer = (src, typeOfSrc, context) -> {
            JsonObject jsonObj = context.serialize(src).getAsJsonObject();
            jsonObj.addProperty("jenisKendaraan", src.getClass().getSimpleName()); // Menandai tipe "Mobil" atau "Motor"
            jsonObj.addProperty("status", normalisasiStatus(src.getStatus()));
            return jsonObj;
        };

        JsonDeserializer<Kendaraan> deserializer = (json, typeOfT, context) -> {
            JsonObject jsonObj = json.getAsJsonObject();

            String type;
            if (jsonObj.has("type") && !jsonObj.get("type").isJsonNull()) {
                type = jsonObj.get("type").getAsString();
            } else if (jsonObj.has("jenisKendaraan") && !jsonObj.get("jenisKendaraan").isJsonNull()) {
                type = jsonObj.get("jenisKendaraan").getAsString();
            } else {
                throw new JsonParseException("Data kendaraan tidak memiliki type atau jenisKendaraan.");
            }

            Kendaraan kendaraan;
            if ("Mobil".equalsIgnoreCase(type)) {
                kendaraan = context.deserialize(json, Mobil.class);
            } else if ("Motor".equalsIgnoreCase(type)) {
                kendaraan = context.deserialize(json, Motor.class);
            } else {
                throw new JsonParseException("Tipe kendaraan tidak dikenali: " + type);
            }

            kendaraan.setStatus(normalisasiStatus(kendaraan.getStatus()));
            return kendaraan;
        };

        // Build Gson dengan format cetak rapi (Pretty Printing)
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Kendaraan.class, serializer)
                .registerTypeAdapter(Kendaraan.class, deserializer)
                .setPrettyPrinting()
                .create();
    }

    // Fungsi untuk membaca (LOAD) seluruh data kendaraan dari file json
    public List<Kendaraan> loadAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>(); // Kembalikan list kosong jika file json belum dibuat
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Kendaraan>>(){}.getType();
            List<Kendaraan> data = gson.fromJson(reader, listType);

            if (data == null) {
                return new ArrayList<>();
            }

            for (Kendaraan kendaraan : data) {
                kendaraan.setStatus(normalisasiStatus(kendaraan.getStatus()));
            }

            return data;
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal membaca file database kendaraan: " + e.getMessage());
            return new ArrayList<>();
        } catch (JsonParseException e) {
            System.out.println("[ERROR] Format JSON kendaraan.json tidak valid: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Fungsi untuk menyimpan (SAVE) seluruh data kendaraan kembali ke file json
    public void saveAll(List<Kendaraan> list) {
        File file = new File(FILE_PATH);
        File parentFolder = file.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        for (Kendaraan kendaraan : list) {
            kendaraan.setStatus(normalisasiStatus(kendaraan.getStatus()));
        }

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(list, writer);
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menyimpan data kendaraan ke JSON: " + e.getMessage());
        }
    }

    private static String normalisasiStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return STATUS_TERSEDIA;
        }

        if (STATUS_TERSEDIA.equalsIgnoreCase(status.trim())) {
            return STATUS_TERSEDIA;
        }

        if (STATUS_DISEWA.equalsIgnoreCase(status.trim())) {
            return STATUS_DISEWA;
        }

        return status.trim().toUpperCase();
    }
}