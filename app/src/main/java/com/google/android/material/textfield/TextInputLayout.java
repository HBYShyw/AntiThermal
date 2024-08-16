package com.google.android.material.textfield;

import android.R;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.t;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.text.BidiFormatter;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import androidx.customview.view.AbsSavedState;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import c.AppCompatResources;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$attr;
import com.google.android.material.R$color;
import com.google.android.material.R$dimen;
import com.google.android.material.R$id;
import com.google.android.material.R$layout;
import com.google.android.material.R$string;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.internal.CollapsingTextHelper;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import d4.MaterialThemeOverlay;
import java.util.Iterator;
import java.util.LinkedHashSet;
import p3.AnimationUtils;
import r3.MaterialColors;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class TextInputLayout extends LinearLayout {
    private static final int M0 = R$style.Widget_Design_TextInputLayout;
    private Fade A;
    private int A0;
    private Fade B;
    private int B0;
    private ColorStateList C;
    private int C0;
    private ColorStateList D;
    private int D0;
    private CharSequence E;
    private int E0;
    private final TextView F;
    private boolean F0;
    private boolean G;
    final CollapsingTextHelper G0;
    private CharSequence H;
    private boolean H0;
    private boolean I;
    private boolean I0;
    private MaterialShapeDrawable J;
    private ValueAnimator J0;
    private MaterialShapeDrawable K;
    private boolean K0;
    private MaterialShapeDrawable L;
    private boolean L0;
    private ShapeAppearanceModel M;
    private boolean N;
    private final int O;
    private int P;
    private int Q;
    private int R;
    private int S;
    private int T;
    private int U;
    private int V;
    private final Rect W;

    /* renamed from: a0, reason: collision with root package name */
    private final Rect f9334a0;

    /* renamed from: b0, reason: collision with root package name */
    private final RectF f9335b0;

    /* renamed from: c0, reason: collision with root package name */
    private Typeface f9336c0;

    /* renamed from: d0, reason: collision with root package name */
    private Drawable f9337d0;

    /* renamed from: e, reason: collision with root package name */
    private final FrameLayout f9338e;

    /* renamed from: e0, reason: collision with root package name */
    private int f9339e0;

    /* renamed from: f, reason: collision with root package name */
    private final StartCompoundLayout f9340f;

    /* renamed from: f0, reason: collision with root package name */
    private final LinkedHashSet<f> f9341f0;

    /* renamed from: g, reason: collision with root package name */
    private final LinearLayout f9342g;

    /* renamed from: g0, reason: collision with root package name */
    private int f9343g0;

    /* renamed from: h, reason: collision with root package name */
    private final FrameLayout f9344h;

    /* renamed from: h0, reason: collision with root package name */
    private final SparseArray<EndIconDelegate> f9345h0;

    /* renamed from: i, reason: collision with root package name */
    EditText f9346i;

    /* renamed from: i0, reason: collision with root package name */
    private final CheckableImageButton f9347i0;

    /* renamed from: j, reason: collision with root package name */
    private CharSequence f9348j;

    /* renamed from: j0, reason: collision with root package name */
    private final LinkedHashSet<g> f9349j0;

    /* renamed from: k, reason: collision with root package name */
    private int f9350k;

    /* renamed from: k0, reason: collision with root package name */
    private ColorStateList f9351k0;

    /* renamed from: l, reason: collision with root package name */
    private int f9352l;

    /* renamed from: l0, reason: collision with root package name */
    private PorterDuff.Mode f9353l0;

    /* renamed from: m, reason: collision with root package name */
    private int f9354m;

    /* renamed from: m0, reason: collision with root package name */
    private Drawable f9355m0;

    /* renamed from: n, reason: collision with root package name */
    private int f9356n;

    /* renamed from: n0, reason: collision with root package name */
    private int f9357n0;

    /* renamed from: o, reason: collision with root package name */
    private final IndicatorViewController f9358o;

    /* renamed from: o0, reason: collision with root package name */
    private Drawable f9359o0;

    /* renamed from: p, reason: collision with root package name */
    boolean f9360p;

    /* renamed from: p0, reason: collision with root package name */
    private View.OnLongClickListener f9361p0;

    /* renamed from: q, reason: collision with root package name */
    private int f9362q;

    /* renamed from: q0, reason: collision with root package name */
    private View.OnLongClickListener f9363q0;

    /* renamed from: r, reason: collision with root package name */
    private boolean f9364r;

    /* renamed from: r0, reason: collision with root package name */
    private final CheckableImageButton f9365r0;

    /* renamed from: s, reason: collision with root package name */
    private TextView f9366s;

    /* renamed from: s0, reason: collision with root package name */
    private ColorStateList f9367s0;

    /* renamed from: t, reason: collision with root package name */
    private int f9368t;

    /* renamed from: t0, reason: collision with root package name */
    private PorterDuff.Mode f9369t0;

    /* renamed from: u, reason: collision with root package name */
    private int f9370u;

    /* renamed from: u0, reason: collision with root package name */
    private ColorStateList f9371u0;

    /* renamed from: v, reason: collision with root package name */
    private CharSequence f9372v;

    /* renamed from: v0, reason: collision with root package name */
    private ColorStateList f9373v0;

    /* renamed from: w, reason: collision with root package name */
    private boolean f9374w;

    /* renamed from: w0, reason: collision with root package name */
    private int f9375w0;

    /* renamed from: x, reason: collision with root package name */
    private TextView f9376x;

    /* renamed from: x0, reason: collision with root package name */
    private int f9377x0;

    /* renamed from: y, reason: collision with root package name */
    private ColorStateList f9378y;

    /* renamed from: y0, reason: collision with root package name */
    private int f9379y0;

    /* renamed from: z, reason: collision with root package name */
    private int f9380z;

    /* renamed from: z0, reason: collision with root package name */
    private ColorStateList f9381z0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        CharSequence f9382e;

        /* renamed from: f, reason: collision with root package name */
        boolean f9383f;

        /* renamed from: g, reason: collision with root package name */
        CharSequence f9384g;

        /* renamed from: h, reason: collision with root package name */
        CharSequence f9385h;

        /* renamed from: i, reason: collision with root package name */
        CharSequence f9386i;

        /* loaded from: classes.dex */
        class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "TextInputLayout.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " error=" + ((Object) this.f9382e) + " hint=" + ((Object) this.f9384g) + " helperText=" + ((Object) this.f9385h) + " placeholderText=" + ((Object) this.f9386i) + "}";
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            TextUtils.writeToParcel(this.f9382e, parcel, i10);
            parcel.writeInt(this.f9383f ? 1 : 0);
            TextUtils.writeToParcel(this.f9384g, parcel, i10);
            TextUtils.writeToParcel(this.f9385h, parcel, i10);
            TextUtils.writeToParcel(this.f9386i, parcel, i10);
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f9382e = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.f9383f = parcel.readInt() == 1;
            this.f9384g = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.f9385h = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.f9386i = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements TextWatcher {
        a() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            TextInputLayout.this.w0(!r0.L0);
            TextInputLayout textInputLayout = TextInputLayout.this;
            if (textInputLayout.f9360p) {
                textInputLayout.m0(editable.length());
            }
            if (TextInputLayout.this.f9374w) {
                TextInputLayout.this.A0(editable.length());
            }
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }
    }

    /* loaded from: classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            TextInputLayout.this.f9347i0.performClick();
            TextInputLayout.this.f9347i0.jumpDrawablesToCurrentState();
        }
    }

    /* loaded from: classes.dex */
    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            TextInputLayout.this.f9346i.requestLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {
        d() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            TextInputLayout.this.G0.setExpansionFraction(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* loaded from: classes.dex */
    public static class e extends AccessibilityDelegateCompat {

        /* renamed from: a, reason: collision with root package name */
        private final TextInputLayout f9391a;

        public e(TextInputLayout textInputLayout) {
            this.f9391a = textInputLayout;
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            EditText editText = this.f9391a.getEditText();
            CharSequence text = editText != null ? editText.getText() : null;
            CharSequence hint = this.f9391a.getHint();
            CharSequence error = this.f9391a.getError();
            CharSequence placeholderText = this.f9391a.getPlaceholderText();
            int counterMaxLength = this.f9391a.getCounterMaxLength();
            CharSequence counterOverflowDescription = this.f9391a.getCounterOverflowDescription();
            boolean z10 = !TextUtils.isEmpty(text);
            boolean z11 = !TextUtils.isEmpty(hint);
            boolean z12 = !this.f9391a.N();
            boolean z13 = !TextUtils.isEmpty(error);
            boolean z14 = z13 || !TextUtils.isEmpty(counterOverflowDescription);
            String charSequence = z11 ? hint.toString() : "";
            this.f9391a.f9340f.v(accessibilityNodeInfoCompat);
            if (z10) {
                accessibilityNodeInfoCompat.y0(text);
            } else if (!TextUtils.isEmpty(charSequence)) {
                accessibilityNodeInfoCompat.y0(charSequence);
                if (z12 && placeholderText != null) {
                    accessibilityNodeInfoCompat.y0(charSequence + ", " + ((Object) placeholderText));
                }
            } else if (placeholderText != null) {
                accessibilityNodeInfoCompat.y0(placeholderText);
            }
            if (!TextUtils.isEmpty(charSequence)) {
                accessibilityNodeInfoCompat.g0(charSequence);
                accessibilityNodeInfoCompat.u0(!z10);
            }
            if (text == null || text.length() != counterMaxLength) {
                counterMaxLength = -1;
            }
            accessibilityNodeInfoCompat.j0(counterMaxLength);
            if (z14) {
                if (!z13) {
                    error = counterOverflowDescription;
                }
                accessibilityNodeInfoCompat.c0(error);
            }
            View s7 = this.f9391a.f9358o.s();
            if (s7 != null) {
                accessibilityNodeInfoCompat.h0(s7);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface f {
        void a(TextInputLayout textInputLayout);
    }

    /* loaded from: classes.dex */
    public interface g {
        void a(TextInputLayout textInputLayout, int i10);
    }

    public TextInputLayout(Context context) {
        this(context, null);
    }

    private boolean A() {
        return this.G && !TextUtils.isEmpty(this.H) && (this.J instanceof CutoutDrawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void A0(int i10) {
        if (i10 == 0 && !this.F0) {
            h0();
        } else {
            J();
        }
    }

    private void B() {
        Iterator<f> it = this.f9341f0.iterator();
        while (it.hasNext()) {
            it.next().a(this);
        }
    }

    private void B0(boolean z10, boolean z11) {
        int defaultColor = this.f9381z0.getDefaultColor();
        int colorForState = this.f9381z0.getColorForState(new int[]{R.attr.state_hovered, R.attr.state_enabled}, defaultColor);
        int colorForState2 = this.f9381z0.getColorForState(new int[]{R.attr.state_activated, R.attr.state_enabled}, defaultColor);
        if (z10) {
            this.U = colorForState2;
        } else if (z11) {
            this.U = colorForState;
        } else {
            this.U = defaultColor;
        }
    }

    private void C(int i10) {
        Iterator<g> it = this.f9349j0.iterator();
        while (it.hasNext()) {
            it.next().a(this, i10);
        }
    }

    private void C0() {
        if (this.f9346i == null) {
            return;
        }
        ViewCompat.A0(this.F, getContext().getResources().getDimensionPixelSize(R$dimen.material_input_text_to_prefix_suffix_padding), this.f9346i.getPaddingTop(), (K() || L()) ? 0 : ViewCompat.B(this.f9346i), this.f9346i.getPaddingBottom());
    }

    private void D(Canvas canvas) {
        MaterialShapeDrawable materialShapeDrawable;
        if (this.L == null || (materialShapeDrawable = this.K) == null) {
            return;
        }
        materialShapeDrawable.draw(canvas);
        if (this.f9346i.isFocused()) {
            Rect bounds = this.L.getBounds();
            Rect bounds2 = this.K.getBounds();
            float expansionFraction = this.G0.getExpansionFraction();
            int centerX = bounds2.centerX();
            bounds.left = AnimationUtils.c(centerX, bounds2.left, expansionFraction);
            bounds.right = AnimationUtils.c(centerX, bounds2.right, expansionFraction);
            this.L.draw(canvas);
        }
    }

    private void D0() {
        int visibility = this.F.getVisibility();
        int i10 = (this.E == null || N()) ? 8 : 0;
        if (visibility != i10) {
            getEndIconDelegate().c(i10 == 0);
        }
        t0();
        this.F.setVisibility(i10);
        q0();
    }

    private void E(Canvas canvas) {
        if (this.G) {
            this.G0.draw(canvas);
        }
    }

    private void F(boolean z10) {
        ValueAnimator valueAnimator = this.J0;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.J0.cancel();
        }
        if (z10 && this.I0) {
            k(0.0f);
        } else {
            this.G0.setExpansionFraction(0.0f);
        }
        if (A() && ((CutoutDrawable) this.J).r0()) {
            x();
        }
        this.F0 = true;
        J();
        this.f9340f.i(true);
        D0();
    }

    private int G(int i10, boolean z10) {
        int compoundPaddingLeft = i10 + this.f9346i.getCompoundPaddingLeft();
        return (getPrefixText() == null || z10) ? compoundPaddingLeft : (compoundPaddingLeft - getPrefixTextView().getMeasuredWidth()) + getPrefixTextView().getPaddingLeft();
    }

    private int H(int i10, boolean z10) {
        int compoundPaddingRight = i10 - this.f9346i.getCompoundPaddingRight();
        return (getPrefixText() == null || !z10) ? compoundPaddingRight : compoundPaddingRight + (getPrefixTextView().getMeasuredWidth() - getPrefixTextView().getPaddingRight());
    }

    private boolean I() {
        return this.f9343g0 != 0;
    }

    private void J() {
        TextView textView = this.f9376x;
        if (textView == null || !this.f9374w) {
            return;
        }
        textView.setText((CharSequence) null);
        TransitionManager.a(this.f9338e, this.B);
        this.f9376x.setVisibility(4);
    }

    private boolean L() {
        return this.f9365r0.getVisibility() == 0;
    }

    private boolean P() {
        return this.P == 1 && this.f9346i.getMinLines() <= 1;
    }

    private void Q() {
        o();
        Z();
        E0();
        j0();
        j();
        if (this.P != 0) {
            v0();
        }
    }

    private void R() {
        if (A()) {
            RectF rectF = this.f9335b0;
            this.G0.getCollapsedTextActualBounds(rectF, this.f9346i.getWidth(), this.f9346i.getGravity());
            n(rectF);
            rectF.offset(-getPaddingLeft(), ((-getPaddingTop()) - (rectF.height() / 2.0f)) + this.R);
            ((CutoutDrawable) this.J).u0(rectF);
        }
    }

    private void S() {
        if (!A() || this.F0) {
            return;
        }
        x();
        R();
    }

    private static void T(ViewGroup viewGroup, boolean z10) {
        int childCount = viewGroup.getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = viewGroup.getChildAt(i10);
            childAt.setEnabled(z10);
            if (childAt instanceof ViewGroup) {
                T((ViewGroup) childAt, z10);
            }
        }
    }

    private void X() {
        TextView textView = this.f9376x;
        if (textView != null) {
            textView.setVisibility(8);
        }
    }

    private void Z() {
        if (g0()) {
            ViewCompat.p0(this.f9346i, this.J);
        }
    }

    private static void a0(CheckableImageButton checkableImageButton, View.OnLongClickListener onLongClickListener) {
        boolean L = ViewCompat.L(checkableImageButton);
        boolean z10 = onLongClickListener != null;
        boolean z11 = L || z10;
        checkableImageButton.setFocusable(z11);
        checkableImageButton.setClickable(L);
        checkableImageButton.setPressable(L);
        checkableImageButton.setLongClickable(z10);
        ViewCompat.w0(checkableImageButton, z11 ? 1 : 2);
    }

    private static void b0(CheckableImageButton checkableImageButton, View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        checkableImageButton.setOnClickListener(onClickListener);
        a0(checkableImageButton, onLongClickListener);
    }

    private static void c0(CheckableImageButton checkableImageButton, View.OnLongClickListener onLongClickListener) {
        checkableImageButton.setOnLongClickListener(onLongClickListener);
        a0(checkableImageButton, onLongClickListener);
    }

    private boolean e0() {
        return (this.f9365r0.getVisibility() == 0 || ((I() && K()) || this.E != null)) && this.f9342g.getMeasuredWidth() > 0;
    }

    private boolean f0() {
        return (getStartIconDrawable() != null || (getPrefixText() != null && getPrefixTextView().getVisibility() == 0)) && this.f9340f.getMeasuredWidth() > 0;
    }

    private boolean g0() {
        EditText editText = this.f9346i;
        return (editText == null || this.J == null || editText.getBackground() != null || this.P == 0) ? false : true;
    }

    private EndIconDelegate getEndIconDelegate() {
        EndIconDelegate endIconDelegate = this.f9345h0.get(this.f9343g0);
        return endIconDelegate != null ? endIconDelegate : this.f9345h0.get(0);
    }

    private CheckableImageButton getEndIconToUpdateDummyDrawable() {
        if (this.f9365r0.getVisibility() == 0) {
            return this.f9365r0;
        }
        if (I() && K()) {
            return this.f9347i0;
        }
        return null;
    }

    private void h0() {
        if (this.f9376x == null || !this.f9374w || TextUtils.isEmpty(this.f9372v)) {
            return;
        }
        this.f9376x.setText(this.f9372v);
        TransitionManager.a(this.f9338e, this.A);
        this.f9376x.setVisibility(0);
        this.f9376x.bringToFront();
        announceForAccessibility(this.f9372v);
    }

    private void i() {
        TextView textView = this.f9376x;
        if (textView != null) {
            this.f9338e.addView(textView);
            this.f9376x.setVisibility(0);
        }
    }

    private void i0(boolean z10) {
        if (z10 && getEndIconDrawable() != null) {
            Drawable mutate = DrawableCompat.l(getEndIconDrawable()).mutate();
            DrawableCompat.h(mutate, this.f9358o.p());
            this.f9347i0.setImageDrawable(mutate);
            return;
        }
        IconHelper.a(this, this.f9347i0, this.f9351k0, this.f9353l0);
    }

    private void j() {
        if (this.f9346i == null || this.P != 1) {
            return;
        }
        if (MaterialResources.j(getContext())) {
            EditText editText = this.f9346i;
            ViewCompat.A0(editText, ViewCompat.C(editText), getResources().getDimensionPixelSize(R$dimen.material_filled_edittext_font_2_0_padding_top), ViewCompat.B(this.f9346i), getResources().getDimensionPixelSize(R$dimen.material_filled_edittext_font_2_0_padding_bottom));
        } else if (MaterialResources.i(getContext())) {
            EditText editText2 = this.f9346i;
            ViewCompat.A0(editText2, ViewCompat.C(editText2), getResources().getDimensionPixelSize(R$dimen.material_filled_edittext_font_1_3_padding_top), ViewCompat.B(this.f9346i), getResources().getDimensionPixelSize(R$dimen.material_filled_edittext_font_1_3_padding_bottom));
        }
    }

    private void j0() {
        if (this.P == 1) {
            if (MaterialResources.j(getContext())) {
                this.Q = getResources().getDimensionPixelSize(R$dimen.material_font_2_0_box_collapsed_padding_top);
            } else if (MaterialResources.i(getContext())) {
                this.Q = getResources().getDimensionPixelSize(R$dimen.material_font_1_3_box_collapsed_padding_top);
            }
        }
    }

    private void k0(Rect rect) {
        MaterialShapeDrawable materialShapeDrawable = this.K;
        if (materialShapeDrawable != null) {
            int i10 = rect.bottom;
            materialShapeDrawable.setBounds(rect.left, i10 - this.S, rect.right, i10);
        }
        MaterialShapeDrawable materialShapeDrawable2 = this.L;
        if (materialShapeDrawable2 != null) {
            int i11 = rect.bottom;
            materialShapeDrawable2.setBounds(rect.left, i11 - this.T, rect.right, i11);
        }
    }

    private void l() {
        MaterialShapeDrawable materialShapeDrawable = this.J;
        if (materialShapeDrawable == null) {
            return;
        }
        ShapeAppearanceModel D = materialShapeDrawable.D();
        ShapeAppearanceModel shapeAppearanceModel = this.M;
        if (D != shapeAppearanceModel) {
            this.J.setShapeAppearanceModel(shapeAppearanceModel);
            p0();
        }
        if (v()) {
            this.J.k0(this.R, this.U);
        }
        int p10 = p();
        this.V = p10;
        this.J.a0(ColorStateList.valueOf(p10));
        if (this.f9343g0 == 3) {
            this.f9346i.getBackground().invalidateSelf();
        }
        m();
        invalidate();
    }

    private void l0() {
        if (this.f9366s != null) {
            EditText editText = this.f9346i;
            m0(editText == null ? 0 : editText.getText().length());
        }
    }

    private void m() {
        ColorStateList valueOf;
        if (this.K == null || this.L == null) {
            return;
        }
        if (w()) {
            MaterialShapeDrawable materialShapeDrawable = this.K;
            if (this.f9346i.isFocused()) {
                valueOf = ColorStateList.valueOf(this.f9375w0);
            } else {
                valueOf = ColorStateList.valueOf(this.U);
            }
            materialShapeDrawable.a0(valueOf);
            this.L.a0(ColorStateList.valueOf(this.U));
        }
        invalidate();
    }

    private void n(RectF rectF) {
        float f10 = rectF.left;
        int i10 = this.O;
        rectF.left = f10 - i10;
        rectF.right += i10;
    }

    private static void n0(Context context, TextView textView, int i10, int i11, boolean z10) {
        int i12;
        if (z10) {
            i12 = R$string.character_counter_overflowed_content_description;
        } else {
            i12 = R$string.character_counter_content_description;
        }
        textView.setContentDescription(context.getString(i12, Integer.valueOf(i10), Integer.valueOf(i11)));
    }

    private void o() {
        int i10 = this.P;
        if (i10 == 0) {
            this.J = null;
            this.K = null;
            this.L = null;
            return;
        }
        if (i10 == 1) {
            this.J = new MaterialShapeDrawable(this.M);
            this.K = new MaterialShapeDrawable();
            this.L = new MaterialShapeDrawable();
        } else {
            if (i10 == 2) {
                if (this.G && !(this.J instanceof CutoutDrawable)) {
                    this.J = new CutoutDrawable(this.M);
                } else {
                    this.J = new MaterialShapeDrawable(this.M);
                }
                this.K = null;
                this.L = null;
                return;
            }
            throw new IllegalArgumentException(this.P + " is illegal; only @BoxBackgroundMode constants are supported.");
        }
    }

    private void o0() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        TextView textView = this.f9366s;
        if (textView != null) {
            d0(textView, this.f9364r ? this.f9368t : this.f9370u);
            if (!this.f9364r && (colorStateList2 = this.C) != null) {
                this.f9366s.setTextColor(colorStateList2);
            }
            if (!this.f9364r || (colorStateList = this.D) == null) {
                return;
            }
            this.f9366s.setTextColor(colorStateList);
        }
    }

    private int p() {
        return this.P == 1 ? MaterialColors.g(MaterialColors.e(this, R$attr.colorSurface, 0), this.V) : this.V;
    }

    private void p0() {
        if (this.f9343g0 == 3 && this.P == 2) {
            ((DropdownMenuEndIconDelegate) this.f9345h0.get(3)).O((AutoCompleteTextView) this.f9346i);
        }
    }

    private Rect q(Rect rect) {
        if (this.f9346i != null) {
            Rect rect2 = this.f9334a0;
            boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
            rect2.bottom = rect.bottom;
            int i10 = this.P;
            if (i10 == 1) {
                rect2.left = G(rect.left, isLayoutRtl);
                rect2.top = rect.top + this.Q;
                rect2.right = H(rect.right, isLayoutRtl);
                return rect2;
            }
            if (i10 != 2) {
                rect2.left = G(rect.left, isLayoutRtl);
                rect2.top = getPaddingTop();
                rect2.right = H(rect.right, isLayoutRtl);
                return rect2;
            }
            rect2.left = rect.left + this.f9346i.getPaddingLeft();
            rect2.top = rect.top - u();
            rect2.right = rect.right - this.f9346i.getPaddingRight();
            return rect2;
        }
        throw new IllegalStateException();
    }

    private int r(Rect rect, Rect rect2, float f10) {
        if (P()) {
            return (int) (rect2.top + f10);
        }
        return rect.bottom - this.f9346i.getCompoundPaddingBottom();
    }

    private int s(Rect rect, float f10) {
        if (P()) {
            return (int) (rect.centerY() - (f10 / 2.0f));
        }
        return rect.top + this.f9346i.getCompoundPaddingTop();
    }

    private boolean s0() {
        int max;
        if (this.f9346i == null || this.f9346i.getMeasuredHeight() >= (max = Math.max(this.f9342g.getMeasuredHeight(), this.f9340f.getMeasuredHeight()))) {
            return false;
        }
        this.f9346i.setMinimumHeight(max);
        return true;
    }

    private void setEditText(EditText editText) {
        if (this.f9346i == null) {
            if (this.f9343g0 != 3 && !(editText instanceof TextInputEditText)) {
                Log.i("TextInputLayout", "EditText added is not a TextInputEditText. Please switch to using that class instead.");
            }
            this.f9346i = editText;
            int i10 = this.f9350k;
            if (i10 != -1) {
                setMinEms(i10);
            } else {
                setMinWidth(this.f9354m);
            }
            int i11 = this.f9352l;
            if (i11 != -1) {
                setMaxEms(i11);
            } else {
                setMaxWidth(this.f9356n);
            }
            Q();
            setTextInputAccessibilityDelegate(new e(this));
            this.G0.setTypefaces(this.f9346i.getTypeface());
            this.G0.setExpandedTextSize(this.f9346i.getTextSize());
            this.G0.setExpandedLetterSpacing(this.f9346i.getLetterSpacing());
            int gravity = this.f9346i.getGravity();
            this.G0.setCollapsedTextGravity((gravity & (-113)) | 48);
            this.G0.setExpandedTextGravity(gravity);
            this.f9346i.addTextChangedListener(new a());
            if (this.f9371u0 == null) {
                this.f9371u0 = this.f9346i.getHintTextColors();
            }
            if (this.G) {
                if (TextUtils.isEmpty(this.H)) {
                    CharSequence hint = this.f9346i.getHint();
                    this.f9348j = hint;
                    setHint(hint);
                    this.f9346i.setHint((CharSequence) null);
                }
                this.I = true;
            }
            if (this.f9366s != null) {
                m0(this.f9346i.getText().length());
            }
            r0();
            this.f9358o.f();
            this.f9340f.bringToFront();
            this.f9342g.bringToFront();
            this.f9344h.bringToFront();
            this.f9365r0.bringToFront();
            B();
            C0();
            if (!isEnabled()) {
                editText.setEnabled(false);
            }
            x0(false, true);
            return;
        }
        throw new IllegalArgumentException("We already have an EditText, can only have one");
    }

    private void setHintInternal(CharSequence charSequence) {
        if (TextUtils.equals(charSequence, this.H)) {
            return;
        }
        this.H = charSequence;
        this.G0.setText(charSequence);
        if (this.F0) {
            return;
        }
        R();
    }

    private void setPlaceholderTextEnabled(boolean z10) {
        if (this.f9374w == z10) {
            return;
        }
        if (z10) {
            i();
        } else {
            X();
            this.f9376x = null;
        }
        this.f9374w = z10;
    }

    private Rect t(Rect rect) {
        if (this.f9346i != null) {
            Rect rect2 = this.f9334a0;
            float expandedTextHeight = this.G0.getExpandedTextHeight();
            rect2.left = rect.left + this.f9346i.getCompoundPaddingLeft();
            rect2.top = s(rect, expandedTextHeight);
            rect2.right = rect.right - this.f9346i.getCompoundPaddingRight();
            rect2.bottom = r(rect, rect2, expandedTextHeight);
            return rect2;
        }
        throw new IllegalStateException();
    }

    private void t0() {
        this.f9344h.setVisibility((this.f9347i0.getVisibility() != 0 || L()) ? 8 : 0);
        this.f9342g.setVisibility(K() || L() || !((this.E == null || N()) ? 8 : false) ? 0 : 8);
    }

    private int u() {
        float collapsedTextHeight;
        if (!this.G) {
            return 0;
        }
        int i10 = this.P;
        if (i10 == 0) {
            collapsedTextHeight = this.G0.getCollapsedTextHeight();
        } else {
            if (i10 != 2) {
                return 0;
            }
            collapsedTextHeight = this.G0.getCollapsedTextHeight() / 2.0f;
        }
        return (int) collapsedTextHeight;
    }

    private void u0() {
        this.f9365r0.setVisibility(getErrorIconDrawable() != null && this.f9358o.z() && this.f9358o.l() ? 0 : 8);
        t0();
        C0();
        if (I()) {
            return;
        }
        q0();
    }

    private boolean v() {
        return this.P == 2 && w();
    }

    private void v0() {
        if (this.P != 1) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f9338e.getLayoutParams();
            int u7 = u();
            if (u7 != layoutParams.topMargin) {
                layoutParams.topMargin = u7;
                this.f9338e.requestLayout();
            }
        }
    }

    private boolean w() {
        return this.R > -1 && this.U != 0;
    }

    private void x() {
        if (A()) {
            ((CutoutDrawable) this.J).s0();
        }
    }

    private void x0(boolean z10, boolean z11) {
        ColorStateList colorStateList;
        TextView textView;
        int i10;
        boolean isEnabled = isEnabled();
        EditText editText = this.f9346i;
        boolean z12 = (editText == null || TextUtils.isEmpty(editText.getText())) ? false : true;
        EditText editText2 = this.f9346i;
        boolean z13 = editText2 != null && editText2.hasFocus();
        boolean l10 = this.f9358o.l();
        ColorStateList colorStateList2 = this.f9371u0;
        if (colorStateList2 != null) {
            this.G0.setCollapsedTextColor(colorStateList2);
            this.G0.setExpandedTextColor(this.f9371u0);
        }
        if (!isEnabled) {
            ColorStateList colorStateList3 = this.f9371u0;
            if (colorStateList3 != null) {
                i10 = colorStateList3.getColorForState(new int[]{-16842910}, this.E0);
            } else {
                i10 = this.E0;
            }
            this.G0.setCollapsedTextColor(ColorStateList.valueOf(i10));
            this.G0.setExpandedTextColor(ColorStateList.valueOf(i10));
        } else if (l10) {
            this.G0.setCollapsedTextColor(this.f9358o.q());
        } else if (this.f9364r && (textView = this.f9366s) != null) {
            this.G0.setCollapsedTextColor(textView.getTextColors());
        } else if (z13 && (colorStateList = this.f9373v0) != null) {
            this.G0.setCollapsedTextColor(colorStateList);
        }
        if (!z12 && this.H0 && (!isEnabled() || !z13)) {
            if (z11 || !this.F0) {
                F(z10);
                return;
            }
            return;
        }
        if (z11 || this.F0) {
            y(z10);
        }
    }

    private void y(boolean z10) {
        ValueAnimator valueAnimator = this.J0;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.J0.cancel();
        }
        if (z10 && this.I0) {
            k(1.0f);
        } else {
            this.G0.setExpansionFraction(1.0f);
        }
        this.F0 = false;
        if (A()) {
            R();
        }
        z0();
        this.f9340f.i(false);
        D0();
    }

    private void y0() {
        EditText editText;
        if (this.f9376x == null || (editText = this.f9346i) == null) {
            return;
        }
        this.f9376x.setGravity(editText.getGravity());
        this.f9376x.setPadding(this.f9346i.getCompoundPaddingLeft(), this.f9346i.getCompoundPaddingTop(), this.f9346i.getCompoundPaddingRight(), this.f9346i.getCompoundPaddingBottom());
    }

    private Fade z() {
        Fade fade = new Fade();
        fade.setDuration(87L);
        fade.setInterpolator(AnimationUtils.f16555a);
        return fade;
    }

    private void z0() {
        EditText editText = this.f9346i;
        A0(editText == null ? 0 : editText.getText().length());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void E0() {
        TextView textView;
        EditText editText;
        EditText editText2;
        if (this.J == null || this.P == 0) {
            return;
        }
        boolean z10 = false;
        boolean z11 = isFocused() || ((editText2 = this.f9346i) != null && editText2.hasFocus());
        if (isHovered() || ((editText = this.f9346i) != null && editText.isHovered())) {
            z10 = true;
        }
        if (!isEnabled()) {
            this.U = this.E0;
        } else if (this.f9358o.l()) {
            if (this.f9381z0 != null) {
                B0(z11, z10);
            } else {
                this.U = this.f9358o.p();
            }
        } else if (!this.f9364r || (textView = this.f9366s) == null) {
            if (z11) {
                this.U = this.f9379y0;
            } else if (z10) {
                this.U = this.f9377x0;
            } else {
                this.U = this.f9375w0;
            }
        } else if (this.f9381z0 != null) {
            B0(z11, z10);
        } else {
            this.U = textView.getCurrentTextColor();
        }
        u0();
        V();
        W();
        U();
        if (getEndIconDelegate().d()) {
            i0(this.f9358o.l());
        }
        if (this.P == 2) {
            int i10 = this.R;
            if (z11 && isEnabled()) {
                this.R = this.T;
            } else {
                this.R = this.S;
            }
            if (this.R != i10) {
                S();
            }
        }
        if (this.P == 1) {
            if (!isEnabled()) {
                this.V = this.B0;
            } else if (z10 && !z11) {
                this.V = this.D0;
            } else if (z11) {
                this.V = this.C0;
            } else {
                this.V = this.A0;
            }
        }
        l();
    }

    public boolean K() {
        return this.f9344h.getVisibility() == 0 && this.f9347i0.getVisibility() == 0;
    }

    public boolean M() {
        return this.f9358o.A();
    }

    final boolean N() {
        return this.F0;
    }

    public boolean O() {
        return this.I;
    }

    public void U() {
        IconHelper.c(this, this.f9347i0, this.f9351k0);
    }

    public void V() {
        IconHelper.c(this, this.f9365r0, this.f9367s0);
    }

    public void W() {
        this.f9340f.j();
    }

    public void Y(float f10, float f11, float f12, float f13) {
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        this.N = isLayoutRtl;
        float f14 = isLayoutRtl ? f11 : f10;
        if (!isLayoutRtl) {
            f10 = f11;
        }
        float f15 = isLayoutRtl ? f13 : f12;
        if (!isLayoutRtl) {
            f12 = f13;
        }
        MaterialShapeDrawable materialShapeDrawable = this.J;
        if (materialShapeDrawable != null && materialShapeDrawable.I() == f14 && this.J.J() == f10 && this.J.r() == f15 && this.J.s() == f12) {
            return;
        }
        this.M = this.M.v().J(f14).O(f10).w(f15).B(f12).m();
        l();
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i10, ViewGroup.LayoutParams layoutParams) {
        if (view instanceof EditText) {
            FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(layoutParams);
            layoutParams2.gravity = (layoutParams2.gravity & (-113)) | 16;
            this.f9338e.addView(view, layoutParams2);
            this.f9338e.setLayoutParams(layoutParams);
            v0();
            setEditText((EditText) view);
            return;
        }
        super.addView(view, i10, layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d0(TextView textView, int i10) {
        boolean z10 = true;
        try {
            TextViewCompat.n(textView, i10);
            if (textView.getTextColors().getDefaultColor() != -65281) {
                z10 = false;
            }
        } catch (Exception unused) {
        }
        if (z10) {
            TextViewCompat.n(textView, R$style.TextAppearance_AppCompat_Caption);
            textView.setTextColor(ContextCompat.c(getContext(), R$color.design_error));
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    @TargetApi(26)
    public void dispatchProvideAutofillStructure(ViewStructure viewStructure, int i10) {
        EditText editText = this.f9346i;
        if (editText == null) {
            super.dispatchProvideAutofillStructure(viewStructure, i10);
            return;
        }
        if (this.f9348j != null) {
            boolean z10 = this.I;
            this.I = false;
            CharSequence hint = editText.getHint();
            this.f9346i.setHint(this.f9348j);
            try {
                super.dispatchProvideAutofillStructure(viewStructure, i10);
                return;
            } finally {
                this.f9346i.setHint(hint);
                this.I = z10;
            }
        }
        viewStructure.setAutofillId(getAutofillId());
        onProvideAutofillStructure(viewStructure, i10);
        onProvideAutofillVirtualStructure(viewStructure, i10);
        viewStructure.setChildCount(this.f9338e.getChildCount());
        for (int i11 = 0; i11 < this.f9338e.getChildCount(); i11++) {
            View childAt = this.f9338e.getChildAt(i11);
            ViewStructure newChild = viewStructure.newChild(i11);
            childAt.dispatchProvideAutofillStructure(newChild, i10);
            if (childAt == this.f9346i) {
                newChild.setHint(getHint());
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        this.L0 = true;
        super.dispatchRestoreInstanceState(sparseArray);
        this.L0 = false;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        E(canvas);
        D(canvas);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        if (this.K0) {
            return;
        }
        this.K0 = true;
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        CollapsingTextHelper collapsingTextHelper = this.G0;
        boolean state = collapsingTextHelper != null ? collapsingTextHelper.setState(drawableState) | false : false;
        if (this.f9346i != null) {
            w0(ViewCompat.Q(this) && isEnabled());
        }
        r0();
        E0();
        if (state) {
            invalidate();
        }
        this.K0 = false;
    }

    public void g(f fVar) {
        this.f9341f0.add(fVar);
        if (this.f9346i != null) {
            fVar.a(this);
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    public int getBaseline() {
        EditText editText = this.f9346i;
        if (editText != null) {
            return editText.getBaseline() + getPaddingTop() + u();
        }
        return super.getBaseline();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MaterialShapeDrawable getBoxBackground() {
        int i10 = this.P;
        if (i10 != 1 && i10 != 2) {
            throw new IllegalStateException();
        }
        return this.J;
    }

    public int getBoxBackgroundColor() {
        return this.V;
    }

    public int getBoxBackgroundMode() {
        return this.P;
    }

    public int getBoxCollapsedPaddingTop() {
        return this.Q;
    }

    public float getBoxCornerRadiusBottomEnd() {
        if (ViewUtils.isLayoutRtl(this)) {
            return this.M.j().a(this.f9335b0);
        }
        return this.M.l().a(this.f9335b0);
    }

    public float getBoxCornerRadiusBottomStart() {
        if (ViewUtils.isLayoutRtl(this)) {
            return this.M.l().a(this.f9335b0);
        }
        return this.M.j().a(this.f9335b0);
    }

    public float getBoxCornerRadiusTopEnd() {
        if (ViewUtils.isLayoutRtl(this)) {
            return this.M.r().a(this.f9335b0);
        }
        return this.M.t().a(this.f9335b0);
    }

    public float getBoxCornerRadiusTopStart() {
        if (ViewUtils.isLayoutRtl(this)) {
            return this.M.t().a(this.f9335b0);
        }
        return this.M.r().a(this.f9335b0);
    }

    public int getBoxStrokeColor() {
        return this.f9379y0;
    }

    public ColorStateList getBoxStrokeErrorColor() {
        return this.f9381z0;
    }

    public int getBoxStrokeWidth() {
        return this.S;
    }

    public int getBoxStrokeWidthFocused() {
        return this.T;
    }

    public int getCounterMaxLength() {
        return this.f9362q;
    }

    CharSequence getCounterOverflowDescription() {
        TextView textView;
        if (this.f9360p && this.f9364r && (textView = this.f9366s) != null) {
            return textView.getContentDescription();
        }
        return null;
    }

    public ColorStateList getCounterOverflowTextColor() {
        return this.C;
    }

    public ColorStateList getCounterTextColor() {
        return this.C;
    }

    public ColorStateList getDefaultHintTextColor() {
        return this.f9371u0;
    }

    public EditText getEditText() {
        return this.f9346i;
    }

    public CharSequence getEndIconContentDescription() {
        return this.f9347i0.getContentDescription();
    }

    public Drawable getEndIconDrawable() {
        return this.f9347i0.getDrawable();
    }

    public int getEndIconMode() {
        return this.f9343g0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CheckableImageButton getEndIconView() {
        return this.f9347i0;
    }

    public CharSequence getError() {
        if (this.f9358o.z()) {
            return this.f9358o.o();
        }
        return null;
    }

    public CharSequence getErrorContentDescription() {
        return this.f9358o.n();
    }

    public int getErrorCurrentTextColors() {
        return this.f9358o.p();
    }

    public Drawable getErrorIconDrawable() {
        return this.f9365r0.getDrawable();
    }

    final int getErrorTextCurrentColor() {
        return this.f9358o.p();
    }

    public CharSequence getHelperText() {
        if (this.f9358o.A()) {
            return this.f9358o.r();
        }
        return null;
    }

    public int getHelperTextCurrentTextColor() {
        return this.f9358o.t();
    }

    public CharSequence getHint() {
        if (this.G) {
            return this.H;
        }
        return null;
    }

    final float getHintCollapsedTextHeight() {
        return this.G0.getCollapsedTextHeight();
    }

    final int getHintCurrentCollapsedTextColor() {
        return this.G0.getCurrentCollapsedTextColor();
    }

    public ColorStateList getHintTextColor() {
        return this.f9373v0;
    }

    public int getMaxEms() {
        return this.f9352l;
    }

    public int getMaxWidth() {
        return this.f9356n;
    }

    public int getMinEms() {
        return this.f9350k;
    }

    public int getMinWidth() {
        return this.f9354m;
    }

    @Deprecated
    public CharSequence getPasswordVisibilityToggleContentDescription() {
        return this.f9347i0.getContentDescription();
    }

    @Deprecated
    public Drawable getPasswordVisibilityToggleDrawable() {
        return this.f9347i0.getDrawable();
    }

    public CharSequence getPlaceholderText() {
        if (this.f9374w) {
            return this.f9372v;
        }
        return null;
    }

    public int getPlaceholderTextAppearance() {
        return this.f9380z;
    }

    public ColorStateList getPlaceholderTextColor() {
        return this.f9378y;
    }

    public CharSequence getPrefixText() {
        return this.f9340f.a();
    }

    public ColorStateList getPrefixTextColor() {
        return this.f9340f.b();
    }

    public TextView getPrefixTextView() {
        return this.f9340f.c();
    }

    public CharSequence getStartIconContentDescription() {
        return this.f9340f.d();
    }

    public Drawable getStartIconDrawable() {
        return this.f9340f.e();
    }

    public CharSequence getSuffixText() {
        return this.E;
    }

    public ColorStateList getSuffixTextColor() {
        return this.F.getTextColors();
    }

    public TextView getSuffixTextView() {
        return this.F;
    }

    public Typeface getTypeface() {
        return this.f9336c0;
    }

    public void h(g gVar) {
        this.f9349j0.add(gVar);
    }

    void k(float f10) {
        if (this.G0.getExpansionFraction() == f10) {
            return;
        }
        if (this.J0 == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.J0 = valueAnimator;
            valueAnimator.setInterpolator(AnimationUtils.f16556b);
            this.J0.setDuration(167L);
            this.J0.addUpdateListener(new d());
        }
        this.J0.setFloatValues(this.G0.getExpansionFraction(), f10);
        this.J0.start();
    }

    void m0(int i10) {
        boolean z10 = this.f9364r;
        int i11 = this.f9362q;
        if (i11 == -1) {
            this.f9366s.setText(String.valueOf(i10));
            this.f9366s.setContentDescription(null);
            this.f9364r = false;
        } else {
            this.f9364r = i10 > i11;
            n0(getContext(), this.f9366s, i10, this.f9362q, this.f9364r);
            if (z10 != this.f9364r) {
                o0();
            }
            this.f9366s.setText(BidiFormatter.c().j(getContext().getString(R$string.character_counter_pattern, Integer.valueOf(i10), Integer.valueOf(this.f9362q))));
        }
        if (this.f9346i == null || z10 == this.f9364r) {
            return;
        }
        w0(false);
        E0();
        r0();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.G0.maybeUpdateFontWeightAdjustment(configuration);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        EditText editText = this.f9346i;
        if (editText != null) {
            Rect rect = this.W;
            DescendantOffsetUtils.getDescendantRect(this, editText, rect);
            k0(rect);
            if (this.G) {
                this.G0.setExpandedTextSize(this.f9346i.getTextSize());
                int gravity = this.f9346i.getGravity();
                this.G0.setCollapsedTextGravity((gravity & (-113)) | 48);
                this.G0.setExpandedTextGravity(gravity);
                this.G0.setCollapsedBounds(q(rect));
                this.G0.setExpandedBounds(t(rect));
                this.G0.recalculate();
                if (!A() || this.F0) {
                    return;
                }
                R();
            }
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        boolean s02 = s0();
        boolean q02 = q0();
        if (s02 || q02) {
            this.f9346i.post(new c());
        }
        y0();
        C0();
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setError(savedState.f9382e);
        if (savedState.f9383f) {
            this.f9347i0.post(new b());
        }
        setHint(savedState.f9384g);
        setHelperText(savedState.f9385h);
        setPlaceholderText(savedState.f9386i);
        requestLayout();
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onRtlPropertiesChanged(int i10) {
        super.onRtlPropertiesChanged(i10);
        boolean z10 = false;
        boolean z11 = i10 == 1;
        boolean z12 = this.N;
        if (z11 != z12) {
            if (z11 && !z12) {
                z10 = true;
            }
            float a10 = this.M.r().a(this.f9335b0);
            float a11 = this.M.t().a(this.f9335b0);
            float a12 = this.M.j().a(this.f9335b0);
            float a13 = this.M.l().a(this.f9335b0);
            float f10 = z10 ? a10 : a11;
            if (z10) {
                a10 = a11;
            }
            float f11 = z10 ? a12 : a13;
            if (z10) {
                a12 = a13;
            }
            Y(f10, a10, f11, a12);
        }
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.f9358o.l()) {
            savedState.f9382e = getError();
        }
        savedState.f9383f = I() && this.f9347i0.isChecked();
        savedState.f9384g = getHint();
        savedState.f9385h = getHelperText();
        savedState.f9386i = getPlaceholderText();
        return savedState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean q0() {
        boolean z10;
        if (this.f9346i == null) {
            return false;
        }
        boolean z11 = true;
        if (f0()) {
            int measuredWidth = this.f9340f.getMeasuredWidth() - this.f9346i.getPaddingLeft();
            if (this.f9337d0 == null || this.f9339e0 != measuredWidth) {
                ColorDrawable colorDrawable = new ColorDrawable();
                this.f9337d0 = colorDrawable;
                this.f9339e0 = measuredWidth;
                colorDrawable.setBounds(0, 0, measuredWidth, 1);
            }
            Drawable[] a10 = TextViewCompat.a(this.f9346i);
            Drawable drawable = a10[0];
            Drawable drawable2 = this.f9337d0;
            if (drawable != drawable2) {
                TextViewCompat.i(this.f9346i, drawable2, a10[1], a10[2], a10[3]);
                z10 = true;
            }
            z10 = false;
        } else {
            if (this.f9337d0 != null) {
                Drawable[] a11 = TextViewCompat.a(this.f9346i);
                TextViewCompat.i(this.f9346i, null, a11[1], a11[2], a11[3]);
                this.f9337d0 = null;
                z10 = true;
            }
            z10 = false;
        }
        if (e0()) {
            int measuredWidth2 = this.F.getMeasuredWidth() - this.f9346i.getPaddingRight();
            CheckableImageButton endIconToUpdateDummyDrawable = getEndIconToUpdateDummyDrawable();
            if (endIconToUpdateDummyDrawable != null) {
                measuredWidth2 = measuredWidth2 + endIconToUpdateDummyDrawable.getMeasuredWidth() + MarginLayoutParamsCompat.b((ViewGroup.MarginLayoutParams) endIconToUpdateDummyDrawable.getLayoutParams());
            }
            Drawable[] a12 = TextViewCompat.a(this.f9346i);
            Drawable drawable3 = this.f9355m0;
            if (drawable3 != null && this.f9357n0 != measuredWidth2) {
                this.f9357n0 = measuredWidth2;
                drawable3.setBounds(0, 0, measuredWidth2, 1);
                TextViewCompat.i(this.f9346i, a12[0], a12[1], this.f9355m0, a12[3]);
            } else {
                if (drawable3 == null) {
                    ColorDrawable colorDrawable2 = new ColorDrawable();
                    this.f9355m0 = colorDrawable2;
                    this.f9357n0 = measuredWidth2;
                    colorDrawable2.setBounds(0, 0, measuredWidth2, 1);
                }
                Drawable drawable4 = a12[2];
                Drawable drawable5 = this.f9355m0;
                if (drawable4 != drawable5) {
                    this.f9359o0 = a12[2];
                    TextViewCompat.i(this.f9346i, a12[0], a12[1], drawable5, a12[3]);
                } else {
                    z11 = z10;
                }
            }
        } else {
            if (this.f9355m0 == null) {
                return z10;
            }
            Drawable[] a13 = TextViewCompat.a(this.f9346i);
            if (a13[2] == this.f9355m0) {
                TextViewCompat.i(this.f9346i, a13[0], a13[1], this.f9359o0, a13[3]);
            } else {
                z11 = z10;
            }
            this.f9355m0 = null;
        }
        return z11;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r0() {
        Drawable background;
        TextView textView;
        EditText editText = this.f9346i;
        if (editText == null || this.P != 0 || (background = editText.getBackground()) == null) {
            return;
        }
        if (t.a(background)) {
            background = background.mutate();
        }
        if (this.f9358o.l()) {
            background.setColorFilter(AppCompatDrawableManager.e(this.f9358o.p(), PorterDuff.Mode.SRC_IN));
        } else if (this.f9364r && (textView = this.f9366s) != null) {
            background.setColorFilter(AppCompatDrawableManager.e(textView.getCurrentTextColor(), PorterDuff.Mode.SRC_IN));
        } else {
            DrawableCompat.a(background);
            this.f9346i.refreshDrawableState();
        }
    }

    public void setBoxBackgroundColor(int i10) {
        if (this.V != i10) {
            this.V = i10;
            this.A0 = i10;
            this.C0 = i10;
            this.D0 = i10;
            l();
        }
    }

    public void setBoxBackgroundColorResource(int i10) {
        setBoxBackgroundColor(ContextCompat.c(getContext(), i10));
    }

    public void setBoxBackgroundColorStateList(ColorStateList colorStateList) {
        int defaultColor = colorStateList.getDefaultColor();
        this.A0 = defaultColor;
        this.V = defaultColor;
        this.B0 = colorStateList.getColorForState(new int[]{-16842910}, -1);
        this.C0 = colorStateList.getColorForState(new int[]{R.attr.state_focused, R.attr.state_enabled}, -1);
        this.D0 = colorStateList.getColorForState(new int[]{R.attr.state_hovered, R.attr.state_enabled}, -1);
        l();
    }

    public void setBoxBackgroundMode(int i10) {
        if (i10 == this.P) {
            return;
        }
        this.P = i10;
        if (this.f9346i != null) {
            Q();
        }
    }

    public void setBoxCollapsedPaddingTop(int i10) {
        this.Q = i10;
    }

    public void setBoxStrokeColor(int i10) {
        if (this.f9379y0 != i10) {
            this.f9379y0 = i10;
            E0();
        }
    }

    public void setBoxStrokeColorStateList(ColorStateList colorStateList) {
        if (colorStateList.isStateful()) {
            this.f9375w0 = colorStateList.getDefaultColor();
            this.E0 = colorStateList.getColorForState(new int[]{-16842910}, -1);
            this.f9377x0 = colorStateList.getColorForState(new int[]{R.attr.state_hovered, R.attr.state_enabled}, -1);
            this.f9379y0 = colorStateList.getColorForState(new int[]{R.attr.state_focused, R.attr.state_enabled}, -1);
        } else if (this.f9379y0 != colorStateList.getDefaultColor()) {
            this.f9379y0 = colorStateList.getDefaultColor();
        }
        E0();
    }

    public void setBoxStrokeErrorColor(ColorStateList colorStateList) {
        if (this.f9381z0 != colorStateList) {
            this.f9381z0 = colorStateList;
            E0();
        }
    }

    public void setBoxStrokeWidth(int i10) {
        this.S = i10;
        E0();
    }

    public void setBoxStrokeWidthFocused(int i10) {
        this.T = i10;
        E0();
    }

    public void setBoxStrokeWidthFocusedResource(int i10) {
        setBoxStrokeWidthFocused(getResources().getDimensionPixelSize(i10));
    }

    public void setBoxStrokeWidthResource(int i10) {
        setBoxStrokeWidth(getResources().getDimensionPixelSize(i10));
    }

    public void setCounterEnabled(boolean z10) {
        if (this.f9360p != z10) {
            if (z10) {
                AppCompatTextView appCompatTextView = new AppCompatTextView(getContext());
                this.f9366s = appCompatTextView;
                appCompatTextView.setId(R$id.textinput_counter);
                Typeface typeface = this.f9336c0;
                if (typeface != null) {
                    this.f9366s.setTypeface(typeface);
                }
                this.f9366s.setMaxLines(1);
                this.f9358o.e(this.f9366s, 2);
                MarginLayoutParamsCompat.d((ViewGroup.MarginLayoutParams) this.f9366s.getLayoutParams(), getResources().getDimensionPixelOffset(R$dimen.mtrl_textinput_counter_margin_start));
                o0();
                l0();
            } else {
                this.f9358o.B(this.f9366s, 2);
                this.f9366s = null;
            }
            this.f9360p = z10;
        }
    }

    public void setCounterMaxLength(int i10) {
        if (this.f9362q != i10) {
            if (i10 > 0) {
                this.f9362q = i10;
            } else {
                this.f9362q = -1;
            }
            if (this.f9360p) {
                l0();
            }
        }
    }

    public void setCounterOverflowTextAppearance(int i10) {
        if (this.f9368t != i10) {
            this.f9368t = i10;
            o0();
        }
    }

    public void setCounterOverflowTextColor(ColorStateList colorStateList) {
        if (this.D != colorStateList) {
            this.D = colorStateList;
            o0();
        }
    }

    public void setCounterTextAppearance(int i10) {
        if (this.f9370u != i10) {
            this.f9370u = i10;
            o0();
        }
    }

    public void setCounterTextColor(ColorStateList colorStateList) {
        if (this.C != colorStateList) {
            this.C = colorStateList;
            o0();
        }
    }

    public void setDefaultHintTextColor(ColorStateList colorStateList) {
        this.f9371u0 = colorStateList;
        this.f9373v0 = colorStateList;
        if (this.f9346i != null) {
            w0(false);
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        T(this, z10);
        super.setEnabled(z10);
    }

    public void setEndIconActivated(boolean z10) {
        this.f9347i0.setActivated(z10);
    }

    public void setEndIconCheckable(boolean z10) {
        this.f9347i0.setCheckable(z10);
    }

    public void setEndIconContentDescription(int i10) {
        setEndIconContentDescription(i10 != 0 ? getResources().getText(i10) : null);
    }

    public void setEndIconDrawable(int i10) {
        setEndIconDrawable(i10 != 0 ? AppCompatResources.b(getContext(), i10) : null);
    }

    public void setEndIconMode(int i10) {
        int i11 = this.f9343g0;
        if (i11 == i10) {
            return;
        }
        this.f9343g0 = i10;
        C(i11);
        setEndIconVisible(i10 != 0);
        if (getEndIconDelegate().b(this.P)) {
            getEndIconDelegate().a();
            IconHelper.a(this, this.f9347i0, this.f9351k0, this.f9353l0);
            return;
        }
        throw new IllegalStateException("The current box background mode " + this.P + " is not supported by the end icon mode " + i10);
    }

    public void setEndIconOnClickListener(View.OnClickListener onClickListener) {
        b0(this.f9347i0, onClickListener, this.f9361p0);
    }

    public void setEndIconOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.f9361p0 = onLongClickListener;
        c0(this.f9347i0, onLongClickListener);
    }

    public void setEndIconTintList(ColorStateList colorStateList) {
        if (this.f9351k0 != colorStateList) {
            this.f9351k0 = colorStateList;
            IconHelper.a(this, this.f9347i0, colorStateList, this.f9353l0);
        }
    }

    public void setEndIconTintMode(PorterDuff.Mode mode) {
        if (this.f9353l0 != mode) {
            this.f9353l0 = mode;
            IconHelper.a(this, this.f9347i0, this.f9351k0, mode);
        }
    }

    public void setEndIconVisible(boolean z10) {
        if (K() != z10) {
            this.f9347i0.setVisibility(z10 ? 0 : 8);
            t0();
            C0();
            q0();
        }
    }

    public void setError(CharSequence charSequence) {
        if (!this.f9358o.z()) {
            if (TextUtils.isEmpty(charSequence)) {
                return;
            } else {
                setErrorEnabled(true);
            }
        }
        if (!TextUtils.isEmpty(charSequence)) {
            this.f9358o.O(charSequence);
        } else {
            this.f9358o.v();
        }
    }

    public void setErrorContentDescription(CharSequence charSequence) {
        this.f9358o.D(charSequence);
    }

    public void setErrorEnabled(boolean z10) {
        this.f9358o.E(z10);
    }

    public void setErrorIconDrawable(int i10) {
        setErrorIconDrawable(i10 != 0 ? AppCompatResources.b(getContext(), i10) : null);
        V();
    }

    public void setErrorIconOnClickListener(View.OnClickListener onClickListener) {
        b0(this.f9365r0, onClickListener, this.f9363q0);
    }

    public void setErrorIconOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.f9363q0 = onLongClickListener;
        c0(this.f9365r0, onLongClickListener);
    }

    public void setErrorIconTintList(ColorStateList colorStateList) {
        if (this.f9367s0 != colorStateList) {
            this.f9367s0 = colorStateList;
            IconHelper.a(this, this.f9365r0, colorStateList, this.f9369t0);
        }
    }

    public void setErrorIconTintMode(PorterDuff.Mode mode) {
        if (this.f9369t0 != mode) {
            this.f9369t0 = mode;
            IconHelper.a(this, this.f9365r0, this.f9367s0, mode);
        }
    }

    public void setErrorTextAppearance(int i10) {
        this.f9358o.F(i10);
    }

    public void setErrorTextColor(ColorStateList colorStateList) {
        this.f9358o.G(colorStateList);
    }

    public void setExpandedHintEnabled(boolean z10) {
        if (this.H0 != z10) {
            this.H0 = z10;
            w0(false);
        }
    }

    public void setHelperText(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            if (M()) {
                setHelperTextEnabled(false);
            }
        } else {
            if (!M()) {
                setHelperTextEnabled(true);
            }
            this.f9358o.P(charSequence);
        }
    }

    public void setHelperTextColor(ColorStateList colorStateList) {
        this.f9358o.J(colorStateList);
    }

    public void setHelperTextEnabled(boolean z10) {
        this.f9358o.I(z10);
    }

    public void setHelperTextTextAppearance(int i10) {
        this.f9358o.H(i10);
    }

    public void setHint(CharSequence charSequence) {
        if (this.G) {
            setHintInternal(charSequence);
            sendAccessibilityEvent(2048);
        }
    }

    public void setHintAnimationEnabled(boolean z10) {
        this.I0 = z10;
    }

    public void setHintEnabled(boolean z10) {
        if (z10 != this.G) {
            this.G = z10;
            if (!z10) {
                this.I = false;
                if (!TextUtils.isEmpty(this.H) && TextUtils.isEmpty(this.f9346i.getHint())) {
                    this.f9346i.setHint(this.H);
                }
                setHintInternal(null);
            } else {
                CharSequence hint = this.f9346i.getHint();
                if (!TextUtils.isEmpty(hint)) {
                    if (TextUtils.isEmpty(this.H)) {
                        setHint(hint);
                    }
                    this.f9346i.setHint((CharSequence) null);
                }
                this.I = true;
            }
            if (this.f9346i != null) {
                v0();
            }
        }
    }

    public void setHintTextAppearance(int i10) {
        this.G0.setCollapsedTextAppearance(i10);
        this.f9373v0 = this.G0.getCollapsedTextColor();
        if (this.f9346i != null) {
            w0(false);
            v0();
        }
    }

    public void setHintTextColor(ColorStateList colorStateList) {
        if (this.f9373v0 != colorStateList) {
            if (this.f9371u0 == null) {
                this.G0.setCollapsedTextColor(colorStateList);
            }
            this.f9373v0 = colorStateList;
            if (this.f9346i != null) {
                w0(false);
            }
        }
    }

    public void setMaxEms(int i10) {
        this.f9352l = i10;
        EditText editText = this.f9346i;
        if (editText == null || i10 == -1) {
            return;
        }
        editText.setMaxEms(i10);
    }

    public void setMaxWidth(int i10) {
        this.f9356n = i10;
        EditText editText = this.f9346i;
        if (editText == null || i10 == -1) {
            return;
        }
        editText.setMaxWidth(i10);
    }

    public void setMaxWidthResource(int i10) {
        setMaxWidth(getContext().getResources().getDimensionPixelSize(i10));
    }

    public void setMinEms(int i10) {
        this.f9350k = i10;
        EditText editText = this.f9346i;
        if (editText == null || i10 == -1) {
            return;
        }
        editText.setMinEms(i10);
    }

    public void setMinWidth(int i10) {
        this.f9354m = i10;
        EditText editText = this.f9346i;
        if (editText == null || i10 == -1) {
            return;
        }
        editText.setMinWidth(i10);
    }

    public void setMinWidthResource(int i10) {
        setMinWidth(getContext().getResources().getDimensionPixelSize(i10));
    }

    @Deprecated
    public void setPasswordVisibilityToggleContentDescription(int i10) {
        setPasswordVisibilityToggleContentDescription(i10 != 0 ? getResources().getText(i10) : null);
    }

    @Deprecated
    public void setPasswordVisibilityToggleDrawable(int i10) {
        setPasswordVisibilityToggleDrawable(i10 != 0 ? AppCompatResources.b(getContext(), i10) : null);
    }

    @Deprecated
    public void setPasswordVisibilityToggleEnabled(boolean z10) {
        if (z10 && this.f9343g0 != 1) {
            setEndIconMode(1);
        } else {
            if (z10) {
                return;
            }
            setEndIconMode(0);
        }
    }

    @Deprecated
    public void setPasswordVisibilityToggleTintList(ColorStateList colorStateList) {
        this.f9351k0 = colorStateList;
        IconHelper.a(this, this.f9347i0, colorStateList, this.f9353l0);
    }

    @Deprecated
    public void setPasswordVisibilityToggleTintMode(PorterDuff.Mode mode) {
        this.f9353l0 = mode;
        IconHelper.a(this, this.f9347i0, this.f9351k0, mode);
    }

    public void setPlaceholderText(CharSequence charSequence) {
        if (this.f9376x == null) {
            AppCompatTextView appCompatTextView = new AppCompatTextView(getContext());
            this.f9376x = appCompatTextView;
            appCompatTextView.setId(R$id.textinput_placeholder);
            ViewCompat.w0(this.f9376x, 2);
            Fade z10 = z();
            this.A = z10;
            z10.setStartDelay(67L);
            this.B = z();
            setPlaceholderTextAppearance(this.f9380z);
            setPlaceholderTextColor(this.f9378y);
        }
        if (TextUtils.isEmpty(charSequence)) {
            setPlaceholderTextEnabled(false);
        } else {
            if (!this.f9374w) {
                setPlaceholderTextEnabled(true);
            }
            this.f9372v = charSequence;
        }
        z0();
    }

    public void setPlaceholderTextAppearance(int i10) {
        this.f9380z = i10;
        TextView textView = this.f9376x;
        if (textView != null) {
            TextViewCompat.n(textView, i10);
        }
    }

    public void setPlaceholderTextColor(ColorStateList colorStateList) {
        if (this.f9378y != colorStateList) {
            this.f9378y = colorStateList;
            TextView textView = this.f9376x;
            if (textView == null || colorStateList == null) {
                return;
            }
            textView.setTextColor(colorStateList);
        }
    }

    public void setPrefixText(CharSequence charSequence) {
        this.f9340f.k(charSequence);
    }

    public void setPrefixTextAppearance(int i10) {
        this.f9340f.l(i10);
    }

    public void setPrefixTextColor(ColorStateList colorStateList) {
        this.f9340f.m(colorStateList);
    }

    public void setStartIconCheckable(boolean z10) {
        this.f9340f.n(z10);
    }

    public void setStartIconContentDescription(int i10) {
        setStartIconContentDescription(i10 != 0 ? getResources().getText(i10) : null);
    }

    public void setStartIconDrawable(int i10) {
        setStartIconDrawable(i10 != 0 ? AppCompatResources.b(getContext(), i10) : null);
    }

    public void setStartIconOnClickListener(View.OnClickListener onClickListener) {
        this.f9340f.q(onClickListener);
    }

    public void setStartIconOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.f9340f.r(onLongClickListener);
    }

    public void setStartIconTintList(ColorStateList colorStateList) {
        this.f9340f.s(colorStateList);
    }

    public void setStartIconTintMode(PorterDuff.Mode mode) {
        this.f9340f.t(mode);
    }

    public void setStartIconVisible(boolean z10) {
        this.f9340f.u(z10);
    }

    public void setSuffixText(CharSequence charSequence) {
        this.E = TextUtils.isEmpty(charSequence) ? null : charSequence;
        this.F.setText(charSequence);
        D0();
    }

    public void setSuffixTextAppearance(int i10) {
        TextViewCompat.n(this.F, i10);
    }

    public void setSuffixTextColor(ColorStateList colorStateList) {
        this.F.setTextColor(colorStateList);
    }

    public void setTextInputAccessibilityDelegate(e eVar) {
        EditText editText = this.f9346i;
        if (editText != null) {
            ViewCompat.l0(editText, eVar);
        }
    }

    public void setTypeface(Typeface typeface) {
        if (typeface != this.f9336c0) {
            this.f9336c0 = typeface;
            this.G0.setTypefaces(typeface);
            this.f9358o.L(typeface);
            TextView textView = this.f9366s;
            if (textView != null) {
                textView.setTypeface(typeface);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w0(boolean z10) {
        x0(z10, false);
    }

    public TextInputLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.textInputStyle);
    }

    public void setEndIconContentDescription(CharSequence charSequence) {
        if (getEndIconContentDescription() != charSequence) {
            this.f9347i0.setContentDescription(charSequence);
        }
    }

    public void setEndIconDrawable(Drawable drawable) {
        this.f9347i0.setImageDrawable(drawable);
        if (drawable != null) {
            IconHelper.a(this, this.f9347i0, this.f9351k0, this.f9353l0);
            U();
        }
    }

    public void setStartIconContentDescription(CharSequence charSequence) {
        this.f9340f.o(charSequence);
    }

    public void setStartIconDrawable(Drawable drawable) {
        this.f9340f.p(drawable);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v108 */
    /* JADX WARN: Type inference failed for: r3v49 */
    /* JADX WARN: Type inference failed for: r3v50, types: [int, boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public TextInputLayout(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r9), attributeSet, i10);
        int i11;
        ?? r32;
        boolean z10;
        int i12;
        int i13 = M0;
        this.f9350k = -1;
        this.f9352l = -1;
        this.f9354m = -1;
        this.f9356n = -1;
        this.f9358o = new IndicatorViewController(this);
        this.W = new Rect();
        this.f9334a0 = new Rect();
        this.f9335b0 = new RectF();
        this.f9341f0 = new LinkedHashSet<>();
        this.f9343g0 = 0;
        SparseArray<EndIconDelegate> sparseArray = new SparseArray<>();
        this.f9345h0 = sparseArray;
        this.f9349j0 = new LinkedHashSet<>();
        CollapsingTextHelper collapsingTextHelper = new CollapsingTextHelper(this);
        this.G0 = collapsingTextHelper;
        Context context2 = getContext();
        setOrientation(1);
        setWillNotDraw(false);
        setAddStatesFromChildren(true);
        FrameLayout frameLayout = new FrameLayout(context2);
        this.f9338e = frameLayout;
        FrameLayout frameLayout2 = new FrameLayout(context2);
        this.f9344h = frameLayout2;
        LinearLayout linearLayout = new LinearLayout(context2);
        this.f9342g = linearLayout;
        AppCompatTextView appCompatTextView = new AppCompatTextView(context2);
        this.F = appCompatTextView;
        linearLayout.setVisibility(8);
        frameLayout2.setVisibility(8);
        appCompatTextView.setVisibility(8);
        LayoutInflater from = LayoutInflater.from(context2);
        int i14 = R$layout.design_text_input_end_icon;
        CheckableImageButton checkableImageButton = (CheckableImageButton) from.inflate(i14, (ViewGroup) linearLayout, false);
        this.f9365r0 = checkableImageButton;
        CheckableImageButton checkableImageButton2 = (CheckableImageButton) from.inflate(i14, (ViewGroup) frameLayout2, false);
        this.f9347i0 = checkableImageButton2;
        frameLayout.setAddStatesFromChildren(true);
        linearLayout.setOrientation(0);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -1, 8388613));
        frameLayout2.setLayoutParams(new FrameLayout.LayoutParams(-2, -1));
        TimeInterpolator timeInterpolator = AnimationUtils.f16555a;
        collapsingTextHelper.setTextSizeInterpolator(timeInterpolator);
        collapsingTextHelper.setPositionInterpolator(timeInterpolator);
        collapsingTextHelper.setCollapsedTextGravity(8388659);
        int[] iArr = R$styleable.TextInputLayout;
        int i15 = R$styleable.TextInputLayout_counterTextAppearance;
        int i16 = R$styleable.TextInputLayout_counterOverflowTextAppearance;
        int i17 = R$styleable.TextInputLayout_errorTextAppearance;
        int i18 = R$styleable.TextInputLayout_helperTextTextAppearance;
        int i19 = R$styleable.TextInputLayout_hintTextAppearance;
        TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(context2, attributeSet, iArr, i10, i13, i15, i16, i17, i18, i19);
        StartCompoundLayout startCompoundLayout = new StartCompoundLayout(this, obtainTintedStyledAttributes);
        this.f9340f = startCompoundLayout;
        this.G = obtainTintedStyledAttributes.a(R$styleable.TextInputLayout_hintEnabled, true);
        setHint(obtainTintedStyledAttributes.p(R$styleable.TextInputLayout_android_hint));
        this.I0 = obtainTintedStyledAttributes.a(R$styleable.TextInputLayout_hintAnimationEnabled, true);
        this.H0 = obtainTintedStyledAttributes.a(R$styleable.TextInputLayout_expandedHintEnabled, true);
        int i20 = R$styleable.TextInputLayout_android_minEms;
        if (obtainTintedStyledAttributes.s(i20)) {
            i11 = -1;
            setMinEms(obtainTintedStyledAttributes.k(i20, -1));
        } else {
            i11 = -1;
            int i21 = R$styleable.TextInputLayout_android_minWidth;
            if (obtainTintedStyledAttributes.s(i21)) {
                setMinWidth(obtainTintedStyledAttributes.f(i21, -1));
            }
        }
        int i22 = R$styleable.TextInputLayout_android_maxEms;
        if (obtainTintedStyledAttributes.s(i22)) {
            setMaxEms(obtainTintedStyledAttributes.k(i22, i11));
        } else {
            int i23 = R$styleable.TextInputLayout_android_maxWidth;
            if (obtainTintedStyledAttributes.s(i23)) {
                setMaxWidth(obtainTintedStyledAttributes.f(i23, i11));
            }
        }
        this.M = ShapeAppearanceModel.e(context2, attributeSet, i10, i13).m();
        this.O = context2.getResources().getDimensionPixelOffset(R$dimen.mtrl_textinput_box_label_cutout_padding);
        this.Q = obtainTintedStyledAttributes.e(R$styleable.TextInputLayout_boxCollapsedPaddingTop, 0);
        this.S = obtainTintedStyledAttributes.f(R$styleable.TextInputLayout_boxStrokeWidth, context2.getResources().getDimensionPixelSize(R$dimen.mtrl_textinput_box_stroke_width_default));
        this.T = obtainTintedStyledAttributes.f(R$styleable.TextInputLayout_boxStrokeWidthFocused, context2.getResources().getDimensionPixelSize(R$dimen.mtrl_textinput_box_stroke_width_focused));
        this.R = this.S;
        float d10 = obtainTintedStyledAttributes.d(R$styleable.TextInputLayout_boxCornerRadiusTopStart, -1.0f);
        float d11 = obtainTintedStyledAttributes.d(R$styleable.TextInputLayout_boxCornerRadiusTopEnd, -1.0f);
        float d12 = obtainTintedStyledAttributes.d(R$styleable.TextInputLayout_boxCornerRadiusBottomEnd, -1.0f);
        float d13 = obtainTintedStyledAttributes.d(R$styleable.TextInputLayout_boxCornerRadiusBottomStart, -1.0f);
        ShapeAppearanceModel.b v7 = this.M.v();
        if (d10 >= 0.0f) {
            v7.J(d10);
        }
        if (d11 >= 0.0f) {
            v7.O(d11);
        }
        if (d12 >= 0.0f) {
            v7.B(d12);
        }
        if (d13 >= 0.0f) {
            v7.w(d13);
        }
        this.M = v7.m();
        ColorStateList b10 = MaterialResources.b(context2, obtainTintedStyledAttributes, R$styleable.TextInputLayout_boxBackgroundColor);
        if (b10 != null) {
            int defaultColor = b10.getDefaultColor();
            this.A0 = defaultColor;
            this.V = defaultColor;
            if (b10.isStateful()) {
                this.B0 = b10.getColorForState(new int[]{-16842910}, -1);
                this.C0 = b10.getColorForState(new int[]{R.attr.state_focused, R.attr.state_enabled}, -1);
                this.D0 = b10.getColorForState(new int[]{R.attr.state_hovered, R.attr.state_enabled}, -1);
            } else {
                this.C0 = this.A0;
                ColorStateList a10 = AppCompatResources.a(context2, R$color.mtrl_filled_background_color);
                this.B0 = a10.getColorForState(new int[]{-16842910}, -1);
                this.D0 = a10.getColorForState(new int[]{R.attr.state_hovered}, -1);
            }
        } else {
            this.V = 0;
            this.A0 = 0;
            this.B0 = 0;
            this.C0 = 0;
            this.D0 = 0;
        }
        int i24 = R$styleable.TextInputLayout_android_textColorHint;
        if (obtainTintedStyledAttributes.s(i24)) {
            ColorStateList c10 = obtainTintedStyledAttributes.c(i24);
            this.f9373v0 = c10;
            this.f9371u0 = c10;
        }
        int i25 = R$styleable.TextInputLayout_boxStrokeColor;
        ColorStateList b11 = MaterialResources.b(context2, obtainTintedStyledAttributes, i25);
        this.f9379y0 = obtainTintedStyledAttributes.b(i25, 0);
        this.f9375w0 = ContextCompat.c(context2, R$color.mtrl_textinput_default_box_stroke_color);
        this.E0 = ContextCompat.c(context2, R$color.mtrl_textinput_disabled_color);
        this.f9377x0 = ContextCompat.c(context2, R$color.mtrl_textinput_hovered_box_stroke_color);
        if (b11 != null) {
            setBoxStrokeColorStateList(b11);
        }
        int i26 = R$styleable.TextInputLayout_boxStrokeErrorColor;
        if (obtainTintedStyledAttributes.s(i26)) {
            setBoxStrokeErrorColor(MaterialResources.b(context2, obtainTintedStyledAttributes, i26));
        }
        if (obtainTintedStyledAttributes.n(i19, -1) != -1) {
            r32 = 0;
            setHintTextAppearance(obtainTintedStyledAttributes.n(i19, 0));
        } else {
            r32 = 0;
        }
        int n10 = obtainTintedStyledAttributes.n(i17, r32);
        CharSequence p10 = obtainTintedStyledAttributes.p(R$styleable.TextInputLayout_errorContentDescription);
        boolean a11 = obtainTintedStyledAttributes.a(R$styleable.TextInputLayout_errorEnabled, r32);
        checkableImageButton.setId(R$id.text_input_error_icon);
        if (MaterialResources.i(context2)) {
            MarginLayoutParamsCompat.d((ViewGroup.MarginLayoutParams) checkableImageButton.getLayoutParams(), r32);
        }
        int i27 = R$styleable.TextInputLayout_errorIconTint;
        if (obtainTintedStyledAttributes.s(i27)) {
            this.f9367s0 = MaterialResources.b(context2, obtainTintedStyledAttributes, i27);
        }
        int i28 = R$styleable.TextInputLayout_errorIconTintMode;
        if (obtainTintedStyledAttributes.s(i28)) {
            this.f9369t0 = ViewUtils.parseTintMode(obtainTintedStyledAttributes.k(i28, -1), null);
        }
        int i29 = R$styleable.TextInputLayout_errorIconDrawable;
        if (obtainTintedStyledAttributes.s(i29)) {
            setErrorIconDrawable(obtainTintedStyledAttributes.g(i29));
        }
        checkableImageButton.setContentDescription(getResources().getText(R$string.error_icon_content_description));
        ViewCompat.w0(checkableImageButton, 2);
        checkableImageButton.setClickable(false);
        checkableImageButton.setPressable(false);
        checkableImageButton.setFocusable(false);
        int n11 = obtainTintedStyledAttributes.n(i18, 0);
        boolean a12 = obtainTintedStyledAttributes.a(R$styleable.TextInputLayout_helperTextEnabled, false);
        CharSequence p11 = obtainTintedStyledAttributes.p(R$styleable.TextInputLayout_helperText);
        int n12 = obtainTintedStyledAttributes.n(R$styleable.TextInputLayout_placeholderTextAppearance, 0);
        CharSequence p12 = obtainTintedStyledAttributes.p(R$styleable.TextInputLayout_placeholderText);
        int n13 = obtainTintedStyledAttributes.n(R$styleable.TextInputLayout_suffixTextAppearance, 0);
        CharSequence p13 = obtainTintedStyledAttributes.p(R$styleable.TextInputLayout_suffixText);
        boolean a13 = obtainTintedStyledAttributes.a(R$styleable.TextInputLayout_counterEnabled, false);
        setCounterMaxLength(obtainTintedStyledAttributes.k(R$styleable.TextInputLayout_counterMaxLength, -1));
        this.f9370u = obtainTintedStyledAttributes.n(i15, 0);
        this.f9368t = obtainTintedStyledAttributes.n(i16, 0);
        setBoxBackgroundMode(obtainTintedStyledAttributes.k(R$styleable.TextInputLayout_boxBackgroundMode, 0));
        if (MaterialResources.i(context2)) {
            MarginLayoutParamsCompat.d((ViewGroup.MarginLayoutParams) checkableImageButton2.getLayoutParams(), 0);
        }
        int n14 = obtainTintedStyledAttributes.n(R$styleable.TextInputLayout_endIconDrawable, 0);
        sparseArray.append(-1, new CustomEndIconDelegate(this, n14));
        sparseArray.append(0, new NoEndIconDelegate(this));
        if (n14 == 0) {
            z10 = a12;
            i12 = obtainTintedStyledAttributes.n(R$styleable.TextInputLayout_passwordToggleDrawable, 0);
        } else {
            z10 = a12;
            i12 = n14;
        }
        sparseArray.append(1, new PasswordToggleEndIconDelegate(this, i12));
        sparseArray.append(2, new ClearTextEndIconDelegate(this, n14));
        sparseArray.append(3, new DropdownMenuEndIconDelegate(this, n14));
        int i30 = R$styleable.TextInputLayout_passwordToggleEnabled;
        if (!obtainTintedStyledAttributes.s(i30)) {
            int i31 = R$styleable.TextInputLayout_endIconTint;
            if (obtainTintedStyledAttributes.s(i31)) {
                this.f9351k0 = MaterialResources.b(context2, obtainTintedStyledAttributes, i31);
            }
            int i32 = R$styleable.TextInputLayout_endIconTintMode;
            if (obtainTintedStyledAttributes.s(i32)) {
                this.f9353l0 = ViewUtils.parseTintMode(obtainTintedStyledAttributes.k(i32, -1), null);
            }
        }
        int i33 = R$styleable.TextInputLayout_endIconMode;
        if (obtainTintedStyledAttributes.s(i33)) {
            setEndIconMode(obtainTintedStyledAttributes.k(i33, 0));
            int i34 = R$styleable.TextInputLayout_endIconContentDescription;
            if (obtainTintedStyledAttributes.s(i34)) {
                setEndIconContentDescription(obtainTintedStyledAttributes.p(i34));
            }
            setEndIconCheckable(obtainTintedStyledAttributes.a(R$styleable.TextInputLayout_endIconCheckable, true));
        } else if (obtainTintedStyledAttributes.s(i30)) {
            int i35 = R$styleable.TextInputLayout_passwordToggleTint;
            if (obtainTintedStyledAttributes.s(i35)) {
                this.f9351k0 = MaterialResources.b(context2, obtainTintedStyledAttributes, i35);
            }
            int i36 = R$styleable.TextInputLayout_passwordToggleTintMode;
            if (obtainTintedStyledAttributes.s(i36)) {
                this.f9353l0 = ViewUtils.parseTintMode(obtainTintedStyledAttributes.k(i36, -1), null);
            }
            setEndIconMode(obtainTintedStyledAttributes.a(i30, false) ? 1 : 0);
            setEndIconContentDescription(obtainTintedStyledAttributes.p(R$styleable.TextInputLayout_passwordToggleContentDescription));
        }
        appCompatTextView.setId(R$id.textinput_suffix_text);
        appCompatTextView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 80));
        ViewCompat.n0(appCompatTextView, 1);
        setErrorContentDescription(p10);
        setCounterOverflowTextAppearance(this.f9368t);
        setHelperTextTextAppearance(n11);
        setErrorTextAppearance(n10);
        setCounterTextAppearance(this.f9370u);
        setPlaceholderText(p12);
        setPlaceholderTextAppearance(n12);
        setSuffixTextAppearance(n13);
        int i37 = R$styleable.TextInputLayout_errorTextColor;
        if (obtainTintedStyledAttributes.s(i37)) {
            setErrorTextColor(obtainTintedStyledAttributes.c(i37));
        }
        int i38 = R$styleable.TextInputLayout_helperTextTextColor;
        if (obtainTintedStyledAttributes.s(i38)) {
            setHelperTextColor(obtainTintedStyledAttributes.c(i38));
        }
        int i39 = R$styleable.TextInputLayout_hintTextColor;
        if (obtainTintedStyledAttributes.s(i39)) {
            setHintTextColor(obtainTintedStyledAttributes.c(i39));
        }
        int i40 = R$styleable.TextInputLayout_counterTextColor;
        if (obtainTintedStyledAttributes.s(i40)) {
            setCounterTextColor(obtainTintedStyledAttributes.c(i40));
        }
        int i41 = R$styleable.TextInputLayout_counterOverflowTextColor;
        if (obtainTintedStyledAttributes.s(i41)) {
            setCounterOverflowTextColor(obtainTintedStyledAttributes.c(i41));
        }
        int i42 = R$styleable.TextInputLayout_placeholderTextColor;
        if (obtainTintedStyledAttributes.s(i42)) {
            setPlaceholderTextColor(obtainTintedStyledAttributes.c(i42));
        }
        int i43 = R$styleable.TextInputLayout_suffixTextColor;
        if (obtainTintedStyledAttributes.s(i43)) {
            setSuffixTextColor(obtainTintedStyledAttributes.c(i43));
        }
        setEnabled(obtainTintedStyledAttributes.a(R$styleable.TextInputLayout_android_enabled, true));
        obtainTintedStyledAttributes.x();
        ViewCompat.w0(this, 2);
        ViewCompat.x0(this, 1);
        frameLayout2.addView(checkableImageButton2);
        linearLayout.addView(appCompatTextView);
        linearLayout.addView(checkableImageButton);
        linearLayout.addView(frameLayout2);
        frameLayout.addView(startCompoundLayout);
        frameLayout.addView(linearLayout);
        addView(frameLayout);
        setHelperTextEnabled(z10);
        setErrorEnabled(a11);
        setCounterEnabled(a13);
        setHelperText(p11);
        setSuffixText(p13);
    }

    public void setErrorIconDrawable(Drawable drawable) {
        this.f9365r0.setImageDrawable(drawable);
        u0();
        IconHelper.a(this, this.f9365r0, this.f9367s0, this.f9369t0);
    }

    @Deprecated
    public void setPasswordVisibilityToggleContentDescription(CharSequence charSequence) {
        this.f9347i0.setContentDescription(charSequence);
    }

    @Deprecated
    public void setPasswordVisibilityToggleDrawable(Drawable drawable) {
        this.f9347i0.setImageDrawable(drawable);
    }

    public void setHint(int i10) {
        setHint(i10 != 0 ? getResources().getText(i10) : null);
    }
}
