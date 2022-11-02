package com.kmalki.app.utils;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class EntityDtoConvertor {

    @Autowired
    private final ModelMapper modelMapper;

    public <T, E> T convert(E entity, Class<T> t_class) {
        return modelMapper.map(entity, t_class);
    }
}
