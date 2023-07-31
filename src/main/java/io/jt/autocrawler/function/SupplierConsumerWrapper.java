package io.jt.autocrawler.function;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SupplierConsumerWrapper<T, R> implements Supplier<R> {
    private T o;
    private Consumer<T> consumer;

    public SupplierConsumerWrapper(T o, Consumer<T> consumer) {
        this.o = o;
        this.consumer = consumer;
    }

    @Override
    public R get() {
        consumer.accept(o);
        return null;
    }
}
