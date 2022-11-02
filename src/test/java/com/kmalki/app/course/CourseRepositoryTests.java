package com.kmalki.app.course;

import com.kmalki.app.entities.Classe;
import com.kmalki.app.entities.Course;
import com.kmalki.app.entities.Discipline;
import com.kmalki.app.entities.Teacher;
import com.kmalki.app.repositories.ClasseRepository;
import com.kmalki.app.repositories.CourseRepository;
import com.kmalki.app.repositories.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CourseRepositoryTests {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ClasseRepository classeRepository;


    @BeforeEach
    void fixtures() {
        Teacher teacher = new Teacher("Sidney", "Reeves", "sidneyreeves@edu.com", Stream.of(Discipline.ENGLISH)
                .collect(Collectors.toCollection(HashSet::new)));
        Teacher teacher2 = new Teacher("Maxine", "Stone", "maxinestone@edu.com", Stream.of(Discipline.FRENCH)
                .collect(Collectors.toCollection(HashSet::new)));

        teacher = teacherRepository.save(teacher);
        teacher2 = teacherRepository.save(teacher2);

        Classe classe = new Classe("A");
        Classe classe2 = new Classe("B");

        classe = classeRepository.save(classe);
        classe2 = classeRepository.save(classe2);

        Course course = new Course(Discipline.ENGLISH, LocalDateTime.now(), LocalDateTime.now(), classe, teacher);
        Course course2 = new Course(Discipline.FRENCH, LocalDateTime.now(), LocalDateTime.now(), classe2, teacher2);

        courseRepository.saveAll(Arrays.asList(course, course2));
    }

    @AfterEach
    void tearDown() {
        courseRepository.deleteAll();
    }

    @Test
    void willReturnCoursesForAGivenTeacherId() {
        //when
        assertThat(courseRepository.findAllByTeacherId(1L)).hasSize(1)
                .first().hasFieldOrPropertyWithValue("Discipline", Discipline.ENGLISH);
    }
}
