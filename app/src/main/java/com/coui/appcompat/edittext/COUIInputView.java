package com.coui.appcompat.edittext;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.coui.appcompat.edittext.COUIEditText;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$layout;
import com.support.appcompat.R$styleable;
import m1.COUIEaseInterpolator;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUIInputView extends ConstraintLayout {
    protected View B;
    protected TextView C;
    protected boolean D;
    protected int E;
    protected int F;
    protected CheckBox G;
    protected COUIEditText H;
    protected l I;
    private CharSequence J;
    private CharSequence K;
    private boolean L;
    private int M;
    private boolean N;
    private TextView O;
    private TextView P;
    private ValueAnimator Q;
    private ValueAnimator R;
    private PathInterpolator S;
    private j T;
    private LinearLayout U;
    private Paint V;
    private Drawable W;

    /* renamed from: a0, reason: collision with root package name */
    private k f5767a0;

    /* renamed from: b0, reason: collision with root package name */
    private boolean f5768b0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIInputView.this.f5767a0 != null) {
                COUIInputView.this.f5767a0.onClick(view);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements COUIEditText.i {
        b() {
        }

        @Override // com.coui.appcompat.edittext.COUIEditText.i
        public void a(boolean z10) {
        }

        @Override // com.coui.appcompat.edittext.COUIEditText.i
        public void b(boolean z10) {
            COUIInputView.this.H.setSelectAllOnFocus(z10);
            if (z10) {
                COUIInputView.this.X();
            } else {
                COUIInputView.this.P();
            }
            if (COUIInputView.this.T != null) {
                COUIInputView.this.T.a(z10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements TextWatcher {
        c() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            l lVar = COUIInputView.this.I;
            if (lVar != null) {
                lVar.a(editable);
            } else {
                int length = editable.length();
                COUIInputView cOUIInputView = COUIInputView.this;
                if (length < cOUIInputView.E) {
                    cOUIInputView.C.setText(length + "/" + COUIInputView.this.E);
                    COUIInputView cOUIInputView2 = COUIInputView.this;
                    cOUIInputView2.C.setTextColor(COUIContextUtil.a(cOUIInputView2.getContext(), R$attr.couiColorHintNeutral));
                } else {
                    cOUIInputView.C.setText(COUIInputView.this.E + "/" + COUIInputView.this.E);
                    COUIInputView cOUIInputView3 = COUIInputView.this;
                    cOUIInputView3.C.setTextColor(COUIContextUtil.a(cOUIInputView3.getContext(), R$attr.couiColorError));
                    COUIInputView cOUIInputView4 = COUIInputView.this;
                    int i10 = cOUIInputView4.E;
                    if (length > i10) {
                        cOUIInputView4.H.setText(editable.subSequence(0, i10));
                    }
                }
            }
            COUIInputView cOUIInputView5 = COUIInputView.this;
            cOUIInputView5.Y(cOUIInputView5.hasFocus());
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements View.OnFocusChangeListener {
        d() {
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z10) {
            COUIInputView.this.Y(z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ boolean f5773e;

        e(boolean z10) {
            this.f5773e = z10;
        }

        @Override // java.lang.Runnable
        public void run() {
            int deleteIconWidth = (!TextUtils.isEmpty(COUIInputView.this.H.getText()) && this.f5773e && COUIInputView.this.H.A()) ? COUIInputView.this.H.getDeleteIconWidth() : 0;
            if (COUIInputView.this.L) {
                deleteIconWidth += COUIInputView.this.B.getWidth();
            }
            TextView textView = COUIInputView.this.C;
            textView.setPaddingRelative(0, 0, deleteIconWidth, textView.getPaddingBottom());
            if (this.f5773e && !TextUtils.isEmpty(COUIInputView.this.H.getText())) {
                int width = COUIInputView.this.L ? COUIInputView.this.B.getWidth() : 0;
                if (!COUIInputView.this.H.A()) {
                    width += COUIInputView.this.getCountTextWidth();
                }
                COUIEditText cOUIEditText = COUIInputView.this.H;
                cOUIEditText.setPaddingRelative(0, cOUIEditText.getPaddingTop(), width, COUIInputView.this.H.getPaddingBottom());
                COUIInputView cOUIInputView = COUIInputView.this;
                cOUIInputView.H.setCompoundDrawablePadding(cOUIInputView.getCountTextWidth());
                return;
            }
            COUIEditText cOUIEditText2 = COUIInputView.this.H;
            cOUIEditText2.setPaddingRelative(0, cOUIEditText2.getPaddingTop(), (COUIInputView.this.L ? COUIInputView.this.B.getWidth() : 0) + COUIInputView.this.getCountTextWidth(), COUIInputView.this.H.getPaddingBottom());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f implements CompoundButton.OnCheckedChangeListener {
        f() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z10) {
            if (z10) {
                COUIInputView cOUIInputView = COUIInputView.this;
                if (cOUIInputView.F == 1) {
                    cOUIInputView.H.setInputType(2);
                    return;
                } else {
                    cOUIInputView.H.setInputType(145);
                    return;
                }
            }
            COUIInputView cOUIInputView2 = COUIInputView.this;
            if (cOUIInputView2.F == 1) {
                cOUIInputView2.H.setInputType(18);
            } else {
                cOUIInputView2.H.setInputType(129);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class g implements Runnable {
        g() {
        }

        @Override // java.lang.Runnable
        public void run() {
            TextView textView = COUIInputView.this.C;
            textView.setPaddingRelative(0, 0, textView.getPaddingEnd() + COUIInputView.this.B.getWidth(), COUIInputView.this.C.getPaddingBottom());
            if (!COUIInputView.this.L && COUIInputView.this.W != null) {
                COUIEditText cOUIEditText = COUIInputView.this.H;
                cOUIEditText.setPaddingRelative(cOUIEditText.getPaddingStart(), COUIInputView.this.H.getPaddingTop(), (COUIInputView.this.H.getPaddingEnd() + COUIInputView.this.B.getWidth()) - COUIInputView.this.G.getWidth(), COUIInputView.this.H.getPaddingBottom());
            } else {
                COUIEditText cOUIEditText2 = COUIInputView.this.H;
                cOUIEditText2.setPaddingRelative(cOUIEditText2.getPaddingStart(), COUIInputView.this.H.getPaddingTop(), COUIInputView.this.H.getPaddingEnd() + COUIInputView.this.B.getWidth(), COUIInputView.this.H.getPaddingBottom());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class h implements ValueAnimator.AnimatorUpdateListener {
        h() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIInputView.this.O.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class i implements ValueAnimator.AnimatorUpdateListener {
        i() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIInputView.this.O.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* loaded from: classes.dex */
    public interface j {
        void a(boolean z10);
    }

    /* loaded from: classes.dex */
    public interface k {
        void onClick(View view);
    }

    /* loaded from: classes.dex */
    public interface l {
        void a(Editable editable);
    }

    public COUIInputView(Context context) {
        this(context, null);
    }

    private void M() {
        if (this.N) {
            this.O.setVisibility(0);
            this.H.k(new b());
        } else {
            this.O.setVisibility(8);
        }
    }

    private void O() {
        if (TextUtils.isEmpty(this.K)) {
            return;
        }
        this.P.setText(this.K);
        this.P.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void P() {
        ValueAnimator valueAnimator = this.Q;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.Q.cancel();
        }
        if (this.R == null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            this.R = ofFloat;
            ofFloat.setDuration(283L).setInterpolator(this.S);
            this.R.addUpdateListener(new i());
        }
        if (this.R.isStarted()) {
            this.R.cancel();
        }
        this.R.start();
    }

    private void Q(Context context, AttributeSet attributeSet) {
        O();
        this.H.setTopHint(this.J);
        if (this.f5768b0) {
            this.H.setDefaultStrokeColor(COUIContextUtil.a(getContext(), R$attr.couiColorPrimary));
        }
        L();
        N();
        M();
        Z();
        R();
    }

    private void R() {
        CheckBox checkBox;
        if (this.W == null || (checkBox = this.G) == null) {
            return;
        }
        checkBox.setVisibility(0);
        this.G.setButtonDrawable(this.W);
        this.G.setOnClickListener(new a());
    }

    private void W() {
        int i10 = this.F;
        if (i10 == -1) {
            return;
        }
        if (i10 == 0) {
            this.H.setInputType(1);
            return;
        }
        if (i10 == 1) {
            this.H.setInputType(2);
        } else if (i10 != 2) {
            this.H.setInputType(0);
        } else {
            this.H.setInputType(18);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void X() {
        ValueAnimator valueAnimator = this.R;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.R.cancel();
        }
        if (this.Q == null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.Q = ofFloat;
            ofFloat.setDuration(217L).setInterpolator(this.S);
            this.Q.addUpdateListener(new h());
        }
        if (this.Q.isStarted()) {
            this.Q.cancel();
        }
        this.Q.start();
    }

    private void Z() {
        b0();
        a0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCountTextWidth() {
        if (!this.D) {
            return 0;
        }
        if (this.V == null) {
            Paint paint = new Paint();
            this.V = paint;
            paint.setTextSize(this.C.getTextSize());
        }
        return ((int) this.V.measureText((String) this.C.getText())) + 8;
    }

    protected void L() {
        if (!this.D || this.E <= 0) {
            return;
        }
        this.C.setVisibility(0);
        this.C.setText(this.H.getText().length() + "/" + this.E);
        this.H.addTextChangedListener(new c());
        this.H.setOnFocusChangeListener(new d());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void N() {
        if (this.L) {
            CheckBox checkBox = (CheckBox) findViewById(R$id.checkbox_password);
            checkBox.setVisibility(0);
            if (this.M == 1) {
                checkBox.setChecked(false);
                if (this.F == 1) {
                    this.H.setInputType(18);
                } else {
                    this.H.setInputType(129);
                }
            } else {
                checkBox.setChecked(true);
                if (this.F == 1) {
                    this.H.setInputType(2);
                } else {
                    this.H.setInputType(145);
                }
            }
            checkBox.setOnCheckedChangeListener(new f());
            return;
        }
        W();
    }

    protected COUIEditText S(Context context, AttributeSet attributeSet) {
        return new COUIEditText(context, attributeSet, R$attr.couiInputPreferenceEditTextStyle);
    }

    public boolean T() {
        return this.D;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void U(Context context, AttributeSet attributeSet) {
        COUIEditText S = S(context, attributeSet);
        this.H = S;
        S.setMaxLines(5);
        this.U.addView(this.H, -1, -2);
        Q(context, attributeSet);
    }

    protected void V(Context context, AttributeSet attributeSet) {
        U(context, attributeSet);
    }

    protected void Y(boolean z10) {
        post(new e(z10));
    }

    protected void a0() {
        if (this.L || this.W != null) {
            this.H.post(new g());
        }
    }

    protected void b0() {
        CheckBox checkBox = (CheckBox) findViewById(R$id.checkbox_password);
        int dimension = (int) getResources().getDimension(R$dimen.coui_input_edit_text_no_title_padding_top);
        int dimension2 = (int) getResources().getDimension(R$dimen.coui_input_edit_text_no_title_padding_bottom);
        if (!TextUtils.isEmpty(this.K)) {
            dimension = getResources().getDimensionPixelSize(R$dimen.coui_input_edit_text_has_title_padding_top);
            dimension2 = getResources().getDimensionPixelSize(getHasTitlePaddingBottomDimen());
            if (this.N) {
                dimension2 = getResources().getDimensionPixelSize(R$dimen.coui_input_edit_text_error_padding_bottom);
                TextView textView = this.O;
                textView.setPaddingRelative(textView.getPaddingStart(), this.O.getPaddingTop(), this.O.getPaddingEnd(), dimension2);
            }
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) checkBox.getLayoutParams();
            marginLayoutParams.topMargin = getResources().getDimensionPixelSize(R$dimen.coui_input_eye_has_title_padding_top);
            checkBox.setLayoutParams(marginLayoutParams);
        } else {
            if (this.N) {
                dimension2 = getResources().getDimensionPixelSize(R$dimen.coui_input_edit_text_error_padding_bottom);
                TextView textView2 = this.O;
                textView2.setPaddingRelative(textView2.getPaddingStart(), this.O.getPaddingTop(), this.O.getPaddingEnd(), dimension2);
            }
            ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) checkBox.getLayoutParams();
            marginLayoutParams2.topMargin = getResources().getDimensionPixelSize(R$dimen.coui_input_eye_no_title_padding_top);
            checkBox.setLayoutParams(marginLayoutParams2);
        }
        View view = this.B;
        view.setPaddingRelative(view.getPaddingStart(), this.B.getPaddingTop(), this.B.getPaddingEnd(), dimension2 + 3);
        this.H.setPaddingRelative(0, dimension, getCountTextWidth(), dimension2);
        this.C.setPaddingRelative(0, 0, 0, dimension2 + 10);
    }

    public TextView getCountTextView() {
        return this.C;
    }

    public COUIEditText getEditText() {
        return this.H;
    }

    protected int getHasTitlePaddingBottomDimen() {
        return R$dimen.coui_input_edit_text_has_title_padding_bottom;
    }

    public CharSequence getHint() {
        return this.J;
    }

    protected int getLayoutResId() {
        return R$layout.coui_input_view;
    }

    public int getMaxCount() {
        return this.E;
    }

    public CharSequence getTitle() {
        return this.K;
    }

    public void setEnableError(boolean z10) {
        if (this.N != z10) {
            this.N = z10;
            M();
            Z();
        }
    }

    public void setEnablePassword(boolean z10) {
        if (this.L != z10) {
            this.L = z10;
            N();
            a0();
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        super.setEnabled(z10);
        this.H.setEnabled(z10);
        this.P.setEnabled(z10);
    }

    public void setErrorStateChangeCallBack(j jVar) {
        this.T = jVar;
    }

    public void setHint(CharSequence charSequence) {
        this.J = charSequence;
        this.H.setTopHint(charSequence);
    }

    public void setMaxCount(int i10) {
        this.E = i10;
        L();
    }

    public void setOnCustomIconClickListener(k kVar) {
        this.f5767a0 = kVar;
    }

    public void setOnEditTextChangeListener(l lVar) {
        this.I = lVar;
    }

    public void setPasswordType(int i10) {
        if (this.M != i10) {
            this.M = i10;
            N();
        }
    }

    public void setTitle(CharSequence charSequence) {
        if (charSequence == null || charSequence.equals(this.K)) {
            return;
        }
        this.K = charSequence;
        O();
        b0();
    }

    public COUIInputView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIInputView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.I = null;
        this.S = new COUIEaseInterpolator();
        this.V = null;
        this.f5768b0 = false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIInputView, i10, 0);
        this.K = obtainStyledAttributes.getText(R$styleable.COUIInputView_couiTitle);
        this.J = obtainStyledAttributes.getText(R$styleable.COUIInputView_couiHint);
        this.L = obtainStyledAttributes.getBoolean(R$styleable.COUIInputView_couiEnablePassword, false);
        this.M = obtainStyledAttributes.getInt(R$styleable.COUIInputView_couiPasswordType, 0);
        this.N = obtainStyledAttributes.getBoolean(R$styleable.COUIInputView_couiEnableError, false);
        this.E = obtainStyledAttributes.getInt(R$styleable.COUIInputView_couiInputMaxCount, 0);
        this.D = obtainStyledAttributes.getBoolean(R$styleable.COUIInputView_couiEnableInputCount, false);
        this.F = obtainStyledAttributes.getInt(R$styleable.COUIInputView_couiInputType, -1);
        this.W = obtainStyledAttributes.getDrawable(R$styleable.COUIInputView_couiCustomIcon);
        this.f5768b0 = obtainStyledAttributes.getBoolean(R$styleable.COUIInputView_couiEditLineColor, false);
        obtainStyledAttributes.recycle();
        LayoutInflater.from(getContext()).inflate(getLayoutResId(), (ViewGroup) this, true);
        this.P = (TextView) findViewById(R$id.title);
        this.C = (TextView) findViewById(R$id.input_count);
        this.O = (TextView) findViewById(R$id.text_input_error);
        this.B = findViewById(R$id.button_layout);
        this.U = (LinearLayout) findViewById(R$id.edittext_container);
        this.G = (CheckBox) findViewById(R$id.checkbox_custom);
        V(context, attributeSet);
    }
}
