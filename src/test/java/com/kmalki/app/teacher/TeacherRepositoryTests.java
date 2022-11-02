package com.kmalki.app.teacher;

import com.kmalki.app.entities.Discipline;
import com.kmalki.app.entities.Teacher;
import com.kmalki.app.repositories.TeacherRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest
public class TeacherRepositoryTests {

    @Autowired
    private TeacherRepository teacherRepository;

    @AfterEach
    void tearDown() {
        teacherRepository.deleteAll();
    }

    @Test
    void itShouldFindTeacherById() {
        //given
        Teacher teacher = new Teacher("Sidney", "Reeves", "sidneyreeves@edu.com", Stream.of(Discipline.ENGLISH)
                .collect(Collectors.toCollection(HashSet::new)));
        //when
        Long id = teacherRepository.save(teacher).getId();
        //then
        Assertions.assertThat(teacherRepository.findById(id)).isPresent();
    }

    @Test
    void itShouldFindExactsTeachers() {
        //given
        Teacher teacher = new Teacher("Sidney", "Reeves", "sidneyreeves@edu.com", Stream.of(Discipline.ENGLISH)
                .collect(Collectors.toCollection(HashSet::new)));
        Teacher teacher2 = new Teacher("Maxine", "Stone", "maxinestone@edu.com", Stream.of(Discipline.ENGLISH)
                .collect(Collectors.toCollection(HashSet::new)));
        //when
        teacherRepository.saveAll(Arrays.asList(teacher, teacher2));
        //then
        Assertions.assertThat(teacherRepository.findAll()).hasSize(2).containsAll(Arrays.asList(teacher, teacher2));
    }

    @Test
    void itShouldFindAllTeachersQualifiedForAGivenDiscipline() {
        Teacher teacher = new Teacher("Sidney", "Reeves", "sidneyreeves@edu.com", Stream.of(Discipline.ENGLISH, Discipline.FRENCH)
                .collect(Collectors.toCollection(HashSet::new)));
        Teacher teacher2 = new Teacher("Maxine", "Stone", "maxinestone@edu.com", Stream.of(Discipline.ENGLISH, Discipline.COMPUTER_SCIENCE)
                .collect(Collectors.toCollection(HashSet::new)));
        Teacher teacher3 = new Teacher("Noelle", "Cooper", "noellecooper@edu.com", new HashSet<>());

        teacherRepository.saveAll(Arrays.asList(teacher, teacher2, teacher3));

        Assertions.assertThat(teacherRepository.findAllByDiscipline(Discipline.COMPUTER_SCIENCE.ordinal())).hasSize(1).contains(teacher2);
    }
}
