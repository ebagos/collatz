package com.github.ebagos.collatz

import com.google.rpc.BadRequest
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.protobuf.ProtoUtils

class CollatzServer(private val port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(CollatzService())
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@CollatzServer.stop()
                println("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    internal class CollatzService : CalculatorGrpcKt.CalculatorCoroutineImplBase() {
        override suspend fun calcCollatz(request: CollatzRequest) = collatzResponse {
            validate(request)
            val result = collatzLoop(request.indexFrom, request.indexTo)
            maxLength = result.first
            index = result.second
        }

        private fun validate(request: CollatzRequest) {
            if (request.indexFrom > request.indexTo) {
                val valueError = BadRequest.FieldViolation.newBuilder()
                        .setField("indexFrom")
                        .setDescription("index_from > index_to").build()
                val badRequestError = BadRequest.newBuilder()
                        .addFieldViolations(valueError).build()
                val errorDetail = Metadata()
                errorDetail.put(ProtoUtils.keyForProto(badRequestError), badRequestError)
                throw StatusException(Status.INVALID_ARGUMENT, errorDetail)
            }
            if (request.indexFrom < 2 || request.indexTo > 100_000_000L) {
                val valueError = BadRequest.FieldViolation.newBuilder()
                        .setField("indexFrom")
                        .setDescription("out of range").build()
                val badRequestError = BadRequest.newBuilder()
                        .addFieldViolations(valueError).build()
                val errorDetail = Metadata()
                errorDetail.put(ProtoUtils.keyForProto(badRequestError), badRequestError)
                throw StatusException(Status.INVALID_ARGUMENT, errorDetail)
            }
        }

        private fun collatz(n: Long) : Long {
            var count = 1L
            var m = n
            while (m > 1L) {
                if (m % 2L == 0L) {
                    m /= 2L
                } else {
                    m = m * 3L + 1L
                }
                count++
            }
            return count
        }

        private fun collatzLoop(start: Long, end: Long) : Pair<Long, Long> {
            var max = 0L
            var key = 0L
            for (i in start..end) {
                var rc = collatz(i)
                if (rc > max) {
                    max = rc
                    key = i
                }
            }
            return Pair(max, key)
        }
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50051
    val server = CollatzServer(port)
    server.start()
    server.blockUntilShutdown()
}
