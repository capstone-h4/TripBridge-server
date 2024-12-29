package com.example.tripbridgeserver.common;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {
    private ModelMapper modelMapper;

    public DtoMapper() {
        modelMapper = new ModelMapper();
        setConfig(modelMapper);
        setMappings(modelMapper);
    }

    private void setConfig(ModelMapper modelMapper) {
        Configuration configuration = modelMapper.getConfiguration();
        configuration.setSkipNullEnabled(true);
    }

    private void setMappings(ModelMapper modelMapper) {
    }

    public <R, T> R transform(T t, Class<R> returnType) {
        R result = modelMapper.map(t, returnType);
        return result;
    }
}
