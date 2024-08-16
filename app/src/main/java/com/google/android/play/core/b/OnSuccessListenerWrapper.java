package com.google.android.play.core.b;

import com.oplus.oms.split.core.tasks.OplusOnSuccessListener;

/* compiled from: OnSuccessListenerWrapper.java */
/* renamed from: com.google.android.play.core.b.d, reason: use source file name */
/* loaded from: classes.dex */
public class OnSuccessListenerWrapper<T> implements OplusOnSuccessListener<T> {

    /* renamed from: a, reason: collision with root package name */
    public final g4.c<T> f9566a;

    public OnSuccessListenerWrapper(g4.c<T> cVar) {
        this.f9566a = cVar;
    }

    public void onSuccess(T t7) {
        this.f9566a.onSuccess(t7);
    }
}
