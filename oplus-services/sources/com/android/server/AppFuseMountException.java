package com.android.server;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AppFuseMountException extends Exception {
    public AppFuseMountException(String str) {
        super(str);
    }

    public AppFuseMountException(String str, Throwable th) {
        super(str, th);
    }

    public IllegalArgumentException rethrowAsParcelableException() {
        throw new IllegalStateException(getMessage(), this);
    }
}
