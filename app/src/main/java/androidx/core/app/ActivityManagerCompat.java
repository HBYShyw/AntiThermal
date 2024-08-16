package androidx.core.app;

import android.app.ActivityManager;

/* compiled from: ActivityManagerCompat.java */
/* renamed from: androidx.core.app.b, reason: use source file name */
/* loaded from: classes.dex */
public final class ActivityManagerCompat {
    public static boolean a(ActivityManager activityManager) {
        return activityManager.isLowRamDevice();
    }
}
