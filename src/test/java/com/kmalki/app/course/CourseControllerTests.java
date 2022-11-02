package com.kmalki.app.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmalki.app.controllers.CourseController;
import com.kmalki.app.dtos.post.CoursePostDto;
import com.kmalki.app.exceptions.CourseNotFound;
import com.kmalki.app.services.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CourseController.class)
public class CourseControllerTests {

    private final String path = "/api/v1/course/";

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CourseService courseService;

    @Test
    void willAskAndGetCourses() throws Exception {
        //when
        mockMvc.perform(get(path + "all"))
                //then
                .andExpect(status().isOk());

        verify(courseService).getCourses();
    }

    @Test
    void willAskAndGetCourse() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(get(path).param("id", id.toString()))
                //then
                .andExpect(status().isOk());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(courseService, times(1)).getCourse(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(id);

        verify(courseService).getCourse(anyLong());
    }

    @Test
    void willThrowCourseNotFoundWhenGetCourse() throws Exception {
        //given
        Long id = 1L;
        given(courseService.getCourse(any())).willThrow(CourseNotFound.class);

        //when
        mockMvc.perform(get(path).param("id", id.toString()))
                //then
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void willAskAndGetAllCoursesForAGivenTeacherId() throws Exception {
        //given
        Long id = 1L;

        given(courseService.getCoursesByTeacher(anyLong())).willReturn(new ArrayList<>());

        //when
        mockMvc.perform(get(path + "all/{id}", id))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void willAskCreateCourse() throws Exception {
        //given
        given(courseService.createCourse(any())).willReturn(new CoursePostDto());

        //when
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CoursePostDto())))
                //then
                .andExpect(status().isOk());
    }

    @Test
    void willAskButFailCreateCourse() throws Exception {
        //given
        given(courseService.createCourse(any())).willReturn(null);

        //when
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CoursePostDto())))
                //then
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$").doesNotExist());
        ;
    }
}