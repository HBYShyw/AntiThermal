package com.google.android.play.core.splitinstall;

/* compiled from: SplitInstallException.java */
/* renamed from: com.google.android.play.core.splitinstall.a, reason: use source file name */
/* loaded from: classes.dex */
public class SplitInstallException extends RuntimeException {

    /* renamed from: e, reason: collision with root package name */
    public final int f9568e;

    public SplitInstallException(int i10) {
        super("Split Install Error: " + i10);
        this.f9568e = i10;
    }
}
