package com.nir.utils;

public final class CSS {

    private static String elementsType;
    private static String components;

    public static String elementsType() {
        if (elementsType == null) {
            elementsType = CSS.class.getClassLoader().getResource("./css/elementsType.scss").toExternalForm();
        }
        return elementsType;
    }

    public static String components() {
        if (components == null) {
            components = CSS.class.getClassLoader().getResource("./css/components.scss").toExternalForm();
        }
        return components;
    }
}
