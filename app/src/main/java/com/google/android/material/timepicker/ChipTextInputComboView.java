package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.material.R$id;
import com.google.android.material.R$layout;
import com.google.android.material.chip.Chip;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.textfield.TextInputLayout;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ChipTextInputComboView extends FrameLayout implements Checkable {

    /* renamed from: e, reason: collision with root package name */
    private final Chip f9490e;

    /* renamed from: f, reason: collision with root package name */
    private final TextInputLayout f9491f;

    /* renamed from: g, reason: collision with root package name */
    private final EditText f9492g;

    /* renamed from: h, reason: collision with root package name */
    private TextWatcher f9493h;

    /* renamed from: i, reason: collision with root package name */
    private TextView f9494i;

    /* loaded from: classes.dex */
    private class b extends TextWatcherAdapter {
        private b() {
        }

        @Override // com.google.android.material.internal.TextWatcherAdapter, android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (TextUtils.isEmpty(editable)) {
                ChipTextInputComboView.this.f9490e.setText(ChipTextInputComboView.this.c("00"));
            } else {
                ChipTextInputComboView.this.f9490e.setText(ChipTextInputComboView.this.c(editable));
            }
        }
    }

    public ChipTextInputComboView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String c(CharSequence charSequence) {
        return TimeModel.j(getResources(), charSequence);
    }

    private void d() {
        this.f9492g.setImeHintLocales(getContext().getResources().getConfiguration().getLocales());
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.f9490e.isChecked();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        d();
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z10) {
        this.f9490e.setChecked(z10);
        this.f9492g.setVisibility(z10 ? 0 : 4);
        this.f9490e.setVisibility(z10 ? 8 : 0);
        if (isChecked()) {
            ViewUtils.requestFocusAndShowKeyboard(this.f9492g);
            if (TextUtils.isEmpty(this.f9492g.getText())) {
                return;
            }
            EditText editText = this.f9492g;
            editText.setSelection(editText.getText().length());
        }
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.f9490e.setOnClickListener(onClickListener);
    }

    @Override // android.view.View
    public void setTag(int i10, Object obj) {
        this.f9490e.setTag(i10, obj);
    }

    @Override // android.widget.Checkable
    public void toggle() {
        this.f9490e.toggle();
    }

    public ChipTextInputComboView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        LayoutInflater from = LayoutInflater.from(context);
        Chip chip = (Chip) from.inflate(R$layout.material_time_chip, (ViewGroup) this, false);
        this.f9490e = chip;
        chip.setAccessibilityClassName("android.view.View");
        TextInputLayout textInputLayout = (TextInputLayout) from.inflate(R$layout.material_time_input, (ViewGroup) this, false);
        this.f9491f = textInputLayout;
        EditText editText = textInputLayout.getEditText();
        this.f9492g = editText;
        editText.setVisibility(4);
        b bVar = new b();
        this.f9493h = bVar;
        editText.addTextChangedListener(bVar);
        d();
        addView(chip);
        addView(textInputLayout);
        this.f9494i = (TextView) findViewById(R$id.material_label);
        editText.setSaveEnabled(false);
        editText.setLongClickable(false);
    }
}
