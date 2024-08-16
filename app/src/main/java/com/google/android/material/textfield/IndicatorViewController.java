package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import com.google.android.material.R$dimen;
import com.google.android.material.R$id;
import java.util.ArrayList;
import java.util.List;
import p3.AnimationUtils;
import p3.AnimatorSetCompat;
import z3.MaterialResources;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: IndicatorViewController.java */
/* renamed from: com.google.android.material.textfield.g, reason: use source file name */
/* loaded from: classes.dex */
public final class IndicatorViewController {

    /* renamed from: a, reason: collision with root package name */
    private final Context f9446a;

    /* renamed from: b, reason: collision with root package name */
    private final TextInputLayout f9447b;

    /* renamed from: c, reason: collision with root package name */
    private LinearLayout f9448c;

    /* renamed from: d, reason: collision with root package name */
    private int f9449d;

    /* renamed from: e, reason: collision with root package name */
    private FrameLayout f9450e;

    /* renamed from: f, reason: collision with root package name */
    private Animator f9451f;

    /* renamed from: g, reason: collision with root package name */
    private final float f9452g;

    /* renamed from: h, reason: collision with root package name */
    private int f9453h;

    /* renamed from: i, reason: collision with root package name */
    private int f9454i;

    /* renamed from: j, reason: collision with root package name */
    private CharSequence f9455j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f9456k;

    /* renamed from: l, reason: collision with root package name */
    private TextView f9457l;

    /* renamed from: m, reason: collision with root package name */
    private CharSequence f9458m;

    /* renamed from: n, reason: collision with root package name */
    private int f9459n;

    /* renamed from: o, reason: collision with root package name */
    private ColorStateList f9460o;

    /* renamed from: p, reason: collision with root package name */
    private CharSequence f9461p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f9462q;

    /* renamed from: r, reason: collision with root package name */
    private TextView f9463r;

    /* renamed from: s, reason: collision with root package name */
    private int f9464s;

    /* renamed from: t, reason: collision with root package name */
    private ColorStateList f9465t;

    /* renamed from: u, reason: collision with root package name */
    private Typeface f9466u;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: IndicatorViewController.java */
    /* renamed from: com.google.android.material.textfield.g$a */
    /* loaded from: classes.dex */
    public class a extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f9467a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ TextView f9468b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f9469c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ TextView f9470d;

        a(int i10, TextView textView, int i11, TextView textView2) {
            this.f9467a = i10;
            this.f9468b = textView;
            this.f9469c = i11;
            this.f9470d = textView2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            IndicatorViewController.this.f9453h = this.f9467a;
            IndicatorViewController.this.f9451f = null;
            TextView textView = this.f9468b;
            if (textView != null) {
                textView.setVisibility(4);
                if (this.f9469c == 1 && IndicatorViewController.this.f9457l != null) {
                    IndicatorViewController.this.f9457l.setText((CharSequence) null);
                }
            }
            TextView textView2 = this.f9470d;
            if (textView2 != null) {
                textView2.setTranslationY(0.0f);
                this.f9470d.setAlpha(1.0f);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            TextView textView = this.f9470d;
            if (textView != null) {
                textView.setVisibility(0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: IndicatorViewController.java */
    /* renamed from: com.google.android.material.textfield.g$b */
    /* loaded from: classes.dex */
    public class b extends View.AccessibilityDelegate {
        b() {
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            EditText editText = IndicatorViewController.this.f9447b.getEditText();
            if (editText != null) {
                accessibilityNodeInfo.setLabeledBy(editText);
            }
        }
    }

    public IndicatorViewController(TextInputLayout textInputLayout) {
        this.f9446a = textInputLayout.getContext();
        this.f9447b = textInputLayout;
        this.f9452g = r0.getResources().getDimensionPixelSize(R$dimen.design_textinput_caption_translate_y);
    }

    private void C(int i10, int i11) {
        TextView m10;
        TextView m11;
        if (i10 == i11) {
            return;
        }
        if (i11 != 0 && (m11 = m(i11)) != null) {
            m11.setVisibility(0);
            m11.setAlpha(1.0f);
        }
        if (i10 != 0 && (m10 = m(i10)) != null) {
            m10.setVisibility(4);
            if (i10 == 1) {
                m10.setText((CharSequence) null);
            }
        }
        this.f9453h = i11;
    }

    private void K(TextView textView, Typeface typeface) {
        if (textView != null) {
            textView.setTypeface(typeface);
        }
    }

    private void M(ViewGroup viewGroup, int i10) {
        if (i10 == 0) {
            viewGroup.setVisibility(8);
        }
    }

    private boolean N(TextView textView, CharSequence charSequence) {
        return ViewCompat.Q(this.f9447b) && this.f9447b.isEnabled() && !(this.f9454i == this.f9453h && textView != null && TextUtils.equals(textView.getText(), charSequence));
    }

    private void Q(int i10, int i11, boolean z10) {
        if (i10 == i11) {
            return;
        }
        if (z10) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.f9451f = animatorSet;
            ArrayList arrayList = new ArrayList();
            i(arrayList, this.f9462q, this.f9463r, 2, i10, i11);
            i(arrayList, this.f9456k, this.f9457l, 1, i10, i11);
            AnimatorSetCompat.a(animatorSet, arrayList);
            animatorSet.addListener(new a(i11, m(i10), i10, m(i11)));
            animatorSet.start();
        } else {
            C(i10, i11);
        }
        this.f9447b.r0();
        this.f9447b.w0(z10);
        this.f9447b.E0();
    }

    private boolean g() {
        return (this.f9448c == null || this.f9447b.getEditText() == null) ? false : true;
    }

    private void i(List<Animator> list, boolean z10, TextView textView, int i10, int i11, int i12) {
        if (textView == null || !z10) {
            return;
        }
        if (i10 == i12 || i10 == i11) {
            list.add(j(textView, i12 == i10));
            if (i12 == i10) {
                list.add(k(textView));
            }
        }
    }

    private ObjectAnimator j(TextView textView, boolean z10) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(textView, (Property<TextView, Float>) View.ALPHA, z10 ? 1.0f : 0.0f);
        ofFloat.setDuration(167L);
        ofFloat.setInterpolator(AnimationUtils.f16555a);
        return ofFloat;
    }

    private ObjectAnimator k(TextView textView) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(textView, (Property<TextView, Float>) View.TRANSLATION_Y, -this.f9452g, 0.0f);
        ofFloat.setDuration(217L);
        ofFloat.setInterpolator(AnimationUtils.f16558d);
        return ofFloat;
    }

    private TextView m(int i10) {
        if (i10 == 1) {
            return this.f9457l;
        }
        if (i10 != 2) {
            return null;
        }
        return this.f9463r;
    }

    private int u(boolean z10, int i10, int i11) {
        return z10 ? this.f9446a.getResources().getDimensionPixelSize(i10) : i11;
    }

    private boolean x(int i10) {
        return (i10 != 1 || this.f9457l == null || TextUtils.isEmpty(this.f9455j)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean A() {
        return this.f9462q;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void B(TextView textView, int i10) {
        FrameLayout frameLayout;
        if (this.f9448c == null) {
            return;
        }
        if (y(i10) && (frameLayout = this.f9450e) != null) {
            frameLayout.removeView(textView);
        } else {
            this.f9448c.removeView(textView);
        }
        int i11 = this.f9449d - 1;
        this.f9449d = i11;
        M(this.f9448c, i11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void D(CharSequence charSequence) {
        this.f9458m = charSequence;
        TextView textView = this.f9457l;
        if (textView != null) {
            textView.setContentDescription(charSequence);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void E(boolean z10) {
        if (this.f9456k == z10) {
            return;
        }
        h();
        if (z10) {
            AppCompatTextView appCompatTextView = new AppCompatTextView(this.f9446a);
            this.f9457l = appCompatTextView;
            appCompatTextView.setId(R$id.textinput_error);
            this.f9457l.setTextAlignment(5);
            Typeface typeface = this.f9466u;
            if (typeface != null) {
                this.f9457l.setTypeface(typeface);
            }
            F(this.f9459n);
            G(this.f9460o);
            D(this.f9458m);
            this.f9457l.setVisibility(4);
            ViewCompat.n0(this.f9457l, 1);
            e(this.f9457l, 0);
        } else {
            v();
            B(this.f9457l, 0);
            this.f9457l = null;
            this.f9447b.r0();
            this.f9447b.E0();
        }
        this.f9456k = z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void F(int i10) {
        this.f9459n = i10;
        TextView textView = this.f9457l;
        if (textView != null) {
            this.f9447b.d0(textView, i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void G(ColorStateList colorStateList) {
        this.f9460o = colorStateList;
        TextView textView = this.f9457l;
        if (textView == null || colorStateList == null) {
            return;
        }
        textView.setTextColor(colorStateList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H(int i10) {
        this.f9464s = i10;
        TextView textView = this.f9463r;
        if (textView != null) {
            TextViewCompat.n(textView, i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void I(boolean z10) {
        if (this.f9462q == z10) {
            return;
        }
        h();
        if (z10) {
            AppCompatTextView appCompatTextView = new AppCompatTextView(this.f9446a);
            this.f9463r = appCompatTextView;
            appCompatTextView.setId(R$id.textinput_helper_text);
            this.f9463r.setTextAlignment(5);
            Typeface typeface = this.f9466u;
            if (typeface != null) {
                this.f9463r.setTypeface(typeface);
            }
            this.f9463r.setVisibility(4);
            ViewCompat.n0(this.f9463r, 1);
            H(this.f9464s);
            J(this.f9465t);
            e(this.f9463r, 1);
            this.f9463r.setAccessibilityDelegate(new b());
        } else {
            w();
            B(this.f9463r, 1);
            this.f9463r = null;
            this.f9447b.r0();
            this.f9447b.E0();
        }
        this.f9462q = z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J(ColorStateList colorStateList) {
        this.f9465t = colorStateList;
        TextView textView = this.f9463r;
        if (textView == null || colorStateList == null) {
            return;
        }
        textView.setTextColor(colorStateList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L(Typeface typeface) {
        if (typeface != this.f9466u) {
            this.f9466u = typeface;
            K(this.f9457l, typeface);
            K(this.f9463r, typeface);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void O(CharSequence charSequence) {
        h();
        this.f9455j = charSequence;
        this.f9457l.setText(charSequence);
        int i10 = this.f9453h;
        if (i10 != 1) {
            this.f9454i = 1;
        }
        Q(i10, this.f9454i, N(this.f9457l, charSequence));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void P(CharSequence charSequence) {
        h();
        this.f9461p = charSequence;
        this.f9463r.setText(charSequence);
        int i10 = this.f9453h;
        if (i10 != 2) {
            this.f9454i = 2;
        }
        Q(i10, this.f9454i, N(this.f9463r, charSequence));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(TextView textView, int i10) {
        if (this.f9448c == null && this.f9450e == null) {
            LinearLayout linearLayout = new LinearLayout(this.f9446a);
            this.f9448c = linearLayout;
            linearLayout.setOrientation(0);
            this.f9447b.addView(this.f9448c, -1, -2);
            this.f9450e = new FrameLayout(this.f9446a);
            this.f9448c.addView(this.f9450e, new LinearLayout.LayoutParams(0, -2, 1.0f));
            if (this.f9447b.getEditText() != null) {
                f();
            }
        }
        if (y(i10)) {
            this.f9450e.setVisibility(0);
            this.f9450e.addView(textView);
        } else {
            this.f9448c.addView(textView, new LinearLayout.LayoutParams(-2, -2));
        }
        this.f9448c.setVisibility(0);
        this.f9449d++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        if (g()) {
            EditText editText = this.f9447b.getEditText();
            boolean i10 = MaterialResources.i(this.f9446a);
            LinearLayout linearLayout = this.f9448c;
            int i11 = R$dimen.material_helper_text_font_1_3_padding_horizontal;
            ViewCompat.A0(linearLayout, u(i10, i11, ViewCompat.C(editText)), u(i10, R$dimen.material_helper_text_font_1_3_padding_top, this.f9446a.getResources().getDimensionPixelSize(R$dimen.material_helper_text_default_padding_top)), u(i10, i11, ViewCompat.B(editText)), 0);
        }
    }

    void h() {
        Animator animator = this.f9451f;
        if (animator != null) {
            animator.cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean l() {
        return x(this.f9454i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence n() {
        return this.f9458m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence o() {
        return this.f9455j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int p() {
        TextView textView = this.f9457l;
        if (textView != null) {
            return textView.getCurrentTextColor();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList q() {
        TextView textView = this.f9457l;
        if (textView != null) {
            return textView.getTextColors();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence r() {
        return this.f9461p;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View s() {
        return this.f9463r;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int t() {
        TextView textView = this.f9463r;
        if (textView != null) {
            return textView.getCurrentTextColor();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v() {
        this.f9455j = null;
        h();
        if (this.f9453h == 1) {
            if (this.f9462q && !TextUtils.isEmpty(this.f9461p)) {
                this.f9454i = 2;
            } else {
                this.f9454i = 0;
            }
        }
        Q(this.f9453h, this.f9454i, N(this.f9457l, ""));
    }

    void w() {
        h();
        int i10 = this.f9453h;
        if (i10 == 2) {
            this.f9454i = 0;
        }
        Q(i10, this.f9454i, N(this.f9463r, ""));
    }

    boolean y(int i10) {
        return i10 == 0 || i10 == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean z() {
        return this.f9456k;
    }
}
