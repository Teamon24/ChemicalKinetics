package com.nir.beans

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.nir.beans.method.MethodConverterComponent
import com.nir.beans.method.MethodInfoComponentImpl
import com.nir.beans.method.HardcodedMethodComponent
import com.nir.beans.method.MethodInfoComponent
import com.nir.beans.reaction.parsing.JacksonComponent
import com.nir.beans.method.generalized.MethodInfoJsonPojo
import com.nir.beans.method.jsonPojos.AdamsBashforthMethodJsonPojo
import com.nir.beans.method.jsonPojos.AdamsMoultonMethodJsonPojo
import com.nir.beans.method.jsonPojos.ExplicitRKMethodJsonPojo
import org.apache.commons.lang3.tuple.Pair

object Beans {
    @JvmStatic
    fun periodicElementsGetter(): PeriodicElementsGetter {
        val kotlinMapper: ObjectMapper = kotlinObjectMapper()
        return PeriodicElementsGetterImpl(kotlinMapper)
    }

    fun kotlinObjectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.registerModule(KotlinModule())
        //        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        return mapper
    }

    @JvmStatic
    fun methodInfoComponent(): MethodInfoComponent {
        val jacksonComponents: List<JacksonComponent<out MethodInfoJsonPojo>> = arrayListOf(
                Pair.of("json/runge-kutta.json", ExplicitRKMethodJsonPojo::class.java),
                Pair.of("json/adams-bashforth.json", AdamsBashforthMethodJsonPojo::class.java),
                Pair.of("json/adams-multon.json", AdamsMoultonMethodJsonPojo::class.java)
        )
                .map(Beans::jacksonComponent)
                .toList()

        return MethodInfoComponentImpl(jacksonComponents)
    }

    fun jacksonComponent(
            jsonAndClass: Pair<String, out Class<out MethodInfoJsonPojo>>
    ): JacksonComponent<out MethodInfoJsonPojo> {
        val filename = jsonAndClass.key
        val aClass: Class<out MethodInfoJsonPojo> = jsonAndClass.value
        return JacksonComponent(filename, aClass)
    }

    @JvmStatic
    fun methodConverterComponent(): MethodConverterComponent {
        return MethodConverterComponent()
    }

    @JvmStatic
    fun hardcodedMethodComponent(): HardcodedMethodComponent {
        return HardcodedMethodComponent()
    }
}