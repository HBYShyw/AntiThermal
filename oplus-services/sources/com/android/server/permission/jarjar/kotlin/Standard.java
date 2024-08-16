package com.android.server.permission.jarjar.kotlin;

import com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: Standard.kt */
/* renamed from: com.android.server.permission.jarjar.kotlin.NotImplementedError, reason: use source file name */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class Standard extends Error {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Standard(@NotNull String str) {
        super(str);
        Intrinsics.checkNotNullParameter(str, "message");
    }

    public /* synthetic */ Standard(String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "An operation is not implemented." : str);
    }
}
