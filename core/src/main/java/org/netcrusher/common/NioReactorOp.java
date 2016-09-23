package org.netcrusher.common;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class NioReactorOp<T> implements Runnable {

    private final CompletableFuture<T> future;

    private final Callable<T> callable;

    public NioReactorOp(Callable<T> callable) {
        this.callable = callable;
        this.future = new CompletableFuture<>();
    }

    @Override
    public void run() {
        try {
            T result = callable.call();
            future.complete(result);
        } catch (Throwable e) {
            future.completeExceptionally(e);
        }
    }

    public CompletableFuture<T> getFuture() {
        return future;
    }

}