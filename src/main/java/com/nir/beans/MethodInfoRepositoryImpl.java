package com.nir.beans;

import com.nir.utils.math.MethodInfoJsonPojo;

import java.util.ArrayList;
import java.util.List;

public class MethodInfoRepositoryImpl implements MethodInfoRepository {

    private JacksonComponent<MethodInfoJsonPojo> jacksonComponent;

    public MethodInfoRepositoryImpl(JacksonComponent<MethodInfoJsonPojo> jacksonComponent) {
        this.jacksonComponent = jacksonComponent;
    }

    public List<MethodInfoJsonPojo> getAll() {
        return jacksonComponent.readAll();
    }
}
