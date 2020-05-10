package com.nir.beans;

import com.nir.utils.math.method.generalized.GeneralizedMethodInfoJsonPojo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralizedMethodInfoComponentImpl implements GeneralizedMethodInfoComponent {

    private List<JacksonComponent<GeneralizedMethodInfoJsonPojo>> jacksonComponents;

    public GeneralizedMethodInfoComponentImpl(List<JacksonComponent<GeneralizedMethodInfoJsonPojo>> jacksonComponents) {
        this.jacksonComponents = jacksonComponents;
    }

    public List<GeneralizedMethodInfoJsonPojo> getAll() {
        return jacksonComponents.stream()
            .map(JacksonComponent::readAll).flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
