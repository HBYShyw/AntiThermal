package com.android.server.os;

import java.io.File;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface INativeTombstoneManagerExt {
    default boolean isOverLimitSize(File file) {
        return false;
    }
}
