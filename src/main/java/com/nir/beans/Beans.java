package com.nir.beans;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.google.common.collect.Lists;
import com.nir.utils.math.method.generalized.GeneralizedMethodInfoJsonPojo;
import com.nir.utils.math.method.jsonPojos.AdamsBashforthGeneralizedMethodJsonPojo;
import com.nir.utils.math.method.jsonPojos.AdamsMoultonGeneralizedMethodJsonPojo;
import com.nir.utils.math.method.jsonPojos.ExplicitRKGeneralizedMethodJsonPojo;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;

public final class Beans {

    public static PeriodicElementsGetter periodicElementsGetter() {
        ObjectMapper kotlinMapper = kotlinObjectMapper();
        return new PeriodicElementsGetterImpl(kotlinMapper);
    }

    public static ObjectMapper kotlinObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new KotlinModule());
//        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return mapper;
    }

    public static GeneralizedMethodInfoComponentImpl generalizedMethodInfoComponent() {
        final List<JacksonComponent<GeneralizedMethodInfoJsonPojo>> jacksonComponents =
            Lists.newArrayList(
                Pair.of("json/runge-kutta.json", ExplicitRKGeneralizedMethodJsonPojo.class),
                Pair.of("json/adams-bashforth.json", AdamsBashforthGeneralizedMethodJsonPojo.class),
                Pair.of("json/adams-multon.json", AdamsMoultonGeneralizedMethodJsonPojo.class)
            )
            .stream()
            .map(Beans::jacksonComponent)
            .collect(Collectors.toList());
        return new GeneralizedMethodInfoComponentImpl(jacksonComponents);
    }

    public static JacksonComponent<GeneralizedMethodInfoJsonPojo> jacksonComponent(
        Pair<
            String,
            ? extends Class<? extends GeneralizedMethodInfoJsonPojo>
        >
            jsonAndClass
    )
    {
        final String filename = jsonAndClass.getKey();
        final Class<? extends GeneralizedMethodInfoJsonPojo> aClass = jsonAndClass.getValue();
        return new JacksonComponent<>(filename, aClass);
    }

    public static GeneralizedMethodComponent generalizedMethodComponent() {
        return new GeneralizedMethodComponent();
    }

    public static HardcodedMethodComponent hardcodedMethodComponent() {
        return new HardcodedMethodComponent();
    }
}
