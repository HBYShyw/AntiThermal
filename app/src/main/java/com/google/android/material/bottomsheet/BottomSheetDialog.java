package com.google.android.material.bottomsheet;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.t;
import c4.MaterialShapeDrawable;
import com.google.android.material.R$attr;
import com.google.android.material.R$id;
import com.google.android.material.R$layout;
import com.google.android.material.R$style;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import r3.MaterialColors;

/* compiled from: BottomSheetDialog.java */
/* renamed from: com.google.android.material.bottomsheet.a, reason: use source file name */
/* loaded from: classes.dex */
public class BottomSheetDialog extends AppCompatDialog {

    /* renamed from: i, reason: collision with root package name */
    private BottomSheetBehavior<FrameLayout> f8442i;

    /* renamed from: j, reason: collision with root package name */
    private FrameLayout f8443j;

    /* renamed from: k, reason: collision with root package name */
    private CoordinatorLayout f8444k;

    /* renamed from: l, reason: collision with root package name */
    private FrameLayout f8445l;

    /* renamed from: m, reason: collision with root package name */
    boolean f8446m;

    /* renamed from: n, reason: collision with root package name */
    boolean f8447n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f8448o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f8449p;

    /* renamed from: q, reason: collision with root package name */
    private BottomSheetBehavior.f f8450q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f8451r;

    /* renamed from: s, reason: collision with root package name */
    private BottomSheetBehavior.f f8452s;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BottomSheetDialog.java */
    /* renamed from: com.google.android.material.bottomsheet.a$a */
    /* loaded from: classes.dex */
    public class a implements t {
        a() {
        }

        @Override // androidx.core.view.t
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            if (BottomSheetDialog.this.f8450q != null) {
                BottomSheetDialog.this.f8442i.O(BottomSheetDialog.this.f8450q);
            }
            if (windowInsetsCompat != null) {
                BottomSheetDialog bottomSheetDialog = BottomSheetDialog.this;
                bottomSheetDialog.f8450q = new f(bottomSheetDialog.f8445l, windowInsetsCompat, null);
                BottomSheetDialog.this.f8442i.v(BottomSheetDialog.this.f8450q);
            }
            return windowInsetsCompat;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BottomSheetDialog.java */
    /* renamed from: com.google.android.material.bottomsheet.a$b */
    /* loaded from: classes.dex */
    public class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            BottomSheetDialog bottomSheetDialog = BottomSheetDialog.this;
            if (bottomSheetDialog.f8447n && bottomSheetDialog.isShowing() && BottomSheetDialog.this.p()) {
                BottomSheetDialog.this.cancel();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BottomSheetDialog.java */
    /* renamed from: com.google.android.material.bottomsheet.a$c */
    /* loaded from: classes.dex */
    public class c extends AccessibilityDelegateCompat {
        c() {
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            if (BottomSheetDialog.this.f8447n) {
                accessibilityNodeInfoCompat.a(1048576);
                accessibilityNodeInfoCompat.a0(true);
            } else {
                accessibilityNodeInfoCompat.a0(false);
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean performAccessibilityAction(View view, int i10, Bundle bundle) {
            if (i10 == 1048576) {
                BottomSheetDialog bottomSheetDialog = BottomSheetDialog.this;
                if (bottomSheetDialog.f8447n) {
                    bottomSheetDialog.cancel();
                    return true;
                }
            }
            return super.performAccessibilityAction(view, i10, bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BottomSheetDialog.java */
    /* renamed from: com.google.android.material.bottomsheet.a$d */
    /* loaded from: classes.dex */
    public class d implements View.OnTouchListener {
        d() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    }

    /* compiled from: BottomSheetDialog.java */
    /* renamed from: com.google.android.material.bottomsheet.a$e */
    /* loaded from: classes.dex */
    class e extends BottomSheetBehavior.f {
        e() {
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.f
        public void b(View view, float f10) {
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.f
        public void c(View view, int i10) {
            if (i10 == 5) {
                BottomSheetDialog.this.cancel();
            }
        }
    }

    /* compiled from: BottomSheetDialog.java */
    /* renamed from: com.google.android.material.bottomsheet.a$f */
    /* loaded from: classes.dex */
    private static class f extends BottomSheetBehavior.f {

        /* renamed from: a, reason: collision with root package name */
        private final boolean f8458a;

        /* renamed from: b, reason: collision with root package name */
        private final boolean f8459b;

        /* renamed from: c, reason: collision with root package name */
        private final WindowInsetsCompat f8460c;

        /* synthetic */ f(View view, WindowInsetsCompat windowInsetsCompat, a aVar) {
            this(view, windowInsetsCompat);
        }

        private void d(View view) {
            if (view.getTop() < this.f8460c.l()) {
                BottomSheetDialog.o(view, this.f8458a);
                view.setPadding(view.getPaddingLeft(), this.f8460c.l() - view.getTop(), view.getPaddingRight(), view.getPaddingBottom());
            } else if (view.getTop() != 0) {
                BottomSheetDialog.o(view, this.f8459b);
                view.setPadding(view.getPaddingLeft(), 0, view.getPaddingRight(), view.getPaddingBottom());
            }
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.f
        void a(View view) {
            d(view);
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.f
        public void b(View view, float f10) {
            d(view);
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.f
        public void c(View view, int i10) {
            d(view);
        }

        private f(View view, WindowInsetsCompat windowInsetsCompat) {
            ColorStateList p10;
            this.f8460c = windowInsetsCompat;
            boolean z10 = (view.getSystemUiVisibility() & 8192) != 0;
            this.f8459b = z10;
            MaterialShapeDrawable H = BottomSheetBehavior.E(view).H();
            if (H != null) {
                p10 = H.w();
            } else {
                p10 = ViewCompat.p(view);
            }
            if (p10 != null) {
                this.f8458a = MaterialColors.f(p10.getDefaultColor());
            } else if (view.getBackground() instanceof ColorDrawable) {
                this.f8458a = MaterialColors.f(((ColorDrawable) view.getBackground()).getColor());
            } else {
                this.f8458a = z10;
            }
        }
    }

    public BottomSheetDialog(Context context, int i10) {
        super(context, f(context, i10));
        this.f8447n = true;
        this.f8448o = true;
        this.f8452s = new e();
        h(1);
        this.f8451r = getContext().getTheme().obtainStyledAttributes(new int[]{R$attr.enableEdgeToEdge}).getBoolean(0, false);
    }

    private static int f(Context context, int i10) {
        if (i10 != 0) {
            return i10;
        }
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(R$attr.bottomSheetDialogTheme, typedValue, true)) {
            return typedValue.resourceId;
        }
        return R$style.Theme_Design_Light_BottomSheetDialog;
    }

    private FrameLayout m() {
        if (this.f8443j == null) {
            FrameLayout frameLayout = (FrameLayout) View.inflate(getContext(), R$layout.design_bottom_sheet_dialog, null);
            this.f8443j = frameLayout;
            this.f8444k = (CoordinatorLayout) frameLayout.findViewById(R$id.coordinator);
            FrameLayout frameLayout2 = (FrameLayout) this.f8443j.findViewById(R$id.design_bottom_sheet);
            this.f8445l = frameLayout2;
            BottomSheetBehavior<FrameLayout> E = BottomSheetBehavior.E(frameLayout2);
            this.f8442i = E;
            E.v(this.f8452s);
            this.f8442i.Y(this.f8447n);
        }
        return this.f8443j;
    }

    public static void o(View view, boolean z10) {
        int systemUiVisibility = view.getSystemUiVisibility();
        view.setSystemUiVisibility(z10 ? systemUiVisibility | 8192 : systemUiVisibility & (-8193));
    }

    private View q(int i10, View view, ViewGroup.LayoutParams layoutParams) {
        m();
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) this.f8443j.findViewById(R$id.coordinator);
        if (i10 != 0 && view == null) {
            view = getLayoutInflater().inflate(i10, (ViewGroup) coordinatorLayout, false);
        }
        if (this.f8451r) {
            ViewCompat.z0(this.f8445l, new a());
        }
        this.f8445l.removeAllViews();
        if (layoutParams == null) {
            this.f8445l.addView(view);
        } else {
            this.f8445l.addView(view, layoutParams);
        }
        coordinatorLayout.findViewById(R$id.touch_outside).setOnClickListener(new b());
        ViewCompat.l0(this.f8445l, new c());
        this.f8445l.setOnTouchListener(new d());
        return this.f8443j;
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void cancel() {
        BottomSheetBehavior<FrameLayout> n10 = n();
        if (this.f8446m && n10.I() != 5) {
            n10.f0(5);
        } else {
            super.cancel();
        }
    }

    public BottomSheetBehavior<FrameLayout> n() {
        if (this.f8442i == null) {
            m();
        }
        return this.f8442i;
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        if (window != null) {
            boolean z10 = this.f8451r && Color.alpha(window.getNavigationBarColor()) < 255;
            FrameLayout frameLayout = this.f8443j;
            if (frameLayout != null) {
                frameLayout.setFitsSystemWindows(!z10);
            }
            CoordinatorLayout coordinatorLayout = this.f8444k;
            if (coordinatorLayout != null) {
                coordinatorLayout.setFitsSystemWindows(!z10);
            }
            if (z10) {
                window.getDecorView().setSystemUiVisibility(768);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatDialog, android.view.ComponentDialog, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        if (window != null) {
            window.setStatusBarColor(0);
            window.addFlags(Integer.MIN_VALUE);
            window.setLayout(-1, -1);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ComponentDialog, android.app.Dialog
    public void onStart() {
        super.onStart();
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.f8442i;
        if (bottomSheetBehavior == null || bottomSheetBehavior.I() != 5) {
            return;
        }
        this.f8442i.f0(4);
    }

    boolean p() {
        if (!this.f8449p) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{R.attr.windowCloseOnTouchOutside});
            this.f8448o = obtainStyledAttributes.getBoolean(0, true);
            obtainStyledAttributes.recycle();
            this.f8449p = true;
        }
        return this.f8448o;
    }

    @Override // android.app.Dialog
    public void setCancelable(boolean z10) {
        super.setCancelable(z10);
        if (this.f8447n != z10) {
            this.f8447n = z10;
            BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.f8442i;
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.Y(z10);
            }
        }
    }

    @Override // android.app.Dialog
    public void setCanceledOnTouchOutside(boolean z10) {
        super.setCanceledOnTouchOutside(z10);
        if (z10 && !this.f8447n) {
            this.f8447n = true;
        }
        this.f8448o = z10;
        this.f8449p = true;
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public void setContentView(View view) {
        super.setContentView(q(0, view, null));
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        super.setContentView(q(0, view, layoutParams));
    }
}
