package com.google.android.play.core.splitinstall;

import android.content.Context;

/* compiled from: SplitInstallManagerFactory.java */
/* renamed from: com.google.android.play.core.splitinstall.c, reason: use source file name */
/* loaded from: classes.dex */
public class SplitInstallManagerFactory {
    public static SplitInstallManager a(Context context) {
        return new SplitInstallManagerWrapper(context);
    }
}
