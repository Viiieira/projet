package com.example.web.services;

import com.example.web.models.UsersModel;
import com.example.web.repository.UsersRepository;
import org.springframework.stereotype.Service;

import static com.example.web.utils.Utils.*;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UsersModel registerUser(String login, String password, String email) {
        if (isNull(login, email, password)) {
            return null;
        } else {
            if(usersRepository.findByLoginOrEmail(login, email).isPresent()) {
                // TODO: Turn this Sout into alert for the frontend
                System.out.println("duplicated user");
                return null;
            }
            UsersModel usersModel = new UsersModel();
            usersModel.setLogin(login);
            usersModel.setPassword(hashPassword(password));
            usersModel.setEmail(email);
            return usersRepository.save(usersModel);
        }
    }

    public UsersModel authenticate(String login, String password) {
        UsersModel usersModel = usersRepository.findByLogin(login).orElse(null);
        if (usersModel != null && verifyPassword(password, usersModel.getPassword())) {
            return usersModel;
        }
        return null;
    }
}
