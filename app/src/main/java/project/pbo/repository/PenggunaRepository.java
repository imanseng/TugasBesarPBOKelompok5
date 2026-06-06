package project.pbo.repository;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import project.pbo.domain.Pengguna;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PenggunaRepository {
    private static final String FILE_PATH = "app/data/akun.json";

    public List<Pengguna> findAll(){
        File file = new File(FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            System.out.println("[INFO] File akun.json belum ada atau masih kosong.");
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)){
            Type listType = new TypeToken<List<Pengguna>>(){}.getType();
            List<Pengguna> userAkunList = new Gson().fromJson(reader, listType);

            if (userAkunList == null){
                return new ArrayList<>();
            }
            return userAkunList;
        } catch (IOException e){
            System.out.println("[ERROR] Gagal membaca file database akun: " + e.getMessage());
            return new ArrayList<>();
        } catch (JsonParseException e) {
            System.out.println("[ERROR] Format JSON akun.json tidak valid: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // TAMBAH METHOD Untuk menyelesaikan di Main.java
    public Pengguna cariPengguna(String username) {
        List<Pengguna> semuaAkun = findAll(); 
        
        // Melakukan looping untuk mencari username yang cocok
        for (Pengguna p : semuaAkun) {
            if (p.getUsername().equalsIgnoreCase(username)) {
                return p; // Ketemu
            }
        }
        return null; // Tidak ketemu
    }
}