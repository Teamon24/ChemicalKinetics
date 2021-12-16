import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun simple(): Flow<Int> = flow { // flow builder
    for (i in 1..15) {
        delay(100) // pretend we are doing something useful here
        emit(i) // emit next value
    }
}

fun simple2(): Flow<Int> = flow {
    println("Flow started")
    for (i in 1..15) {
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    // Launch a concurrent coroutine to check if the main thread is blocked
    val simple1 = simple()
    val simple2 = simple2()
    launch {
        for (k in 1..5) {
            println("I'm not blocked $k")
            delay(300)
        }
    }
    // Collect the flow

    simple().collect { value -> println(value) }

    println("Calling simple function...")
    println("Calling collect...")
    simple2.collect { value -> println(value) }
    println("Calling collect again...")
    simple2.collect { value -> println(value) }
}