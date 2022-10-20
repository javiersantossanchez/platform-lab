package com.platform.general.microservice.web.credential.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class DateManager {

    public LocalDateTime getCurrentLocalDate(){
        return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
    }
}
