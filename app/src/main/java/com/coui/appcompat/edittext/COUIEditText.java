package com.coui.appcompat.edittext;

import android.R;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.coui.appcompat.edittext.COUICutoutDrawable;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$string;
import com.support.appcompat.R$style;
import com.support.appcompat.R$styleable;
import java.util.List;
import m1.COUIInEaseInterpolator;
import m1.COUILinearInterpolator;
import m1.COUIMoveEaseInterpolator;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUIEditText extends AppCompatEditText {
    private g A;
    private h A0;
    private CharSequence B;
    private COUIErrorEditTextHelper B0;
    private boolean C;
    private Runnable C0;
    private CharSequence D;
    private Runnable D0;
    private boolean E;
    private GradientDrawable F;
    private int G;
    private int H;
    private float I;
    private float J;
    private float K;
    private float L;
    private int M;
    private int N;
    private int O;
    private int P;
    private RectF Q;
    private ColorStateList R;
    private ColorStateList S;
    private int T;
    private int U;
    private int V;
    private int W;

    /* renamed from: a0, reason: collision with root package name */
    private boolean f5714a0;

    /* renamed from: b0, reason: collision with root package name */
    private boolean f5715b0;

    /* renamed from: c0, reason: collision with root package name */
    private ValueAnimator f5716c0;

    /* renamed from: d0, reason: collision with root package name */
    private ValueAnimator f5717d0;

    /* renamed from: e0, reason: collision with root package name */
    private ValueAnimator f5718e0;

    /* renamed from: f0, reason: collision with root package name */
    private boolean f5719f0;

    /* renamed from: g0, reason: collision with root package name */
    private boolean f5720g0;

    /* renamed from: h0, reason: collision with root package name */
    private boolean f5721h0;

    /* renamed from: i0, reason: collision with root package name */
    private Paint f5722i0;

    /* renamed from: j0, reason: collision with root package name */
    private Paint f5723j0;

    /* renamed from: k, reason: collision with root package name */
    private final COUICutoutDrawable.a f5724k;

    /* renamed from: k0, reason: collision with root package name */
    private Paint f5725k0;

    /* renamed from: l, reason: collision with root package name */
    private Interpolator f5726l;

    /* renamed from: l0, reason: collision with root package name */
    private Paint f5727l0;

    /* renamed from: m, reason: collision with root package name */
    private Interpolator f5728m;

    /* renamed from: m0, reason: collision with root package name */
    private TextPaint f5729m0;

    /* renamed from: n, reason: collision with root package name */
    private int f5730n;

    /* renamed from: n0, reason: collision with root package name */
    private int f5731n0;

    /* renamed from: o, reason: collision with root package name */
    private Drawable f5732o;

    /* renamed from: o0, reason: collision with root package name */
    private float f5733o0;

    /* renamed from: p, reason: collision with root package name */
    private Drawable f5734p;

    /* renamed from: p0, reason: collision with root package name */
    private int f5735p0;

    /* renamed from: q, reason: collision with root package name */
    private boolean f5736q;

    /* renamed from: q0, reason: collision with root package name */
    private int f5737q0;

    /* renamed from: r, reason: collision with root package name */
    private boolean f5738r;

    /* renamed from: r0, reason: collision with root package name */
    private int f5739r0;

    /* renamed from: s, reason: collision with root package name */
    private boolean f5740s;

    /* renamed from: s0, reason: collision with root package name */
    private int f5741s0;

    /* renamed from: t, reason: collision with root package name */
    private k f5742t;

    /* renamed from: t0, reason: collision with root package name */
    private int f5743t0;

    /* renamed from: u, reason: collision with root package name */
    private j f5744u;

    /* renamed from: u0, reason: collision with root package name */
    private int f5745u0;

    /* renamed from: v, reason: collision with root package name */
    private int f5746v;

    /* renamed from: v0, reason: collision with root package name */
    private boolean f5747v0;

    /* renamed from: w, reason: collision with root package name */
    private Context f5748w;

    /* renamed from: w0, reason: collision with root package name */
    private boolean f5749w0;

    /* renamed from: x, reason: collision with root package name */
    private boolean f5750x;

    /* renamed from: x0, reason: collision with root package name */
    private String f5751x0;

    /* renamed from: y, reason: collision with root package name */
    private f f5752y;

    /* renamed from: y0, reason: collision with root package name */
    private int f5753y0;

    /* renamed from: z, reason: collision with root package name */
    private String f5754z;

    /* renamed from: z0, reason: collision with root package name */
    private View.OnFocusChangeListener f5755z0;

    /* loaded from: classes.dex */
    public static class COUISavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<COUISavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        String f5756e;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<COUISavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public COUISavedState createFromParcel(Parcel parcel) {
                return new COUISavedState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public COUISavedState[] newArray(int i10) {
                return new COUISavedState[i10];
            }
        }

        /* synthetic */ COUISavedState(Parcel parcel, a aVar) {
            this(parcel);
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeString(this.f5756e);
        }

        COUISavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private COUISavedState(Parcel parcel) {
            super(parcel);
            this.f5756e = parcel.readString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIEditText.this.setCompoundDrawables(null, null, null, null);
        }
    }

    /* loaded from: classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIEditText cOUIEditText = COUIEditText.this;
            cOUIEditText.setCompoundDrawables(null, null, cOUIEditText.f5732o, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {
        c() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIEditText.this.f5733o0 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            COUIEditText.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {
        d() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIEditText.this.f5731n0 = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            COUIEditText.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements ValueAnimator.AnimatorUpdateListener {
        e() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIEditText.this.f5724k.R(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* loaded from: classes.dex */
    public class f extends ExploreByTouchHelper implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        private View f5762e;

        /* renamed from: f, reason: collision with root package name */
        private Rect f5763f;

        /* renamed from: g, reason: collision with root package name */
        private Rect f5764g;

        public f(View view) {
            super(view);
            this.f5763f = null;
            this.f5764g = null;
            this.f5762e = view;
        }

        private Rect a(int i10) {
            if (i10 == 0) {
                if (this.f5763f == null) {
                    b();
                }
                return this.f5763f;
            }
            return new Rect();
        }

        private void b() {
            Rect rect = new Rect();
            this.f5763f = rect;
            rect.left = COUIEditText.this.getDeleteButtonLeft();
            this.f5763f.right = COUIEditText.this.getWidth();
            Rect rect2 = this.f5763f;
            rect2.top = 0;
            rect2.bottom = COUIEditText.this.getHeight();
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float f10, float f11) {
            if (this.f5763f == null) {
                b();
            }
            Rect rect = this.f5763f;
            return (f10 < ((float) rect.left) || f10 > ((float) rect.right) || f11 < ((float) rect.top) || f11 > ((float) rect.bottom) || !COUIEditText.this.y()) ? Integer.MIN_VALUE : 0;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> list) {
            if (COUIEditText.this.y()) {
                list.add(0);
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle) {
            if (i11 != 16) {
                return false;
            }
            if (i10 != 0 || !COUIEditText.this.y()) {
                return true;
            }
            COUIEditText.this.F();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onPopulateEventForVirtualView(int i10, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.setContentDescription(COUIEditText.this.f5754z);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (i10 == 0) {
                accessibilityNodeInfoCompat.Z(COUIEditText.this.f5754z);
                accessibilityNodeInfoCompat.V(Button.class.getName());
                accessibilityNodeInfoCompat.a(16);
            }
            accessibilityNodeInfoCompat.Q(a(i10));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class g implements TextWatcher {
        private g() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            COUIEditText cOUIEditText = COUIEditText.this;
            cOUIEditText.L(cOUIEditText.hasFocus());
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }

        /* synthetic */ g(COUIEditText cOUIEditText, a aVar) {
            this();
        }
    }

    /* loaded from: classes.dex */
    public interface h {
        void a();
    }

    /* loaded from: classes.dex */
    public interface i {
        void a(boolean z10);

        void b(boolean z10);
    }

    /* loaded from: classes.dex */
    public interface j {
        boolean a();
    }

    /* loaded from: classes.dex */
    public interface k {
        boolean a();
    }

    public COUIEditText(Context context) {
        this(context, null);
    }

    private boolean B() {
        return (getGravity() & 7) == 1;
    }

    private boolean D() {
        return getLayoutDirection() == 1;
    }

    private void E() {
        q();
        Q();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F() {
        Editable text = getText();
        text.delete(0, text.length());
    }

    private void G() {
        if (u()) {
            RectF rectF = this.Q;
            this.f5724k.m(rectF);
            p(rectF);
            ((COUICutoutDrawable) this.F).h(rectF);
        }
    }

    private void H() {
        int i10 = this.H;
        if (i10 != 1) {
            if (i10 == 2 && this.U == 0) {
                this.U = this.S.getColorForState(getDrawableState(), this.S.getDefaultColor());
                return;
            }
            return;
        }
        this.M = 0;
    }

    private void J() {
        E();
        this.f5724k.Q(getTextSize());
        int gravity = getGravity();
        this.f5724k.M((gravity & (-113)) | 48);
        this.f5724k.P(gravity);
        if (this.R == null) {
            this.R = getHintTextColors();
        }
        setHint(this.C ? null : "");
        if (TextUtils.isEmpty(this.D)) {
            CharSequence hint = getHint();
            this.B = hint;
            setTopHint(hint);
            setHint(this.C ? null : "");
        }
        this.E = true;
        N(false, true);
        if (this.C) {
            P();
        }
    }

    private void K() {
        if (isFocused()) {
            if (this.f5747v0) {
                setText(this.f5751x0);
                setSelection(this.f5753y0 >= getSelectionEnd() ? getSelectionEnd() : this.f5753y0);
            }
            this.f5747v0 = false;
            return;
        }
        if (this.f5729m0.measureText(String.valueOf(getText())) <= getWidth() || this.f5747v0) {
            return;
        }
        this.f5751x0 = String.valueOf(getText());
        this.f5747v0 = true;
        setText(TextUtils.ellipsize(getText(), this.f5729m0, getWidth(), TextUtils.TruncateAt.END));
        if (this.f5720g0) {
            setErrorState(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void L(boolean z10) {
        if (TextUtils.isEmpty(getText().toString())) {
            if (B()) {
                setPaddingRelative(0, getPaddingTop(), getPaddingEnd(), getPaddingBottom());
            }
            if (this.f5740s) {
                setCompoundDrawables(null, null, null, null);
            } else {
                post(this.C0);
            }
            this.f5740s = false;
            return;
        }
        if (z10) {
            if (this.f5732o == null || this.f5740s) {
                return;
            }
            if (B()) {
                setPaddingRelative(this.f5743t0 + getCompoundDrawablePadding(), getPaddingTop(), getPaddingEnd(), getPaddingBottom());
            }
            post(this.D0);
            this.f5740s = true;
            return;
        }
        if (this.f5740s) {
            if (B()) {
                setPaddingRelative(0, getPaddingTop(), getPaddingEnd(), getPaddingBottom());
            }
            post(this.C0);
            this.f5740s = false;
        }
    }

    private void N(boolean z10, boolean z11) {
        ColorStateList colorStateList;
        boolean isEnabled = isEnabled();
        boolean z12 = !TextUtils.isEmpty(getText());
        if (this.R != null) {
            this.R = getHintTextColors();
            COUICutoutDrawable.a aVar = this.f5724k;
            if (aVar != null) {
                aVar.L(this.S);
                this.f5724k.O(this.R);
            }
        }
        COUICutoutDrawable.a aVar2 = this.f5724k;
        if (aVar2 != null) {
            if (!isEnabled) {
                aVar2.L(ColorStateList.valueOf(this.V));
                this.f5724k.O(ColorStateList.valueOf(this.V));
            } else if (hasFocus() && (colorStateList = this.S) != null) {
                this.f5724k.L(colorStateList);
            }
        }
        if (!z12 && (!isEnabled() || !hasFocus())) {
            if ((z11 || !this.f5714a0) && C()) {
                v(z10);
            }
        } else if (z11 || this.f5714a0) {
            t(z10);
        }
        COUIErrorEditTextHelper cOUIErrorEditTextHelper = this.B0;
        if (cOUIErrorEditTextHelper != null) {
            cOUIErrorEditTextHelper.L(this.f5724k);
        }
    }

    private void O() {
        if (this.H != 1) {
            return;
        }
        if (isEnabled()) {
            if (hasFocus()) {
                if (this.f5721h0) {
                    return;
                }
                n();
                return;
            } else {
                if (this.f5721h0) {
                    m();
                    return;
                }
                return;
            }
        }
        this.f5733o0 = 0.0f;
    }

    private void P() {
        ViewCompat.A0(this, D() ? getPaddingRight() : getPaddingLeft(), getModePaddingTop(), D() ? getPaddingLeft() : getPaddingRight(), getPaddingBottom());
    }

    private void Q() {
        if (this.H == 0 || this.F == null || getRight() == 0) {
            return;
        }
        this.F.setBounds(0, getBoundsTop(), getWidth(), getHeight());
        o();
    }

    private void R() {
        int i10;
        if (this.F == null || (i10 = this.H) == 0 || i10 != 2) {
            return;
        }
        if (!isEnabled()) {
            this.P = this.V;
        } else if (hasFocus()) {
            this.P = this.U;
        } else {
            this.P = this.T;
        }
        o();
    }

    private int getBoundsTop() {
        int i10 = this.H;
        if (i10 == 1) {
            return this.f5737q0;
        }
        if (i10 == 2 || i10 == 3) {
            return (int) (this.f5724k.p() / 2.0f);
        }
        return 0;
    }

    private Drawable getBoxBackground() {
        int i10 = this.H;
        if (i10 == 1 || i10 == 2) {
            return this.F;
        }
        return null;
    }

    private float[] getCornerRadiiAsArray() {
        float f10 = this.J;
        float f11 = this.I;
        float f12 = this.L;
        float f13 = this.K;
        return new float[]{f10, f10, f11, f11, f12, f12, f13, f13};
    }

    private int getModePaddingTop() {
        int x10;
        int i10;
        int i11 = this.H;
        if (i11 == 1) {
            x10 = this.f5737q0 + ((int) this.f5724k.x());
            i10 = this.f5741s0;
        } else {
            if (i11 != 2 && i11 != 3) {
                return 0;
            }
            x10 = this.f5735p0;
            i10 = (int) (this.f5724k.p() / 2.0f);
        }
        return x10 + i10;
    }

    private void l(float f10) {
        if (this.f5724k.w() == f10) {
            return;
        }
        if (this.f5716c0 == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.f5716c0 = valueAnimator;
            valueAnimator.setInterpolator(this.f5726l);
            this.f5716c0.setDuration(200L);
            this.f5716c0.addUpdateListener(new e());
        }
        this.f5716c0.setFloatValues(this.f5724k.w(), f10);
        this.f5716c0.start();
    }

    private void m() {
        if (this.f5718e0 == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.f5718e0 = valueAnimator;
            valueAnimator.setInterpolator(this.f5728m);
            this.f5718e0.setDuration(250L);
            this.f5718e0.addUpdateListener(new d());
        }
        this.f5718e0.setIntValues(255, 0);
        this.f5718e0.start();
        this.f5721h0 = false;
    }

    private void n() {
        if (this.f5717d0 == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.f5717d0 = valueAnimator;
            valueAnimator.setInterpolator(this.f5728m);
            this.f5717d0.setDuration(250L);
            this.f5717d0.addUpdateListener(new c());
        }
        this.f5731n0 = 255;
        this.f5717d0.setFloatValues(0.0f, 1.0f);
        ValueAnimator valueAnimator2 = this.f5718e0;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            this.f5718e0.cancel();
        }
        this.f5717d0.start();
        this.f5721h0 = true;
    }

    private void o() {
        int i10;
        if (this.F == null) {
            return;
        }
        H();
        int i11 = this.M;
        if (i11 > -1 && (i10 = this.P) != 0) {
            this.F.setStroke(i11, i10);
        }
        this.F.setCornerRadii(getCornerRadiiAsArray());
        invalidate();
    }

    private void p(RectF rectF) {
        float f10 = rectF.left;
        int i10 = this.G;
        rectF.left = f10 - i10;
        rectF.top -= i10;
        rectF.right += i10;
        rectF.bottom += i10;
    }

    private void q() {
        int i10 = this.H;
        if (i10 == 0) {
            this.F = null;
            return;
        }
        if (i10 == 2 && this.C && !(this.F instanceof COUICutoutDrawable)) {
            this.F = new COUICutoutDrawable();
        } else if (this.F == null) {
            this.F = new GradientDrawable();
        }
    }

    private int r() {
        int i10 = this.H;
        if (i10 == 1) {
            if (getBoxBackground() != null) {
                return getBoxBackground().getBounds().top;
            }
            return 0;
        }
        if (i10 != 2 && i10 != 3) {
            return getPaddingTop();
        }
        if (getBoxBackground() != null) {
            return getBoxBackground().getBounds().top - getLabelMarginTop();
        }
        return 0;
    }

    private void s() {
        if (u()) {
            ((COUICutoutDrawable) this.F).e();
        }
    }

    private void setHintInternal(CharSequence charSequence) {
        if (TextUtils.equals(charSequence, this.D)) {
            return;
        }
        this.D = charSequence;
        this.f5724k.X(charSequence);
        if (!this.f5714a0) {
            G();
        }
        COUIErrorEditTextHelper cOUIErrorEditTextHelper = this.B0;
        if (cOUIErrorEditTextHelper != null) {
            cOUIErrorEditTextHelper.J(this.f5724k);
        }
    }

    private void t(boolean z10) {
        ValueAnimator valueAnimator = this.f5716c0;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.f5716c0.cancel();
        }
        if (z10 && this.f5715b0) {
            l(1.0f);
        } else {
            this.f5724k.R(1.0f);
        }
        this.f5714a0 = false;
        if (u()) {
            G();
        }
    }

    private boolean u() {
        return this.C && !TextUtils.isEmpty(this.D) && (this.F instanceof COUICutoutDrawable);
    }

    private void v(boolean z10) {
        if (this.F != null) {
            Log.d("COUIEditText", "mBoxBackground: " + this.F.getBounds());
        }
        ValueAnimator valueAnimator = this.f5716c0;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.f5716c0.cancel();
        }
        if (z10 && this.f5715b0) {
            l(0.0f);
        } else {
            this.f5724k.R(0.0f);
        }
        if (u() && ((COUICutoutDrawable) this.F).b()) {
            s();
        }
        this.f5714a0 = true;
    }

    private boolean w(Rect rect) {
        int width;
        if (D()) {
            width = (getCompoundPaddingLeft() - this.f5743t0) - getCompoundDrawablePadding();
        } else {
            width = (getWidth() - getCompoundPaddingRight()) + getCompoundDrawablePadding();
        }
        int i10 = this.f5743t0 + width;
        int height = ((((getHeight() - getCompoundPaddingTop()) - getCompoundPaddingBottom()) - this.f5743t0) / 2) + getCompoundPaddingTop();
        rect.set(width, height, i10, this.f5743t0 + height);
        return true;
    }

    private void x(Context context, AttributeSet attributeSet, int i10) {
        this.f5724k.Y(new COUILinearInterpolator());
        this.f5724k.V(new COUILinearInterpolator());
        this.f5724k.M(8388659);
        this.f5726l = new COUIMoveEaseInterpolator();
        this.f5728m = new COUIInEaseInterpolator();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIEditText, i10, R$style.Widget_COUI_EditText_HintAnim_Line);
        this.C = obtainStyledAttributes.getBoolean(R$styleable.COUIEditText_couiHintEnabled, false);
        setTopHint(obtainStyledAttributes.getText(R$styleable.COUIEditText_android_hint));
        if (this.C) {
            this.f5715b0 = obtainStyledAttributes.getBoolean(R$styleable.COUIEditText_couiHintAnimationEnabled, true);
        }
        this.f5735p0 = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIEditText_rectModePaddingTop, 0);
        float dimension = obtainStyledAttributes.getDimension(R$styleable.COUIEditText_cornerRadius, 0.0f);
        this.I = dimension;
        this.J = dimension;
        this.K = dimension;
        this.L = dimension;
        this.U = obtainStyledAttributes.getColor(R$styleable.COUIEditText_couiStrokeColor, COUIContextUtil.b(context, R$attr.couiColorPrimary, 0));
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIEditText_couiStrokeWidth, 0);
        this.M = dimensionPixelSize;
        this.O = dimensionPixelSize;
        this.f5739r0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_textinput_line_padding);
        if (this.C) {
            this.G = context.getResources().getDimensionPixelOffset(R$dimen.coui_textinput_label_cutout_padding);
            this.f5737q0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_textinput_line_padding_top);
            this.f5741s0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_textinput_line_padding_middle);
        }
        int i11 = obtainStyledAttributes.getInt(R$styleable.COUIEditText_couiBackgroundMode, 0);
        setBoxBackgroundMode(i11);
        if (this.H != 0) {
            setBackgroundDrawable(null);
        }
        int i12 = R$styleable.COUIEditText_android_textColorHint;
        if (obtainStyledAttributes.hasValue(i12)) {
            ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(i12);
            this.R = colorStateList;
            this.S = colorStateList;
        }
        this.T = obtainStyledAttributes.getColor(R$styleable.COUIEditText_couiDefaultStrokeColor, 0);
        this.V = obtainStyledAttributes.getColor(R$styleable.COUIEditText_couiDisabledStrokeColor, 0);
        String string = obtainStyledAttributes.getString(R$styleable.COUIEditText_couiEditTextNoEllipsisText);
        this.f5751x0 = string;
        setText(string);
        I(obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIEditText_collapsedTextSize, 0), obtainStyledAttributes.getColorStateList(R$styleable.COUIEditText_collapsedTextColor));
        if (i11 == 2) {
            this.f5724k.a0(Typeface.create("sans-serif-medium", 0));
        }
        obtainStyledAttributes.recycle();
        this.f5727l0 = new Paint();
        TextPaint textPaint = new TextPaint();
        this.f5729m0 = textPaint;
        textPaint.setTextSize(getTextSize());
        Paint paint = new Paint();
        this.f5723j0 = paint;
        paint.setColor(this.T);
        this.f5723j0.setStrokeWidth(this.M);
        Paint paint2 = new Paint();
        this.f5725k0 = paint2;
        paint2.setColor(this.V);
        this.f5725k0.setStrokeWidth(this.M);
        Paint paint3 = new Paint();
        this.f5722i0 = paint3;
        paint3.setColor(this.U);
        this.f5722i0.setStrokeWidth(this.N);
        J();
    }

    private boolean z(String str) {
        if (str == null) {
            return false;
        }
        return TextUtils.isEmpty(str);
    }

    public boolean A() {
        return this.f5738r;
    }

    public boolean C() {
        return this.C;
    }

    public void I(int i10, ColorStateList colorStateList) {
        this.f5724k.K(i10, colorStateList);
        this.S = this.f5724k.n();
        M(false);
        this.B0.B(i10, colorStateList);
    }

    public void M(boolean z10) {
        N(z10, false);
    }

    @Override // android.view.View
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        f fVar;
        if (y() && (fVar = this.f5752y) != null && fVar.dispatchHoverEvent(motionEvent)) {
            return true;
        }
        return super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public void dispatchStartTemporaryDetach() {
        super.dispatchStartTemporaryDetach();
        if (this.f5750x) {
            onStartTemporaryDetach();
        }
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (getMaxLines() < 2 && this.f5749w0) {
            K();
        }
        if (getHintTextColors() != this.R) {
            M(false);
        }
        int save = canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        if (!this.C && getText().length() != 0) {
            canvas.drawText(" ", 0.0f, 0.0f, this.f5727l0);
        } else {
            this.f5724k.j(canvas);
        }
        if (this.F != null && this.H == 2) {
            if (getScrollX() != 0) {
                Q();
            }
            if (!this.B0.v()) {
                this.F.draw(canvas);
            } else {
                this.B0.o(canvas, this.F, this.P);
            }
        }
        if (this.H == 1) {
            int height = getHeight() - ((int) ((this.O / 2.0d) + 0.5d));
            this.f5722i0.setAlpha(this.f5731n0);
            if (!isEnabled()) {
                float f10 = height;
                canvas.drawLine(0.0f, f10, getWidth(), f10, this.f5725k0);
            } else if (!this.B0.v()) {
                float f11 = height;
                canvas.drawLine(0.0f, f11, getWidth(), f11, this.f5723j0);
                canvas.drawLine(0.0f, f11, this.f5733o0 * getWidth(), f11, this.f5722i0);
            } else {
                this.B0.n(canvas, height, getWidth(), (int) (this.f5733o0 * getWidth()), this.f5723j0, this.f5722i0);
            }
        }
        canvas.restoreToCount(save);
        super.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0048  */
    @Override // androidx.appcompat.widget.AppCompatEditText, android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void drawableStateChanged() {
        boolean z10;
        if (this.f5719f0) {
            return;
        }
        this.f5719f0 = true;
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        if (this.C) {
            M(ViewCompat.Q(this) && isEnabled());
        } else {
            M(false);
        }
        O();
        if (this.C) {
            Q();
            R();
            COUICutoutDrawable.a aVar = this.f5724k;
            if (aVar != null) {
                z10 = aVar.W(drawableState) | false;
                this.B0.p(drawableState);
                if (z10) {
                    invalidate();
                }
                this.f5719f0 = false;
            }
        }
        z10 = false;
        if (z10) {
        }
        this.f5719f0 = false;
    }

    public Rect getBackgroundRect() {
        int i10 = this.H;
        if ((i10 == 1 || i10 == 2 || i10 == 3) && getBoxBackground() != null) {
            getBoxBackground().getBounds();
        }
        return null;
    }

    public int getBoxStrokeColor() {
        return this.U;
    }

    public String getCouiEditTexttNoEllipsisText() {
        if (this.f5747v0) {
            return this.f5751x0;
        }
        return String.valueOf(getText());
    }

    public int getDeleteButtonLeft() {
        Drawable drawable = this.f5732o;
        return ((getRight() - getLeft()) - getPaddingRight()) - (drawable != null ? drawable.getIntrinsicWidth() : 0);
    }

    public int getDeleteIconWidth() {
        return this.f5743t0;
    }

    @Override // android.widget.TextView
    public CharSequence getHint() {
        if (this.C) {
            return this.D;
        }
        return null;
    }

    public int getLabelMarginTop() {
        if (this.C) {
            return (int) (this.f5724k.p() / 2.0f);
        }
        return 0;
    }

    public void k(i iVar) {
        this.B0.l(iVar);
    }

    @Override // androidx.appcompat.widget.AppCompatEditText, android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        h hVar = this.A0;
        if (hVar != null) {
            hVar.a();
        }
        return super.onCreateInputConnection(editorInfo);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.A0 != null) {
            this.A0 = null;
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.B0.x(canvas);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onFocusChanged(boolean z10, int i10, Rect rect) {
        super.onFocusChanged(z10, i10, rect);
        if (this.f5738r) {
            L(z10);
        }
        View.OnFocusChangeListener onFocusChangeListener = this.f5755z0;
        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(this, z10);
        }
    }

    @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i10, KeyEvent keyEvent) {
        if (this.f5738r && i10 == 67) {
            super.onKeyDown(i10, keyEvent);
            j jVar = this.f5744u;
            if (jVar == null) {
                return true;
            }
            jVar.a();
            return true;
        }
        return super.onKeyDown(i10, keyEvent);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        if (this.F != null) {
            Q();
        }
        if (this.C) {
            P();
        }
        int compoundPaddingLeft = getCompoundPaddingLeft();
        int width = getWidth() - getCompoundPaddingRight();
        int r10 = r();
        this.f5724k.N(compoundPaddingLeft, getCompoundPaddingTop(), width, getHeight() - getCompoundPaddingBottom());
        this.f5724k.J(compoundPaddingLeft, r10, width, getHeight() - getCompoundPaddingBottom());
        this.f5724k.H();
        if (u() && !this.f5714a0) {
            G();
        }
        this.B0.y(this.f5724k);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
    }

    @Override // android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        String str;
        if (getMaxLines() < 2 && this.f5749w0 && (parcelable instanceof COUISavedState) && (str = ((COUISavedState) parcelable).f5756e) != null) {
            setText(str);
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override // android.widget.TextView, android.view.View
    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        if (getMaxLines() >= 2 || !this.f5749w0 || isFocused()) {
            return onSaveInstanceState;
        }
        COUISavedState cOUISavedState = new COUISavedState(onSaveInstanceState);
        cOUISavedState.f5756e = getCouiEditTexttNoEllipsisText();
        return cOUISavedState;
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.f5738r && !TextUtils.isEmpty(getText()) && hasFocus()) {
            Rect rect = new Rect();
            boolean z10 = w(rect) && rect.contains((int) motionEvent.getX(), (int) motionEvent.getY());
            if (this.f5740s && z10) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    this.f5736q = true;
                    return true;
                }
                if (action != 1) {
                    if (action == 2 && this.f5736q) {
                        return true;
                    }
                } else if (this.f5736q) {
                    k kVar = this.f5742t;
                    if (kVar != null && kVar.a()) {
                        return true;
                    }
                    F();
                    this.f5736q = false;
                    return true;
                }
            }
        }
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        this.f5753y0 = getSelectionEnd();
        return onTouchEvent;
    }

    public void setBoxBackgroundMode(int i10) {
        if (i10 == this.H) {
            return;
        }
        this.H = i10;
        E();
    }

    public void setBoxStrokeColor(int i10) {
        if (this.U != i10) {
            this.U = i10;
            this.f5722i0.setColor(i10);
            R();
        }
    }

    @Override // androidx.appcompat.widget.AppCompatEditText, android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        if (drawable3 != null) {
            this.f5746v = drawable3.getBounds().width();
        } else {
            this.f5746v = 0;
        }
    }

    public void setCouiEditTexttNoEllipsisText(String str) {
        this.f5751x0 = str;
        setText(str);
    }

    public void setDefaultStrokeColor(int i10) {
        if (this.T != i10) {
            this.T = i10;
            this.f5723j0.setColor(i10);
            R();
        }
    }

    public void setDisabledStrokeColor(int i10) {
        if (this.V != i10) {
            this.V = i10;
            this.f5725k0.setColor(i10);
            R();
        }
    }

    public void setEditFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.f5755z0 = onFocusChangeListener;
    }

    public void setEditTextColor(int i10) {
        setTextColor(i10);
        this.B0.K(getTextColors());
    }

    public void setEditTextDeleteIconNormal(Drawable drawable) {
        if (drawable != null) {
            this.f5732o = drawable;
            this.f5743t0 = drawable.getIntrinsicWidth();
            int intrinsicHeight = this.f5732o.getIntrinsicHeight();
            this.f5745u0 = intrinsicHeight;
            this.f5732o.setBounds(0, 0, this.f5743t0, intrinsicHeight);
            invalidate();
        }
    }

    public void setEditTextDeleteIconPressed(Drawable drawable) {
        if (drawable != null) {
            this.f5734p = drawable;
            drawable.setBounds(0, 0, this.f5743t0, this.f5745u0);
            invalidate();
        }
    }

    public void setEditTextErrorColor(int i10) {
        if (i10 != this.W) {
            this.W = i10;
            this.B0.C(i10);
            invalidate();
        }
    }

    public void setErrorState(boolean z10) {
        this.f5720g0 = z10;
        this.B0.D(z10);
    }

    public void setFastDeletable(boolean z10) {
        if (this.f5738r != z10) {
            this.f5738r = z10;
            if (z10) {
                if (this.A == null) {
                    g gVar = new g(this, null);
                    this.A = gVar;
                    addTextChangedListener(gVar);
                }
                setCompoundDrawablePadding(this.f5748w.getResources().getDimensionPixelSize(R$dimen.coui_edit_text_drawable_padding));
            }
        }
    }

    public void setHintEnabled(boolean z10) {
        if (z10 != this.C) {
            this.C = z10;
            if (!z10) {
                this.E = false;
                if (!TextUtils.isEmpty(this.D) && TextUtils.isEmpty(getHint())) {
                    setHint(this.D);
                }
                setHintInternal(null);
                return;
            }
            CharSequence hint = getHint();
            if (!TextUtils.isEmpty(hint)) {
                if (TextUtils.isEmpty(this.D)) {
                    setTopHint(hint);
                }
                setHint((CharSequence) null);
            }
            this.E = true;
        }
    }

    public void setInputConnectionListener(h hVar) {
        this.A0 = hVar;
    }

    public void setIsEllipsisEnabled(boolean z10) {
        this.f5749w0 = z10;
    }

    public void setOnTextDeletedListener(k kVar) {
        this.f5742t = kVar;
    }

    @Override // android.widget.EditText, android.widget.TextView
    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        super.setText(charSequence, bufferType);
        Selection.setSelection(getText(), length());
    }

    public void setTextDeletedListener(j jVar) {
        this.f5744u = jVar;
    }

    public void setTopHint(CharSequence charSequence) {
        setHintInternal(charSequence);
    }

    public void setmHintAnimationEnabled(boolean z10) {
        this.f5715b0 = z10;
    }

    public boolean y() {
        return this.f5738r && !z(getText().toString()) && hasFocus();
    }

    public COUIEditText(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.editTextStyle);
    }

    @SuppressLint({"WrongConstant"})
    public COUIEditText(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        COUICutoutDrawable.a aVar = new COUICutoutDrawable.a(this);
        this.f5724k = aVar;
        this.f5736q = false;
        this.f5738r = false;
        this.f5740s = false;
        this.f5742t = null;
        this.f5744u = null;
        this.f5750x = false;
        this.f5754z = null;
        this.A = null;
        this.M = 1;
        this.N = 1;
        this.Q = new RectF();
        this.f5747v0 = false;
        this.f5749w0 = false;
        this.f5751x0 = "";
        this.f5753y0 = 0;
        this.C0 = new a();
        this.D0 = new b();
        if (attributeSet != null) {
            this.f5730n = attributeSet.getStyleAttribute();
        }
        if (this.f5730n == 0) {
            this.f5730n = i10;
        }
        this.f5748w = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIEditText, i10, 0);
        boolean z10 = obtainStyledAttributes.getBoolean(R$styleable.COUIEditText_quickDelete, false);
        this.W = obtainStyledAttributes.getColor(R$styleable.COUIEditText_couiEditTextErrorColor, COUIContextUtil.a(context, R$attr.couiColorErrorTextBg));
        this.f5732o = obtainStyledAttributes.getDrawable(R$styleable.COUIEditText_couiEditTextDeleteIconNormal);
        this.f5734p = obtainStyledAttributes.getDrawable(R$styleable.COUIEditText_couiEditTextDeleteIconPressed);
        this.f5749w0 = obtainStyledAttributes.getBoolean(R$styleable.COUIEditText_couiEditTextIsEllipsis, true);
        int i11 = obtainStyledAttributes.getInt(R$styleable.COUIEditText_couiEditTextHintLines, 1);
        aVar.S(i11);
        obtainStyledAttributes.recycle();
        setFastDeletable(z10);
        Drawable drawable = this.f5732o;
        if (drawable != null) {
            this.f5743t0 = drawable.getIntrinsicWidth();
            int intrinsicHeight = this.f5732o.getIntrinsicHeight();
            this.f5745u0 = intrinsicHeight;
            this.f5732o.setBounds(0, 0, this.f5743t0, intrinsicHeight);
        }
        Drawable drawable2 = this.f5734p;
        if (drawable2 != null) {
            drawable2.setBounds(0, 0, this.f5743t0, this.f5745u0);
        }
        aVar.T(context.getResources().getDimensionPixelSize(R$dimen.coui_edit_text_hint_start_padding));
        f fVar = new f(this);
        this.f5752y = fVar;
        ViewCompat.l0(this, fVar);
        ViewCompat.w0(this, 1);
        this.f5754z = this.f5748w.getString(R$string.coui_slide_delete);
        this.f5752y.invalidateRoot();
        this.B0 = new COUIErrorEditTextHelper(this, i11);
        x(context, attributeSet, i10);
        this.B0.t(this.W, this.N, this.H, getCornerRadiiAsArray(), aVar);
    }
}
