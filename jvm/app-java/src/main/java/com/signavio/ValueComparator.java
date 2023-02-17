package com.signavio;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class ValueComparator implements Comparator<String> {
    Map<String, AtomicInteger> base;

    public ValueComparator(Map<String, AtomicInteger> map) {
        this.base = map;
    }

    public int compare(String a, String b) {
        if (base.get(a).get() >= base.get(b).get()) {
            return -1;
        } else {
            return 1;
        }
    }
}
