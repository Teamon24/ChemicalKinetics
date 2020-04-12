package com.nir.beans;

import com.nir.utils.math.method.MethodInfoJsonPojo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MethodInfoComponentImpl implements MethodInfoComponent {

    private List<JacksonComponent<MethodInfoJsonPojo>> jacksonComponents;

    public MethodInfoComponentImpl(List<JacksonComponent<MethodInfoJsonPojo>> jacksonComponents) {
        this.jacksonComponents = jacksonComponents;
    }

    public List<MethodInfoJsonPojo> getAll() {
        return jacksonComponents.stream().map(JacksonComponent<MethodInfoJsonPojo>::readAll).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
