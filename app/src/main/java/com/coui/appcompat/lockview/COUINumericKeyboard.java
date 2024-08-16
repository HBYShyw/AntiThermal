package com.coui.appcompat.lockview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.support.control.R$array;
import com.support.control.R$attr;
import com.support.control.R$dimen;
import com.support.control.R$drawable;
import com.support.control.R$string;
import com.support.control.R$style;
import com.support.control.R$styleable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import k3.VibrateUtils;
import m1.COUIEaseInterpolator;
import m1.COUIInEaseInterpolator;
import m1.COUIOutEaseInterpolator;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUINumericKeyboard extends View {

    /* renamed from: r0, reason: collision with root package name */
    public static int f6364r0 = 0;

    /* renamed from: s0, reason: collision with root package name */
    public static int f6365s0 = 1;

    /* renamed from: t0, reason: collision with root package name */
    public static int f6366t0 = 2;
    private int A;
    private int[] B;
    private TextPaint C;
    private Paint.FontMetrics D;
    private Paint.FontMetricsInt E;
    private Paint F;
    private int G;
    private int H;
    private int I;
    private int J;
    private int K;
    private int L;
    private float M;
    private int N;
    private int O;
    private TextPaint P;
    private float Q;
    private float R;
    private float S;
    private float T;
    private SideStyle U;
    private SideStyle V;
    private AnimatorSet W;

    /* renamed from: a0, reason: collision with root package name */
    private AnimatorSet f6367a0;

    /* renamed from: b0, reason: collision with root package name */
    private boolean f6368b0;

    /* renamed from: c0, reason: collision with root package name */
    private Animator.AnimatorListener f6369c0;

    /* renamed from: d0, reason: collision with root package name */
    private PatternExploreByTouchHelper f6370d0;

    /* renamed from: e, reason: collision with root package name */
    @Deprecated
    public int f6371e;

    /* renamed from: e0, reason: collision with root package name */
    private final AccessibilityManager f6372e0;

    /* renamed from: f, reason: collision with root package name */
    @Deprecated
    public int f6373f;

    /* renamed from: f0, reason: collision with root package name */
    private Context f6374f0;

    /* renamed from: g, reason: collision with root package name */
    public SideStyle f6375g;

    /* renamed from: g0, reason: collision with root package name */
    private int f6376g0;

    /* renamed from: h, reason: collision with root package name */
    public final SideStyle f6377h;

    /* renamed from: h0, reason: collision with root package name */
    private int f6378h0;

    /* renamed from: i, reason: collision with root package name */
    private float f6379i;

    /* renamed from: i0, reason: collision with root package name */
    private int f6380i0;

    /* renamed from: j, reason: collision with root package name */
    private Paint f6381j;

    /* renamed from: j0, reason: collision with root package name */
    private int f6382j0;

    /* renamed from: k, reason: collision with root package name */
    private Cell f6383k;

    /* renamed from: k0, reason: collision with root package name */
    private float f6384k0;

    /* renamed from: l, reason: collision with root package name */
    private int f6385l;

    /* renamed from: l0, reason: collision with root package name */
    private int f6386l0;

    /* renamed from: m, reason: collision with root package name */
    private OnClickItemListener f6387m;

    /* renamed from: m0, reason: collision with root package name */
    private int f6388m0;

    /* renamed from: n, reason: collision with root package name */
    private int f6389n;

    /* renamed from: n0, reason: collision with root package name */
    private float f6390n0;

    /* renamed from: o, reason: collision with root package name */
    private int f6391o;

    /* renamed from: o0, reason: collision with root package name */
    private Interpolator f6392o0;

    /* renamed from: p, reason: collision with root package name */
    private int f6393p;

    /* renamed from: p0, reason: collision with root package name */
    private Interpolator f6394p0;

    /* renamed from: q, reason: collision with root package name */
    private int f6395q;

    /* renamed from: q0, reason: collision with root package name */
    private int f6396q0;

    /* renamed from: r, reason: collision with root package name */
    private boolean f6397r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f6398s;

    /* renamed from: t, reason: collision with root package name */
    private Cell[][] f6399t;

    /* renamed from: u, reason: collision with root package name */
    private Drawable f6400u;

    /* renamed from: v, reason: collision with root package name */
    private Drawable f6401v;

    /* renamed from: w, reason: collision with root package name */
    private Drawable f6402w;

    /* renamed from: x, reason: collision with root package name */
    private GradientDrawable f6403x;

    /* renamed from: y, reason: collision with root package name */
    private int f6404y;

    /* renamed from: z, reason: collision with root package name */
    private int f6405z;

    /* loaded from: classes.dex */
    public class Cell {

        /* renamed from: a, reason: collision with root package name */
        int f6407a;

        /* renamed from: b, reason: collision with root package name */
        int f6408b;

        /* renamed from: c, reason: collision with root package name */
        String f6409c;

        /* renamed from: d, reason: collision with root package name */
        String f6410d;

        /* renamed from: e, reason: collision with root package name */
        float f6411e;

        /* renamed from: f, reason: collision with root package name */
        int f6412f;

        /* renamed from: g, reason: collision with root package name */
        int f6413g;

        public int getColumn() {
            return this.f6408b;
        }

        public int getRow() {
            return this.f6407a;
        }

        public void setCellNumberAlpha(float f10) {
            this.f6411e = f10;
            COUINumericKeyboard.this.invalidate();
        }

        public void setCellNumberTranslateX(int i10) {
            this.f6412f = i10;
            COUINumericKeyboard.this.invalidate();
        }

        public void setCellNumberTranslateY(int i10) {
            this.f6413g = i10;
            COUINumericKeyboard.this.invalidate();
        }

        public String toString() {
            return "row " + this.f6407a + "column " + this.f6408b;
        }

        private Cell(int i10, int i11) {
            this.f6409c = "";
            this.f6410d = "";
            this.f6411e = 1.0f;
            COUINumericKeyboard.this.n(i10, i11);
            this.f6407a = i10;
            this.f6408b = i11;
        }
    }

    /* loaded from: classes.dex */
    public interface OnClickItemListener {
        void a();

        void b();

        void c(int i10);
    }

    @Deprecated
    /* loaded from: classes.dex */
    public interface OnItemTouchListener {
    }

    @Deprecated
    /* loaded from: classes.dex */
    public interface OnTouchTextListener {
    }

    @Deprecated
    /* loaded from: classes.dex */
    public interface OnTouchUpListener {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class PatternExploreByTouchHelper extends ExploreByTouchHelper {

        /* renamed from: a, reason: collision with root package name */
        private Rect f6415a;

        public PatternExploreByTouchHelper(View view) {
            super(view);
            this.f6415a = new Rect();
        }

        private Rect a(int i10) {
            int i11;
            Rect rect = this.f6415a;
            int i12 = 0;
            if (i10 != -1) {
                Cell P = COUINumericKeyboard.this.P(i10 / 3, i10 % 3);
                i12 = (int) COUINumericKeyboard.this.t(P.f6408b);
                i11 = (int) COUINumericKeyboard.this.u(P.f6407a);
            } else {
                i11 = 0;
            }
            rect.left = i12 - COUINumericKeyboard.this.f6395q;
            rect.right = i12 + COUINumericKeyboard.this.f6395q;
            rect.top = i11 - COUINumericKeyboard.this.f6395q;
            rect.bottom = i11 + COUINumericKeyboard.this.f6395q;
            return rect;
        }

        private int c(float f10, float f11) {
            Cell m10 = COUINumericKeyboard.this.m(f10, f11);
            if (m10 == null) {
                return -1;
            }
            int row = (m10.getRow() * 3) + m10.getColumn();
            if (row == 9) {
                COUINumericKeyboard cOUINumericKeyboard = COUINumericKeyboard.this;
                if (cOUINumericKeyboard.L(cOUINumericKeyboard.U)) {
                    row = -1;
                }
            }
            if (row == 11) {
                COUINumericKeyboard cOUINumericKeyboard2 = COUINumericKeyboard.this;
                if (cOUINumericKeyboard2.L(cOUINumericKeyboard2.V)) {
                    return -1;
                }
            }
            return row;
        }

        public CharSequence b(int i10) {
            if (i10 == 9) {
                COUINumericKeyboard cOUINumericKeyboard = COUINumericKeyboard.this;
                if (!cOUINumericKeyboard.L(cOUINumericKeyboard.U)) {
                    return COUINumericKeyboard.this.U.f6421e;
                }
            }
            if (i10 == 11) {
                COUINumericKeyboard cOUINumericKeyboard2 = COUINumericKeyboard.this;
                if (!cOUINumericKeyboard2.L(cOUINumericKeyboard2.V)) {
                    return COUINumericKeyboard.this.V.f6421e;
                }
            }
            if (i10 == -1) {
                return PatternExploreByTouchHelper.class.getSimpleName();
            }
            return COUINumericKeyboard.this.B[i10] + "";
        }

        boolean d(int i10) {
            invalidateVirtualView(i10);
            if (COUINumericKeyboard.this.isEnabled()) {
                COUINumericKeyboard.this.l(i10);
                COUINumericKeyboard.this.announceForAccessibility(b(i10));
            }
            sendEventForVirtualView(i10, 1);
            return true;
        }

        public int getItemCounts() {
            return 12;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float f10, float f11) {
            return c(f10, f11);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> list) {
            for (int i10 = 0; i10 < getItemCounts(); i10++) {
                if (i10 == 9) {
                    COUINumericKeyboard cOUINumericKeyboard = COUINumericKeyboard.this;
                    if (cOUINumericKeyboard.L(cOUINumericKeyboard.U)) {
                        list.add(-1);
                    }
                }
                if (i10 == 11) {
                    COUINumericKeyboard cOUINumericKeyboard2 = COUINumericKeyboard.this;
                    if (cOUINumericKeyboard2.L(cOUINumericKeyboard2.V)) {
                        list.add(-1);
                    }
                }
                list.add(Integer.valueOf(i10));
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle) {
            if (i11 != 16) {
                return false;
            }
            return d(i10);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onPopulateEventForVirtualView(int i10, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.getText().add(b(i10));
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.Z(b(i10));
            accessibilityNodeInfoCompat.b(AccessibilityNodeInfoCompat.a.f2322i);
            accessibilityNodeInfoCompat.W(true);
            accessibilityNodeInfoCompat.Q(a(i10));
        }
    }

    /* loaded from: classes.dex */
    public static class SideStyle {

        /* renamed from: a, reason: collision with root package name */
        private Drawable f6417a;

        /* renamed from: b, reason: collision with root package name */
        private String f6418b;

        /* renamed from: c, reason: collision with root package name */
        private int f6419c;

        /* renamed from: d, reason: collision with root package name */
        private float f6420d;

        /* renamed from: e, reason: collision with root package name */
        private String f6421e;

        /* renamed from: f, reason: collision with root package name */
        private int f6422f;

        /* loaded from: classes.dex */
        public static class Builder {

            /* renamed from: a, reason: collision with root package name */
            private Drawable f6423a;

            /* renamed from: b, reason: collision with root package name */
            private String f6424b;

            /* renamed from: c, reason: collision with root package name */
            private int f6425c;

            /* renamed from: d, reason: collision with root package name */
            private float f6426d;

            /* renamed from: e, reason: collision with root package name */
            private String f6427e;

            /* renamed from: f, reason: collision with root package name */
            private int f6428f = COUINumericKeyboard.f6364r0;

            public SideStyle g() {
                return new SideStyle(this);
            }

            public Builder h(String str) {
                this.f6427e = str;
                return this;
            }

            public Builder i(Drawable drawable) {
                this.f6423a = drawable;
                return this;
            }

            public Builder j(String str) {
                this.f6424b = str;
                return this;
            }

            public Builder k(int i10) {
                this.f6425c = i10;
                return this;
            }

            public Builder l(float f10) {
                this.f6426d = f10;
                return this;
            }

            public Builder m(int i10) {
                this.f6428f = i10;
                return this;
            }
        }

        private SideStyle(Builder builder) {
            this.f6417a = builder.f6423a;
            this.f6418b = builder.f6424b;
            this.f6419c = builder.f6425c;
            this.f6420d = builder.f6426d;
            this.f6421e = builder.f6427e;
            this.f6422f = builder.f6428f;
        }
    }

    public COUINumericKeyboard(Context context) {
        this(context, null);
    }

    private void A(float f10, float f11) {
        if (!this.f6372e0.isTouchExplorationEnabled()) {
            Cell m10 = m(f10, f11);
            this.f6383k = m10;
            if (m10 != null) {
                int y4 = y(m10);
                this.f6370d0.invalidateRoot();
                if (this.f6397r && y4 != -1) {
                    Q();
                }
            } else {
                this.f6385l = -1;
            }
        }
        this.W.removeAllListeners();
        if (this.f6367a0.isRunning()) {
            this.f6367a0.end();
        }
        if (this.W.isRunning()) {
            this.W.end();
        }
        this.W.start();
        invalidate();
    }

    private void B(MotionEvent motionEvent) {
        A(motionEvent.getX(), motionEvent.getY());
    }

    private void C(float f10, float f11) {
        if (this.f6372e0.isTouchExplorationEnabled()) {
            Cell m10 = m(f10, f11);
            this.f6383k = m10;
            if (m10 != null) {
                int y4 = y(m10);
                this.f6370d0.invalidateRoot();
                if (this.f6397r && y4 != -1) {
                    Q();
                }
            } else {
                this.f6385l = -1;
            }
        }
        s();
        if (x(f11) != -1 && v(f10) != -1) {
            l(this.f6385l);
        }
        if (this.f6385l != -1 && isEnabled() && !hasOnClickListeners()) {
            R();
        }
        invalidate();
    }

    private void D(MotionEvent motionEvent) {
        C(motionEvent.getX(), motionEvent.getY());
    }

    private void E(int i10, boolean z10) {
        if (N(i10)) {
            float[] w10 = w(i10);
            if (z10) {
                A(w10[0], w10[1]);
            } else {
                C(w10[0], w10[1]);
            }
        }
    }

    private void F() {
        J();
        H();
    }

    private void G(Cell cell, List<Animator> list, int i10) {
        cell.setCellNumberAlpha(0.0f);
        cell.setCellNumberTranslateY(this.f6378h0);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(cell, "cellNumberAlpha", 0.0f, 1.0f);
        ofFloat.setStartDelay(166 + (((i10 == 10 && L(this.U)) ? i10 - 1 : i10) * 16));
        ofFloat.setDuration(167L);
        ofFloat.setInterpolator(this.f6392o0);
        list.add(ofFloat);
        ObjectAnimator ofInt = ObjectAnimator.ofInt(cell, "cellNumberTranslateY", this.f6378h0, 0);
        if (i10 == 10 && L(this.U)) {
            i10--;
        }
        ofInt.setStartDelay(16 * i10);
        ofInt.setDuration(500L);
        ofInt.setInterpolator(this.f6394p0);
        list.add(ofInt);
    }

    private void H() {
        PathInterpolator pathInterpolator = new PathInterpolator(0.0f, 0.0f, 0.6f, 1.0f);
        this.f6367a0 = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "normalScale", 2.0f, 2.5f);
        ofFloat.setDuration(160L);
        ofFloat.setInterpolator(pathInterpolator);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, "normalAlpha", this.f6379i, 0.0f);
        ofFloat2.setDuration(160L);
        ofFloat2.setInterpolator(pathInterpolator);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofKeyframe("blurAlpha", Keyframe.ofFloat(0.0f, 0.0f), Keyframe.ofFloat(0.5f, this.f6379i), Keyframe.ofFloat(1.0f, 0.0f)));
        ofPropertyValuesHolder.setDuration(400L);
        ofPropertyValuesHolder.setInterpolator(pathInterpolator);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this, "blurScale", 1.0f, 2.0f);
        ofPropertyValuesHolder.setDuration(400L);
        ofPropertyValuesHolder.setInterpolator(pathInterpolator);
        this.f6367a0.play(ofFloat).with(ofFloat2).with(ofFloat3).with(ofPropertyValuesHolder);
    }

    private void I() {
        Typeface typeface;
        Paint paint = new Paint(5);
        this.f6381j = paint;
        paint.setColor(this.f6389n);
        this.f6381j.setMaskFilter(new BlurMaskFilter(20.0f, BlurMaskFilter.Blur.NORMAL));
        this.f6381j.setAlpha(0);
        this.C.setTextSize(this.M);
        this.C.setColor(this.N);
        this.C.setAntiAlias(true);
        try {
            typeface = z(getStatusAndVariation());
        } catch (Exception unused) {
            typeface = Typeface.DEFAULT;
        }
        this.C.setTypeface(typeface);
        this.D = this.C.getFontMetrics();
        this.F.setColor(this.O);
        this.F.setAntiAlias(true);
        this.F.setStyle(Paint.Style.STROKE);
        this.P.setFakeBoldText(true);
        this.P.setAntiAlias(true);
    }

    private void J() {
        AnimatorSet animatorSet = new AnimatorSet();
        this.W = animatorSet;
        animatorSet.setDuration(100L);
        this.W.setInterpolator(new COUIOutEaseInterpolator());
        this.W.play(ObjectAnimator.ofFloat(this, "normalScale", 1.0f, 2.0f)).with(ObjectAnimator.ofFloat(this, "normalAlpha", 0.0f, this.f6379i));
    }

    private void K(SideStyle sideStyle, List<Animator> list, int i10) {
        if (L(sideStyle)) {
            return;
        }
        if (sideStyle.f6417a != null) {
            setDrawableAlpha(0.0f);
            setDrawableTranslateY(this.f6378h0);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "drawableAlpha", 0.0f, 1.0f);
            long j10 = i10 * 16;
            ofFloat.setStartDelay(166 + j10);
            ofFloat.setDuration(167L);
            ofFloat.setInterpolator(this.f6392o0);
            list.add(ofFloat);
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "drawableTranslateY", this.f6378h0, 0);
            ofInt.setStartDelay(j10);
            ofInt.setDuration(500L);
            ofInt.setInterpolator(this.f6394p0);
            list.add(ofInt);
            return;
        }
        if (TextUtils.isEmpty(sideStyle.f6418b)) {
            return;
        }
        setTextAlpha(0.0f);
        setTextTranslateY(this.f6378h0);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, "textAlpha", 0.0f, 1.0f);
        long j11 = i10 * 16;
        ofFloat2.setStartDelay(166 + j11);
        ofFloat2.setDuration(167L);
        ofFloat2.setInterpolator(this.f6392o0);
        list.add(ofFloat2);
        ObjectAnimator ofInt2 = ObjectAnimator.ofInt(this, "textTranslateY", this.f6378h0, 0);
        ofInt2.setStartDelay(j11);
        ofInt2.setDuration(500L);
        ofInt2.setInterpolator(this.f6394p0);
        list.add(ofInt2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean L(SideStyle sideStyle) {
        return sideStyle == null || (sideStyle.f6417a == null && TextUtils.isEmpty(sideStyle.f6418b));
    }

    private boolean M(MotionEvent motionEvent) {
        return motionEvent.getPointerId(motionEvent.getActionIndex()) > 0;
    }

    private boolean N(int i10) {
        return (i10 >= 7 && i10 <= 16) || (i10 >= 144 && i10 <= 153) || i10 == 67 || i10 == 66 || i10 == 160;
    }

    private boolean O(int i10) {
        return this.R > 0.0f && (1 == i10 || 3 == i10 || i10 == 0);
    }

    private void Q() {
        if (this.f6368b0) {
            performHapticFeedback(302);
        } else {
            performHapticFeedback(301);
        }
    }

    private void R() {
        playSoundEffect(0);
    }

    private int[] getDeleteCellIndex() {
        SideStyle sideStyle = this.U;
        if (sideStyle != null && sideStyle.f6422f == f6365s0) {
            return new int[]{0, 3};
        }
        SideStyle sideStyle2 = this.V;
        if (sideStyle2 == null || sideStyle2.f6422f != f6365s0) {
            return null;
        }
        return new int[]{2, 3};
    }

    private int[] getFinishCellIndex() {
        SideStyle sideStyle = this.U;
        if (sideStyle != null && sideStyle.f6422f == f6366t0) {
            return new int[]{0, 3};
        }
        SideStyle sideStyle2 = this.V;
        if (sideStyle2 == null || sideStyle2.f6422f != f6366t0) {
            return null;
        }
        return new int[]{2, 3};
    }

    private int[] getStatusAndVariation() {
        int i10 = Settings.System.getInt(this.f6374f0.getContentResolver(), "font_variation_settings", 550);
        return new int[]{(61440 & i10) >> 12, i10 & 4095};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l(int i10) {
        OnClickItemListener onClickItemListener = this.f6387m;
        if (onClickItemListener != null) {
            if (i10 >= 0 && i10 <= 8) {
                onClickItemListener.c(i10 + 1);
            }
            if (i10 == 10) {
                this.f6387m.c(0);
            }
            if (i10 == 9) {
                this.f6387m.a();
            }
            if (i10 == 11) {
                this.f6387m.b();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Cell m(float f10, float f11) {
        int v7;
        int x10 = x(f11);
        if (x10 >= 0 && (v7 = v(f10)) >= 0) {
            return P(x10, v7);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n(int i10, int i11) {
        if (i10 < 0 || i10 > 3) {
            throw new IllegalArgumentException("row must be in range 0-3");
        }
        if (i11 < 0 || i11 > 2) {
            throw new IllegalArgumentException("column must be in range 0-2");
        }
    }

    private void o(Canvas canvas, float f10, float f11, int i10, int i11, int i12) {
        int i13 = this.A;
        this.f6403x.setBounds(((int) (f10 - i13)) + i11, ((int) (f11 - i13)) + i12, ((int) (f10 + i13)) + i11, ((int) (f11 + i13)) + i12);
        this.f6403x.setAlpha(i10);
        this.f6403x.draw(canvas);
    }

    private void p(Canvas canvas, int i10, int i11) {
        Cell cell = this.f6399t[i11][i10];
        float t7 = t(i10);
        float u7 = u(i11);
        int i12 = (i11 * 3) + i10;
        if (i12 == 9) {
            r(this.U, canvas, t7, u7);
            return;
        }
        if (i12 == 11) {
            r(this.V, canvas, t7, u7);
            return;
        }
        if (i12 != -1) {
            float measureText = this.C.measureText(cell.f6409c);
            Paint.FontMetrics fontMetrics = this.D;
            float f10 = (u7 - ((fontMetrics.descent + fontMetrics.ascent) / 2.0f)) - this.L;
            this.C.setAlpha((int) (cell.f6411e * 255.0f));
            this.f6403x.setColor(this.f6404y);
            o(canvas, t7, u7, (int) (cell.f6411e * 255.0f), cell.f6412f, cell.f6413g);
            canvas.drawText(cell.f6409c, (t7 - (measureText / 2.0f)) + cell.f6412f, f10 + cell.f6413g, this.C);
        }
    }

    private void q(Canvas canvas) {
        Cell cell = this.f6383k;
        if (cell != null) {
            float t7 = t(cell.f6408b);
            float u7 = u(this.f6383k.f6407a);
            if (y(this.f6383k) != -1) {
                int i10 = this.f6395q;
                int i11 = (int) (t7 - i10);
                int i12 = (int) (u7 - i10);
                int i13 = (int) (i10 + t7);
                int i14 = (int) (i10 + u7);
                canvas.save();
                float f10 = this.T;
                canvas.scale(f10, f10, t7, u7);
                this.f6401v.setAlpha((int) (this.R * 255.0f));
                this.f6401v.setBounds(i11, i12, i13, i14);
                this.f6401v.draw(canvas);
                canvas.restore();
                canvas.save();
                float f11 = this.S;
                canvas.scale(f11, f11, t7, u7);
                this.f6402w.setBounds(i11, i12, i13, i14);
                this.f6402w.setAlpha((int) (this.Q * 255.0f));
                this.f6402w.draw(canvas);
                canvas.restore();
            }
        }
    }

    private void r(SideStyle sideStyle, Canvas canvas, float f10, float f11) {
        if (L(sideStyle)) {
            return;
        }
        this.f6403x.setColor(this.f6405z);
        if (sideStyle.f6417a != null) {
            int intrinsicWidth = (int) (f10 - (sideStyle.f6417a.getIntrinsicWidth() / 2));
            int intrinsicWidth2 = intrinsicWidth + sideStyle.f6417a.getIntrinsicWidth();
            int intrinsicHeight = (int) (f11 - (sideStyle.f6417a.getIntrinsicHeight() / 2));
            int intrinsicHeight2 = intrinsicHeight + sideStyle.f6417a.getIntrinsicHeight();
            o(canvas, f10, f11, (int) (this.f6384k0 * 255.0f), this.f6380i0, this.f6382j0);
            Drawable drawable = sideStyle.f6417a;
            int i10 = this.f6380i0;
            int i11 = this.f6382j0;
            drawable.setBounds(intrinsicWidth + i10, intrinsicHeight + i11, intrinsicWidth2 + i10, intrinsicHeight2 + i11);
            sideStyle.f6417a.setAlpha((int) (this.f6384k0 * 255.0f));
            sideStyle.f6417a.draw(canvas);
            return;
        }
        if (TextUtils.isEmpty(sideStyle.f6418b)) {
            return;
        }
        this.P.setTextSize(sideStyle.f6420d);
        this.P.setColor(sideStyle.f6419c);
        this.P.setAlpha((int) (this.f6390n0 * 255.0f));
        float measureText = this.P.measureText(sideStyle.f6418b);
        this.E = this.P.getFontMetricsInt();
        o(canvas, f10, f11, (int) (this.f6390n0 * 255.0f), this.f6386l0, this.f6388m0);
        canvas.drawText(sideStyle.f6418b, (f10 - (measureText / 2.0f)) + this.f6386l0, (f11 - ((r1.descent + r1.ascent) / 2)) + this.f6388m0, this.P);
    }

    private void s() {
        if (this.W.isRunning()) {
            this.W.addListener(this.f6369c0);
        } else {
            this.f6367a0.start();
        }
    }

    private void setBlurAlpha(float f10) {
        this.Q = f10;
        invalidate();
    }

    private void setBlurScale(float f10) {
        this.S = f10;
        invalidate();
    }

    private void setNormalAlpha(float f10) {
        this.R = f10;
        invalidate();
    }

    private void setNormalScale(float f10) {
        this.T = f10;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float t(int i10) {
        return getPaddingLeft() + (this.f6391o / 2.0f) + (r1 * i10) + (this.K * i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float u(int i10) {
        return getPaddingTop() + (this.f6393p / 2.0f) + (r1 * i10) + (this.J * i10);
    }

    private int v(float f10) {
        for (int i10 = 0; i10 < 3; i10++) {
            int t7 = (int) t(i10);
            int i11 = this.f6391o;
            int i12 = t7 - (i11 / 2);
            int i13 = t7 + (i11 / 2);
            if (i12 <= f10 && f10 <= i13) {
                return i10;
            }
        }
        return -1;
    }

    private float[] w(int i10) {
        int i11;
        int i12 = 3;
        if (i10 >= 8 && i10 <= 16) {
            int i13 = i10 - 8;
            i11 = i13 % 3;
            i12 = i13 / 3;
        } else if (i10 >= 145 && i10 <= 153) {
            int i14 = i10 - 145;
            i11 = i14 % 3;
            i12 = i14 / 3;
        } else if (i10 == 67) {
            int[] deleteCellIndex = getDeleteCellIndex();
            if (deleteCellIndex == null || deleteCellIndex.length != 2) {
                return new float[]{-1.0f, -1.0f};
            }
            i11 = deleteCellIndex[0];
            i12 = deleteCellIndex[1];
        } else if (i10 == 7 || i10 == 144) {
            i11 = 1;
        } else {
            if (i10 != 66 && i10 != 160) {
                return new float[]{-1.0f, -1.0f};
            }
            int[] finishCellIndex = getFinishCellIndex();
            if (finishCellIndex == null || finishCellIndex.length != 2) {
                return new float[]{-1.0f, -1.0f};
            }
            i11 = finishCellIndex[0];
            i12 = finishCellIndex[1];
        }
        Cell cell = this.f6399t[i12][i11];
        float t7 = t(i11);
        float u7 = u(i12);
        Paint.FontMetrics fontMetrics = this.D;
        return new float[]{t7 + cell.f6412f, (u7 - ((fontMetrics.descent + fontMetrics.ascent) / 2.0f)) + cell.f6413g};
    }

    private int x(float f10) {
        for (int i10 = 0; i10 < 4; i10++) {
            int u7 = (int) u(i10);
            int i11 = this.f6393p;
            int i12 = u7 - (i11 / 2);
            int i13 = u7 + (i11 / 2);
            if (i12 <= f10 && f10 <= i13) {
                return i10;
            }
        }
        return -1;
    }

    private int y(Cell cell) {
        int row = (cell.getRow() * 3) + cell.getColumn();
        this.f6385l = row;
        if (row == 9 && L(this.U)) {
            this.f6385l = -1;
        }
        if (this.f6385l == 11 && L(this.V)) {
            this.f6385l = -1;
        }
        return this.f6385l;
    }

    private Typeface z(int[] iArr) {
        this.f6376g0 = iArr[1];
        Typeface typeface = Typeface.DEFAULT;
        if (iArr[0] != 0) {
            return new Typeface.Builder("/system/fonts/SysSans-En-Regular.ttf").setFontVariationSettings("'wght' " + (iArr[1] + 200)).build();
        }
        return new Typeface.Builder("/system/fonts/SysSans-En-Regular.ttf").build();
    }

    public synchronized Cell P(int i10, int i11) {
        n(i10, i11);
        return this.f6399t[i10][i11];
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.f6370d0.dispatchHoverEvent(motionEvent) | super.dispatchHoverEvent(motionEvent);
    }

    public AnimatorSet getEnterAnim() {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        for (int i10 = 0; i10 < 4; i10++) {
            for (int i11 = 0; i11 < 3; i11++) {
                Cell P = P(i10, i11);
                int i12 = (i10 * 3) + i11;
                if (i12 == 9) {
                    K(this.U, arrayList, i12);
                } else if (i12 == 11) {
                    SideStyle sideStyle = this.V;
                    if (L(this.U)) {
                        i12--;
                    }
                    K(sideStyle, arrayList, i12);
                } else {
                    G(P, arrayList, i12);
                }
            }
        }
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    @Deprecated
    public int getTouchIndex() {
        return 0;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int[] statusAndVariation = getStatusAndVariation();
        if (this.f6376g0 != statusAndVariation[1]) {
            this.C.setTypeface(z(statusAndVariation));
            invalidate();
        }
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.f6381j != null) {
            this.f6381j = null;
        }
        if (this.f6383k != null) {
            this.f6383k = null;
        }
        this.f6398s = false;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        q(canvas);
        for (int i10 = 0; i10 < 4; i10++) {
            for (int i11 = 0; i11 < 3; i11++) {
                p(canvas, i11, i10);
            }
        }
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        if (this.f6372e0.isTouchExplorationEnabled()) {
            int action = motionEvent.getAction();
            if (action == 7) {
                motionEvent.setAction(2);
            } else if (action == 9) {
                motionEvent.setAction(0);
            } else if (action == 10) {
                motionEvent.setAction(1);
            }
            onTouchEvent(motionEvent);
            motionEvent.setAction(action);
        }
        return super.onHoverEvent(motionEvent);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i10, KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() == 0) {
            E(i10, true);
        }
        return super.onKeyDown(i10, keyEvent);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i10, KeyEvent keyEvent) {
        if (keyEvent.getScanCode() != 0) {
            E(i10, false);
        }
        return super.onKeyUp(i10, keyEvent);
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i10);
        int size = View.MeasureSpec.getSize(i10);
        int mode2 = View.MeasureSpec.getMode(i11);
        int size2 = View.MeasureSpec.getSize(i11);
        if (mode == Integer.MIN_VALUE) {
            size = this.G;
        }
        if (mode2 == Integer.MIN_VALUE) {
            size2 = this.H;
        }
        setMeasuredDimension(size, size2);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        int i14 = this.I;
        this.f6391o = i14;
        this.f6393p = i14;
        this.A = i14 / 2;
        this.K = (((getWidth() - getPaddingLeft()) - getPaddingRight()) - (this.f6391o * 3)) / 2;
        int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
        int i15 = this.f6393p;
        this.J = (height - (i15 * 4)) / 3;
        this.f6395q = i15 / 2;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (M(motionEvent)) {
            return true;
        }
        int action = motionEvent.getAction();
        if (!isEnabled()) {
            if (O(action)) {
                s();
            }
            return false;
        }
        if (action == 0) {
            this.f6398s = true;
            B(motionEvent);
        } else if (action == 1) {
            this.f6398s = false;
            D(motionEvent);
        } else if (action == 3) {
            this.f6398s = false;
            D(motionEvent);
        } else if (action == 6) {
            this.f6398s = false;
            D(motionEvent);
        }
        return true;
    }

    public void setCircleMaxAlpha(int i10) {
        this.f6379i = i10;
        F();
    }

    public void setDeleteStyle(Drawable drawable) {
        this.f6375g = new SideStyle.Builder().i(drawable).h(getResources().getString(R$string.coui_number_keyboard_delete)).m(f6365s0).g();
    }

    public void setDrawableAlpha(float f10) {
        this.f6384k0 = f10;
        invalidate();
    }

    public void setDrawableTranslateX(int i10) {
        this.f6380i0 = i10;
        invalidate();
    }

    public void setDrawableTranslateY(int i10) {
        this.f6382j0 = i10;
        invalidate();
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        Paint paint;
        if (!z10 && this.f6398s && (paint = this.f6381j) != null) {
            paint.setAlpha(0);
            this.f6398s = false;
            invalidate();
        }
        super.setEnabled(z10);
    }

    @Deprecated
    public void setHasFinishButton(boolean z10) {
    }

    @Deprecated
    public void setItemTouchListener(OnItemTouchListener onItemTouchListener) {
    }

    public void setKeyboardLineColor(int i10) {
        this.O = i10;
        I();
    }

    public void setKeyboardNumberTextColor(int i10) {
        this.N = i10;
        this.f6400u.setTint(i10);
    }

    public void setLeftStyle(SideStyle sideStyle) {
        this.U = sideStyle;
        this.f6370d0.invalidateVirtualView(9);
        invalidate();
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.f6387m = onClickItemListener;
    }

    public void setPressedColor(int i10) {
        this.f6389n = i10;
        this.f6401v.setTint(i10);
        this.f6402w.setTint(this.f6389n);
        I();
    }

    public void setRightStyle(SideStyle sideStyle) {
        this.V = sideStyle;
        this.f6370d0.invalidateVirtualView(11);
        invalidate();
    }

    public void setTactileFeedbackEnabled(boolean z10) {
        this.f6397r = z10;
    }

    public void setTextAlpha(float f10) {
        this.f6390n0 = f10;
        invalidate();
    }

    public void setTextTranslateX(int i10) {
        this.f6386l0 = i10;
        invalidate();
    }

    public void setTextTranslateY(int i10) {
        this.f6388m0 = i10;
        invalidate();
    }

    @Deprecated
    public void setTouchTextListener(OnTouchTextListener onTouchTextListener) {
    }

    @Deprecated
    public void setTouchUpListener(OnTouchUpListener onTouchUpListener) {
    }

    @Deprecated
    public void setType(int i10) {
    }

    public void setWordTextNormalColor(int i10) {
        this.f6377h.f6419c = i10;
    }

    public COUINumericKeyboard(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiNumericKeyboardStyle);
    }

    public COUINumericKeyboard(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Widget_COUI_COUINumericKeyboard);
    }

    public COUINumericKeyboard(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6371e = 1;
        this.f6373f = 2;
        this.f6381j = null;
        this.f6383k = null;
        this.f6385l = -1;
        this.f6397r = true;
        this.f6398s = false;
        this.f6399t = (Cell[][]) Array.newInstance((Class<?>) Cell.class, 4, 3);
        this.f6400u = null;
        this.B = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, -1, 0, -1};
        this.C = new TextPaint();
        this.D = null;
        this.E = null;
        this.F = new Paint();
        this.M = -1.0f;
        this.N = -1;
        this.O = -1;
        this.P = new TextPaint();
        this.R = 0.12f;
        this.f6369c0 = new AnimatorListenerAdapter() { // from class: com.coui.appcompat.lockview.COUINumericKeyboard.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                COUINumericKeyboard.this.f6367a0.start();
            }
        };
        this.f6384k0 = 1.0f;
        this.f6390n0 = 1.0f;
        this.f6392o0 = new COUIEaseInterpolator();
        this.f6394p0 = new COUIInEaseInterpolator();
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.f6396q0 = attributeSet.getStyleAttribute();
        } else {
            this.f6396q0 = i10;
        }
        COUIDarkModeUtil.b(this, false);
        this.f6374f0 = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUINumericKeyboard, i10, i11);
        this.f6389n = obtainStyledAttributes.getColor(R$styleable.COUINumericKeyboard_couiNumPressColor, 0);
        Resources resources = context.getResources();
        this.G = resources.getDimensionPixelSize(R$dimen.coui_numeric_keyboard_view_width);
        this.H = resources.getDimensionPixelSize(R$dimen.coui_numeric_keyboard_view_height);
        this.I = resources.getDimensionPixelSize(R$dimen.coui_numeric_keyboard_view_size);
        this.L = resources.getDimensionPixelOffset(R$dimen.coui_numeric_keyboard_number_offset_y);
        this.M = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUINumericKeyboard_couiNumberTextSize, resources.getDimensionPixelSize(R$dimen.number_keyboard_number_size));
        this.f6378h0 = resources.getDimensionPixelSize(R$dimen.coui_numeric_keyboard_max_translate_y);
        this.N = obtainStyledAttributes.getColor(R$styleable.COUINumericKeyboard_couiNumberColor, 0);
        this.O = obtainStyledAttributes.getColor(R$styleable.COUINumericKeyboard_couiLineColor, 0);
        int color = obtainStyledAttributes.getColor(R$styleable.COUINumericKeyboard_couiWordTextNormalColor, 0);
        this.f6379i = obtainStyledAttributes.getFloat(R$styleable.COUINumericKeyboard_couiCircleMaxAlpha, 0.0f);
        this.f6404y = obtainStyledAttributes.getColor(R$styleable.COUINumericKeyboard_couiNumberBackgroundColor, 0);
        this.f6405z = obtainStyledAttributes.getColor(R$styleable.COUINumericKeyboard_couiSideBackgroundColor, 0);
        this.f6400u = obtainStyledAttributes.getDrawable(R$styleable.COUINumericKeyboard_couiKeyboardDelete);
        obtainStyledAttributes.recycle();
        if (this.f6400u == null) {
            this.f6400u = context.getDrawable(R$drawable.ic_coui_number_keyboard_launhcer_delete);
        }
        PatternExploreByTouchHelper patternExploreByTouchHelper = new PatternExploreByTouchHelper(this);
        this.f6370d0 = patternExploreByTouchHelper;
        ViewCompat.l0(this, patternExploreByTouchHelper);
        setImportantForAccessibility(1);
        this.f6370d0.invalidateRoot();
        String[] stringArray = context.getResources().getStringArray(R$array.coui_number_keyboard_letters);
        this.f6401v = context.getDrawable(R$drawable.coui_number_keyboard_normal_circle);
        this.f6402w = context.getDrawable(R$drawable.coui_number_keyboard_blur_circle);
        this.f6401v.setTint(this.f6389n);
        this.f6402w.setTint(this.f6389n);
        this.f6368b0 = VibrateUtils.h(context);
        GradientDrawable gradientDrawable = new GradientDrawable();
        this.f6403x = gradientDrawable;
        gradientDrawable.setShape(1);
        this.f6403x.setCornerRadius(this.A);
        for (int i12 = 0; i12 < 4; i12++) {
            for (int i13 = 0; i13 < 3; i13++) {
                this.f6399t[i12][i13] = new Cell(i12, i13);
                Cell[][] cellArr = this.f6399t;
                int i14 = (i12 * 3) + i13;
                cellArr[i12][i13].f6410d = stringArray[i14];
                int i15 = this.B[i14];
                if (i15 > -1) {
                    cellArr[i12][i13].f6409c = String.format(Locale.getDefault(), "%d", Integer.valueOf(i15));
                }
            }
        }
        String string = getResources().getString(R$string.coui_numeric_keyboard_sure);
        this.f6377h = new SideStyle.Builder().j(string).k(color).l(resources.getDimensionPixelSize(R$dimen.coui_number_keyboard_finish_text_size)).h(string).m(f6366t0).g();
        this.f6400u.setTint(this.N);
        this.f6375g = new SideStyle.Builder().i(this.f6400u).h(getResources().getString(R$string.coui_number_keyboard_delete)).m(f6365s0).g();
        this.f6372e0 = (AccessibilityManager) context.getSystemService("accessibility");
        I();
        F();
    }
}
