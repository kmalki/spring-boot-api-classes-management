package com.kmalki.app.controllers;

import com.kmalki.app.dtos.post.CoursePostDto;
import com.kmalki.app.entities.Course;
import com.kmalki.app.exceptions.CourseNotFound;
import com.kmalki.app.exceptions.TeacherNotFound;
import com.kmalki.app.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/course")
public class CourseController {

    private final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @Operation(summary = "Create a new course")
    @PostMapping("/")
    ResponseEntity<CoursePostDto> createCourse(@RequestBody CoursePostDto coursePostDto) {
        CoursePostDto result = courseService.createCourse(coursePostDto);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Get a course information using its id")
    @GetMapping("/")
    ResponseEntity<Course> getCourse(@RequestParam(value = "id") Long id) {
        try {
            return new ResponseEntity<>(courseService.getCourse(id), HttpStatus.OK);
        } catch (CourseNotFound e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get all courses information")
    @GetMapping("/all")
    ResponseEntity<List<Course>> getCourses() {
        return new ResponseEntity<>(courseService.getCourses(), HttpStatus.OK);
    }

    @Operation(summary = "Get all courses information for a given teacher id")
    @GetMapping("/all/{id}")
    ResponseEntity<List<Course>> getCourses(@PathVariable("id") Long id) throws TeacherNotFound {
        return new ResponseEntity<>(courseService.getCoursesByTeacher(id), HttpStatus.OK);
    }
}
