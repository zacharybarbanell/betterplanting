package com.zacharybarbanell.betterplanting.config;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;

public class Parser {
    private Parser() {}

    private static final Map<Class<?>, Function<String, ?>> PARSERS = ImmutableMap.<Class<?>, Function<String, ?>>builder()
        .put(String.class, Functions.identity())
        .put(Double.class, Double::valueOf)
        .put(Integer.class, Integer::valueOf) 
        .put(Boolean.class, Boolean::valueOf)
        .build();

    public static <T> Optional<T> parse(String s, Class<T> clazz) {
        if (!PARSERS.containsKey(clazz)) {
            throw new IllegalArgumentException("Type %s has no parser".formatted(clazz));
        }
        else {
            Function<String, T> func = (Function<String, T>) PARSERS.get(clazz);    
            T result;
            try {
                result = func.apply(s);
            }
            catch (Exception e) {
                return Optional.empty(); //error gets logged upstream
            }
            return Optional.of(result);
        }
    }
}
