package com.android.server.permission.jarjar.kotlin.jdk7;

import com.android.server.permission.jarjar.kotlin.ExceptionsKt__ExceptionsKt;
import org.jetbrains.annotations.Nullable;

/* compiled from: AutoCloseable.kt */
/* renamed from: com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt, reason: use source file name */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class AutoCloseable {
    public static final void closeFinally(@Nullable java.lang.AutoCloseable autoCloseable, @Nullable Throwable th) {
        if (autoCloseable != null) {
            if (th == null) {
                autoCloseable.close();
                return;
            }
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                ExceptionsKt__ExceptionsKt.addSuppressed(th, th2);
            }
        }
    }
}
