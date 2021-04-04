package com.heisenberg.logeasy.backend.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationUtils {

    public String getId(){
        return UUID.randomUUID().toString() ;
    }
}
