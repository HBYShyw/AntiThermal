package androidx.recyclerview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import b2.COUILog;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$styleable;
import h3.UIUtil;
import java.util.ArrayList;
import l3.ViewNative;
import m1.COUIPhysicalAnimationUtil;
import o2.COUIFlingLocateHelper;
import o2.COUIIOverScroller;
import o2.COUILocateOverScroller;
import o2.SpringOverScroller;
import q2.COUIScrollBar;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUIRecyclerView extends RecyclerView implements COUIScrollBar.c {

    /* renamed from: q0, reason: collision with root package name */
    private static final boolean f3365q0;
    private long A;
    private boolean B;
    private boolean C;
    private boolean D;
    private boolean E;
    private float F;
    private float G;
    private float H;
    private float I;
    private Paint J;
    private boolean K;
    private boolean L;
    private boolean M;
    private boolean N;
    private int O;
    private int P;
    private int Q;
    private Drawable R;
    private COUIScrollBar S;
    private int T;
    private int U;
    private int V;
    private VelocityTracker W;

    /* renamed from: a0, reason: collision with root package name */
    private int f3366a0;

    /* renamed from: b0, reason: collision with root package name */
    private int f3367b0;

    /* renamed from: c0, reason: collision with root package name */
    private int f3368c0;

    /* renamed from: d0, reason: collision with root package name */
    private int f3369d0;

    /* renamed from: e, reason: collision with root package name */
    private final int f3370e;

    /* renamed from: e0, reason: collision with root package name */
    private int f3371e0;

    /* renamed from: f, reason: collision with root package name */
    private final int f3372f;

    /* renamed from: f0, reason: collision with root package name */
    private RecyclerView.r f3373f0;

    /* renamed from: g, reason: collision with root package name */
    private ArrayList<RecyclerView.s> f3374g;

    /* renamed from: g0, reason: collision with root package name */
    private final int f3375g0;

    /* renamed from: h, reason: collision with root package name */
    private RecyclerView.s f3376h;

    /* renamed from: h0, reason: collision with root package name */
    private final int f3377h0;

    /* renamed from: i, reason: collision with root package name */
    private boolean f3378i;

    /* renamed from: i0, reason: collision with root package name */
    private c f3379i0;

    /* renamed from: j, reason: collision with root package name */
    private boolean f3380j;

    /* renamed from: j0, reason: collision with root package name */
    private final int[] f3381j0;

    /* renamed from: k, reason: collision with root package name */
    boolean f3382k;

    /* renamed from: k0, reason: collision with root package name */
    private final int[] f3383k0;

    /* renamed from: l, reason: collision with root package name */
    private int f3384l;

    /* renamed from: l0, reason: collision with root package name */
    private float f3385l0;

    /* renamed from: m, reason: collision with root package name */
    final int f3386m;

    /* renamed from: m0, reason: collision with root package name */
    private boolean f3387m0;

    /* renamed from: n, reason: collision with root package name */
    final int f3388n;

    /* renamed from: n0, reason: collision with root package name */
    private float f3389n0;

    /* renamed from: o, reason: collision with root package name */
    final int f3390o;

    /* renamed from: o0, reason: collision with root package name */
    private int f3391o0;

    /* renamed from: p, reason: collision with root package name */
    final int f3392p;

    /* renamed from: p0, reason: collision with root package name */
    private boolean f3393p0;

    /* renamed from: q, reason: collision with root package name */
    private int f3394q;

    /* renamed from: r, reason: collision with root package name */
    private int f3395r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f3396s;

    /* renamed from: t, reason: collision with root package name */
    private int f3397t;

    /* renamed from: u, reason: collision with root package name */
    private int f3398u;

    /* renamed from: v, reason: collision with root package name */
    private COUIIOverScroller f3399v;

    /* renamed from: w, reason: collision with root package name */
    private SpringOverScroller f3400w;

    /* renamed from: x, reason: collision with root package name */
    private COUILocateOverScroller f3401x;

    /* renamed from: y, reason: collision with root package name */
    private COUIFlingLocateHelper f3402y;

    /* renamed from: z, reason: collision with root package name */
    private boolean f3403z;

    /* loaded from: classes.dex */
    public static class a extends RecyclerView.o {

        /* renamed from: a, reason: collision with root package name */
        private Drawable f3404a;

        /* renamed from: b, reason: collision with root package name */
        private Paint f3405b;

        /* renamed from: c, reason: collision with root package name */
        private int f3406c;

        /* renamed from: d, reason: collision with root package name */
        private int f3407d;

        public a(Context context) {
            m(context);
        }

        private void m(Context context) {
            this.f3406c = COUIContextUtil.a(context, R$attr.couiColorDivider);
            this.f3407d = context.getResources().getDimensionPixelOffset(R$dimen.coui_list_divider_height);
            Paint paint = new Paint(1);
            this.f3405b = paint;
            paint.setColor(this.f3406c);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.o
        public void i(Canvas canvas, RecyclerView recyclerView, RecyclerView.z zVar) {
            int childCount = recyclerView.getChildCount();
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = recyclerView.getChildAt(i10);
                if (n(recyclerView, i10)) {
                    j(canvas, recyclerView, childAt);
                    boolean z10 = childAt.getLayoutDirection() == 1;
                    int y4 = (int) (childAt.getY() + childAt.getHeight());
                    int max = Math.max(1, this.f3407d) + y4;
                    int x10 = (int) (childAt.getX() + (z10 ? k(recyclerView, i10) : l(recyclerView, i10)));
                    int x11 = (int) ((childAt.getX() + childAt.getWidth()) - (z10 ? l(recyclerView, i10) : k(recyclerView, i10)));
                    Drawable drawable = this.f3404a;
                    if (drawable == null) {
                        canvas.drawRect(x10, y4, x11, max, this.f3405b);
                    } else {
                        drawable.setBounds(x10, y4, x11, max);
                        this.f3404a.draw(canvas);
                    }
                }
            }
        }

        public void j(Canvas canvas, RecyclerView recyclerView, View view) {
        }

        public int k(RecyclerView recyclerView, int i10) {
            return 0;
        }

        public int l(RecyclerView recyclerView, int i10) {
            return 0;
        }

        public boolean n(RecyclerView recyclerView, int i10) {
            throw null;
        }
    }

    /* loaded from: classes.dex */
    public interface b {
        default boolean drawDivider() {
            return false;
        }

        default View getDividerEndAlignView() {
            return null;
        }

        default int getDividerEndInset() {
            return 0;
        }

        default View getDividerStartAlignView() {
            return null;
        }

        default int getDividerStartInset() {
            return 0;
        }
    }

    /* loaded from: classes.dex */
    public class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private int f3408e;

        /* renamed from: f, reason: collision with root package name */
        private int f3409f;

        /* renamed from: g, reason: collision with root package name */
        Interpolator f3410g = RecyclerView.sQuinticInterpolator;

        /* renamed from: h, reason: collision with root package name */
        private boolean f3411h = false;

        /* renamed from: i, reason: collision with root package name */
        private boolean f3412i = false;

        c() {
        }

        private int a(int i10, int i11, int i12, int i13) {
            int i14;
            int abs = Math.abs(i10);
            int abs2 = Math.abs(i11);
            boolean z10 = abs > abs2;
            int sqrt = (int) Math.sqrt((i12 * i12) + (i13 * i13));
            int sqrt2 = (int) Math.sqrt((i10 * i10) + (i11 * i11));
            COUIRecyclerView cOUIRecyclerView = COUIRecyclerView.this;
            int width = z10 ? cOUIRecyclerView.getWidth() : cOUIRecyclerView.getHeight();
            int i15 = width / 2;
            float f10 = width;
            float f11 = i15;
            float b10 = f11 + (b(Math.min(1.0f, (sqrt2 * 1.0f) / f10)) * f11);
            if (sqrt > 0) {
                i14 = Math.round(Math.abs(b10 / sqrt) * 1000.0f) * 4;
            } else {
                if (!z10) {
                    abs = abs2;
                }
                i14 = (int) (((abs / f10) + 1.0f) * 300.0f);
            }
            return Math.min(i14, 2000);
        }

        private float b(float f10) {
            return (float) Math.sin((f10 - 0.5f) * 0.47123894f);
        }

        private void d() {
            COUIRecyclerView.this.removeCallbacks(this);
            ViewCompat.c0(COUIRecyclerView.this, this);
        }

        public void c(int i10, int i11) {
            COUIRecyclerView.this.H = i10;
            COUIRecyclerView.this.I = i11;
            COUIRecyclerView.this.setScrollState(2);
            this.f3409f = 0;
            this.f3408e = 0;
            Interpolator interpolator = this.f3410g;
            Interpolator interpolator2 = RecyclerView.sQuinticInterpolator;
            if (interpolator != interpolator2) {
                this.f3410g = interpolator2;
                COUIRecyclerView.this.f3399v.i(interpolator2);
            }
            COUIRecyclerView.this.f3399v.fling(0, 0, i10, i11, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            COUIRecyclerView.this.f3399v.setFinalX(COUIRecyclerView.this.f3402y.j(COUIRecyclerView.this.f3399v.f()));
            e();
        }

        void e() {
            if (this.f3411h) {
                this.f3412i = true;
            } else {
                d();
            }
        }

        public void f(int i10, int i11, int i12, Interpolator interpolator) {
            if (i12 == Integer.MIN_VALUE) {
                i12 = a(i10, i11, 0, 0);
            }
            int i13 = i12;
            if (interpolator == null) {
                interpolator = RecyclerView.sQuinticInterpolator;
            }
            if (this.f3410g != interpolator) {
                this.f3410g = interpolator;
                COUIRecyclerView.this.f3399v.i(interpolator);
            }
            this.f3409f = 0;
            this.f3408e = 0;
            COUIRecyclerView.this.setScrollState(2);
            COUIRecyclerView.this.f3399v.startScroll(0, 0, i10, i11, i13);
            e();
        }

        public void g() {
            COUIRecyclerView.this.removeCallbacks(this);
            COUIRecyclerView cOUIRecyclerView = COUIRecyclerView.this;
            cOUIRecyclerView.v(cOUIRecyclerView.getContext());
            COUIRecyclerView.this.f3399v.abortAnimation();
            COUIRecyclerView.this.f3400w.abortAnimation();
        }

        @Override // java.lang.Runnable
        public void run() {
            int i10;
            int i11;
            int i12;
            COUIRecyclerView cOUIRecyclerView = COUIRecyclerView.this;
            if (cOUIRecyclerView.mLayout == null) {
                g();
                return;
            }
            this.f3412i = false;
            this.f3411h = true;
            cOUIRecyclerView.consumePendingUpdateOperations();
            COUIIOverScroller cOUIIOverScroller = COUIRecyclerView.this.f3399v;
            if (cOUIIOverScroller.computeScrollOffset()) {
                int b10 = cOUIIOverScroller.b();
                int g6 = cOUIIOverScroller.g();
                int i13 = b10 - this.f3408e;
                int i14 = g6 - this.f3409f;
                this.f3408e = b10;
                this.f3409f = g6;
                COUIRecyclerView cOUIRecyclerView2 = COUIRecyclerView.this;
                int[] iArr = cOUIRecyclerView2.mReusableIntPair;
                iArr[0] = 0;
                iArr[1] = 0;
                if (cOUIRecyclerView2.dispatchNestedPreScroll(i13, i14, iArr, null, 1)) {
                    int[] iArr2 = COUIRecyclerView.this.mReusableIntPair;
                    i13 -= iArr2[0];
                    i14 -= iArr2[1];
                }
                COUIRecyclerView cOUIRecyclerView3 = COUIRecyclerView.this;
                if (cOUIRecyclerView3.mAdapter != null) {
                    int[] iArr3 = cOUIRecyclerView3.mReusableIntPair;
                    iArr3[0] = 0;
                    iArr3[1] = 0;
                    cOUIRecyclerView3.scrollStep(i13, i14, iArr3);
                    COUIRecyclerView cOUIRecyclerView4 = COUIRecyclerView.this;
                    int[] iArr4 = cOUIRecyclerView4.mReusableIntPair;
                    i11 = iArr4[0];
                    i10 = iArr4[1];
                    i13 -= i11;
                    i14 -= i10;
                    RecyclerView.y yVar = cOUIRecyclerView4.mLayout.f3492g;
                    if (yVar != null && !yVar.g() && yVar.h()) {
                        int b11 = COUIRecyclerView.this.mState.b();
                        if (b11 == 0) {
                            yVar.r();
                        } else if (yVar.f() >= b11) {
                            yVar.p(b11 - 1);
                            yVar.j(i11, i10);
                        } else {
                            yVar.j(i11, i10);
                        }
                    }
                } else {
                    i10 = 0;
                    i11 = 0;
                }
                if (!COUIRecyclerView.this.mItemDecorations.isEmpty()) {
                    COUIRecyclerView.this.invalidate();
                }
                COUIRecyclerView cOUIRecyclerView5 = COUIRecyclerView.this;
                int[] iArr5 = cOUIRecyclerView5.mReusableIntPair;
                iArr5[0] = 0;
                iArr5[1] = 0;
                cOUIRecyclerView5.dispatchNestedScroll(i11, i10, i13, i14, null, 1, iArr5);
                COUIRecyclerView cOUIRecyclerView6 = COUIRecyclerView.this;
                int[] iArr6 = cOUIRecyclerView6.mReusableIntPair;
                int i15 = i13 - iArr6[0];
                int i16 = i14 - iArr6[1];
                if (i11 != 0 || i10 != 0) {
                    cOUIRecyclerView6.dispatchOnScrolled(i11, i10);
                }
                if (!COUIRecyclerView.this.M || (i15 == 0 && i16 == 0)) {
                    i12 = i16;
                } else {
                    cOUIIOverScroller.abortAnimation();
                    COUIRecyclerView.this.M = false;
                    i12 = 0;
                    i15 = 0;
                }
                if (i12 != 0) {
                    COUIRecyclerView cOUIRecyclerView7 = COUIRecyclerView.this;
                    if (cOUIRecyclerView7.f3382k) {
                        cOUIRecyclerView7.f3384l = 3;
                        COUIRecyclerView.this.D();
                        COUIRecyclerView cOUIRecyclerView8 = COUIRecyclerView.this;
                        cOUIRecyclerView8.overScrollBy(0, i12, 0, cOUIRecyclerView8.getScrollY(), 0, 0, 0, COUIRecyclerView.this.f3395r, false);
                        if (COUIRecyclerView.this.f3403z) {
                            COUIRecyclerView.this.f3400w.h(cOUIIOverScroller.e());
                            COUIRecyclerView.this.f3400w.notifyVerticalEdgeReached(i12, 0, COUIRecyclerView.this.f3395r);
                        } else {
                            COUIRecyclerView.this.f3399v.notifyVerticalEdgeReached(i12, 0, COUIRecyclerView.this.f3395r);
                        }
                    }
                }
                if (i15 != 0) {
                    COUIRecyclerView cOUIRecyclerView9 = COUIRecyclerView.this;
                    if (cOUIRecyclerView9.f3382k) {
                        cOUIRecyclerView9.f3384l = 3;
                        COUIRecyclerView.this.D();
                        COUIRecyclerView cOUIRecyclerView10 = COUIRecyclerView.this;
                        cOUIRecyclerView10.overScrollBy(i15, 0, cOUIRecyclerView10.getScrollX(), 0, 0, 0, COUIRecyclerView.this.f3395r, 0, false);
                        if (COUIRecyclerView.this.f3403z) {
                            COUIRecyclerView.this.f3400w.d(cOUIIOverScroller.a());
                            COUIRecyclerView.this.f3400w.notifyHorizontalEdgeReached(i15, 0, COUIRecyclerView.this.f3395r);
                        } else {
                            COUIRecyclerView.this.f3399v.notifyHorizontalEdgeReached(i15, 0, COUIRecyclerView.this.f3395r);
                        }
                    }
                }
                if (!COUIRecyclerView.this.awakenScrollBars()) {
                    COUIRecyclerView.this.invalidate();
                }
                boolean z10 = cOUIIOverScroller.j() || (((cOUIIOverScroller.b() == cOUIIOverScroller.f()) || i15 != 0) && ((cOUIIOverScroller.g() == cOUIIOverScroller.c()) || i12 != 0));
                RecyclerView.y yVar2 = COUIRecyclerView.this.mLayout.f3492g;
                if (!(yVar2 != null && yVar2.g()) && z10) {
                    if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                        COUIRecyclerView.this.mPrefetchRegistry.b();
                    }
                } else {
                    e();
                    COUIRecyclerView cOUIRecyclerView11 = COUIRecyclerView.this;
                    GapWorker gapWorker = cOUIRecyclerView11.mGapWorker;
                    if (gapWorker != null) {
                        gapWorker.f(cOUIRecyclerView11, i11, i10);
                    }
                }
            }
            RecyclerView.y yVar3 = COUIRecyclerView.this.mLayout.f3492g;
            if (yVar3 != null && yVar3.g()) {
                yVar3.j(0, 0);
            }
            this.f3411h = false;
            if (this.f3412i) {
                d();
            } else {
                if (COUIRecyclerView.this.f3384l == 3 && COUIRecyclerView.this.f3382k) {
                    return;
                }
                COUIRecyclerView.this.setScrollState(0);
                COUIRecyclerView.this.stopNestedScroll(1);
            }
        }
    }

    static {
        f3365q0 = COUILog.f4543b || COUILog.d("COUIRecyclerView", 3);
    }

    public COUIRecyclerView(Context context) {
        this(context, null);
    }

    private boolean A() {
        RecyclerView.p pVar = this.mLayout;
        if (pVar == null) {
            return false;
        }
        return (pVar.l() && this.mLayout.k()) ? (getScrollY() == 0 || getScrollX() == 0) ? false : true : this.mLayout.l() ? getScrollY() != 0 : this.mLayout.k() && getScrollX() != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void B() {
        this.f3402y.n();
    }

    private boolean C() {
        return getLayoutManager() != null && (getLayoutManager() instanceof LinearLayoutManager) && ((LinearLayoutManager) getLayoutManager()).p2() == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void D() {
        if (this.f3393p0) {
            performHapticFeedback(307);
        }
    }

    private void E(float f10, float f11) {
        this.f3396s = true;
        this.f3400w.v(getScrollX(), getScrollY(), (int) f10, (int) f11);
        o(false);
    }

    private void F(boolean z10) {
        if (this.f3400w.springBack(getScrollX(), getScrollY(), 0, 0, 0, 0)) {
            o(z10);
        }
    }

    private void cancelScroll() {
        resetScroll();
        setScrollState(0);
        ViewNative.b(this, 0);
        ViewNative.c(this, 0);
    }

    private boolean dispatchToOnItemTouchListeners(MotionEvent motionEvent) {
        RecyclerView.s sVar = this.f3376h;
        if (sVar == null) {
            if (motionEvent.getAction() == 0) {
                return false;
            }
            return findInterceptingOnItemTouchListener(motionEvent);
        }
        sVar.a(this, motionEvent);
        int action = motionEvent.getAction();
        if (action == 3 || action == 1) {
            this.f3376h = null;
        }
        return true;
    }

    private boolean findInterceptingOnItemTouchListener(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        int size = this.f3374g.size();
        for (int i10 = 0; i10 < size; i10++) {
            RecyclerView.s sVar = this.f3374g.get(i10);
            if (sVar.b(this, motionEvent) && action != 3) {
                this.f3376h = sVar;
                return true;
            }
        }
        return false;
    }

    private float getVelocityAlongScrollableDirection() {
        COUIIOverScroller cOUIIOverScroller;
        COUIIOverScroller cOUIIOverScroller2;
        RecyclerView.p layoutManager = getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            return 0.0f;
        }
        if (layoutManager.k() && (cOUIIOverScroller2 = this.f3399v) != null) {
            return cOUIIOverScroller2.a();
        }
        if (!layoutManager.l() || (cOUIIOverScroller = this.f3399v) == null) {
            return 0.0f;
        }
        return cOUIIOverScroller.e();
    }

    private void o(boolean z10) {
        if (!z10) {
            D();
        }
        if (this.U != 0) {
            this.U = 0;
            dispatchOnScrollStateChanged(0);
        }
    }

    private void onPointerUp(MotionEvent motionEvent) {
        int a10 = UIUtil.a(motionEvent, motionEvent.getActionIndex());
        if (motionEvent.getPointerId(a10) == this.V) {
            int i10 = a10 == 0 ? 1 : 0;
            this.V = motionEvent.getPointerId(i10);
            int x10 = (int) (motionEvent.getX(i10) + 0.5f);
            this.f3368c0 = x10;
            this.f3366a0 = x10;
            int y4 = (int) (motionEvent.getY(i10) + 0.5f);
            this.f3369d0 = y4;
            this.f3367b0 = y4;
        }
    }

    private void p(Context context) {
        this.S = new COUIScrollBar.b(this).a();
    }

    private boolean q(View view, MotionEvent motionEvent) {
        int[] iArr = {0, 1};
        boolean z10 = true;
        for (int i10 = 0; i10 < 2; i10++) {
            motionEvent.setAction(iArr[i10]);
            z10 &= view.dispatchTouchEvent(motionEvent);
        }
        return z10;
    }

    private View r(MotionEvent motionEvent) {
        if (!y(motionEvent)) {
            return null;
        }
        Rect rect = new Rect();
        View view = null;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            if (childAt.getVisibility() == 0 || childAt.getAnimation() != null) {
                childAt.getHitRect(rect);
                boolean contains = rect.contains(((int) motionEvent.getX()) + getScrollX(), ((int) motionEvent.getY()) + getScrollY());
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                obtain.offsetLocation(getScrollX() - childAt.getLeft(), getScrollY() - childAt.getTop());
                if (contains && q(childAt, obtain)) {
                    view = childAt;
                }
                if (f3365q0) {
                    childAt.setBackground(childAt == view ? new ColorDrawable(Color.parseColor("#80FF0000")) : null);
                }
            }
        }
        return view;
    }

    private void resetScroll() {
        VelocityTracker velocityTracker = this.W;
        if (velocityTracker != null) {
            velocityTracker.clear();
        }
        stopNestedScroll(0);
    }

    private boolean s(float f10, float f11) {
        return !this.N || f10 == 0.0f || ((double) Math.abs(f11 / f10)) > Math.tan(0.3490658503988659d);
    }

    private void stopScrollersInternal() {
        x();
        this.f3379i0.g();
        RecyclerView.p pVar = this.mLayout;
        if (pVar != null) {
            pVar.M1();
        }
    }

    private void t(Context context, AttributeSet attributeSet, int i10) {
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.f3391o0 = attributeSet.getStyleAttribute();
        } else {
            this.f3391o0 = i10;
        }
        if (context != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIRecyclerView, i10, 0);
            this.P = obtainStyledAttributes.getInteger(R$styleable.COUIRecyclerView_couiScrollbars, 0);
            this.Q = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIRecyclerView_couiScrollbarSize, 0);
            this.R = obtainStyledAttributes.getDrawable(R$styleable.COUIRecyclerView_couiScrollbarThumbVertical);
            this.f3393p0 = obtainStyledAttributes.getBoolean(R$styleable.COUIRecyclerView_couiRecyclerViewEnableVibrator, true);
            obtainStyledAttributes.recycle();
        }
    }

    private void u() {
        if (this.f3374g == null) {
            this.f3374g = new ArrayList<>();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void v(Context context) {
        if (this.f3399v == null) {
            this.f3385l0 = 2.15f;
            this.f3400w = new SpringOverScroller(context);
            this.f3401x = new COUILocateOverScroller(context);
            setIsUseNativeOverScroll(false);
            setEnableFlingSpeedIncrease(this.K);
        }
    }

    private void w(Context context) {
        int i10 = context.getResources().getDisplayMetrics().heightPixels;
        this.f3394q = i10;
        this.f3395r = i10;
    }

    private void x() {
        if (this.f3379i0 == null) {
            this.f3379i0 = new c();
        }
    }

    private boolean y(MotionEvent motionEvent) {
        int x10 = (int) (motionEvent.getX() - this.f3366a0);
        int y4 = (int) (motionEvent.getY() - this.f3367b0);
        int sqrt = (int) Math.sqrt((x10 * x10) + (y4 * y4));
        long currentTimeMillis = System.currentTimeMillis() - this.A;
        if (f3365q0) {
            Log.d("COUIRecyclerView", "onTouchEvent: ACTION_UP. touchDuration = " + currentTimeMillis + ", offset = " + sqrt);
        }
        return currentTimeMillis < 100 && sqrt < 10;
    }

    private boolean z() {
        return this.f3382k && this.f3384l == 2 && A();
    }

    boolean G(int i10, int i11, MotionEvent motionEvent) {
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        consumePendingUpdateOperations();
        if (this.mAdapter == null || (i10 == 0 && i11 == 0)) {
            i12 = 0;
            i13 = 0;
            i14 = 0;
            i15 = 0;
        } else {
            if (!this.f3382k || ((getScrollY() >= 0 || i11 <= 0) && ((getScrollY() <= 0 || i11 >= 0) && ((getScrollX() >= 0 || i10 <= 0) && (getScrollX() <= 0 || i10 >= 0))))) {
                int[] iArr = this.mReusableIntPair;
                iArr[0] = 0;
                iArr[1] = 0;
                scrollStep(i10, i11, iArr);
                int[] iArr2 = this.mReusableIntPair;
                i16 = iArr2[0];
                i17 = iArr2[1];
                i18 = i10 - i16;
                i19 = i11 - i17;
            } else {
                i17 = 0;
                i16 = 0;
                i18 = 0;
                i19 = 0;
            }
            if (f3365q0) {
                Log.d("COUIRecyclerView", "scrollByInternal: y: " + i11 + " consumedY: " + i17 + " unconsumedY: " + i19);
            }
            i12 = i17;
            i13 = i16;
            i14 = i18;
            i15 = i19;
        }
        if (!this.mItemDecorations.isEmpty()) {
            invalidate();
        }
        int[] iArr3 = this.mReusableIntPair;
        iArr3[0] = 0;
        iArr3[1] = 0;
        dispatchNestedScroll(i13, i12, i14, i15, this.f3381j0, 0, iArr3);
        int[] iArr4 = this.mReusableIntPair;
        int i20 = i14 - iArr4[0];
        int i21 = i15 - iArr4[1];
        int i22 = this.f3368c0;
        int[] iArr5 = this.f3381j0;
        this.f3368c0 = i22 - iArr5[0];
        this.f3369d0 -= iArr5[1];
        if (motionEvent != null) {
            motionEvent.offsetLocation(iArr5[0], iArr5[1]);
        }
        int[] iArr6 = this.f3383k0;
        int i23 = iArr6[0];
        int[] iArr7 = this.f3381j0;
        iArr6[0] = i23 + iArr7[0];
        iArr6[1] = iArr6[1] + iArr7[1];
        if (getOverScrollMode() != 2 && motionEvent != null && this.f3382k && (MotionEventCompat.b(motionEvent, 4098) || MotionEventCompat.b(motionEvent, 8194))) {
            if (i21 != 0 || i20 != 0) {
                this.f3384l = 2;
            }
            if (Math.abs(i21) == 0 && Math.abs(i12) < 2 && Math.abs(i11) < 2 && Math.abs(getScrollY()) > 2) {
                this.f3384l = 2;
            }
            if (i21 == 0 && i12 == 0 && Math.abs(i11) > 2) {
                this.f3384l = 2;
            }
            if (Math.abs(i20) == 0 && Math.abs(i13) < 2 && Math.abs(i10) < 2 && Math.abs(getScrollX()) > 2) {
                this.f3384l = 2;
            }
            if (i20 == 0 && i13 == 0 && Math.abs(i10) > 2) {
                this.f3384l = 2;
            }
            if (this.f3380j && (getScrollX() != 0 || getScrollY() != 0)) {
                this.f3384l = 2;
            }
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            int b10 = (int) (COUIPhysicalAnimationUtil.b(i21, scrollY, this.f3394q) * this.f3389n0);
            int b11 = (int) (COUIPhysicalAnimationUtil.b(i20, scrollX, this.f3394q) * this.f3389n0);
            if ((scrollY < 0 && i11 > 0) || (scrollY > 0 && i11 < 0)) {
                b10 = (int) (COUIPhysicalAnimationUtil.b(i11, scrollX, this.f3394q) * this.f3389n0);
            }
            int i24 = b10;
            if ((scrollX < 0 && i10 > 0) || (scrollX > 0 && i10 < 0)) {
                b11 = (int) (COUIPhysicalAnimationUtil.b(i10, scrollX, this.f3394q) * this.f3389n0);
            }
            if (i24 != 0 || b11 != 0) {
                int i25 = this.f3394q;
                overScrollBy(b11, i24, scrollX, scrollY, 0, 0, i25, i25, true);
            }
        }
        if (i13 != 0 || i12 != 0) {
            dispatchOnScrolled(i13, i12);
        }
        if (!awakenScrollBars()) {
            invalidate();
        }
        return (i13 == 0 && i12 == 0) ? false : true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void addOnItemTouchListener(RecyclerView.s sVar) {
        u();
        this.f3374g.add(sVar);
    }

    @Override // android.view.View
    protected boolean awakenScrollBars() {
        COUIScrollBar cOUIScrollBar = this.S;
        return cOUIScrollBar != null ? cOUIScrollBar.c() : super.awakenScrollBars();
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.f3396s) {
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            if (scrollX == 0 && scrollY == 0) {
                overScrollBy(-scrollX, -scrollY, scrollX, scrollY, 0, 0, 0, 0, false);
                onScrollChanged(getScrollX(), getScrollY(), scrollX, scrollY);
                this.f3396s = false;
                int a10 = (int) this.f3400w.a();
                int e10 = (int) this.f3400w.e();
                this.f3400w.abortAnimation();
                setScrollState(0);
                fling(a10, e10);
                return;
            }
        }
        if (this.f3382k) {
            int i10 = this.f3384l;
            if (i10 == 2 || i10 == 3) {
                SpringOverScroller springOverScroller = this.f3400w;
                if (springOverScroller.computeScrollOffset()) {
                    int scrollX2 = getScrollX();
                    int scrollY2 = getScrollY();
                    int b10 = springOverScroller.b();
                    int g6 = springOverScroller.g();
                    if (scrollX2 != b10 || scrollY2 != g6) {
                        int i11 = this.f3395r;
                        overScrollBy(b10 - scrollX2, g6 - scrollY2, scrollX2, scrollY2, 0, 0, i11, i11, false);
                        onScrollChanged(getScrollX(), getScrollY(), scrollX2, scrollY2);
                    }
                    if (springOverScroller.j()) {
                        setScrollState(0);
                    } else {
                        setScrollState(2);
                    }
                    if (awakenScrollBars()) {
                        return;
                    }
                    postInvalidateOnAnimation();
                }
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (f3365q0) {
            this.J.setTextSize(30.0f);
            this.J.setColor(-65536);
            canvas.drawText("isOverScrolling: " + z(), getWidth() / 2.0f, (getHeight() / 2.0f) - 50.0f, this.J);
            canvas.drawText("X: FlingVelX: " + this.H + ", ClickVelX: " + this.F, getWidth() / 2.0f, getHeight() / 2.0f, this.J);
            canvas.drawText("Y: FlingVelY: " + this.I + ", ClickVelY: " + this.G, getWidth() / 2.0f, (getHeight() / 2.0f) + 50.0f, this.J);
        }
        COUIScrollBar cOUIScrollBar = this.S;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.e(canvas);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.N) {
            float velocityAlongScrollableDirection = getVelocityAlongScrollableDirection();
            if (motionEvent.getActionMasked() == 0 && this.O >= Math.abs(velocityAlongScrollableDirection)) {
                this.f3399v.abortAnimation();
                stopScroll();
            }
            if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 3) {
                F(false);
                postInvalidateOnAnimation();
            }
        }
        if (motionEvent.getActionMasked() == 5 && !this.f3387m0) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v5 */
    @Override // androidx.recyclerview.widget.RecyclerView
    public boolean fling(int i10, int i11) {
        RecyclerView.p pVar = this.mLayout;
        if (pVar == null) {
            Log.e("COUIRecyclerView", "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return false;
        }
        if (this.mLayoutSuppressed) {
            return false;
        }
        int k10 = pVar.k();
        boolean l10 = this.mLayout.l();
        if (k10 == 0 || Math.abs(i10) < this.f3375g0) {
            i10 = 0;
        }
        if (!l10 || Math.abs(i11) < this.f3375g0) {
            i11 = 0;
        }
        if (i10 == 0 && i11 == 0) {
            return false;
        }
        float f10 = i10;
        float f11 = i11;
        if (!dispatchNestedPreFling(f10, f11)) {
            this.f3384l = 1;
            boolean z10 = k10 != 0 || l10;
            dispatchNestedFling(f10, f11, z10);
            RecyclerView.r rVar = this.f3373f0;
            if (rVar != null && rVar.a(i10, i11)) {
                return true;
            }
            if (z10) {
                if (l10) {
                    k10 = (k10 == true ? 1 : 0) | 2;
                }
                startNestedScroll(k10, 1);
                int i12 = this.f3377h0;
                int max = Math.max(-i12, Math.min(i10, i12));
                int i13 = this.f3377h0;
                this.f3379i0.c(max, Math.max(-i13, Math.min(i11, i13)));
                return true;
            }
        }
        return false;
    }

    public COUIScrollBar getCOUIScrollDelegate() {
        return this.S;
    }

    @Override // q2.COUIScrollBar.c
    public View getCOUIScrollableView() {
        return this;
    }

    public int getHorizontalItemAlign() {
        return this.f3402y.h();
    }

    public boolean getIsUseNativeOverScroll() {
        return this.f3403z;
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public int getMaxFlingVelocity() {
        return this.f3377h0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public int getMinFlingVelocity() {
        return this.f3375g0;
    }

    public COUILocateOverScroller getNativeOverScroller() {
        return this.f3401x;
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public RecyclerView.r getOnFlingListener() {
        return this.f3373f0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public int getScrollState() {
        return this.U;
    }

    public c getViewFlinger() {
        return this.f3379i0;
    }

    protected void invalidateParentIfNeeded() {
        if (isHardwareAccelerated() && (getParent() instanceof View)) {
            ((View) getParent()).invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        cancelScroll();
        COUIScrollBar cOUIScrollBar = this.S;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.h();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f3400w.u();
        COUIScrollBar cOUIScrollBar = this.S;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.q();
            this.S = null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z10;
        COUIScrollBar cOUIScrollBar = this.S;
        if (cOUIScrollBar != null && cOUIScrollBar.j(motionEvent)) {
            return true;
        }
        if (this.mLayoutSuppressed) {
            return false;
        }
        this.f3376h = null;
        if (findInterceptingOnItemTouchListener(motionEvent)) {
            cancelScroll();
            return true;
        }
        RecyclerView.p pVar = this.mLayout;
        if (pVar == null) {
            return false;
        }
        boolean k10 = pVar.k();
        boolean l10 = this.mLayout.l();
        if (this.W == null) {
            this.W = VelocityTracker.obtain();
        }
        this.W.addMovement(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        int a10 = UIUtil.a(motionEvent, motionEvent.getActionIndex());
        if (actionMasked == 0) {
            if (this.f3378i) {
                this.f3378i = false;
            }
            COUIIOverScroller cOUIIOverScroller = this.f3399v;
            float a11 = cOUIIOverScroller != null ? cOUIIOverScroller.a() : 0.0f;
            COUIIOverScroller cOUIIOverScroller2 = this.f3399v;
            float e10 = cOUIIOverScroller2 != null ? cOUIIOverScroller2.e() : 0.0f;
            this.B = (Math.abs(a11) > 0.0f && Math.abs(a11) < ((float) this.T) && ((Math.abs(this.H) > 1500.0f ? 1 : (Math.abs(this.H) == 1500.0f ? 0 : -1)) > 0)) || (Math.abs(e10) > 0.0f && Math.abs(e10) < ((float) this.T) && ((Math.abs(this.I) > 1500.0f ? 1 : (Math.abs(this.I) == 1500.0f ? 0 : -1)) > 0));
            this.C = z();
            this.A = System.currentTimeMillis();
            if (f3365q0) {
                this.F = a11;
                this.G = e10;
                Log.d("COUIRecyclerView", "onInterceptTouchEvent: ACTION_DOWN, isOverScrolling = " + this.C + ", scrollVelocityX = " + Math.abs(a11) + ", mFlingVelocityX = " + this.H + ", scrollVelocityY = " + Math.abs(e10) + ", mFlingVelocityY = " + this.I);
            }
            this.V = motionEvent.getPointerId(0);
            int x10 = (int) (motionEvent.getX() + 0.5f);
            this.f3368c0 = x10;
            this.f3366a0 = x10;
            int y4 = (int) (motionEvent.getY() + 0.5f);
            this.f3369d0 = y4;
            this.f3367b0 = y4;
            if (this.U == 2) {
                getParent().requestDisallowInterceptTouchEvent(true);
                setScrollState(1);
                stopNestedScroll(1);
            }
            int[] iArr = this.f3383k0;
            iArr[1] = 0;
            iArr[0] = 0;
            int i10 = k10;
            if (l10) {
                i10 = (k10 ? 1 : 0) | 2;
            }
            startNestedScroll(i10, 0);
            this.M = false;
        } else if (actionMasked == 1) {
            this.W.clear();
            stopNestedScroll(0);
        } else if (actionMasked == 2) {
            int findPointerIndex = motionEvent.findPointerIndex(this.V);
            if (findPointerIndex < 0) {
                Log.e("COUIRecyclerView", "Error processing scroll; pointer index for id " + this.V + " not found. Did any MotionEvents get skipped?");
                return false;
            }
            int x11 = (int) (motionEvent.getX(findPointerIndex) + 0.5f);
            int y10 = (int) (motionEvent.getY(findPointerIndex) + 0.5f);
            if (this.U != 1) {
                int i11 = x11 - this.f3366a0;
                int i12 = y10 - this.f3367b0;
                if (k10 == 0 || Math.abs(i11) <= this.f3371e0 || !s(i12, i11)) {
                    z10 = false;
                } else {
                    this.f3368c0 = x11;
                    z10 = true;
                }
                if (l10 && Math.abs(i12) > this.f3371e0 && s(i11, i12)) {
                    this.f3369d0 = y10;
                    z10 = true;
                }
                if (z10) {
                    setScrollState(1);
                }
            }
        } else if (actionMasked == 3) {
            cancelScroll();
        } else if (actionMasked == 5) {
            this.V = motionEvent.getPointerId(a10);
            int x12 = (int) (motionEvent.getX(a10) + 0.5f);
            this.f3368c0 = x12;
            this.f3366a0 = x12;
            int y11 = (int) (motionEvent.getY(a10) + 0.5f);
            this.f3369d0 = y11;
            this.f3367b0 = y11;
            if (!this.f3387m0) {
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
        } else if (actionMasked == 6) {
            onPointerUp(motionEvent);
        }
        return this.U == 1;
    }

    @Override // android.view.View
    protected void onOverScrolled(int i10, int i11, boolean z10, boolean z11) {
        if (getScrollY() == i11 && getScrollX() == i10) {
            return;
        }
        if (f3365q0) {
            Log.d("COUIRecyclerView", "onOverScrolled: scrollX: " + i10 + " scrollY: " + i11);
        }
        if (this.f3384l == 3) {
            i10 = (int) (COUIPhysicalAnimationUtil.a(0, i10 + 0, this.f3398u) * this.f3389n0);
            i11 = (int) (COUIPhysicalAnimationUtil.a(0, i11 + 0, this.f3397t) * this.f3389n0);
        }
        onScrollChanged(i10, i11, getScrollX(), getScrollY());
        ViewNative.b(this, i10);
        ViewNative.c(this, i11);
        invalidateParentIfNeeded();
        awakenScrollBars();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        this.f3398u = displayMetrics.widthPixels;
        this.f3397t = displayMetrics.heightPixels;
        if (this.f3402y != null) {
            post(new Runnable() { // from class: androidx.recyclerview.widget.c
                @Override // java.lang.Runnable
                public final void run() {
                    COUIRecyclerView.this.B();
                }
            });
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0207  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0219  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x021d  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x013f  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x014f  */
    /* JADX WARN: Type inference failed for: r0v5, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v7 */
    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13;
        COUIScrollBar cOUIScrollBar = this.S;
        if (cOUIScrollBar != null && cOUIScrollBar.l(motionEvent)) {
            return true;
        }
        boolean z14 = false;
        if (this.mLayoutSuppressed || this.f3378i) {
            return false;
        }
        if (dispatchToOnItemTouchListeners(motionEvent)) {
            cancelScroll();
            return true;
        }
        RecyclerView.p pVar = this.mLayout;
        if (pVar == null) {
            return false;
        }
        int k10 = pVar.k();
        boolean l10 = this.mLayout.l();
        if (this.W == null) {
            this.W = VelocityTracker.obtain();
        }
        if (this.f3382k) {
            this.W.addMovement(motionEvent);
        }
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        int a10 = UIUtil.a(motionEvent, motionEvent.getActionIndex());
        if (actionMasked == 0) {
            int[] iArr = this.f3383k0;
            iArr[1] = 0;
            iArr[0] = 0;
        }
        int[] iArr2 = this.f3383k0;
        obtain.offsetLocation(iArr2[0], iArr2[1]);
        if (actionMasked == 0) {
            this.V = motionEvent.getPointerId(0);
            int x10 = (int) (motionEvent.getX() + 0.5f);
            this.f3368c0 = x10;
            this.f3366a0 = x10;
            int y4 = (int) (motionEvent.getY() + 0.5f);
            this.f3369d0 = y4;
            this.f3367b0 = y4;
            if ((!this.f3399v.j() || !this.f3400w.j()) && this.f3382k) {
                this.f3399v.abortAnimation();
                this.f3400w.abortAnimation();
            }
            if (l10) {
                k10 = (k10 == true ? 1 : 0) | 2;
            }
            startNestedScroll(k10, 0);
        } else if (actionMasked == 1) {
            if (this.f3382k) {
                z10 = false;
            } else {
                this.W.addMovement(obtain);
                z10 = true;
            }
            this.W.computeCurrentVelocity(1000, this.f3377h0);
            float f10 = k10 != 0 ? -this.W.getXVelocity(this.V) : 0.0f;
            float f11 = l10 ? -this.W.getYVelocity(this.V) : 0.0f;
            boolean z15 = z();
            boolean z16 = this.D && this.B;
            boolean z17 = this.E && this.C && z15;
            if (z16 || z17) {
                r(motionEvent);
            }
            if (z15) {
                if (Math.abs(f10) > 6000.0f) {
                    this.f3399v.d(f10);
                    if (getScrollX() * f10 < 0.0f) {
                        z11 = true;
                        z12 = z11;
                        if (Math.abs(f11) > 6000.0f) {
                            this.f3399v.h(f11);
                            z12 = z11;
                            if (getScrollY() * f11 < 0.0f) {
                                z12 = true;
                            }
                        }
                        if (!z12) {
                            E(f10, f11);
                        } else {
                            if (z17 && y(motionEvent)) {
                                z14 = true;
                            }
                            F(z14);
                        }
                        postInvalidateOnAnimation();
                    }
                }
                z11 = false;
                z12 = z11;
                if (Math.abs(f11) > 6000.0f) {
                }
                if (!z12) {
                }
                postInvalidateOnAnimation();
            } else if ((f10 == 0.0f && f11 == 0.0f) || !fling((int) f10, (int) f11)) {
                setScrollState(0);
            }
            resetScroll();
            z14 = z10;
        } else if (actionMasked == 2) {
            COUIIOverScroller cOUIIOverScroller = this.f3399v;
            if ((cOUIIOverScroller instanceof SpringOverScroller) && this.L) {
                ((SpringOverScroller) cOUIIOverScroller).G();
            }
            int findPointerIndex = motionEvent.findPointerIndex(this.V);
            if (findPointerIndex < 0) {
                Log.e("COUIRecyclerView", "Error processing scroll; pointer index for id " + this.V + " not found. Did any MotionEvents get skipped?");
                obtain.recycle();
                return false;
            }
            int x11 = (int) (motionEvent.getX(findPointerIndex) + 0.5f);
            int y10 = (int) (motionEvent.getY(findPointerIndex) + 0.5f);
            int i10 = this.f3368c0 - x11;
            int i11 = this.f3369d0 - y10;
            int[] iArr3 = this.mReusableIntPair;
            iArr3[0] = 0;
            iArr3[1] = 0;
            if (dispatchNestedPreScroll(i10, i11, iArr3, this.f3381j0, 0)) {
                int[] iArr4 = this.mReusableIntPair;
                i10 -= iArr4[0];
                i11 -= iArr4[1];
                int[] iArr5 = this.f3381j0;
                obtain.offsetLocation(iArr5[0], iArr5[1]);
                int[] iArr6 = this.f3383k0;
                int i12 = iArr6[0];
                int[] iArr7 = this.f3381j0;
                iArr6[0] = i12 + iArr7[0];
                iArr6[1] = iArr6[1] + iArr7[1];
            }
            if (this.U != 1) {
                if (k10 != 0) {
                    int abs = Math.abs(i10);
                    int i13 = this.f3371e0;
                    if (abs > i13) {
                        i10 = i10 > 0 ? i10 - i13 : i10 + i13;
                        z13 = true;
                        if (l10) {
                            int abs2 = Math.abs(i11);
                            int i14 = this.f3371e0;
                            if (abs2 > i14) {
                                i11 = i11 > 0 ? i11 - i14 : i11 + i14;
                                z13 = true;
                            }
                        }
                        if (z13) {
                            setScrollState(1);
                        }
                    }
                }
                z13 = false;
                if (l10) {
                }
                if (z13) {
                }
            }
            if (this.U == 1) {
                int[] iArr8 = this.f3381j0;
                this.f3368c0 = x11 - iArr8[0];
                this.f3369d0 = y10 - iArr8[1];
                if (this.f3382k) {
                    this.f3384l = 0;
                }
                if (G(k10 != 0 ? i10 : 0, l10 ? i11 : 0, obtain)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                GapWorker gapWorker = this.mGapWorker;
                if (gapWorker != null && (i10 != 0 || i11 != 0)) {
                    gapWorker.f(this, i10, i11);
                }
            }
        } else if (actionMasked == 3) {
            this.f3400w.u();
            cancelScroll();
        } else if (actionMasked == 5) {
            this.V = motionEvent.getPointerId(a10);
            int x12 = (int) (motionEvent.getX(a10) + 0.5f);
            this.f3368c0 = x12;
            this.f3366a0 = x12;
            int y11 = (int) (motionEvent.getY(a10) + 0.5f);
            this.f3369d0 = y11;
            this.f3367b0 = y11;
        } else if (actionMasked == 6) {
            onPointerUp(motionEvent);
        }
        if (!z14 && !this.f3382k) {
            this.W.addMovement(obtain);
        }
        obtain.recycle();
        return true;
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i10) {
        super.onVisibilityChanged(view, i10);
        if (i10 != 0) {
            cancelScroll();
            SpringOverScroller springOverScroller = this.f3400w;
            if (springOverScroller != null) {
                springOverScroller.abortAnimation();
            }
        }
        COUIScrollBar cOUIScrollBar = this.S;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.n(view, i10);
        }
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i10) {
        super.onWindowVisibilityChanged(i10);
        COUIScrollBar cOUIScrollBar = this.S;
        if (cOUIScrollBar != null) {
            cOUIScrollBar.o(i10);
        }
    }

    @Override // android.view.View
    protected boolean overScrollBy(int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, boolean z10) {
        int i18 = i10 + i12;
        int i19 = i11 + i13;
        if ((i12 < 0 && i18 > 0) || (i12 > 0 && i18 < 0)) {
            i18 = 0;
        }
        if ((i13 < 0 && i19 > 0) || (i13 > 0 && i19 < 0)) {
            i19 = 0;
        }
        onOverScrolled(i18, i19, false, false);
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void removeOnItemTouchListener(RecyclerView.s sVar) {
        this.f3374g.remove(sVar);
        if (this.f3376h == sVar) {
            this.f3376h = null;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z10) {
        int size = this.f3374g.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f3374g.get(i10).c(z10);
        }
        super.requestDisallowInterceptTouchEvent(z10);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void scrollBy(int i10, int i11) {
        RecyclerView.p pVar = this.mLayout;
        if (pVar == null) {
            Log.e("COUIRecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        if (this.mLayoutSuppressed) {
            return;
        }
        boolean k10 = pVar.k();
        boolean l10 = this.mLayout.l();
        if (k10 || l10) {
            if (!k10) {
                i10 = 0;
            }
            if (!l10) {
                i11 = 0;
            }
            G(i10, i11, null);
        }
    }

    public void setCustomTouchSlop(int i10) {
        Log.w("COUIRecyclerView", "setTouchSlop: set touchSlop from " + this.f3371e0 + " to " + i10);
        this.f3371e0 = i10;
    }

    public void setDispatchEventWhileScrolling(boolean z10) {
        this.N = z10;
    }

    public void setDispatchEventWhileScrollingThreshold(int i10) {
        this.O = i10;
    }

    public void setEnableFlingSpeedIncrease(boolean z10) {
        SpringOverScroller springOverScroller = this.f3400w;
        if (springOverScroller != null) {
            springOverScroller.B(z10);
        }
    }

    public void setEnablePointerDownAction(boolean z10) {
        this.f3387m0 = z10;
    }

    public void setEnableVibrator(boolean z10) {
        this.f3393p0 = z10;
    }

    public void setFlingRatio(float f10) {
        this.f3389n0 = f10;
    }

    public void setHorizontalFlingFriction(float f10) {
        this.f3401x.l(f10);
    }

    public void setHorizontalItemAlign(int i10) {
        if (C()) {
            setIsUseNativeOverScroll(true);
            this.f3402y.l(i10);
        }
    }

    public void setIsUseNativeOverScroll(boolean z10) {
        this.f3403z = z10;
        if (z10) {
            this.f3399v = this.f3401x;
        } else {
            this.f3399v = this.f3400w;
        }
    }

    public void setIsUseOptimizedScroll(boolean z10) {
        this.L = z10;
    }

    public void setItemClickableWhileOverScrolling(boolean z10) {
        this.E = z10;
    }

    public void setItemClickableWhileSlowScrolling(boolean z10) {
        this.D = z10;
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setLayoutManager(RecyclerView.p pVar) {
        super.setLayoutManager(pVar);
        if (pVar != null) {
            if (pVar.k()) {
                this.f3400w.F(3.2f);
            } else {
                this.f3400w.F(this.f3385l0);
            }
        }
    }

    public void setNativeOverScroller(COUILocateOverScroller cOUILocateOverScroller) {
        this.f3401x = cOUILocateOverScroller;
        if (this.f3403z) {
            this.f3399v = cOUILocateOverScroller;
        }
    }

    public void setNewCOUIScrollDelegate(COUIScrollBar cOUIScrollBar) {
        if (cOUIScrollBar != null) {
            this.S = cOUIScrollBar;
            cOUIScrollBar.h();
            return;
        }
        throw new IllegalArgumentException("setNewCOUIScrollDelegate must NOT be NULL.");
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setOnFlingListener(RecyclerView.r rVar) {
        this.f3373f0 = rVar;
    }

    public void setOverScrollEnable(boolean z10) {
        this.f3382k = z10;
    }

    public void setOverScrollingFixed(boolean z10) {
        this.f3380j = z10;
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    void setScrollState(int i10) {
        if (i10 == this.U) {
            return;
        }
        this.U = i10;
        if (i10 != 2) {
            stopScrollersInternal();
        }
        dispatchOnScrollStateChanged(i10);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setScrollingTouchSlop(int i10) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        if (i10 != 0) {
            if (i10 != 1) {
                Log.w("COUIRecyclerView", "setScrollingTouchSlop(): bad argument constant " + i10 + "; using default value");
            } else {
                this.f3371e0 = viewConfiguration.getScaledPagingTouchSlop();
                return;
            }
        }
        this.f3371e0 = viewConfiguration.getScaledTouchSlop();
    }

    public void setSlowScrollThreshold(int i10) {
        Log.d("COUIRecyclerView", "Slow scroll threshold set to " + i10);
        this.T = i10;
    }

    public void setSpringBackFriction(float f10) {
        this.f3400w.E(f10);
    }

    public void setSpringBackTension(float f10) {
        this.f3385l0 = f10;
        this.f3400w.F(f10);
    }

    public void setSpringOverScrollerDebug(boolean z10) {
        this.f3400w.A(z10);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void smoothScrollBy(int i10, int i11) {
        smoothScrollBy(i10, i11, null);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void smoothScrollToPosition(int i10) {
        cancelScroll();
        super.smoothScrollToPosition(i10);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void stopScroll() {
        super.stopScroll();
        setScrollState(0);
        stopScrollersInternal();
    }

    @Override // q2.COUIScrollBar.c
    public int superComputeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override // q2.COUIScrollBar.c
    public int superComputeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }

    @Override // q2.COUIScrollBar.c
    public int superComputeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    @Override // q2.COUIScrollBar.c
    public void superOnTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
    }

    public COUIRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void smoothScrollBy(int i10, int i11, Interpolator interpolator) {
        smoothScrollBy(i10, i11, interpolator, Integer.MIN_VALUE);
    }

    public COUIRecyclerView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f3370e = 0;
        this.f3372f = 512;
        this.f3380j = true;
        this.f3382k = true;
        this.f3386m = 0;
        this.f3388n = 1;
        this.f3390o = 2;
        this.f3392p = 3;
        this.f3396s = false;
        this.f3397t = 0;
        this.f3398u = 0;
        this.D = true;
        this.E = true;
        this.J = new Paint();
        this.K = true;
        this.L = true;
        this.M = false;
        this.N = false;
        this.O = 2500;
        this.P = 0;
        this.T = 2500;
        this.U = 0;
        this.V = -1;
        this.f3381j0 = new int[2];
        this.f3383k0 = new int[2];
        this.f3385l0 = 2.15f;
        this.f3387m0 = true;
        this.f3389n0 = 1.0f;
        this.f3393p0 = true;
        t(context, attributeSet, i10);
        x();
        u();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.f3371e0 = viewConfiguration.getScaledTouchSlop();
        this.f3375g0 = viewConfiguration.getScaledMinimumFlingVelocity();
        this.f3377h0 = viewConfiguration.getScaledMaximumFlingVelocity();
        setSlowScrollThreshold(2500);
        w(context);
        if (f3365q0) {
            Log.d("COUIRecyclerView", "COUIRecyclerView: overscroll_mode: " + getOverScrollMode() + " mOverScrollEnable: " + this.f3382k);
        }
        v(context);
        COUIFlingLocateHelper cOUIFlingLocateHelper = new COUIFlingLocateHelper();
        this.f3402y = cOUIFlingLocateHelper;
        cOUIFlingLocateHelper.b(this);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        this.f3398u = displayMetrics.widthPixels;
        this.f3397t = displayMetrics.heightPixels;
        if (this.P == 512) {
            p(context);
            int i11 = this.Q;
            if (i11 != 0) {
                this.S.t(i11);
            }
            Drawable drawable = this.R;
            if (drawable != null) {
                this.S.s(drawable);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void smoothScrollBy(int i10, int i11, Interpolator interpolator, int i12) {
        smoothScrollBy(i10, i11, interpolator, i12, false);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    void smoothScrollBy(int i10, int i11, Interpolator interpolator, int i12, boolean z10) {
        if (z()) {
            return;
        }
        this.M = true;
        RecyclerView.p pVar = this.mLayout;
        if (pVar == null) {
            Log.e("COUIRecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        if (this.mLayoutSuppressed) {
            return;
        }
        if (!pVar.k()) {
            i10 = 0;
        }
        if (!this.mLayout.l()) {
            i11 = 0;
        }
        if (i10 == 0 && i11 == 0) {
            return;
        }
        this.f3384l = 0;
        if (i12 == Integer.MIN_VALUE || i12 > 0) {
            if (z10) {
                int i13 = i10 != 0 ? 1 : 0;
                if (i11 != 0) {
                    i13 |= 2;
                }
                startNestedScroll(i13, 1);
            }
            this.f3379i0.f(i10, i11, i12, interpolator);
            return;
        }
        scrollBy(i10, i11);
    }
}
