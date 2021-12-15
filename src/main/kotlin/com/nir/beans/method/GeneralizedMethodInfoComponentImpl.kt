package com.nir.beans.method

import com.nir.beans.method.generalized.GeneralizedMethodInfoJsonPojo
import com.nir.beans.reaction.parsing.JacksonComponent
import java.util.stream.Collectors

class GeneralizedMethodInfoComponentImpl(private val jacksonComponents: List<JacksonComponent<out GeneralizedMethodInfoJsonPojo>>) :
    GeneralizedMethodInfoComponent {
    override val getMethods: List<GeneralizedMethodInfoJsonPojo>
        get() = jacksonComponents.stream()
                .map { obj: JacksonComponent<out GeneralizedMethodInfoJsonPojo> -> obj.readAll() }.flatMap { obj: List<GeneralizedMethodInfoJsonPojo> -> obj.stream() }
                .collect(Collectors.toList())
}