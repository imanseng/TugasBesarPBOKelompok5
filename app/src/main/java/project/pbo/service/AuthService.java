package project.pbo.service;

import project.pbo.domain.Pengguna;
import project.pbo.repository.PenggunaRepository;

public class AuthService {
    private final PenggunaRepository penggunaRepository;

    public AuthService(PenggunaRepository penggunaRepository) {
        this.penggunaRepository = penggunaRepository;
    }

    public Pengguna login(String username, String password) {
        Pengguna pengguna = penggunaRepository.findByUsername(username);

        if (pengguna != null && pengguna.getPassword().equals(password)) {
            return pengguna;
        }

        return null;
    }
}


// public class AuthService {
//     private PenggunaRepository penggunaRepository = new PenggunaRepository();

//     public Pengguna login (String username, String password){
//         for (Pengguna list : penggunaRepository.findAll()){
//             if (list.getUsername().equals(username) && list.getPassword().equals(password)){
//                 return list;
//             }
//         }
//         return null;
//     }
// }
