package com.nir.beans;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.nir.utils.math.method.MethodInfoJsonPojo;

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

    public static MethodInfoRepositoryImpl methodInfoRepositoryImpl() {
        String filename = "json/butchers-tables.json";
        final JacksonComponent<MethodInfoJsonPojo> jacksonComponent = jacksonComponent(filename, MethodInfoJsonPojo.class);
        return new MethodInfoRepositoryImpl(jacksonComponent);
    }

    public static <T> JacksonComponent<T> jacksonComponent(String fileName, Class<T> type) {
        return new JacksonComponent<T>(fileName, type);
    }

    public static MethodComponent methodComponent() {
        return new MethodComponent();
    }
}
