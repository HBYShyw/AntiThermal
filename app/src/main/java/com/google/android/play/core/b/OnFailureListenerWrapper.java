package com.google.android.play.core.b;

import com.oplus.oms.split.core.tasks.OplusOnFailureListener;
import f4.ExceptionConvertor;
import g4.OnFailureListener;

/* compiled from: OnFailureListenerWrapper.java */
/* renamed from: com.google.android.play.core.b.c, reason: use source file name */
/* loaded from: classes.dex */
public class OnFailureListenerWrapper implements OplusOnFailureListener {

    /* renamed from: a, reason: collision with root package name */
    public final OnFailureListener f9565a;

    public OnFailureListenerWrapper(OnFailureListener onFailureListener) {
        this.f9565a = onFailureListener;
    }

    public void onFailure(Exception exc) {
        this.f9565a.onFailure(ExceptionConvertor.a(exc));
    }
}
