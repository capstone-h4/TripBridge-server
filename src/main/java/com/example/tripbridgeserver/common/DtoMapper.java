package com.example.tripbridgeserver.common;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    // 객체 매핑
    public <R, T> List<R> transform(List<T> list, Class<R> returnType) {
        if( list == null) return null;

        List<R> resultList = new ArrayList<>();
        if (!ArrayUtils.isEmpty(list)) {
                for (T t : list) {
                    R result = modelMapper.map(t, returnType);
                    resultList.add(result);
                }
            return resultList;
        }
        return resultList;
    }

    public <R, T> R transform(T t, Class<R> returnType) {
        R result = modelMapper.map(t, returnType);
        return result;
    }
}
