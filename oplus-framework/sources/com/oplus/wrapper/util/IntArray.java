package com.oplus.wrapper.util;

/* loaded from: classes.dex */
public class IntArray {
    private final android.util.IntArray mIntArray = new android.util.IntArray();

    public void add(int value) {
        this.mIntArray.add(value);
    }

    public int binarySearch(int value) {
        return this.mIntArray.binarySearch(value);
    }
}
