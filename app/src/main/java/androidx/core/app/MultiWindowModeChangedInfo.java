package androidx.core.app;

import android.content.res.Configuration;

/* compiled from: MultiWindowModeChangedInfo.java */
/* renamed from: androidx.core.app.e, reason: use source file name */
/* loaded from: classes.dex */
public final class MultiWindowModeChangedInfo {

    /* renamed from: a, reason: collision with root package name */
    private final boolean f2112a;

    /* renamed from: b, reason: collision with root package name */
    private final Configuration f2113b;

    public MultiWindowModeChangedInfo(boolean z10) {
        this.f2112a = z10;
        this.f2113b = null;
    }

    public MultiWindowModeChangedInfo(boolean z10, Configuration configuration) {
        this.f2112a = z10;
        this.f2113b = configuration;
    }
}
