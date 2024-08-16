package com.android.server.autofill;

import android.service.autofill.Dataset;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ISessionWrapper {
    default void autofill(int i, int i2, Dataset dataset, boolean z, int i3) {
    }

    default ISessionExt getSessionExt() {
        return null;
    }

    default void save() {
    }
}
