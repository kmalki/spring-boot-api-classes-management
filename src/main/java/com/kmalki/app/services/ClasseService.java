package com.kmalki.app.services;

import com.kmalki.app.entities.Classe;
import com.kmalki.app.exceptions.ClasseNotFound;
import com.kmalki.app.repositories.ClasseRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ClasseService {

    private final Logger logger = LoggerFactory.getLogger(ClasseService.class);

    @Autowired
    private final ClasseRepository classeRepository;

    public List<Classe> getClasses() {
        return classeRepository.findAll();
    }


    public Classe getUniqueClasse(Long id, String name) {
        if (id != null) {
            return classeRepository.findById(id).orElseThrow(() -> new ClasseNotFound(String.format("Classe id %d not found", id)));
        } else {
            return classeRepository.findByName(name).orElseThrow(() -> new ClasseNotFound(String.format("Classe named %s not found", name)));
        }
    }
}
