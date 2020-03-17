package com.nir.ui.reaction

import com.nir.ui.beans.Beans
import javafx.scene.control.ScrollPane
import javafx.scene.layout.GridPane

class PeriodicTable : ScrollPane() {
    private val periodicElementsGetter = Beans.periodicElementsGetter()
    private val elementsGrid: GridPane = GridPane()

    init {
        elementsGrid.vgap = 8.0
        elementsGrid.hgap = 8.0
        super.setContent(elementsGrid)
        super.getStyleClass().add("periodicTable")

        for (element in periodicElementsGetter.elements) {
            elementsGrid.add(ElementPane(element), element.xpos!! - 1, element.ypos!! - 1)
        }
    }

    fun getGrid(): Map<Int, ElementPane> {
        return elementsGrid.children
            .map { node -> node as ElementPane }
            .filter { pane -> castable(pane.lables.first().text) }
            .map { Pair(it.lables.first().text.toInt(), it)}.toMap()
    }

    fun castable(string: String): Boolean {
        return try {
            string.toInt()
            true
        } catch (e: Exception) {
            false
        }
    }
}
