package com.coui.appcompat.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$layout;
import com.support.appcompat.R$styleable;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUICardMultiInputView extends ConstraintLayout {
    private CharSequence B;
    private COUIEditText C;
    private LinearLayout D;
    private TextView E;
    private boolean F;
    private int G;
    private InputMethodManager H;

    /* loaded from: classes.dex */
    class a implements View.OnTouchListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f5680e;

        a(int i10) {
            this.f5680e = i10;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if ((action == 1 || action == 3) && motionEvent.getX() > this.f5680e && motionEvent.getX() < COUICardMultiInputView.this.getWidth() - this.f5680e) {
                if (COUICardMultiInputView.this.H == null) {
                    COUICardMultiInputView cOUICardMultiInputView = COUICardMultiInputView.this;
                    cOUICardMultiInputView.H = (InputMethodManager) cOUICardMultiInputView.getContext().getSystemService("input_method");
                }
                COUICardMultiInputView.this.C.setFocusable(true);
                COUICardMultiInputView.this.C.requestFocus();
                COUICardMultiInputView.this.H.showSoftInput(COUICardMultiInputView.this.C, 0);
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUICardMultiInputView.this.C.setPadding(COUICardMultiInputView.this.C.getPaddingLeft(), COUICardMultiInputView.this.C.getPaddingTop(), COUICardMultiInputView.this.C.getPaddingRight(), COUICardMultiInputView.this.E.getMeasuredHeight());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements TextWatcher {
        c() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            int length = editable.length();
            if (length < COUICardMultiInputView.this.G) {
                COUICardMultiInputView.this.E.setText(length + "/" + COUICardMultiInputView.this.G);
                COUICardMultiInputView.this.E.setTextColor(COUIContextUtil.a(COUICardMultiInputView.this.getContext(), R$attr.couiColorHintNeutral));
                return;
            }
            COUICardMultiInputView.this.E.setText(COUICardMultiInputView.this.G + "/" + COUICardMultiInputView.this.G);
            COUICardMultiInputView.this.E.setTextColor(COUIContextUtil.a(COUICardMultiInputView.this.getContext(), R$attr.couiColorError));
            if (length > COUICardMultiInputView.this.G) {
                COUICardMultiInputView.this.C.setText(editable.subSequence(0, COUICardMultiInputView.this.G));
            }
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }
    }

    public COUICardMultiInputView(Context context) {
        this(context, null);
    }

    private void I() {
        if (this.F && this.G > 0) {
            this.E.setVisibility(0);
            this.E.setText(this.C.getText().length() + "/" + this.G);
            this.C.post(new b());
            this.C.addTextChangedListener(new c());
            return;
        }
        this.E.setVisibility(8);
        COUIEditText cOUIEditText = this.C;
        cOUIEditText.setPadding(cOUIEditText.getPaddingLeft(), this.C.getPaddingTop(), this.C.getPaddingRight(), this.C.getPaddingTop());
    }

    private void J() {
        this.C.setTopHint(this.B);
        I();
    }

    protected COUIEditText K(Context context, AttributeSet attributeSet) {
        return new COUIEditText(context, attributeSet, R$attr.couiCardMultiInputEditTextStyle);
    }

    public COUIEditText getEditText() {
        return this.C;
    }

    public CharSequence getHint() {
        return this.B;
    }

    protected int getLayoutResId() {
        return R$layout.coui_multi_input_card_view;
    }

    public void setHint(CharSequence charSequence) {
        this.B = charSequence;
        this.C.setTopHint(charSequence);
    }

    public void setMaxCount(int i10) {
        this.G = i10;
        I();
    }

    public COUICardMultiInputView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUICardMultiInputView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIInputView, i10, 0);
        this.B = obtainStyledAttributes.getText(R$styleable.COUIInputView_couiHint);
        this.G = obtainStyledAttributes.getInt(R$styleable.COUIInputView_couiInputMaxCount, 0);
        this.F = obtainStyledAttributes.getBoolean(R$styleable.COUIInputView_couiEnableInputCount, false);
        obtainStyledAttributes.recycle();
        LayoutInflater.from(getContext()).inflate(getLayoutResId(), (ViewGroup) this, true);
        this.D = (LinearLayout) findViewById(R$id.edittext_container);
        COUIEditText K = K(context, attributeSet);
        this.C = K;
        K.setMaxLines(5);
        this.C.setGravity(8388659);
        this.D.addView(this.C, -1, -1);
        this.E = (TextView) findViewById(R$id.input_count);
        findViewById(R$id.single_card).setOnTouchListener(new a(getResources().getDimensionPixelSize(R$dimen.support_preference_category_layout_title_margin_start)));
        J();
    }
}
