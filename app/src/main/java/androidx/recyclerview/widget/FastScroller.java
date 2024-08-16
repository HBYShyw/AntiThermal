package androidx.recyclerview.widget;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.MotionEvent;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FastScroller.java */
/* renamed from: androidx.recyclerview.widget.g, reason: use source file name */
/* loaded from: classes.dex */
public class FastScroller extends RecyclerView.o implements RecyclerView.s {
    private static final int[] D = {R.attr.state_pressed};
    private static final int[] E = new int[0];
    int A;
    private final Runnable B;
    private final RecyclerView.t C;

    /* renamed from: a, reason: collision with root package name */
    private final int f3732a;

    /* renamed from: b, reason: collision with root package name */
    private final int f3733b;

    /* renamed from: c, reason: collision with root package name */
    final StateListDrawable f3734c;

    /* renamed from: d, reason: collision with root package name */
    final Drawable f3735d;

    /* renamed from: e, reason: collision with root package name */
    private final int f3736e;

    /* renamed from: f, reason: collision with root package name */
    private final int f3737f;

    /* renamed from: g, reason: collision with root package name */
    private final StateListDrawable f3738g;

    /* renamed from: h, reason: collision with root package name */
    private final Drawable f3739h;

    /* renamed from: i, reason: collision with root package name */
    private final int f3740i;

    /* renamed from: j, reason: collision with root package name */
    private final int f3741j;

    /* renamed from: k, reason: collision with root package name */
    int f3742k;

    /* renamed from: l, reason: collision with root package name */
    int f3743l;

    /* renamed from: m, reason: collision with root package name */
    float f3744m;

    /* renamed from: n, reason: collision with root package name */
    int f3745n;

    /* renamed from: o, reason: collision with root package name */
    int f3746o;

    /* renamed from: p, reason: collision with root package name */
    float f3747p;

    /* renamed from: s, reason: collision with root package name */
    private RecyclerView f3750s;

    /* renamed from: z, reason: collision with root package name */
    final ValueAnimator f3757z;

    /* renamed from: q, reason: collision with root package name */
    private int f3748q = 0;

    /* renamed from: r, reason: collision with root package name */
    private int f3749r = 0;

    /* renamed from: t, reason: collision with root package name */
    private boolean f3751t = false;

    /* renamed from: u, reason: collision with root package name */
    private boolean f3752u = false;

    /* renamed from: v, reason: collision with root package name */
    private int f3753v = 0;

    /* renamed from: w, reason: collision with root package name */
    private int f3754w = 0;

    /* renamed from: x, reason: collision with root package name */
    private final int[] f3755x = new int[2];

    /* renamed from: y, reason: collision with root package name */
    private final int[] f3756y = new int[2];

    /* compiled from: FastScroller.java */
    /* renamed from: androidx.recyclerview.widget.g$a */
    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            FastScroller.this.q(500);
        }
    }

    /* compiled from: FastScroller.java */
    /* renamed from: androidx.recyclerview.widget.g$b */
    /* loaded from: classes.dex */
    class b extends RecyclerView.t {
        b() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.t
        public void onScrolled(RecyclerView recyclerView, int i10, int i11) {
            FastScroller.this.B(recyclerView.computeHorizontalScrollOffset(), recyclerView.computeVerticalScrollOffset());
        }
    }

    /* compiled from: FastScroller.java */
    /* renamed from: androidx.recyclerview.widget.g$c */
    /* loaded from: classes.dex */
    private class c extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        private boolean f3760a = false;

        c() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f3760a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f3760a) {
                this.f3760a = false;
                return;
            }
            if (((Float) FastScroller.this.f3757z.getAnimatedValue()).floatValue() == 0.0f) {
                FastScroller fastScroller = FastScroller.this;
                fastScroller.A = 0;
                fastScroller.y(0);
            } else {
                FastScroller fastScroller2 = FastScroller.this;
                fastScroller2.A = 2;
                fastScroller2.v();
            }
        }
    }

    /* compiled from: FastScroller.java */
    /* renamed from: androidx.recyclerview.widget.g$d */
    /* loaded from: classes.dex */
    private class d implements ValueAnimator.AnimatorUpdateListener {
        d() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int floatValue = (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 255.0f);
            FastScroller.this.f3734c.setAlpha(floatValue);
            FastScroller.this.f3735d.setAlpha(floatValue);
            FastScroller.this.v();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FastScroller(RecyclerView recyclerView, StateListDrawable stateListDrawable, Drawable drawable, StateListDrawable stateListDrawable2, Drawable drawable2, int i10, int i11, int i12) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f3757z = ofFloat;
        this.A = 0;
        this.B = new a();
        this.C = new b();
        this.f3734c = stateListDrawable;
        this.f3735d = drawable;
        this.f3738g = stateListDrawable2;
        this.f3739h = drawable2;
        this.f3736e = Math.max(i10, stateListDrawable.getIntrinsicWidth());
        this.f3737f = Math.max(i10, drawable.getIntrinsicWidth());
        this.f3740i = Math.max(i10, stateListDrawable2.getIntrinsicWidth());
        this.f3741j = Math.max(i10, drawable2.getIntrinsicWidth());
        this.f3732a = i11;
        this.f3733b = i12;
        stateListDrawable.setAlpha(255);
        drawable.setAlpha(255);
        ofFloat.addListener(new c());
        ofFloat.addUpdateListener(new d());
        j(recyclerView);
    }

    private void C(float f10) {
        int[] p10 = p();
        float max = Math.max(p10[0], Math.min(p10[1], f10));
        if (Math.abs(this.f3743l - max) < 2.0f) {
            return;
        }
        int x10 = x(this.f3744m, max, p10, this.f3750s.computeVerticalScrollRange(), this.f3750s.computeVerticalScrollOffset(), this.f3749r);
        if (x10 != 0) {
            this.f3750s.scrollBy(0, x10);
        }
        this.f3744m = max;
    }

    private void k() {
        this.f3750s.removeCallbacks(this.B);
    }

    private void l() {
        this.f3750s.removeItemDecoration(this);
        this.f3750s.removeOnItemTouchListener(this);
        this.f3750s.removeOnScrollListener(this.C);
        k();
    }

    private void m(Canvas canvas) {
        int i10 = this.f3749r;
        int i11 = this.f3740i;
        int i12 = this.f3746o;
        int i13 = this.f3745n;
        this.f3738g.setBounds(0, 0, i13, i11);
        this.f3739h.setBounds(0, 0, this.f3748q, this.f3741j);
        canvas.translate(0.0f, i10 - i11);
        this.f3739h.draw(canvas);
        canvas.translate(i12 - (i13 / 2), 0.0f);
        this.f3738g.draw(canvas);
        canvas.translate(-r2, -r0);
    }

    private void n(Canvas canvas) {
        int i10 = this.f3748q;
        int i11 = this.f3736e;
        int i12 = i10 - i11;
        int i13 = this.f3743l;
        int i14 = this.f3742k;
        int i15 = i13 - (i14 / 2);
        this.f3734c.setBounds(0, 0, i11, i14);
        this.f3735d.setBounds(0, 0, this.f3737f, this.f3749r);
        if (s()) {
            this.f3735d.draw(canvas);
            canvas.translate(this.f3736e, i15);
            canvas.scale(-1.0f, 1.0f);
            this.f3734c.draw(canvas);
            canvas.scale(-1.0f, 1.0f);
            canvas.translate(-this.f3736e, -i15);
            return;
        }
        canvas.translate(i12, 0.0f);
        this.f3735d.draw(canvas);
        canvas.translate(0.0f, i15);
        this.f3734c.draw(canvas);
        canvas.translate(-i12, -i15);
    }

    private int[] o() {
        int[] iArr = this.f3756y;
        int i10 = this.f3733b;
        iArr[0] = i10;
        iArr[1] = this.f3748q - i10;
        return iArr;
    }

    private int[] p() {
        int[] iArr = this.f3755x;
        int i10 = this.f3733b;
        iArr[0] = i10;
        iArr[1] = this.f3749r - i10;
        return iArr;
    }

    private void r(float f10) {
        int[] o10 = o();
        float max = Math.max(o10[0], Math.min(o10[1], f10));
        if (Math.abs(this.f3746o - max) < 2.0f) {
            return;
        }
        int x10 = x(this.f3747p, max, o10, this.f3750s.computeHorizontalScrollRange(), this.f3750s.computeHorizontalScrollOffset(), this.f3748q);
        if (x10 != 0) {
            this.f3750s.scrollBy(x10, 0);
        }
        this.f3747p = max;
    }

    private boolean s() {
        return ViewCompat.x(this.f3750s) == 1;
    }

    private void w(int i10) {
        k();
        this.f3750s.postDelayed(this.B, i10);
    }

    private int x(float f10, float f11, int[] iArr, int i10, int i11, int i12) {
        int i13 = iArr[1] - iArr[0];
        if (i13 == 0) {
            return 0;
        }
        int i14 = i10 - i12;
        int i15 = (int) (((f11 - f10) / i13) * i14);
        int i16 = i11 + i15;
        if (i16 >= i14 || i16 < 0) {
            return 0;
        }
        return i15;
    }

    private void z() {
        this.f3750s.addItemDecoration(this);
        this.f3750s.addOnItemTouchListener(this);
        this.f3750s.addOnScrollListener(this.C);
    }

    public void A() {
        int i10 = this.A;
        if (i10 != 0) {
            if (i10 != 3) {
                return;
            } else {
                this.f3757z.cancel();
            }
        }
        this.A = 1;
        ValueAnimator valueAnimator = this.f3757z;
        valueAnimator.setFloatValues(((Float) valueAnimator.getAnimatedValue()).floatValue(), 1.0f);
        this.f3757z.setDuration(500L);
        this.f3757z.setStartDelay(0L);
        this.f3757z.start();
    }

    void B(int i10, int i11) {
        int computeVerticalScrollRange = this.f3750s.computeVerticalScrollRange();
        int i12 = this.f3749r;
        this.f3751t = computeVerticalScrollRange - i12 > 0 && i12 >= this.f3732a;
        int computeHorizontalScrollRange = this.f3750s.computeHorizontalScrollRange();
        int i13 = this.f3748q;
        boolean z10 = computeHorizontalScrollRange - i13 > 0 && i13 >= this.f3732a;
        this.f3752u = z10;
        boolean z11 = this.f3751t;
        if (!z11 && !z10) {
            if (this.f3753v != 0) {
                y(0);
                return;
            }
            return;
        }
        if (z11) {
            float f10 = i12;
            this.f3743l = (int) ((f10 * (i11 + (f10 / 2.0f))) / computeVerticalScrollRange);
            this.f3742k = Math.min(i12, (i12 * i12) / computeVerticalScrollRange);
        }
        if (this.f3752u) {
            float f11 = i13;
            this.f3746o = (int) ((f11 * (i10 + (f11 / 2.0f))) / computeHorizontalScrollRange);
            this.f3745n = Math.min(i13, (i13 * i13) / computeHorizontalScrollRange);
        }
        int i14 = this.f3753v;
        if (i14 == 0 || i14 == 1) {
            y(1);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.s
    public void a(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (this.f3753v == 0) {
            return;
        }
        if (motionEvent.getAction() == 0) {
            boolean u7 = u(motionEvent.getX(), motionEvent.getY());
            boolean t7 = t(motionEvent.getX(), motionEvent.getY());
            if (u7 || t7) {
                if (t7) {
                    this.f3754w = 1;
                    this.f3747p = (int) motionEvent.getX();
                } else if (u7) {
                    this.f3754w = 2;
                    this.f3744m = (int) motionEvent.getY();
                }
                y(2);
                return;
            }
            return;
        }
        if (motionEvent.getAction() == 1 && this.f3753v == 2) {
            this.f3744m = 0.0f;
            this.f3747p = 0.0f;
            y(1);
            this.f3754w = 0;
            return;
        }
        if (motionEvent.getAction() == 2 && this.f3753v == 2) {
            A();
            if (this.f3754w == 1) {
                r(motionEvent.getX());
            }
            if (this.f3754w == 2) {
                C(motionEvent.getY());
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.s
    public boolean b(RecyclerView recyclerView, MotionEvent motionEvent) {
        int i10 = this.f3753v;
        if (i10 == 1) {
            boolean u7 = u(motionEvent.getX(), motionEvent.getY());
            boolean t7 = t(motionEvent.getX(), motionEvent.getY());
            if (motionEvent.getAction() != 0) {
                return false;
            }
            if (!u7 && !t7) {
                return false;
            }
            if (t7) {
                this.f3754w = 1;
                this.f3747p = (int) motionEvent.getX();
            } else if (u7) {
                this.f3754w = 2;
                this.f3744m = (int) motionEvent.getY();
            }
            y(2);
        } else if (i10 != 2) {
            return false;
        }
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.s
    public void c(boolean z10) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void i(Canvas canvas, RecyclerView recyclerView, RecyclerView.z zVar) {
        if (this.f3748q == this.f3750s.getWidth() && this.f3749r == this.f3750s.getHeight()) {
            if (this.A != 0) {
                if (this.f3751t) {
                    n(canvas);
                }
                if (this.f3752u) {
                    m(canvas);
                    return;
                }
                return;
            }
            return;
        }
        this.f3748q = this.f3750s.getWidth();
        this.f3749r = this.f3750s.getHeight();
        y(0);
    }

    public void j(RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.f3750s;
        if (recyclerView2 == recyclerView) {
            return;
        }
        if (recyclerView2 != null) {
            l();
        }
        this.f3750s = recyclerView;
        if (recyclerView != null) {
            z();
        }
    }

    void q(int i10) {
        int i11 = this.A;
        if (i11 == 1) {
            this.f3757z.cancel();
        } else if (i11 != 2) {
            return;
        }
        this.A = 3;
        ValueAnimator valueAnimator = this.f3757z;
        valueAnimator.setFloatValues(((Float) valueAnimator.getAnimatedValue()).floatValue(), 0.0f);
        this.f3757z.setDuration(i10);
        this.f3757z.start();
    }

    boolean t(float f10, float f11) {
        if (f11 >= this.f3749r - this.f3740i) {
            int i10 = this.f3746o;
            int i11 = this.f3745n;
            if (f10 >= i10 - (i11 / 2) && f10 <= i10 + (i11 / 2)) {
                return true;
            }
        }
        return false;
    }

    boolean u(float f10, float f11) {
        if (!s() ? f10 >= this.f3748q - this.f3736e : f10 <= this.f3736e) {
            int i10 = this.f3743l;
            int i11 = this.f3742k;
            if (f11 >= i10 - (i11 / 2) && f11 <= i10 + (i11 / 2)) {
                return true;
            }
        }
        return false;
    }

    void v() {
        this.f3750s.invalidate();
    }

    void y(int i10) {
        if (i10 == 2 && this.f3753v != 2) {
            this.f3734c.setState(D);
            k();
        }
        if (i10 == 0) {
            v();
        } else {
            A();
        }
        if (this.f3753v == 2 && i10 != 2) {
            this.f3734c.setState(E);
            w(1200);
        } else if (i10 == 1) {
            w(1500);
        }
        this.f3753v = i10;
    }
}
