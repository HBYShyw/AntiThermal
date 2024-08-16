package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import com.google.android.material.R$drawable;
import com.google.android.material.R$string;
import com.google.android.material.textfield.TextInputLayout;
import p3.AnimationUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ClearTextEndIconDelegate.java */
/* renamed from: com.google.android.material.textfield.a, reason: use source file name */
/* loaded from: classes.dex */
public class ClearTextEndIconDelegate extends EndIconDelegate {

    /* renamed from: e, reason: collision with root package name */
    private final TextWatcher f9392e;

    /* renamed from: f, reason: collision with root package name */
    private final View.OnFocusChangeListener f9393f;

    /* renamed from: g, reason: collision with root package name */
    private final TextInputLayout.f f9394g;

    /* renamed from: h, reason: collision with root package name */
    private final TextInputLayout.g f9395h;

    /* renamed from: i, reason: collision with root package name */
    private AnimatorSet f9396i;

    /* renamed from: j, reason: collision with root package name */
    private ValueAnimator f9397j;

    /* compiled from: ClearTextEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.a$a */
    /* loaded from: classes.dex */
    class a implements TextWatcher {
        a() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (ClearTextEndIconDelegate.this.f9442a.getSuffixText() != null) {
                return;
            }
            ClearTextEndIconDelegate clearTextEndIconDelegate = ClearTextEndIconDelegate.this;
            clearTextEndIconDelegate.i(clearTextEndIconDelegate.m());
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }
    }

    /* compiled from: ClearTextEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.a$b */
    /* loaded from: classes.dex */
    class b implements View.OnFocusChangeListener {
        b() {
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z10) {
            ClearTextEndIconDelegate clearTextEndIconDelegate = ClearTextEndIconDelegate.this;
            clearTextEndIconDelegate.i(clearTextEndIconDelegate.m());
        }
    }

    /* compiled from: ClearTextEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.a$c */
    /* loaded from: classes.dex */
    class c implements TextInputLayout.f {
        c() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.f
        public void a(TextInputLayout textInputLayout) {
            EditText editText = textInputLayout.getEditText();
            textInputLayout.setEndIconVisible(ClearTextEndIconDelegate.this.m());
            editText.setOnFocusChangeListener(ClearTextEndIconDelegate.this.f9393f);
            ClearTextEndIconDelegate clearTextEndIconDelegate = ClearTextEndIconDelegate.this;
            clearTextEndIconDelegate.f9444c.setOnFocusChangeListener(clearTextEndIconDelegate.f9393f);
            editText.removeTextChangedListener(ClearTextEndIconDelegate.this.f9392e);
            editText.addTextChangedListener(ClearTextEndIconDelegate.this.f9392e);
        }
    }

    /* compiled from: ClearTextEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.a$d */
    /* loaded from: classes.dex */
    class d implements TextInputLayout.g {

        /* compiled from: ClearTextEndIconDelegate.java */
        /* renamed from: com.google.android.material.textfield.a$d$a */
        /* loaded from: classes.dex */
        class a implements Runnable {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ EditText f9402e;

            a(EditText editText) {
                this.f9402e = editText;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.f9402e.removeTextChangedListener(ClearTextEndIconDelegate.this.f9392e);
                ClearTextEndIconDelegate.this.i(true);
            }
        }

        d() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.g
        public void a(TextInputLayout textInputLayout, int i10) {
            EditText editText = textInputLayout.getEditText();
            if (editText == null || i10 != 2) {
                return;
            }
            editText.post(new a(editText));
            if (editText.getOnFocusChangeListener() == ClearTextEndIconDelegate.this.f9393f) {
                editText.setOnFocusChangeListener(null);
            }
            if (ClearTextEndIconDelegate.this.f9444c.getOnFocusChangeListener() == ClearTextEndIconDelegate.this.f9393f) {
                ClearTextEndIconDelegate.this.f9444c.setOnFocusChangeListener(null);
            }
        }
    }

    /* compiled from: ClearTextEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.a$e */
    /* loaded from: classes.dex */
    class e implements View.OnClickListener {
        e() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Editable text = ClearTextEndIconDelegate.this.f9442a.getEditText().getText();
            if (text != null) {
                text.clear();
            }
            ClearTextEndIconDelegate.this.f9442a.U();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClearTextEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.a$f */
    /* loaded from: classes.dex */
    public class f extends AnimatorListenerAdapter {
        f() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            ClearTextEndIconDelegate.this.f9442a.setEndIconVisible(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClearTextEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.a$g */
    /* loaded from: classes.dex */
    public class g extends AnimatorListenerAdapter {
        g() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ClearTextEndIconDelegate.this.f9442a.setEndIconVisible(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClearTextEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.a$h */
    /* loaded from: classes.dex */
    public class h implements ValueAnimator.AnimatorUpdateListener {
        h() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ClearTextEndIconDelegate.this.f9444c.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClearTextEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.a$i */
    /* loaded from: classes.dex */
    public class i implements ValueAnimator.AnimatorUpdateListener {
        i() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            ClearTextEndIconDelegate.this.f9444c.setScaleX(floatValue);
            ClearTextEndIconDelegate.this.f9444c.setScaleY(floatValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClearTextEndIconDelegate(TextInputLayout textInputLayout, int i10) {
        super(textInputLayout, i10);
        this.f9392e = new a();
        this.f9393f = new b();
        this.f9394g = new c();
        this.f9395h = new d();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i(boolean z10) {
        boolean z11 = this.f9442a.K() == z10;
        if (z10 && !this.f9396i.isRunning()) {
            this.f9397j.cancel();
            this.f9396i.start();
            if (z11) {
                this.f9396i.end();
                return;
            }
            return;
        }
        if (z10) {
            return;
        }
        this.f9396i.cancel();
        this.f9397j.start();
        if (z11) {
            this.f9397j.end();
        }
    }

    private ValueAnimator j(float... fArr) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.setInterpolator(AnimationUtils.f16555a);
        ofFloat.setDuration(100L);
        ofFloat.addUpdateListener(new h());
        return ofFloat;
    }

    private ValueAnimator k() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.8f, 1.0f);
        ofFloat.setInterpolator(AnimationUtils.f16558d);
        ofFloat.setDuration(150L);
        ofFloat.addUpdateListener(new i());
        return ofFloat;
    }

    private void l() {
        ValueAnimator k10 = k();
        ValueAnimator j10 = j(0.0f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        this.f9396i = animatorSet;
        animatorSet.playTogether(k10, j10);
        this.f9396i.addListener(new f());
        ValueAnimator j11 = j(1.0f, 0.0f);
        this.f9397j = j11;
        j11.addListener(new g());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean m() {
        EditText editText = this.f9442a.getEditText();
        return editText != null && (editText.hasFocus() || this.f9444c.hasFocus()) && editText.getText().length() > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.EndIconDelegate
    public void a() {
        TextInputLayout textInputLayout = this.f9442a;
        int i10 = this.f9445d;
        if (i10 == 0) {
            i10 = R$drawable.mtrl_ic_cancel;
        }
        textInputLayout.setEndIconDrawable(i10);
        TextInputLayout textInputLayout2 = this.f9442a;
        textInputLayout2.setEndIconContentDescription(textInputLayout2.getResources().getText(R$string.clear_text_end_icon_content_description));
        this.f9442a.setEndIconCheckable(false);
        this.f9442a.setEndIconOnClickListener(new e());
        this.f9442a.g(this.f9394g);
        this.f9442a.h(this.f9395h);
        l();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.EndIconDelegate
    public void c(boolean z10) {
        if (this.f9442a.getSuffixText() == null) {
            return;
        }
        i(z10);
    }
}
