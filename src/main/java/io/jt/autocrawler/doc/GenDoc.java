package io.jt.autocrawler.doc;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class GenDoc {
    private static final Logger logger = LoggerFactory.getLogger(GenDoc.class);

    public static void genDoc(String path, Class<?>... cls) {
        for (Class<?> cs : cls) {
            genDoc(path, cs);
        }
    }

    public static void genDoc(String path, Class<?> cls) {
        if (StrUtil.isNotBlank(path) && cls != null) {
            Doc doc = cls.getAnnotation(Doc.class);
            if (doc != null) {
                try {

                    DocBuilder docBuilder = DocBuilder.create();
                    docBuilder.title(cls.getSimpleName());
                    docBuilder.description(doc.value());
                    for (Field field : ReflectUtil.getFields(cls)) {
                        docBuilder.doc(resolveDocList(cls, field, 6));
                    }
                    FileUtil.writeUtf8String(docBuilder.genText(), path + "/" + cls.getSimpleName() + ".txt");


                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    private static DocBuilder.DocList resolveDocList(Class<?> cls, Field field, int deep) {
        DocBuilder.DocList docList = new DocBuilder.DocList();
        docList.setField(field.getName());
        try {
            Doc doc = field.getAnnotation(Doc.class);
            if (doc != null) {
                docList.setDesc(doc.value());
            }
            SubDoc subDoc = field.getAnnotation(SubDoc.class);
            if (subDoc != null && deep > 0) {
                Class<?> subCls = field.getType();
                for (Field subField : ReflectUtil.getFields(subCls)) {
                    docList.getSubDocs().add(resolveDocList(subCls, subField, (deep - 1)));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return docList;
    }
}
