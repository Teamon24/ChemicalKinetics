package com.nir.beans;

import com.nir.utils.math.method.MethodInfoJsonPojo;

import java.util.List;

public interface MethodInfoRepository {
    List<MethodInfoJsonPojo> getAll();
}
