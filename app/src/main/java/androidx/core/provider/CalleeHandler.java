package androidx.core.provider;

import android.os.Handler;
import android.os.Looper;

/* compiled from: CalleeHandler.java */
/* renamed from: androidx.core.provider.b, reason: use source file name */
/* loaded from: classes.dex */
class CalleeHandler {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static Handler a() {
        if (Looper.myLooper() == null) {
            return new Handler(Looper.getMainLooper());
        }
        return new Handler();
    }
}
