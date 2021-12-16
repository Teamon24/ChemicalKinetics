package com.nir.beans.method

import com.nir.beans.method.generalized.MethodInfoJsonPojo

interface MethodInfoComponent {
    val getMethods: List<MethodInfoJsonPojo>
}