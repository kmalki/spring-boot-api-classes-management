package com.kmalki.app.services;

import com.kmalki.app.entities.Discipline;
import com.kmalki.app.entities.Teacher;
import com.kmalki.app.exceptions.TeacherEmailAlreadyExists;
import com.kmalki.app.exceptions.TeacherNotFound;
import com.kmalki.app.repositories.TeacherRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TeacherService {

    private final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    @Autowired
    private final TeacherRepository teacherRepository;

    public Teacher getTeacher(Long id) throws TeacherNotFound {
        return teacherRepository.findById(id).orElseThrow(() -> new TeacherNotFound(id));
    }

    public List<Teacher> getTeachers() {
        return teacherRepository.findAll();
    }

    public List<Teacher> getTeachersByDiscipline(Discipline discipline) {
        int disciplineId = discipline.ordinal();
        return teacherRepository.findAllByDiscipline(disciplineId);
    }

    public Teacher addTeacher(Teacher teacher) {
        if (teacherRepository.findByEmail(teacher.getEmail()).isPresent()) {
            throw new TeacherEmailAlreadyExists(String.format("Teacher email %s already exists", teacher.getEmail()), HttpStatus.BAD_REQUEST);
        }
        return teacherRepository.save(teacher);
    }

    public boolean deleteTeacher(Long teacherId) {
        if (teacherRepository.findById(teacherId).isPresent()) {
            teacherRepository.deleteById(teacherId);
            return true;
        }
        throw new TeacherNotFound(teacherId);
    }
}
