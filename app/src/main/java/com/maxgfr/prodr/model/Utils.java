package com.maxgfr.prodr.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Utils {

    public static Set<String> videoToSet(Object o) {
        System.out.println(o);
        ArrayList<String> list_url = new ArrayList<>();
        Set<String> set = new HashSet<>(list_url);
        return set;
    }
}
