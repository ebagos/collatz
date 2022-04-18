package com.github.ebagos.collatz.literal;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.grpc.stub.StreamObserver;

@SpringBootApplication
public class LiteralApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiteralApplication.class, args);
	}

	@GRpcService
	public static class CalculatorService extends CalculatorGrpc.CalculatorImplBase {
		@Override
		public void calcCollatz(CollatzRequest req, StreamObserver<CollatzResponse> resObserver) {
			Long index_from = req.getIndexFrom();
			Long index_to = req.getIndexTo();
			Pair pair = collatzLoop(index_from, index_to);
			CollatzResponse.Builder res = CollatzResponse.newBuilder()
				.setIndex(pair.second)
				.setMaxLength(pair.first);
			resObserver.onNext(res.build());
			resObserver.onCompleted();
		}

		public static class Pair {
			Long first;
			Long second;
		}

		private static Long collatz(Long n) {
			Long count = 1L;
			while (n > 1L) {
				if (n % 2L == 0L) {
					n /= 2L;
				} else {
					n = n * 3L + 1L;
				}
				count++;
			}
			return count;
		}

		private static Pair collatzLoop(Long s, Long e) {
			Long max = 0L;
			Long index = 0L;
			for (Long i = s	; i < e; i++) {
				Long rc = collatz(i);
				if (rc > max) {
					max = rc;
					index = i;
				}
			}
			Pair result = new Pair();
			result.first = max;
			result.second = index;
			return result;
		}
	}
}
