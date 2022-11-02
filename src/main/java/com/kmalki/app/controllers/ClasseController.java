package com.kmalki.app.controllers;

import com.kmalki.app.entities.Classe;
import com.kmalki.app.services.ClasseService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/classe")
public class ClasseController {
    private final Logger logger = LoggerFactory.getLogger(ClasseController.class);

    @Autowired
    private final ClasseService classeService;

    public ClasseController(ClasseService classeService) {
        this.classeService = classeService;
    }


    @Operation(summary = "Get a classe information using its id or name")
    @GetMapping("/")
    ResponseEntity<Classe> getUniqueClasse(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "name", required = false) String name) {
        if (id == null && name == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Classe uniqueClasse = classeService.getUniqueClasse(id, name);
        return new ResponseEntity<>(uniqueClasse, HttpStatus.OK);
    }

    @Operation(summary = "Get all classes information")
    @GetMapping("/all")
    ResponseEntity<List<Classe>> getClasses() {
        return new ResponseEntity<>(classeService.getClasses(), HttpStatus.OK);
    }
}
