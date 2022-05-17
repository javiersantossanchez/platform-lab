package com.platform.general.microservice.web.credential;


import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebCredentialCreator {

    @Autowired
    WebCredentialRepository repository;

    @Autowired
    PasswordValidator validator;

    public void create(String password, String userName) {
        if(validator.isValid(password)) {
            repository.save();
        }
    }
}
