package com.platform.general.microservice;

import com.platform.general.microservice.credential.PasswordAuthenticationCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RefreshScope
public class API {
    private static final Logger LOGGER = LoggerFactory.getLogger(API.class);

    @RequestMapping("/password-credential"  )
    void getMessage() {
        LOGGER.info("Esto es una pruebaaaaaaaa Javavavavavavav");
        LOGGER.info("Esto es una pruebaaaaaaaa");
        PasswordAuthenticationCredential.creator().create("HolaHasdasdasdh","javier");
    }
}
