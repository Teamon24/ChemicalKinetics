package com.nir.utils;

import java.util.Map;

public class ExpressionUtils {
    public static Double[] vals(Double... doubles) {
        return doubles;
    }
    public static String[] vars(String ... strings) {
        return strings;
    }
    public static Map<String, Double> varsAndVals(
        final String[] variables,
        final Double[] value
    ) {
        return ZipUtils.zipToMap(variables, value);
    }
}
