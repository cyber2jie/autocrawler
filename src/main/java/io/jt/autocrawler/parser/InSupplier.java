package io.jt.autocrawler.parser;

import io.jt.autocrawler.model.In;

import java.util.List;

public interface InSupplier {
    List<String> get(In in);
}
