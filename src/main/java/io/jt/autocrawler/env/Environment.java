package io.jt.autocrawler.env;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class Environment {
    public static final class EnvID {
        public static final String ARGV = "argv";

    }

    public final static class EnvironmentValue<T> {
        private Class<T> clazz;
        private T o;
        private String id;

        public EnvironmentValue(Class<T> clazz, T o) {
            this.clazz = clazz;
            this.o = o;
            if (o != null)
                this.id = String.valueOf(o.hashCode());
        }

        public EnvironmentValue(String id, T o) {
            this.o = o;
            this.id = id;
        }

        @Override
        public boolean equals(Object o1) {
            if (this == o1) return true;
            if (o1 == null || getClass() != o1.getClass()) return false;
            EnvironmentValue<?> that = (EnvironmentValue<?>) o1;
            return Objects.equals(clazz, that.clazz) &&
                    Objects.equals(o, that.o) &&
                    Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clazz, o, id);
        }
    }

    private static final Set<EnvironmentValue> values = new HashSet<>();

    private static final AtomicBoolean locked = new AtomicBoolean(Boolean.FALSE);

    private static final ReadWriteLock lock = new ReentrantReadWriteLock(false);

    public static void lock() {
        locked.set(Boolean.TRUE);
    }

    public static <T> void put(T o) {
        if (o != null && !locked.get()) {
            Class<T> cls = (Class<T>) o.getClass();
            try {
                lock.writeLock().lock();
                values.add(new EnvironmentValue(cls, o));
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    public static void put(String id, Object o) {
        if (StrUtil.isNotBlank(id) && o != null && !locked.get()) {
            try {
                lock.writeLock().lock();
                Object od = getById(id);
                if (od != null)
                    values.add(new EnvironmentValue(id, o));
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    public static <T> T getById(String id) {
        T o = null;
        Optional<EnvironmentValue> op = values.stream().filter((v) -> {
            return StrUtil.equals(id, v.id);
        }).findFirst();
        if (op.isPresent()) o = (T) op.get().o;
        return o;
    }

    public static <T> T get(String clsName) {
        return get(ClassUtil.loadClass(clsName));
    }

    public static <T> T get(Class<T> clazz) {
        T o = null;
        Optional<EnvironmentValue> op = values.stream().filter((v) -> {
            return clazz == v.clazz;
        }).findFirst();
        if (op.isPresent()) o = (T) op.get().o;
        else {
            op = values.stream().filter((v) -> {
                return clazz.isAssignableFrom(v.clazz);
            }).findFirst();
            if (op.isPresent()) o = (T) op.get().o;
        }
        return o;
    }
}
