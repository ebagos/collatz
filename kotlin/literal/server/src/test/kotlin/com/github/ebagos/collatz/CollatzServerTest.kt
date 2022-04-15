package com.github.ebagos.collatz

import io.grpc.testing.GrpcServerRule
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class CollatzServerTest {

    @get:Rule
    val grpcServerRule: GrpcServerRule = GrpcServerRule().directExecutor()

    @Test
    fun calcCollatz() = runBlocking {
        val service = CollatzServer.CollatzService()
        grpcServerRule.serviceRegistry.addService(service)

        val stub = CalculatorGrpcKt.CalculatorCoroutineStub(grpcServerRule.channel)
        val start = 2L
        val end = 999_999L
        val res = stub.calcCollatz(collatzRequest { indexFrom = start; indexTo = end })

        assertEquals(res.index, 837799L)
        assertEquals(res.maxLength, 525L)

        val startNG= 1L
        try {
            stub.calcCollatz(collatzRequest { indexFrom = startNG; indexTo = end })
        } catch (e: io.grpc.StatusException) {
            assertEquals(e.status, io.grpc.Status.INVALID_ARGUMENT)
        }

        val endNG = 111_111_111L
        val res2 = try {
            stub.calcCollatz(collatzRequest { indexFrom = start; indexTo = endNG })
        } catch (e: io.grpc.StatusException) {
            assertEquals(e.status, io.grpc.Status.INVALID_ARGUMENT)
            Pair(0, 0)
        }
        assertEquals(res2, Pair(0, 0))

        val res3 = try {
            stub.calcCollatz(collatzRequest { indexFrom = end; indexTo = start })
        } catch (e: io.grpc.StatusException) {
            assertEquals(e.status, io.grpc.Status.INVALID_ARGUMENT)
            Pair(0, 0)
        }
        assertEquals(res3, Pair(0, 0))
    }
}
