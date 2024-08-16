package com.android.server.permission.jarjar.kotlin.ranges;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: _Ranges.kt */
/* renamed from: com.android.server.permission.jarjar.kotlin.ranges.RangesKt___RangesKt, reason: use source file name */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class _Ranges extends Ranges {
    public static int coerceAtMost(int i, int i2) {
        return i > i2 ? i2 : i;
    }

    public static long coerceAtMost(long j, long j2) {
        return j > j2 ? j2 : j;
    }
}
