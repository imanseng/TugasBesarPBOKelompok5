package project.pbo.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import project.pbo.domain.Pengguna;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PenggunaRepository {
    private static final String FILE_PATH = "app/data/akun.json";

    // 1. Method Lama Kelompok Anda (Tetap Dipertahankan)
    public List<Pengguna> findAll(){
        try (FileReader reader = new FileReader(FILE_PATH)){
            Type listType = new TypeToken<List<Pengguna>>(){}.getType();
            List<Pengguna> userAkunList = new Gson().fromJson(reader, listType);

            if (userAkunList == null){
                return new ArrayList<>();
            }
            return userAkunList;
        } catch (Exception e){
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
