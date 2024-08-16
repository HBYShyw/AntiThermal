package androidx.core.app;

import android.content.res.Configuration;

/* compiled from: PictureInPictureModeChangedInfo.java */
/* renamed from: androidx.core.app.h, reason: use source file name */
/* loaded from: classes.dex */
public final class PictureInPictureModeChangedInfo {

    /* renamed from: a, reason: collision with root package name */
    private final boolean f2119a;

    /* renamed from: b, reason: collision with root package name */
    private final Configuration f2120b;

    public PictureInPictureModeChangedInfo(boolean z10) {
        this.f2119a = z10;
        this.f2120b = null;
    }

    public PictureInPictureModeChangedInfo(boolean z10, Configuration configuration) {
        this.f2119a = z10;
        this.f2120b = configuration;
    }
}
