package com.android.server.am;

import java.nio.ByteBuffer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IProcessListWrapper {
    public static final int PRELOAD_APP_ADJ = 850;

    default boolean writeLmkd(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        return false;
    }
}
