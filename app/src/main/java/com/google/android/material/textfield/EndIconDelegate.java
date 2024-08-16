package com.google.android.material.textfield;

import android.content.Context;
import com.google.android.material.internal.CheckableImageButton;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: EndIconDelegate.java */
/* renamed from: com.google.android.material.textfield.e, reason: use source file name */
/* loaded from: classes.dex */
public abstract class EndIconDelegate {

    /* renamed from: a, reason: collision with root package name */
    TextInputLayout f9442a;

    /* renamed from: b, reason: collision with root package name */
    Context f9443b;

    /* renamed from: c, reason: collision with root package name */
    CheckableImageButton f9444c;

    /* renamed from: d, reason: collision with root package name */
    final int f9445d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EndIconDelegate(TextInputLayout textInputLayout, int i10) {
        this.f9442a = textInputLayout;
        this.f9443b = textInputLayout.getContext();
        this.f9444c = textInputLayout.getEndIconView();
        this.f9445d = i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a();

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(int i10) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(boolean z10) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        return false;
    }
}
