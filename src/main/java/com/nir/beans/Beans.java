package com.nir.beans;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.google.common.collect.Lists;
import com.nir.utils.math.method.MethodInfoJsonPojo;
import com.nir.utils.math.method.jsonPojos.AdamsBashforthMethodJsonPojo;
import com.nir.utils.math.method.jsonPojos.AdamsMoultonMethodJsonPojo;
import com.nir.utils.math.method.jsonPojos.ExplicitRKMethodJsonPojo;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;

public final class Beans {

    public static PeriodicElementsGetter periodicElementsGetter() {
        ObjectMapper kotlinMapper = kotlinObjectMapper();
        return new PeriodicElementsGetterLocal(kotlinMapper);
    }

    public static ObjectMapper kotlinObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new KotlinModule());
//        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return mapper;
    }

    public static MethodInfoComponentImpl methodInfoComponent() {
        final List<JacksonComponent<MethodInfoJsonPojo>> jacksonComponents =
            Lists.newArrayList(
                Pair.of("json/runge-kutta.json", ExplicitRKMethodJsonPojo.class),
                Pair.of("json/adams-bashforth.json", AdamsBashforthMethodJsonPojo.class),
                Pair.of("json/adams-multon.json", AdamsMoultonMethodJsonPojo.class)
            )
            .stream()
            .map(Beans::jacksonComponent)
            .collect(Collectors.toList());
        return new MethodInfoComponentImpl(jacksonComponents);
    }

    public static JacksonComponent<MethodInfoJsonPojo> jacksonComponent(
        Pair<
            String,
            ? extends Class<? extends MethodInfoJsonPojo>
        >
            jsonAndClass
    )
    {
        final String filename = jsonAndClass.getKey();
        final Class<? extends MethodInfoJsonPojo> aClass = jsonAndClass.getValue();
        return new JacksonComponent<>(filename, aClass);
    }
    public static MethodComponent methodComponent() {
        return new MethodComponent();
    }
}
