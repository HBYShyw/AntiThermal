package com.coui.appcompat.searchview;

import a3.COUITextViewCompatUtil;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.coui.appcompat.searchview.COUISearchViewAnimate;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$layout;
import com.support.appcompat.R$string;
import com.support.appcompat.R$styleable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import m1.COUIEaseInterpolator;
import m1.COUIMoveEaseInterpolator;
import m2.COUIShapePath;
import s2.CustomWindowInsetsAnimationControlListener;
import w1.COUIDarkModeUtil;
import z2.COUIChangeTextUtil;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class COUISearchViewAnimate extends LinearLayout implements CollapsibleActionView {
    private static final String[] B0 = {"TYPE_INSTANT_SEARCH", "TYPE_NON_INSTANT_SEARCH"};
    private static final Interpolator C0 = new COUIMoveEaseInterpolator();
    private static final Interpolator D0 = new COUIMoveEaseInterpolator();
    private static final Interpolator E0 = new COUIMoveEaseInterpolator();
    private static final Interpolator F0 = new COUIEaseInterpolator();
    private static final ArgbEvaluator G0 = new ArgbEvaluator();
    private boolean A;
    private int A0;
    private boolean B;
    private int C;
    private boolean D;
    private int E;
    private ImageView F;
    private ImageView G;
    private ImageView H;
    private boolean I;
    private boolean J;
    private int K;
    private int L;
    private int M;
    private int N;
    private int O;
    private int P;
    private int Q;
    private int R;
    private int S;
    private int T;
    private int U;
    private int V;
    private int W;

    /* renamed from: a0, reason: collision with root package name */
    private int f7440a0;

    /* renamed from: b0, reason: collision with root package name */
    private int f7441b0;

    /* renamed from: c0, reason: collision with root package name */
    private int f7442c0;

    /* renamed from: d0, reason: collision with root package name */
    private float f7443d0;

    /* renamed from: e, reason: collision with root package name */
    private final Path f7444e;

    /* renamed from: e0, reason: collision with root package name */
    private RectF f7445e0;

    /* renamed from: f, reason: collision with root package name */
    private final Path f7446f;

    /* renamed from: f0, reason: collision with root package name */
    private RectF f7447f0;

    /* renamed from: g, reason: collision with root package name */
    private final Paint f7448g;

    /* renamed from: g0, reason: collision with root package name */
    private Rect f7449g0;

    /* renamed from: h, reason: collision with root package name */
    private final Paint f7450h;

    /* renamed from: h0, reason: collision with root package name */
    private Rect f7451h0;

    /* renamed from: i, reason: collision with root package name */
    private final int[] f7452i;

    /* renamed from: i0, reason: collision with root package name */
    private Rect f7453i0;

    /* renamed from: j, reason: collision with root package name */
    private final int[] f7454j;

    /* renamed from: j0, reason: collision with root package name */
    private ObjectAnimator f7455j0;

    /* renamed from: k, reason: collision with root package name */
    private final ArgbEvaluator f7456k;

    /* renamed from: k0, reason: collision with root package name */
    private boolean f7457k0;

    /* renamed from: l, reason: collision with root package name */
    private final RectF f7458l;

    /* renamed from: l0, reason: collision with root package name */
    private boolean f7459l0;

    /* renamed from: m, reason: collision with root package name */
    private final Rect f7460m;

    /* renamed from: m0, reason: collision with root package name */
    private boolean f7461m0;

    /* renamed from: n, reason: collision with root package name */
    private Context f7462n;

    /* renamed from: n0, reason: collision with root package name */
    private AnimatorSet f7463n0;

    /* renamed from: o, reason: collision with root package name */
    private ImageView f7464o;

    /* renamed from: o0, reason: collision with root package name */
    private ValueAnimator f7465o0;

    /* renamed from: p, reason: collision with root package name */
    private COUISearchView f7466p;

    /* renamed from: p0, reason: collision with root package name */
    private ValueAnimator f7467p0;

    /* renamed from: q, reason: collision with root package name */
    private SearchFunctionalButton f7468q;

    /* renamed from: q0, reason: collision with root package name */
    private ValueAnimator f7469q0;

    /* renamed from: r, reason: collision with root package name */
    private ImageView f7470r;

    /* renamed from: r0, reason: collision with root package name */
    private AnimatorSet f7471r0;

    /* renamed from: s, reason: collision with root package name */
    private ConstraintLayout f7472s;

    /* renamed from: s0, reason: collision with root package name */
    private ValueAnimator f7473s0;

    /* renamed from: t, reason: collision with root package name */
    private volatile s f7474t;

    /* renamed from: t0, reason: collision with root package name */
    private ValueAnimator f7475t0;

    /* renamed from: u, reason: collision with root package name */
    public AtomicInteger f7476u;

    /* renamed from: u0, reason: collision with root package name */
    private ValueAnimator f7477u0;

    /* renamed from: v, reason: collision with root package name */
    private List<u> f7478v;

    /* renamed from: v0, reason: collision with root package name */
    private boolean f7479v0;

    /* renamed from: w, reason: collision with root package name */
    private t f7480w;

    /* renamed from: w0, reason: collision with root package name */
    private int f7481w0;

    /* renamed from: x, reason: collision with root package name */
    private MenuItem f7482x;

    /* renamed from: x0, reason: collision with root package name */
    private Interpolator f7483x0;

    /* renamed from: y, reason: collision with root package name */
    private COUIToolbar f7484y;

    /* renamed from: y0, reason: collision with root package name */
    private boolean f7485y0;

    /* renamed from: z, reason: collision with root package name */
    private int f7486z;

    /* renamed from: z0, reason: collision with root package name */
    private u f7487z0;

    /* loaded from: classes.dex */
    public static class COUISavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<COUISavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        float f7488e;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<COUISavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public COUISavedState createFromParcel(Parcel parcel) {
                return new COUISavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public COUISavedState[] newArray(int i10) {
                return new COUISavedState[i10];
            }
        }

        public COUISavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeFloat(this.f7488e);
        }

        public COUISavedState(Parcel parcel) {
            super(parcel);
            this.f7488e = parcel.readFloat();
        }
    }

    /* loaded from: classes.dex */
    public static class SearchFunctionalButton extends AppCompatButton {

        /* renamed from: h, reason: collision with root package name */
        a f7489h;

        /* renamed from: i, reason: collision with root package name */
        volatile boolean f7490i;

        /* loaded from: classes.dex */
        public interface a {
            void a();
        }

        public SearchFunctionalButton(Context context) {
            this(context, null);
        }

        @Override // android.view.View
        public boolean performClick() {
            if (this.f7489h != null) {
                this.f7490i = true;
                this.f7489h.a();
            }
            return super.performClick();
        }

        public void setPerformClickCallback(a aVar) {
            this.f7489h = aVar;
        }

        public void setPerformClicked(boolean z10) {
            this.f7490i = z10;
        }

        public SearchFunctionalButton(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, 0);
        }

        public SearchFunctionalButton(Context context, AttributeSet attributeSet, int i10) {
            super(context, attributeSet, i10);
            this.f7489h = null;
            this.f7490i = false;
            setMaxLines(1);
            setMaxWidth(context.getResources().getDimensionPixelOffset(R$dimen.coui_search_function_button_max_width));
            setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            COUISearchViewAnimate.this.B = false;
            COUISearchViewAnimate.this.setToolBarChildVisibility(0);
            COUISearchViewAnimate.this.p0();
            COUISearchViewAnimate.this.f7468q.setVisibility(4);
            COUISearchViewAnimate.this.f7470r.setVisibility(4);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements Animator.AnimatorListener {
        b() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUISearchViewAnimate.this.Q = 0;
            COUISearchViewAnimate cOUISearchViewAnimate = COUISearchViewAnimate.this;
            cOUISearchViewAnimate.R = cOUISearchViewAnimate.getTop();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements Animator.AnimatorListener {
        c() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUISearchViewAnimate.this.s0();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISearchViewAnimate.this.s0();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {
        d() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUISearchViewAnimate.this.f7442c0 == 0) {
                COUISearchViewAnimate.this.f7468q.setAlpha(floatValue);
            } else if (COUISearchViewAnimate.this.f7442c0 == 1) {
                COUISearchViewAnimate.this.f7470r.setAlpha(floatValue);
                COUISearchViewAnimate.this.f7468q.setAlpha(floatValue);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements Animator.AnimatorListener {
        e() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUISearchViewAnimate.this.f7442c0 == 1) {
                COUISearchViewAnimate.this.f7457k0 = true;
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUISearchViewAnimate.this.f7442c0 == 1) {
                COUISearchViewAnimate.this.f7470r.setVisibility(0);
            }
            COUISearchViewAnimate.this.f7468q.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f implements Animator.AnimatorListener {
        f() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUISearchViewAnimate.this.s0();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISearchViewAnimate.this.Q = 0;
            if (COUISearchViewAnimate.this.f7442c0 == 1) {
                COUISearchViewAnimate.this.f7457k0 = true;
            }
            COUISearchViewAnimate.this.s0();
            COUISearchViewAnimate.this.o0();
            COUISearchViewAnimate.this.getAnimatorHelper().f7509a.set(false);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUISearchViewAnimate.this.Q = 0;
            if (COUISearchViewAnimate.this.f7442c0 == 1) {
                COUISearchViewAnimate.this.f7470r.setVisibility(0);
            }
            COUISearchViewAnimate.this.f7468q.setVisibility(0);
            COUISearchViewAnimate.this.f7476u.set(1);
            if (!COUISearchViewAnimate.this.f7479v0 || COUISearchViewAnimate.this.f7481w0 == 0 || COUISearchViewAnimate.this.b0()) {
                COUISearchViewAnimate.this.p0();
                COUISearchViewAnimate.this.k0(true);
            }
            COUISearchViewAnimate.this.h0(0, 1);
            COUISearchViewAnimate cOUISearchViewAnimate = COUISearchViewAnimate.this;
            cOUISearchViewAnimate.R = cOUISearchViewAnimate.getTop();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class g implements ValueAnimator.AnimatorUpdateListener {
        g() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUISearchViewAnimate.this.f7442c0 == 0) {
                int i10 = (int) (floatValue * (COUISearchViewAnimate.this.R - COUISearchViewAnimate.this.S));
                ((ViewGroup.MarginLayoutParams) COUISearchViewAnimate.this.getLayoutParams()).topMargin += i10 - COUISearchViewAnimate.this.Q;
                COUISearchViewAnimate.this.requestLayout();
                COUISearchViewAnimate.this.Q = i10;
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
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUISearchViewAnimate.this.f7442c0 == 0) {
                COUISearchViewAnimate.this.M = (int) ((1.0f - floatValue) * (r0.getOriginWidth() - COUISearchViewAnimate.this.getShrinkWidth()));
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) COUISearchViewAnimate.this.f7466p.getLayoutParams();
                marginLayoutParams.setMarginEnd(COUISearchViewAnimate.this.M);
                COUISearchViewAnimate.this.f7466p.setLayoutParams(marginLayoutParams);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class i implements ValueAnimator.AnimatorUpdateListener {
        i() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUISearchViewAnimate.this.f7442c0 == 0) {
                COUISearchViewAnimate.this.f7468q.setAlpha(1.0f - floatValue);
            } else if (COUISearchViewAnimate.this.f7442c0 == 1) {
                float f10 = 1.0f - floatValue;
                COUISearchViewAnimate.this.f7470r.setAlpha(f10);
                COUISearchViewAnimate.this.f7468q.setAlpha(f10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class j implements Animator.AnimatorListener {
        j() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUISearchViewAnimate.this.s0();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISearchViewAnimate.this.Q = 0;
            if (COUISearchViewAnimate.this.f7442c0 == 1) {
                COUISearchViewAnimate.this.f7457k0 = false;
                COUISearchViewAnimate.this.f7470r.setVisibility(8);
                COUISearchViewAnimate.this.f7468q.setVisibility(8);
            } else if (COUISearchViewAnimate.this.f7442c0 == 0) {
                COUISearchViewAnimate.this.f7468q.setVisibility(4);
            }
            COUISearchViewAnimate.this.s0();
            COUISearchViewAnimate.this.p0();
            COUISearchViewAnimate.this.getAnimatorHelper().f7509a.set(false);
            COUISearchViewAnimate.this.f7466p.setQuery("", false);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUISearchViewAnimate.this.Q = 0;
            COUISearchViewAnimate.this.f7466p.getSearchAutoComplete().setText((CharSequence) null);
            COUISearchViewAnimate.this.f7466p.getSearchAutoComplete().clearFocus();
            COUISearchViewAnimate.this.o0();
            COUISearchViewAnimate.this.k0(false);
            COUISearchViewAnimate.this.f7476u.set(0);
            COUISearchViewAnimate.this.h0(1, 0);
        }
    }

    /* loaded from: classes.dex */
    class k implements u {
        k() {
        }

        @Override // com.coui.appcompat.searchview.COUISearchViewAnimate.u
        public void onStateChange(int i10, int i11) {
            if (i11 == 1) {
                COUISearchViewAnimate.this.q0();
            } else if (i11 == 0) {
                COUISearchViewAnimate.this.Q();
            }
        }
    }

    /* loaded from: classes.dex */
    class l implements TextWatcher {
        l() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if ("".contentEquals(editable)) {
                COUISearchViewAnimate.this.G.setVisibility(0);
                COUISearchViewAnimate.this.H.setVisibility(0);
            } else {
                COUISearchViewAnimate.this.G.setVisibility(8);
                COUISearchViewAnimate.this.H.setVisibility(8);
            }
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
    public class m implements Runnable {
        m() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUISearchViewAnimate.this.f7468q.getHitRect(COUISearchViewAnimate.this.f7460m);
            COUISearchViewAnimate.this.f7460m.right += COUISearchViewAnimate.this.getPaddingStart();
            COUISearchViewAnimate.this.f7460m.left += COUISearchViewAnimate.this.getPaddingStart();
            COUISearchViewAnimate.this.f7460m.top += COUISearchViewAnimate.this.f7472s.getTop();
            COUISearchViewAnimate.this.f7460m.bottom += COUISearchViewAnimate.this.f7472s.getTop();
            float max = Float.max(0.0f, COUISearchViewAnimate.this.f7472s.getMeasuredHeight() - COUISearchViewAnimate.this.f7460m.height());
            float f10 = max / 2.0f;
            COUISearchViewAnimate.this.f7460m.top = (int) (r1.top - f10);
            COUISearchViewAnimate.this.f7460m.bottom = (int) (r4.bottom + f10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class n implements Runnable {
        n() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) COUISearchViewAnimate.this.f7466p.getLayoutParams();
            marginLayoutParams.setMarginEnd(COUISearchViewAnimate.this.getOriginWidth() - COUISearchViewAnimate.this.getShrinkWidth());
            COUISearchViewAnimate.this.f7466p.setLayoutParams(marginLayoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class o implements ValueAnimator.AnimatorUpdateListener {
        o() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISearchViewAnimate.this.setToolBarAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class p extends AnimatorListenerAdapter {
        p() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            COUISearchViewAnimate.this.B = false;
            COUISearchViewAnimate.this.o0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class q extends AnimatorListenerAdapter {
        q() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            COUISearchViewAnimate.this.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class r implements ValueAnimator.AnimatorUpdateListener {
        r() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISearchViewAnimate.this.setToolBarAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class s {

        /* renamed from: a, reason: collision with root package name */
        private volatile AtomicBoolean f7509a = new AtomicBoolean(false);

        /* renamed from: b, reason: collision with root package name */
        private Runnable f7510b = new a();

        /* renamed from: c, reason: collision with root package name */
        private Runnable f7511c = new b();

        /* renamed from: d, reason: collision with root package name */
        private Runnable f7512d = new c();

        /* renamed from: e, reason: collision with root package name */
        private Runnable f7513e = new d();

        /* loaded from: classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (COUISearchViewAnimate.this.I) {
                    COUISearchViewAnimate.this.p0();
                    COUISearchViewAnimate.this.k0(true);
                }
                COUISearchViewAnimate.this.I = true;
                if (COUISearchViewAnimate.this.f7480w != null) {
                    COUISearchViewAnimate.this.f7480w.b(1);
                }
                COUISearchViewAnimate.this.h0(0, 1);
            }
        }

        /* loaded from: classes.dex */
        class b implements Runnable {
            b() {
            }

            @Override // java.lang.Runnable
            public void run() {
                COUISearchViewAnimate.this.o0();
                s.this.f7509a.set(false);
                if (COUISearchViewAnimate.this.f7480w != null) {
                    COUISearchViewAnimate.this.f7480w.a(1);
                }
            }
        }

        /* loaded from: classes.dex */
        class c implements Runnable {
            c() {
            }

            @Override // java.lang.Runnable
            public void run() {
                COUISearchViewAnimate.this.o0();
                COUISearchViewAnimate.this.k0(false);
                if (COUISearchViewAnimate.this.f7480w != null) {
                    COUISearchViewAnimate.this.f7480w.b(0);
                }
                COUISearchViewAnimate.this.h0(1, 0);
            }
        }

        /* loaded from: classes.dex */
        class d implements Runnable {
            d() {
            }

            @Override // java.lang.Runnable
            public void run() {
                COUISearchViewAnimate.this.p0();
                s.this.f7509a.set(false);
                COUISearchViewAnimate.this.f7466p.setQuery("", false);
                if (COUISearchViewAnimate.this.f7480w != null) {
                    COUISearchViewAnimate.this.f7480w.a(0);
                }
            }
        }

        s() {
        }

        public void b(int i10) {
            if (COUISearchViewAnimate.this.f7476u.get() == i10) {
                Log.d("COUISearchViewAnimate", "runStateChangeAnimation: same state , return. targetState = " + i10);
                return;
            }
            if (i10 == 1) {
                c();
            } else if (i10 == 0) {
                d();
            }
        }

        void c() {
            synchronized (this) {
                if (this.f7509a.compareAndSet(false, true)) {
                    if (COUISearchViewAnimate.this.f7479v0 && COUISearchViewAnimate.this.f7481w0 != 0 && !COUISearchViewAnimate.this.b0()) {
                        COUISearchViewAnimate.this.p0();
                        COUISearchViewAnimate.this.k0(true);
                    } else {
                        COUISearchViewAnimate.this.f7476u.set(1);
                        COUISearchViewAnimate.this.f7463n0.start();
                    }
                }
            }
        }

        void d() {
            synchronized (this) {
                if (this.f7509a.compareAndSet(false, true)) {
                    COUISearchViewAnimate.this.f7471r0.start();
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public interface t {
        void a(int i10);

        void b(int i10);
    }

    /* loaded from: classes.dex */
    public interface u {
        void onStateChange(int i10, int i11);
    }

    public COUISearchViewAnimate(Context context) {
        this(context, null);
    }

    private float M(float f10) {
        return Math.max(0.0f, Math.min(1.0f, f10 / 0.3f));
    }

    private float N(float f10) {
        return (f10 / 0.7f) - 0.42857146f;
    }

    private void O() {
        if (this.A) {
            return;
        }
        this.A = true;
        if (this.f7484y != null) {
            n0();
            COUIToolbar.LayoutParams layoutParams = new COUIToolbar.LayoutParams(-1, this.f7484y.getHeight() - this.f7484y.getPaddingTop());
            layoutParams.f320a = this.f7486z;
            this.f7484y.n(this, layoutParams);
        }
    }

    private List P(List list) {
        return list == null ? new ArrayList() : list;
    }

    private void R(Context context, AttributeSet attributeSet) {
        LinearLayout.inflate(context, R$layout.coui_search_view_animate_layout, this);
        this.f7464o = (ImageView) findViewById(R$id.animated_search_icon);
        this.f7466p = (COUISearchView) findViewById(R$id.animated_search_view);
        this.f7468q = (SearchFunctionalButton) findViewById(R$id.animated_cancel_button);
        this.f7470r = (ImageView) findViewById(R$id.button_divider);
        this.f7472s = (ConstraintLayout) findViewById(R$id.coui_search_view_wrapper);
    }

    private void S() {
        V();
        W();
        T();
        U();
        X();
        Y();
    }

    private void T() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f7469q0 = ofFloat;
        ofFloat.setDuration(this.f7442c0 == 0 ? 350L : 100L);
        this.f7469q0.setInterpolator(E0);
        this.f7469q0.setStartDelay(this.f7442c0 != 0 ? 0L : 100L);
        this.f7469q0.addUpdateListener(new d());
        this.f7469q0.addListener(new e());
    }

    private void U() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f7477u0 = ofFloat;
        ofFloat.setDuration(this.f7442c0 == 0 ? 350L : 100L);
        this.f7477u0.setInterpolator(E0);
        this.f7477u0.addUpdateListener(new i());
    }

    private void V() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f7465o0 = ofFloat;
        ofFloat.setDuration(450L);
        this.f7465o0.setInterpolator(C0);
        this.f7465o0.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: s2.a
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUISearchViewAnimate.this.e0(valueAnimator);
            }
        });
        this.f7465o0.addListener(new b());
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f7467p0 = ofFloat2;
        ofFloat2.setDuration(450L);
        this.f7467p0.setInterpolator(D0);
        this.f7467p0.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: s2.b
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUISearchViewAnimate.this.f0(valueAnimator);
            }
        });
        this.f7467p0.addListener(new c());
    }

    private void W() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f7473s0 = ofFloat;
        ofFloat.setDuration(450L);
        ValueAnimator valueAnimator = this.f7473s0;
        Interpolator interpolator = C0;
        valueAnimator.setInterpolator(interpolator);
        this.f7473s0.addUpdateListener(new g());
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f7475t0 = ofFloat2;
        ofFloat2.setDuration(450L);
        this.f7475t0.setInterpolator(interpolator);
        this.f7475t0.addUpdateListener(new h());
    }

    private void X() {
        AnimatorSet animatorSet = new AnimatorSet();
        this.f7463n0 = animatorSet;
        animatorSet.addListener(new f());
        this.f7463n0.playTogether(this.f7465o0, this.f7467p0, this.f7469q0);
    }

    private void Y() {
        AnimatorSet animatorSet = new AnimatorSet();
        this.f7471r0 = animatorSet;
        animatorSet.addListener(new j());
        this.f7471r0.playTogether(this.f7473s0, this.f7475t0, this.f7477u0);
    }

    private boolean Z(float f10, float f11) {
        return this.f7458l.contains(f10, f11);
    }

    private boolean a0(float f10, float f11) {
        getGlobalVisibleRect(this.f7449g0);
        this.G.getGlobalVisibleRect(this.f7451h0);
        this.H.getGlobalVisibleRect(this.f7453i0);
        this.f7451h0.offset(0, -this.f7449g0.top);
        this.f7453i0.offset(0, -this.f7449g0.top);
        int i10 = (int) f10;
        int i11 = (int) f11;
        return this.f7451h0.contains(i10, i11) || this.f7453i0.contains(i10, i11);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b0() {
        Context context = this.f7462n;
        if (context instanceof Activity) {
            return ((Activity) context).isInMultiWindowMode();
        }
        return false;
    }

    private boolean c0(float f10, float f11) {
        float f12 = (int) f10;
        float f13 = (int) f11;
        return this.f7445e0.contains(f12, f13) || this.f7447f0.contains(f12, f13);
    }

    private boolean d0() {
        return getLayoutDirection() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void e0(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (this.f7442c0 == 0) {
            int i10 = (int) (floatValue * (this.R - this.S));
            ((ViewGroup.MarginLayoutParams) getLayoutParams()).topMargin -= i10 - this.Q;
            requestLayout();
            this.Q = i10;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f0(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (this.f7442c0 == 0) {
            this.M = (int) (floatValue * (getOriginWidth() - getShrinkWidth()));
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.f7466p.getLayoutParams();
            marginLayoutParams.setMarginEnd(this.M);
            this.f7466p.setLayoutParams(marginLayoutParams);
        }
    }

    private void g0(Context context, AttributeSet attributeSet, int i10, int i11) {
        Drawable drawable;
        if (attributeSet != null) {
            int styleAttribute = attributeSet.getStyleAttribute();
            this.E = styleAttribute;
            if (styleAttribute == 0) {
                this.E = i10;
            }
        } else {
            this.E = i10;
        }
        this.L = context.getResources().getDimensionPixelOffset(R$dimen.coui_search_view_background_end_gap);
        this.K = context.getResources().getDimensionPixelOffset(R$dimen.coui_search_view_background_start_gap);
        this.O = context.getResources().getDimensionPixelOffset(R$dimen.coui_search_view_cancel_margin_small);
        this.P = context.getResources().getDimensionPixelOffset(R$dimen.coui_search_view_cancel_margin_large);
        this.T = context.getResources().getDimensionPixelSize(R$dimen.coui_search_view_collapsed_min_height);
        this.U = context.getResources().getDimensionPixelOffset(R$dimen.coui_search_view_wrapper_height);
        this.V = context.getResources().getDimensionPixelOffset(R$dimen.coui_search_view_animate_height);
        if (this.f7445e0 == null) {
            this.f7445e0 = new RectF();
        }
        if (this.f7447f0 == null) {
            this.f7447f0 = new RectF();
        }
        if (this.f7449g0 == null) {
            this.f7449g0 = new Rect();
        }
        if (this.f7451h0 == null) {
            this.f7451h0 = new Rect();
        }
        if (this.f7453i0 == null) {
            this.f7453i0 = new Rect();
        }
        this.f7440a0 = context.getResources().getColor(R$color.coui_search_view_selector_color_normal);
        this.f7441b0 = context.getResources().getColor(R$color.coui_search_view_selector_color_pressed);
        this.W = this.f7440a0;
        this.N = context.getResources().getColor(R$color.coui_color_divider);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUISearchViewAnimate, i10, i11);
        float f10 = context.getResources().getConfiguration().fontScale;
        this.f7466p.getSearchAutoComplete().setTextSize(0, obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUISearchViewAnimate_inputTextSize, getResources().getDimensionPixelSize(R$dimen.coui_search_view_input_text_size)));
        SearchView.SearchAutoComplete searchAutoComplete = this.f7466p.getSearchAutoComplete();
        searchAutoComplete.setPaddingRelative(0, 0, getResources().getDimensionPixelSize(R$dimen.coui_search_view_auto_complete_padding_end), 0);
        searchAutoComplete.setTextColor(obtainStyledAttributes.getColor(R$styleable.COUISearchViewAnimate_inputTextColor, 0));
        this.f7464o.setImageDrawable(MaterialResources.e(context, obtainStyledAttributes, R$styleable.COUISearchViewAnimate_couiSearchIcon));
        this.f7466p.getSearchAutoComplete().setHintTextColor(obtainStyledAttributes.getColorStateList(R$styleable.COUISearchViewAnimate_normalHintColor));
        this.f7442c0 = obtainStyledAttributes.getInt(R$styleable.COUISearchViewAnimate_couiSearchViewAnimateType, 0);
        int i12 = R$styleable.COUISearchViewAnimate_searchHint;
        if (obtainStyledAttributes.hasValue(i12)) {
            setQueryHint(obtainStyledAttributes.getString(i12));
        }
        int i13 = R$styleable.COUISearchViewAnimate_functionalButtonTextColor;
        if (obtainStyledAttributes.hasValue(i13)) {
            this.f7468q.setTextColor(obtainStyledAttributes.getColor(i13, 0));
        }
        int i14 = R$styleable.COUISearchViewAnimate_functionalButtonText;
        if (obtainStyledAttributes.hasValue(i14)) {
            this.f7468q.setText(obtainStyledAttributes.getString(i14));
        } else {
            this.f7468q.setText(R$string.coui_search_view_cancel);
        }
        this.f7468q.setTextSize(0, COUIChangeTextUtil.e(this.f7468q.getTextSize(), f10, 2));
        COUITextViewCompatUtil.b(this.f7468q);
        int i15 = R$styleable.COUISearchViewAnimate_buttonDivider;
        if (obtainStyledAttributes.hasValue(i15) && (drawable = obtainStyledAttributes.getDrawable(i15)) != null) {
            this.f7470r.setImageDrawable(drawable);
        }
        this.f7466p.setBackgroundColor(obtainStyledAttributes.getColor(R$styleable.COUISearchViewAnimate_searchBackground, 0));
        this.G = (ImageView) this.f7466p.findViewById(R$id.search_main_icon_btn);
        this.H = (ImageView) this.f7466p.findViewById(R$id.search_sub_icon_btn);
        this.G.setImageDrawable(MaterialResources.e(context, obtainStyledAttributes, R$styleable.COUISearchViewAnimate_couiSearchViewMainIcon));
        this.H.setImageDrawable(MaterialResources.e(context, obtainStyledAttributes, R$styleable.COUISearchViewAnimate_couiSearchViewSubIcon));
        this.F = (ImageView) this.f7466p.findViewById(R$id.search_close_btn);
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.COUISearchViewAnimate_couiSearchClearSelector, 0);
        ImageView imageView = this.F;
        if (imageView != null) {
            imageView.setImageResource(resourceId);
        }
        int i16 = obtainStyledAttributes.getInt(R$styleable.COUISearchViewAnimate_android_gravity, 16);
        Log.i("COUISearchViewAnimate", "gravity " + i16);
        setGravity(i16);
        obtainStyledAttributes.recycle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public s getAnimatorHelper() {
        if (this.f7474t == null) {
            synchronized (this) {
                if (this.f7474t == null) {
                    this.f7474t = new s();
                }
            }
        }
        return this.f7474t;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getOriginWidth() {
        return (((getMeasuredWidth() - getPaddingStart()) - getPaddingEnd()) - this.K) - this.L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getShrinkWidth() {
        int i10 = this.f7442c0;
        if (i10 == 0) {
            return ((getOriginWidth() - this.P) - this.f7468q.getMeasuredWidth()) + this.f7468q.getPaddingEnd();
        }
        if (i10 == 1) {
            return (((getOriginWidth() - this.O) - this.L) - this.f7468q.getMeasuredWidth()) - this.f7470r.getMeasuredWidth();
        }
        return getOriginWidth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h0(int i10, int i11) {
        List<u> list = this.f7478v;
        if (list != null) {
            for (u uVar : list) {
                if (uVar != null) {
                    uVar.onStateChange(i10, i11);
                }
            }
        }
    }

    private void i0() {
        getAnimatorHelper().b(0);
    }

    private void j0() {
        getAnimatorHelper().b(1);
    }

    private void l0() {
        ObjectAnimator objectAnimator = this.f7455j0;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.f7455j0.cancel();
        }
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "currentBackgroundColor", this.W, this.f7441b0);
        this.f7455j0 = ofInt;
        ofInt.setDuration(150L);
        this.f7455j0.setInterpolator(F0);
        this.f7455j0.setEvaluator(G0);
        this.f7455j0.start();
    }

    private void m0() {
        ObjectAnimator objectAnimator = this.f7455j0;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.f7455j0.cancel();
        }
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "currentBackgroundColor", this.W, this.f7440a0);
        this.f7455j0 = ofInt;
        ofInt.setDuration(150L);
        this.f7455j0.setInterpolator(F0);
        this.f7455j0.setEvaluator(G0);
        this.f7455j0.start();
    }

    private void n0() {
        int childCount = this.f7484y.getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            if (getClass().isInstance(this.f7484y.getChildAt(i10))) {
                this.f7484y.removeViewAt(i10);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o0() {
        SearchView.SearchAutoComplete searchAutoComplete;
        COUISearchView cOUISearchView = this.f7466p;
        if (cOUISearchView == null || (searchAutoComplete = cOUISearchView.getSearchAutoComplete()) == null) {
            return;
        }
        searchAutoComplete.setFocusable(true);
        searchAutoComplete.setFocusableInTouchMode(true);
        searchAutoComplete.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p0() {
        COUISearchView cOUISearchView = this.f7466p;
        if (cOUISearchView != null) {
            cOUISearchView.clearFocus();
            this.f7466p.setFocusable(false);
            this.f7466p.onWindowFocusChanged(false);
            SearchView.SearchAutoComplete searchAutoComplete = this.f7466p.getSearchAutoComplete();
            if (searchAutoComplete != null) {
                searchAutoComplete.setFocusable(false);
            }
        }
    }

    private void r0() {
        CustomWindowInsetsAnimationControlListener customWindowInsetsAnimationControlListener = new CustomWindowInsetsAnimationControlListener(true, this.f7481w0, this.f7483x0);
        COUISearchView cOUISearchView = this.f7466p;
        if (cOUISearchView == null || cOUISearchView.getWindowInsetsController() == null) {
            return;
        }
        this.f7466p.getWindowInsetsController().controlWindowInsetsAnimation(WindowInsets.Type.ime(), this.f7481w0, this.f7483x0, null, customWindowInsetsAnimationControlListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s0() {
        if (this.f7457k0) {
            if (d0()) {
                this.f7445e0.right = this.f7472s.getRight();
                this.f7445e0.left = this.f7470r.getRight() + this.f7472s.getLeft();
            } else {
                this.f7445e0.left = this.f7472s.getLeft();
                this.f7445e0.right = this.f7470r.getLeft() + this.f7445e0.left;
            }
            this.f7445e0.top = this.f7472s.getTop();
            this.f7445e0.bottom = this.f7472s.getBottom();
            this.f7459l0 = true;
            if (d0()) {
                RectF rectF = this.f7447f0;
                rectF.right = this.f7445e0.left;
                rectF.left = this.f7472s.getLeft();
            } else {
                RectF rectF2 = this.f7447f0;
                rectF2.left = this.f7445e0.right;
                rectF2.right = this.f7472s.getRight();
            }
            RectF rectF3 = this.f7447f0;
            RectF rectF4 = this.f7445e0;
            rectF3.top = rectF4.top;
            rectF3.bottom = rectF4.bottom;
            this.f7461m0 = true;
            return;
        }
        if (d0()) {
            this.f7445e0.right = this.f7472s.getRight();
            int i10 = this.f7442c0;
            if (i10 == 0) {
                this.f7445e0.left = this.f7466p.getLeft() + getPaddingEnd();
            } else if (i10 == 1) {
                this.f7445e0.left = this.f7472s.getLeft();
            }
        } else {
            this.f7445e0.left = this.f7472s.getLeft();
            int i11 = this.f7442c0;
            if (i11 == 0) {
                this.f7445e0.right = this.f7466p.getRight() + getPaddingStart();
            } else if (i11 == 1) {
                this.f7445e0.right = this.f7472s.getRight();
            }
        }
        this.f7445e0.top = this.f7472s.getTop();
        this.f7445e0.bottom = this.f7472s.getBottom();
        this.f7459l0 = true;
    }

    private void setCurrentBackgroundColor(int i10) {
        this.W = i10;
        invalidate();
    }

    private void setMenuItem(MenuItem menuItem) {
        this.f7482x = menuItem;
        if (menuItem == null || menuItem.getActionView() != this) {
            return;
        }
        this.f7482x.setActionView((View) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setToolBarAlpha(float f10) {
        COUIToolbar cOUIToolbar = this.f7484y;
        if (cOUIToolbar != null) {
            int childCount = cOUIToolbar.getChildCount();
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = this.f7484y.getChildAt(i10);
                if (childAt != this) {
                    childAt.setAlpha(f10);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setToolBarChildVisibility(int i10) {
        COUIToolbar cOUIToolbar = this.f7484y;
        if (cOUIToolbar != null) {
            int childCount = cOUIToolbar.getChildCount();
            for (int i11 = 0; i11 < childCount; i11++) {
                View childAt = this.f7484y.getChildAt(i11);
                if (childAt != this) {
                    childAt.setVisibility(i10);
                }
            }
        }
    }

    private void t0() {
        this.f7468q.getLocationOnScreen(this.f7452i);
        getLocationOnScreen(this.f7454j);
        RectF rectF = this.f7458l;
        int[] iArr = this.f7452i;
        rectF.set(iArr[0], iArr[1] - this.f7454j[1], iArr[0] + this.f7468q.getWidth(), (this.f7452i[1] - this.f7454j[1]) + this.f7468q.getHeight());
        this.f7468q.post(new m());
    }

    private void u0() {
        RectF rectF = this.f7445e0;
        float f10 = (rectF.bottom - rectF.top) / 2.0f;
        boolean d02 = d0();
        if (this.f7461m0) {
            COUIShapePath.b(this.f7446f, this.f7447f0, f10, d02, !d02, d02, !d02);
            this.f7461m0 = false;
        }
        if (this.f7459l0) {
            if (this.f7457k0) {
                COUIShapePath.b(this.f7444e, this.f7445e0, f10, !d02, d02, !d02, d02);
            } else {
                COUIShapePath.b(this.f7444e, this.f7445e0, f10, true, true, true, true);
            }
            this.f7459l0 = false;
        }
    }

    public void K(u uVar) {
        List<u> P = P(this.f7478v);
        this.f7478v = P;
        P.add(uVar);
    }

    public void L(int i10) {
        if (this.f7476u.get() == i10) {
            Log.d("COUISearchViewAnimate", "changeStateWithAnimation: same state , return. targetState = " + i10);
            return;
        }
        if (this.f7476u.get() == 1) {
            i0();
        } else if (this.f7476u.get() == 0) {
            j0();
        }
    }

    public void Q() {
        if (this.B) {
            return;
        }
        this.B = true;
        O();
        if (this.C == 1) {
            animate().alpha(0.0f).setDuration(150L).setListener(new q()).start();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(150L);
        ofFloat.addUpdateListener(new r());
        ofFloat.addListener(new a());
        ofFloat.start();
        if (this.J) {
            k0(false);
        }
    }

    @Override // android.view.ViewGroup
    public void addView(View view) {
        super.addView(view);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return super.dispatchTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1 && action != 3) {
                if (!c0(motionEvent.getX(), motionEvent.getY()) && this.f7485y0) {
                    this.f7485y0 = false;
                    m0();
                }
            } else if (c0(motionEvent.getX(), motionEvent.getY()) || this.f7485y0) {
                this.f7485y0 = false;
                m0();
            }
        } else {
            if (motionEvent.getY() < this.f7472s.getTop() || motionEvent.getY() > this.f7472s.getBottom()) {
                return false;
            }
            if (c0(motionEvent.getX(), motionEvent.getY()) && !Z(motionEvent.getX(), motionEvent.getY())) {
                this.f7485y0 = true;
                l0();
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public long getAnimatorDuration() {
        return 150L;
    }

    public boolean getCancelIconAnimating() {
        return this.B;
    }

    public SearchFunctionalButton getFunctionalButton() {
        return this.f7468q;
    }

    @Override // android.widget.LinearLayout
    public int getGravity() {
        return this.A0;
    }

    public boolean getInputMethodAnimationEnabled() {
        return this.J;
    }

    public ImageView getMainIconView() {
        return this.G;
    }

    public int getSearchState() {
        return this.f7476u.get();
    }

    public COUISearchView getSearchView() {
        return this.f7466p;
    }

    public float getSearchViewAnimateHeightPercent() {
        return this.f7443d0;
    }

    public ImageView getSubIconView() {
        return this.H;
    }

    public void k0(boolean z10) {
        COUISearchView cOUISearchView = this.f7466p;
        if (cOUISearchView == null || cOUISearchView.getSearchAutoComplete() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
        Log.d("COUISearchViewAnimate", "openSoftInput: " + z10);
        if (z10) {
            o0();
            if (inputMethodManager != null) {
                if (this.f7479v0 && this.f7481w0 != 0) {
                    r0();
                    return;
                } else {
                    inputMethodManager.showSoftInput(this.f7466p.getSearchAutoComplete(), 0);
                    return;
                }
            }
            return;
        }
        if (inputMethodManager == null || !inputMethodManager.isActive()) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(this.f7466p.getSearchAutoComplete().getWindowToken(), 0);
    }

    @Override // androidx.appcompat.view.CollapsibleActionView
    public void onActionViewCollapsed() {
    }

    @Override // androidx.appcompat.view.CollapsibleActionView
    public void onActionViewExpanded() {
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.f7461m0 || this.f7459l0) {
            u0();
        }
        this.f7448g.setStyle(Paint.Style.FILL);
        this.f7450h.setStyle(Paint.Style.FILL);
        int save = canvas.save();
        if (this.f7457k0) {
            this.f7450h.setColor(this.W);
            canvas.drawPath(this.f7446f, this.f7450h);
        }
        this.f7448g.setColor(this.W);
        canvas.drawPath(this.f7444e, this.f7448g);
        canvas.restoreToCount(save);
        super.onDraw(canvas);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (a0(motionEvent.getX(), motionEvent.getY())) {
            return false;
        }
        if (this.f7476u.get() != 0 || Z(motionEvent.getX(), motionEvent.getY())) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        s0();
        t0();
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        if (this.f7442c0 == 1) {
            int measuredWidth = (this.L * 2) + this.f7468q.getMeasuredWidth() + this.f7470r.getMeasuredWidth();
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.f7466p.getLayoutParams();
            if (marginLayoutParams.getMarginEnd() != measuredWidth) {
                marginLayoutParams.setMarginEnd(measuredWidth);
                this.f7466p.setLayoutParams(marginLayoutParams);
            }
        }
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof COUISavedState) {
            setSearchViewAnimateHeightPercent(((COUISavedState) parcelable).f7488e);
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        COUISavedState cOUISavedState = new COUISavedState(super.onSaveInstanceState());
        cOUISavedState.f7488e = this.f7443d0;
        return cOUISavedState;
    }

    public void q0() {
        if (this.B) {
            return;
        }
        this.B = true;
        O();
        if (this.C == 1) {
            setVisibility(0);
            setAlpha(0.0f);
            int i10 = this.f7442c0;
            if (i10 == 0) {
                this.f7468q.setVisibility(0);
                this.f7470r.setVisibility(8);
            } else if (i10 == 1) {
                this.f7468q.setVisibility(0);
                this.f7470r.setVisibility(0);
            }
            post(new n());
            animate().alpha(1.0f).setDuration(150L).setListener(null).start();
        }
        setToolBarChildVisibility(8);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat.setDuration(150L);
        ofFloat.addUpdateListener(new o());
        ofFloat.addListener(new p());
        ofFloat.start();
        p0();
        if (this.J) {
            k0(true);
        }
    }

    public void setCancelButtonBackground(Drawable drawable) {
        this.f7468q.setBackground(drawable);
    }

    public void setCancelDividerImageDrawable(Drawable drawable) {
        this.f7470r.setImageDrawable(drawable);
    }

    public void setCloseBtnBackground(Drawable drawable) {
        this.F.setBackground(drawable);
    }

    public void setCloseBtnImageDrawable(Drawable drawable) {
        this.F.setImageDrawable(drawable);
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        super.setEnabled(z10);
        ImageView imageView = this.f7464o;
        if (imageView != null) {
            imageView.setEnabled(z10);
        }
        COUISearchView cOUISearchView = this.f7466p;
        if (cOUISearchView != null) {
            cOUISearchView.setEnabled(z10);
        }
        SearchFunctionalButton searchFunctionalButton = this.f7468q;
        if (searchFunctionalButton != null) {
            searchFunctionalButton.setEnabled(z10);
        }
    }

    public void setExtraActivateMarginTop(int i10) {
        this.S = i10;
    }

    public void setFunctionalButtonText(String str) {
        this.f7468q.setText(str);
    }

    @Override // android.widget.LinearLayout
    public void setGravity(int i10) {
        if (this.A0 != i10) {
            if ((8388615 & i10) == 0) {
                i10 |= 8388611;
            }
            if ((i10 & 112) == 0) {
                i10 |= 16;
            }
            this.A0 = i10;
        }
    }

    @Deprecated
    public void setHintTextViewHintTextColor(int i10) {
    }

    @Deprecated
    public void setHintTextViewTextColor(int i10) {
    }

    @Deprecated
    public void setHintViewBackground(Drawable drawable) {
    }

    @Deprecated
    public void setHintViewLayoutOnClickListener(View.OnClickListener onClickListener) {
    }

    @Deprecated
    public void setIconCanAnimate(boolean z10) {
        this.D = z10;
    }

    public void setInputHintTextColor(int i10) {
        this.f7466p.getSearchAutoComplete().setHintTextColor(i10);
    }

    public void setInputMethodAnimationEnabled(boolean z10) {
        this.J = z10;
    }

    public void setInputTextColor(int i10) {
        this.f7466p.getSearchAutoComplete().setTextColor(i10);
    }

    public void setMainIconDrawable(Drawable drawable) {
        this.G.setImageDrawable(drawable);
    }

    public void setOnAnimationListener(t tVar) {
        this.f7480w = tVar;
    }

    public void setQueryHint(CharSequence charSequence) {
        COUISearchView cOUISearchView = this.f7466p;
        if (cOUISearchView != null) {
            cOUISearchView.setQueryHint(charSequence);
        }
    }

    public void setSearchAnimateType(int i10) {
        if (this.f7476u.get() == 1) {
            Log.d("COUISearchViewAnimate", "setSearchAnimateType to " + B0[i10] + " is not allowed in STATE_EDIT");
            return;
        }
        this.f7442c0 = i10;
        if (i10 == 0) {
            this.f7470r.setVisibility(8);
            this.f7468q.setVisibility(4);
            this.f7468q.setAlpha(0.0f);
            ((ViewGroup.MarginLayoutParams) this.f7468q.getLayoutParams()).setMarginStart(this.P);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.f7466p.getLayoutParams();
            marginLayoutParams.setMarginEnd(0);
            this.f7466p.setLayoutParams(marginLayoutParams);
            return;
        }
        if (i10 == 1) {
            this.f7470r.setVisibility(8);
            this.f7468q.setVisibility(8);
            ((ViewGroup.MarginLayoutParams) this.f7468q.getLayoutParams()).setMarginStart(this.O);
            ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.f7466p.getLayoutParams();
            marginLayoutParams2.setMarginEnd(getOriginWidth() - getShrinkWidth());
            this.f7466p.setLayoutParams(marginLayoutParams2);
        }
    }

    public void setSearchViewAnimateHeightPercent(float f10) {
        this.f7443d0 = f10;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getChildAt(0).getLayoutParams();
        marginLayoutParams.height = (int) Float.max(this.T, this.U * N(f10));
        marginLayoutParams.setMarginStart((int) (getPaddingStart() * (1.0f - M(f10)) * (-1.0f)));
        marginLayoutParams.setMarginEnd((int) (getPaddingEnd() * (1.0f - M(f10)) * (-1.0f)));
        getChildAt(0).setLayoutParams(marginLayoutParams);
        setTranslationY((this.V / 2.0f) * (1.0f - f10));
        float f11 = (f10 - 0.5f) * 2.0f;
        this.f7466p.setAlpha(f11);
        this.f7464o.setAlpha(f11);
        this.W = ((Integer) this.f7456k.evaluate(M(f10), Integer.valueOf(this.N), Integer.valueOf(this.f7440a0))).intValue();
    }

    public void setSearchViewBackgroundColor(int i10) {
        this.f7466p.setBackgroundColor(i10);
    }

    public void setSearchViewIcon(Drawable drawable) {
        this.f7464o.setImageDrawable(drawable);
    }

    public void setSubIconDrawable(Drawable drawable) {
        this.H.setImageDrawable(drawable);
    }

    public COUISearchViewAnimate(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiSearchViewAnimateStyle);
    }

    public COUISearchViewAnimate(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7444e = new Path();
        this.f7446f = new Path();
        this.f7448g = new Paint(1);
        this.f7450h = new Paint(1);
        this.f7452i = new int[2];
        this.f7454j = new int[2];
        this.f7456k = new ArgbEvaluator();
        this.f7458l = new RectF();
        Rect rect = new Rect();
        this.f7460m = rect;
        this.f7476u = new AtomicInteger(0);
        this.f7486z = 48;
        this.C = 0;
        this.D = true;
        this.I = true;
        this.J = true;
        this.Q = 0;
        this.S = 0;
        this.f7442c0 = 1;
        this.f7443d0 = 1.0f;
        this.f7457k0 = false;
        this.f7459l0 = true;
        this.f7461m0 = true;
        this.f7481w0 = 0;
        this.f7483x0 = null;
        this.f7485y0 = false;
        this.f7487z0 = new k();
        this.A0 = 16;
        this.f7462n = context;
        COUIDarkModeUtil.b(this, false);
        this.f7479v0 = true;
        R(context, attributeSet);
        g0(context, attributeSet, i10, 0);
        setClipToPadding(false);
        setClipChildren(false);
        setWillNotDraw(false);
        S();
        setLayoutDirection(3);
        setSearchAnimateType(this.f7442c0);
        setTouchDelegate(new TouchDelegate(rect, this.f7468q));
        this.f7466p.getSearchAutoComplete().addTextChangedListener(new l());
    }
}
