package com.coui.appcompat.seekbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.AbsSeekBar;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.animation.PathInterpolatorCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import com.oplus.os.LinearmotorVibrator;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import com.support.control.R$attr;
import com.support.control.R$color;
import com.support.control.R$dimen;
import com.support.control.R$style;
import com.support.control.R$styleable;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import k3.VibrateUtils;
import n3.SpringConfig;
import n3.SpringListener;
import n3.SpringSystem;
import r7.AnimationListener;
import r7.AnimationUpdateListener;
import r7.FlingBehavior;
import r7.PhysicalAnimator;
import r7.j;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUISeekBar extends AbsSeekBar implements AnimationListener, AnimationUpdateListener {
    protected float A;
    private int A0;
    protected float B;
    private int B0;
    protected float C;
    private int C0;
    protected float D;
    private PhysicalAnimator D0;
    protected float E;
    private FlingBehavior E0;
    protected float F;
    private j F0;
    protected float G;
    private float G0;
    protected float H;
    private float H0;
    private boolean I;
    private float I0;
    protected float J;
    private float J0;
    protected float K;
    protected float L;
    protected float M;
    protected Bitmap N;
    protected Path O;
    protected RectF P;
    protected RectF Q;
    protected RectF R;
    protected AnimatorSet S;
    protected AnimatorSet T;
    protected float U;
    protected Paint V;
    protected float W;

    /* renamed from: a0, reason: collision with root package name */
    protected Interpolator f7551a0;

    /* renamed from: b0, reason: collision with root package name */
    protected Interpolator f7552b0;

    /* renamed from: c0, reason: collision with root package name */
    protected float f7553c0;

    /* renamed from: d0, reason: collision with root package name */
    protected boolean f7554d0;

    /* renamed from: e, reason: collision with root package name */
    protected float f7555e;

    /* renamed from: e0, reason: collision with root package name */
    protected boolean f7556e0;

    /* renamed from: f, reason: collision with root package name */
    protected boolean f7557f;

    /* renamed from: f0, reason: collision with root package name */
    private n3.f f7558f0;

    /* renamed from: g, reason: collision with root package name */
    protected boolean f7559g;

    /* renamed from: g0, reason: collision with root package name */
    private int f7560g0;

    /* renamed from: h, reason: collision with root package name */
    protected boolean f7561h;

    /* renamed from: h0, reason: collision with root package name */
    private h f7562h0;

    /* renamed from: i, reason: collision with root package name */
    protected Object f7563i;

    /* renamed from: i0, reason: collision with root package name */
    private boolean f7564i0;

    /* renamed from: j, reason: collision with root package name */
    protected int f7565j;

    /* renamed from: j0, reason: collision with root package name */
    private RectF f7566j0;

    /* renamed from: k, reason: collision with root package name */
    protected float f7567k;

    /* renamed from: k0, reason: collision with root package name */
    private int f7568k0;

    /* renamed from: l, reason: collision with root package name */
    protected int f7569l;

    /* renamed from: l0, reason: collision with root package name */
    private i f7570l0;

    /* renamed from: m, reason: collision with root package name */
    protected int f7571m;

    /* renamed from: m0, reason: collision with root package name */
    private int f7572m0;

    /* renamed from: n, reason: collision with root package name */
    protected int f7573n;

    /* renamed from: n0, reason: collision with root package name */
    private float f7574n0;

    /* renamed from: o, reason: collision with root package name */
    protected int f7575o;

    /* renamed from: o0, reason: collision with root package name */
    private SpringConfig f7576o0;

    /* renamed from: p, reason: collision with root package name */
    protected boolean f7577p;

    /* renamed from: p0, reason: collision with root package name */
    private VelocityTracker f7578p0;

    /* renamed from: q, reason: collision with root package name */
    ColorStateList f7579q;

    /* renamed from: q0, reason: collision with root package name */
    private boolean f7580q0;

    /* renamed from: r, reason: collision with root package name */
    ColorStateList f7581r;

    /* renamed from: r0, reason: collision with root package name */
    private float f7582r0;

    /* renamed from: s, reason: collision with root package name */
    ColorStateList f7583s;

    /* renamed from: s0, reason: collision with root package name */
    private Interpolator f7584s0;

    /* renamed from: t, reason: collision with root package name */
    protected int f7585t;

    /* renamed from: t0, reason: collision with root package name */
    private int f7586t0;

    /* renamed from: u, reason: collision with root package name */
    protected int f7587u;

    /* renamed from: u0, reason: collision with root package name */
    private String f7588u0;

    /* renamed from: v, reason: collision with root package name */
    protected int f7589v;

    /* renamed from: v0, reason: collision with root package name */
    private int f7590v0;

    /* renamed from: w, reason: collision with root package name */
    protected int f7591w;

    /* renamed from: w0, reason: collision with root package name */
    private TextDrawable f7592w0;

    /* renamed from: x, reason: collision with root package name */
    protected float f7593x;

    /* renamed from: x0, reason: collision with root package name */
    private boolean f7594x0;

    /* renamed from: y, reason: collision with root package name */
    protected float f7595y;

    /* renamed from: y0, reason: collision with root package name */
    private ExecutorService f7596y0;

    /* renamed from: z, reason: collision with root package name */
    protected float f7597z;

    /* renamed from: z0, reason: collision with root package name */
    private int f7598z0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f7599e;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        /* synthetic */ SavedState(Parcel parcel, a aVar) {
            this(parcel);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f7599e);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.f7599e = parcel.readInt();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements SpringListener {
        a() {
        }

        @Override // n3.SpringListener
        public void onSpringActivate(n3.f fVar) {
        }

        @Override // n3.SpringListener
        public void onSpringAtRest(n3.f fVar) {
        }

        @Override // n3.SpringListener
        public void onSpringEndStateChange(n3.f fVar) {
        }

        @Override // n3.SpringListener
        public void onSpringUpdate(n3.f fVar) {
            if (COUISeekBar.this.f7574n0 != fVar.e()) {
                if (COUISeekBar.this.isEnabled()) {
                    COUISeekBar.this.f7574n0 = (float) fVar.c();
                } else {
                    COUISeekBar.this.f7574n0 = 0.0f;
                }
                COUISeekBar.this.invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISeekBar.this.H(valueAnimator);
            COUISeekBar.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements Animator.AnimatorListener {
        c() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (COUISeekBar.this.f7562h0 != null) {
                h hVar = COUISeekBar.this.f7562h0;
                COUISeekBar cOUISeekBar = COUISeekBar.this;
                hVar.b(cOUISeekBar, cOUISeekBar.f7569l, true);
            }
            COUISeekBar.this.J();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUISeekBar.this.f7562h0 != null) {
                h hVar = COUISeekBar.this.f7562h0;
                COUISeekBar cOUISeekBar = COUISeekBar.this;
                hVar.b(cOUISeekBar, cOUISeekBar.f7569l, true);
            }
            COUISeekBar.this.J();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUISeekBar.this.I();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ float f7603a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f7604b;

        d(float f10, int i10) {
            this.f7603a = f10;
            this.f7604b = i10;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            COUISeekBar.this.setLocalProgress((int) (floatValue / this.f7603a));
            COUISeekBar cOUISeekBar = COUISeekBar.this;
            cOUISeekBar.f7555e = (floatValue - (cOUISeekBar.f7575o * this.f7603a)) / this.f7604b;
            cOUISeekBar.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements ValueAnimator.AnimatorUpdateListener {
        e() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUISeekBar.this.F = ((Float) valueAnimator.getAnimatedValue("progressRadius")).floatValue();
            COUISeekBar.this.A = ((Float) valueAnimator.getAnimatedValue("backgroundRadius")).floatValue();
            COUISeekBar.this.E = ((Float) valueAnimator.getAnimatedValue("progressHeight")).floatValue();
            COUISeekBar.this.f7597z = ((Float) valueAnimator.getAnimatedValue("backgroundHeight")).floatValue();
            COUISeekBar.this.M = ((Float) valueAnimator.getAnimatedValue("animatePadding")).floatValue();
            COUISeekBar.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f implements Runnable {
        f() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUISeekBar cOUISeekBar = COUISeekBar.this;
            if (cOUISeekBar.f7577p) {
                cOUISeekBar.performHapticFeedback(305, 0);
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
            COUISeekBar cOUISeekBar = COUISeekBar.this;
            if (cOUISeekBar.f7577p) {
                LinearmotorVibrator linearmotorVibrator = (LinearmotorVibrator) cOUISeekBar.f7563i;
                int i10 = cOUISeekBar.f7569l;
                int i11 = cOUISeekBar.f7575o;
                VibrateUtils.j(linearmotorVibrator, 152, i10 - i11, cOUISeekBar.f7573n - i11, 200, 2000);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface h {
        void a(COUISeekBar cOUISeekBar);

        void b(COUISeekBar cOUISeekBar, int i10, boolean z10);

        void c(COUISeekBar cOUISeekBar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class i extends ExploreByTouchHelper {

        /* renamed from: a, reason: collision with root package name */
        private Rect f7609a;

        public i(View view) {
            super(view);
            this.f7609a = new Rect();
        }

        private Rect a(int i10) {
            Rect rect = this.f7609a;
            rect.left = 0;
            rect.top = 0;
            rect.right = COUISeekBar.this.getWidth();
            rect.bottom = COUISeekBar.this.getHeight();
            return rect;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float f10, float f11) {
            return (f10 < 0.0f || f10 > ((float) COUISeekBar.this.getWidth()) || f11 < 0.0f || f11 > ((float) COUISeekBar.this.getHeight())) ? -1 : 0;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> list) {
            for (int i10 = 0; i10 < 1; i10++) {
                list.add(Integer.valueOf(i10));
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper, androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.b(AccessibilityNodeInfoCompat.a.L);
            accessibilityNodeInfoCompat.p0(AccessibilityNodeInfoCompat.d.a(1, COUISeekBar.this.getMin(), COUISeekBar.this.getMax(), COUISeekBar.this.f7569l));
            if (COUISeekBar.this.isEnabled()) {
                int progress = COUISeekBar.this.getProgress();
                if (progress > COUISeekBar.this.getMin()) {
                    accessibilityNodeInfoCompat.a(8192);
                }
                if (progress < COUISeekBar.this.getMax()) {
                    accessibilityNodeInfoCompat.a(4096);
                }
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle) {
            sendEventForVirtualView(i10, 4);
            return false;
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onPopulateEventForVirtualView(int i10, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.getText().add(i.class.getSimpleName());
            accessibilityEvent.setItemCount(COUISeekBar.this.getMax() - COUISeekBar.this.getMin());
            accessibilityEvent.setCurrentItemIndex(COUISeekBar.this.getProgress());
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.Z("");
            accessibilityNodeInfoCompat.V(COUISeekBar.class.getName());
            accessibilityNodeInfoCompat.Q(a(i10));
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean performAccessibilityAction(View view, int i10, Bundle bundle) {
            if (super.performAccessibilityAction(view, i10, bundle)) {
                return true;
            }
            if (!COUISeekBar.this.isEnabled()) {
                return false;
            }
            if (i10 == 4096) {
                COUISeekBar cOUISeekBar = COUISeekBar.this;
                cOUISeekBar.Q(cOUISeekBar.getProgress() + COUISeekBar.this.f7560g0, false, true);
                COUISeekBar cOUISeekBar2 = COUISeekBar.this;
                cOUISeekBar2.announceForAccessibility(cOUISeekBar2.f7588u0);
                return true;
            }
            if (i10 != 8192) {
                return false;
            }
            COUISeekBar cOUISeekBar3 = COUISeekBar.this;
            cOUISeekBar3.Q(cOUISeekBar3.getProgress() - COUISeekBar.this.f7560g0, false, true);
            COUISeekBar cOUISeekBar4 = COUISeekBar.this;
            cOUISeekBar4.announceForAccessibility(cOUISeekBar4.f7588u0);
            return true;
        }
    }

    public COUISeekBar(Context context) {
        this(context, null);
    }

    private void A() {
        this.f7558f0.p(this.f7576o0);
        this.f7558f0.a(new a());
        this.S.setInterpolator(this.f7551a0);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(183L);
        ofFloat.addUpdateListener(new b());
        this.S.play(ofFloat);
    }

    private void B() {
        VelocityTracker velocityTracker = this.f7578p0;
        if (velocityTracker == null) {
            this.f7578p0 = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void C(Context context) {
        this.D0 = PhysicalAnimator.e(context);
        this.F0 = new j(0.0f);
        FlingBehavior flingBehavior = (FlingBehavior) ((FlingBehavior) new FlingBehavior(0.0f, getNormalSeekBarWidth()).I(this.F0)).z(this.H0, this.I0).b(null);
        this.E0 = flingBehavior;
        flingBehavior.h0(this.J0);
        this.D0.c(this.E0);
        this.D0.a(this.E0, this);
        this.D0.b(this.E0, this);
    }

    private void D() {
        if (this.f7578p0 == null) {
            this.f7578p0 = VelocityTracker.obtain();
        }
    }

    private void E() {
        this.f7565j = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        i iVar = new i(this);
        this.f7570l0 = iVar;
        ViewCompat.l0(this, iVar);
        ViewCompat.w0(this, 1);
        this.f7570l0.invalidateRoot();
        Paint paint = new Paint();
        this.V = paint;
        paint.setAntiAlias(true);
        this.V.setDither(true);
    }

    private void F(MotionEvent motionEvent) {
        float start;
        float x10 = motionEvent.getX();
        float seekBarWidth = getSeekBarWidth();
        float f10 = this.F;
        float f11 = seekBarWidth + (2.0f * f10);
        float f12 = this.M - f10;
        if (isLayoutRtl()) {
            start = (((getWidth() - x10) - getStart()) - f12) / f11;
        } else {
            start = ((x10 - getStart()) - f12) / f11;
        }
        this.f7555e = Math.max(0.0f, Math.min(start, 1.0f));
        int w10 = w(Math.round((this.f7555e * (getMax() - getMin())) + getMin()));
        int i10 = this.f7569l;
        setLocalProgress(w10);
        invalidate();
        int i11 = this.f7569l;
        if (i10 != i11) {
            h hVar = this.f7562h0;
            if (hVar != null) {
                hVar.b(this, i11, true);
            }
            M();
        }
    }

    private boolean G() {
        return this.f7568k0 != 2;
    }

    private void N() {
        VelocityTracker velocityTracker = this.f7578p0;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.f7578p0 = null;
        }
    }

    private void P() {
        if (this.I) {
            this.D = this.f7595y;
            this.C = this.f7593x;
            this.H = this.B;
        }
    }

    private void S(float f10) {
        if (this.f7558f0.c() == this.f7558f0.e()) {
            int i10 = this.f7573n - this.f7575o;
            if (f10 >= 95.0f) {
                int i11 = this.f7569l;
                float f11 = i10;
                if (i11 > 0.95f * f11 || i11 < f11 * 0.05f) {
                    return;
                }
                this.f7558f0.o(1.0d);
                return;
            }
            if (f10 <= -95.0f) {
                int i12 = this.f7569l;
                float f12 = i10;
                if (i12 > 0.95f * f12 || i12 < f12 * 0.05f) {
                    return;
                }
                this.f7558f0.o(-1.0d);
                return;
            }
            this.f7558f0.o(UserProfileInfo.Constant.NA_LAT_LON);
        }
    }

    private void X(MotionEvent motionEvent) {
        float x10 = motionEvent.getX();
        float f10 = x10 - this.U;
        int i10 = this.f7573n - this.f7575o;
        if (isLayoutRtl()) {
            f10 = -f10;
        }
        float f11 = i10;
        int w10 = w(this.f7569l + Math.round(((f10 * l(x10)) / getSeekBarWidth()) * f11));
        int i11 = this.f7569l;
        setLocalProgress(w10);
        this.f7555e = i10 > 0 ? (this.f7569l - this.f7575o) / f11 : 0.0f;
        invalidate();
        int i12 = this.f7569l;
        if (i11 != i12) {
            this.U = x10;
            h hVar = this.f7562h0;
            if (hVar != null) {
                hVar.b(this, i12, true);
            }
            M();
        }
        this.f7578p0.computeCurrentVelocity(100);
        S(this.f7578p0.getXVelocity());
    }

    private void Y(MotionEvent motionEvent) {
        int start;
        float f10;
        int round = Math.round(((motionEvent.getX() - this.U) * l(motionEvent.getX())) + this.U);
        int width = getWidth();
        int width2 = (getWidth() - getStart()) - getEnd();
        if (isLayoutRtl()) {
            if (round <= width - getStart()) {
                if (round >= getEnd()) {
                    start = (width - round) - getEnd();
                    f10 = start / width2;
                }
                f10 = 1.0f;
            }
            f10 = 0.0f;
        } else {
            if (round >= getStart()) {
                if (round <= width - getEnd()) {
                    start = round - getStart();
                    f10 = start / width2;
                }
                f10 = 1.0f;
            }
            f10 = 0.0f;
        }
        this.f7555e = Math.max(0.0f, Math.min(f10, 1.0f));
        int w10 = w(Math.round((this.f7555e * (getMax() - getMin())) + getMin()));
        int i10 = this.f7569l;
        setLocalProgress(w10);
        invalidate();
        int i11 = this.f7569l;
        if (i10 != i11) {
            this.U = round;
            h hVar = this.f7562h0;
            if (hVar != null) {
                hVar.b(this, i11, true);
            }
            M();
        }
    }

    private void Z() {
        FlingBehavior flingBehavior;
        if (!this.f7594x0 || this.D0 == null || (flingBehavior = this.E0) == null) {
            return;
        }
        flingBehavior.f0(0.0f, getNormalSeekBarWidth());
    }

    private int getNormalSeekBarWidth() {
        return (int) (((getWidth() - getStart()) - getEnd()) - (this.L * 2.0f));
    }

    private void k() {
        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).requestDisallowInterceptTouchEvent(true);
        }
    }

    private float l(float f10) {
        float f11 = this.f7582r0;
        if (f11 != 0.0f) {
            return f11;
        }
        float seekBarWidth = getSeekBarWidth();
        float f12 = seekBarWidth / 2.0f;
        float interpolation = 1.0f - this.f7584s0.getInterpolation(Math.abs(f10 - f12) / f12);
        if (f10 > seekBarWidth - getPaddingRight() || f10 < getPaddingLeft() || interpolation < 0.4f) {
            return 0.4f;
        }
        return interpolation;
    }

    private void q(Canvas canvas, int i10, float f10, float f11) {
        if (this.C0 > 0 && this.F > this.D) {
            this.V.setStyle(Paint.Style.STROKE);
            this.V.setStrokeWidth(0.0f);
            this.V.setColor(0);
            this.V.setShadowLayer(this.C0, 0.0f, 0.0f, this.f7598z0);
            RectF rectF = this.Q;
            int i11 = this.C0;
            float f12 = this.F;
            float f13 = i10;
            float f14 = this.E;
            rectF.set((f10 - (i11 / 2)) - f12, (f13 - (f14 / 2.0f)) - (i11 / 2), (i11 / 2) + f11 + f12, f13 + (f14 / 2.0f) + (i11 / 2));
            RectF rectF2 = this.Q;
            float f15 = this.F;
            canvas.drawRoundRect(rectF2, f15, f15, this.V);
            this.V.clearShadowLayer();
            this.V.setStyle(Paint.Style.FILL);
        }
        this.V.setColor(this.f7585t);
        if (this.f7580q0 && f10 > f11) {
            RectF rectF3 = this.Q;
            float f16 = i10;
            float f17 = this.E;
            rectF3.set(f11, f16 - (f17 / 2.0f), f10, f16 + (f17 / 2.0f));
        } else {
            RectF rectF4 = this.Q;
            float f18 = i10;
            float f19 = this.E;
            rectF4.set(f10, f18 - (f19 / 2.0f), f11, f18 + (f19 / 2.0f));
        }
        this.O.reset();
        Path path = this.O;
        RectF rectF5 = this.P;
        float f20 = this.F;
        path.addRoundRect(rectF5, f20, f20, Path.Direction.CCW);
        canvas.save();
        canvas.clipPath(this.O);
        if (this.f7556e0) {
            RectF rectF6 = this.Q;
            float f21 = rectF6.left;
            float f22 = this.J;
            rectF6.left = f21 - (f22 / 2.0f);
            rectF6.right += f22 / 2.0f;
            float f23 = this.F;
            canvas.drawRoundRect(rectF6, f23, f23, this.V);
        } else {
            canvas.drawRect(this.Q, this.V);
        }
        canvas.restore();
    }

    private void r(Canvas canvas, int i10, float f10, float f11) {
        if (this.B0 > 0 && this.F < this.K) {
            this.V.setStyle(Paint.Style.FILL);
            this.V.setShadowLayer(this.B0, 0.0f, 8.0f, this.f7598z0);
        }
        if (getThumb() != null) {
            canvas.drawBitmap(s(getThumb()), f10, i10 - (this.J / 2.0f), this.V);
        } else {
            this.V.setColor(this.f7589v);
            float f12 = i10;
            float f13 = this.J;
            float f14 = this.K;
            canvas.drawRoundRect(f10, f12 - (f13 / 2.0f), f11, f12 + (f13 / 2.0f), f14, f14, this.V);
        }
        this.V.clearShadowLayer();
    }

    private Bitmap s(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int max = Math.max(1, drawable.getIntrinsicHeight());
        int max2 = Math.max(1, drawable.getIntrinsicWidth());
        this.N = Bitmap.createBitmap(max2, max, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.N);
        drawable.setBounds(0, 0, max2, max);
        drawable.draw(canvas);
        return this.N;
    }

    private void t() {
        P();
        this.f7553c0 = this.B != 1.0f ? (getResources().getDimensionPixelSize(R$dimen.coui_seekbar_progress_pressed_padding_horizontal) + (this.f7595y * this.B)) / this.L : 1.0f;
        float f10 = this.D;
        this.F = f10;
        this.A = this.f7595y;
        float f11 = this.H;
        this.K = f10 * f11;
        float f12 = this.C;
        this.E = f12;
        this.f7597z = this.f7593x;
        this.J = f12 * f11;
        this.M = this.L;
        Z();
    }

    private void u(float f10) {
        int normalSeekBarWidth = getNormalSeekBarWidth();
        int i10 = this.f7573n - this.f7575o;
        float f11 = i10 > 0 ? normalSeekBarWidth / i10 : 0.0f;
        if (isLayoutRtl()) {
            this.F0.c(((this.f7573n - this.f7569l) + this.f7575o) * f11);
        } else {
            this.F0.c((this.f7569l - this.f7575o) * f11);
        }
        this.E0.j0(f10);
    }

    private int w(int i10) {
        return Math.max(this.f7575o, Math.min(i10, this.f7573n));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void H(ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float f10 = this.f7595y;
        float f11 = this.B;
        this.A = f10 + (((f10 * f11) - f10) * animatedFraction);
        float f12 = this.D;
        float f13 = this.H;
        this.F = f12 + (((f12 * f13) - f12) * animatedFraction);
        float f14 = this.f7593x;
        this.f7597z = f14 + (((f11 * f14) - f14) * animatedFraction);
        float f15 = this.C;
        this.E = f15 + (((f13 * f15) - f15) * animatedFraction);
        float f16 = this.L;
        this.M = f16 + (animatedFraction * ((this.f7553c0 * f16) - f16));
    }

    void I() {
        this.f7577p = true;
        this.f7564i0 = true;
        h hVar = this.f7562h0;
        if (hVar != null) {
            hVar.c(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J() {
        K(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void K(boolean z10) {
        h hVar;
        this.f7577p = false;
        this.f7564i0 = false;
        if (!z10 || (hVar = this.f7562h0) == null) {
            return;
        }
        hVar.a(this);
    }

    protected boolean L() {
        if (this.f7563i == null) {
            LinearmotorVibrator e10 = VibrateUtils.e(getContext());
            this.f7563i = e10;
            this.f7561h = e10 != null;
        }
        if (this.f7563i == null) {
            return false;
        }
        if (this.f7569l != getMax() && this.f7569l != 0) {
            if (this.f7596y0 == null) {
                this.f7596y0 = Executors.newSingleThreadExecutor();
            }
            this.f7596y0.execute(new g());
        } else {
            LinearmotorVibrator linearmotorVibrator = (LinearmotorVibrator) this.f7563i;
            int i10 = this.f7569l;
            int i11 = this.f7575o;
            VibrateUtils.j(linearmotorVibrator, 154, i10 - i11, this.f7573n - i11, DataLinkConstants.LONG_TERM_CHARGING_DATA, 1200);
        }
        return true;
    }

    protected void M() {
        if (this.f7557f) {
            if (this.f7561h && this.f7559g && L()) {
                return;
            }
            if (this.f7569l != getMax() && this.f7569l != getMin()) {
                if (this.f7596y0 == null) {
                    this.f7596y0 = Executors.newSingleThreadExecutor();
                }
                this.f7596y0.execute(new f());
                return;
            }
            performHapticFeedback(306, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void O() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setValues(PropertyValuesHolder.ofFloat("progressRadius", this.F, this.D), PropertyValuesHolder.ofFloat("backgroundRadius", this.A, this.f7595y), PropertyValuesHolder.ofFloat("progressHeight", this.E, this.C), PropertyValuesHolder.ofFloat("backgroundHeight", this.f7597z, this.f7593x), PropertyValuesHolder.ofFloat("animatePadding", this.M, this.L));
        valueAnimator.setDuration(183L);
        valueAnimator.setInterpolator(this.f7551a0);
        valueAnimator.addUpdateListener(new e());
        this.S.cancel();
        valueAnimator.cancel();
        valueAnimator.start();
    }

    public void Q(int i10, boolean z10, boolean z11) {
        this.f7571m = this.f7569l;
        int max = Math.max(this.f7575o, Math.min(i10, this.f7573n));
        if (this.f7571m != max) {
            if (z10) {
                j(max);
                return;
            }
            setLocalProgress(max);
            this.f7571m = max;
            int i11 = this.f7573n - this.f7575o;
            this.f7555e = i11 > 0 ? (this.f7569l - r0) / i11 : 0.0f;
            h hVar = this.f7562h0;
            if (hVar != null) {
                hVar.b(this, max, z11);
            }
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void R() {
        setPressed(true);
        I();
        k();
    }

    public void T() {
        FlingBehavior flingBehavior;
        if (!this.f7594x0 || this.D0 == null || (flingBehavior = this.E0) == null) {
            return;
        }
        flingBehavior.l0();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float U(float f10, float f11) {
        return new BigDecimal(Float.toString(f10)).subtract(new BigDecimal(Float.toString(f11))).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void V() {
        if (this.S.isRunning()) {
            this.S.cancel();
        }
        this.S.start();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean W(MotionEvent motionEvent, View view) {
        float x10 = motionEvent.getX();
        float y4 = motionEvent.getY();
        return x10 >= ((float) view.getPaddingLeft()) && x10 <= ((float) (view.getWidth() - view.getPaddingRight())) && y4 >= 0.0f && y4 <= ((float) view.getHeight());
    }

    @Override // r7.AnimationListener
    public void a(r7.c cVar) {
        J();
    }

    @Override // r7.AnimationUpdateListener
    public void b(r7.c cVar) {
        float f10;
        float floatValue = ((Float) cVar.n()).floatValue();
        int normalSeekBarWidth = getNormalSeekBarWidth();
        if (isLayoutRtl()) {
            float f11 = normalSeekBarWidth;
            f10 = (f11 - floatValue) / f11;
        } else {
            f10 = floatValue / normalSeekBarWidth;
        }
        float max = Math.max(0.0f, Math.min(f10, 1.0f));
        this.f7555e = max;
        float f12 = this.f7569l;
        setLocalProgress(w(Math.round((this.f7573n - this.f7575o) * max) + this.f7575o));
        invalidate();
        if (f12 != this.f7569l) {
            this.U = floatValue + getStart();
            h hVar = this.f7562h0;
            if (hVar != null) {
                hVar.b(this, this.f7569l, true);
            }
        }
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return super.dispatchHoverEvent(motionEvent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getEnd() {
        return getPaddingEnd();
    }

    public int getLabelHeight() {
        return this.f7592w0.getIntrinsicHeight();
    }

    @Override // android.widget.ProgressBar
    public int getMax() {
        return this.f7573n;
    }

    @Override // android.widget.ProgressBar
    public int getMin() {
        return this.f7575o;
    }

    public float getMoveDamping() {
        return this.f7582r0;
    }

    public int getMoveType() {
        return this.f7568k0;
    }

    @Override // android.widget.ProgressBar
    public int getProgress() {
        return this.f7569l;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getSeekBarCenterY() {
        return getPaddingTop() + (((getHeight() - getPaddingBottom()) - getPaddingTop()) >> 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getSeekBarWidth() {
        return (int) (((getWidth() - getStart()) - getEnd()) - (this.M * 2.0f));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getStart() {
        return getPaddingStart();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void i(float f10) {
        float start;
        float seekBarWidth = getSeekBarWidth();
        float f11 = this.F;
        float f12 = seekBarWidth + (2.0f * f11);
        float f13 = this.M - f11;
        if (isLayoutRtl()) {
            start = (((getWidth() - f10) - getStart()) - f13) / f12;
        } else {
            start = ((f10 - getStart()) - f13) / f12;
        }
        j(w(Math.round((start * (getMax() - getMin())) + getMin())));
    }

    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    protected void j(int i10) {
        AnimatorSet animatorSet = this.T;
        if (animatorSet == null) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.T = animatorSet2;
            animatorSet2.addListener(new c());
        } else {
            animatorSet.cancel();
        }
        int i11 = this.f7569l;
        int seekBarWidth = getSeekBarWidth();
        int i12 = this.f7573n - this.f7575o;
        float f10 = i12 > 0 ? seekBarWidth / i12 : 0.0f;
        if (f10 > 0.0f) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(i11 * f10, i10 * f10);
            ofFloat.setInterpolator(this.f7552b0);
            ofFloat.addUpdateListener(new d(f10, seekBarWidth));
            long abs = (i12 > 0 ? Math.abs(i10 - i11) / i12 : 0.0f) * 483.0f;
            if (abs < 150) {
                abs = 150;
            }
            this.T.setDuration(abs);
            this.T.play(ofFloat);
            this.T.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void m(int i10) {
        n(i10, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void n(int i10, boolean z10) {
        if (this.f7569l != i10) {
            setLocalProgress(i10);
            h hVar = this.f7562h0;
            if (hVar != null) {
                hVar.b(this, this.f7569l, true);
            }
            if (z10) {
                M();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void o(Canvas canvas, float f10) {
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        int seekBarCenterY = getSeekBarCenterY();
        if (!this.f7556e0) {
            float f18 = this.M;
            float f19 = this.F;
            f13 = f10 + (f19 * 2.0f);
            f14 = f18 - f19;
            f11 = f14;
            f12 = f13;
        } else {
            float f20 = this.M;
            float f21 = this.J;
            float f22 = this.K;
            f11 = ((f21 / 2.0f) - f22) + f20;
            float f23 = f10 - (f21 - (f22 * 2.0f));
            float f24 = this.F;
            float f25 = f20 - f24;
            f12 = f10 + (f24 * 2.0f);
            f13 = f23;
            f14 = f25;
        }
        RectF rectF = this.P;
        float f26 = seekBarCenterY;
        float f27 = this.E;
        rectF.top = f26 - (f27 / 2.0f);
        rectF.bottom = f26 + (f27 / 2.0f);
        if (this.f7580q0) {
            if (isLayoutRtl()) {
                f16 = getWidth() / 2.0f;
                f17 = f16 - ((this.f7555e - 0.5f) * f13);
                RectF rectF2 = this.P;
                float f28 = f12 / 2.0f;
                rectF2.left = f16 - f28;
                rectF2.right = f28 + f16;
                f15 = f17;
            } else {
                float width = getWidth() / 2.0f;
                float f29 = width + ((this.f7555e - 0.5f) * f13);
                RectF rectF3 = this.P;
                float f30 = f12 / 2.0f;
                rectF3.left = width - f30;
                rectF3.right = f30 + width;
                f15 = f29;
                f17 = width;
                f16 = f15;
            }
        } else if (isLayoutRtl()) {
            float start = getStart() + f11 + f13;
            f17 = start - (this.f7555e * f13);
            this.P.right = getStart() + f14 + f12;
            RectF rectF4 = this.P;
            rectF4.left = rectF4.right - f12;
            f15 = f17;
            f16 = start;
        } else {
            float start2 = f11 + getStart();
            float f31 = start2 + (this.f7555e * f13);
            this.P.left = getStart() + f14;
            RectF rectF5 = this.P;
            rectF5.right = rectF5.left + f12;
            f15 = f31;
            f16 = f15;
            f17 = start2;
        }
        if (this.f7554d0) {
            q(canvas, seekBarCenterY, f17, f16);
        }
        float f32 = this.J;
        float f33 = f15 - (f32 / 2.0f);
        float f34 = f15 + (f32 / 2.0f);
        this.W = ((f34 - f33) / 2.0f) + f33;
        if (this.f7556e0) {
            r(canvas, seekBarCenterY, f33, f34);
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        VibrateUtils.i(getContext());
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        T();
        VibrateUtils.l();
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    protected void onDraw(Canvas canvas) {
        float seekBarWidth = getSeekBarWidth();
        p(canvas);
        o(canvas, seekBarWidth);
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    protected void onMeasure(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i11);
        int size = View.MeasureSpec.getSize(i11);
        int size2 = View.MeasureSpec.getSize(i10);
        int paddingTop = this.f7572m0 + getPaddingTop() + getPaddingBottom();
        if (1073741824 != mode || size < paddingTop) {
            size = paddingTop;
        }
        int i12 = this.f7590v0;
        if (i12 > 0 && size2 > i12) {
            size2 = i12;
        }
        setMeasuredDimension(size2, size);
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setProgress(savedState.f7599e);
    }

    @Override // android.widget.ProgressBar, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f7599e = this.f7569l;
        return savedState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        this.f7564i0 = false;
        T();
        Z();
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0026, code lost:
    
        if (r0 != 3) goto L25;
     */
    @Override // android.widget.AbsSeekBar, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            if (motionEvent.getAction() != 1 && motionEvent.getAction() != 3) {
                return false;
            }
            z(motionEvent);
            return true;
        }
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    D();
                    this.f7578p0.addMovement(motionEvent);
                    y(motionEvent);
                }
            }
            this.f7578p0.computeCurrentVelocity(1000, 8000.0f);
            this.G0 = this.f7578p0.getXVelocity();
            N();
            z(motionEvent);
        } else {
            if (this.f7594x0) {
                this.E0.l0();
            }
            B();
            this.f7578p0.addMovement(motionEvent);
            this.f7577p = false;
            this.f7564i0 = false;
            x(motionEvent);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void p(Canvas canvas) {
        float start = (getStart() + this.M) - this.A;
        float width = ((getWidth() - getEnd()) - this.M) + this.A;
        int seekBarCenterY = getSeekBarCenterY();
        if (this.A0 > 0) {
            this.V.setStyle(Paint.Style.STROKE);
            this.V.setStrokeWidth(0.0f);
            this.V.setColor(0);
            this.V.setShadowLayer(this.A0, 0.0f, 0.0f, this.f7598z0);
            RectF rectF = this.f7566j0;
            int i10 = this.A0;
            float f10 = seekBarCenterY;
            float f11 = this.f7597z;
            rectF.set(start - (i10 / 2), (f10 - (f11 / 2.0f)) - (i10 / 2), (i10 / 2) + width, f10 + (f11 / 2.0f) + (i10 / 2));
            RectF rectF2 = this.f7566j0;
            float f12 = this.A;
            canvas.drawRoundRect(rectF2, f12, f12, this.V);
            this.V.clearShadowLayer();
            this.V.setStyle(Paint.Style.FILL);
        }
        this.V.setColor(this.f7587u);
        RectF rectF3 = this.f7566j0;
        float f13 = seekBarCenterY;
        float f14 = this.f7597z;
        rectF3.set(start, f13 - (f14 / 2.0f), width, f13 + (f14 / 2.0f));
        RectF rectF4 = this.f7566j0;
        float f15 = this.A;
        canvas.drawRoundRect(rectF4, f15, f15, this.V);
    }

    public void setBackgroundEnlargeScale(float f10) {
        this.B = f10;
        t();
        invalidate();
    }

    public void setBackgroundHeight(float f10) {
        this.f7593x = f10;
        t();
        invalidate();
    }

    public void setBackgroundRadius(float f10) {
        this.f7595y = f10;
        t();
        invalidate();
    }

    public void setEnableAdaptiveVibrator(boolean z10) {
        this.f7559g = z10;
    }

    public void setEnableVibrator(boolean z10) {
        this.f7557f = z10;
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        super.setEnabled(z10);
        ColorStateList colorStateList = this.f7579q;
        Context context = getContext();
        int i10 = R$color.coui_seekbar_progress_color_normal;
        this.f7585t = v(this, colorStateList, COUIContextUtil.d(context, i10));
        this.f7587u = v(this, this.f7581r, COUIContextUtil.d(getContext(), R$color.coui_seekbar_background_color_normal));
        this.f7589v = v(this, this.f7583s, COUIContextUtil.d(getContext(), i10));
        if (z10) {
            this.B0 = getContext().getResources().getDimensionPixelSize(R$dimen.coui_seekbar_thumb_shadow_size);
        } else {
            this.B0 = 0;
        }
    }

    public void setFlingLinearDamping(float f10) {
        FlingBehavior flingBehavior;
        if (this.f7594x0) {
            this.J0 = f10;
            if (this.D0 == null || (flingBehavior = this.E0) == null) {
                return;
            }
            flingBehavior.h0(f10);
        }
    }

    public void setIncrement(int i10) {
        this.f7560g0 = Math.abs(i10);
    }

    @Override // android.widget.ProgressBar
    public void setInterpolator(Interpolator interpolator) {
        this.f7584s0 = interpolator;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setLocalMax(int i10) {
        this.f7573n = i10;
        super.setMax(i10);
    }

    protected void setLocalMin(int i10) {
        this.f7575o = i10;
        super.setMin(i10);
    }

    protected void setLocalProgress(int i10) {
        this.f7569l = i10;
        super.setProgress(i10);
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar
    public void setMax(int i10) {
        if (i10 < getMin()) {
            int min = getMin();
            Log.e("COUISeekBar", "setMax : the input params is lower than min. (inputMax:" + i10 + ",mMin:" + this.f7575o + ")");
            i10 = min;
        }
        if (i10 != this.f7573n) {
            setLocalMax(i10);
            if (this.f7569l > i10) {
                setProgress(i10);
            }
        }
        invalidate();
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar
    public void setMin(int i10) {
        int i11 = i10 < 0 ? 0 : i10;
        if (i10 > getMax()) {
            i11 = getMax();
            Log.e("COUISeekBar", "setMin : the input params is greater than max. (inputMin:" + i10 + ",mMax:" + this.f7573n + ")");
        }
        if (i11 != this.f7575o) {
            setLocalMin(i11);
            if (this.f7569l < i11) {
                setProgress(i11);
            }
        }
        invalidate();
    }

    public void setMoveDamping(float f10) {
        this.f7582r0 = f10;
    }

    public void setMoveType(int i10) {
        this.f7568k0 = i10;
    }

    public void setOnSeekBarChangeListener(h hVar) {
        this.f7562h0 = hVar;
    }

    public void setPaddingHorizontal(float f10) {
        this.L = f10;
        t();
        invalidate();
    }

    public void setPhysicalEnabled(boolean z10) {
        this.f7594x0 = z10;
    }

    @Override // android.widget.ProgressBar
    public void setProgress(int i10) {
        setProgress(i10, false);
    }

    public void setProgressColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.f7579q = colorStateList;
            this.f7585t = v(this, colorStateList, COUIContextUtil.d(getContext(), R$color.coui_seekbar_progress_color_normal));
            invalidate();
        }
    }

    public void setProgressContentDescription(String str) {
        this.f7588u0 = str;
    }

    public void setProgressEnlargeScale(float f10) {
        this.H = f10;
        t();
        invalidate();
    }

    public void setProgressHeight(float f10) {
        this.C = f10;
        t();
        invalidate();
    }

    public void setProgressRadius(float f10) {
        this.D = f10;
        t();
        invalidate();
    }

    public void setSeekBarBackgroundColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.f7581r = colorStateList;
            this.f7587u = v(this, colorStateList, COUIContextUtil.d(getContext(), R$color.coui_seekbar_background_color_normal));
            invalidate();
        }
    }

    public void setStartFromMiddle(boolean z10) {
        this.f7580q0 = z10;
    }

    public void setThumbColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.f7583s = colorStateList;
            this.f7589v = v(this, colorStateList, COUIContextUtil.d(getContext(), R$color.coui_seekbar_progress_color_normal));
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int v(View view, ColorStateList colorStateList, int i10) {
        return colorStateList == null ? i10 : colorStateList.getColorForState(view.getDrawableState(), i10);
    }

    protected void x(MotionEvent motionEvent) {
        this.f7567k = motionEvent.getX();
        this.U = motionEvent.getX();
    }

    protected void y(MotionEvent motionEvent) {
        float seekBarWidth = getSeekBarWidth();
        int i10 = this.f7573n;
        int i11 = this.f7575o;
        int i12 = i10 - i11;
        float f10 = (i12 > 0 ? (this.f7569l * seekBarWidth) / i12 : 0.0f) + i11;
        if (this.f7580q0 && f10 == seekBarWidth / 2.0f && Math.abs(motionEvent.getX() - this.U) < 20.0f) {
            return;
        }
        if (this.f7577p && this.f7564i0) {
            int i13 = this.f7568k0;
            if (i13 != 0) {
                if (i13 == 1) {
                    Y(motionEvent);
                    return;
                } else if (i13 != 2) {
                    return;
                }
            }
            X(motionEvent);
            return;
        }
        if (W(motionEvent, this)) {
            float x10 = motionEvent.getX();
            if (Math.abs(x10 - this.f7567k) > this.f7565j) {
                R();
                V();
                this.U = x10;
                if (G()) {
                    F(motionEvent);
                }
            }
        }
    }

    protected void z(MotionEvent motionEvent) {
        this.f7558f0.o(UserProfileInfo.Constant.NA_LAT_LON);
        if (this.f7577p) {
            if (this.f7594x0 && Math.abs(this.G0) >= 100.0f) {
                u(this.G0);
            } else {
                J();
            }
            setPressed(false);
            O();
            return;
        }
        if (isEnabled() && W(motionEvent, this) && G()) {
            i(motionEvent.getX());
        }
    }

    public COUISeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiSeekBarStyle);
    }

    @Override // android.widget.ProgressBar
    public void setProgress(int i10, boolean z10) {
        Q(i10, z10, false);
    }

    public COUISeekBar(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, COUIContextUtil.e(context) ? R$style.COUISeekBar_Dark : R$style.COUISeekBar);
    }

    public COUISeekBar(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f7555e = 0.0f;
        this.f7557f = true;
        this.f7559g = true;
        this.f7561h = true;
        this.f7563i = null;
        this.f7565j = 0;
        this.f7569l = 0;
        this.f7571m = 0;
        this.f7573n = 100;
        this.f7575o = 0;
        this.f7577p = false;
        this.f7579q = null;
        this.f7581r = null;
        this.f7583s = null;
        this.I = false;
        this.O = new Path();
        this.P = new RectF();
        this.Q = new RectF();
        this.R = new RectF();
        this.S = new AnimatorSet();
        this.f7551a0 = PathInterpolatorCompat.a(0.33f, 0.0f, 0.67f, 1.0f);
        this.f7552b0 = PathInterpolatorCompat.a(0.3f, 0.0f, 0.1f, 1.0f);
        this.f7554d0 = false;
        this.f7556e0 = false;
        this.f7558f0 = SpringSystem.g().c();
        this.f7560g0 = 1;
        this.f7564i0 = false;
        this.f7566j0 = new RectF();
        this.f7568k0 = 1;
        this.f7576o0 = SpringConfig.b(500.0d, 30.0d);
        this.f7580q0 = false;
        this.f7582r0 = 0.0f;
        this.f7584s0 = PathInterpolatorCompat.a(0.3f, 0.0f, 0.1f, 1.0f);
        this.f7594x0 = false;
        this.G0 = 0.0f;
        this.H0 = 5.5f;
        this.I0 = 1.1f;
        this.J0 = 15.0f;
        if (attributeSet != null) {
            this.f7586t0 = attributeSet.getStyleAttribute();
        }
        if (this.f7586t0 == 0) {
            this.f7586t0 = i10;
        }
        COUIDarkModeUtil.b(this, false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUISeekBar, i10, i11);
        this.f7557f = obtainStyledAttributes.getBoolean(R$styleable.COUISeekBar_couiSeekBarEnableVibrator, true);
        this.f7559g = obtainStyledAttributes.getBoolean(R$styleable.COUISeekBar_couiSeekBarAdaptiveVibrator, false);
        this.f7594x0 = obtainStyledAttributes.getBoolean(R$styleable.COUISeekBar_couiSeekBarPhysicsEnable, true);
        this.f7554d0 = obtainStyledAttributes.getBoolean(R$styleable.COUISeekBar_couiSeekBarShowProgress, true);
        this.f7556e0 = obtainStyledAttributes.getBoolean(R$styleable.COUISeekBar_couiSeekBarShowThumb, true);
        this.f7580q0 = obtainStyledAttributes.getBoolean(R$styleable.COUISeekBar_couiSeekBarStartMiddle, false);
        this.I = obtainStyledAttributes.getBoolean(R$styleable.COUISeekBar_couiSeekBarProgressFull, false);
        this.G = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUISeekBar_couiSeekBarProgressScaleRadius, getResources().getDimensionPixelSize(R$dimen.coui_seekbar_progress_scale_radius));
        this.f7581r = obtainStyledAttributes.getColorStateList(R$styleable.COUISeekBar_couiSeekBarBackgroundColor);
        this.f7579q = obtainStyledAttributes.getColorStateList(R$styleable.COUISeekBar_couiSeekBarProgressColor);
        this.f7583s = obtainStyledAttributes.getColorStateList(R$styleable.COUISeekBar_couiSeekBarThumbColor);
        this.f7587u = v(this, this.f7581r, COUIContextUtil.d(getContext(), R$color.coui_seekbar_background_color_normal));
        ColorStateList colorStateList = this.f7579q;
        Context context2 = getContext();
        int i12 = R$color.coui_seekbar_progress_color_normal;
        this.f7585t = v(this, colorStateList, COUIContextUtil.d(context2, i12));
        this.f7589v = v(this, this.f7583s, COUIContextUtil.d(getContext(), i12));
        this.f7598z0 = obtainStyledAttributes.getColor(R$styleable.COUISeekBar_couiSeekBarShadowColor, COUIContextUtil.d(getContext(), R$color.coui_seekbar_shadow_color));
        this.f7591w = obtainStyledAttributes.getColor(R$styleable.COUISeekBar_couiSeekBarThumbShadowColor, COUIContextUtil.d(getContext(), R$color.coui_seekbar_thumb_shadow_color));
        this.f7595y = obtainStyledAttributes.getDimension(R$styleable.COUISeekBar_couiSeekBarBackgroundRadius, getResources().getDimension(R$dimen.coui_seekbar_background_radius));
        this.D = obtainStyledAttributes.getDimension(R$styleable.COUISeekBar_couiSeekBarProgressRadius, getResources().getDimension(R$dimen.coui_seekbar_progress_radius));
        this.A0 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUISeekBar_couiSeekBarShadowSize, 0);
        this.B0 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUISeekBar_couiSeekBarThumbShadowSize, 0);
        this.C0 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUISeekBar_couiSeekBarInnerShadowSize, 0);
        this.L = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUISeekBar_couiSeekBarProgressPaddingHorizontal, getResources().getDimensionPixelSize(R$dimen.coui_seekbar_progress_padding_horizontal));
        this.f7593x = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUISeekBar_couiSeekBarBackgroundHeight, (int) (this.f7595y * 2.0f));
        this.C = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUISeekBar_couiSeekBarProgressHeight, (int) (this.D * 2.0f));
        this.f7572m0 = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUISeekBar_couiSeekBarMinHeight, getResources().getDimensionPixelSize(R$dimen.coui_seekbar_view_min_height));
        this.f7590v0 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUISeekBar_couiSeekBarMaxWidth, 0);
        this.B = obtainStyledAttributes.getFloat(R$styleable.COUISeekBar_couiSeekBarBackGroundEnlargeScale, 6.0f);
        this.H = obtainStyledAttributes.getFloat(R$styleable.COUISeekBar_couiSeekBarProgressEnlargeScale, 4.0f);
        obtainStyledAttributes.recycle();
        this.f7592w0 = new TextDrawable(getContext());
        this.f7561h = VibrateUtils.h(context);
        E();
        t();
        A();
        if (this.f7594x0) {
            C(context);
        }
    }
}
