package com.google.android.material.textfield;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityManagerCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$drawable;
import com.google.android.material.R$string;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputLayout;
import p3.AnimationUtils;
import r3.MaterialColors;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DropdownMenuEndIconDelegate.java */
/* renamed from: com.google.android.material.textfield.d, reason: use source file name */
/* loaded from: classes.dex */
public class DropdownMenuEndIconDelegate extends EndIconDelegate {

    /* renamed from: t, reason: collision with root package name */
    private static final boolean f9409t = true;

    /* renamed from: e, reason: collision with root package name */
    private final TextWatcher f9410e;

    /* renamed from: f, reason: collision with root package name */
    private final View.OnFocusChangeListener f9411f;

    /* renamed from: g, reason: collision with root package name */
    private final TextInputLayout.e f9412g;

    /* renamed from: h, reason: collision with root package name */
    private final TextInputLayout.f f9413h;

    /* renamed from: i, reason: collision with root package name */
    @SuppressLint({"ClickableViewAccessibility"})
    private final TextInputLayout.g f9414i;

    /* renamed from: j, reason: collision with root package name */
    private final View.OnAttachStateChangeListener f9415j;

    /* renamed from: k, reason: collision with root package name */
    private final AccessibilityManagerCompat.b f9416k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f9417l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f9418m;

    /* renamed from: n, reason: collision with root package name */
    private long f9419n;

    /* renamed from: o, reason: collision with root package name */
    private StateListDrawable f9420o;

    /* renamed from: p, reason: collision with root package name */
    private MaterialShapeDrawable f9421p;

    /* renamed from: q, reason: collision with root package name */
    private AccessibilityManager f9422q;

    /* renamed from: r, reason: collision with root package name */
    private ValueAnimator f9423r;

    /* renamed from: s, reason: collision with root package name */
    private ValueAnimator f9424s;

    /* compiled from: DropdownMenuEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.d$a */
    /* loaded from: classes.dex */
    class a extends TextWatcherAdapter {

        /* compiled from: DropdownMenuEndIconDelegate.java */
        /* renamed from: com.google.android.material.textfield.d$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        class RunnableC0017a implements Runnable {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ AutoCompleteTextView f9426e;

            RunnableC0017a(AutoCompleteTextView autoCompleteTextView) {
                this.f9426e = autoCompleteTextView;
            }

            @Override // java.lang.Runnable
            public void run() {
                boolean isPopupShowing = this.f9426e.isPopupShowing();
                DropdownMenuEndIconDelegate.this.J(isPopupShowing);
                DropdownMenuEndIconDelegate.this.f9417l = isPopupShowing;
            }
        }

        a() {
        }

        @Override // com.google.android.material.internal.TextWatcherAdapter, android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            AutoCompleteTextView C = DropdownMenuEndIconDelegate.C(DropdownMenuEndIconDelegate.this.f9442a.getEditText());
            if (DropdownMenuEndIconDelegate.this.f9422q.isTouchExplorationEnabled() && DropdownMenuEndIconDelegate.H(C) && !DropdownMenuEndIconDelegate.this.f9444c.hasFocus()) {
                C.dismissDropDown();
            }
            C.post(new RunnableC0017a(C));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DropdownMenuEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.d$b */
    /* loaded from: classes.dex */
    public class b implements AutoCompleteTextView.OnDismissListener {
        b() {
        }

        @Override // android.widget.AutoCompleteTextView.OnDismissListener
        public void onDismiss() {
            DropdownMenuEndIconDelegate.this.N();
            DropdownMenuEndIconDelegate.this.J(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DropdownMenuEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.d$c */
    /* loaded from: classes.dex */
    public class c extends AnimatorListenerAdapter {
        c() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate = DropdownMenuEndIconDelegate.this;
            dropdownMenuEndIconDelegate.f9444c.setChecked(dropdownMenuEndIconDelegate.f9418m);
            DropdownMenuEndIconDelegate.this.f9424s.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DropdownMenuEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.d$d */
    /* loaded from: classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {
        d() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            DropdownMenuEndIconDelegate.this.f9444c.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* compiled from: DropdownMenuEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.d$e */
    /* loaded from: classes.dex */
    class e implements View.OnFocusChangeListener {
        e() {
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z10) {
            DropdownMenuEndIconDelegate.this.f9442a.setEndIconActivated(z10);
            if (z10) {
                return;
            }
            DropdownMenuEndIconDelegate.this.J(false);
            DropdownMenuEndIconDelegate.this.f9417l = false;
        }
    }

    /* compiled from: DropdownMenuEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.d$f */
    /* loaded from: classes.dex */
    class f extends TextInputLayout.e {
        f(TextInputLayout textInputLayout) {
            super(textInputLayout);
        }

        @Override // com.google.android.material.textfield.TextInputLayout.e, androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            if (!DropdownMenuEndIconDelegate.H(DropdownMenuEndIconDelegate.this.f9442a.getEditText())) {
                accessibilityNodeInfoCompat.V(Spinner.class.getName());
            }
            if (accessibilityNodeInfoCompat.H()) {
                accessibilityNodeInfoCompat.g0(null);
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
            AutoCompleteTextView C = DropdownMenuEndIconDelegate.C(DropdownMenuEndIconDelegate.this.f9442a.getEditText());
            if (accessibilityEvent.getEventType() == 1 && DropdownMenuEndIconDelegate.this.f9422q.isEnabled() && !DropdownMenuEndIconDelegate.H(DropdownMenuEndIconDelegate.this.f9442a.getEditText())) {
                DropdownMenuEndIconDelegate.this.M(C);
                DropdownMenuEndIconDelegate.this.N();
            }
        }
    }

    /* compiled from: DropdownMenuEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.d$g */
    /* loaded from: classes.dex */
    class g implements TextInputLayout.f {
        g() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.f
        public void a(TextInputLayout textInputLayout) {
            AutoCompleteTextView C = DropdownMenuEndIconDelegate.C(textInputLayout.getEditText());
            DropdownMenuEndIconDelegate.this.K(C);
            DropdownMenuEndIconDelegate.this.y(C);
            DropdownMenuEndIconDelegate.this.L(C);
            C.setThreshold(0);
            C.removeTextChangedListener(DropdownMenuEndIconDelegate.this.f9410e);
            C.addTextChangedListener(DropdownMenuEndIconDelegate.this.f9410e);
            textInputLayout.setEndIconCheckable(true);
            textInputLayout.setErrorIconDrawable((Drawable) null);
            if (!DropdownMenuEndIconDelegate.H(C) && DropdownMenuEndIconDelegate.this.f9422q.isTouchExplorationEnabled()) {
                ViewCompat.w0(DropdownMenuEndIconDelegate.this.f9444c, 2);
            }
            textInputLayout.setTextInputAccessibilityDelegate(DropdownMenuEndIconDelegate.this.f9412g);
            textInputLayout.setEndIconVisible(true);
        }
    }

    /* compiled from: DropdownMenuEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.d$h */
    /* loaded from: classes.dex */
    class h implements TextInputLayout.g {

        /* compiled from: DropdownMenuEndIconDelegate.java */
        /* renamed from: com.google.android.material.textfield.d$h$a */
        /* loaded from: classes.dex */
        class a implements Runnable {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ AutoCompleteTextView f9435e;

            a(AutoCompleteTextView autoCompleteTextView) {
                this.f9435e = autoCompleteTextView;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.f9435e.removeTextChangedListener(DropdownMenuEndIconDelegate.this.f9410e);
            }
        }

        h() {
        }

        @Override // com.google.android.material.textfield.TextInputLayout.g
        public void a(TextInputLayout textInputLayout, int i10) {
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
            if (autoCompleteTextView != null && i10 == 3) {
                autoCompleteTextView.post(new a(autoCompleteTextView));
                if (autoCompleteTextView.getOnFocusChangeListener() == DropdownMenuEndIconDelegate.this.f9411f) {
                    autoCompleteTextView.setOnFocusChangeListener(null);
                }
                autoCompleteTextView.setOnTouchListener(null);
                if (DropdownMenuEndIconDelegate.f9409t) {
                    autoCompleteTextView.setOnDismissListener(null);
                }
            }
            if (i10 == 3) {
                textInputLayout.removeOnAttachStateChangeListener(DropdownMenuEndIconDelegate.this.f9415j);
                DropdownMenuEndIconDelegate.this.I();
            }
        }
    }

    /* compiled from: DropdownMenuEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.d$i */
    /* loaded from: classes.dex */
    class i implements View.OnAttachStateChangeListener {
        i() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            DropdownMenuEndIconDelegate.this.B();
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            DropdownMenuEndIconDelegate.this.I();
        }
    }

    /* compiled from: DropdownMenuEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.d$j */
    /* loaded from: classes.dex */
    class j implements AccessibilityManagerCompat.b {
        j() {
        }

        @Override // androidx.core.view.accessibility.AccessibilityManagerCompat.b
        public void onTouchExplorationStateChanged(boolean z10) {
            AutoCompleteTextView autoCompleteTextView;
            TextInputLayout textInputLayout = DropdownMenuEndIconDelegate.this.f9442a;
            if (textInputLayout == null || (autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText()) == null || DropdownMenuEndIconDelegate.H(autoCompleteTextView)) {
                return;
            }
            ViewCompat.w0(DropdownMenuEndIconDelegate.this.f9444c, z10 ? 2 : 1);
        }
    }

    /* compiled from: DropdownMenuEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.d$k */
    /* loaded from: classes.dex */
    class k implements View.OnClickListener {
        k() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            DropdownMenuEndIconDelegate.this.M((AutoCompleteTextView) DropdownMenuEndIconDelegate.this.f9442a.getEditText());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DropdownMenuEndIconDelegate.java */
    /* renamed from: com.google.android.material.textfield.d$l */
    /* loaded from: classes.dex */
    public class l implements View.OnTouchListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ AutoCompleteTextView f9440e;

        l(AutoCompleteTextView autoCompleteTextView) {
            this.f9440e = autoCompleteTextView;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                if (DropdownMenuEndIconDelegate.this.G()) {
                    DropdownMenuEndIconDelegate.this.f9417l = false;
                }
                DropdownMenuEndIconDelegate.this.M(this.f9440e);
                DropdownMenuEndIconDelegate.this.N();
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DropdownMenuEndIconDelegate(TextInputLayout textInputLayout, int i10) {
        super(textInputLayout, i10);
        this.f9410e = new a();
        this.f9411f = new e();
        this.f9412g = new f(this.f9442a);
        this.f9413h = new g();
        this.f9414i = new h();
        this.f9415j = new i();
        this.f9416k = new j();
        this.f9417l = false;
        this.f9418m = false;
        this.f9419n = Long.MAX_VALUE;
    }

    private void A(AutoCompleteTextView autoCompleteTextView, int i10, int[][] iArr, MaterialShapeDrawable materialShapeDrawable) {
        LayerDrawable layerDrawable;
        int d10 = MaterialColors.d(autoCompleteTextView, R$attr.colorSurface);
        MaterialShapeDrawable materialShapeDrawable2 = new MaterialShapeDrawable(materialShapeDrawable.D());
        int h10 = MaterialColors.h(i10, d10, 0.1f);
        materialShapeDrawable2.a0(new ColorStateList(iArr, new int[]{h10, 0}));
        if (f9409t) {
            materialShapeDrawable2.setTint(d10);
            ColorStateList colorStateList = new ColorStateList(iArr, new int[]{h10, d10});
            MaterialShapeDrawable materialShapeDrawable3 = new MaterialShapeDrawable(materialShapeDrawable.D());
            materialShapeDrawable3.setTint(-1);
            layerDrawable = new LayerDrawable(new Drawable[]{new RippleDrawable(colorStateList, materialShapeDrawable2, materialShapeDrawable3), materialShapeDrawable});
        } else {
            layerDrawable = new LayerDrawable(new Drawable[]{materialShapeDrawable2, materialShapeDrawable});
        }
        ViewCompat.p0(autoCompleteTextView, layerDrawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void B() {
        TextInputLayout textInputLayout;
        if (this.f9422q == null || (textInputLayout = this.f9442a) == null || !ViewCompat.P(textInputLayout)) {
            return;
        }
        AccessibilityManagerCompat.a(this.f9422q, this.f9416k);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AutoCompleteTextView C(EditText editText) {
        if (editText instanceof AutoCompleteTextView) {
            return (AutoCompleteTextView) editText;
        }
        throw new RuntimeException("EditText needs to be an AutoCompleteTextView if an Exposed Dropdown Menu is being used.");
    }

    private ValueAnimator D(int i10, float... fArr) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.setInterpolator(AnimationUtils.f16555a);
        ofFloat.setDuration(i10);
        ofFloat.addUpdateListener(new d());
        return ofFloat;
    }

    private MaterialShapeDrawable E(float f10, float f11, float f12, int i10) {
        ShapeAppearanceModel m10 = ShapeAppearanceModel.a().J(f10).O(f10).w(f11).B(f11).m();
        MaterialShapeDrawable l10 = MaterialShapeDrawable.l(this.f9443b, f12);
        l10.setShapeAppearanceModel(m10);
        l10.c0(0, i10, 0, i10);
        return l10;
    }

    private void F() {
        this.f9424s = D(67, 0.0f, 1.0f);
        ValueAnimator D = D(50, 1.0f, 0.0f);
        this.f9423r = D;
        D.addListener(new c());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean G() {
        long currentTimeMillis = System.currentTimeMillis() - this.f9419n;
        return currentTimeMillis < 0 || currentTimeMillis > 300;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean H(EditText editText) {
        return editText.getKeyListener() != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void I() {
        AccessibilityManager accessibilityManager = this.f9422q;
        if (accessibilityManager != null) {
            AccessibilityManagerCompat.c(accessibilityManager, this.f9416k);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void J(boolean z10) {
        if (this.f9418m != z10) {
            this.f9418m = z10;
            this.f9424s.cancel();
            this.f9423r.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void K(AutoCompleteTextView autoCompleteTextView) {
        if (f9409t) {
            int boxBackgroundMode = this.f9442a.getBoxBackgroundMode();
            if (boxBackgroundMode == 2) {
                autoCompleteTextView.setDropDownBackgroundDrawable(this.f9421p);
            } else if (boxBackgroundMode == 1) {
                autoCompleteTextView.setDropDownBackgroundDrawable(this.f9420o);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"ClickableViewAccessibility"})
    public void L(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setOnTouchListener(new l(autoCompleteTextView));
        autoCompleteTextView.setOnFocusChangeListener(this.f9411f);
        if (f9409t) {
            autoCompleteTextView.setOnDismissListener(new b());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void M(AutoCompleteTextView autoCompleteTextView) {
        if (autoCompleteTextView == null) {
            return;
        }
        if (G()) {
            this.f9417l = false;
        }
        if (!this.f9417l) {
            if (f9409t) {
                J(!this.f9418m);
            } else {
                this.f9418m = !this.f9418m;
                this.f9444c.toggle();
            }
            if (this.f9418m) {
                autoCompleteTextView.requestFocus();
                autoCompleteTextView.showDropDown();
                return;
            } else {
                autoCompleteTextView.dismissDropDown();
                return;
            }
        }
        this.f9417l = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void N() {
        this.f9417l = true;
        this.f9419n = System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void y(AutoCompleteTextView autoCompleteTextView) {
        if (H(autoCompleteTextView)) {
            return;
        }
        int boxBackgroundMode = this.f9442a.getBoxBackgroundMode();
        MaterialShapeDrawable boxBackground = this.f9442a.getBoxBackground();
        int d10 = MaterialColors.d(autoCompleteTextView, R$attr.colorControlHighlight);
        int[][] iArr = {new int[]{R.attr.state_pressed}, new int[0]};
        if (boxBackgroundMode == 2) {
            A(autoCompleteTextView, d10, iArr, boxBackground);
        } else if (boxBackgroundMode == 1) {
            z(autoCompleteTextView, d10, iArr, boxBackground);
        }
    }

    private void z(AutoCompleteTextView autoCompleteTextView, int i10, int[][] iArr, MaterialShapeDrawable materialShapeDrawable) {
        int boxBackgroundColor = this.f9442a.getBoxBackgroundColor();
        int[] iArr2 = {MaterialColors.h(i10, boxBackgroundColor, 0.1f), boxBackgroundColor};
        if (f9409t) {
            ViewCompat.p0(autoCompleteTextView, new RippleDrawable(new ColorStateList(iArr, iArr2), materialShapeDrawable, materialShapeDrawable));
            return;
        }
        MaterialShapeDrawable materialShapeDrawable2 = new MaterialShapeDrawable(materialShapeDrawable.D());
        materialShapeDrawable2.a0(new ColorStateList(iArr, iArr2));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{materialShapeDrawable, materialShapeDrawable2});
        int C = ViewCompat.C(autoCompleteTextView);
        int paddingTop = autoCompleteTextView.getPaddingTop();
        int B = ViewCompat.B(autoCompleteTextView);
        int paddingBottom = autoCompleteTextView.getPaddingBottom();
        ViewCompat.p0(autoCompleteTextView, layerDrawable);
        ViewCompat.A0(autoCompleteTextView, C, paddingTop, B, paddingBottom);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void O(AutoCompleteTextView autoCompleteTextView) {
        if (!H(autoCompleteTextView) && this.f9442a.getBoxBackgroundMode() == 2 && (autoCompleteTextView.getBackground() instanceof LayerDrawable)) {
            y(autoCompleteTextView);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.EndIconDelegate
    public void a() {
        float dimensionPixelOffset = this.f9443b.getResources().getDimensionPixelOffset(R$dimen.mtrl_shape_corner_size_small_component);
        float dimensionPixelOffset2 = this.f9443b.getResources().getDimensionPixelOffset(R$dimen.mtrl_exposed_dropdown_menu_popup_elevation);
        int dimensionPixelOffset3 = this.f9443b.getResources().getDimensionPixelOffset(R$dimen.mtrl_exposed_dropdown_menu_popup_vertical_padding);
        MaterialShapeDrawable E = E(dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset3);
        MaterialShapeDrawable E2 = E(0.0f, dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset3);
        this.f9421p = E;
        StateListDrawable stateListDrawable = new StateListDrawable();
        this.f9420o = stateListDrawable;
        stateListDrawable.addState(new int[]{R.attr.state_above_anchor}, E);
        this.f9420o.addState(new int[0], E2);
        int i10 = this.f9445d;
        if (i10 == 0) {
            i10 = f9409t ? R$drawable.mtrl_dropdown_arrow : R$drawable.mtrl_ic_arrow_drop_down;
        }
        this.f9442a.setEndIconDrawable(i10);
        TextInputLayout textInputLayout = this.f9442a;
        textInputLayout.setEndIconContentDescription(textInputLayout.getResources().getText(R$string.exposed_dropdown_menu_content_description));
        this.f9442a.setEndIconOnClickListener(new k());
        this.f9442a.g(this.f9413h);
        this.f9442a.h(this.f9414i);
        F();
        this.f9422q = (AccessibilityManager) this.f9443b.getSystemService("accessibility");
        this.f9442a.addOnAttachStateChangeListener(this.f9415j);
        B();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.EndIconDelegate
    public boolean b(int i10) {
        return i10 != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.EndIconDelegate
    public boolean d() {
        return true;
    }
}
