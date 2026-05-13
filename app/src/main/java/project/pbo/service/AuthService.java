package project.pbo.service;

import project.pbo.domain.User;
import project.pbo.repository.UserRepository;

public class AuthService {
    private UserRepository userRepository = new UserRepository();

    public User login (String username, String password){
        for (User list : userRepository.findAll()){
            if (list.getUsername().equals(username) && list.getPassword().equals(password)){
                return list;
            }
        }
        return null;
    }
}
