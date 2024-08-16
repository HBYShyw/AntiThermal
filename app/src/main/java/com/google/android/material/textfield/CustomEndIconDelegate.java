package com.google.android.material.textfield;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CustomEndIconDelegate.java */
/* renamed from: com.google.android.material.textfield.b, reason: use source file name */
/* loaded from: classes.dex */
public class CustomEndIconDelegate extends EndIconDelegate {
    /* JADX INFO: Access modifiers changed from: package-private */
    public CustomEndIconDelegate(TextInputLayout textInputLayout, int i10) {
        super(textInputLayout, i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.EndIconDelegate
    public void a() {
        this.f9442a.setEndIconDrawable(this.f9445d);
        this.f9442a.setEndIconOnClickListener(null);
        this.f9442a.setEndIconOnLongClickListener(null);
    }
}
