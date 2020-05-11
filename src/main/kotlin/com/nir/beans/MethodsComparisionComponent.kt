package com.nir.beans

import com.nir.utils.PlotUtils.dataSets
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.method.F
import com.nir.utils.math.method.Method
import com.nir.utils.math.method.X
import com.nir.utils.math.solution.Solution
import com.nir.utils.to
import de.gsi.dataset.spi.DoubleDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.util.concurrent.FutureTask
import kotlin.math.abs

typealias MethodName = String
typealias FormattedTime = String
typealias VarName = String
typealias VarValues = List<Double>
typealias AnalyticalVarValues = List<Double>
typealias MethodsTasksDatasets = List<Triple<String, Runnable, List<DoubleDataSet>>>

data class Comparision(
        val variableName: String,
        val xValues: DoubleArray,
        val analyticalVariableValues: List<Double>,
        val methodsSolutions: List<MethodSolution>
)

data class MethodSolution(
        val methodName: String,
        val values: List<Double>,
        val deviations: List<Double>,
        val averageDeviation: Double,
        val executionTime: Long
)

class MethodsComparisionComponent {

    fun compare(
            titles: List<String>,
            system: F,
            initialPoint: InitialPoint,
            computationConfigs: ComputationConfigs,
            methods: List<Method>,
            analyticalSolution: Map<String, (X) -> Double>
    ): List<Comparision> {
        methods.forEach { it.setUp(initialPoint, computationConfigs) }

        val methodsRunnablesDatasets = methods.map {
            val dataSets = dataSets(titles)
            it.name to
            Solution
                .method(it)
                .computation(computationConfigs)
                .system(system)
                .initialPoint(initialPoint)
                .datasets(dataSets)
                .futureTask() to
            dataSets
        }


        val methodsAndExecutionTimes = compute(methodsRunnablesDatasets)
        val comparisions = createComparisions(methodsRunnablesDatasets, titles, analyticalSolution, methodsAndExecutionTimes)

        return comparisions.sortedWith(kotlin.Comparator { c1, c2 ->
            titles.indexNameOf(c1).compareTo(titles.indexNameOf(c2))
        })
    }

    private fun List<String>.indexNameOf(c: Comparision) = this.indexOf(c.variableName)

    private fun createComparisions(
            dataSetsList: MethodsTasksDatasets,
            titles: List<String>,
            analyticalSolution: Map<String, (X) -> Double>,
            methodsAndExecutionTimes: Map<MethodName, Long>
    ): List<Comparision> {
        val infos = ArrayList<Triple<VarName, AnalyticalVarValues, Map<MethodName, VarValues>>>()

        val comparisions = runBlocking {
            val xValues = dataSetsList[0].third[0].xValues
            titles.parallelEach { varName ->
                val methodYis = getMethodsResults(varName, dataSetsList)
                val analyticalYis = xValues!!.map { analyticalSolution[varName]!!(it) }
                infos.add(varName to analyticalYis to methodYis)
            }

            infos.parallelMap { info ->
                val analyticalVarValues = info.second
                val third = info.third
                val methodsSolutions = third.entries.map { (method, varValues) ->
                    val deviations = analyticalVarValues.zip(varValues).map { abs(it.first - it.second) }
                    MethodSolution(method, varValues, deviations, deviations.average(), methodsAndExecutionTimes[method]!!)
                }
                Comparision(info.first, xValues, analyticalVarValues, methodsSolutions)
            }
        }
        return comparisions
    }

    private suspend fun getMethodsResults(
            variableName: String,
            methodsTasksDataSets: MethodsTasksDatasets
    )
            : Map<MethodName, VarValues>
    {
        val methodsDatasets = methodsTasksDataSets.map { it.first to it.third }
        return methodsDatasets.parallelMap { methodDatasets ->
            val datasets = methodDatasets.second.first { dataSet -> dataSet.name == variableName }
            val methodName = methodDatasets.first
            methodName to datasets.yValues.toList()
        }.toMap()
    }

    private fun compute(methodsTasksDatasets: List<Triple<String, FutureTask<Long>, List<DoubleDataSet>>>): Map<MethodName, Long> {
        val methodsTasks = methodsTasksDatasets.map { it.first to it.second }
        runBlocking { methodsTasks.parallelEach { it.second.run() } }
        return methodsTasks.map { it.first to it.second.get() }.toMap()
    }
}

suspend fun <A, B> Iterable<A>.parallelMap(f: (A) -> B): List<B> = coroutineScope {
    map { async(Dispatchers.IO) { f(it) } }.awaitAll()
}

suspend fun <A>Collection<A>.parallelEach(f: suspend (A) -> Unit): Unit = coroutineScope {
    map { async(Dispatchers.IO) { f(it) } }.forEach { it.await() }
}