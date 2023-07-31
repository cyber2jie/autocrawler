package io.jt.autocrawler.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class KryoUtil {
    private static final Logger logger = LoggerFactory.getLogger(KryoUtil.class);
    private static Kryo kryo;

    static {
        kryo = new Kryo();
        kryo.register(ArrayList.class);
        kryo.register(Vector.class);
        kryo.register(HashSet.class);
        kryo.register(HashMap.class);
        kryo.register(LinkedHashMap.class);
        kryo.register(ConcurrentHashMap.class);

    }

    public static ByteArrayOutputStream serialize(Object o) {
        ByteArrayOutputStream outputStream = null;
        if (o != null) {
            try {
                kryo.register(o.getClass());
                outputStream = new ByteArrayOutputStream();
                Output out = new Output(outputStream);
                kryo.writeObject(out, o);
                out.flush();
                out.close();
            } catch (Exception e) {
                outputStream = null;
                logger.warn("kryo 序列化失败{}", e.getMessage());
            }

        }
        return outputStream;
    }

    public static <T> T deserialize(ByteArrayInputStream inputStream, Class<T> cls) {
        T o = null;
        try {
            Input input = new Input(inputStream);
            kryo.register(cls);
            o = kryo.readObject(input, cls);
            input.close();
        } catch (Exception e) {
            logger.warn("kryo 反序列化失败{}", e.getMessage());
        }
        return o;
    }

    public static void registType(Class<?> cls) {
        kryo.register(cls);
    }

}
