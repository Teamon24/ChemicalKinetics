package com.nir.beans.method

import com.nir.beans.method.generalized.GeneralizedMethodInfoJsonPojo

interface GeneralizedMethodInfoComponent {
    val getMethods: List<GeneralizedMethodInfoJsonPojo>
}