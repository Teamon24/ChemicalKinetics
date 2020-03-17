package com.nir.ui.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

public final class Beans {

    public static PeriodicElementsGetter periodicElementsGetter() {
        ObjectMapper kotlinMapper = kotlinObjectMapper();
        return new PeriodicElementsGetterLocal(kotlinMapper);
    }

    public static ObjectMapper kotlinObjectMapper() {
        ObjectMapper kotlinMapper = new ObjectMapper();
        kotlinMapper.registerModule(new KotlinModule());
        kotlinMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return kotlinMapper;
    }
}
