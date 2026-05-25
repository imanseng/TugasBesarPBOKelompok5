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
    private final Gson gson;

    public KendaraanRepository() {
        // Custom Serializer & Deserializer untuk menangani Polimorfisme Mobil/Motor di JSON
        JsonSerializer<Kendaraan> serializer = (src, typeOfSrc, context) -> {
            JsonObject jsonObj = context.serialize(src).getAsJsonObject();
            jsonObj.addProperty("jenisKendaraan", src.getClass().getSimpleName()); // Menandai tipe "Mobil" atau "Motor"
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

        if ("Mobil".equalsIgnoreCase(type)) {
            return context.deserialize(json, Mobil.class);
        } else if ("Motor".equalsIgnoreCase(type)) {
            return context.deserialize(json, Motor.class);
        }

        throw new JsonParseException("Tipe kendaraan tidak dikenali: " + type);
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
            return data != null ? data : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal membaca file database kendaraan.");
            return new ArrayList<>();
        }
    }

    // Fungsi untuk menyimpan (SAVE) seluruh data kendaraan kembali ke file json
    public void saveAll(List<Kendaraan> list) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(list, writer);
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menyimpan data kendaraan ke JSON.");
        }
    }
}
