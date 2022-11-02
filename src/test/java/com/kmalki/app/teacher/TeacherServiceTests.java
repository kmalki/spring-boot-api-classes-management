package com.kmalki.app.teacher;

import com.kmalki.app.entities.Discipline;
import com.kmalki.app.entities.Teacher;
import com.kmalki.app.exceptions.TeacherEmailAlreadyExists;
import com.kmalki.app.exceptions.TeacherNotFound;
import com.kmalki.app.repositories.TeacherRepository;
import com.kmalki.app.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTests {

    @Mock
    private TeacherRepository teacherRepository;
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherRepository);
    }


    @Test
    void willGetAllTeachers() {
        //when
        teacherService.getTeachers();

        //then
        verify(teacherRepository).findAll();
    }

    @Test
    void willGetTeacherById() {
        Long id = 1L;
        //when
        try {
            teacherService.getTeacher(id);
        } catch (TeacherNotFound ignored) {
        } finally {
            //then
            ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
            verify(teacherRepository).findById(argumentCaptor.capture());
            Long longCaptured = argumentCaptor.getValue();
            assertThat(longCaptured).isEqualTo(id);
        }
    }

    @Test
    void willGetTeachersByDiscipline() {
        //when
        Discipline discipline = Discipline.ENGLISH;
        teacherService.getTeachersByDiscipline(discipline);

        //then
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(teacherRepository).findAllByDiscipline(argumentCaptor.capture());
        Integer integerCaptured = argumentCaptor.getValue();
        assertThat(integerCaptured).isEqualTo(discipline.ordinal());
    }

    @Test
    void willAddTeacher() {
        //given
        Teacher teacher = new Teacher("Sidney", "Reeves", "sidneyreeves@edu.com", Stream.of(Discipline.ENGLISH)
                .collect(Collectors.toCollection(HashSet::new)));

        //when
        teacherService.addTeacher(teacher);

        //then
        ArgumentCaptor<Teacher> argumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).save(argumentCaptor.capture());
        Teacher capturedTeacher = argumentCaptor.getValue();
        assertThat(capturedTeacher).isEqualTo(teacher);
    }

    @Test
    void willThrowTeacherEmailAlreadyExistsInAddTeacher() {
        //given
        Teacher teacher = new Teacher("Sidney", "Reeves", "sidneyreeves@edu.com", Stream.of(Discipline.ENGLISH)
                .collect(Collectors.toCollection(HashSet::new)));
        given(teacherRepository.findByEmail(anyString()))
                .willReturn(Optional.of(teacher));

        //then
        assertThatThrownBy(() -> teacherService.addTeacher(teacher))
                .isInstanceOf(TeacherEmailAlreadyExists.class)
                .hasMessageContaining(String.format("Teacher email %s already exists", teacher.getEmail()));
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void willDeleteATeacher() {
        //given
        Long id = 1L;
        given(teacherRepository.findById(anyLong())).willReturn(Optional.of(new Teacher()));

        //when
        teacherService.deleteTeacher(id);

        //then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(teacherRepository).deleteById(argumentCaptor.capture());
        Long capturedId = argumentCaptor.getValue();
        assertThat(capturedId).isEqualTo(id);
    }

    @Test
    void willThrowTeacherNotFoundInDeleteTeacher() {
        //given
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        given(teacherRepository.findById(teacher.getId()))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> teacherService.deleteTeacher(teacher.getId()))
                .isInstanceOf(TeacherNotFound.class)
                .hasMessageContaining(String.format("Teacher id %d not found", teacher.getId()));
        verify(teacherRepository, never()).deleteById(any());
    }
}
