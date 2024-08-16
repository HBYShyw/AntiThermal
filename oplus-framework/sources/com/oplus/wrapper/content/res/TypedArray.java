package com.oplus.wrapper.content.res;

/* loaded from: classes.dex */
public class TypedArray {
    private final android.content.res.TypedArray mTypedArray;

    public TypedArray(android.content.res.TypedArray typedArray) {
        this.mTypedArray = typedArray;
    }

    public int getThemeAttributeId(int index, int defValue) {
        return this.mTypedArray.getThemeAttributeId(index, defValue);
    }
}
