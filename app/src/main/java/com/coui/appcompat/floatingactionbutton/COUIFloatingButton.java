package com.coui.appcompat.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.PathInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import c.AppCompatResources;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$color;
import com.support.control.R$dimen;
import com.support.control.R$id;
import com.support.control.R$styleable;
import h3.UIUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import m1.COUIMoveEaseInterpolator;
import s.DynamicAnimation;
import s.SpringAnimation;
import u2.COUIStateListUtil;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUIFloatingButton extends LinearLayout {
    private static final String K = COUIFloatingButton.class.getSimpleName();
    private static final PathInterpolator L = new COUIMoveEaseInterpolator();
    private static final PathInterpolator M = new COUIMoveEaseInterpolator();
    private boolean A;
    private int B;
    private boolean C;
    private ValueAnimator D;
    private m E;
    private l F;
    private l G;
    private l H;
    private n I;
    private float J;

    /* renamed from: e, reason: collision with root package name */
    private int f5967e;

    /* renamed from: f, reason: collision with root package name */
    private int f5968f;

    /* renamed from: g, reason: collision with root package name */
    private float f5969g;

    /* renamed from: h, reason: collision with root package name */
    private final InstanceState f5970h;

    /* renamed from: i, reason: collision with root package name */
    private int f5971i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f5972j;

    /* renamed from: k, reason: collision with root package name */
    private List<COUIFloatingButtonLabel> f5973k;

    /* renamed from: l, reason: collision with root package name */
    private Drawable f5974l;

    /* renamed from: m, reason: collision with root package name */
    private AppCompatImageView f5975m;

    /* renamed from: n, reason: collision with root package name */
    private float f5976n;

    /* renamed from: o, reason: collision with root package name */
    private int f5977o;

    /* renamed from: p, reason: collision with root package name */
    private int f5978p;

    /* renamed from: q, reason: collision with root package name */
    private int f5979q;

    /* renamed from: r, reason: collision with root package name */
    private Runnable f5980r;

    /* renamed from: s, reason: collision with root package name */
    private ValueAnimator f5981s;

    /* renamed from: t, reason: collision with root package name */
    private PathInterpolator f5982t;

    /* renamed from: u, reason: collision with root package name */
    private PathInterpolator f5983u;

    /* renamed from: v, reason: collision with root package name */
    private PathInterpolator f5984v;

    /* renamed from: w, reason: collision with root package name */
    private PathInterpolator f5985w;

    /* renamed from: x, reason: collision with root package name */
    private PathInterpolator f5986x;

    /* renamed from: y, reason: collision with root package name */
    private PathInterpolator f5987y;

    /* renamed from: z, reason: collision with root package name */
    private boolean f5988z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Animator.AnimatorListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f6001a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ boolean f6002b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ COUIFloatingButtonLabel f6003c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f6004d;

        a(int i10, boolean z10, COUIFloatingButtonLabel cOUIFloatingButtonLabel, int i11) {
            this.f6001a = i10;
            this.f6002b = z10;
            this.f6003c = cOUIFloatingButtonLabel;
            this.f6004d = i11;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f6003c.setTranslationY(COUIFloatingButton.this.Z(this.f6001a));
            this.f6003c.getChildFloatingButton().setPivotX(this.f6003c.getChildFloatingButton().getWidth() / 2.0f);
            this.f6003c.getChildFloatingButton().setPivotY(this.f6003c.getChildFloatingButton().getHeight() / 2.0f);
            this.f6003c.setPivotX(r3.getWidth());
            this.f6003c.setPivotY(r3.getHeight());
            if (COUIFloatingButton.this.h0(this.f6001a)) {
                COUIFloatingButton.this.f5970h.f5993f = false;
            }
            COUIFloatingButton.this.A0(1);
            COUIFloatingButton.this.Q(1.0f);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUIFloatingButton.this.g0(this.f6001a)) {
                COUIFloatingButton.this.f5970h.f5993f = true;
                COUIFloatingButton.this.setOnActionSelectedListener(null);
            }
            if (this.f6002b) {
                COUIFloatingButton.this.r0(this.f6003c, this.f6001a, this.f6004d, true);
            } else {
                COUIFloatingButton.this.r0(this.f6003c, this.f6001a, this.f6004d, false);
            }
            COUIFloatingButton.this.A0(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements Animator.AnimatorListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ObjectAnimator f6006a;

        b(ObjectAnimator objectAnimator) {
            this.f6006a = objectAnimator;
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
            this.f6006a.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements l {
        c() {
        }

        @Override // com.coui.appcompat.floatingactionbutton.COUIFloatingButton.l
        public boolean a(COUIFloatingButtonItem cOUIFloatingButtonItem) {
            if (COUIFloatingButton.this.F == null) {
                return false;
            }
            boolean a10 = COUIFloatingButton.this.F.a(cOUIFloatingButtonItem);
            if (!a10) {
                COUIFloatingButton.this.L(false, 300);
            }
            return a10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements View.OnTouchListener {
        d() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (!COUIFloatingButton.this.isEnabled()) {
                return false;
            }
            int action = motionEvent.getAction();
            if (action == 0) {
                COUIFloatingButton.this.D();
                return false;
            }
            if (action != 1 && action != 3) {
                return false;
            }
            COUIFloatingButton.this.C(motionEvent);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements View.OnClickListener {
        e() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIFloatingButton.this.I != null) {
                COUIFloatingButton.this.I.a();
            }
            COUIFloatingButton.this.a0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f extends ViewOutlineProvider {
        f() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class g implements Animator.AnimatorListener {
        g() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUIFloatingButton cOUIFloatingButton = COUIFloatingButton.this;
            cOUIFloatingButton.removeCallbacks(cOUIFloatingButton.f5980r);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUIFloatingButton.this.f5970h.f5993f = false;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIFloatingButton.this.f5970h.f5993f = true;
            COUIFloatingButton cOUIFloatingButton = COUIFloatingButton.this;
            cOUIFloatingButton.removeCallbacks(cOUIFloatingButton.f5980r);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class h implements ValueAnimator.AnimatorUpdateListener {
        h() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue("alpha")).floatValue();
            float floatValue2 = ((Float) valueAnimator.getAnimatedValue("scaleX")).floatValue();
            float floatValue3 = ((Float) valueAnimator.getAnimatedValue("scaleY")).floatValue();
            COUIFloatingButton.this.f5975m.setAlpha(floatValue);
            COUIFloatingButton.this.f5975m.setScaleX(floatValue2);
            COUIFloatingButton.this.f5975m.setScaleY(floatValue3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class i implements Animator.AnimatorListener {
        i() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUIFloatingButton cOUIFloatingButton = COUIFloatingButton.this;
            cOUIFloatingButton.removeCallbacks(cOUIFloatingButton.f5980r);
            COUIFloatingButton.this.f5975m.setVisibility(8);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUIFloatingButton.this.f5975m.setVisibility(0);
            COUIFloatingButton.this.f5970h.f5993f = false;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUIFloatingButton.this.f5970h.f5993f = true;
            COUIFloatingButton cOUIFloatingButton = COUIFloatingButton.this;
            cOUIFloatingButton.postDelayed(cOUIFloatingButton.f5980r, 5000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class j implements Animator.AnimatorListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f6015a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ObjectAnimator f6016b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ SpringAnimation f6017c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ COUIFloatingButtonLabel f6018d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f6019e;

        j(int i10, ObjectAnimator objectAnimator, SpringAnimation springAnimation, COUIFloatingButtonLabel cOUIFloatingButtonLabel, int i11) {
            this.f6015a = i10;
            this.f6016b = objectAnimator;
            this.f6017c = springAnimation;
            this.f6018d = cOUIFloatingButtonLabel;
            this.f6019e = i11;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUIFloatingButton.this.g0(this.f6015a)) {
                COUIFloatingButton.this.f5970h.f5993f = false;
                COUIFloatingButton cOUIFloatingButton = COUIFloatingButton.this;
                cOUIFloatingButton.setOnActionSelectedListener(cOUIFloatingButton.G);
            }
            COUIFloatingButton.this.A0(2);
            COUIFloatingButton.this.Q(0.0f);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUIFloatingButton.this.h0(this.f6015a)) {
                COUIFloatingButton.this.f5970h.f5993f = true;
                COUIFloatingButton.this.setOnActionSelectedListener(null);
            }
            this.f6016b.start();
            this.f6017c.q(0.0f);
            this.f6018d.setVisibility(this.f6019e);
            COUIFloatingButton.this.A0(3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class k implements Runnable {
        private k() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIFloatingButton.this.E();
        }

        /* synthetic */ k(COUIFloatingButton cOUIFloatingButton, c cVar) {
            this();
        }
    }

    /* loaded from: classes.dex */
    public interface l {
        boolean a(COUIFloatingButtonItem cOUIFloatingButtonItem);
    }

    /* loaded from: classes.dex */
    public interface m {
        boolean a();

        void b(boolean z10);
    }

    /* loaded from: classes.dex */
    public interface n {
        void a();
    }

    public COUIFloatingButton(Context context) {
        super(context);
        this.f5967e = 0;
        this.f5968f = 0;
        this.f5969g = 1.0f;
        this.f5970h = new InstanceState();
        this.f5973k = new ArrayList();
        this.f5974l = null;
        this.f5982t = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5983u = new COUIMoveEaseInterpolator();
        this.f5984v = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5985w = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5986x = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5987y = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5988z = true;
        this.A = true;
        this.C = true;
        this.D = null;
        this.H = new c();
        c0(context, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean A0(int i10) {
        int i11 = this.f5967e;
        if (i11 != -1) {
            if (i11 != 0) {
                if (i11 != 1) {
                    if (i11 != 2) {
                        if (i11 != 3) {
                            if (i11 == 4 && i10 == 1) {
                                this.f5967e = i10;
                            }
                        } else if (i10 == 2) {
                            this.f5967e = i10;
                        }
                    } else if (i10 == 4 || i10 == -1) {
                        this.f5967e = i10;
                    }
                } else if (i10 == 3 || i10 == -1 || i10 == 0) {
                    this.f5967e = i10;
                }
            } else if (i10 == -1 || i10 == 1) {
                this.f5967e = i10;
            }
        } else if (i10 == 0 || i10 == 1) {
            this.f5967e = i10;
        }
        return i10 == this.f5967e;
    }

    private void B0(boolean z10, boolean z11, int i10, boolean z12) {
        if (this.f5988z) {
            if (z10 && this.f5973k.isEmpty()) {
                z10 = false;
                m mVar = this.E;
                if (mVar != null) {
                    mVar.a();
                }
            }
            if (i0() == z10) {
                return;
            }
            if (!e0()) {
                E0(z10, z11, i10, z12);
                C0(z11, z12);
                D0();
            }
            m mVar2 = this.E;
            if (mVar2 != null) {
                mVar2.b(z10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void C(MotionEvent motionEvent) {
        ValueAnimator valueAnimator;
        t0();
        ValueAnimator valueAnimator2 = this.D;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            this.D.cancel();
        }
        int i10 = this.f5967e;
        if (i10 != 0) {
            if (i10 == 1) {
                T(false);
                if (!f0((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
                    this.D.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.floatingactionbutton.c
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                            COUIFloatingButton.this.l0(valueAnimator3);
                        }
                    });
                }
            } else if (i10 == 2) {
                T(false);
            } else if (i10 == 3) {
                T(false);
                this.D.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.floatingactionbutton.b
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        COUIFloatingButton.this.m0(valueAnimator3);
                    }
                });
            } else if (i10 != 4) {
                return;
            }
            valueAnimator = this.D;
            if (valueAnimator == null) {
                valueAnimator.start();
                return;
            }
            return;
        }
        T(false);
        this.D.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.floatingactionbutton.g
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                COUIFloatingButton.this.k0(valueAnimator3);
            }
        });
        valueAnimator = this.D;
        if (valueAnimator == null) {
        }
    }

    private void C0(boolean z10, boolean z11) {
        if (i0()) {
            y0(this.f5975m, 45.0f, z11);
            return;
        }
        x0(z11).start();
        Drawable drawable = this.f5974l;
        if (drawable != null) {
            this.f5975m.setImageDrawable(drawable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void D() {
        ValueAnimator valueAnimator;
        t0();
        ValueAnimator valueAnimator2 = this.D;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            this.D.cancel();
        }
        int i10 = this.f5967e;
        if (i10 != 0 && i10 != 1) {
            if (i10 == 2 || i10 == 3) {
                T(true);
                this.D.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.floatingactionbutton.d
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        COUIFloatingButton.this.o0(valueAnimator3);
                    }
                });
                valueAnimator = this.D;
                if (valueAnimator == null) {
                    valueAnimator.start();
                    return;
                }
                return;
            }
            if (i10 != 4) {
                return;
            }
        }
        T(true);
        this.D.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.floatingactionbutton.e
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                COUIFloatingButton.this.n0(valueAnimator3);
            }
        });
        valueAnimator = this.D;
        if (valueAnimator == null) {
        }
    }

    private void D0() {
        ColorStateList mainFloatingButtonBackgroundColor = getMainFloatingButtonBackgroundColor();
        if (mainFloatingButtonBackgroundColor != ColorStateList.valueOf(Integer.MIN_VALUE)) {
            this.f5975m.setBackgroundTintList(mainFloatingButtonBackgroundColor);
        } else {
            int color = getContext().getResources().getColor(R$color.couiGreenTintControlNormal);
            this.f5975m.setBackgroundTintList(COUIStateListUtil.a(COUIContextUtil.b(getContext(), R$attr.couiColorPrimary, color), color));
        }
    }

    private void E0(boolean z10, boolean z11, int i10, boolean z12) {
        int size = this.f5973k.size();
        if (z10) {
            for (int i11 = 0; i11 < size; i11++) {
                int i12 = (size - 1) - i11;
                COUIFloatingButtonLabel cOUIFloatingButtonLabel = this.f5973k.get(i12);
                if (this.f5977o != 0) {
                    if (!d0(i12)) {
                        cOUIFloatingButtonLabel.setVisibility(8);
                        if (z11) {
                            G(cOUIFloatingButtonLabel, i11 * 50, i12, 8);
                        }
                    } else {
                        cOUIFloatingButtonLabel.setVisibility(0);
                        if (z11) {
                            G(cOUIFloatingButtonLabel, i11 * 50, i12, 0);
                        }
                    }
                } else if (z11) {
                    G(cOUIFloatingButtonLabel, i11 * 50, i12, 0);
                }
            }
            this.f5970h.f5992e = true;
            return;
        }
        for (int i13 = 0; i13 < size; i13++) {
            COUIFloatingButtonLabel cOUIFloatingButtonLabel2 = this.f5973k.get(i13);
            if (z11) {
                F(cOUIFloatingButtonLabel2, i13 * 50, i13, i10, z12);
            }
        }
        this.f5970h.f5992e = false;
    }

    private void F(COUIFloatingButtonLabel cOUIFloatingButtonLabel, int i10, int i11, int i12, boolean z10) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        int Z = Z(i11);
        if (z10) {
            Z += marginLayoutParams.bottomMargin + this.f5975m.getHeight();
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel, "translationY", Z);
        ofFloat.setStartDelay(i10);
        ofFloat.setDuration(i12);
        ofFloat.setInterpolator(this.f5983u);
        if (cOUIFloatingButtonLabel.getFloatingButtonLabelText().getText() != "") {
            if (j0()) {
                cOUIFloatingButtonLabel.getFloatingButtonLabelBackground().setPivotX(0.0f);
                cOUIFloatingButtonLabel.getFloatingButtonLabelBackground().setPivotY(cOUIFloatingButtonLabel.getFloatingButtonLabelBackground().getHeight());
            } else {
                cOUIFloatingButtonLabel.getFloatingButtonLabelBackground().setPivotX(cOUIFloatingButtonLabel.getFloatingButtonLabelBackground().getWidth());
                cOUIFloatingButtonLabel.getFloatingButtonLabelBackground().setPivotY(cOUIFloatingButtonLabel.getFloatingButtonLabelBackground().getHeight());
            }
        }
        ofFloat.addListener(new a(i11, z10, cOUIFloatingButtonLabel, i12));
        ofFloat.start();
        ValueAnimator N = N(true);
        if (i0()) {
            N.start();
        }
    }

    private void G(COUIFloatingButtonLabel cOUIFloatingButtonLabel, int i10, int i11, int i12) {
        AnimatorSet animatorSet = new AnimatorSet();
        SpringAnimation springAnimation = new SpringAnimation(cOUIFloatingButtonLabel, DynamicAnimation.f17868n, 0.0f);
        springAnimation.s().f(500.0f);
        springAnimation.s().d(0.8f);
        springAnimation.m(0.0f);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel.getChildFloatingButton(), "scaleX", 0.6f, 1.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel.getChildFloatingButton(), "scaleY", 0.6f, 1.0f);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel.getFloatingButtonLabelBackground(), "scaleX", 0.6f, 1.0f);
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel.getFloatingButtonLabelBackground(), "scaleY", 0.6f, 1.0f);
        ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel.getChildFloatingButton(), "alpha", 0.0f, 1.0f);
        ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel.getFloatingButtonLabelBackground(), "alpha", 0.0f, 1.0f);
        ofFloat6.setInterpolator(this.f5982t);
        ofFloat6.setDuration(350L);
        animatorSet.playTogether(ofFloat, ofFloat2, ofFloat5, ofFloat3, ofFloat4);
        animatorSet.setInterpolator(this.f5982t);
        animatorSet.setDuration(300L);
        animatorSet.setStartDelay(i10);
        if (cOUIFloatingButtonLabel.getFloatingButtonLabelText().getText() != "") {
            if (j0()) {
                cOUIFloatingButtonLabel.getFloatingButtonLabelBackground().setPivotX(0.0f);
                cOUIFloatingButtonLabel.getFloatingButtonLabelBackground().setPivotY(0.0f);
            } else {
                cOUIFloatingButtonLabel.getFloatingButtonLabelBackground().setPivotX(cOUIFloatingButtonLabel.getFloatingButtonLabelBackground().getWidth());
                cOUIFloatingButtonLabel.getFloatingButtonLabelBackground().setPivotY(0.0f);
            }
        }
        animatorSet.addListener(new j(i11, ofFloat6, springAnimation, cOUIFloatingButtonLabel, i12));
        animatorSet.start();
        ValueAnimator N = N(false);
        if (i0()) {
            return;
        }
        N.start();
    }

    private void J() {
        ValueAnimator valueAnimator = this.f5981s;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        this.f5981s.cancel();
    }

    private ValueAnimator N(final boolean z10) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(200L);
        ofFloat.setInterpolator(M);
        ofFloat.setCurrentFraction(1.0f - this.f5969g);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.floatingactionbutton.h
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUIFloatingButton.this.p0(z10, valueAnimator);
            }
        });
        return ofFloat;
    }

    private AppCompatImageView O() {
        AppCompatImageView appCompatImageView = new AppCompatImageView(getContext());
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_floating_button_item_stroke_width);
        int i10 = this.B;
        if (i10 > 0) {
            this.f5971i = i10;
        } else {
            this.f5971i = getResources().getDimensionPixelSize(R$dimen.coui_floating_button_size);
        }
        int i11 = this.f5971i;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i11, i11);
        layoutParams.gravity = 8388613;
        int P = P(getContext(), 0.0f);
        P(getContext(), 8.0f);
        layoutParams.setMargins(P, 0, P, 0);
        appCompatImageView.setId(R$id.coui_floating_button_main_fab);
        appCompatImageView.setLayoutParams(layoutParams);
        appCompatImageView.setPaddingRelative(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        appCompatImageView.setScaleType(ImageView.ScaleType.CENTER);
        appCompatImageView.setClickable(true);
        appCompatImageView.setFocusable(true);
        int color = getContext().getResources().getColor(R$color.couiGreenTintControlNormal);
        appCompatImageView.setBackgroundTintList(COUIStateListUtil.a(COUIContextUtil.b(getContext(), R$attr.couiColorPrimary, color), color));
        return appCompatImageView;
    }

    private static int P(Context context, float f10) {
        return Math.round(TypedValue.applyDimension(1, f10, context.getResources().getDisplayMetrics()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Q(float f10) {
        this.f5975m.getBackground().setTintList(COUIStateListUtil.a(U(f10), this.f5979q));
    }

    private COUIFloatingButtonLabel R(int i10) {
        if (i10 < this.f5973k.size()) {
            return this.f5973k.get(i10);
        }
        return null;
    }

    private COUIFloatingButtonLabel S(int i10) {
        for (COUIFloatingButtonLabel cOUIFloatingButtonLabel : this.f5973k) {
            if (cOUIFloatingButtonLabel.getId() == i10) {
                return cOUIFloatingButtonLabel;
            }
        }
        return null;
    }

    private void T(boolean z10) {
        float[] fArr = new float[2];
        fArr[0] = z10 ? 1.0f : 0.0f;
        fArr[1] = z10 ? 0.0f : 1.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        this.D = ofFloat;
        ofFloat.setDuration(200L);
        this.D.setInterpolator(M);
        this.D.setCurrentFraction(1.0f - this.f5969g);
        this.D.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.floatingactionbutton.f
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUIFloatingButton.this.q0(valueAnimator);
            }
        });
    }

    private int U(float f10) {
        int color;
        ColorStateList backgroundTintList = this.f5975m.getBackgroundTintList();
        if (backgroundTintList != null) {
            color = backgroundTintList.getDefaultColor();
        } else {
            color = this.f5975m.getBackground() instanceof ColorDrawable ? ((ColorDrawable) this.f5975m.getBackground()).getColor() : Integer.MIN_VALUE;
        }
        return color != Integer.MIN_VALUE ? V(color, W(f10)) : color;
    }

    private int V(int i10, float f10) {
        float[] fArr = new float[3];
        ColorUtils.f(i10, fArr);
        if (this.J == 0.0f) {
            fArr[2] = fArr[2] * f10;
            int a10 = ColorUtils.a(fArr);
            return Color.argb(Color.alpha(a10), Math.min(255, Color.red(a10)), Math.min(255, Color.green(a10)), Math.min(255, Color.blue(a10)));
        }
        int a11 = ColorUtils.a(fArr);
        return Color.argb((int) (Color.alpha(i10) / f10), Math.min(255, Color.red(a11)), Math.min(255, Color.green(a11)), Math.min(255, Color.blue(a11)));
    }

    private float W(float f10) {
        float f11 = this.J;
        float f12 = f11 != 0.0f ? 1.0f / f11 : 0.88f;
        return f12 + ((1.0f - f12) * f10);
    }

    private int X(int i10) {
        return this.f5973k.size() - i10;
    }

    private float Y(float f10) {
        return (f10 * 0.100000024f) + 0.9f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int Z(int i10) {
        if (i10 < 0 || i10 >= this.f5973k.size()) {
            return 0;
        }
        return P(getContext(), (i10 * 72) + 88);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a0() {
        if (i0()) {
            m mVar = this.E;
            if (mVar == null || !mVar.a()) {
                K();
                return;
            }
            return;
        }
        s0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b0(FloatingActionButton.b bVar) {
        if (i0()) {
            K();
            ViewCompat.d(this.f5975m).f(0.0f).g(0L).m();
        }
    }

    private void c0(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIFloatingButton, 0, 0);
        this.f5988z = obtainStyledAttributes.getBoolean(R$styleable.COUIFloatingButton_fabNeedElevation, true);
        this.A = obtainStyledAttributes.getBoolean(R$styleable.COUIFloatingButton_fabNeedVibrate, true);
        this.B = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIFloatingButton_fabButtonSize, 0);
        this.C = obtainStyledAttributes.getBoolean(R$styleable.COUIFloatingButton_fabScaleAnimation, true);
        this.J = obtainStyledAttributes.getFloat(R$styleable.COUIFloatingButton_fabTranslateEnhancementRatio, 0.0f);
        this.f5975m = O();
        f fVar = new f();
        if (this.f5988z) {
            UIUtil.j(this.f5975m, getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.support_shadow_size_level_three), getResources().getColor(com.support.control.R$color.coui_floating_button_elevation_color));
        }
        this.f5975m.setOutlineProvider(fVar);
        this.f5975m.setClipToOutline(true);
        this.f5975m.setBackgroundColor(COUIContextUtil.b(getContext(), R$attr.couiColorPrimary, 0));
        addView(this.f5975m);
        setClipChildren(false);
        setClipToPadding(false);
        this.f5980r = new k(this, null);
        this.f5979q = ResourcesCompat.d(context.getResources(), com.support.control.R$color.coui_floating_button_disabled_color, context.getTheme());
        try {
            try {
                this.f5972j = obtainStyledAttributes.getBoolean(R$styleable.COUIFloatingButton_fabExpandAnimationEnable, true);
                setEnabled(obtainStyledAttributes.getBoolean(R$styleable.COUIFloatingButton_android_enabled, isEnabled()));
                int resourceId = obtainStyledAttributes.getResourceId(R$styleable.COUIFloatingButton_mainFloatingButtonSrc, Integer.MIN_VALUE);
                if (resourceId != Integer.MIN_VALUE) {
                    setMainFabDrawable(AppCompatResources.b(getContext(), resourceId));
                }
                z0();
                setMainFloatingButtonBackgroundColor(obtainStyledAttributes.getColorStateList(R$styleable.COUIFloatingButton_mainFloatingButtonBackgroundColor));
                setFloatingButtonExpandEnable(this.f5972j);
            } catch (Exception e10) {
                Log.e(K, "Failure setting FabWithLabelView icon" + e10.getMessage());
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private boolean f0(int i10, int i11) {
        int[] iArr = new int[2];
        this.f5975m.getLocationInWindow(iArr);
        int i12 = iArr[0];
        int measuredWidth = this.f5975m.getMeasuredWidth() + i12;
        int i13 = iArr[1];
        return i10 >= i12 && i10 <= measuredWidth && i11 >= i13 && i11 <= this.f5975m.getMeasuredHeight() + i13;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean g0(int i10) {
        COUIFloatingButtonLabel R = R(i10);
        return R != null && indexOfChild(R) == this.f5973k.size() - 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean h0(int i10) {
        COUIFloatingButtonLabel R = R(i10);
        return R != null && indexOfChild(R) == 0;
    }

    private boolean j0() {
        return getLayoutDirection() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void k0(ValueAnimator valueAnimator) {
        this.f5975m.getBackground().setTintList(COUIStateListUtil.a(U(valueAnimator.getAnimatedFraction()), this.f5979q));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void l0(ValueAnimator valueAnimator) {
        this.f5975m.getBackground().setTintList(COUIStateListUtil.a(U(valueAnimator.getAnimatedFraction()), this.f5979q));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void m0(ValueAnimator valueAnimator) {
        this.f5975m.getBackground().setTintList(COUIStateListUtil.a(U(0.0f), this.f5979q));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void n0(ValueAnimator valueAnimator) {
        this.f5975m.getBackground().setTintList(COUIStateListUtil.a(U(1.0f - this.f5969g), this.f5979q));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void o0(ValueAnimator valueAnimator) {
        this.f5975m.getBackground().setTintList(COUIStateListUtil.a(U(0.0f), this.f5979q));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p0(boolean z10, ValueAnimator valueAnimator) {
        this.f5975m.getBackground().setTintList(COUIStateListUtil.a(U(z10 ? valueAnimator.getAnimatedFraction() : 0.0f), this.f5979q));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void q0(ValueAnimator valueAnimator) {
        if (this.C) {
            this.f5969g = valueAnimator.getAnimatedFraction();
            float Y = Y(((Float) valueAnimator.getAnimatedValue()).floatValue());
            this.f5975m.setPivotX(r0.getWidth() / 2.0f);
            this.f5975m.setPivotY(r0.getHeight() / 2.0f);
            this.f5975m.setScaleX(Y);
            this.f5975m.setScaleY(Y);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r0(COUIFloatingButtonLabel cOUIFloatingButtonLabel, int i10, int i11, boolean z10) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel.getChildFloatingButton(), "scaleX", 1.0f, 0.6f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel.getChildFloatingButton(), "scaleY", 1.0f, 0.6f);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel.getFloatingButtonLabelBackground(), "scaleX", 1.0f, 0.6f);
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel.getFloatingButtonLabelBackground(), "scaleY", 1.0f, 0.6f);
        ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel.getChildFloatingButton(), "alpha", 1.0f, 0.0f);
        ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(cOUIFloatingButtonLabel.getFloatingButtonLabelBackground(), "alpha", 1.0f, 0.0f);
        ofFloat6.setInterpolator(this.f5985w);
        ofFloat6.setDuration(200L);
        animatorSet.playTogether(ofFloat, ofFloat2, ofFloat5, ofFloat4, ofFloat3);
        animatorSet.setInterpolator(this.f5983u);
        animatorSet.setDuration(i11);
        animatorSet.addListener(new b(ofFloat6));
        animatorSet.start();
    }

    private void t0() {
        if (this.A) {
            performHapticFeedback(302);
        }
    }

    private COUIFloatingButtonItem u0(COUIFloatingButtonLabel cOUIFloatingButtonLabel, Iterator<COUIFloatingButtonLabel> it, boolean z10) {
        if (cOUIFloatingButtonLabel == null) {
            return null;
        }
        COUIFloatingButtonItem floatingButtonItem = cOUIFloatingButtonLabel.getFloatingButtonItem();
        if (it != null) {
            it.remove();
        } else {
            this.f5973k.remove(cOUIFloatingButtonLabel);
        }
        removeView(cOUIFloatingButtonLabel);
        return floatingButtonItem;
    }

    private void z0() {
        setOrientation(1);
        Iterator<COUIFloatingButtonLabel> it = this.f5973k.iterator();
        while (it.hasNext()) {
            it.next().setOrientation(0);
        }
        L(false, 300);
        ArrayList<COUIFloatingButtonItem> actionItems = getActionItems();
        v0();
        B(actionItems);
    }

    public COUIFloatingButtonLabel A(COUIFloatingButtonItem cOUIFloatingButtonItem, int i10, boolean z10, int i11) {
        COUIFloatingButtonLabel S = S(cOUIFloatingButtonItem.v());
        if (S != null) {
            return w0(S.getFloatingButtonItem(), cOUIFloatingButtonItem);
        }
        COUIFloatingButtonLabel s7 = cOUIFloatingButtonItem.s(getContext());
        s7.setMainButtonSize(this.B);
        s7.setOrientation(getOrientation() == 1 ? 0 : 1);
        s7.setOnActionSelectedListener(this.H);
        s7.setVisibility(i11);
        int X = X(i10);
        if (i10 == 0) {
            s7.setPaddingRelative(getPaddingStart(), getPaddingTop(), getPaddingEnd(), getResources().getDimensionPixelSize(R$dimen.coui_floating_button_item_first_bottom_margin));
            addView(s7, X);
        } else {
            s7.setPaddingRelative(getPaddingStart(), getPaddingTop(), getPaddingEnd(), getResources().getDimensionPixelSize(R$dimen.coui_floating_button_item_normal_bottom_margin));
            addView(s7, X);
        }
        this.f5973k.add(i10, s7);
        F(s7, 0, i10, 300, false);
        return s7;
    }

    public Collection<COUIFloatingButtonLabel> B(Collection<COUIFloatingButtonItem> collection) {
        ArrayList arrayList = new ArrayList();
        Iterator<COUIFloatingButtonItem> it = collection.iterator();
        while (it.hasNext()) {
            arrayList.add(x(it.next()));
        }
        return arrayList;
    }

    public void E() {
        ViewCompat.d(this.f5975m).c();
        J();
        this.f5975m.setVisibility(0);
        this.f5975m.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setInterpolator(L).setDuration(350L).setListener(new g());
    }

    public ValueAnimator H(Animator.AnimatorListener animatorListener) {
        ViewCompat.d(this.f5975m).c();
        ValueAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat("alpha", this.f5975m.getAlpha(), 0.0f), PropertyValuesHolder.ofFloat("scaleX", this.f5975m.getScaleX(), 0.6f), PropertyValuesHolder.ofFloat("scaleY", this.f5975m.getScaleY(), 0.6f));
        this.f5981s = ofPropertyValuesHolder;
        ofPropertyValuesHolder.setInterpolator(L);
        this.f5981s.setDuration(350L);
        this.f5981s.addListener(animatorListener);
        this.f5981s.addUpdateListener(new h());
        return this.f5981s;
    }

    @Deprecated
    public ValueAnimator I() {
        return H(new i());
    }

    public void K() {
        B0(false, true, 300, false);
    }

    public void L(boolean z10, int i10) {
        B0(false, z10, i10, false);
    }

    public void M(boolean z10, int i10, boolean z11) {
        B0(false, z10, i10, z11);
    }

    public boolean d0(int i10) {
        if (i10 < 0 || i10 >= this.f5973k.size()) {
            return false;
        }
        return (((float) Z(i10)) + ((float) ((ViewGroup.MarginLayoutParams) getLayoutParams()).bottomMargin)) + ((float) this.f5975m.getHeight()) <= ((float) (this.f5977o + this.f5978p));
    }

    public boolean e0() {
        return this.f5970h.f5993f;
    }

    public ArrayList<COUIFloatingButtonItem> getActionItems() {
        ArrayList<COUIFloatingButtonItem> arrayList = new ArrayList<>(this.f5973k.size());
        Iterator<COUIFloatingButtonLabel> it = this.f5973k.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getFloatingButtonItem());
        }
        return arrayList;
    }

    public AppCompatImageView getMainFloatingButton() {
        return this.f5975m;
    }

    public ColorStateList getMainFloatingButtonBackgroundColor() {
        return this.f5970h.f5994g;
    }

    public boolean i0() {
        return this.f5970h.f5992e;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.B <= 0) {
            Context createConfigurationContext = getContext().createConfigurationContext(configuration);
            if (COUIResponsiveUtils.j(configuration.screenWidthDp)) {
                this.f5971i = createConfigurationContext.getResources().getDimensionPixelOffset(R$dimen.coui_floating_button_normal_size);
            } else {
                this.f5971i = createConfigurationContext.getResources().getDimensionPixelOffset(R$dimen.coui_floating_button_large_size);
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f5975m.getLayoutParams();
            int i10 = this.f5971i;
            layoutParams.width = i10;
            layoutParams.height = i10;
            this.f5975m.setLayoutParams(layoutParams);
        }
    }

    public void s0() {
        B0(true, true, 300, false);
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        super.setEnabled(z10);
        getMainFloatingButton().setEnabled(z10);
        if (!z10) {
            this.f5968f = this.f5967e;
            A0(-1);
        } else {
            A0(this.f5968f);
        }
    }

    public void setFloatingButtonClickListener(n nVar) {
        this.I = nVar;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void setFloatingButtonExpandEnable(boolean z10) {
        if (z10) {
            this.f5975m.setOnTouchListener(new d());
        }
        this.f5975m.setOnClickListener(new e());
    }

    public void setIsFloatingButtonExpandEnable(boolean z10) {
        this.f5972j = z10;
        if (z10) {
            A0(1);
        } else {
            A0(0);
        }
    }

    public void setMainFabDrawable(Drawable drawable) {
        this.f5974l = drawable;
        C0(false, false);
    }

    public void setMainFloatingButtonBackgroundColor(ColorStateList colorStateList) {
        this.f5970h.f5994g = colorStateList;
        D0();
    }

    public void setOnActionSelectedListener(l lVar) {
        this.F = lVar;
        if (lVar != null) {
            this.G = lVar;
        }
        for (int i10 = 0; i10 < this.f5973k.size(); i10++) {
            this.f5973k.get(i10).setOnActionSelectedListener(this.H);
        }
    }

    public void setOnChangeListener(m mVar) {
        this.E = mVar;
    }

    public void setScaleAnimation(boolean z10) {
        this.C = z10;
    }

    public void v0() {
        Iterator<COUIFloatingButtonLabel> it = this.f5973k.iterator();
        while (it.hasNext()) {
            u0(it.next(), it, true);
        }
    }

    public COUIFloatingButtonLabel w0(COUIFloatingButtonItem cOUIFloatingButtonItem, COUIFloatingButtonItem cOUIFloatingButtonItem2) {
        COUIFloatingButtonLabel S;
        int indexOf;
        if (cOUIFloatingButtonItem == null || (S = S(cOUIFloatingButtonItem.v())) == null || (indexOf = this.f5973k.indexOf(S)) < 0) {
            return null;
        }
        int visibility = S.getVisibility();
        u0(S(cOUIFloatingButtonItem2.v()), null, false);
        u0(S(cOUIFloatingButtonItem.v()), null, false);
        return A(cOUIFloatingButtonItem2, indexOf, false, visibility);
    }

    public COUIFloatingButtonLabel x(COUIFloatingButtonItem cOUIFloatingButtonItem) {
        return y(cOUIFloatingButtonItem, this.f5973k.size());
    }

    public ObjectAnimator x0(boolean z10) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.f5975m, "rotation", this.f5976n, 0.0f);
        ofFloat.setInterpolator(this.f5987y);
        ofFloat.setDuration(z10 ? 250L : 300L);
        return ofFloat;
    }

    public COUIFloatingButtonLabel y(COUIFloatingButtonItem cOUIFloatingButtonItem, int i10) {
        return z(cOUIFloatingButtonItem, i10, true);
    }

    public void y0(View view, float f10, boolean z10) {
        this.f5976n = f10;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.f5975m, "rotation", 0.0f, f10);
        ofFloat.setInterpolator(this.f5986x);
        ofFloat.setDuration(z10 ? 250L : 300L);
        ofFloat.start();
    }

    public COUIFloatingButtonLabel z(COUIFloatingButtonItem cOUIFloatingButtonItem, int i10, boolean z10) {
        return A(cOUIFloatingButtonItem, i10, z10, 0);
    }

    /* loaded from: classes.dex */
    public static class COUIFloatingButtonBehavior extends CoordinatorLayout.Behavior<View> {

        /* renamed from: a, reason: collision with root package name */
        private Rect f5989a;

        /* renamed from: b, reason: collision with root package name */
        private FloatingActionButton.b f5990b;

        /* renamed from: c, reason: collision with root package name */
        private boolean f5991c;

        public COUIFloatingButtonBehavior() {
            this.f5991c = true;
        }

        private int d(AppBarLayout appBarLayout) {
            int z10 = ViewCompat.z(appBarLayout);
            if (z10 != 0) {
                return z10 * 2;
            }
            int childCount = appBarLayout.getChildCount();
            if (childCount >= 1) {
                return ViewCompat.z(appBarLayout.getChildAt(childCount - 1)) * 2;
            }
            return 0;
        }

        private static boolean f(View view) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.e) {
                return ((CoordinatorLayout.e) layoutParams).f() instanceof BottomSheetBehavior;
            }
            return false;
        }

        private boolean g(View view, View view2) {
            return this.f5991c && ((CoordinatorLayout.e) view2.getLayoutParams()).e() == view.getId() && view2.getVisibility() == 0;
        }

        private boolean i(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view) {
            if (!g(appBarLayout, view)) {
                return false;
            }
            if (this.f5989a == null) {
                this.f5989a = new Rect();
            }
            Rect rect = this.f5989a;
            com.coui.appcompat.floatingactionbutton.j.a(coordinatorLayout, appBarLayout, rect);
            if (rect.bottom <= d(appBarLayout)) {
                view.setVisibility(8);
                return true;
            }
            view.setVisibility(0);
            return true;
        }

        private boolean j(View view, View view2) {
            if (!g(view, view2)) {
                return false;
            }
            if (view.getTop() < (view2.getHeight() / 2) + ((ViewGroup.MarginLayoutParams) ((CoordinatorLayout.e) view2.getLayoutParams())).topMargin) {
                e(view2);
                return true;
            }
            h(view2);
            return true;
        }

        protected void e(View view) {
            if (view instanceof FloatingActionButton) {
                ((FloatingActionButton) view).k(this.f5990b);
            } else if (view instanceof COUIFloatingButton) {
                ((COUIFloatingButton) view).b0(this.f5990b);
            } else {
                view.setVisibility(4);
            }
        }

        protected void h(View view) {
            if (view instanceof FloatingActionButton) {
                ((FloatingActionButton) view).r(this.f5990b);
            } else if (view instanceof COUIFloatingButton) {
                view.setVisibility(0);
            } else {
                view.setVisibility(0);
            }
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onAttachedToLayoutParams(CoordinatorLayout.e eVar) {
            if (eVar.f2069h == 0) {
                eVar.f2069h = 80;
            }
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
            if (view2 instanceof AppBarLayout) {
                i(coordinatorLayout, (AppBarLayout) view2, view);
                return false;
            }
            if (!f(view2)) {
                return false;
            }
            j(view2, view);
            return false;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i10) {
            List<View> v7 = coordinatorLayout.v(view);
            int size = v7.size();
            for (int i11 = 0; i11 < size; i11++) {
                View view2 = v7.get(i11);
                if (view2 instanceof AppBarLayout) {
                    if (i(coordinatorLayout, (AppBarLayout) view2, view)) {
                        break;
                    }
                } else {
                    if (f(view2) && j(view2, view)) {
                        break;
                    }
                }
            }
            coordinatorLayout.M(view, i10);
            return true;
        }

        public COUIFloatingButtonBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.google.android.material.R$styleable.FloatingActionButton_Behavior_Layout);
            this.f5991c = obtainStyledAttributes.getBoolean(com.google.android.material.R$styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, true);
            obtainStyledAttributes.recycle();
        }
    }

    /* loaded from: classes.dex */
    public static class ScrollViewBehavior extends COUIFloatingButtonBehavior {

        /* renamed from: d, reason: collision with root package name */
        ValueAnimator f5997d;

        /* renamed from: e, reason: collision with root package name */
        private boolean f5998e;

        /* loaded from: classes.dex */
        class a extends RecyclerView.t {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ View f5999a;

            a(View view) {
                this.f5999a = view;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.t
            public void onScrollStateChanged(RecyclerView recyclerView, int i10) {
                super.onScrollStateChanged(recyclerView, i10);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.t
            public void onScrolled(RecyclerView recyclerView, int i10, int i11) {
                super.onScrolled(recyclerView, i10, i11);
                View view = this.f5999a;
                if (view instanceof COUIFloatingButton) {
                    ScrollViewBehavior.this.l((COUIFloatingButton) view, i11);
                }
            }
        }

        public ScrollViewBehavior() {
            this.f5997d = new ObjectAnimator();
            this.f5998e = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void l(COUIFloatingButton cOUIFloatingButton, int i10) {
            if (i10 <= 10 || cOUIFloatingButton.getVisibility() != 0) {
                if (i10 < -10) {
                    cOUIFloatingButton.E();
                    return;
                }
                return;
            }
            if (cOUIFloatingButton.i0() && !this.f5997d.isRunning()) {
                AnimatorSet animatorSet = new AnimatorSet();
                ValueAnimator I = cOUIFloatingButton.I();
                this.f5997d = I;
                animatorSet.playTogether(I, cOUIFloatingButton.x0(true));
                animatorSet.setDuration(150L);
                cOUIFloatingButton.M(true, 250, true);
                animatorSet.start();
                return;
            }
            if (this.f5997d.isRunning()) {
                return;
            }
            ValueAnimator I2 = cOUIFloatingButton.I();
            this.f5997d = I2;
            I2.start();
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View view, View view2, int i10, int i11, int[] iArr, int i12) {
            super.onNestedPreScroll(coordinatorLayout, view, view2, i10, i11, iArr, i12);
            if (view instanceof COUIFloatingButton) {
                l((COUIFloatingButton) view, i11);
            }
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, View view3, int i10, int i11) {
            if (view3 instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) view3;
                int itemCount = recyclerView.getAdapter().getItemCount();
                if (recyclerView.getChildCount() != 0 && itemCount != 0 && !this.f5998e) {
                    recyclerView.addOnScrollListener(new a(view));
                    this.f5998e = true;
                }
                return false;
            }
            if (view3 instanceof AbsListView) {
                AbsListView absListView = (AbsListView) view3;
                int count = absListView.getCount();
                int childCount = absListView.getChildCount();
                View childAt = absListView.getChildAt(0);
                int top = view3.getTop() - view3.getPaddingTop();
                int bottom = view3.getBottom() - view3.getPaddingBottom();
                AbsListView absListView2 = (AbsListView) view3;
                View childAt2 = absListView2.getChildAt(childCount - 1);
                if (childCount > 0 && count > 0) {
                    if (absListView2.getFirstVisiblePosition() == 0 && childAt.getTop() >= (-top)) {
                        return false;
                    }
                    if (childAt2 != null && absListView2.getLastVisiblePosition() == count - 1 && childAt2.getBottom() <= bottom) {
                        return false;
                    }
                }
            }
            return true;
        }

        public ScrollViewBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f5997d = new ObjectAnimator();
            this.f5998e = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class InstanceState implements Parcelable {
        public static final Parcelable.Creator<InstanceState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        private boolean f5992e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f5993f;

        /* renamed from: g, reason: collision with root package name */
        private ColorStateList f5994g;

        /* renamed from: h, reason: collision with root package name */
        private boolean f5995h;

        /* renamed from: i, reason: collision with root package name */
        private ArrayList<COUIFloatingButtonItem> f5996i;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<InstanceState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public InstanceState createFromParcel(Parcel parcel) {
                return new InstanceState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public InstanceState[] newArray(int i10) {
                return new InstanceState[i10];
            }
        }

        public InstanceState() {
            this.f5992e = false;
            this.f5993f = false;
            this.f5994g = ColorStateList.valueOf(Integer.MIN_VALUE);
            this.f5995h = false;
            this.f5996i = new ArrayList<>();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            parcel.writeByte(this.f5992e ? (byte) 1 : (byte) 0);
            parcel.writeByte(this.f5993f ? (byte) 1 : (byte) 0);
            parcel.writeByte(this.f5995h ? (byte) 1 : (byte) 0);
            parcel.writeTypedList(this.f5996i);
        }

        protected InstanceState(Parcel parcel) {
            this.f5992e = false;
            this.f5993f = false;
            this.f5994g = ColorStateList.valueOf(Integer.MIN_VALUE);
            this.f5995h = false;
            this.f5996i = new ArrayList<>();
            this.f5992e = parcel.readByte() != 0;
            this.f5993f = parcel.readByte() != 0;
            this.f5995h = parcel.readByte() != 0;
            this.f5996i = parcel.createTypedArrayList(COUIFloatingButtonItem.CREATOR);
        }
    }

    public COUIFloatingButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f5967e = 0;
        this.f5968f = 0;
        this.f5969g = 1.0f;
        this.f5970h = new InstanceState();
        this.f5973k = new ArrayList();
        this.f5974l = null;
        this.f5982t = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5983u = new COUIMoveEaseInterpolator();
        this.f5984v = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5985w = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5986x = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5987y = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5988z = true;
        this.A = true;
        this.C = true;
        this.D = null;
        this.H = new c();
        c0(context, attributeSet);
    }

    public COUIFloatingButton(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f5967e = 0;
        this.f5968f = 0;
        this.f5969g = 1.0f;
        this.f5970h = new InstanceState();
        this.f5973k = new ArrayList();
        this.f5974l = null;
        this.f5982t = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5983u = new COUIMoveEaseInterpolator();
        this.f5984v = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5985w = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5986x = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5987y = new PathInterpolator(0.25f, 0.1f, 0.25f, 1.0f);
        this.f5988z = true;
        this.A = true;
        this.C = true;
        this.D = null;
        this.H = new c();
        c0(context, attributeSet);
    }
}
