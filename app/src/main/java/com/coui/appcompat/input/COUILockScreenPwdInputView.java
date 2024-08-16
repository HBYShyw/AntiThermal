package com.coui.appcompat.input;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import com.coui.appcompat.edittext.COUIEditText;
import com.coui.appcompat.edittext.COUIInputView;
import com.support.component.R$attr;
import com.support.component.R$dimen;
import com.support.component.R$drawable;
import com.support.component.R$id;
import com.support.component.R$layout;
import z2.COUIChangeTextUtil;

/* loaded from: classes.dex */
public class COUILockScreenPwdInputView extends COUIInputView {

    /* renamed from: c0, reason: collision with root package name */
    private final int f6247c0;

    /* renamed from: d0, reason: collision with root package name */
    private final int f6248d0;

    /* renamed from: e0, reason: collision with root package name */
    private int f6249e0;

    /* renamed from: f0, reason: collision with root package name */
    private View f6250f0;

    /* renamed from: g0, reason: collision with root package name */
    private int f6251g0;

    /* renamed from: h0, reason: collision with root package name */
    private TextWatcher f6252h0;

    /* renamed from: i0, reason: collision with root package name */
    private int f6253i0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements TextWatcher {
        a() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (((COUIInputView) COUILockScreenPwdInputView.this).D && ((COUIInputView) COUILockScreenPwdInputView.this).E > 0 && editable.length() > ((COUIInputView) COUILockScreenPwdInputView.this).E) {
                ((COUIInputView) COUILockScreenPwdInputView.this).H.setText(editable.subSequence(0, ((COUIInputView) COUILockScreenPwdInputView.this).E));
            }
            if (((COUIInputView) COUILockScreenPwdInputView.this).I != null) {
                ((COUIInputView) COUILockScreenPwdInputView.this).I.a(((COUIInputView) COUILockScreenPwdInputView.this).H.getText());
            }
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }
    }

    public COUILockScreenPwdInputView(Context context) {
        this(context, null);
    }

    private void n0() {
        this.f6249e0 = getResources().getDimensionPixelOffset(R$dimen.coui_input_lock_screen_pwd_setting_width);
        this.f6250f0 = findViewById(R$id.lock_screen_pwd_card);
        getEditText().setVerticalScrollBarEnabled(false);
        COUIChangeTextUtil.c(getEditText(), 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void o0() {
        this.f6250f0.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p0() {
        COUIEditText cOUIEditText = this.H;
        cOUIEditText.setPaddingRelative(cOUIEditText.getPaddingStart(), this.H.getPaddingTop(), this.B.getWidth() - this.f6248d0, this.H.getPaddingBottom());
    }

    @Override // com.coui.appcompat.edittext.COUIInputView
    protected void L() {
        TextWatcher textWatcher = this.f6252h0;
        if (textWatcher != null) {
            this.H.removeTextChangedListener(textWatcher);
        }
        a aVar = new a();
        this.f6252h0 = aVar;
        this.H.addTextChangedListener(aVar);
    }

    @Override // com.coui.appcompat.edittext.COUIInputView
    protected COUIEditText S(Context context, AttributeSet attributeSet) {
        if (this.f6253i0 == 1) {
            return new COUIEditText(context, attributeSet, R$attr.COUICardLockScreenPwdInputStyleEditDesktop);
        }
        return new COUIEditText(context, attributeSet, R$attr.COUICardLockScreenPwdInputStyleEdit);
    }

    @Override // com.coui.appcompat.edittext.COUIInputView
    protected void V(Context context, AttributeSet attributeSet) {
    }

    @Override // com.coui.appcompat.edittext.COUIInputView
    protected void a0() {
        this.H.post(new Runnable() { // from class: com.coui.appcompat.input.d
            @Override // java.lang.Runnable
            public final void run() {
                COUILockScreenPwdInputView.this.p0();
            }
        });
    }

    @Override // com.coui.appcompat.edittext.COUIInputView
    protected void b0() {
        COUIEditText editText = getEditText();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_input_lock_screen_pwd_title_padding_top);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.coui_input_lock_screen_pwd_title_padding_bottom);
        View view = this.B;
        view.setPaddingRelative(view.getPaddingStart(), dimensionPixelSize, this.B.getPaddingEnd(), dimensionPixelSize2);
        editText.setPaddingRelative(0, dimensionPixelSize, 0, dimensionPixelSize2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getInputCount() {
        String couiEditTexttNoEllipsisText = this.H.getCouiEditTexttNoEllipsisText();
        if (this.H.getText() == null || couiEditTexttNoEllipsisText.length() <= 0) {
            return 0;
        }
        return couiEditTexttNoEllipsisText.length();
    }

    @Override // com.coui.appcompat.edittext.COUIInputView
    protected int getLayoutResId() {
        return R$layout.coui_input_lock_screen_pwd_view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMinInputCount() {
        return this.f6251g0;
    }

    public View getmLockScreenPwdCard() {
        return this.f6250f0;
    }

    void m0() {
        String couiEditTexttNoEllipsisText = this.H.getCouiEditTexttNoEllipsisText();
        if (this.E <= 0 || this.H.getText() == null) {
            return;
        }
        int length = couiEditTexttNoEllipsisText.length();
        int i10 = this.E;
        if (length > i10) {
            this.H.setText(couiEditTexttNoEllipsisText.subSequence(0, i10));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q0(AttributeSet attributeSet, int i10) {
        this.f6253i0 = i10;
        U(getContext(), attributeSet);
        n0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r0() {
        ((CheckBox) findViewById(R$id.checkbox_password)).setButtonDrawable(R$drawable.coui_edittext_password_icon_desktop);
    }

    public void s0() {
        if (this.f6250f0 == null) {
            return;
        }
        this.f6250f0.getLayoutParams().width = (int) (this.f6249e0 * (Math.min(getResources().getConfiguration().screenWidthDp, 360.0d) / 360.0d));
        this.f6250f0.post(new Runnable() { // from class: com.coui.appcompat.input.c
            @Override // java.lang.Runnable
            public final void run() {
                COUILockScreenPwdInputView.this.o0();
            }
        });
    }

    public void setDefaultInputLockScreenPwdWidth(int i10) {
        this.f6249e0 = i10;
        s0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setEnableInputCount(boolean z10) {
        this.D = z10;
        m0();
        L();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInputType(int i10) {
        if (this.F == i10) {
            return;
        }
        this.F = i10;
        N();
    }

    @Override // com.coui.appcompat.edittext.COUIInputView
    public void setMaxCount(int i10) {
        this.E = i10;
        m0();
        L();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMinInputCount(int i10) {
        this.f6251g0 = i10;
    }

    public COUILockScreenPwdInputView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUILockScreenPwdInputView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f6247c0 = 360;
        this.f6248d0 = getResources().getDimensionPixelOffset(R$dimen.coui_input_lock_screen_pwd_edit_margin);
        this.f6251g0 = 6;
        this.f6253i0 = 0;
    }
}
