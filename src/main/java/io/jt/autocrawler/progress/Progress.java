package io.jt.autocrawler.progress;

import cn.hutool.core.util.NumberUtil;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Progress {

    private final long total;
    private final AtomicLong complete;
    private final AtomicLong failed;
    private Set<ProgressEventHandler> eventHandlers;

    private Progress(long total) {
        if (total <= 0) throw new ProgressException("进度总量需大于0");
        this.total = total;
        complete = new AtomicLong(0);
        failed = new AtomicLong(0);
        eventHandlers = new LinkedHashSet<>(8);
    }

    public static Progress create(long total) {
        return new Progress(total);
    }

    public Progress registerEvent(ProgressEventHandler handler) {
        Objects.requireNonNull(handler, "进度事件处理器不能为空值");
        eventHandlers.add(handler);
        return this;
    }

    public void fail() {
        failed.getAndIncrement();
        eventHandlers.stream().forEach((handler) -> {
            handler.onFailed(this);
            handler.onProgress(this);
        });
    }

    public void complete() {
        complete.getAndIncrement();
        eventHandlers.stream().forEach(handler -> {
            handler.onComplete(this);
            handler.onProgress(this);
        });
    }

    public Long getTotal() {
        return total;
    }

    public Long getComplete() {
        return complete.get();
    }

    public Long getFailed() {
        return failed.get();
    }

    public Double getCompleteRatio() {
        return NumberUtil.mul(100D, NumberUtil.div(getComplete(), getTotal())).setScale(2).doubleValue();
    }
}
