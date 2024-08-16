package com.coui.appcompat.panel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.coui.appcompat.panel.COUIBottomSheetBehavior;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import com.support.appcompat.R$id;
import com.support.panel.R$attr;
import com.support.panel.R$bool;
import com.support.panel.R$color;
import com.support.panel.R$dimen;
import com.support.panel.R$drawable;
import com.support.panel.R$layout;
import com.support.panel.R$styleable;
import f2.COUINavigationBarUtil;
import f2.COUIPanelAdjustResizeHelper;
import f2.COUIPanelMultiWindowUtils;
import f2.COUIPanelPullUpListener;
import f2.COUIViewMarginUtil;
import h3.UIUtil;
import java.lang.ref.WeakReference;
import m1.COUIEaseInterpolator;
import m1.COUIInEaseInterpolator;
import m1.COUIOutEaseInterpolator;
import n3.SpringConfig;
import n3.SpringListener;
import n3.SpringSystem;
import s.DynamicAnimation;
import s.SpringAnimation;
import s.SpringForce;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* compiled from: COUIBottomSheetDialog.java */
/* renamed from: com.coui.appcompat.panel.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIBottomSheetDialog extends BottomSheetDialog implements DynamicAnimation.r, DynamicAnimation.q {
    private static final String W0 = COUIBottomSheetDialog.class.getSimpleName();
    private static final Interpolator X0;
    private static final Interpolator Y0;
    private static final Interpolator Z0;

    /* renamed from: a1, reason: collision with root package name */
    private static final Interpolator f6633a1;

    /* renamed from: b1, reason: collision with root package name */
    private static final Interpolator f6634b1;

    /* renamed from: c1, reason: collision with root package name */
    private static final Interpolator f6635c1;
    private Drawable A;
    private boolean A0;
    private int B;
    private boolean B0;
    private Drawable C;
    private boolean C0;
    private int D;
    private float D0;
    private WeakReference<Activity> E;
    private float E0;
    private boolean F;
    private View F0;
    private boolean G;
    private int G0;
    private boolean H;
    private int H0;
    private boolean I;
    private float I0;
    private int J;
    private float J0;
    private int K;
    private boolean K0;
    private int L;
    private SpringForce L0;
    private int M;
    private SpringAnimation M0;
    private int N;
    private boolean N0;
    private View O;
    private int O0;
    private n3.f P;
    private int P0;
    private n3.f Q;
    private t Q0;
    private int R;
    private s R0;
    private boolean S;
    private int S0;
    private boolean T;
    private int T0;
    private InputMethodManager U;
    private ComponentCallbacks U0;
    private AnimatorSet V;
    private ViewTreeObserver.OnPreDrawListener V0;
    private float W;
    private float X;
    private boolean Y;
    private View.OnApplyWindowInsetsListener Z;

    /* renamed from: a0, reason: collision with root package name */
    private COUIPanelPullUpListener f6636a0;

    /* renamed from: b0, reason: collision with root package name */
    private COUIPanelAdjustResizeHelper f6637b0;

    /* renamed from: c0, reason: collision with root package name */
    private WindowInsets f6638c0;

    /* renamed from: d0, reason: collision with root package name */
    private boolean f6639d0;

    /* renamed from: e0, reason: collision with root package name */
    private int f6640e0;

    /* renamed from: f0, reason: collision with root package name */
    private boolean f6641f0;

    /* renamed from: g0, reason: collision with root package name */
    private int f6642g0;

    /* renamed from: h0, reason: collision with root package name */
    private boolean f6643h0;

    /* renamed from: i0, reason: collision with root package name */
    private boolean f6644i0;

    /* renamed from: j0, reason: collision with root package name */
    private boolean f6645j0;

    /* renamed from: k0, reason: collision with root package name */
    private boolean f6646k0;

    /* renamed from: l0, reason: collision with root package name */
    private boolean f6647l0;

    /* renamed from: m0, reason: collision with root package name */
    private float f6648m0;

    /* renamed from: n0, reason: collision with root package name */
    private boolean f6649n0;

    /* renamed from: o0, reason: collision with root package name */
    private int f6650o0;

    /* renamed from: p0, reason: collision with root package name */
    private int f6651p0;

    /* renamed from: q0, reason: collision with root package name */
    private boolean f6652q0;

    /* renamed from: r0, reason: collision with root package name */
    private Configuration f6653r0;

    /* renamed from: s0, reason: collision with root package name */
    private r f6654s0;

    /* renamed from: t, reason: collision with root package name */
    private IgnoreWindowInsetsFrameLayout f6655t;

    /* renamed from: t0, reason: collision with root package name */
    private boolean f6656t0;

    /* renamed from: u, reason: collision with root package name */
    private View f6657u;

    /* renamed from: u0, reason: collision with root package name */
    private boolean f6658u0;

    /* renamed from: v, reason: collision with root package name */
    private View f6659v;

    /* renamed from: v0, reason: collision with root package name */
    private boolean f6660v0;

    /* renamed from: w, reason: collision with root package name */
    private COUIPanelPercentFrameLayout f6661w;

    /* renamed from: w0, reason: collision with root package name */
    private float f6662w0;

    /* renamed from: x, reason: collision with root package name */
    private View f6663x;

    /* renamed from: x0, reason: collision with root package name */
    private COUIPanelBarView f6664x0;

    /* renamed from: y, reason: collision with root package name */
    private COUIPanelContentLayout f6665y;

    /* renamed from: y0, reason: collision with root package name */
    private q f6666y0;

    /* renamed from: z, reason: collision with root package name */
    private ViewGroup f6667z;

    /* renamed from: z0, reason: collision with root package name */
    private boolean f6668z0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$a */
    /* loaded from: classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ boolean f6669a;

        a(boolean z10) {
            this.f6669a = z10;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            View findFocus;
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUIBottomSheetDialog.this.f6657u != null) {
                COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
                cOUIBottomSheetDialog.X = cOUIBottomSheetDialog.X0(floatValue);
                COUIBottomSheetDialog.this.f6657u.setAlpha(COUIBottomSheetDialog.this.X);
            }
            if (COUIBottomSheetDialog.this.f6657u != null && COUIPanelMultiWindowUtils.v(COUIBottomSheetDialog.this.getContext()) && ((COUIBottomSheetDialog.this.q1() || COUIBottomSheetDialog.this.p1()) && !COUIBottomSheetDialog.this.f6668z0)) {
                COUIBottomSheetDialog cOUIBottomSheetDialog2 = COUIBottomSheetDialog.this;
                cOUIBottomSheetDialog2.N1(cOUIBottomSheetDialog2.X);
            }
            if (COUIBottomSheetDialog.this.f6665y == null || !COUIBottomSheetDialog.this.f6652q0 || (findFocus = COUIBottomSheetDialog.this.f6665y.findFocus()) == null || !this.f6669a) {
                return;
            }
            COUIBottomSheetDialog.this.U.showSoftInput(findFocus, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$b */
    /* loaded from: classes.dex */
    public class b extends AnimatorListenerAdapter {
        b() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            if (COUIBottomSheetDialog.this.f6661w != null && COUIBottomSheetDialog.this.f6661w.getAlpha() == 0.0f) {
                COUIBottomSheetDialog.this.f6661w.setAlpha(1.0f);
            }
            COUIBottomSheetDialog.this.f6652q0 = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$c */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Window f6672a;

        c(Window window) {
            this.f6672a = window;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.f6672a.setNavigationBarColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }

    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$d */
    /* loaded from: classes.dex */
    class d implements ViewTreeObserver.OnPreDrawListener {
        d() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            COUIBottomSheetDialog.this.D1();
            if (COUIBottomSheetDialog.this.f6661w != null) {
                int T0 = COUIBottomSheetDialog.this.T0();
                if (COUIBottomSheetDialog.this.T) {
                    T0 = COUIBottomSheetDialog.this.R;
                }
                if ((COUIBottomSheetDialog.this.f6665y == null || COUIBottomSheetDialog.this.f6665y.findFocus() == null) && !COUIBottomSheetDialog.this.q1() && !COUIBottomSheetDialog.this.o1()) {
                    COUIBottomSheetDialog.this.f6661w.setTranslationY(T0);
                }
                COUIBottomSheetDialog.this.f6657u.setAlpha(0.0f);
                if (COUIBottomSheetDialog.this.f6661w.getRatio() == 2.0f) {
                    COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
                    cOUIBottomSheetDialog.M0(cOUIBottomSheetDialog.f6659v.getHeight() / 2, COUIBottomSheetDialog.this.Z0());
                } else {
                    COUIBottomSheetDialog cOUIBottomSheetDialog2 = COUIBottomSheetDialog.this;
                    cOUIBottomSheetDialog2.M0(0, cOUIBottomSheetDialog2.Z0());
                }
                return true;
            }
            COUIBottomSheetDialog cOUIBottomSheetDialog3 = COUIBottomSheetDialog.this;
            cOUIBottomSheetDialog3.M0(0, cOUIBottomSheetDialog3.Z0());
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$e */
    /* loaded from: classes.dex */
    public class e extends AnimatorListenerAdapter {
        e() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUIBottomSheetDialog.this.f6661w != null) {
                if (!COUIBottomSheetDialog.this.q1() && !COUIBottomSheetDialog.this.o1()) {
                    COUIBottomSheetDialog.this.f6661w.setTranslationY(COUIBottomSheetDialog.this.W);
                }
                if (COUIBottomSheetDialog.this.n() != null && COUIBottomSheetDialog.this.n().I() == 3 && COUIBottomSheetDialog.this.f6646k0) {
                    COUIBottomSheetDialog.this.f6661w.performHapticFeedback(14);
                }
            }
            if (COUIBottomSheetDialog.this.Q0 != null) {
                COUIBottomSheetDialog.this.Q0.a();
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUIBottomSheetDialog.this.n() == null || COUIBottomSheetDialog.this.n().I() != 5) {
                return;
            }
            ((COUIBottomSheetBehavior) COUIBottomSheetDialog.this.n()).Z0(3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$f */
    /* loaded from: classes.dex */
    public class f implements COUIPanelPullUpListener {

        /* renamed from: a, reason: collision with root package name */
        private int f6676a = -1;

        f() {
        }

        @Override // f2.COUIPanelPullUpListener
        public void a() {
            COUIBottomSheetDialog.this.Q1(0);
        }

        @Override // f2.COUIPanelPullUpListener
        public void b(int i10) {
            COUIBottomSheetDialog.this.K1(false);
            int top = COUIBottomSheetDialog.this.f6661w.getTop() - (i10 - COUIBottomSheetDialog.this.K);
            COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
            cOUIBottomSheetDialog.N0(cOUIBottomSheetDialog.K - top);
        }

        @Override // f2.COUIPanelPullUpListener
        public int c(int i10, int i11) {
            if (COUIBottomSheetDialog.this.P != null && COUIBottomSheetDialog.this.P.g() != UserProfileInfo.Constant.NA_LAT_LON) {
                COUIBottomSheetDialog.this.P.l();
                return COUIBottomSheetDialog.this.K;
            }
            int b10 = q.a.b((int) (COUIBottomSheetDialog.this.O.getPaddingBottom() - (i10 * 0.19999999f)), 0, Math.min(COUIBottomSheetDialog.this.J, COUIBottomSheetDialog.this.f6661w.getTop()));
            if (COUIBottomSheetDialog.this.K != b10) {
                COUIBottomSheetDialog.this.K = b10;
                COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
                cOUIBottomSheetDialog.Q1(cOUIBottomSheetDialog.K);
            }
            return COUIBottomSheetDialog.this.K;
        }

        @Override // f2.COUIPanelPullUpListener
        public void d() {
            boolean unused = COUIBottomSheetDialog.this.f6668z0;
        }

        @Override // f2.COUIPanelPullUpListener
        public void e() {
            boolean unused = COUIBottomSheetDialog.this.f6668z0;
        }

        @Override // f2.COUIPanelPullUpListener
        public void f(float f10) {
            if (this.f6676a == -1) {
                this.f6676a = COUIBottomSheetDialog.this.f6661w.getHeight();
            }
            if (COUIBottomSheetDialog.this.f6654s0 != null) {
                COUIBottomSheetDialog.this.f6654s0.a(COUIBottomSheetDialog.this.f6661w.getTop());
            }
            if (COUIBottomSheetDialog.this.f6656t0) {
                if (!COUIBottomSheetDialog.this.f6639d0) {
                    COUIBottomSheetDialog.this.f6657u.setAlpha(COUIBottomSheetDialog.this.X0(f10));
                    COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
                    cOUIBottomSheetDialog.X = cOUIBottomSheetDialog.X0(f10);
                }
                boolean z10 = !COUIPanelMultiWindowUtils.t(COUIBottomSheetDialog.this.getContext(), null);
                int i10 = Settings.Secure.getInt(COUIBottomSheetDialog.this.getContext().getContentResolver(), "hide_navigationbar_enable", 0);
                if (z10 && COUINavigationBarUtil.b(COUIBottomSheetDialog.this.getContext()) && COUIBottomSheetDialog.this.getWindow() != null && ((int) (COUIBottomSheetDialog.this.f6648m0 * f10)) != 0 && i10 != 3) {
                    COUIBottomSheetDialog.this.N1(f10);
                }
            }
            if (COUIBottomSheetDialog.this.f6664x0 == null || f10 == 1.0f || !COUIBottomSheetDialog.this.f6668z0) {
                return;
            }
            COUIBottomSheetDialog.this.f6664x0.setPanelOffset(this.f6676a - ((int) (COUIBottomSheetDialog.this.f6661w.getHeight() * f10)));
            this.f6676a = (int) (COUIBottomSheetDialog.this.f6661w.getHeight() * f10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$g */
    /* loaded from: classes.dex */
    public class g implements SpringListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f6678a;

        g(int i10) {
            this.f6678a = i10;
        }

        @Override // n3.SpringListener
        public void onSpringActivate(n3.f fVar) {
        }

        @Override // n3.SpringListener
        public void onSpringAtRest(n3.f fVar) {
            if ((COUIBottomSheetDialog.this.n() instanceof COUIBottomSheetBehavior) && COUIBottomSheetDialog.this.O != null) {
                COUIBottomSheetDialog.this.K = 0;
                COUIBottomSheetDialog.this.Q1(0);
                ((COUIBottomSheetBehavior) COUIBottomSheetDialog.this.n()).g0(3);
            }
            COUIBottomSheetDialog.this.K1(true);
        }

        @Override // n3.SpringListener
        public void onSpringEndStateChange(n3.f fVar) {
        }

        @Override // n3.SpringListener
        public void onSpringUpdate(n3.f fVar) {
            if (COUIBottomSheetDialog.this.P == null || COUIBottomSheetDialog.this.f6661w == null) {
                return;
            }
            if (fVar.s() && fVar.g() == UserProfileInfo.Constant.NA_LAT_LON) {
                COUIBottomSheetDialog.this.P.l();
                return;
            }
            int c10 = (int) fVar.c();
            COUIBottomSheetDialog.this.f6661w.offsetTopAndBottom(c10 - COUIBottomSheetDialog.this.L);
            COUIBottomSheetDialog.this.L = c10;
            COUIBottomSheetDialog.this.Q1(this.f6678a - c10);
        }
    }

    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$h */
    /* loaded from: classes.dex */
    class h implements ComponentCallbacks {
        h() {
        }

        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration configuration) {
            if (COUIBottomSheetDialog.this.f6649n0) {
                COUIBottomSheetDialog.this.h2(configuration);
            }
        }

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$i */
    /* loaded from: classes.dex */
    public class i extends COUIBottomSheetBehavior.i {
        i() {
        }

        @Override // com.coui.appcompat.panel.COUIBottomSheetBehavior.i
        public void a(View view, float f10) {
        }

        @Override // com.coui.appcompat.panel.COUIBottomSheetBehavior.i
        public void b(View view, int i10) {
            COUIBottomSheetDialog.this.b1(view, i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$j */
    /* loaded from: classes.dex */
    public class j implements Runnable {
        j() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (COUIBottomSheetDialog.this.c1()) {
                UIUtil.j(COUIBottomSheetDialog.this.f6661w, COUIBottomSheetDialog.this.getContext().getResources().getDimensionPixelOffset(R$dimen.coui_bottom_sheet_dialog_elevation), ContextCompat.c(COUIBottomSheetDialog.this.getContext(), R$color.coui_panel_follow_hand_spot_shadow_color));
                COUIBottomSheetDialog.this.K1(false);
                COUIBottomSheetDialog.this.n().T(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$k */
    /* loaded from: classes.dex */
    public class k implements View.OnClickListener {
        k() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIBottomSheetDialog.this.G && COUIBottomSheetDialog.this.isShowing() && COUIBottomSheetDialog.this.H) {
                COUIBottomSheetDialog.this.cancel();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$l */
    /* loaded from: classes.dex */
    public class l implements View.OnApplyWindowInsetsListener {
        l() {
        }

        @Override // android.view.View.OnApplyWindowInsetsListener
        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            if (view == null || view.getLayoutParams() == null) {
                return windowInsets;
            }
            COUIBottomSheetDialog.this.f1(windowInsets);
            COUIBottomSheetDialog.this.i1(windowInsets);
            if (COUIBottomSheetDialog.this.U == null) {
                COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
                cOUIBottomSheetDialog.U = (InputMethodManager) cOUIBottomSheetDialog.getContext().getSystemService("input_method");
            }
            boolean z10 = COUIBottomSheetDialog.this.getContext().getResources().getBoolean(R$bool.is_coui_bottom_sheet_ime_adjust_in_constraint_layout);
            ViewGroup viewGroup = (ViewGroup) COUIBottomSheetDialog.this.findViewById(R$id.design_bottom_sheet);
            ViewGroup viewGroup2 = (ViewGroup) COUIBottomSheetDialog.this.findViewById(com.support.panel.R$id.coui_panel_content_layout);
            if (z10) {
                viewGroup = viewGroup2;
            }
            ViewGroup viewGroup3 = COUIBottomSheetDialog.this.f6667z;
            COUIBottomSheetDialog cOUIBottomSheetDialog2 = COUIBottomSheetDialog.this;
            if (viewGroup3 != (z10 ? cOUIBottomSheetDialog2.f6665y : cOUIBottomSheetDialog2.f6661w)) {
                COUIViewMarginUtil.b(COUIBottomSheetDialog.this.f6667z, 3, 0);
            }
            COUIBottomSheetDialog cOUIBottomSheetDialog3 = COUIBottomSheetDialog.this;
            cOUIBottomSheetDialog3.f6667z = z10 ? cOUIBottomSheetDialog3.f6665y : cOUIBottomSheetDialog3.f6661w;
            if (COUIBottomSheetDialog.this.f6667z != null) {
                viewGroup = COUIBottomSheetDialog.this.f6667z;
            }
            ViewGroup viewGroup4 = viewGroup;
            if (COUIBottomSheetDialog.this.f6643h0) {
                COUIBottomSheetDialog.this.R0().a(COUIBottomSheetDialog.this.getContext(), viewGroup4, windowInsets, COUIBottomSheetDialog.this.f6659v, COUIBottomSheetDialog.this.f6658u0);
            }
            COUIBottomSheetDialog.this.f6638c0 = windowInsets;
            view.onApplyWindowInsets(COUIBottomSheetDialog.this.f6638c0);
            return COUIBottomSheetDialog.this.f6638c0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$m */
    /* loaded from: classes.dex */
    public class m extends AnimatorListenerAdapter {

        /* compiled from: COUIBottomSheetDialog.java */
        /* renamed from: com.coui.appcompat.panel.a$m$a */
        /* loaded from: classes.dex */
        class a extends AnimatorListenerAdapter {
            a() {
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                COUIBottomSheetDialog.this.f2();
            }
        }

        m() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            super.onAnimationCancel(animator);
            COUIBottomSheetDialog.this.f6639d0 = false;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            if (COUIBottomSheetDialog.this.f6666y0 != null) {
                COUIBottomSheetDialog.this.f6666y0.b();
            }
            COUIBottomSheetDialog.this.f6639d0 = false;
            if (COUIBottomSheetDialog.this.f6641f0) {
                COUIBottomSheetDialog cOUIBottomSheetDialog = COUIBottomSheetDialog.this;
                ValueAnimator E0 = cOUIBottomSheetDialog.E0(cOUIBottomSheetDialog.f6642g0);
                if (E0 != null) {
                    E0.addListener(new a());
                    E0.start();
                } else {
                    COUIBottomSheetDialog.this.f2();
                }
            } else {
                COUIBottomSheetDialog.this.f2();
            }
            COUIBottomSheetDialog.this.B1();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            COUIBottomSheetDialog.this.f6639d0 = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$n */
    /* loaded from: classes.dex */
    public class n extends AnimatorListenerAdapter {
        n() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUIBottomSheetDialog.this.f6639d0 = false;
            super.onAnimationCancel(animator);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUIBottomSheetDialog.this.f6666y0 != null) {
                COUIBottomSheetDialog.this.f6666y0.b();
            }
            COUIBottomSheetDialog.this.f6639d0 = false;
            COUIBottomSheetDialog.this.f2();
            COUIBottomSheetDialog.this.B1();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIBottomSheetDialog.this.f6639d0 = true;
            super.onAnimationStart(animator);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$o */
    /* loaded from: classes.dex */
    public class o implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ boolean f6688a;

        o(boolean z10) {
            this.f6688a = z10;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUIBottomSheetDialog.this.f6661w != null) {
                COUIBottomSheetDialog.this.f6661w.setAlpha(floatValue);
                if (this.f6688a) {
                    float f10 = (floatValue * 0.2f) + 0.8f;
                    COUIBottomSheetDialog.this.f6661w.setScaleX(f10);
                    COUIBottomSheetDialog.this.f6661w.setScaleY(f10);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$p */
    /* loaded from: classes.dex */
    public class p implements ValueAnimator.AnimatorUpdateListener {
        p() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (COUIBottomSheetDialog.this.f6661w != null) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                COUIBottomSheetDialog.this.f6661w.setTranslationY(floatValue);
                if (!COUIBottomSheetDialog.this.Y) {
                    COUIBottomSheetDialog.this.W = floatValue;
                }
                COUIBottomSheetDialog.this.Y = false;
            }
        }
    }

    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$q */
    /* loaded from: classes.dex */
    public interface q {
        void a();

        void b();
    }

    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$r */
    /* loaded from: classes.dex */
    public interface r {
        void a(float f10);
    }

    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$s */
    /* loaded from: classes.dex */
    public interface s {
        void a();
    }

    /* compiled from: COUIBottomSheetDialog.java */
    /* renamed from: com.coui.appcompat.panel.a$t */
    /* loaded from: classes.dex */
    public interface t {
        void a();
    }

    static {
        COUIInEaseInterpolator cOUIInEaseInterpolator = new COUIInEaseInterpolator();
        X0 = cOUIInEaseInterpolator;
        Y0 = new COUIEaseInterpolator();
        Z0 = new COUIInEaseInterpolator();
        f6633a1 = new COUIOutEaseInterpolator();
        f6634b1 = new COUIOutEaseInterpolator();
        f6635c1 = cOUIInEaseInterpolator;
    }

    public COUIBottomSheetDialog(Context context, int i10) {
        super(context, H1(context, i10));
        this.F = false;
        this.G = true;
        this.H = true;
        this.I = true;
        this.L = 0;
        this.M = 0;
        this.N = 0;
        this.R = 0;
        this.S = true;
        this.T = false;
        this.W = 0.0f;
        this.X = 0.0f;
        this.Y = false;
        this.Z = null;
        this.f6636a0 = null;
        this.f6640e0 = Integer.MAX_VALUE;
        this.f6644i0 = false;
        this.f6645j0 = false;
        this.f6646k0 = false;
        this.f6649n0 = true;
        this.f6652q0 = false;
        this.f6656t0 = true;
        this.f6658u0 = true;
        this.f6660v0 = true;
        this.f6662w0 = 333.0f;
        this.f6664x0 = null;
        this.f6666y0 = null;
        this.B0 = false;
        this.C0 = true;
        this.D0 = Float.MIN_VALUE;
        this.E0 = Float.MIN_VALUE;
        this.F0 = null;
        this.G0 = 0;
        this.H0 = -1;
        this.I0 = Float.MIN_VALUE;
        this.J0 = Float.MIN_VALUE;
        this.K0 = false;
        this.N0 = true;
        this.S0 = -1;
        this.T0 = -1;
        this.U0 = new h();
        this.V0 = new d();
        h1(i10);
        k1(i10);
        J1(context);
    }

    private int[] A0(View view) {
        int i10;
        int i11;
        ViewGroup viewGroup = (ViewGroup) view.getRootView();
        viewGroup.getChildAt(0);
        Rect V0 = V0(view);
        Rect rect = new Rect(0, 0, getContext().getResources().getDisplayMetrics().widthPixels, getContext().getResources().getDisplayMetrics().heightPixels);
        Rect V02 = V0(this.f6661w);
        WindowInsetsCompat E = ViewCompat.E(viewGroup);
        if (E != null) {
            this.O0 = E.l();
            this.P0 = E.j();
        }
        int measuredWidth = this.f6661w.getMeasuredWidth();
        int measuredHeight = this.f6661w.getMeasuredHeight();
        int dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.coui_bottom_sheet_dialog_follow_hand_margin_bottom);
        int dimensionPixelOffset2 = getContext().getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.coui_bottom_sheet_dialog_follow_hand_margin_left);
        int t12 = t1((((V0.left + V0.right) / 2) - (measuredWidth / 2)) - this.P0, rect.right - measuredWidth);
        if (t12 <= dimensionPixelOffset2) {
            t12 = dimensionPixelOffset2;
        } else {
            int i12 = t12 + measuredWidth + dimensionPixelOffset2;
            int i13 = rect.right;
            if (i12 >= i13) {
                t12 = (i13 - dimensionPixelOffset2) - measuredWidth;
            }
        }
        int i14 = rect.bottom;
        int i15 = i14 - measuredHeight;
        int i16 = rect.right - V0.right;
        int i17 = V0.left - rect.left;
        int i18 = V0.top;
        int i19 = i18 - rect.top;
        int i20 = this.M;
        int i21 = (i19 - i20) - dimensionPixelOffset;
        int i22 = t12;
        int i23 = V0.bottom;
        int i24 = i14 - i23;
        if (measuredHeight < i21) {
            i10 = t1(((((i18 - measuredHeight) - i20) + this.G0) - dimensionPixelOffset) - this.O0, i15);
        } else if (measuredHeight < i24) {
            i10 = t1((i23 - i20) + dimensionPixelOffset, i15);
        } else {
            int t13 = t1((((i23 + i18) / 2) - (measuredHeight / 2)) - this.O0, i15);
            if (measuredWidth < i17) {
                i11 = (V0.left - measuredWidth) - dimensionPixelOffset2;
            } else if (measuredWidth < i16) {
                i11 = V0.right + dimensionPixelOffset2;
            } else {
                i10 = t13;
            }
            i10 = t13;
            Log.d(W0, "calculateFinalLocationInScreen: \n anchorViewLocationRect = " + V0 + ", \n anchorContentViewLocationRect = " + rect + ", \n dialogViewLocalRect = " + V02 + "\n -> final : x = " + i11 + ", y = " + i10 + "\n -> insetTop: " + this.O0 + " maxY: " + i15);
            return new int[]{i11, i10};
        }
        i11 = i22;
        Log.d(W0, "calculateFinalLocationInScreen: \n anchorViewLocationRect = " + V0 + ", \n anchorContentViewLocationRect = " + rect + ", \n dialogViewLocalRect = " + V02 + "\n -> final : x = " + i11 + ", y = " + i10 + "\n -> insetTop: " + this.O0 + " maxY: " + i15);
        return new int[]{i11, i10};
    }

    private void A1() {
        Window window = getWindow();
        if (window != null) {
            window.getDecorView().setOnApplyWindowInsetsListener(null);
            this.Z = null;
        }
    }

    private void B0(Animator animator) {
        if (animator == null || !animator.isRunning()) {
            return;
        }
        animator.end();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void B1() {
        if (n() instanceof COUIBottomSheetBehavior) {
            ((COUIBottomSheetBehavior) n()).a1(null);
            this.f6636a0 = null;
        }
    }

    private void C0() {
        if (this.f6655t != null) {
            if (this.f6659v != null) {
                if (this.f6657u != null) {
                    if (this.f6661w == null) {
                        throw new IllegalArgumentException("design_bottom_sheet can not be null");
                    }
                    return;
                }
                throw new IllegalArgumentException("panel_outside can not be null");
            }
            throw new IllegalArgumentException("coordinator can not be null");
        }
        throw new IllegalArgumentException("container can not be null");
    }

    private void C1() {
        COUIPanelAdjustResizeHelper cOUIPanelAdjustResizeHelper = this.f6637b0;
        if (cOUIPanelAdjustResizeHelper != null) {
            cOUIPanelAdjustResizeHelper.b();
            this.f6637b0 = null;
        }
    }

    private ValueAnimator D0(boolean z10, PathInterpolator pathInterpolator) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(z10 ? 0.0f : 1.0f, z10 ? 1.0f : 0.0f);
        ofFloat.setDuration(167L);
        ofFloat.setInterpolator(pathInterpolator);
        ofFloat.addUpdateListener(new o(z10));
        return ofFloat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void D1() {
        View view = this.f6657u;
        if (view != null) {
            view.getViewTreeObserver().removeOnPreDrawListener(this.V0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ValueAnimator E0(int i10) {
        if (COUINavigationBarUtil.b(getContext()) && getWindow() != null) {
            Window window = getWindow();
            int navigationBarColor = window.getNavigationBarColor();
            if (Color.alpha(i10) == 0) {
                i10 = Color.argb(1, Color.red(i10), Color.green(i10), Color.blue(i10));
            }
            if (navigationBarColor != i10) {
                ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), Integer.valueOf(navigationBarColor), Integer.valueOf(i10));
                ofObject.setDuration(200L);
                ofObject.addUpdateListener(new c(window));
                return ofObject;
            }
        }
        return null;
    }

    private void E1(Configuration configuration) {
        getWindow().setNavigationBarColor(W0(configuration));
    }

    private ValueAnimator F0(boolean z10, float f10, PathInterpolator pathInterpolator) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.X, z10 ? 1.0f : 0.0f);
        ofFloat.setDuration(f10);
        ofFloat.setInterpolator(pathInterpolator);
        ofFloat.addUpdateListener(new a(z10));
        ofFloat.addListener(new b());
        return ofFloat;
    }

    private void F1(Configuration configuration) {
        if (this.f6661w == null) {
            return;
        }
        COUIPanelMultiWindowUtils.f(getContext(), configuration);
        COUIViewMarginUtil.b(this.f6661w, 3, 0);
    }

    private void G0() {
        COUIPanelContentLayout cOUIPanelContentLayout = (COUIPanelContentLayout) getLayoutInflater().inflate(this.f6668z0 ? R$layout.coui_panel_view_layout_tiny : R$layout.coui_panel_view_layout, (ViewGroup) null);
        Drawable drawable = this.A;
        if (drawable != null) {
            drawable.setTint(this.B);
            cOUIPanelContentLayout.setDragViewDrawable(this.A);
        }
        cOUIPanelContentLayout.e(null, COUIViewMarginUtil.a(this.f6659v, 3), this.f6638c0);
        this.f6665y = cOUIPanelContentLayout;
    }

    private void G1() {
        this.f6643h0 = true;
        int i10 = 0;
        this.f6652q0 = false;
        Window window = getWindow();
        R0().d(window.getAttributes().type);
        int i11 = window.getAttributes().softInputMode & 15;
        if (i11 != 5 || r1() || this.f6645j0) {
            i10 = i11;
        } else {
            this.f6652q0 = true;
        }
        window.setSoftInputMode(i10 | 16);
    }

    private ValueAnimator H0(int i10, int i11, float f10, PathInterpolator pathInterpolator) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(i10, i11);
        ofFloat.setDuration(f10);
        ofFloat.setInterpolator(pathInterpolator);
        ofFloat.addUpdateListener(new p());
        return ofFloat;
    }

    static int H1(Context context, int i10) {
        if (((i10 >>> 24) & 255) >= 1) {
            return i10;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R$attr.couiBottomSheetDialogStyle, typedValue, true);
        return typedValue.resourceId;
    }

    private void I1() {
        if (this.T0 == -1) {
            return;
        }
        try {
            Resources resources = getContext().getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.screenWidthDp = this.T0;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Log.d(W0, "restoreScreenWidth : PreferWidth=" + this.S0 + " ,OriginWidth=" + this.T0);
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
            if (cOUIPanelPercentFrameLayout != null) {
                cOUIPanelPercentFrameLayout.g();
            }
        } catch (Exception unused) {
            Log.d(W0, "restoreScreenWidth : failed to updateConfiguration");
        }
    }

    private void J0() {
        ValueAnimator E0 = this.f6641f0 ? E0(this.f6642g0) : null;
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200L);
        animatorSet.setInterpolator(f6635c1);
        animatorSet.addListener(new n());
        if (E0 == null) {
            animatorSet.playTogether(F0(false, 200.0f, (PathInterpolator) Y0));
        } else {
            animatorSet.playTogether(F0(false, 200.0f, (PathInterpolator) Y0), E0);
        }
        animatorSet.start();
    }

    private void J1(Context context) {
        if (context instanceof Activity) {
            this.E = new WeakReference<>((Activity) context);
        }
    }

    private void K0() {
        L0(new m());
    }

    private void L0(Animator.AnimatorListener animatorListener) {
        d2();
        int U0 = U0();
        if (U0 == 0) {
            return;
        }
        int height = (this.f6655t.getHeight() - this.f6661w.getTop()) + COUIViewMarginUtil.a(this.f6661w, 3);
        int i10 = (int) this.W;
        if (this.T && n().I() == 4) {
            height = this.R;
        }
        float f10 = i10 - height;
        float f11 = U0;
        float abs = Math.abs((133.0f * f10) / f11) + 200.0f;
        Interpolator interpolator = f6633a1;
        if (COUIPanelMultiWindowUtils.r(getContext(), null)) {
            abs = Math.abs((f10 * 117.0f) / f11) + 200.0f;
            interpolator = f6634b1;
        }
        this.V = new AnimatorSet();
        if (this.f6668z0) {
            Y1(i10, height, this.f6662w0, animatorListener);
            return;
        }
        if (q1()) {
            X1(animatorListener);
        } else if (o1()) {
            W1(animatorListener);
        } else {
            this.V.playTogether(H0(i10, height, abs, (PathInterpolator) interpolator), F0(false, abs, (PathInterpolator) Y0));
            V1(animatorListener);
        }
    }

    private void L1(View view) {
        if (!this.F) {
            Q0();
            this.f6665y.d();
            this.f6665y.a(view);
            super.setContentView(this.f6665y);
        } else {
            super.setContentView(view);
        }
        this.f6663x = view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void M0(int i10, Animator.AnimatorListener animatorListener) {
        d2();
        int U0 = U0();
        if (U0 == 0) {
            return;
        }
        int T0 = this.T ? this.R : T0() + i10;
        float f10 = T0 + 0;
        float f11 = U0;
        float abs = Math.abs((132.0f * f10) / f11) + 300.0f;
        Interpolator interpolator = X0;
        if (COUIPanelMultiWindowUtils.r(getContext(), null)) {
            abs = Math.abs((f10 * 150.0f) / f11) + 300.0f;
            interpolator = Z0;
        }
        this.V = new AnimatorSet();
        COUIPanelContentLayout cOUIPanelContentLayout = this.f6665y;
        if (cOUIPanelContentLayout != null && cOUIPanelContentLayout.findFocus() != null) {
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
            if (cOUIPanelPercentFrameLayout != null && cOUIPanelPercentFrameLayout.getAlpha() != 0.0f) {
                this.f6661w.setAlpha(0.0f);
            }
            this.V.playTogether(F0(true, abs, (PathInterpolator) Y0));
            Z1(animatorListener);
            return;
        }
        if (this.f6668z0) {
            c2(i10, animatorListener);
            return;
        }
        if (q1()) {
            b2(animatorListener);
        } else if (o1()) {
            a2(animatorListener);
        } else {
            this.V.playTogether(H0(T0, 0, abs, (PathInterpolator) interpolator), F0(true, abs, (PathInterpolator) Y0));
            Z1(animatorListener);
        }
    }

    private void M1() {
        if (Settings.Secure.getInt(getContext().getContentResolver(), "hide_navigationbar_enable", 0) == 3) {
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | 512);
            getWindow().setNavigationBarContrastEnforced(false);
            getWindow().setNavigationBarColor(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void N0(int i10) {
        n3.f c10 = SpringSystem.g().c();
        this.P = c10;
        c10.p(SpringConfig.a(6.0d, 42.0d));
        this.L = 0;
        this.P.a(new g(i10));
        this.P.o(i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void N1(float f10) {
        int i10 = (int) (f10 * this.f6648m0);
        if (i10 > 0) {
            getWindow().setNavigationBarColor(Color.argb(i10, 0, 0, 0));
        } else {
            getWindow().setNavigationBarColor(0);
            getWindow().setNavigationBarContrastEnforced(false);
        }
    }

    private void O0() {
        if (this.S0 == -1) {
            return;
        }
        try {
            Resources resources = getContext().getResources();
            Configuration configuration = resources.getConfiguration();
            this.T0 = configuration.screenWidthDp;
            configuration.screenWidthDp = this.S0;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Log.d(W0, "enforceChangeScreenWidth : OriginWidth=" + this.T0 + " ,PreferWidth:" + this.S0);
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
            if (cOUIPanelPercentFrameLayout != null) {
                cOUIPanelPercentFrameLayout.setPreferWidth(this.S0);
            }
        } catch (Exception unused) {
            Log.d(W0, "enforceChangeScreenWidth : failed to updateConfiguration");
        }
    }

    private void O1() {
        COUIPanelContentLayout cOUIPanelContentLayout = this.f6665y;
        if (cOUIPanelContentLayout != null) {
            ViewGroup.LayoutParams layoutParams = cOUIPanelContentLayout.getLayoutParams();
            int i10 = this.f6650o0;
            if (i10 != 0) {
                layoutParams.height = i10;
            }
            this.f6665y.setLayoutParams(layoutParams);
        }
        WindowInsets windowInsets = this.f6638c0;
        if (windowInsets != null) {
            i1(windowInsets);
        }
    }

    private void P0(Configuration configuration) {
        if (this.S0 == -1) {
            return;
        }
        try {
            Resources resources = getContext().getResources();
            this.T0 = configuration.screenWidthDp;
            configuration.screenWidthDp = this.S0;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Log.d(W0, "enforceChangeScreenWidth : OriginWidth=" + this.T0 + " ,PreferWidth:" + this.S0);
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
            if (cOUIPanelPercentFrameLayout != null) {
                cOUIPanelPercentFrameLayout.setPreferWidth(this.S0);
            }
        } catch (Exception unused) {
            Log.d(W0, "enforceChangeScreenWidth : failed to updateConfiguration");
        }
    }

    private void P1() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
        if (cOUIPanelPercentFrameLayout != null) {
            ViewGroup.LayoutParams layoutParams = cOUIPanelPercentFrameLayout.getLayoutParams();
            int i10 = this.f6651p0;
            if (i10 != 0) {
                layoutParams.width = i10;
            }
            this.f6661w.setLayoutParams(layoutParams);
        }
    }

    private void Q0() {
        if (this.f6665y == null) {
            G0();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Q1(int i10) {
        View view = this.O;
        if (view != null) {
            view.setPadding(view.getPaddingLeft(), this.O.getPaddingTop(), this.O.getPaddingRight(), i10);
        }
    }

    private void R1(float f10) {
        this.M0.l(f10);
    }

    private void S1(Window window) {
        if (window == null) {
            return;
        }
        View decorView = window.getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility() | 1024;
        window.setStatusBarColor(0);
        window.addFlags(Integer.MIN_VALUE);
        decorView.setSystemUiVisibility(COUIDarkModeUtil.a(getContext()) ? systemUiVisibility & (-8193) & (-17) : systemUiVisibility | 256);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int T0() {
        return this.f6661w.getMeasuredHeight() + COUIViewMarginUtil.a(this.f6661w, 3);
    }

    private void T1(Window window) {
    }

    private void U1() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
        if (cOUIPanelPercentFrameLayout != null) {
            this.H0 = cOUIPanelPercentFrameLayout.getBottom();
        }
        this.K0 = true;
        this.M0.n();
    }

    private Rect V0(View view) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        return new Rect(iArr[0], iArr[1], iArr[0] + view.getMeasuredWidth(), iArr[1] + view.getMeasuredHeight());
    }

    private void V1(Animator.AnimatorListener animatorListener) {
        if (animatorListener != null) {
            this.V.addListener(animatorListener);
        }
        this.V.start();
    }

    private int W0(Configuration configuration) {
        int i10 = this.f6640e0;
        if (i10 != Integer.MAX_VALUE) {
            return i10;
        }
        if (configuration == null) {
            return getContext().getResources().getColor(R$color.coui_panel_navigation_bar_color);
        }
        return getContext().createConfigurationContext(configuration).getResources().getColor(R$color.coui_panel_navigation_bar_color);
    }

    private void W1(Animator.AnimatorListener animatorListener) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
        if (cOUIPanelPercentFrameLayout != null && cOUIPanelPercentFrameLayout.getAlpha() != 1.0f) {
            this.f6661w.setAlpha(1.0f);
        }
        AnimatorSet animatorSet = this.V;
        Interpolator interpolator = Y0;
        animatorSet.playTogether(F0(false, 167.0f, (PathInterpolator) interpolator), D0(false, (PathInterpolator) interpolator));
        V1(animatorListener);
    }

    private void X1(Animator.AnimatorListener animatorListener) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
        if (cOUIPanelPercentFrameLayout != null && cOUIPanelPercentFrameLayout.getAlpha() != 1.0f) {
            this.f6661w.setAlpha(1.0f);
        }
        if (c1()) {
            this.V.playTogether(D0(false, (PathInterpolator) Y0));
        } else {
            AnimatorSet animatorSet = this.V;
            Interpolator interpolator = Y0;
            animatorSet.playTogether(F0(false, 167.0f, (PathInterpolator) interpolator), D0(false, (PathInterpolator) interpolator));
        }
        V1(animatorListener);
    }

    private COUIPanelPullUpListener Y0() {
        return new f();
    }

    private void Y1(int i10, int i11, float f10, Animator.AnimatorListener animatorListener) {
        this.V.playTogether(H0(i10, i11, this.f6662w0, new COUIOutEaseInterpolator()), F0(false, 183.0f, new COUIEaseInterpolator()));
        V1(animatorListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Animator.AnimatorListener Z0() {
        return new e();
    }

    private void Z1(Animator.AnimatorListener animatorListener) {
        if (animatorListener != null) {
            this.V.addListener(animatorListener);
        }
        this.V.start();
    }

    private Drawable a1(TypedArray typedArray, int i10, int i11) {
        Drawable drawable = typedArray != null ? typedArray.getDrawable(i10) : null;
        return drawable == null ? getContext().getResources().getDrawable(i11, getContext().getTheme()) : drawable;
    }

    private void a2(Animator.AnimatorListener animatorListener) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
        if (cOUIPanelPercentFrameLayout != null && cOUIPanelPercentFrameLayout.getAlpha() != 0.0f) {
            this.f6661w.setAlpha(0.0f);
            this.f6661w.setScaleX(0.8f);
            this.f6661w.setScaleY(0.8f);
        }
        g2();
        AnimatorSet animatorSet = this.V;
        Interpolator interpolator = Y0;
        animatorSet.playTogether(F0(true, 167.0f, (PathInterpolator) interpolator), D0(true, (PathInterpolator) interpolator));
        Z1(animatorListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b1(View view, int i10) {
        if (i10 == 2) {
            if (s1()) {
                d1();
            }
        } else if (i10 == 3) {
            this.f6643h0 = true;
            this.f6644i0 = false;
        } else {
            if (i10 != 5) {
                return;
            }
            dismiss();
        }
    }

    private void b2(Animator.AnimatorListener animatorListener) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
        if (cOUIPanelPercentFrameLayout != null && cOUIPanelPercentFrameLayout.getAlpha() != 0.0f) {
            this.f6661w.setAlpha(0.0f);
            this.f6661w.setScaleX(0.8f);
            this.f6661w.setScaleY(0.8f);
        }
        if (c1()) {
            u1();
            this.V.playTogether(D0(true, (PathInterpolator) Y0));
        } else {
            g2();
            AnimatorSet animatorSet = this.V;
            Interpolator interpolator = Y0;
            animatorSet.playTogether(F0(true, 167.0f, (PathInterpolator) interpolator), D0(true, (PathInterpolator) interpolator));
        }
        Z1(animatorListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean c1() {
        View view;
        if (this.f6661w == null || (view = this.F0) == null) {
            return false;
        }
        Rect V0 = V0(view);
        int measuredWidth = this.f6661w.getMeasuredWidth();
        int measuredHeight = this.f6661w.getMeasuredHeight();
        Rect V02 = V0(((ViewGroup) this.F0.getRootView()).getChildAt(0));
        int a10 = COUINavigationBarUtil.a(getContext());
        int dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.coui_bottom_sheet_dialog_follow_hand_margin_bottom);
        int dimensionPixelOffset2 = getContext().getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.coui_bottom_sheet_dialog_follow_hand_margin_right);
        if ((V0.left - measuredWidth) - dimensionPixelOffset2 <= V02.left && V0.right + measuredWidth + dimensionPixelOffset2 >= V02.right && ((V0.top - measuredHeight) - this.M) - dimensionPixelOffset <= V02.top && V0.bottom + measuredHeight + a10 + dimensionPixelOffset >= V02.bottom) {
            Log.d(W0, "anchor view have no enoughSpace anchorContentViewLocationRect: " + V02);
            this.f6661w.setHasAnchor(false);
            this.f6661w.setElevation(0.0f);
            this.f6657u.setAlpha(1.0f);
            return false;
        }
        Log.d(W0, "anchor view haveEnoughSpace");
        this.f6661w.setHasAnchor(true);
        this.f6661w.setTop(0);
        this.f6661w.setBottom(measuredHeight);
        UIUtil.j(this.f6661w, getContext().getResources().getDimensionPixelOffset(R$dimen.coui_bottom_sheet_dialog_elevation), ContextCompat.c(getContext(), R$color.coui_panel_follow_hand_spot_shadow_color));
        this.f6657u.setAlpha(0.0f);
        K1(false);
        n().T(false);
        return true;
    }

    private void c2(int i10, Animator.AnimatorListener animatorListener) {
        this.V.playTogether(F0(true, 167.0f, (PathInterpolator) Y0));
        R1(this.T ? this.R : T0() + i10);
        U1();
        Z1(animatorListener);
    }

    private void d1() {
        InputMethodManager inputMethodManager = this.U;
        if (inputMethodManager == null || !inputMethodManager.isActive()) {
            return;
        }
        if (getWindow() != null) {
            this.f6643h0 = false;
        }
        this.U.hideSoftInputFromWindow(this.f6661w.getWindowToken(), 0);
    }

    private void d2() {
        AnimatorSet animatorSet = this.V;
        if (animatorSet != null && animatorSet.isRunning()) {
            this.Y = true;
            this.V.end();
        }
        if (this.f6668z0 && this.K0) {
            this.M0.c();
        }
    }

    private void e1() {
        if (n() instanceof COUIBottomSheetBehavior) {
            COUIBottomSheetBehavior cOUIBottomSheetBehavior = (COUIBottomSheetBehavior) n();
            cOUIBottomSheetBehavior.L0(this.D0, this.E0);
            cOUIBottomSheetBehavior.U0(this.C0);
            cOUIBottomSheetBehavior.V0(this.f6668z0);
            cOUIBottomSheetBehavior.W0(this.R);
            cOUIBottomSheetBehavior.Y0(this.S);
            cOUIBottomSheetBehavior.Z0(this.T ? 4 : 3);
            cOUIBottomSheetBehavior.K0(new i());
            return;
        }
        throw new IllegalArgumentException("Must use COUIBottomSheetBehavior, check value of bottom_sheet_behavior in strings.xml");
    }

    private void e2() {
        n3.f fVar = this.Q;
        if (fVar == null || fVar.g() == UserProfileInfo.Constant.NA_LAT_LON) {
            return;
        }
        this.Q.l();
        this.Q = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f1(WindowInsets windowInsets) {
        View view = this.f6659v;
        if (view != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
            this.M = (int) getContext().getResources().getDimension(R$dimen.coui_panel_min_padding_top);
            if (this.f6668z0) {
                if (this.A0) {
                    this.M = (int) getContext().getResources().getDimension(R$dimen.coui_panel_min_padding_top_tiny_screen);
                } else {
                    this.M = (int) getContext().getResources().getDimension(R$dimen.coui_panel_normal_padding_top_tiny_screen);
                }
            }
            layoutParams.topMargin = this.M;
            this.f6659v.setLayoutParams(layoutParams);
            COUIPanelContentLayout cOUIPanelContentLayout = this.f6665y;
            if (cOUIPanelContentLayout != null) {
                cOUIPanelContentLayout.e(this.f6653r0, layoutParams.bottomMargin, windowInsets);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f2() {
        try {
            super.dismiss();
            s sVar = this.R0;
            if (sVar != null) {
                sVar.a();
            }
        } catch (Exception e10) {
            Log.e(W0, e10.getMessage(), e10);
        }
    }

    private void g1() {
        P1();
        O1();
    }

    private void g2() {
        int measuredHeight = this.f6659v.getMeasuredHeight();
        int max = (int) Math.max(0.0f, ((measuredHeight - (this.f6661w.getLayoutParams() instanceof ViewGroup.MarginLayoutParams ? ((ViewGroup.MarginLayoutParams) r1).bottomMargin : 0)) / this.f6661w.getRatio()) - (this.f6661w.getHeight() / this.f6661w.getRatio()));
        if (this.f6661w.getBottom() + max <= measuredHeight) {
            this.f6661w.setY(max);
        }
    }

    private void h1(int i10) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(null, R$styleable.COUIBottomSheetDialog, R$attr.couiBottomSheetDialogStyle, i10);
        this.A = a1(obtainStyledAttributes, R$styleable.COUIBottomSheetDialog_panelDragViewIcon, R$drawable.coui_panel_drag_view);
        this.B = obtainStyledAttributes.getColor(R$styleable.COUIBottomSheetDialog_panelDragViewTintColor, getContext().getResources().getColor(R$color.coui_panel_drag_view_color));
        this.C = a1(obtainStyledAttributes, R$styleable.COUIBottomSheetDialog_panelBackground, R$drawable.coui_panel_bg_without_shadow);
        this.D = obtainStyledAttributes.getColor(R$styleable.COUIBottomSheetDialog_panelBackgroundTintColor, COUIContextUtil.a(getContext(), com.support.appcompat.R$attr.couiColorSurface));
        obtainStyledAttributes.recycle();
        Drawable drawable = this.C;
        if (drawable != null) {
            drawable.setTint(this.D);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i1(WindowInsets windowInsets) {
        boolean z10 = this.f6650o0 >= COUIPanelMultiWindowUtils.i(getContext(), null, windowInsets);
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
        if (cOUIPanelPercentFrameLayout != null) {
            cOUIPanelPercentFrameLayout.getLayoutParams().height = (this.f6647l0 || z10) ? -1 : -2;
        }
        COUIPanelContentLayout cOUIPanelContentLayout = this.f6665y;
        if (cOUIPanelContentLayout != null) {
            if (this.f6647l0 || z10) {
                cOUIPanelContentLayout.getLayoutParams().height = -1;
            }
        }
    }

    private void i2(Configuration configuration, WindowInsets windowInsets) {
        if (windowInsets == null || configuration == null) {
            return;
        }
        ((ViewGroup.MarginLayoutParams) ((CoordinatorLayout.e) this.f6661w.getLayoutParams())).bottomMargin = COUIPanelMultiWindowUtils.g(getContext(), configuration, windowInsets);
    }

    private void k1(int i10) {
        this.J = (int) getContext().getResources().getDimension(R$dimen.coui_panel_pull_up_max_offset);
        this.M = (int) getContext().getResources().getDimension(R$dimen.coui_panel_min_padding_top);
        this.N = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_panel_normal_padding_top);
        this.f6648m0 = Color.alpha(getContext().getResources().getColor(com.support.appcompat.R$color.coui_color_mask));
    }

    private void l1() {
        this.f6655t = (IgnoreWindowInsetsFrameLayout) findViewById(com.support.panel.R$id.container);
        this.f6657u = findViewById(com.support.panel.R$id.panel_outside);
        this.f6659v = findViewById(com.support.panel.R$id.coordinator);
        this.f6661w = (COUIPanelPercentFrameLayout) findViewById(R$id.design_bottom_sheet);
        this.f6664x0 = (COUIPanelBarView) findViewById(com.support.panel.R$id.panel_drag_bar);
        this.f6661w.getLayoutParams().height = this.f6647l0 ? -1 : -2;
        if (q1()) {
            this.f6661w.post(new j());
        }
        COUIPanelContentLayout cOUIPanelContentLayout = this.f6665y;
        if (cOUIPanelContentLayout != null) {
            cOUIPanelContentLayout.setLayoutAtMaxHeight(this.f6647l0);
        }
        this.O = this.f6661w;
        C0();
        this.f6657u.setOnClickListener(new k());
        this.f6661w.setBackground(this.C);
    }

    private void m1() {
        Window window = getWindow();
        if (window != null) {
            window.setDimAmount(0.0f);
            window.setLayout(-1, -1);
            window.setGravity(80);
        }
    }

    private void n1() {
        if (this.N0 && getWindow() != null && this.Z == null) {
            View decorView = getWindow().getDecorView();
            l lVar = new l();
            this.Z = lVar;
            decorView.setOnApplyWindowInsetsListener(lVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean o1() {
        return this.f6661w.getRatio() == 2.0f && (n() == null || !(n() == null || n().I() == 4));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean p1() {
        return this.f6661w.getRatio() == 2.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean q1() {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout;
        return this.F0 != null && (cOUIPanelPercentFrameLayout = this.f6661w) != null && cOUIPanelPercentFrameLayout.getRatio() == 2.0f && this.F0.isAttachedToWindow();
    }

    private boolean r1() {
        WeakReference<Activity> weakReference = this.E;
        return (weakReference == null || weakReference.get() == null || !COUIPanelMultiWindowUtils.q(this.E.get())) ? false : true;
    }

    private boolean s1() {
        return ((COUIBottomSheetBehavior) n()).R0();
    }

    private int t1(int i10, int i11) {
        return Math.max(0, Math.min(i10, i11));
    }

    private void u1() {
        int[] A0 = A0(this.F0);
        this.f6661w.setX(A0[0]);
        this.f6661w.setY(A0[1]);
        this.W = this.f6661w.getY();
    }

    private void v1() {
        if (COUIPanelMultiWindowUtils.v(getContext())) {
            return;
        }
        F1(getContext().getResources().getConfiguration());
        E1(null);
    }

    private void w1() {
        getContext().registerComponentCallbacks(this.U0);
    }

    private void x1() {
        if (n() instanceof COUIBottomSheetBehavior) {
            this.f6636a0 = this.I ? Y0() : null;
            ((COUIBottomSheetBehavior) n()).a1(this.f6636a0);
        }
    }

    private void y1() {
        View view = this.f6657u;
        if (view != null) {
            view.getViewTreeObserver().addOnPreDrawListener(this.V0);
        }
    }

    private void z1() {
        if (this.U0 != null) {
            getContext().unregisterComponentCallbacks(this.U0);
        }
    }

    public void I0(boolean z10) {
        if (isShowing() && z10 && !this.f6639d0) {
            d1();
            if (n().I() == 5) {
                J0();
                return;
            } else {
                K0();
                return;
            }
        }
        f2();
    }

    public void K1(boolean z10) {
        if (this.I != z10) {
            this.I = z10;
            if (n() instanceof COUIBottomSheetBehavior) {
                this.f6636a0 = this.I ? Y0() : null;
                ((COUIBottomSheetBehavior) n()).a1(this.f6636a0);
            }
        }
    }

    public COUIPanelAdjustResizeHelper R0() {
        if (this.f6637b0 == null) {
            this.f6637b0 = new COUIPanelAdjustResizeHelper();
        }
        return this.f6637b0;
    }

    public View S0() {
        return this.f6663x;
    }

    public int U0() {
        View view = this.f6659v;
        if (view != null) {
            return view.getMeasuredHeight();
        }
        return 0;
    }

    float X0(float f10) {
        return !this.f6668z0 ? f10 : Math.max(0.0f, f10 - 0.5f) * 2.0f;
    }

    @Override // s.DynamicAnimation.r
    public void a(DynamicAnimation dynamicAnimation, float f10, float f11) {
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
        if (cOUIPanelPercentFrameLayout == null || this.H0 == -1) {
            return;
        }
        if (f10 < 0.0f) {
            cOUIPanelPercentFrameLayout.layout(cOUIPanelPercentFrameLayout.getLeft(), this.f6661w.getTop(), this.f6661w.getRight(), (int) (this.H0 - f10));
        }
        this.f6661w.setTranslationY(f10);
        if (!this.Y) {
            this.W = this.f6661w.getTranslationY();
        }
        this.Y = false;
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        e2();
        I0(true);
    }

    public void h2(Configuration configuration) {
        P0(configuration);
        this.f6653r0 = configuration;
        R0().c();
        F1(configuration);
        E1(configuration);
        M1();
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
        if (cOUIPanelPercentFrameLayout != null) {
            cOUIPanelPercentFrameLayout.j(configuration);
        }
        i2(configuration, this.f6638c0);
    }

    @Override // android.app.Dialog
    public void hide() {
        COUIPanelContentLayout cOUIPanelContentLayout;
        if (!this.F || (cOUIPanelContentLayout = this.f6665y) == null || cOUIPanelContentLayout.findFocus() == null) {
            super.hide();
        }
    }

    public void j1() {
        if (this.I0 == Float.MIN_VALUE) {
            this.I0 = 200.0f;
        }
        if (this.J0 == Float.MIN_VALUE) {
            this.J0 = 0.7f;
        }
        this.L0 = new SpringForce(0.0f).f(this.I0).d(this.J0);
        SpringAnimation v7 = new SpringAnimation(new s.e()).v(this.L0);
        this.M0 = v7;
        v7.b(this);
        this.M0.a(this);
    }

    @Override // s.DynamicAnimation.q
    public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z10, float f10, float f11) {
        this.K0 = false;
        COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = this.f6661w;
        if (cOUIPanelPercentFrameLayout != null && this.H0 != -1) {
            cOUIPanelPercentFrameLayout.layout(cOUIPanelPercentFrameLayout.getLeft(), this.f6661w.getTop(), this.f6661w.getRight(), this.H0);
        }
        this.H0 = -1;
        q qVar = this.f6666y0;
        if (qVar != null) {
            qVar.a();
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        O0();
        v1();
        G1();
        S1(getWindow());
        T1(getWindow());
        y1();
        w1();
        x1();
        n1();
        M1();
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, androidx.appcompat.app.AppCompatDialog, android.view.ComponentDialog, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f6653r0 = getContext().getResources().getConfiguration();
        int identifier = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            this.G0 = getContext().getResources().getDimensionPixelSize(identifier);
        }
        if (this.f6668z0) {
            j1();
        }
        e1();
        m1();
        g1();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        C1();
        A1();
        B0(this.V);
        z1();
        B1();
        I1();
        super.onDetachedFromWindow();
    }

    @Override // android.app.Dialog
    public void onRestoreInstanceState(Bundle bundle) {
        this.f6658u0 = bundle.getBoolean("state_focus_changes", this.f6658u0);
        super.onRestoreInstanceState(bundle);
    }

    @Override // android.app.Dialog
    public Bundle onSaveInstanceState() {
        Bundle onSaveInstanceState = super.onSaveInstanceState();
        onSaveInstanceState.putBoolean("state_focus_changes", this.f6658u0);
        return onSaveInstanceState;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, android.app.Dialog
    public void setCancelable(boolean z10) {
        super.setCancelable(z10);
        this.G = z10;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, android.app.Dialog
    public void setCanceledOnTouchOutside(boolean z10) {
        super.setCanceledOnTouchOutside(z10);
        if (z10 && !this.G) {
            this.G = true;
        }
        this.H = z10;
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public void setContentView(int i10) {
        setContentView(getLayoutInflater().inflate(i10, (ViewGroup) null));
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public void setContentView(View view) {
        if (view != null) {
            COUIThemeOverlay.i().b(getContext());
            L1(view);
            l1();
            return;
        }
        throw new IllegalArgumentException("ContentView can't be null");
    }
}
