package com.nir.beans;

import com.nir.utils.math.MethodInfoJsonPojo;

import java.util.List;

public interface MethodInfoRepository {
    List<MethodInfoJsonPojo> getAll();
}
