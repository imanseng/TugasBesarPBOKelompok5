package project.pbo.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import project.pbo.domain.User;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String FILE_PATH = "app/data/akun.json";

    public List<User> findAll(){
        try (FileReader reader = new FileReader(FILE_PATH)){
        Type lisType = new TypeToken<List<User>>(){}.getType();
        }
    }
}
