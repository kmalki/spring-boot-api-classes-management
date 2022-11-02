package com.kmalki.app.controllers;

import com.kmalki.app.entities.Discipline;
import com.kmalki.app.entities.Teacher;
import com.kmalki.app.services.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/teacher")
public class TeacherController {

    private final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Operation(summary = "Get a teacher information using his id")
    @GetMapping("/{id}")
    ResponseEntity<Teacher> getTeacher(@PathVariable("id") Long id) {
        return new ResponseEntity<>(teacherService.getTeacher(id), HttpStatus.OK);
    }

    @Operation(summary = "Get all teachers information")
    @GetMapping("/all")
    ResponseEntity<List<Teacher>> getTeachers() {
        return new ResponseEntity<>(teacherService.getTeachers(), HttpStatus.OK);
    }

    @Operation(summary = "Get all teachers qualified for a given discipline")
    @GetMapping(value = "/discipline/{discipline}")
    ResponseEntity<List<Teacher>> getTeachersByDiscipline(@PathVariable Discipline discipline) {
        List<Teacher> teachersByDiscipline = teacherService.getTeachersByDiscipline(discipline);
        if (teachersByDiscipline == null) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(teachersByDiscipline, HttpStatus.OK);
    }
}
