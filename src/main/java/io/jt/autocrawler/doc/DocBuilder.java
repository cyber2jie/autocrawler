package io.jt.autocrawler.doc;

import io.jt.autocrawler.lang.StrUtilX;

import java.util.ArrayList;
import java.util.List;


public class DocBuilder {

    public static class DocList {
        String field;
        String desc;
        List<DocList> subDocs = new ArrayList<>();

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<DocList> getSubDocs() {
            return subDocs;
        }

        public void setSubDocs(List<DocList> subDocs) {
            this.subDocs = subDocs;
        }
    }

    public static DocBuilder create() {
        return new DocBuilder();
    }

    private String title;
    private String description;
    private List<DocList> docs = new ArrayList<>();

    public DocBuilder title(String title) {
        this.title = title;
        return this;
    }

    public DocBuilder description(String description) {
        this.description = description;
        return this;
    }

    public DocBuilder doc(DocList doc) {
        this.docs.add(doc);
        return this;
    }

    public final char BLANK = ' ';
    public final int LEFT_ALIGN = 4;

    public String genText() {
        StringBuilder sb = new StringBuilder();
        sb.append(StrUtilX.nullToBlank(title));
        sb.append(makeChars(BLANK, LEFT_ALIGN));
        sb.append(StrUtilX.nullToBlank(description));
        nextLine(sb);

        for (DocList doc : docs) {
            sb.append(genDocRecursive(doc, LEFT_ALIGN));
            nextLine(sb);
        }

        return sb.toString();
    }

    private String genDocRecursive(DocList doc, int layer) {

        StringBuilder sb = new StringBuilder();
        sb.append(makeChars(BLANK, layer));
        sb.append(StrUtilX.nullToBlank(doc.getField()));
        sb.append(makeChars(BLANK, LEFT_ALIGN));
        sb.append(StrUtilX.nullToBlank(doc.getDesc()));
        nextLine(sb);
        for (DocList subDoc : doc.getSubDocs()) {
            sb.append(genDocRecursive(subDoc, layer + LEFT_ALIGN));
            nextLine(sb);
        }
        return sb.toString();
    }

    public void nextLine(StringBuilder sb) {
        sb.append("\n");
    }

    public String makeChars(char ch, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) sb.append(ch);
        return sb.toString();
    }
}
