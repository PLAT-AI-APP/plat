package com.plat.platai.service;

import com.plat.platai.entity.Persona;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonaService {

    public Persona getById(long id) {
        return Persona.builder().build();
    }

}
