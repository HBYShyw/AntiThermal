package com.coui.appcompat.input;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.coui.appcompat.edittext.COUIEditText;
import com.coui.appcompat.edittext.COUIInputView;
import com.support.component.R$color;
import com.support.component.R$dimen;
import com.support.component.R$drawable;
import com.support.component.R$id;
import com.support.component.R$layout;
import com.support.component.R$styleable;

/* loaded from: classes.dex */
public class COUILockScreenPwdInputLayout extends ConstraintLayout {
    public static final int H = R$color.coui_input_lock_screen_pwd_view_bg_color_desktop;
    private COUILockScreenPwdInputView B;
    private ImageView C;
    private a D;
    private COUIInputView.l E;
    private int F;
    private boolean G;

    /* loaded from: classes.dex */
    public interface a {
        void a(String str);
    }

    public COUILockScreenPwdInputLayout(Context context) {
        this(context, null);
    }

    private void F(boolean z10) {
        if (this.G == z10) {
            return;
        }
        this.G = z10;
        if (z10 && this.F == 1) {
            M();
            return;
        }
        if (!z10 && this.F == 1) {
            O();
        } else if (z10) {
            N();
        } else {
            P();
        }
    }

    private void G(Context context, AttributeSet attributeSet, int i10) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUILockScreenPwdInputLayout, i10, 0);
        boolean z10 = obtainStyledAttributes.getBoolean(R$styleable.COUILockScreenPwdInputLayout_couiEnableInputCount, false);
        int i11 = obtainStyledAttributes.getInt(R$styleable.COUILockScreenPwdInputLayout_couiInputMaxCount, 0);
        int i12 = obtainStyledAttributes.getInt(R$styleable.COUILockScreenPwdInputLayout_couiInputType, 2);
        this.F = obtainStyledAttributes.getInt(R$styleable.COUILockScreenPwdInputLayout_couiIsScenesMode, 0);
        int i13 = obtainStyledAttributes.getInt(R$styleable.COUILockScreenPwdInputLayout_couiInputMinCount, 6);
        obtainStyledAttributes.recycle();
        COUILockScreenPwdInputView cOUILockScreenPwdInputView = (COUILockScreenPwdInputView) findViewById(R$id.coui_lock_screen_pwd_input_view);
        this.B = cOUILockScreenPwdInputView;
        cOUILockScreenPwdInputView.q0(attributeSet, this.F);
        this.B.setInputType(i12);
        this.B.setEnableInputCount(z10);
        this.C = (ImageView) findViewById(R$id.iv_intput_next);
        H(i11, i13);
        I();
        J(context);
    }

    private void H(int i10, int i11) {
        if (i10 > 0 && i11 > 0 && i10 > i11) {
            this.B.setMaxCount(i10);
            this.B.setMinInputCount(i11);
        } else {
            this.B.setMaxCount(16);
            this.B.setMinInputCount(6);
        }
    }

    private void I() {
        this.C.setOnClickListener(new View.OnClickListener() { // from class: com.coui.appcompat.input.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                COUILockScreenPwdInputLayout.this.K(view);
            }
        });
        this.B.setOnEditTextChangeListener(new COUIInputView.l() { // from class: com.coui.appcompat.input.b
            @Override // com.coui.appcompat.edittext.COUIInputView.l
            public final void a(Editable editable) {
                COUILockScreenPwdInputLayout.this.L(editable);
            }
        });
    }

    private void J(Context context) {
        int i10 = this.F;
        if (i10 == 1) {
            this.B.setDefaultInputLockScreenPwdWidth(getResources().getDimensionPixelOffset(R$dimen.coui_input_lock_screen_pwd_desktop_width));
            this.B.r0();
            COUIEditText editText = this.B.getEditText();
            Resources resources = getResources();
            int i11 = R$color.coui_input_lock_screen_pwd_view_edittext_text_color_desktop;
            editText.setTextColor(resources.getColor(i11, context.getTheme()));
            this.B.getEditText().setEditTextColor(getResources().getColor(i11, context.getTheme()));
            O();
        } else if (i10 == 2) {
            this.B.setDefaultInputLockScreenPwdWidth(getResources().getDimensionPixelOffset(R$dimen.coui_input_lock_screen_pwd_setting1_width));
        }
        if (this.B.T() && this.B.getInputCount() < this.B.getMinInputCount()) {
            F(false);
        } else {
            F(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void K(View view) {
        a aVar;
        if ((!this.B.T() || this.B.getMinInputCount() <= this.B.getInputCount()) && (aVar = this.D) != null) {
            aVar.a(this.B.getEditText().getCouiEditTexttNoEllipsisText());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void L(Editable editable) {
        if (this.B.T()) {
            if (this.B.getMinInputCount() <= editable.length()) {
                F(true);
            } else {
                F(false);
            }
        }
        COUIInputView.l lVar = this.E;
        if (lVar != null) {
            lVar.a(editable);
        }
    }

    private void M() {
        this.C.setBackgroundResource(R$drawable.coui_input_lock_screen_pwd_next_desktop_bg_allow);
        this.C.setImageResource(R$drawable.coui_input_lock_screen_pwd_next_desktop_src);
    }

    private void N() {
        this.C.setImageResource(R$drawable.coui_input_lock_screen_pwd_next_src_allow);
        this.C.setBackgroundResource(R$drawable.coui_input_lock_screen_pwd_next_bg);
    }

    private void O() {
        this.C.setBackgroundResource(R$drawable.coui_input_lock_screen_pwd_next_desktop_bg);
        this.C.setImageResource(R$drawable.coui_input_lock_screen_pwd_next_desktop_src);
    }

    private void P() {
        this.C.setBackgroundResource(R$drawable.coui_input_lock_screen_pwd_next_bg);
        this.C.setImageResource(R$drawable.coui_input_lock_screen_pwd_next_src);
    }

    public COUILockScreenPwdInputView getInputView() {
        return this.B;
    }

    protected int getLayoutResId() {
        return R$layout.coui_input_lock_screen_pwd_layout;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        if (i10 != i12) {
            this.B.s0();
        }
    }

    public void setCOUIEditTextChangeListener(COUIInputView.l lVar) {
        this.E = lVar;
    }

    public void setCOUIInputType(int i10) {
        this.B.setInputType(i10);
    }

    public void setNextIcOnClickListener(a aVar) {
        this.D = aVar;
    }

    public COUILockScreenPwdInputLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUILockScreenPwdInputLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.F = 0;
        this.G = false;
        LayoutInflater.from(getContext()).inflate(getLayoutResId(), (ViewGroup) this, true);
        G(context, attributeSet, i10);
    }
}
