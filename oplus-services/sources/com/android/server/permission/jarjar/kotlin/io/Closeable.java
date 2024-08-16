package com.android.server.permission.jarjar.kotlin.io;

import com.android.server.permission.jarjar.kotlin.ExceptionsKt__ExceptionsKt;
import org.jetbrains.annotations.Nullable;

/* compiled from: Closeable.kt */
/* renamed from: com.android.server.permission.jarjar.kotlin.io.CloseableKt, reason: use source file name */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class Closeable {
    public static final void closeFinally(@Nullable java.io.Closeable closeable, @Nullable Throwable th) {
        if (closeable != null) {
            if (th == null) {
                closeable.close();
                return;
            }
            try {
                closeable.close();
            } catch (Throwable th2) {
                ExceptionsKt__ExceptionsKt.addSuppressed(th, th2);
            }
        }
    }
}
