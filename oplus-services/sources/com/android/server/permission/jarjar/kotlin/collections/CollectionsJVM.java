package com.android.server.permission.jarjar.kotlin.collections;

import com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: CollectionsJVM.kt */
/* renamed from: com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__CollectionsJVMKt, reason: use source file name */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class CollectionsJVM {
    @NotNull
    public static final <T> List<T> listOf(T t) {
        List<T> singletonList = Collections.singletonList(t);
        Intrinsics.checkNotNullExpressionValue(singletonList, "singletonList(element)");
        return singletonList;
    }
}
