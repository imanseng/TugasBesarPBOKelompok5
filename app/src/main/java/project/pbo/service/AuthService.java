package project.pbo.service;

import project.pbo.domain.Pengguna;
import project.pbo.repository.UserRepository;

public class AuthService {
    private UserRepository userRepository = new UserRepository();

    public Pengguna login (String username, String password){
        for (Pengguna list : userRepository.findAll()){
            if (list.getUsername().equals(username) && list.getPassword().equals(password)){
                return list;
            }
        }
        return null;
    }
}
