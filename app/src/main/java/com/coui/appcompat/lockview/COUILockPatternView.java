package com.coui.appcompat.lockview;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.support.control.R$attr;
import com.support.control.R$dimen;
import com.support.control.R$drawable;
import com.support.control.R$string;
import com.support.control.R$style;
import com.support.control.R$styleable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import k3.VibrateUtils;
import m1.COUIEaseInterpolator;
import m1.COUIInEaseInterpolator;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUILockPatternView extends View {
    private final Rect A;
    private final Rect B;
    private int C;
    private int D;
    private int E;
    private int F;
    private int G;
    private final Interpolator H;
    private PatternExploreByTouchHelper I;
    private boolean J;
    private Drawable K;
    private Drawable L;
    private ValueAnimator M;
    private boolean N;
    private Context O;
    private AccessibilityManager P;
    private int Q;
    private Interpolator R;
    private Interpolator S;
    private int T;
    private AnimatorListenerAdapter U;

    /* renamed from: e, reason: collision with root package name */
    private final CellState[][] f6290e;

    /* renamed from: f, reason: collision with root package name */
    private final int f6291f;

    /* renamed from: g, reason: collision with root package name */
    private float f6292g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f6293h;

    /* renamed from: i, reason: collision with root package name */
    private final Paint f6294i;

    /* renamed from: j, reason: collision with root package name */
    private final Paint f6295j;

    /* renamed from: k, reason: collision with root package name */
    private OnPatternListener f6296k;

    /* renamed from: l, reason: collision with root package name */
    private final ArrayList<Cell> f6297l;

    /* renamed from: m, reason: collision with root package name */
    private final boolean[][] f6298m;

    /* renamed from: n, reason: collision with root package name */
    private float f6299n;

    /* renamed from: o, reason: collision with root package name */
    private float f6300o;

    /* renamed from: p, reason: collision with root package name */
    private long f6301p;

    /* renamed from: q, reason: collision with root package name */
    private DisplayMode f6302q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f6303r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f6304s;

    /* renamed from: t, reason: collision with root package name */
    private boolean f6305t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f6306u;

    /* renamed from: v, reason: collision with root package name */
    private float f6307v;

    /* renamed from: w, reason: collision with root package name */
    private float f6308w;

    /* renamed from: x, reason: collision with root package name */
    private float f6309x;

    /* renamed from: y, reason: collision with root package name */
    private float f6310y;

    /* renamed from: z, reason: collision with root package name */
    private final Path f6311z;

    /* loaded from: classes.dex */
    public static final class Cell {

        /* renamed from: c, reason: collision with root package name */
        private static final Cell[][] f6330c = d();

        /* renamed from: a, reason: collision with root package name */
        private final int f6331a;

        /* renamed from: b, reason: collision with root package name */
        private final int f6332b;

        private Cell(int i10, int i11) {
            c(i10, i11);
            this.f6331a = i10;
            this.f6332b = i11;
        }

        private static void c(int i10, int i11) {
            if (i10 < 0 || i10 > 2) {
                throw new IllegalArgumentException("row must be in range 0-2");
            }
            if (i11 < 0 || i11 > 2) {
                throw new IllegalArgumentException("column must be in range 0-2");
            }
        }

        private static Cell[][] d() {
            Cell[][] cellArr = (Cell[][]) Array.newInstance((Class<?>) Cell.class, 3, 3);
            for (int i10 = 0; i10 < 3; i10++) {
                for (int i11 = 0; i11 < 3; i11++) {
                    cellArr[i10][i11] = new Cell(i10, i11);
                }
            }
            return cellArr;
        }

        public static Cell e(int i10, int i11) {
            c(i10, i11);
            return f6330c[i10][i11];
        }

        public int getColumn() {
            return this.f6332b;
        }

        public int getRow() {
            return this.f6331a;
        }

        public String toString() {
            return "(row=" + this.f6331a + ",clmn=" + this.f6332b + ")";
        }
    }

    /* loaded from: classes.dex */
    public static class CellState {

        /* renamed from: a, reason: collision with root package name */
        int f6333a;

        /* renamed from: b, reason: collision with root package name */
        int f6334b;

        /* renamed from: c, reason: collision with root package name */
        float f6335c;

        /* renamed from: d, reason: collision with root package name */
        float f6336d;

        /* renamed from: e, reason: collision with root package name */
        float f6337e;

        /* renamed from: f, reason: collision with root package name */
        float f6338f;

        /* renamed from: g, reason: collision with root package name */
        public float f6339g = Float.MIN_VALUE;

        /* renamed from: h, reason: collision with root package name */
        public float f6340h = Float.MIN_VALUE;

        /* renamed from: i, reason: collision with root package name */
        public ValueAnimator f6341i;

        /* renamed from: j, reason: collision with root package name */
        float f6342j;

        /* renamed from: k, reason: collision with root package name */
        float f6343k;

        /* renamed from: l, reason: collision with root package name */
        float f6344l;

        /* renamed from: m, reason: collision with root package name */
        float f6345m;

        /* renamed from: n, reason: collision with root package name */
        boolean f6346n;

        /* renamed from: o, reason: collision with root package name */
        OnCellDrawListener f6347o;

        public void setCellDrawListener(OnCellDrawListener onCellDrawListener) {
            this.f6347o = onCellDrawListener;
        }

        public void setCellNumberAlpha(float f10) {
            this.f6338f = f10;
            this.f6347o.a();
        }

        public void setCellNumberTranslateX(int i10) {
            this.f6336d = i10;
            this.f6347o.a();
        }

        public void setCellNumberTranslateY(int i10) {
            this.f6335c = i10;
            this.f6347o.a();
        }
    }

    /* loaded from: classes.dex */
    public enum DisplayMode {
        Correct,
        Animate,
        Wrong,
        FingerprintMatch,
        FingerprintNoMatch
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnCellDrawListener {
        void a();
    }

    /* loaded from: classes.dex */
    public interface OnPatternListener {
        void a();

        void b(List<Cell> list);

        void c();

        void d(List<Cell> list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class PatternExploreByTouchHelper extends ExploreByTouchHelper {

        /* renamed from: a, reason: collision with root package name */
        private Rect f6354a;

        /* renamed from: b, reason: collision with root package name */
        private final SparseArray<VirtualViewContainer> f6355b;

        /* loaded from: classes.dex */
        class VirtualViewContainer {

            /* renamed from: a, reason: collision with root package name */
            CharSequence f6357a;

            public VirtualViewContainer(CharSequence charSequence) {
                this.f6357a = charSequence;
            }
        }

        public PatternExploreByTouchHelper(View view) {
            super(view);
            this.f6354a = new Rect();
            this.f6355b = new SparseArray<>();
            for (int i10 = 1; i10 < 10; i10++) {
                this.f6355b.put(i10, new VirtualViewContainer(b(i10)));
            }
        }

        private Rect a(int i10) {
            int i11 = i10 - 1;
            Rect rect = this.f6354a;
            int i12 = i11 / 3;
            float w10 = COUILockPatternView.this.w(i11 % 3);
            float x10 = COUILockPatternView.this.x(i12);
            float f10 = COUILockPatternView.this.f6309x * COUILockPatternView.this.f6307v * 0.5f;
            float f11 = COUILockPatternView.this.f6308w * COUILockPatternView.this.f6307v * 0.5f;
            rect.left = (int) (w10 - f11);
            rect.right = (int) (w10 + f11);
            rect.top = (int) (x10 - f10);
            rect.bottom = (int) (x10 + f10);
            return rect;
        }

        private CharSequence b(int i10) {
            return COUILockPatternView.this.getResources().getString(R$string.lockscreen_access_pattern_cell_added_verbose, String.valueOf(i10));
        }

        private int c(float f10, float f11) {
            int y4;
            int A = COUILockPatternView.this.A(f11);
            if (A < 0 || (y4 = COUILockPatternView.this.y(f10)) < 0) {
                return Integer.MIN_VALUE;
            }
            boolean z10 = COUILockPatternView.this.f6298m[A][y4];
            int i10 = (A * 3) + y4 + 1;
            if (z10) {
                return i10;
            }
            return Integer.MIN_VALUE;
        }

        private boolean d(int i10) {
            if (i10 == Integer.MIN_VALUE || i10 == Integer.MAX_VALUE) {
                return false;
            }
            int i11 = i10 - 1;
            return !COUILockPatternView.this.f6298m[i11 / 3][i11 % 3];
        }

        boolean e(int i10) {
            invalidateVirtualView(i10);
            sendEventForVirtualView(i10, 1);
            return true;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float f10, float f11) {
            return c(f10, f11);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> list) {
            if (COUILockPatternView.this.f6306u) {
                for (int i10 = 1; i10 < 10; i10++) {
                    list.add(Integer.valueOf(i10));
                }
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle) {
            if (i11 != 16) {
                return false;
            }
            return e(i10);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
            if (COUILockPatternView.this.f6306u) {
                return;
            }
            accessibilityEvent.setContentDescription(COUILockPatternView.this.getContext().getText(R$string.lockscreen_access_pattern_area));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onPopulateEventForVirtualView(int i10, AccessibilityEvent accessibilityEvent) {
            VirtualViewContainer virtualViewContainer = this.f6355b.get(i10);
            if (virtualViewContainer != null) {
                accessibilityEvent.getText().add(virtualViewContainer.f6357a);
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.y0(b(i10));
            accessibilityNodeInfoCompat.Z(b(i10));
            if (COUILockPatternView.this.f6306u) {
                accessibilityNodeInfoCompat.d0(true);
                if (d(i10)) {
                    accessibilityNodeInfoCompat.b(AccessibilityNodeInfoCompat.a.f2322i);
                    accessibilityNodeInfoCompat.W(d(i10));
                }
            }
            accessibilityNodeInfoCompat.Q(a(i10));
        }
    }

    public COUILockPatternView(Context context) {
        this(context, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int A(float f10) {
        float f11 = this.f6309x;
        float f12 = this.f6307v * f11;
        float paddingTop = getPaddingTop() + ((f11 - f12) / 2.0f);
        for (int i10 = 0; i10 < 3; i10++) {
            float f13 = (i10 * f11) + paddingTop;
            if (f10 >= f13 && f10 <= f13 + f12) {
                return i10;
            }
        }
        return -1;
    }

    private void B(MotionEvent motionEvent) {
        this.f6292g = 1.0f;
        L();
        float x10 = motionEvent.getX();
        float y4 = motionEvent.getY();
        Cell t7 = t(x10, y4);
        if (t7 != null) {
            setPatternInProgress(true);
            this.f6302q = DisplayMode.Correct;
            I();
        } else if (this.f6306u) {
            setPatternInProgress(false);
            G();
        }
        if (t7 != null) {
            float w10 = w(t7.f6332b);
            float x11 = x(t7.f6331a);
            float f10 = this.f6308w / 2.0f;
            float f11 = this.f6309x / 2.0f;
            invalidate((int) (w10 - f10), (int) (x11 - f11), (int) (w10 + f10), (int) (x11 + f11));
        }
        this.f6299n = x10;
        this.f6300o = y4;
    }

    private void C(MotionEvent motionEvent) {
        float f10 = this.f6291f;
        int historySize = motionEvent.getHistorySize();
        this.B.setEmpty();
        int i10 = 0;
        boolean z10 = false;
        while (i10 < historySize + 1) {
            float historicalX = i10 < historySize ? motionEvent.getHistoricalX(i10) : motionEvent.getX();
            float historicalY = i10 < historySize ? motionEvent.getHistoricalY(i10) : motionEvent.getY();
            Cell t7 = t(historicalX, historicalY);
            int size = this.f6297l.size();
            if (t7 != null && size == 1) {
                setPatternInProgress(true);
                I();
            }
            float abs = Math.abs(historicalX - this.f6299n);
            float abs2 = Math.abs(historicalY - this.f6300o);
            if (abs > 0.0f || abs2 > 0.0f) {
                z10 = true;
            }
            if (this.f6306u && size > 0) {
                Cell cell = this.f6297l.get(size - 1);
                float w10 = w(cell.f6332b);
                float x10 = x(cell.f6331a);
                float min = Math.min(w10, historicalX) - f10;
                float max = Math.max(w10, historicalX) + f10;
                float min2 = Math.min(x10, historicalY) - f10;
                float max2 = Math.max(x10, historicalY) + f10;
                if (t7 != null) {
                    float f11 = this.f6308w * 0.5f;
                    float f12 = this.f6309x * 0.5f;
                    float w11 = w(t7.f6332b);
                    float x11 = x(t7.f6331a);
                    min = Math.min(w11 - f11, min);
                    max = Math.max(w11 + f11, max);
                    min2 = Math.min(x11 - f12, min2);
                    max2 = Math.max(x11 + f12, max2);
                }
                this.B.union(Math.round(min), Math.round(min2), Math.round(max), Math.round(max2));
            }
            i10++;
        }
        this.f6299n = motionEvent.getX();
        this.f6300o = motionEvent.getY();
        if (z10) {
            this.A.union(this.B);
            invalidate(this.A);
            this.A.set(this.B);
        }
    }

    private void D() {
        if (this.f6297l.isEmpty()) {
            return;
        }
        setPatternInProgress(false);
        q();
        H();
        invalidate();
    }

    private void E(CellState cellState, List<Animator> list, int i10) {
        cellState.setCellNumberAlpha(0.0f);
        cellState.setCellNumberTranslateY(this.Q);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(cellState, "cellNumberAlpha", 0.0f, Color.alpha(this.C) / 255.0f);
        long j10 = i10 * 16;
        ofFloat.setStartDelay(166 + j10);
        ofFloat.setDuration(167L);
        ofFloat.setInterpolator(this.R);
        list.add(ofFloat);
        ObjectAnimator ofInt = ObjectAnimator.ofInt(cellState, "cellNumberTranslateY", this.Q, 0);
        ofInt.setStartDelay(j10);
        ofInt.setDuration(500L);
        ofInt.setInterpolator(this.S);
        list.add(ofInt);
    }

    private void F() {
        OnPatternListener onPatternListener = this.f6296k;
        if (onPatternListener != null) {
            onPatternListener.b(this.f6297l);
        }
        this.I.invalidateRoot();
    }

    private void G() {
        M(R$string.lockscreen_access_pattern_cleared);
        OnPatternListener onPatternListener = this.f6296k;
        if (onPatternListener != null) {
            onPatternListener.a();
        }
    }

    private void H() {
        M(R$string.lockscreen_access_pattern_detected);
        OnPatternListener onPatternListener = this.f6296k;
        if (onPatternListener != null) {
            onPatternListener.d(this.f6297l);
        }
    }

    private void I() {
        M(R$string.lockscreen_access_pattern_start);
        OnPatternListener onPatternListener = this.f6296k;
        if (onPatternListener != null) {
            onPatternListener.c();
        }
    }

    private void J() {
        if (this.N) {
            performHapticFeedback(302);
        } else {
            performHapticFeedback(1);
        }
    }

    private void K() {
        if (this.f6305t) {
            if (this.N) {
                performHapticFeedback(304, 3);
            } else {
                performHapticFeedback(300, 3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void L() {
        this.f6297l.clear();
        s();
        this.f6302q = DisplayMode.Correct;
        invalidate();
    }

    private void M(int i10) {
        announceForAccessibility(this.O.getString(i10));
    }

    private void O(Cell cell) {
        CellState cellState = this.f6290e[cell.f6331a][cell.f6332b];
        S(cellState);
        Q(cellState);
        R(cellState, this.f6299n, this.f6300o, w(cell.f6332b), x(cell.f6331a));
    }

    private void P() {
        ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofKeyframe("pathAlpha", Keyframe.ofFloat(0.0f, 1.0f), Keyframe.ofFloat(0.2f, 0.35f), Keyframe.ofFloat(0.4f, 1.0f), Keyframe.ofFloat(0.6f, 0.15f), Keyframe.ofFloat(0.8f, 0.5f), Keyframe.ofFloat(1.0f, 0.0f)));
        ofPropertyValuesHolder.setDuration(1000L);
        ofPropertyValuesHolder.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.lockview.COUILockPatternView.3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                for (int i10 = 0; i10 < 3; i10++) {
                    for (int i11 = 0; i11 < 3; i11++) {
                        CellState cellState = COUILockPatternView.this.f6290e[i10][i11];
                        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                        cellState.f6344l = floatValue;
                        cellState.f6346n = floatValue <= 0.1f;
                    }
                }
                COUILockPatternView.this.invalidate();
            }
        });
        ofPropertyValuesHolder.start();
    }

    private void Q(final CellState cellState) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setInterpolator(new COUIEaseInterpolator());
        ofFloat.setDuration(230L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.lockview.COUILockPatternView.9
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cellState.f6344l = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            }
        });
        ofFloat.start();
    }

    private void R(final CellState cellState, final float f10, final float f11, final float f12, final float f13) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.lockview.COUILockPatternView.5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                CellState cellState2 = cellState;
                float f14 = 1.0f - floatValue;
                cellState2.f6339g = (f10 * f14) + (f12 * floatValue);
                cellState2.f6340h = (f14 * f11) + (floatValue * f13);
                COUILockPatternView.this.invalidate();
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.coui.appcompat.lockview.COUILockPatternView.6
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                cellState.f6341i = null;
            }
        });
        ofFloat.setInterpolator(this.H);
        ofFloat.setDuration(100L);
        ofFloat.start();
        cellState.f6341i = ofFloat;
    }

    private void S(final CellState cellState) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(460L);
        animatorSet.setInterpolator(new COUIInEaseInterpolator());
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 7.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.lockview.COUILockPatternView.7
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cellState.f6343k = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                COUILockPatternView.this.invalidate();
            }
        });
        ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofKeyframe("alpha", Keyframe.ofFloat(0.0f, 0.0f), Keyframe.ofFloat(0.5f, this.f6310y), Keyframe.ofFloat(1.0f, 0.0f)));
        ofPropertyValuesHolder.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.lockview.COUILockPatternView.8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cellState.f6345m = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                COUILockPatternView.this.invalidate();
            }
        });
        animatorSet.play(ofFloat).with(ofPropertyValuesHolder);
        animatorSet.start();
    }

    private void T() {
        ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofKeyframe("pathAlpha", Keyframe.ofFloat(0.0f, 1.0f), Keyframe.ofFloat(0.2f, 0.35f), Keyframe.ofFloat(0.4f, 1.0f), Keyframe.ofFloat(0.6f, 0.15f), Keyframe.ofFloat(0.8f, 0.5f), Keyframe.ofFloat(1.0f, 0.0f)));
        this.M = ofPropertyValuesHolder;
        ofPropertyValuesHolder.setDuration(1000L);
        this.M.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.lockview.COUILockPatternView.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUILockPatternView.this.f6292g = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                Iterator it = COUILockPatternView.this.f6297l.iterator();
                while (it.hasNext()) {
                    Cell cell = (Cell) it.next();
                    CellState cellState = COUILockPatternView.this.f6290e[cell.f6331a][cell.f6332b];
                    cellState.f6344l = COUILockPatternView.this.f6292g;
                    cellState.f6346n = COUILockPatternView.this.f6292g <= 0.1f;
                }
                COUILockPatternView.this.invalidate();
            }
        });
        this.M.start();
    }

    private void p(Cell cell) {
        this.f6298m[cell.getRow()][cell.getColumn()] = true;
        this.f6297l.add(cell);
        if (!this.f6304s) {
            O(cell);
        }
        F();
    }

    private void q() {
        for (int i10 = 0; i10 < 3; i10++) {
            for (int i11 = 0; i11 < 3; i11++) {
                CellState cellState = this.f6290e[i10][i11];
                ValueAnimator valueAnimator = cellState.f6341i;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    cellState.f6339g = Float.MIN_VALUE;
                    cellState.f6340h = Float.MIN_VALUE;
                }
            }
        }
    }

    private Cell r(float f10, float f11) {
        int y4;
        int A = A(f11);
        if (A >= 0 && (y4 = y(f10)) >= 0 && !this.f6298m[A][y4]) {
            return Cell.e(A, y4);
        }
        return null;
    }

    private void s() {
        for (int i10 = 0; i10 < 3; i10++) {
            for (int i11 = 0; i11 < 3; i11++) {
                this.f6298m[i10][i11] = false;
            }
        }
    }

    private void setPatternInProgress(boolean z10) {
        this.f6306u = z10;
        this.I.invalidateRoot();
    }

    private Cell t(float f10, float f11) {
        Cell r10 = r(f10, f11);
        Cell cell = null;
        if (r10 == null) {
            return null;
        }
        ArrayList<Cell> arrayList = this.f6297l;
        if (!arrayList.isEmpty()) {
            Cell cell2 = arrayList.get(arrayList.size() - 1);
            int i10 = r10.f6331a - cell2.f6331a;
            int i11 = r10.f6332b - cell2.f6332b;
            int i12 = cell2.f6331a;
            int i13 = cell2.f6332b;
            if (Math.abs(i10) == 2 && Math.abs(i11) != 1) {
                i12 = cell2.f6331a + (i10 > 0 ? 1 : -1);
            }
            if (Math.abs(i11) == 2 && Math.abs(i10) != 1) {
                i13 = cell2.f6332b + (i11 <= 0 ? -1 : 1);
            }
            cell = Cell.e(i12, i13);
        }
        if (cell != null && !this.f6298m[cell.f6331a][cell.f6332b]) {
            p(cell);
        }
        p(r10);
        if (this.f6305t) {
            J();
        }
        return r10;
    }

    private void u(Canvas canvas, float f10, float f11, float f12, boolean z10, float f13) {
        this.f6294i.setColor(this.C);
        this.f6294i.setAlpha((int) (f13 * 255.0f));
        canvas.drawCircle(f10, f11, f12, this.f6294i);
    }

    private void v(Canvas canvas, float f10, float f11, float f12, float f13, float f14, float f15) {
        canvas.save();
        int intrinsicWidth = this.K.getIntrinsicWidth();
        float f16 = intrinsicWidth / 2;
        int i10 = (int) (f10 - f16);
        int i11 = (int) (f11 - f16);
        canvas.scale(f12, f12, f10, f11);
        this.K.setTint(z(true));
        this.K.setBounds(i10, i11, i10 + intrinsicWidth, intrinsicWidth + i11);
        this.K.setAlpha((int) (f13 * 255.0f));
        this.K.draw(canvas);
        canvas.restore();
        canvas.save();
        int intrinsicWidth2 = this.L.getIntrinsicWidth();
        float f17 = intrinsicWidth2 / 2;
        int i12 = (int) (f10 - f17);
        int i13 = (int) (f11 - f17);
        canvas.scale(f14, f14, f10, f11);
        this.L.setTint(z(true));
        this.L.setBounds(i12, i13, i12 + intrinsicWidth2, intrinsicWidth2 + i13);
        this.L.setAlpha((int) (f15 * 255.0f));
        this.L.draw(canvas);
        canvas.restore();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float w(int i10) {
        float paddingLeft = getPaddingLeft();
        float f10 = this.f6308w;
        return paddingLeft + (i10 * f10) + (f10 / 2.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float x(int i10) {
        float paddingTop = getPaddingTop();
        float f10 = this.f6309x;
        return paddingTop + (i10 * f10) + (f10 / 2.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int y(float f10) {
        float f11 = this.f6308w;
        float f12 = this.f6307v * f11;
        float paddingLeft = getPaddingLeft() + ((f11 - f12) / 2.0f);
        for (int i10 = 0; i10 < 3; i10++) {
            float f13 = (i10 * f11) + paddingLeft;
            if (f10 >= f13 && f10 <= f13 + f12) {
                return i10;
            }
        }
        return -1;
    }

    private int z(boolean z10) {
        DisplayMode displayMode = this.f6302q;
        if (displayMode != DisplayMode.Wrong && displayMode != DisplayMode.FingerprintNoMatch) {
            if (displayMode != DisplayMode.Correct && displayMode != DisplayMode.Animate && displayMode != DisplayMode.FingerprintMatch) {
                if (z10 && !this.f6304s && !this.f6306u) {
                    throw new IllegalStateException("unknown display mode " + this.f6302q);
                }
                return this.C;
            }
            return this.E;
        }
        return this.D;
    }

    public void N(DisplayMode displayMode, List<Cell> list) {
        this.f6297l.clear();
        this.f6297l.addAll(list);
        s();
        for (Cell cell : list) {
            this.f6298m[cell.getRow()][cell.getColumn()] = true;
        }
        setDisplayMode(displayMode);
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.I.dispatchHoverEvent(motionEvent) | super.dispatchHoverEvent(motionEvent);
    }

    public CellState[][] getCellStates() {
        return this.f6290e;
    }

    public AnimatorSet getEnterAnim() {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        for (int i10 = 0; i10 < 3; i10++) {
            for (int i11 = 0; i11 < 3; i11++) {
                E(this.f6290e[i10][i11], arrayList, (i10 * 3) + i11);
            }
        }
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    @Deprecated
    public Animator getFailAnimator() {
        return ValueAnimator.ofFloat(0.0f, 1.0f);
    }

    @Deprecated
    public Animator getSuccessAnimator() {
        return ValueAnimator.ofInt(255, 0);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ValueAnimator valueAnimator = this.M;
        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            this.M.removeAllListeners();
            this.M = null;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float f10;
        float f11;
        float f12;
        CellState cellState;
        COUILockPatternView cOUILockPatternView = this;
        ArrayList<Cell> arrayList = cOUILockPatternView.f6297l;
        int size = arrayList.size();
        boolean[][] zArr = cOUILockPatternView.f6298m;
        if (cOUILockPatternView.f6302q == DisplayMode.Animate) {
            int elapsedRealtime = (((int) (SystemClock.elapsedRealtime() - cOUILockPatternView.f6301p)) % ((size + 1) * 700)) / 700;
            s();
            for (int i10 = 0; i10 < elapsedRealtime; i10++) {
                Cell cell = arrayList.get(i10);
                zArr[cell.getRow()][cell.getColumn()] = true;
            }
            if (elapsedRealtime > 0 && elapsedRealtime < size) {
                float f13 = (r3 % 700) / 700.0f;
                Cell cell2 = arrayList.get(elapsedRealtime - 1);
                float w10 = cOUILockPatternView.w(cell2.f6332b);
                float x10 = cOUILockPatternView.x(cell2.f6331a);
                Cell cell3 = arrayList.get(elapsedRealtime);
                float w11 = (cOUILockPatternView.w(cell3.f6332b) - w10) * f13;
                float x11 = f13 * (cOUILockPatternView.x(cell3.f6331a) - x10);
                cOUILockPatternView.f6299n = w10 + w11;
                cOUILockPatternView.f6300o = x10 + x11;
            }
            invalidate();
        }
        Path path = cOUILockPatternView.f6311z;
        path.rewind();
        if (!cOUILockPatternView.f6304s) {
            cOUILockPatternView.f6295j.setColor(cOUILockPatternView.z(true));
            cOUILockPatternView.f6295j.setAlpha((int) (cOUILockPatternView.f6292g * 255.0f));
            float f14 = 0.0f;
            float f15 = 0.0f;
            int i11 = 0;
            boolean z10 = false;
            while (i11 < size) {
                Cell cell4 = arrayList.get(i11);
                if (!zArr[cell4.f6331a][cell4.f6332b]) {
                    break;
                }
                f14 = cOUILockPatternView.w(cell4.f6332b);
                f15 = cOUILockPatternView.x(cell4.f6331a);
                if (i11 == 0) {
                    path.rewind();
                    path.moveTo(f14, f15);
                }
                if (i11 != 0) {
                    CellState cellState2 = cOUILockPatternView.f6290e[cell4.f6331a][cell4.f6332b];
                    float f16 = cellState2.f6339g;
                    if (f16 != Float.MIN_VALUE) {
                        float f17 = cellState2.f6340h;
                        if (f17 != Float.MIN_VALUE) {
                            path.lineTo(f16, f17);
                        }
                    }
                    path.lineTo(f14, f15);
                }
                i11++;
                z10 = true;
            }
            if ((cOUILockPatternView.f6306u || cOUILockPatternView.f6302q == DisplayMode.Animate) && z10) {
                path.moveTo(f14, f15);
                path.lineTo(cOUILockPatternView.f6299n, cOUILockPatternView.f6300o);
            }
            canvas.drawPath(path, cOUILockPatternView.f6295j);
        }
        int i12 = 0;
        while (true) {
            int i13 = 3;
            if (i12 >= 3) {
                return;
            }
            float x12 = cOUILockPatternView.x(i12);
            int i14 = 0;
            while (i14 < i13) {
                CellState cellState3 = cOUILockPatternView.f6290e[i12][i14];
                float w12 = cOUILockPatternView.w(i14);
                float f18 = cellState3.f6335c;
                float f19 = cellState3.f6336d;
                boolean z11 = zArr[i12][i14];
                if (z11 || cOUILockPatternView.f6302q == DisplayMode.FingerprintNoMatch) {
                    f10 = f19;
                    f11 = f18;
                    f12 = w12;
                    cellState = cellState3;
                    v(canvas, ((int) w12) + f19, ((int) x12) + f18, cellState3.f6342j, cellState3.f6344l, cellState3.f6343k, cellState3.f6345m);
                } else {
                    f10 = f19;
                    f11 = f18;
                    f12 = w12;
                    cellState = cellState3;
                }
                if (cellState.f6346n) {
                    u(canvas, ((int) f12) + f10, ((int) x12) + f11, cellState.f6337e, z11, cellState.f6338f);
                }
                i14++;
                i13 = 3;
                cOUILockPatternView = this;
            }
            i12++;
            cOUILockPatternView = this;
        }
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        if (this.P.isTouchExplorationEnabled()) {
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

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i10);
        int size = View.MeasureSpec.getSize(i10);
        int mode2 = View.MeasureSpec.getMode(i11);
        int size2 = View.MeasureSpec.getSize(i11);
        if (mode == Integer.MIN_VALUE) {
            size = this.F;
        }
        if (mode2 == Integer.MIN_VALUE) {
            size2 = this.G;
        }
        setMeasuredDimension(size, size2);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        N(DisplayMode.Correct, COUILockPatternUtils.b(savedState.getSerializedPattern()));
        this.f6302q = DisplayMode.values()[savedState.getDisplayMode()];
        this.f6303r = savedState.k();
        this.f6304s = savedState.j();
        this.f6305t = savedState.l();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), COUILockPatternUtils.a(this.f6297l), this.f6302q.ordinal(), this.f6303r, this.f6304s, this.f6305t);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        this.f6308w = ((i10 - getPaddingLeft()) - getPaddingRight()) / 3.0f;
        this.f6309x = ((i11 - getPaddingTop()) - getPaddingBottom()) / 3.0f;
        this.I.invalidateRoot();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.f6303r || !isEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            ValueAnimator valueAnimator = this.M;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.M.end();
            }
            B(motionEvent);
            return true;
        }
        if (action == 1) {
            D();
            return true;
        }
        if (action == 2) {
            C(motionEvent);
            return true;
        }
        if (action != 3) {
            return false;
        }
        if (this.f6306u) {
            setPatternInProgress(false);
            L();
            G();
        }
        return true;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.f6302q = displayMode;
        if (displayMode == DisplayMode.Animate) {
            if (this.f6297l.size() != 0) {
                this.f6301p = SystemClock.elapsedRealtime();
                Cell cell = this.f6297l.get(0);
                this.f6299n = w(cell.getColumn());
                this.f6300o = x(cell.getRow());
                s();
            } else {
                throw new IllegalStateException("you must have a pattern to animate if you want to set the display mode to animate");
            }
        }
        if (displayMode == DisplayMode.Wrong) {
            if (this.f6297l.size() > 1) {
                K();
            }
            T();
        }
        if (displayMode == DisplayMode.FingerprintNoMatch) {
            P();
        }
        invalidate();
    }

    public void setErrorColor(int i10) {
        this.D = i10;
    }

    public void setInStealthMode(boolean z10) {
        this.f6304s = z10;
    }

    public void setLockPassword(boolean z10) {
        this.J = z10;
    }

    public void setOnPatternListener(OnPatternListener onPatternListener) {
        this.f6296k = onPatternListener;
    }

    public void setOuterCircleMaxAlpha(int i10) {
        this.f6310y = i10;
    }

    public void setPathColor(int i10) {
        this.f6295j.setColor(i10);
    }

    public void setRegularColor(int i10) {
        this.C = i10;
    }

    public void setSuccessColor(int i10) {
        this.E = i10;
    }

    public void setTactileFeedbackEnabled(boolean z10) {
        this.f6305t = z10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.coui.appcompat.lockview.COUILockPatternView.SavedState.1
            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        };

        /* renamed from: e, reason: collision with root package name */
        private final String f6359e;

        /* renamed from: f, reason: collision with root package name */
        private final int f6360f;

        /* renamed from: g, reason: collision with root package name */
        private final boolean f6361g;

        /* renamed from: h, reason: collision with root package name */
        private final boolean f6362h;

        /* renamed from: i, reason: collision with root package name */
        private final boolean f6363i;

        public int getDisplayMode() {
            return this.f6360f;
        }

        public String getSerializedPattern() {
            return this.f6359e;
        }

        public boolean j() {
            return this.f6362h;
        }

        public boolean k() {
            return this.f6361g;
        }

        public boolean l() {
            return this.f6363i;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeString(this.f6359e);
            parcel.writeInt(this.f6360f);
            parcel.writeValue(Boolean.valueOf(this.f6361g));
            parcel.writeValue(Boolean.valueOf(this.f6362h));
            parcel.writeValue(Boolean.valueOf(this.f6363i));
        }

        private SavedState(Parcelable parcelable, String str, int i10, boolean z10, boolean z11, boolean z12) {
            super(parcelable);
            this.f6359e = str;
            this.f6360f = i10;
            this.f6361g = z10;
            this.f6362h = z11;
            this.f6363i = z12;
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.f6359e = parcel.readString();
            this.f6360f = parcel.readInt();
            this.f6361g = ((Boolean) parcel.readValue(null)).booleanValue();
            this.f6362h = ((Boolean) parcel.readValue(null)).booleanValue();
            this.f6363i = ((Boolean) parcel.readValue(null)).booleanValue();
        }
    }

    public COUILockPatternView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f6292g = 1.0f;
        this.f6293h = false;
        Paint paint = new Paint();
        this.f6294i = paint;
        Paint paint2 = new Paint();
        this.f6295j = paint2;
        this.f6297l = new ArrayList<>(9);
        this.f6298m = (boolean[][]) Array.newInstance((Class<?>) boolean.class, 3, 3);
        this.f6299n = -1.0f;
        this.f6300o = -1.0f;
        this.f6302q = DisplayMode.Correct;
        this.f6303r = true;
        this.f6304s = false;
        this.f6305t = true;
        this.f6306u = false;
        this.f6307v = 0.6f;
        this.f6311z = new Path();
        this.A = new Rect();
        this.B = new Rect();
        this.J = false;
        this.R = new COUIEaseInterpolator();
        this.S = new COUIInEaseInterpolator();
        this.U = new AnimatorListenerAdapter() { // from class: com.coui.appcompat.lockview.COUILockPatternView.4
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                COUILockPatternView.this.L();
                if (COUILockPatternView.this.M != null) {
                    COUILockPatternView.this.M.removeAllListeners();
                }
            }
        };
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.T = attributeSet.getStyleAttribute();
        } else {
            this.T = R$attr.couiLockPatternViewStyle;
        }
        this.O = context;
        COUIDarkModeUtil.b(this, false);
        this.O = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUILockPatternView, R$attr.couiLockPatternViewStyle, COUIContextUtil.e(context) ? R$style.Widget_COUI_COUILockPatternView_Dark : R$style.Widget_COUI_COUILockPatternView);
        setClickable(true);
        paint2.setAntiAlias(true);
        paint2.setDither(true);
        this.C = obtainStyledAttributes.getColor(R$styleable.COUILockPatternView_couiRegularColor, 0);
        this.D = obtainStyledAttributes.getColor(R$styleable.COUILockPatternView_couiErrorColor, 0);
        this.E = obtainStyledAttributes.getColor(R$styleable.COUILockPatternView_couiSuccessColor, 0);
        paint2.setColor(obtainStyledAttributes.getColor(R$styleable.COUILockPatternView_couiPathColor, this.C));
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeJoin(Paint.Join.ROUND);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.lock_pattern_dot_line_width);
        this.f6291f = dimensionPixelSize;
        paint2.setStrokeWidth(dimensionPixelSize);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.lock_pattern_dot_size);
        paint.setAntiAlias(true);
        paint.setDither(true);
        this.Q = getResources().getDimensionPixelSize(R$dimen.color_lock_pattern_view_max_translate_y);
        this.f6290e = (CellState[][]) Array.newInstance((Class<?>) CellState.class, 3, 3);
        for (int i10 = 0; i10 < 3; i10++) {
            for (int i11 = 0; i11 < 3; i11++) {
                this.f6290e[i10][i11] = new CellState();
                CellState[][] cellStateArr = this.f6290e;
                cellStateArr[i10][i11].f6337e = dimensionPixelSize2 / 2;
                cellStateArr[i10][i11].f6333a = i10;
                cellStateArr[i10][i11].f6334b = i11;
                cellStateArr[i10][i11].f6338f = Color.alpha(this.C) / 255.0f;
                CellState[][] cellStateArr2 = this.f6290e;
                cellStateArr2[i10][i11].f6344l = 0.0f;
                cellStateArr2[i10][i11].f6342j = 1.0f;
                cellStateArr2[i10][i11].f6345m = 0.0f;
                cellStateArr2[i10][i11].f6343k = 1.0f;
                cellStateArr2[i10][i11].f6346n = true;
                cellStateArr2[i10][i11].setCellDrawListener(new OnCellDrawListener() { // from class: com.coui.appcompat.lockview.COUILockPatternView.1
                    @Override // com.coui.appcompat.lockview.COUILockPatternView.OnCellDrawListener
                    public void a() {
                        COUILockPatternView.this.invalidate();
                    }
                });
            }
        }
        this.K = getResources().getDrawable(R$drawable.coui_lock_pattern_inner_circle);
        this.L = getResources().getDrawable(R$drawable.coui_lock_pattern_outer_circle);
        this.F = getResources().getDimensionPixelSize(R$dimen.coui_lock_pattern_view_width);
        this.G = getResources().getDimensionPixelSize(R$dimen.coui_lock_pattern_view_height);
        this.f6310y = obtainStyledAttributes.getFloat(R$styleable.COUILockPatternView_couiOuterCircleMaxAlpha, 0.0f);
        this.H = AnimationUtils.loadInterpolator(context, R.interpolator.fast_out_slow_in);
        PatternExploreByTouchHelper patternExploreByTouchHelper = new PatternExploreByTouchHelper(this);
        this.I = patternExploreByTouchHelper;
        ViewCompat.l0(this, patternExploreByTouchHelper);
        this.P = (AccessibilityManager) this.O.getSystemService("accessibility");
        obtainStyledAttributes.recycle();
        this.N = VibrateUtils.h(context);
    }
}
