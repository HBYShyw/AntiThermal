package com.google.android.material.textfield;

import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import com.google.android.material.R$drawable;
import com.google.android.material.R$string;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputLayout;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: PasswordToggleEndIconDelegate.java */
/* renamed from: com.google.android.material.textfield.i, reason: use source file name */
/* loaded from: classes.dex */
public class PasswordToggleEndIconDelegate extends EndIconDelegate {

    /* renamed from: e, reason: collision with root package name */
    private final TextWatcher f9473e;

    /* renamed from: f, reason: collision with root package name */
    private final TextInputLayout.f f9474f;

    /* renamed from: g, reason: collision with root package name */
    private final TextInputLayout.g f9475g;

    /* compiled from: PasswordToggleEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.i$a */
    /* loaded from: classes.dex */
    class a extends TextWatcherAdapter {
        a() {
        }

        @Override // com.google.android.material.internal.TextWatcherAdapter, android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
            PasswordToggleEndIconDelegate.this.f9444c.setChecked(!r0.g());
        }
    }

    /* compiled from: PasswordToggleEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.i$b */
    /* loaded from: classes.dex */
    class b implements TextInputLayout.f {
        b() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.f
        public void a(TextInputLayout textInputLayout) {
            EditText editText = textInputLayout.getEditText();
            PasswordToggleEndIconDelegate.this.f9444c.setChecked(!r0.g());
            editText.removeTextChangedListener(PasswordToggleEndIconDelegate.this.f9473e);
            editText.addTextChangedListener(PasswordToggleEndIconDelegate.this.f9473e);
        }
    }

    /* compiled from: PasswordToggleEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.i$c */
    /* loaded from: classes.dex */
    class c implements TextInputLayout.g {

        /* compiled from: PasswordToggleEndIconDelegate.java */
        /* renamed from: com.google.android.material.textfield.i$c$a */
        /* loaded from: classes.dex */
        class a implements Runnable {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ EditText f9479e;

            a(EditText editText) {
                this.f9479e = editText;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.f9479e.removeTextChangedListener(PasswordToggleEndIconDelegate.this.f9473e);
            }
        }

        c() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.g
        public void a(TextInputLayout textInputLayout, int i10) {
            EditText editText = textInputLayout.getEditText();
            if (editText == null || i10 != 1) {
                return;
            }
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.post(new a(editText));
        }
    }

    /* compiled from: PasswordToggleEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.i$d */
    /* loaded from: classes.dex */
    class d implements View.OnClickListener {
        d() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            EditText editText = PasswordToggleEndIconDelegate.this.f9442a.getEditText();
            if (editText == null) {
                return;
            }
            int selectionEnd = editText.getSelectionEnd();
            if (PasswordToggleEndIconDelegate.this.g()) {
                editText.setTransformationMethod(null);
            } else {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            if (selectionEnd >= 0) {
                editText.setSelection(selectionEnd);
            }
            PasswordToggleEndIconDelegate.this.f9442a.U();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PasswordToggleEndIconDelegate(TextInputLayout textInputLayout, int i10) {
        super(textInputLayout, i10);
        this.f9473e = new a();
        this.f9474f = new b();
        this.f9475g = new c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean g() {
        EditText editText = this.f9442a.getEditText();
        return editText != null && (editText.getTransformationMethod() instanceof PasswordTransformationMethod);
    }

    private static boolean h(EditText editText) {
        return editText != null && (editText.getInputType() == 16 || editText.getInputType() == 128 || editText.getInputType() == 144 || editText.getInputType() == 224);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.EndIconDelegate
    public void a() {
        TextInputLayout textInputLayout = this.f9442a;
        int i10 = this.f9445d;
        if (i10 == 0) {
            i10 = R$drawable.design_password_eye;
        }
        textInputLayout.setEndIconDrawable(i10);
        TextInputLayout textInputLayout2 = this.f9442a;
        textInputLayout2.setEndIconContentDescription(textInputLayout2.getResources().getText(R$string.password_toggle_content_description));
        this.f9442a.setEndIconVisible(true);
        this.f9442a.setEndIconCheckable(true);
        this.f9442a.setEndIconOnClickListener(new d());
        this.f9442a.g(this.f9474f);
        this.f9442a.h(this.f9475g);
        EditText editText = this.f9442a.getEditText();
        if (h(editText)) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}
