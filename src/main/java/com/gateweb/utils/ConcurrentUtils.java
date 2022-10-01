package com.gateweb.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.*;

public class ConcurrentUtils {
    public static ExecutorService pool = Executors.newFixedThreadPool(9);
    private static final Logger logger = LogManager.getLogger(ConcurrentUtils.class);

    public static void completableGet(Collection<CompletableFuture<Void>> completableFutureCollection) {
        completableFutureCollection.parallelStream().forEach(voidCompletableFuture -> {
            try {
                voidCompletableFuture.get();
            } catch (InterruptedException e) {
                logger.error("Interrupted:{}", e.getMessage());
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                logger.error("executionException:{}", e.getMessage());
            }
        });
    }

    public static Optional callableWithRetry(Callable callable) {
        try {
            return callableWithRetry(callable, 1, 5);
        } catch (Exception e) {
            logger.error("callableWithRetry:{}", e.getMessage());
        }
        return Optional.empty();
    }

    public static Optional callableWithRetry(Callable callable, int retryCount, int retryLimit) throws InterruptedException {
        Optional result = Optional.empty();
        try {
            result = Optional.ofNullable(callable.call());
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            if (retryCount <= retryLimit) {
                //sleep a while
                Thread.sleep(retryCount * 1000);
                result = callableWithRetry(callable, ++retryCount, retryLimit);
            }
            logger.error("OutOfRetryCount:{}", e.getMessage());
        }
        return result;
    }
}
