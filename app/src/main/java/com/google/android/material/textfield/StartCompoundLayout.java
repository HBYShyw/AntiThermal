package com.google.android.material.textfield;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import com.google.android.material.R$dimen;
import com.google.android.material.R$id;
import com.google.android.material.R$layout;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.internal.ViewUtils;
import z3.MaterialResources;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: StartCompoundLayout.java */
@SuppressLint({"ViewConstructor"})
/* renamed from: com.google.android.material.textfield.j, reason: use source file name */
/* loaded from: classes.dex */
public class StartCompoundLayout extends LinearLayout {

    /* renamed from: e, reason: collision with root package name */
    private final TextInputLayout f9482e;

    /* renamed from: f, reason: collision with root package name */
    private final TextView f9483f;

    /* renamed from: g, reason: collision with root package name */
    private CharSequence f9484g;

    /* renamed from: h, reason: collision with root package name */
    private final CheckableImageButton f9485h;

    /* renamed from: i, reason: collision with root package name */
    private ColorStateList f9486i;

    /* renamed from: j, reason: collision with root package name */
    private PorterDuff.Mode f9487j;

    /* renamed from: k, reason: collision with root package name */
    private View.OnLongClickListener f9488k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f9489l;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StartCompoundLayout(TextInputLayout textInputLayout, TintTypedArray tintTypedArray) {
        super(textInputLayout.getContext());
        this.f9482e = textInputLayout;
        setVisibility(8);
        setOrientation(0);
        setLayoutParams(new FrameLayout.LayoutParams(-2, -1, 8388611));
        CheckableImageButton checkableImageButton = (CheckableImageButton) LayoutInflater.from(getContext()).inflate(R$layout.design_text_input_start_icon, (ViewGroup) this, false);
        this.f9485h = checkableImageButton;
        AppCompatTextView appCompatTextView = new AppCompatTextView(getContext());
        this.f9483f = appCompatTextView;
        g(tintTypedArray);
        f(tintTypedArray);
        addView(checkableImageButton);
        addView(appCompatTextView);
    }

    private void f(TintTypedArray tintTypedArray) {
        this.f9483f.setVisibility(8);
        this.f9483f.setId(R$id.textinput_prefix_text);
        this.f9483f.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        ViewCompat.n0(this.f9483f, 1);
        l(tintTypedArray.n(R$styleable.TextInputLayout_prefixTextAppearance, 0));
        int i10 = R$styleable.TextInputLayout_prefixTextColor;
        if (tintTypedArray.s(i10)) {
            m(tintTypedArray.c(i10));
        }
        k(tintTypedArray.p(R$styleable.TextInputLayout_prefixText));
    }

    private void g(TintTypedArray tintTypedArray) {
        if (MaterialResources.i(getContext())) {
            MarginLayoutParamsCompat.c((ViewGroup.MarginLayoutParams) this.f9485h.getLayoutParams(), 0);
        }
        q(null);
        r(null);
        int i10 = R$styleable.TextInputLayout_startIconTint;
        if (tintTypedArray.s(i10)) {
            this.f9486i = MaterialResources.b(getContext(), tintTypedArray, i10);
        }
        int i11 = R$styleable.TextInputLayout_startIconTintMode;
        if (tintTypedArray.s(i11)) {
            this.f9487j = ViewUtils.parseTintMode(tintTypedArray.k(i11, -1), null);
        }
        int i12 = R$styleable.TextInputLayout_startIconDrawable;
        if (tintTypedArray.s(i12)) {
            p(tintTypedArray.g(i12));
            int i13 = R$styleable.TextInputLayout_startIconContentDescription;
            if (tintTypedArray.s(i13)) {
                o(tintTypedArray.p(i13));
            }
            n(tintTypedArray.a(R$styleable.TextInputLayout_startIconCheckable, true));
        }
    }

    private void x() {
        int i10 = (this.f9484g == null || this.f9489l) ? 8 : 0;
        setVisibility(this.f9485h.getVisibility() == 0 || i10 == 0 ? 0 : 8);
        this.f9483f.setVisibility(i10);
        this.f9482e.q0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence a() {
        return this.f9484g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList b() {
        return this.f9483f.getTextColors();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TextView c() {
        return this.f9483f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence d() {
        return this.f9485h.getContentDescription();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Drawable e() {
        return this.f9485h.getDrawable();
    }

    boolean h() {
        return this.f9485h.getVisibility() == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(boolean z10) {
        this.f9489l = z10;
        x();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        IconHelper.c(this.f9482e, this.f9485h, this.f9486i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(CharSequence charSequence) {
        this.f9484g = TextUtils.isEmpty(charSequence) ? null : charSequence;
        this.f9483f.setText(charSequence);
        x();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(int i10) {
        TextViewCompat.n(this.f9483f, i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(ColorStateList colorStateList) {
        this.f9483f.setTextColor(colorStateList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n(boolean z10) {
        this.f9485h.setCheckable(z10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(CharSequence charSequence) {
        if (d() != charSequence) {
            this.f9485h.setContentDescription(charSequence);
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        w();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p(Drawable drawable) {
        this.f9485h.setImageDrawable(drawable);
        if (drawable != null) {
            IconHelper.a(this.f9482e, this.f9485h, this.f9486i, this.f9487j);
            u(true);
            j();
        } else {
            u(false);
            q(null);
            r(null);
            o(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(View.OnClickListener onClickListener) {
        IconHelper.e(this.f9485h, onClickListener, this.f9488k);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(View.OnLongClickListener onLongClickListener) {
        this.f9488k = onLongClickListener;
        IconHelper.f(this.f9485h, onLongClickListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(ColorStateList colorStateList) {
        if (this.f9486i != colorStateList) {
            this.f9486i = colorStateList;
            IconHelper.a(this.f9482e, this.f9485h, colorStateList, this.f9487j);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(PorterDuff.Mode mode) {
        if (this.f9487j != mode) {
            this.f9487j = mode;
            IconHelper.a(this.f9482e, this.f9485h, this.f9486i, mode);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(boolean z10) {
        if (h() != z10) {
            this.f9485h.setVisibility(z10 ? 0 : 8);
            w();
            x();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        if (this.f9483f.getVisibility() == 0) {
            accessibilityNodeInfoCompat.h0(this.f9483f);
            accessibilityNodeInfoCompat.z0(this.f9483f);
        } else {
            accessibilityNodeInfoCompat.z0(this.f9485h);
        }
    }

    void w() {
        EditText editText = this.f9482e.f9346i;
        if (editText == null) {
            return;
        }
        ViewCompat.A0(this.f9483f, h() ? 0 : ViewCompat.C(editText), editText.getCompoundPaddingTop(), getContext().getResources().getDimensionPixelSize(R$dimen.material_input_text_to_prefix_suffix_padding), editText.getCompoundPaddingBottom());
    }
}
