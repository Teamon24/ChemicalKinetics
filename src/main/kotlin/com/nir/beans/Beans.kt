package com.nir.beans

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.nir.beans.method.GeneralizedMethodComponent
import com.nir.beans.method.GeneralizedMethodInfoComponentImpl
import com.nir.beans.method.HardcodedMethodComponent
import com.nir.beans.reaction.parsing.JacksonComponent
import com.nir.beans.method.generalized.GeneralizedMethodInfoJsonPojo
import com.nir.beans.method.jsonPojos.AdamsBashforthGeneralizedMethodJsonPojo
import com.nir.beans.method.jsonPojos.AdamsMoultonGeneralizedMethodJsonPojo
import com.nir.beans.method.jsonPojos.ExplicitRKGeneralizedMethodJsonPojo
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
    fun generalizedMethodInfoComponent(): GeneralizedMethodInfoComponentImpl {
        val jacksonComponents: List<JacksonComponent<out GeneralizedMethodInfoJsonPojo>> = arrayListOf(
                Pair.of("json/runge-kutta.json", ExplicitRKGeneralizedMethodJsonPojo::class.java),
                Pair.of("json/adams-bashforth.json", AdamsBashforthGeneralizedMethodJsonPojo::class.java),
                Pair.of("json/adams-multon.json", AdamsMoultonGeneralizedMethodJsonPojo::class.java)
        )
                .map(Beans::jacksonComponent)
                .toList()

        return GeneralizedMethodInfoComponentImpl(jacksonComponents)
    }

    fun jacksonComponent(
            jsonAndClass: Pair<String, out Class<out GeneralizedMethodInfoJsonPojo>>
    ): JacksonComponent<out GeneralizedMethodInfoJsonPojo> {
        val filename = jsonAndClass.key
        val aClass: Class<out GeneralizedMethodInfoJsonPojo> = jsonAndClass.value
        return JacksonComponent(filename, aClass)
    }

    @JvmStatic
    fun generalizedMethodComponent(): GeneralizedMethodComponent {
        return GeneralizedMethodComponent()
    }

    @JvmStatic
    fun hardcodedMethodComponent(): HardcodedMethodComponent {
        return HardcodedMethodComponent()
    }
}