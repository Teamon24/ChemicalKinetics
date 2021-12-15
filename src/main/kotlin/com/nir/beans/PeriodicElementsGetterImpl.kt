package com.nir.beans

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.nir.beans.PeriodicElementsGetter
import com.nir.ui.pojos.PeriodicElement
import com.nir.beans.PeriodicElementsGetterImpl
import java.io.IOException
import java.lang.RuntimeException
import java.util.ArrayList

class PeriodicElementsGetterImpl(private val objectMapper: ObjectMapper) : PeriodicElementsGetter {
    init {
        readElementsIfNoSuch()
    }

    override val elements: List<PeriodicElement>
        get() {
            readElementsIfNoSuch()
            return Companion.elements
        }

    private fun readElementsIfNoSuch() {
        if (Companion.elements.isEmpty()) {
            val resource = this.javaClass.classLoader.getResource("json/table.json")
            try {
                val type: TypeReference<List<PeriodicElement>> = object : TypeReference<List<PeriodicElement>>() {}
                val elements = objectMapper.readValue(resource, type)
                Companion.elements.addAll(elements)
            } catch (e: IOException) {
                val template = "Cant read periodic table elements: %s"
                throw RuntimeException(String.format(template, e.message))
            }
        }
    }

    companion object {
        private val elements: MutableList<PeriodicElement> = ArrayList()
    }
}