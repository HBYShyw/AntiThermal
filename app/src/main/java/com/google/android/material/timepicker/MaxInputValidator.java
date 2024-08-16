package com.google.android.material.timepicker;

import android.text.InputFilter;
import android.text.Spanned;

/* compiled from: MaxInputValidator.java */
/* renamed from: com.google.android.material.timepicker.a, reason: use source file name */
/* loaded from: classes.dex */
class MaxInputValidator implements InputFilter {

    /* renamed from: a, reason: collision with root package name */
    private int f9530a;

    public MaxInputValidator(int i10) {
        this.f9530a = i10;
    }

    @Override // android.text.InputFilter
    public CharSequence filter(CharSequence charSequence, int i10, int i11, Spanned spanned, int i12, int i13) {
        try {
            StringBuilder sb2 = new StringBuilder(spanned);
            sb2.replace(i12, i13, charSequence.subSequence(i10, i11).toString());
            if (Integer.parseInt(sb2.toString()) <= this.f9530a) {
                return null;
            }
            return "";
        } catch (NumberFormatException unused) {
            return "";
        }
    }
}
