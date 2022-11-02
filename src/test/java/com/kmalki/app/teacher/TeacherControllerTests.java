package com.kmalki.app.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmalki.app.controllers.TeacherController;
import com.kmalki.app.entities.Discipline;
import com.kmalki.app.entities.Teacher;
import com.kmalki.app.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TeacherController.class)
public class TeacherControllerTests {

    private final String path = "/api/v1/teacher/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeacherService teacherService;

    @Test
    void willGetTeachers() throws Exception {
        //when
        mockMvc.perform(get(path + "all"))
                //then
                .andExpect(status().isOk());

        verify(teacherService).getTeachers();
    }

    @Test
    void willGetTeacherById() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(get(path + "{id}", id))
                //then
                .andExpect(status().isOk());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(teacherService, times(1)).getTeacher(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(id);
    }

    @Test
    void willGetTeachersByDiscipline() throws Exception {
        //given
        Discipline discipline = Discipline.ENGLISH;
        String disciplinePath = discipline.name();
        Teacher teacher = new Teacher("a", "b", "c", Collections.singleton(discipline));

        given(teacherService.getTeachersByDiscipline(any())).willReturn(Collections.singletonList(teacher));

        //when
        mockMvc.perform(get(path + "discipline/{discipline}", disciplinePath))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo(null)))
                .andExpect(jsonPath("$[0].firstName", equalTo(teacher.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", equalTo(teacher.getLastName())))
                .andExpect(jsonPath("$[0].email", equalTo(teacher.getEmail())))
                .andExpect(jsonPath("$[0].disciplines",
                        equalTo(teacher.getDisciplines().stream().map(Enum::name).collect(Collectors.toList()))));

        ArgumentCaptor<Discipline> disciplineCaptor = ArgumentCaptor.forClass(Discipline.class);
        verify(teacherService, times(1)).getTeachersByDiscipline(disciplineCaptor.capture());
        assertThat(disciplineCaptor.getValue()).isEqualTo(discipline);
    }

    @Test
    void willGetTeachersByDisciplineButNullListFromService() throws Exception {
        //given
        String disciplinePath = Discipline.ENGLISH.name();
        given(teacherService.getTeachersByDiscipline(any())).willReturn(null);

        //when
        mockMvc.perform(get(path + "discipline/{discipline}", disciplinePath))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", empty()));
    }

}
