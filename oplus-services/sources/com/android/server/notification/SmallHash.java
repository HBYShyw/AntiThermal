package com.android.server.notification;

import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SmallHash {
    public static final int MAX_HASH = 8192;

    public static int hash(String str) {
        return hash(Objects.hashCode(str));
    }

    public static int hash(int i) {
        return Math.floorMod(i, 8192);
    }
}
