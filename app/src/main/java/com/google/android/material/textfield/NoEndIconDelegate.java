package com.google.android.material.textfield;

import android.graphics.drawable.Drawable;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NoEndIconDelegate.java */
/* renamed from: com.google.android.material.textfield.h, reason: use source file name */
/* loaded from: classes.dex */
public class NoEndIconDelegate extends EndIconDelegate {
    /* JADX INFO: Access modifiers changed from: package-private */
    public NoEndIconDelegate(TextInputLayout textInputLayout) {
        super(textInputLayout, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.EndIconDelegate
    public void a() {
        this.f9442a.setEndIconOnClickListener(null);
        this.f9442a.setEndIconDrawable((Drawable) null);
        this.f9442a.setEndIconContentDescription((CharSequence) null);
    }
}
