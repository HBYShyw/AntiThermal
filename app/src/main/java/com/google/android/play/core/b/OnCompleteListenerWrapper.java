package com.google.android.play.core.b;

import com.oplus.oms.split.core.tasks.OplusOnCompleteListener;
import com.oplus.oms.split.core.tasks.OplusTask;
import g4.OnCompleteListener;

/* compiled from: OnCompleteListenerWrapper.java */
/* renamed from: com.google.android.play.core.b.b, reason: use source file name */
/* loaded from: classes.dex */
public class OnCompleteListenerWrapper<T> implements OplusOnCompleteListener<T> {

    /* renamed from: a, reason: collision with root package name */
    public final g4.d<T> f9563a;

    /* renamed from: b, reason: collision with root package name */
    public final OnCompleteListener<T> f9564b;

    public OnCompleteListenerWrapper(g4.d<T> dVar, OnCompleteListener<T> onCompleteListener) {
        this.f9563a = dVar;
        this.f9564b = onCompleteListener;
    }

    public void onComplete(OplusTask<T> oplusTask) {
        this.f9564b.a(this.f9563a);
    }
}
