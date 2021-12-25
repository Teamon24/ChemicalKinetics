package com.nir.utils.math.solution

import com.nir.utils.math.XY
import com.nir.utils.math.XsYs
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import java.util.concurrent.FutureTask

interface SolutionFlow<T> {
    suspend fun suspendCollect()
    fun collect()
    fun getFlow(): Flow<T>
    fun getMethodName(): String
}

class SolutionFlowImpl(
    private val methodName: String,
    private val pointsStorage: PointsStorage,
    private val flow: Flow<XY>): SolutionFlow<XY>
{
    override fun getFlow(): Flow<XY> = this.flow

    override suspend fun suspendCollect() {
        this.flow.collect { point -> pointsStorage.addPoint(point) }
    }

    override fun collect() {
        runBlocking {
            flow.collect { point -> pointsStorage.addPoint(point) }
        }
    }

    override fun getMethodName() = this.methodName
}

class SolutionBatchFlow(
    private val methodName: String,
    private val pointsStorage: PointsStorage,
    private val flow: Flow<XsYs>): SolutionFlow<XsYs>
{
    override fun getFlow(): Flow<XsYs> = this.flow

    override suspend fun suspendCollect() {
        this.flow.collect { point -> pointsStorage.addPoints(point) }
    }

    override fun collect() {
        runBlocking {
            flow.collect { point -> pointsStorage.addPoints(point) }
        }
    }

    override fun getMethodName() = this.methodName
}

class SolutionListFlow(
    private val methodName: String,
    private val pointsStorage: PointsStorage,
    private val flow: Flow<List<XY>>): SolutionFlow<List<XY>>
{
    override fun getFlow(): Flow<List<XY>> = this.flow

    override suspend fun suspendCollect() {
        this.flow.collect { point -> pointsStorage.addPoints(point) }
    }

    override fun collect() {
        runBlocking {
            flow.collect { point -> pointsStorage.addPoints(point) }
        }
    }

    override fun getMethodName() = this.methodName
}
