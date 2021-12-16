package com.nir.beans.method

import com.nir.beans.method.generalized.MethodInfoJsonPojo
import com.nir.beans.reaction.parsing.JacksonComponent
import java.util.stream.Collectors

class MethodInfoComponentImpl(private val jacksonComponents: List<JacksonComponent<out MethodInfoJsonPojo>>) :
    MethodInfoComponent {
    override val getMethods: List<MethodInfoJsonPojo>
        get() = jacksonComponents.stream()
                .map { obj: JacksonComponent<out MethodInfoJsonPojo> -> obj.readAll() }.flatMap { obj: List<MethodInfoJsonPojo> -> obj.stream() }
                .collect(Collectors.toList())
}