package com.nir.beans;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nir.ui.dto.PeriodicElement;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class PeriodicElementsGetterLocal implements PeriodicElementsGetter {
    private static List<PeriodicElement> elements = new ArrayList<>();
    private ObjectMapper objectMapper;

    public PeriodicElementsGetterLocal(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.readElementsIfNoSuch();
    }

    public List<PeriodicElement> getElements() {
        this.readElementsIfNoSuch();
        return elements;
    }

    private void readElementsIfNoSuch() {
        if (elements.isEmpty()) {
            URL resource = this.getClass().getClassLoader().getResource("json/table.json");
            try {
                TypeReference<List<PeriodicElement>> type = new TypeReference<List<PeriodicElement>>() {};
                List<PeriodicElement> elements = objectMapper.readValue(resource, type);
                PeriodicElementsGetterLocal.elements.addAll(elements);
            } catch (IOException e) {
                String template = "Cant read periodic table elements: %s";
                throw new RuntimeException(String.format(template, e.getMessage()));
            }
        }
    }
}
