package com.kmalki.app.classe;

import com.kmalki.app.entities.Classe;
import com.kmalki.app.exceptions.ClasseNotFound;
import com.kmalki.app.repositories.ClasseRepository;
import com.kmalki.app.services.ClasseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClasseServiceTests {

    @Mock
    private ClasseRepository classeRepository;
    private ClasseService classeService;

    @BeforeEach
    void setUp() {
        classeService = new ClasseService(classeRepository);
    }

    @Test
    void willGetAllClasses() {
        //given
        classeService.getClasses();

        //then
        verify(classeRepository).findAll();
    }

    @Test
    void willGetUniqueClasseById() {
        //given
        Long id = 1L;
        given(classeRepository.findById(anyLong())).willReturn(Optional.of(new Classe()));
        //when
        classeService.getUniqueClasse(id, null);
        //then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(classeRepository).findById(argumentCaptor.capture());
        Long capturedId = argumentCaptor.getValue();
        assertThat(capturedId).isEqualTo(id);
    }

    @Test
    void willGetUniqueClasseByName() {
        //given
        String name = "A";
        given(classeRepository.findByName(anyString())).willReturn(Optional.of(new Classe()));
        //when
        classeService.getUniqueClasse(null, name);
        //then
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(classeRepository).findByName(argumentCaptor.capture());
        String capturedName = argumentCaptor.getValue();
        assertThat(capturedName).isEqualTo(name);
    }

    @Test
    void willThrowClasseNotFound() {
        //given
        given(classeRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> classeService.getUniqueClasse(1L, null))
                .isInstanceOf(ClasseNotFound.class)
                .hasMessageContaining(String.format("Classe id %d not found", 1L));
        assertThatThrownBy(() -> classeService.getUniqueClasse(null, "A"))
                .isInstanceOf(ClasseNotFound.class)
                .hasMessageContaining(String.format("Classe named %s not found", "A"));
    }
}
