package project.pbo.service;

import project.pbo.domain.Pengguna;
import project.pbo.repository.PenggunaRepository;

public class AuthService {
    private PenggunaRepository penggunaRepository = new PenggunaRepository();

    public Pengguna login(String username, String password) {
        for (Pengguna list : penggunaRepository.findAll()) {
            if (list.getUsername().equals(username) && list.getPassword().equals(password)) {
                return list;
            }
        }
        return null;
    }
}
