package io.jt.autocrawler.util;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Map;

public class VelocityUtil {
    public static String render(Map<String, Object> context, String content) {
        StringWriter writer = new StringWriter();
        Velocity.evaluate(new VelocityContext(context), writer, "", content);
        writer.flush();
        return writer.toString();
    }

}
