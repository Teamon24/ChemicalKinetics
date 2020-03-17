package com.nir.ui.utils

import javafx.scene.layout.Region

object Height {

    class ToStep
    internal constructor(private val child: Region) {
        fun to(root: Region) {
            val childProp = child.minHeightProperty()
            val rootProp = root.heightProperty()
            childProp.bind(rootProp)
        }
    }

    @JvmStatic
    fun bind(child: Region): ToStep {
        return ToStep(child)
    }
}

object Width {

    class ToStep
    internal constructor(private val child: Region) {
        fun to(root: Region) {
            val childProp = child.prefWidthProperty()
            val rootProp = root.widthProperty()
            childProp.bind(rootProp)
        }
    }

    @JvmStatic
    fun bind(child: Region): ToStep {
        return ToStep(child)
    }
}