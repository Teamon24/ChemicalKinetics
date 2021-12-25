package com.nir.utils

import com.nir.beans.method.Method
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object MessageUtils {

    private val decimalFormat: DecimalFormat

    init{
        val nf = NumberFormat.getNumberInstance(Locale.US)
        nf.maximumFractionDigits = 2
        decimalFormat = nf as DecimalFormat
    }

    fun emitionMessage(
        method: Method,
        batchesCounter: Int,
        duration: Long,
        totalTime: Long,
        n: Int,
        totalCounter: Long
    ) = """"${method.name}": batch #$batchesCounter was emitted |${Timer.formatMillis(duration)}|. Total time: ${Timer.formatMillis(totalTime)}. Counted ${totalCounter.separate1000()}/${n.separate1000()}.""".trimMargin()

    fun emitionMessage(method: Method, n: Int, totalCounter: Int) =
        """"${method.name}": value was emitted. Counted ${totalCounter.separate1000()}/${n.separate1000()}.""".trimMargin()

    fun Int.separate1000() = decimalFormat.format(this)
    fun Long.separate1000() = decimalFormat.format(this)
}