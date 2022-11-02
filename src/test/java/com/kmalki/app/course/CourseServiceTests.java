package com.kmalki.app.course;

import com.kmalki.app.dtos.post.CoursePostDto;
import com.kmalki.app.entities.Classe;
import com.kmalki.app.entities.Course;
import com.kmalki.app.entities.Discipline;
import com.kmalki.app.entities.Teacher;
import com.kmalki.app.exceptions.CourseNotFound;
import com.kmalki.app.exceptions.TeacherNotFound;
import com.kmalki.app.exceptions.TeacherNotQualified;
import com.kmalki.app.repositories.CourseRepository;
import com.kmalki.app.services.CourseService;
import com.kmalki.app.services.TeacherService;
import com.kmalki.app.utils.EntityDtoConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTests {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private EntityDtoConvertor entityDtoConvertor;
    @Mock
    private TeacherService teacherService;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseService = new CourseService(courseRepository, entityDtoConvertor, teacherService);
    }


    @Test
    void willGetAllCourses() {
        //when
        courseService.getCourses();

        //then
        verify(courseRepository).findAll();
    }

    @Test
    void willGetCourseById() {
        Long id = 1L;
        //when
        try {
            courseService.getCourse(id);
        } catch (CourseNotFound ignored) {
        } finally {
            //then
            ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
            verify(courseRepository).findById(argumentCaptor.capture());
            Long longCaptured = argumentCaptor.getValue();
            assertThat(longCaptured).isEqualTo(id);
        }
    }

    @Test
    void willGetCoursesGivenATeacherId() {
        Long id = 1L;
        //given
        given(teacherService.getTeacher(id)).willReturn(new Teacher());

        //when
        courseService.getCoursesByTeacher(id);

        //then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(courseRepository).findAllByTeacherId(argumentCaptor.capture());
        Long longCaptured = argumentCaptor.getValue();
        assertThat(longCaptured).isEqualTo(id);
    }

    @Test
    void willReturnNullWhenTeacherDoesntExistInGetCoursesGivenATeacherId() {
        Long id = 1L;
        given(teacherService.getTeacher(id)).willReturn(null);
        assertThat(courseService.getCoursesByTeacher(id)).isNull();
        verify(courseRepository, never()).findAllByTeacherId(any());
    }

    @Test
    void willAddCourse() {
        //given
        Discipline discipline = Discipline.ENGLISH;
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setDisciplines(new HashSet<>(Collections.singletonList(discipline)));

        Classe classe = new Classe("A");
        classe.setId(1L);

        CoursePostDto course = new CoursePostDto(1L, discipline, LocalDateTime.now(),
                LocalDateTime.now(), 1L, teacher.getId());

        Course courseFull = new Course(course.getDiscipline(), course.getBegin_date(), course.getEnd_date(), classe, teacher);
        courseFull.setId(1L);

        given(entityDtoConvertor.convert(course, Course.class)).willReturn(courseFull);
        given(courseRepository.findAnyCourseAlreadyMatchingByTeacherId(any(), any(), any(), any()))
                .willReturn(Optional.empty());
        given(teacherService.getTeacher(any())).willReturn(teacher);

        //when
        courseService.createCourse(course);

        //then
        ArgumentCaptor<Course> argumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(argumentCaptor.capture());
        Course capturedCourse = argumentCaptor.getValue();
        assertThat(capturedCourse).isEqualTo(courseFull);
    }

    @Test
    void willThrowTeacherNotQualifiedWhenAddCourse() {
        //given
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setDisciplines(new HashSet<>());

        CoursePostDto coursePostDto = new CoursePostDto();
        coursePostDto.setId(1L);
        coursePostDto.setTeacherId(1L);
        coursePostDto.setDiscipline(Discipline.ENGLISH);

        Course course = new Course();
        course.setTeacher(teacher);
        course.setDiscipline(Discipline.ENGLISH);

        given(entityDtoConvertor.convert(coursePostDto, Course.class)).willReturn(course);
        given(teacherService.getTeacher(any())).willReturn(teacher);

        //then
        assertThatThrownBy(() -> courseService.createCourse(coursePostDto))
                .isInstanceOf(TeacherNotQualified.class)
                .hasMessageContaining(String.format("Teacher with id %d is not qualified for %s",
                        teacher.getId(), course.getDiscipline().name()));
    }

    @Test
    void willThrowTeacherNotFoundWhenAddCourse() {
        //given
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        CoursePostDto coursePostDto = new CoursePostDto();
        coursePostDto.setId(1L);
        coursePostDto.setTeacherId(1L);

        Course course = new Course();
        course.setTeacher(teacher);

        given(teacherService.getTeacher(anyLong())).willThrow(TeacherNotFound.class);
        given(entityDtoConvertor.convert(coursePostDto, Course.class)).willReturn(course);

        //then
        assertThatThrownBy(() -> courseService.createCourse(coursePostDto))
                .isInstanceOf(TeacherNotFound.class);
        verify(courseRepository, never()).save(any());
    }

    @Test
    void willNotAddACourseAsAvailabilityNotSatisfied() {
        //given
        Discipline discipline = Discipline.ENGLISH;
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setDisciplines(new HashSet<>(Collections.singletonList(discipline)));

        Classe classe = new Classe("A");
        classe.setId(1L);

        CoursePostDto course = new CoursePostDto(1L, discipline, LocalDateTime.now(),
                LocalDateTime.now(), 1L, teacher.getId());

        Course courseFull = new Course(course.getDiscipline(), course.getBegin_date(), course.getEnd_date(), classe, teacher);
        courseFull.setId(1L);

        given(entityDtoConvertor.convert(course, Course.class)).willReturn(courseFull);
        given(teacherService.getTeacher(any())).willReturn(teacher);
        given(courseRepository.findAnyCourseAlreadyMatchingByTeacherId(any(), any(), any(), any()))
                .willReturn(Optional.of(courseFull));

        assertNull(courseService.createCourse(course));
        verify(courseRepository, never()).save(any());
    }

}
