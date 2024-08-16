package com.oplus.wrapper.os;

import android.util.ArraySet;

/* loaded from: classes.dex */
public class Parcel {
    private final android.os.Parcel mParcel;

    public Parcel(android.os.Parcel parcel) {
        this.mParcel = parcel;
    }

    public final String[] readStringArray() {
        return this.mParcel.readStringArray();
    }

    public ArraySet<?> readArraySet(ClassLoader loader) {
        return this.mParcel.readArraySet(loader);
    }

    public void writeArraySet(ArraySet<?> val) {
        this.mParcel.writeArraySet(val);
    }

    public String readStringNoHelper() {
        return this.mParcel.readStringNoHelper();
    }

    public void writeStringNoHelper(String val) {
        this.mParcel.writeStringNoHelper(val);
    }

    public String readString16NoHelper() {
        return this.mParcel.readString16NoHelper();
    }
}
