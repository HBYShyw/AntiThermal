package com.coui.appcompat.lockview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.PathInterpolator;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import com.oplus.deepthinker.sdk.app.geofence.Geofence;
import com.support.control.R$attr;
import com.support.control.R$string;
import com.support.control.R$style;
import com.support.control.R$styleable;
import java.util.LinkedList;
import java.util.List;
import k3.VibrateUtils;
import m1.COUIEaseInterpolator;
import m1.COUILinearInterpolator;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUISimpleLock extends View {
    private int A;
    private boolean B;
    private boolean C;
    private boolean D;
    private int E;
    private int F;
    private boolean G;
    private ValueAnimator H;
    private ValueAnimator I;
    private Animator J;
    private float K;
    private float L;
    private int M;
    private float N;
    private float O;
    private boolean P;
    private int Q;
    private int R;
    private LinkedList<String> S;
    private h T;
    private String U;
    private boolean V;
    private boolean W;

    /* renamed from: a0, reason: collision with root package name */
    private int f6429a0;

    /* renamed from: b0, reason: collision with root package name */
    private Context f6430b0;

    /* renamed from: c0, reason: collision with root package name */
    private PathInterpolator f6431c0;

    /* renamed from: e, reason: collision with root package name */
    public int f6432e;

    /* renamed from: f, reason: collision with root package name */
    private final int f6433f;

    /* renamed from: g, reason: collision with root package name */
    private final int f6434g;

    /* renamed from: h, reason: collision with root package name */
    private final int f6435h;

    /* renamed from: i, reason: collision with root package name */
    private final int f6436i;

    /* renamed from: j, reason: collision with root package name */
    private final int f6437j;

    /* renamed from: k, reason: collision with root package name */
    private final int f6438k;

    /* renamed from: l, reason: collision with root package name */
    private final int f6439l;

    /* renamed from: m, reason: collision with root package name */
    private final int f6440m;

    /* renamed from: n, reason: collision with root package name */
    private final int f6441n;

    /* renamed from: o, reason: collision with root package name */
    private final int f6442o;

    /* renamed from: p, reason: collision with root package name */
    private final float[] f6443p;

    /* renamed from: q, reason: collision with root package name */
    private final float[] f6444q;

    /* renamed from: r, reason: collision with root package name */
    private final float[] f6445r;

    /* renamed from: s, reason: collision with root package name */
    private int f6446s;

    /* renamed from: t, reason: collision with root package name */
    private int f6447t;

    /* renamed from: u, reason: collision with root package name */
    private int f6448u;

    /* renamed from: v, reason: collision with root package name */
    private int f6449v;

    /* renamed from: w, reason: collision with root package name */
    private Drawable f6450w;

    /* renamed from: x, reason: collision with root package name */
    private Drawable f6451x;

    /* renamed from: y, reason: collision with root package name */
    private Drawable f6452y;

    /* renamed from: z, reason: collision with root package name */
    private int f6453z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {
        a() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISimpleLock.this.setOpacity(((Integer) valueAnimator.getAnimatedValue()).intValue());
            COUISimpleLock.this.invalidate();
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
            COUISimpleLock.this.C = true;
            COUISimpleLock.this.invalidate();
            if (COUISimpleLock.this.G) {
                if (COUISimpleLock.this.J != null && COUISimpleLock.this.J.isRunning()) {
                    COUISimpleLock.this.C = false;
                    return;
                }
                COUISimpleLock.this.E = 5;
                COUISimpleLock cOUISimpleLock = COUISimpleLock.this;
                cOUISimpleLock.J = cOUISimpleLock.t();
                COUISimpleLock.this.J.start();
                COUISimpleLock.this.V = true;
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUISimpleLock.this.C = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {
        c() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISimpleLock.this.setOpacity(((Integer) valueAnimator.getAnimatedValue()).intValue());
            COUISimpleLock.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements Animator.AnimatorListener {
        d() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISimpleLock.this.B = true;
            COUISimpleLock.this.invalidate();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUISimpleLock.this.B = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements ValueAnimator.AnimatorUpdateListener {
        e() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISimpleLock.this.setInternalTranslationX(((Float) valueAnimator.getAnimatedValue()).floatValue());
            COUISimpleLock.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f implements ValueAnimator.AnimatorUpdateListener {
        f() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISimpleLock.this.setInternalTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class g implements Animator.AnimatorListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ValueAnimator f6460a;

        g(ValueAnimator valueAnimator) {
            this.f6460a = valueAnimator;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISimpleLock.this.setInternalTranslationX(0.0f);
            COUISimpleLock.this.D = true;
            COUISimpleLock.this.G = false;
            COUISimpleLock.this.invalidate();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUISimpleLock.this.E = 5;
            COUISimpleLock.this.setInternalTranslationX(0.0f);
            COUISimpleLock.this.D = false;
            COUISimpleLock.this.G = true;
            this.f6460a.start();
            if (!COUISimpleLock.this.P) {
                if (COUISimpleLock.this.V) {
                    COUISimpleLock.this.K();
                    COUISimpleLock.this.V = false;
                    return;
                }
                return;
            }
            COUISimpleLock.this.P = false;
        }
    }

    /* loaded from: classes.dex */
    private final class h extends ExploreByTouchHelper {

        /* renamed from: a, reason: collision with root package name */
        private Rect f6462a;

        public h(View view) {
            super(view);
            this.f6462a = new Rect();
        }

        public CharSequence a(int i10) {
            if (COUISimpleLock.this.U != null && COUISimpleLock.this.S != null) {
                COUISimpleLock cOUISimpleLock = COUISimpleLock.this;
                cOUISimpleLock.U = cOUISimpleLock.U.replace('y', String.valueOf(COUISimpleLock.this.R).charAt(0));
                return COUISimpleLock.this.U.replace('x', String.valueOf(COUISimpleLock.this.S.size()).charAt(0));
            }
            return h.class.getSimpleName();
        }

        boolean b(int i10) {
            sendEventForVirtualView(i10, 1);
            return false;
        }

        public void c(int i10, Rect rect) {
            if (i10 < 0 || i10 >= 1) {
                return;
            }
            rect.set(0, 0, COUISimpleLock.this.f6453z, COUISimpleLock.this.f6449v);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float f10, float f11) {
            return (f10 < 0.0f || f10 > ((float) COUISimpleLock.this.f6453z) || f11 < 0.0f || f11 > ((float) COUISimpleLock.this.f6449v)) ? -2 : 0;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> list) {
            for (int i10 = 0; i10 < 1; i10++) {
                list.add(Integer.valueOf(i10));
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle) {
            if (i11 != 16) {
                return false;
            }
            return b(i10);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onPopulateEventForVirtualView(int i10, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.getText().add(a(i10));
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.Z(a(i10));
            accessibilityNodeInfoCompat.a(16);
            c(i10, this.f6462a);
            accessibilityNodeInfoCompat.Q(this.f6462a);
        }
    }

    public COUISimpleLock(Context context) {
        this(context, null);
    }

    private void A(Canvas canvas, int i10, int i11, int i12, int i13, int i14) {
        Drawable newDrawable = this.f6451x.getConstantState().newDrawable();
        this.f6452y = newDrawable;
        float f10 = this.N;
        newDrawable.setBounds((int) (i11 + f10), i10, (int) (i12 + f10), i13);
        this.f6452y.setAlpha(i14 > 0 ? 255 : 0);
        this.f6452y.draw(canvas);
    }

    private void B(Canvas canvas, int i10, int i11, int i12, int i13, float f10, float f11, int i14) {
        this.f6452y = this.f6451x.getConstantState().newDrawable();
        float f12 = this.N;
        this.f6452y.setBounds((int) (i11 + f12), (int) (i10 + I(i14, this.O)), (int) (i12 + f12), (int) (i13 + I(i14, this.O)));
        int I = (int) ((1.0f - (I(i14, this.O) / 150.0f)) * 140.0f);
        Drawable drawable = this.f6452y;
        if (I <= 0) {
            I = 0;
        }
        drawable.setAlpha(I);
        this.f6452y.draw(canvas);
    }

    private void C(Canvas canvas, int i10, int i11, int i12, int i13, int i14) {
        Drawable newDrawable = this.f6451x.getConstantState().newDrawable();
        this.f6452y = newDrawable;
        float f10 = this.N;
        newDrawable.setBounds((int) (i11 + f10), i10, (int) (i12 + f10), i13);
        this.f6452y.setAlpha(i14);
        this.f6452y.draw(canvas);
    }

    private void D(Canvas canvas, int i10) {
        int i11 = this.A;
        int i12 = this.f6449v + 0;
        if (this.B) {
            this.E = 0;
            H(canvas, this.f6432e);
            return;
        }
        int J = J();
        for (int i13 = 0; i13 < J; i13++) {
            int i14 = i11 + this.f6448u;
            E(canvas, i11, 0, i14, i12);
            if (i13 < i10) {
                z(canvas, i11, 0, i14, i12);
            }
            if (i13 == i10) {
                C(canvas, 0, i11, i14, i12, this.M);
            }
            i11 = this.f6446s + i14;
        }
    }

    private void E(Canvas canvas, int i10, int i11, int i12, int i13) {
        Drawable newDrawable = this.f6450w.getConstantState().newDrawable();
        this.f6452y = newDrawable;
        float f10 = this.N;
        newDrawable.setBounds((int) (i10 + f10), i11, (int) (i12 + f10), i13);
        this.f6452y.draw(canvas);
    }

    private void F(Canvas canvas, int i10, int i11, int i12, int i13, float f10, float f11) {
        Drawable newDrawable = this.f6450w.getConstantState().newDrawable();
        this.f6452y = newDrawable;
        float f12 = this.N;
        newDrawable.setBounds((int) (i11 + f12), i10, (int) (i12 + f12), i13);
        this.f6452y.draw(canvas);
    }

    private void G(Canvas canvas, int i10) {
        int i11 = this.A;
        int i12 = this.f6449v + 0;
        if (this.C) {
            this.E = 0;
            H(canvas, this.f6432e);
            return;
        }
        int J = J();
        for (int i13 = 0; i13 < J; i13++) {
            int i14 = i11 + this.f6448u;
            E(canvas, i11, 0, i14, i12);
            if (i13 < i10) {
                z(canvas, i11, 0, i14, i12);
            }
            if (i13 == i10) {
                A(canvas, 0, i11, i14, i12, this.M);
            }
            if (this.G) {
                B(canvas, 0, i11, i14, i12, 0.0f, 0.0f, i13);
            }
            i11 = i11 + this.f6448u + this.f6446s;
        }
    }

    private void H(Canvas canvas, int i10) {
        int i11 = this.A;
        int i12 = this.f6449v + 0;
        int J = J();
        for (int i13 = 0; i13 < J; i13++) {
            int i14 = i11 + this.f6448u;
            if (i13 <= i10) {
                z(canvas, i11, 0, i14, i12);
            }
            if (i13 > i10) {
                E(canvas, i11, 0, i14, i12);
            }
            i11 = this.f6446s + i14;
        }
    }

    private float I(int i10, float f10) {
        int i11 = this.R;
        if (i11 == 4) {
            float f11 = f10 - this.f6444q[i10];
            if (f11 >= 0.0f) {
                return f11;
            }
            return 0.0f;
        }
        if (i11 != 6) {
            return f10;
        }
        float f12 = f10 - this.f6445r[i10];
        if (f12 >= 0.0f) {
            return f12;
        }
        return 0.0f;
    }

    private int J() {
        int i10 = this.R;
        if (i10 == 4) {
            return 4;
        }
        return i10 == 6 ? 6 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void K() {
        if (this.W) {
            performHapticFeedback(304, 3);
        } else {
            performHapticFeedback(300, 3);
        }
    }

    private ValueAnimator u() {
        ValueAnimator valueAnimator = this.I;
        if (valueAnimator != null) {
            return valueAnimator;
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(255, 0);
        this.I = ofInt;
        ofInt.setInterpolator(this.f6431c0);
        this.I.setDuration(230L);
        this.I.addUpdateListener(new c());
        this.I.addListener(new d());
        return this.I;
    }

    private ValueAnimator v() {
        ValueAnimator valueAnimator = this.H;
        if (valueAnimator != null) {
            return valueAnimator;
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(0, 255);
        this.H = ofInt;
        ofInt.setDuration(230L);
        this.H.addUpdateListener(new a());
        this.H.addListener(new b());
        return this.H;
    }

    private void w(Canvas canvas, int i10) {
        int i11 = this.A;
        int i12 = this.f6449v + 0;
        if (this.C) {
            H(canvas, this.f6432e);
            this.E = 0;
            return;
        }
        int J = J();
        for (int i13 = 0; i13 < J; i13++) {
            int i14 = i11 + this.f6448u;
            E(canvas, i11, 0, i14, i12);
            if (i13 <= i10) {
                z(canvas, i11, 0, i14, i12);
            }
            if (i13 > i10) {
                A(canvas, 0, i11, i14, i12, this.M);
            }
            i11 = this.f6446s + i14;
        }
    }

    private void x(Canvas canvas, int i10) {
        int i11 = this.A;
        int i12 = this.f6449v + 0;
        if (this.B) {
            H(canvas, this.f6432e);
            this.E = 0;
            return;
        }
        int J = J();
        for (int i13 = 0; i13 < J; i13++) {
            int i14 = i11 + this.f6448u;
            E(canvas, i11, 0, i14, i12);
            if (i13 <= i10) {
                C(canvas, 0, i11, i14, i12, this.M);
            }
            i11 = this.f6446s + i14;
        }
    }

    private void y(Canvas canvas, int i10) {
        int i11 = this.A;
        int i12 = this.f6449v + 0;
        if (this.D) {
            this.E = 0;
            this.G = false;
            this.f6432e = -1;
            H(canvas, -1);
            return;
        }
        int J = J();
        for (int i13 = 0; i13 < J; i13++) {
            int i14 = i11 + this.f6448u;
            F(canvas, 0, i11, i14, i12, 0.0f, 0.0f);
            if (i13 <= i10) {
                B(canvas, 0, i11, i14, i12, 0.0f, 0.0f, i13);
            }
            i11 = i11 + this.f6448u + this.f6446s;
        }
    }

    private void z(Canvas canvas, int i10, int i11, int i12, int i13) {
        Drawable newDrawable = this.f6451x.getConstantState().newDrawable();
        this.f6452y = newDrawable;
        float f10 = this.N;
        newDrawable.setBounds((int) (i10 + f10), i11, (int) (i12 + f10), i13);
        this.f6452y.draw(canvas);
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        h hVar = this.T;
        if (hVar == null || !hVar.dispatchHoverEvent(motionEvent)) {
            return super.dispatchHoverEvent(motionEvent);
        }
        return true;
    }

    public Animator getAddAnimator() {
        return v();
    }

    public Animator getDeleteAnimator() {
        return u();
    }

    public Animator getFailedAnimator() {
        this.V = true;
        return t();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i10 = this.E;
        if (i10 == 1) {
            D(canvas, this.f6432e + 1);
            return;
        }
        if (i10 == 2) {
            G(canvas, this.f6432e);
            return;
        }
        if (i10 == 3) {
            x(canvas, this.F);
            return;
        }
        if (i10 == 4) {
            w(canvas, this.F);
        } else if (i10 != 5) {
            H(canvas, this.f6432e);
        } else {
            y(canvas, this.f6432e);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        int size = View.MeasureSpec.getSize(i10);
        this.f6453z = size;
        this.A = (size - this.f6447t) / 2;
        setMeasuredDimension(size, this.f6449v + Geofence.MIN_RADIUS);
    }

    public void setAllCode(boolean z10) {
        int i10 = this.R;
        if (i10 == 4) {
            if (this.G || this.f6432e >= 3) {
                return;
            }
            Animator animator = this.J;
            if (animator != null && animator.isRunning()) {
                return;
            }
        } else if (i10 == 6) {
            if (this.G || this.f6432e >= 5) {
                return;
            }
            Animator animator2 = this.J;
            if (animator2 != null && animator2.isRunning()) {
                return;
            }
        }
        if (z10) {
            ValueAnimator valueAnimator = this.I;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.I.end();
            }
            ValueAnimator valueAnimator2 = this.H;
            if (valueAnimator2 != null && valueAnimator2.isRunning()) {
                this.H.end();
            }
            this.E = 4;
            this.F = this.f6432e;
            int i11 = this.R;
            if (i11 == 4) {
                this.f6432e = 3;
            } else if (i11 == 6) {
                this.f6432e = 5;
            }
            ValueAnimator v7 = v();
            this.H = v7;
            v7.start();
        }
    }

    public void setClearAll(boolean z10) {
        int i10 = this.R;
        if (i10 == 4) {
            int i11 = this.f6432e;
            if (i11 == -1 || this.G || i11 > 3 || !z10) {
                return;
            }
            Animator animator = this.J;
            if (animator != null && animator.isRunning()) {
                return;
            }
        } else if (i10 == 6) {
            int i12 = this.f6432e;
            if (i12 == -1 || this.G || i12 > 5 || !z10) {
                return;
            }
            Animator animator2 = this.J;
            if (animator2 != null && animator2.isRunning()) {
                return;
            }
        }
        ValueAnimator valueAnimator = this.I;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.I.end();
        }
        ValueAnimator valueAnimator2 = this.H;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            this.H.end();
        }
        LinkedList<String> linkedList = this.S;
        if (linkedList != null) {
            linkedList.clear();
        }
        this.E = 3;
        this.F = this.f6432e;
        this.f6432e = -1;
        ValueAnimator u7 = u();
        this.I = u7;
        u7.start();
    }

    public void setDeleteLast(boolean z10) {
        int i10;
        int i11 = this.R;
        if ((i11 == 4 || i11 == 6) && ((i10 = this.f6432e) == -1 || !z10 || i10 >= i11 - 1)) {
            return;
        }
        LinkedList<String> linkedList = this.S;
        if (linkedList != null && !linkedList.isEmpty()) {
            this.S.removeFirst();
            String str = this.U;
            if (str != null && this.S != null) {
                this.U = str.replace('y', String.valueOf(this.R).charAt(0));
                announceForAccessibility(this.U.replace('x', String.valueOf(this.S.size()).charAt(0)));
            }
        }
        this.f6432e--;
        if (this.G) {
            return;
        }
        Animator animator = this.J;
        if (animator == null || !animator.isRunning()) {
            if (this.f6432e >= -1) {
                ValueAnimator valueAnimator = this.I;
                if (valueAnimator != null && valueAnimator.isRunning()) {
                    this.I.end();
                }
                ValueAnimator valueAnimator2 = this.H;
                if (valueAnimator2 != null && valueAnimator2.isRunning()) {
                    this.H.end();
                }
                this.E = 1;
                ValueAnimator u7 = u();
                this.I = u7;
                u7.start();
                return;
            }
            this.f6432e = -1;
        }
    }

    public void setFailed(boolean z10) {
        this.G = z10;
    }

    public void setFilledRectangleDrawable(Drawable drawable) {
        this.f6451x = drawable;
    }

    public void setFingerprintRecognition(boolean z10) {
        this.P = z10;
    }

    public void setInternalTranslationX(float f10) {
        this.N = f10;
    }

    public void setInternalTranslationY(float f10) {
        this.O = f10;
    }

    public void setOneCode(int i10) {
        int i11 = this.R;
        if (i11 == 4) {
            if (this.f6432e > 3) {
                return;
            }
        } else if (i11 == 6 && this.f6432e > 5) {
            return;
        }
        if (i11 == 4) {
            if (this.f6432e == 3) {
                this.f6432e = -1;
            }
        } else if (i11 == 6 && this.f6432e == 5) {
            this.f6432e = -1;
        }
        ValueAnimator valueAnimator = this.I;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.I.end();
        }
        ValueAnimator valueAnimator2 = this.H;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            this.H.end();
        }
        this.E = 2;
        this.f6432e++;
        ValueAnimator v7 = v();
        this.H = v7;
        v7.start();
        if (this.S != null) {
            String valueOf = String.valueOf(i10);
            if (this.f6432e != this.R - 1) {
                this.S.addFirst(valueOf);
            } else {
                this.S.clear();
            }
        }
    }

    public void setOpacity(int i10) {
        this.M = i10;
    }

    public void setOutlinedRectangleDrawable(Drawable drawable) {
        this.f6450w = drawable;
    }

    public void setRectanglePadding(int i10) {
        this.f6446s = i10;
    }

    public void setRectangleType(int i10) {
        this.Q = i10;
    }

    @Override // android.view.View
    public void setScaleX(float f10) {
        this.K = f10;
    }

    @Override // android.view.View
    public void setScaleY(float f10) {
        this.L = f10;
    }

    public void setSimpleLockType(int i10) {
        if (i10 == 0) {
            this.R = 4;
            this.f6447t = (this.f6448u * 4) + (this.f6446s * 3);
        } else if (i10 == 1) {
            this.R = 6;
            this.f6447t = (this.f6448u * 6) + (this.f6446s * 5);
        }
        this.A = (this.f6453z - this.f6447t) / 2;
        invalidate();
    }

    public Animator t() {
        Animator animator = this.J;
        if (animator != null) {
            return animator;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 30.0f, -28.0f, 14.0f, -8.0f, 4.0f, -3.0f, 0.0f);
        ofFloat.addUpdateListener(new e());
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 250.0f);
        ofFloat2.addUpdateListener(new f());
        ofFloat.setInterpolator(new COUILinearInterpolator());
        ofFloat2.setInterpolator(new COUILinearInterpolator());
        ofFloat.setDuration(800L);
        ofFloat2.setDuration(800L);
        ofFloat.addListener(new g(ofFloat2));
        this.J = ofFloat;
        return ofFloat;
    }

    public COUISimpleLock(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiSimpleLockStyle);
    }

    public COUISimpleLock(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, COUIContextUtil.e(context) ? R$style.Widget_COUI_COUISimpleLock_Dark : R$style.Widget_COUI_COUISimpleLock);
    }

    public COUISimpleLock(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6432e = -1;
        this.f6433f = 1;
        this.f6434g = 2;
        this.f6435h = 3;
        this.f6436i = 4;
        this.f6437j = 5;
        this.f6438k = 230;
        this.f6439l = 230;
        this.f6440m = DataLinkConstants.LONG_TERM_CHARGING_DATA;
        this.f6441n = 250;
        this.f6442o = Geofence.MIN_RADIUS;
        this.f6443p = new float[]{0.0f, 30.0f, -28.0f, 14.0f, -8.0f, 4.0f, -3.0f, 0.0f};
        this.f6444q = new float[]{0.0f, 38.5f, 91.0f, 63.0f};
        this.f6445r = new float[]{0.0f, 38.5f, 91.0f, 63.0f, 38.5f, 70.0f};
        this.f6447t = 0;
        this.f6452y = null;
        this.B = false;
        this.C = false;
        this.D = false;
        this.E = 0;
        this.G = false;
        this.H = null;
        this.I = null;
        this.J = null;
        this.K = 0.0f;
        this.L = 0.0f;
        this.M = 0;
        this.N = 0.0f;
        this.O = 0.0f;
        this.P = false;
        this.Q = -1;
        this.R = -1;
        this.S = null;
        this.T = null;
        this.U = null;
        this.V = true;
        this.f6431c0 = new COUIEaseInterpolator();
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.f6429a0 = attributeSet.getStyleAttribute();
        } else {
            this.f6429a0 = i10;
        }
        this.f6430b0 = context;
        COUIDarkModeUtil.b(this, false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUISimpleLock, i10, i11);
        this.f6446s = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUISimpleLock_couiRectanglePadding, 0);
        this.f6450w = obtainStyledAttributes.getDrawable(R$styleable.COUISimpleLock_couiOutLinedRectangleIconDrawable);
        this.f6451x = obtainStyledAttributes.getDrawable(R$styleable.COUISimpleLock_couiFilledRectangleIconDrawable);
        this.Q = obtainStyledAttributes.getInteger(R$styleable.COUISimpleLock_couiCircleNum, 0);
        obtainStyledAttributes.recycle();
        Drawable drawable = this.f6451x;
        if (drawable != null) {
            this.f6452y = drawable;
            this.f6448u = drawable.getIntrinsicWidth();
            this.f6449v = this.f6452y.getIntrinsicHeight();
            int i12 = this.Q;
            if (i12 == 0) {
                this.R = 4;
                this.f6447t = (this.f6448u * 4) + (this.f6446s * 3);
            } else if (i12 == 1) {
                this.R = 6;
                this.f6447t = (this.f6448u * 6) + (this.f6446s * 5);
            }
        }
        h hVar = new h(this);
        this.T = hVar;
        ViewCompat.l0(this, hVar);
        LinkedList<String> linkedList = new LinkedList<>();
        this.S = linkedList;
        linkedList.clear();
        this.U = context.getResources().getString(R$string.coui_simple_lock_access_description);
        setImportantForAccessibility(1);
        this.W = VibrateUtils.h(context);
    }
}
