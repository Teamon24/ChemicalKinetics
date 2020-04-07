package com.nir.utils

import javafx.scene.Node
import javafx.scene.Parent

object CSSUtils {
    fun addCss(parent: Parent, cssResource: String)
    {
        val stockLineChartCss = javaClass.classLoader.getResource(cssResource).toExternalForm()
        parent.stylesheets.add(stockLineChartCss)
    }

    fun addCss(parent: Node, cssClass: String)
    {
        val stockLineChartCss = javaClass.classLoader.getResource(cssClass).toExternalForm()
        parent.styleClass.add(stockLineChartCss)
    }
}