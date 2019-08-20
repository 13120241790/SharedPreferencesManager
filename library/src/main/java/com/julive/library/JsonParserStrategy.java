package com.julive.library;

public interface JsonParserStrategy {
    String encode(Object o);

    Object decode(String jsonString, Class<?> c);
}
