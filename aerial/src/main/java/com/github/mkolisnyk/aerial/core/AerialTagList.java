package com.github.mkolisnyk.aerial.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AerialTagList {

    private List<String> tags;
    private Iterator<String> iterator;

    public AerialTagList() {
        tags = new ArrayList<String>();
    }

    public void add(String tag) {
        tags.add(tag);
        iterator = tags.iterator();
    }

    public boolean hasNext() {
        return iterator != null && iterator.hasNext();
    }

    public String next() {
        if (iterator == null || !iterator.hasNext()) {
            return null;
        }
        return iterator.next();
    }
}
