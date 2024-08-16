package com.android.server.permission.access.util;

/* compiled from: IntExtensions.kt */
/* renamed from: com.android.server.permission.access.util.IntExtensionsKt, reason: use source file name */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class IntExtensions {
    public static final int andInv(int i, int i2) {
        return i & (~i2);
    }

    public static final boolean hasAnyBit(int i, int i2) {
        return (i & i2) != 0;
    }

    public static final boolean hasBits(int i, int i2) {
        return (i & i2) == i2;
    }
}
