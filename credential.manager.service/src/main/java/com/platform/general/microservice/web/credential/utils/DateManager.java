package com.platform.general.microservice.web.credential.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateManager {

    public LocalDateTime getCurrentLocalDate(){
        return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
    }
}
