package io.jt.autocrawler.util;


import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AsyncUtil {
    private static Logger logger = LoggerFactory.getLogger(AsyncUtil.class);

    public static interface TaskErrorHandler {
        void handle(Task task, Exception e);
    }

    public static class Task implements Runnable {
        private Runnable delegate;
        private TaskErrorHandler errorHandler;

        public Task(Runnable delegate, TaskErrorHandler errorHandler) {
            this.delegate = delegate;
            this.errorHandler = errorHandler;
        }

        @Override
        public void run() {
            logger.debug("task run");
            try {
                delegate.run();
            } catch (Exception e) {
                errorHandler.handle(this, e);
            }
            logger.debug("task end");
        }
    }

    public static final TaskErrorHandler NOOPERRORHANDLER = (task, errorHandler) -> {
    };

    public static void runAsync(Runnable... tasks) {
        runAsync(NOOPERRORHANDLER, tasks);
    }

    public static void runAsync(TaskErrorHandler errorHandler, Runnable... tasks) {
        if (tasks.length > 0) {
            ExecutorService executor = ThreadUtil.newFixedExecutor(tasks.length, "async-task-runner-", false);
            List<CompletableFuture> futures = Arrays.asList(tasks).stream().map((r) -> {
                return CompletableFuture.runAsync((r instanceof Task ? (Task) r : new Task(r, errorHandler)), executor);
            }).collect(Collectors.toList());
            try {
                CompletableFuture.allOf(ArrayUtil.toArray(futures, CompletableFuture.class)).get();
            } catch (Exception e) {
                logger.warn("task get error,{}", e.getMessage());
            }
            logger.debug("all task end");
            executor.shutdown();
            logger.debug("shutdown task pool");
        }
    }

    public static <T> List<T> asyncSupplyAll(ExecutorService executor, Supplier<T>... suppliers) {
        List<T> results = new ArrayList<>();
        List<CompletableFuture> futures = Arrays.asList(suppliers).stream().map(s -> {
            return CompletableFuture.supplyAsync(s, executor);
        }).collect(Collectors.toList());
        futures.stream().forEach(f -> {
            try {
                T o = (T) f.get();
                results.add(o);
            } catch (Exception e) {
                logger.warn(" supplier get error,{}", e.getMessage());
            }
        });
        return results;
    }

    public static <T> List<T> asyncSupplyAll(int poolSize, Supplier<T>... suppliers) {
        List<T> results = new ArrayList<>();
        if (suppliers.length > 0) {
            ExecutorService executor = ThreadUtil.newFixedExecutor(poolSize, "async-supplier-runner-", false);
            results.addAll(asyncSupplyAll(executor, suppliers));
            logger.debug("all supplier  end");
            executor.shutdown();
            logger.debug("shutdown supplier pool");
        }
        return results;
    }

    public static <T> List<T> asyncSupplyAll(Supplier<T>... suppliers) {
        return asyncSupplyAll(suppliers.length, suppliers);
    }
}
