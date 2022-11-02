package com.kmalki.app.classe;

import com.kmalki.app.controllers.ClasseController;
import com.kmalki.app.services.ClasseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ClasseController.class)
public class ClasseControllerTests {

    private final String path = "/api/v1/classe/";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClasseService classeService;

    @Test
    void willGetClasses() throws Exception {
        //when
        mockMvc.perform(get(path + "all"))
                //then
                .andExpect(status().isOk());

        verify(classeService).getClasses();
    }

    @Test
    void willGetUniqueClasse() throws Exception {
        //given
        Long id = 1L;
        String name = "a";

        //when using id
        mockMvc.perform(get(path).param("id", id.toString()))
                //then
                .andExpect(status().isOk());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(classeService, times(1)).getUniqueClasse(idCaptor.capture(), isNull());
        assertThat(idCaptor.getValue()).isEqualTo(id);

        //when using name
        mockMvc.perform(get(path).param("name", name))
                //then
                .andExpect(status().isOk());

        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        verify(classeService, times(1)).getUniqueClasse(isNull(), nameCaptor.capture());
        assertThat(nameCaptor.getValue()).isEqualTo(name);
    }

    @Test
    void willReturnNullAndBadRequestTryingToGetUniqueClasse() throws Exception {
        //when using name
        mockMvc.perform(get(path))
                //then
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$").doesNotExist());

        verify(classeService, never()).getUniqueClasse(anyLong(), anyString());

    }

//    @Test
//    void willThrowBad() throws Exception {
//        //given
//        given(classeService.getUniqueClasse(anyLong(), anyString())).willThrow(new ClasseNotFound(anyString()));
//
//        //when
//        mockMvc.perform(get(path).param("name", anyString()))
//                //then
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$").doesNotExist());
//
//        verify(classeService, times(1)).getUniqueClasse(anyLong(), anyString());
//    }

//    @Test
//    void willGetTeachersByDiscipline() throws Exception {
//        //given
//        Discipline discipline = Discipline.ENGLISH;
//        String disciplinePath = discipline.name();
//        Teacher teacher = new Teacher("a", "b", "c", Collections.singleton(discipline));
//
//        given(teacherService.getTeachersByDiscipline(any())).willReturn(Collections.singletonList(teacher));
//
//        //when
//        mockMvc.perform(get(path + "discipline/{discipline}", disciplinePath))
//                //then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].id", equalTo(null)))
//                .andExpect(jsonPath("$[0].firstName", equalTo(teacher.getFirstName())))
//                .andExpect(jsonPath("$[0].lastName", equalTo(teacher.getLastName())))
//                .andExpect(jsonPath("$[0].email", equalTo(teacher.getEmail())))
//                .andExpect(jsonPath("$[0].disciplines",
//                        equalTo(teacher.getDisciplines().stream().map(Enum::name).collect(Collectors.toList()))));
//
//        ArgumentCaptor<Discipline> disciplineCaptor = ArgumentCaptor.forClass(Discipline.class);
//        verify(teacherService, times(1)).getTeachersByDiscipline(disciplineCaptor.capture());
//        assertThat(disciplineCaptor.getValue()).isEqualTo(discipline);
//    }
//
//    @Test
//    void willGetTeachersByDisciplineButNullListFromService() throws Exception {
//        //given
//        String disciplinePath = Discipline.ENGLISH.name();
//        given(teacherService.getTeachersByDiscipline(any())).willReturn(null);
//
//        //when
//        mockMvc.perform(get(path + "discipline/{discipline}", disciplinePath))
//                //then
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$", empty()));
//    }

}
