package com.kmalki.app.utils;

import com.kmalki.app.dtos.post.CoursePostDto;
import com.kmalki.app.entities.Classe;
import com.kmalki.app.entities.Course;
import com.kmalki.app.entities.Discipline;
import com.kmalki.app.entities.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class EntityDtoConvertorTests {
    @Autowired
    private ModelMapper modelMapper;
    private EntityDtoConvertor entityDtoConvertor;

    @BeforeEach
    void setUp() {
        entityDtoConvertor = new EntityDtoConvertor(modelMapper);
    }

    @Test
    void willConvertCourseToCoursePostDto() {
        //given
        Classe classe = new Classe("6A");
        classe.setId(1L);
        Teacher teacher = new Teacher("Sidney", "Reeves", "sidneyreeves@edu.com", Stream.of(Discipline.ENGLISH)
                .collect(Collectors.toCollection(HashSet::new)));
        teacher.setId(1L);
        Course course = new Course(Discipline.ENGLISH, LocalDateTime.now(), LocalDateTime.now(), classe, teacher);
        course.setId(1L);

        //when
        CoursePostDto converted = entityDtoConvertor.convert(course, CoursePostDto.class);

        //then
        assertThat(converted.getBegin_date()).isEqualTo(course.getBegin_date());
        assertThat(converted.getEnd_date()).isEqualTo(course.getEnd_date());
        assertThat(converted.getClasseId()).isEqualTo(course.getClasse().getId());
        assertThat(converted.getId()).isEqualTo(course.getId());
        assertThat(converted.getTeacherId()).isEqualTo(course.getTeacher().getId());
    }
}
