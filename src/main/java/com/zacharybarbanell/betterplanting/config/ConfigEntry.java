package com.zacharybarbanell.betterplanting.config;

import java.util.function.Predicate;

public interface ConfigEntry<T> {
    T get();

    ConfigEntry<T> comment(String comment);
    
    ConfigEntry<T> addRestriction(Predicate<T> restriction);

    default <F extends Comparable<T>> ConfigEntry<T> addRangeRestriction(F minimum, F maximum) {
        return addRestriction((val) -> 
            minimum.compareTo(val) <= 0 &&
            maximum.compareTo(val) >= 0); 
    }
}
