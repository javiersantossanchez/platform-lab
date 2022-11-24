package com.platform.general.microservice.web.credential.adapters.web.security;

import com.platform.general.microservice.web.credential.exceptions.InvalidUserInformationException;
import lombok.Data;

import java.security.Principal;
import java.util.UUID;

@Data
public class UserWrapper {

    private final Principal principal;

    public UUID getId(){
        if(principal == null){
            throw new InvalidUserInformationException();
        }
        try {
            return UUID.fromString(principal.getName());
        }catch(Exception ex){
            throw new InvalidUserInformationException();
        }
    }
}
