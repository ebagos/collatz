package com.github.ebagos.collatz

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import com.github.ebagos.collatz.CalculatorGrpcKt.CalculatorCoroutineStub
import java.io.Closeable
import java.util.concurrent.TimeUnit

class CollatzClient(private val channel: ManagedChannel) : Closeable {
    private val stub: CalculatorCoroutineStub = CalculatorCoroutineStub(channel)

    suspend fun calculate(start: Long, end: Long) {
        val request = collatzRequest { this.indexFrom = start; this.indexTo = end }
        val response = stub.calcCollatz(request)
        println("Received: length = ${response.maxLength} at ${response.index}")
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

suspend fun main(args: Array<String>) {
    val port = 50051

    val start = try {
        args[0].toLong()
    } catch (e: Exception) {
        2L
    }
    val end = try {
        args[1].toLong()
    } catch (e: Exception) {
        99_999_999L
    }
    println("${start} - ${end}")
    val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()

    val client = CollatzClient(channel)

    client.calculate(start, end)
}
