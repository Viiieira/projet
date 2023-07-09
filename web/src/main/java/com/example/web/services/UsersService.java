package com.example.web.services;

import com.example.web.models.CustomerEntity;
import com.example.web.models.UsersModel;
import com.example.web.repository.CustomerRepository;
import com.example.web.repository.UsersRepository;
import org.springframework.stereotype.Service;

import static com.example.web.utils.Utils.*;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final CustomerRepository customerRepository;

    public UsersService(UsersRepository usersRepository, CustomerRepository customerRepository) {

        this.usersRepository = usersRepository;
        this.customerRepository = customerRepository;
    }

    public UsersModel registerUser(String name, String email, String nif, String phone, String password) {
        if (isNull(name, email, nif, phone, password)) {
            return null;
        } else {
            if(usersRepository.findByName(name).isPresent()) {
                // TODO: Turn this Sout into alert for the frontend
                System.out.println("duplicated user");
                return null;
            }
            UsersModel usersModel = new UsersModel();
            usersModel.setName(name);
            usersModel.setPassword(hashPassword(password));
            usersModel.setEmail(email);
            usersModel.setNif(nif);
            usersModel.setPhone(phone);
            UsersModel savedUser = usersRepository.save(usersModel);

            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(savedUser.getId());

            customerRepository.save(customerEntity);
            return savedUser;
        }
    }

    public UsersModel authenticate(String name, String password) {
        UsersModel usersModel = usersRepository.findByName(name).orElse(null);
        if (usersModel != null && verifyPassword(password, usersModel.getPassword())) {
            return usersModel;
        }
        return null;
    }
}
