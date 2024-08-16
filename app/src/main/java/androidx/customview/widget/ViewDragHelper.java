package androidx.customview.widget;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import androidx.core.view.ViewCompat;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import java.util.Arrays;

/* compiled from: ViewDragHelper.java */
/* renamed from: androidx.customview.widget.c, reason: use source file name */
/* loaded from: classes.dex */
public class ViewDragHelper {

    /* renamed from: x, reason: collision with root package name */
    private static final Interpolator f2505x = new a();

    /* renamed from: a, reason: collision with root package name */
    private int f2506a;

    /* renamed from: b, reason: collision with root package name */
    private int f2507b;

    /* renamed from: d, reason: collision with root package name */
    private float[] f2509d;

    /* renamed from: e, reason: collision with root package name */
    private float[] f2510e;

    /* renamed from: f, reason: collision with root package name */
    private float[] f2511f;

    /* renamed from: g, reason: collision with root package name */
    private float[] f2512g;

    /* renamed from: h, reason: collision with root package name */
    private int[] f2513h;

    /* renamed from: i, reason: collision with root package name */
    private int[] f2514i;

    /* renamed from: j, reason: collision with root package name */
    private int[] f2515j;

    /* renamed from: k, reason: collision with root package name */
    private int f2516k;

    /* renamed from: l, reason: collision with root package name */
    private VelocityTracker f2517l;

    /* renamed from: m, reason: collision with root package name */
    private float f2518m;

    /* renamed from: n, reason: collision with root package name */
    private float f2519n;

    /* renamed from: o, reason: collision with root package name */
    private int f2520o;

    /* renamed from: p, reason: collision with root package name */
    private final int f2521p;

    /* renamed from: q, reason: collision with root package name */
    private int f2522q;

    /* renamed from: r, reason: collision with root package name */
    private OverScroller f2523r;

    /* renamed from: s, reason: collision with root package name */
    private final c f2524s;

    /* renamed from: t, reason: collision with root package name */
    private View f2525t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f2526u;

    /* renamed from: v, reason: collision with root package name */
    private final ViewGroup f2527v;

    /* renamed from: c, reason: collision with root package name */
    private int f2508c = -1;

    /* renamed from: w, reason: collision with root package name */
    private final Runnable f2528w = new b();

    /* compiled from: ViewDragHelper.java */
    /* renamed from: androidx.customview.widget.c$a */
    /* loaded from: classes.dex */
    class a implements Interpolator {
        a() {
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f10) {
            float f11 = f10 - 1.0f;
            return (f11 * f11 * f11 * f11 * f11) + 1.0f;
        }
    }

    /* compiled from: ViewDragHelper.java */
    /* renamed from: androidx.customview.widget.c$b */
    /* loaded from: classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewDragHelper.this.L(0);
        }
    }

    /* compiled from: ViewDragHelper.java */
    /* renamed from: androidx.customview.widget.c$c */
    /* loaded from: classes.dex */
    public static abstract class c {
        public abstract int a(View view, int i10, int i11);

        public abstract int b(View view, int i10, int i11);

        public int c(int i10) {
            return i10;
        }

        public int d(View view) {
            return 0;
        }

        public int e(View view) {
            return 0;
        }

        public void f(int i10, int i11) {
        }

        public boolean g(int i10) {
            return false;
        }

        public void h(int i10, int i11) {
        }

        public void i(View view, int i10) {
        }

        public abstract void j(int i10);

        public abstract void k(View view, int i10, int i11, int i12, int i13);

        public abstract void l(View view, float f10, float f11);

        public abstract boolean m(View view, int i10);
    }

    private ViewDragHelper(Context context, ViewGroup viewGroup, c cVar) {
        if (viewGroup == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        }
        if (cVar != null) {
            this.f2527v = viewGroup;
            this.f2524s = cVar;
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            int i10 = (int) ((context.getResources().getDisplayMetrics().density * 20.0f) + 0.5f);
            this.f2521p = i10;
            this.f2520o = i10;
            this.f2507b = viewConfiguration.getScaledTouchSlop();
            this.f2518m = viewConfiguration.getScaledMaximumFlingVelocity();
            this.f2519n = viewConfiguration.getScaledMinimumFlingVelocity();
            this.f2523r = new OverScroller(context, f2505x);
            return;
        }
        throw new IllegalArgumentException("Callback may not be null");
    }

    private boolean E(int i10) {
        if (D(i10)) {
            return true;
        }
        Log.e("ViewDragHelper", "Ignoring pointerId=" + i10 + " because ACTION_DOWN was not received for this pointer before ACTION_MOVE. It likely happened because  ViewDragHelper did not receive all the events in the event stream.");
        return false;
    }

    private void H() {
        this.f2517l.computeCurrentVelocity(1000, this.f2518m);
        q(h(this.f2517l.getXVelocity(this.f2508c), this.f2519n, this.f2518m), h(this.f2517l.getYVelocity(this.f2508c), this.f2519n, this.f2518m));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v15 */
    /* JADX WARN: Type inference failed for: r0v16 */
    /* JADX WARN: Type inference failed for: r0v4, types: [int] */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v7 */
    /* JADX WARN: Type inference failed for: r2v1, types: [androidx.customview.widget.c$c] */
    private void I(float f10, float f11, int i10) {
        boolean d10 = d(f10, f11, i10, 1);
        boolean z10 = d10;
        if (d(f11, f10, i10, 4)) {
            z10 = (d10 ? 1 : 0) | 4;
        }
        boolean z11 = z10;
        if (d(f10, f11, i10, 2)) {
            z11 = (z10 ? 1 : 0) | 2;
        }
        ?? r02 = z11;
        if (d(f11, f10, i10, 8)) {
            r02 = (z11 ? 1 : 0) | 8;
        }
        if (r02 != 0) {
            int[] iArr = this.f2514i;
            iArr[i10] = iArr[i10] | r02;
            this.f2524s.f(r02, i10);
        }
    }

    private void J(float f10, float f11, int i10) {
        t(i10);
        float[] fArr = this.f2509d;
        this.f2511f[i10] = f10;
        fArr[i10] = f10;
        float[] fArr2 = this.f2510e;
        this.f2512g[i10] = f11;
        fArr2[i10] = f11;
        this.f2513h[i10] = z((int) f10, (int) f11);
        this.f2516k |= 1 << i10;
    }

    private void K(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        for (int i10 = 0; i10 < pointerCount; i10++) {
            int pointerId = motionEvent.getPointerId(i10);
            if (E(pointerId)) {
                float x10 = motionEvent.getX(i10);
                float y4 = motionEvent.getY(i10);
                this.f2511f[pointerId] = x10;
                this.f2512g[pointerId] = y4;
            }
        }
    }

    private boolean d(float f10, float f11, int i10, int i11) {
        float abs = Math.abs(f10);
        float abs2 = Math.abs(f11);
        if ((this.f2513h[i10] & i11) != i11 || (this.f2522q & i11) == 0 || (this.f2515j[i10] & i11) == i11 || (this.f2514i[i10] & i11) == i11) {
            return false;
        }
        int i12 = this.f2507b;
        if (abs <= i12 && abs2 <= i12) {
            return false;
        }
        if (abs >= abs2 * 0.5f || !this.f2524s.g(i11)) {
            return (this.f2514i[i10] & i11) == 0 && abs > ((float) this.f2507b);
        }
        int[] iArr = this.f2515j;
        iArr[i10] = iArr[i10] | i11;
        return false;
    }

    private boolean g(View view, float f10, float f11) {
        if (view == null) {
            return false;
        }
        boolean z10 = this.f2524s.d(view) > 0;
        boolean z11 = this.f2524s.e(view) > 0;
        if (!z10 || !z11) {
            return z10 ? Math.abs(f10) > ((float) this.f2507b) : z11 && Math.abs(f11) > ((float) this.f2507b);
        }
        float f12 = (f10 * f10) + (f11 * f11);
        int i10 = this.f2507b;
        return f12 > ((float) (i10 * i10));
    }

    private float h(float f10, float f11, float f12) {
        float abs = Math.abs(f10);
        if (abs < f11) {
            return 0.0f;
        }
        return abs > f12 ? f10 > 0.0f ? f12 : -f12 : f10;
    }

    private int i(int i10, int i11, int i12) {
        int abs = Math.abs(i10);
        if (abs < i11) {
            return 0;
        }
        return abs > i12 ? i10 > 0 ? i12 : -i12 : i10;
    }

    private void j() {
        float[] fArr = this.f2509d;
        if (fArr == null) {
            return;
        }
        Arrays.fill(fArr, 0.0f);
        Arrays.fill(this.f2510e, 0.0f);
        Arrays.fill(this.f2511f, 0.0f);
        Arrays.fill(this.f2512g, 0.0f);
        Arrays.fill(this.f2513h, 0);
        Arrays.fill(this.f2514i, 0);
        Arrays.fill(this.f2515j, 0);
        this.f2516k = 0;
    }

    private void k(int i10) {
        if (this.f2509d == null || !D(i10)) {
            return;
        }
        this.f2509d[i10] = 0.0f;
        this.f2510e[i10] = 0.0f;
        this.f2511f[i10] = 0.0f;
        this.f2512g[i10] = 0.0f;
        this.f2513h[i10] = 0;
        this.f2514i[i10] = 0;
        this.f2515j[i10] = 0;
        this.f2516k = (~(1 << i10)) & this.f2516k;
    }

    private int l(int i10, int i11, int i12) {
        int abs;
        if (i10 == 0) {
            return 0;
        }
        int width = this.f2527v.getWidth();
        float f10 = width / 2;
        float r10 = f10 + (r(Math.min(1.0f, Math.abs(i10) / width)) * f10);
        int abs2 = Math.abs(i11);
        if (abs2 > 0) {
            abs = Math.round(Math.abs(r10 / abs2) * 1000.0f) * 4;
        } else {
            abs = (int) (((Math.abs(i10) / i12) + 1.0f) * 256.0f);
        }
        return Math.min(abs, DataLinkConstants.THUMBNAIL_HEALTH);
    }

    private int m(View view, int i10, int i11, int i12, int i13) {
        float f10;
        float f11;
        float f12;
        float f13;
        int i14 = i(i12, (int) this.f2519n, (int) this.f2518m);
        int i15 = i(i13, (int) this.f2519n, (int) this.f2518m);
        int abs = Math.abs(i10);
        int abs2 = Math.abs(i11);
        int abs3 = Math.abs(i14);
        int abs4 = Math.abs(i15);
        int i16 = abs3 + abs4;
        int i17 = abs + abs2;
        if (i14 != 0) {
            f10 = abs3;
            f11 = i16;
        } else {
            f10 = abs;
            f11 = i17;
        }
        float f14 = f10 / f11;
        if (i15 != 0) {
            f12 = abs4;
            f13 = i16;
        } else {
            f12 = abs2;
            f13 = i17;
        }
        return (int) ((l(i10, i14, this.f2524s.d(view)) * f14) + (l(i11, i15, this.f2524s.e(view)) * (f12 / f13)));
    }

    public static ViewDragHelper o(ViewGroup viewGroup, float f10, c cVar) {
        ViewDragHelper p10 = p(viewGroup, cVar);
        p10.f2507b = (int) (p10.f2507b * (1.0f / f10));
        return p10;
    }

    public static ViewDragHelper p(ViewGroup viewGroup, c cVar) {
        return new ViewDragHelper(viewGroup.getContext(), viewGroup, cVar);
    }

    private void q(float f10, float f11) {
        this.f2526u = true;
        this.f2524s.l(this.f2525t, f10, f11);
        this.f2526u = false;
        if (this.f2506a == 1) {
            L(0);
        }
    }

    private float r(float f10) {
        return (float) Math.sin((f10 - 0.5f) * 0.47123894f);
    }

    private void s(int i10, int i11, int i12, int i13) {
        int left = this.f2525t.getLeft();
        int top = this.f2525t.getTop();
        if (i12 != 0) {
            i10 = this.f2524s.a(this.f2525t, i10, i12);
            ViewCompat.V(this.f2525t, i10 - left);
        }
        int i14 = i10;
        if (i13 != 0) {
            i11 = this.f2524s.b(this.f2525t, i11, i13);
            ViewCompat.W(this.f2525t, i11 - top);
        }
        int i15 = i11;
        if (i12 == 0 && i13 == 0) {
            return;
        }
        this.f2524s.k(this.f2525t, i14, i15, i14 - left, i15 - top);
    }

    private void t(int i10) {
        float[] fArr = this.f2509d;
        if (fArr == null || fArr.length <= i10) {
            int i11 = i10 + 1;
            float[] fArr2 = new float[i11];
            float[] fArr3 = new float[i11];
            float[] fArr4 = new float[i11];
            float[] fArr5 = new float[i11];
            int[] iArr = new int[i11];
            int[] iArr2 = new int[i11];
            int[] iArr3 = new int[i11];
            if (fArr != null) {
                System.arraycopy(fArr, 0, fArr2, 0, fArr.length);
                float[] fArr6 = this.f2510e;
                System.arraycopy(fArr6, 0, fArr3, 0, fArr6.length);
                float[] fArr7 = this.f2511f;
                System.arraycopy(fArr7, 0, fArr4, 0, fArr7.length);
                float[] fArr8 = this.f2512g;
                System.arraycopy(fArr8, 0, fArr5, 0, fArr8.length);
                int[] iArr4 = this.f2513h;
                System.arraycopy(iArr4, 0, iArr, 0, iArr4.length);
                int[] iArr5 = this.f2514i;
                System.arraycopy(iArr5, 0, iArr2, 0, iArr5.length);
                int[] iArr6 = this.f2515j;
                System.arraycopy(iArr6, 0, iArr3, 0, iArr6.length);
            }
            this.f2509d = fArr2;
            this.f2510e = fArr3;
            this.f2511f = fArr4;
            this.f2512g = fArr5;
            this.f2513h = iArr;
            this.f2514i = iArr2;
            this.f2515j = iArr3;
        }
    }

    private boolean v(int i10, int i11, int i12, int i13) {
        int left = this.f2525t.getLeft();
        int top = this.f2525t.getTop();
        int i14 = i10 - left;
        int i15 = i11 - top;
        if (i14 == 0 && i15 == 0) {
            this.f2523r.abortAnimation();
            L(0);
            return false;
        }
        this.f2523r.startScroll(left, top, i14, i15, m(this.f2525t, i14, i15, i12, i13));
        L(2);
        return true;
    }

    private int z(int i10, int i11) {
        int i12 = i10 < this.f2527v.getLeft() + this.f2520o ? 1 : 0;
        if (i11 < this.f2527v.getTop() + this.f2520o) {
            i12 |= 4;
        }
        if (i10 > this.f2527v.getRight() - this.f2520o) {
            i12 |= 2;
        }
        return i11 > this.f2527v.getBottom() - this.f2520o ? i12 | 8 : i12;
    }

    public int A() {
        return this.f2507b;
    }

    public int B() {
        return this.f2506a;
    }

    public boolean C(int i10, int i11) {
        return F(this.f2525t, i10, i11);
    }

    public boolean D(int i10) {
        return (this.f2516k & (1 << i10)) != 0;
    }

    public boolean F(View view, int i10, int i11) {
        return view != null && i10 >= view.getLeft() && i10 < view.getRight() && i11 >= view.getTop() && i11 < view.getBottom();
    }

    public void G(MotionEvent motionEvent) {
        int i10;
        int actionMasked = motionEvent.getActionMasked();
        int actionIndex = motionEvent.getActionIndex();
        if (actionMasked == 0) {
            b();
        }
        if (this.f2517l == null) {
            this.f2517l = VelocityTracker.obtain();
        }
        this.f2517l.addMovement(motionEvent);
        int i11 = 0;
        if (actionMasked == 0) {
            float x10 = motionEvent.getX();
            float y4 = motionEvent.getY();
            int pointerId = motionEvent.getPointerId(0);
            View u7 = u((int) x10, (int) y4);
            J(x10, y4, pointerId);
            S(u7, pointerId);
            int i12 = this.f2513h[pointerId];
            int i13 = this.f2522q;
            if ((i12 & i13) != 0) {
                this.f2524s.h(i12 & i13, pointerId);
                return;
            }
            return;
        }
        if (actionMasked == 1) {
            if (this.f2506a == 1) {
                H();
            }
            b();
            return;
        }
        if (actionMasked == 2) {
            if (this.f2506a == 1) {
                if (E(this.f2508c)) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.f2508c);
                    float x11 = motionEvent.getX(findPointerIndex);
                    float y10 = motionEvent.getY(findPointerIndex);
                    float[] fArr = this.f2511f;
                    int i14 = this.f2508c;
                    int i15 = (int) (x11 - fArr[i14]);
                    int i16 = (int) (y10 - this.f2512g[i14]);
                    s(this.f2525t.getLeft() + i15, this.f2525t.getTop() + i16, i15, i16);
                    K(motionEvent);
                    return;
                }
                return;
            }
            int pointerCount = motionEvent.getPointerCount();
            while (i11 < pointerCount) {
                int pointerId2 = motionEvent.getPointerId(i11);
                if (E(pointerId2)) {
                    float x12 = motionEvent.getX(i11);
                    float y11 = motionEvent.getY(i11);
                    float f10 = x12 - this.f2509d[pointerId2];
                    float f11 = y11 - this.f2510e[pointerId2];
                    I(f10, f11, pointerId2);
                    if (this.f2506a != 1) {
                        View u10 = u((int) x12, (int) y11);
                        if (g(u10, f10, f11) && S(u10, pointerId2)) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                i11++;
            }
            K(motionEvent);
            return;
        }
        if (actionMasked == 3) {
            if (this.f2506a == 1) {
                q(0.0f, 0.0f);
            }
            b();
            return;
        }
        if (actionMasked == 5) {
            int pointerId3 = motionEvent.getPointerId(actionIndex);
            float x13 = motionEvent.getX(actionIndex);
            float y12 = motionEvent.getY(actionIndex);
            J(x13, y12, pointerId3);
            if (this.f2506a == 0) {
                S(u((int) x13, (int) y12), pointerId3);
                int i17 = this.f2513h[pointerId3];
                int i18 = this.f2522q;
                if ((i17 & i18) != 0) {
                    this.f2524s.h(i17 & i18, pointerId3);
                    return;
                }
                return;
            }
            if (C((int) x13, (int) y12)) {
                S(this.f2525t, pointerId3);
                return;
            }
            return;
        }
        if (actionMasked != 6) {
            return;
        }
        int pointerId4 = motionEvent.getPointerId(actionIndex);
        if (this.f2506a == 1 && pointerId4 == this.f2508c) {
            int pointerCount2 = motionEvent.getPointerCount();
            while (true) {
                if (i11 >= pointerCount2) {
                    i10 = -1;
                    break;
                }
                int pointerId5 = motionEvent.getPointerId(i11);
                if (pointerId5 != this.f2508c) {
                    View u11 = u((int) motionEvent.getX(i11), (int) motionEvent.getY(i11));
                    View view = this.f2525t;
                    if (u11 == view && S(view, pointerId5)) {
                        i10 = this.f2508c;
                        break;
                    }
                }
                i11++;
            }
            if (i10 == -1) {
                H();
            }
        }
        k(pointerId4);
    }

    void L(int i10) {
        this.f2527v.removeCallbacks(this.f2528w);
        if (this.f2506a != i10) {
            this.f2506a = i10;
            this.f2524s.j(i10);
            if (this.f2506a == 0) {
                this.f2525t = null;
            }
        }
    }

    public void M(int i10) {
        this.f2520o = i10;
    }

    public void N(int i10) {
        this.f2522q = i10;
    }

    public void O(float f10) {
        this.f2519n = f10;
    }

    public boolean P(int i10, int i11) {
        if (this.f2526u) {
            return v(i10, i11, (int) this.f2517l.getXVelocity(this.f2508c), (int) this.f2517l.getYVelocity(this.f2508c));
        }
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x00dd, code lost:
    
        if (r12 != r11) goto L54;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean Q(MotionEvent motionEvent) {
        boolean z10;
        View u7;
        int actionMasked = motionEvent.getActionMasked();
        int actionIndex = motionEvent.getActionIndex();
        if (actionMasked == 0) {
            b();
        }
        if (this.f2517l == null) {
            this.f2517l = VelocityTracker.obtain();
        }
        this.f2517l.addMovement(motionEvent);
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked == 5) {
                            int pointerId = motionEvent.getPointerId(actionIndex);
                            float x10 = motionEvent.getX(actionIndex);
                            float y4 = motionEvent.getY(actionIndex);
                            J(x10, y4, pointerId);
                            int i10 = this.f2506a;
                            if (i10 == 0) {
                                int i11 = this.f2513h[pointerId];
                                int i12 = this.f2522q;
                                if ((i11 & i12) != 0) {
                                    this.f2524s.h(i11 & i12, pointerId);
                                }
                            } else if (i10 == 2 && (u7 = u((int) x10, (int) y4)) == this.f2525t) {
                                S(u7, pointerId);
                            }
                        } else if (actionMasked == 6) {
                            k(motionEvent.getPointerId(actionIndex));
                        }
                    }
                } else if (this.f2509d != null && this.f2510e != null) {
                    int pointerCount = motionEvent.getPointerCount();
                    for (int i13 = 0; i13 < pointerCount; i13++) {
                        int pointerId2 = motionEvent.getPointerId(i13);
                        if (E(pointerId2)) {
                            float x11 = motionEvent.getX(i13);
                            float y10 = motionEvent.getY(i13);
                            float f10 = x11 - this.f2509d[pointerId2];
                            float f11 = y10 - this.f2510e[pointerId2];
                            View u10 = u((int) x11, (int) y10);
                            boolean z11 = u10 != null && g(u10, f10, f11);
                            if (z11) {
                                int left = u10.getLeft();
                                int i14 = (int) f10;
                                int a10 = this.f2524s.a(u10, left + i14, i14);
                                int top = u10.getTop();
                                int i15 = (int) f11;
                                int b10 = this.f2524s.b(u10, top + i15, i15);
                                int d10 = this.f2524s.d(u10);
                                int e10 = this.f2524s.e(u10);
                                if (d10 != 0) {
                                    if (d10 > 0) {
                                    }
                                }
                                if (e10 == 0) {
                                    break;
                                }
                                if (e10 > 0 && b10 == top) {
                                    break;
                                }
                            }
                            I(f10, f11, pointerId2);
                            if (this.f2506a == 1) {
                                break;
                            }
                            if (z11 && S(u10, pointerId2)) {
                                break;
                            }
                        }
                    }
                    K(motionEvent);
                }
                z10 = false;
            }
            b();
            z10 = false;
        } else {
            float x12 = motionEvent.getX();
            float y11 = motionEvent.getY();
            z10 = false;
            int pointerId3 = motionEvent.getPointerId(0);
            J(x12, y11, pointerId3);
            View u11 = u((int) x12, (int) y11);
            if (u11 == this.f2525t && this.f2506a == 2) {
                S(u11, pointerId3);
            }
            int i16 = this.f2513h[pointerId3];
            int i17 = this.f2522q;
            if ((i16 & i17) != 0) {
                this.f2524s.h(i16 & i17, pointerId3);
            }
        }
        if (this.f2506a == 1) {
            return true;
        }
        return z10;
    }

    public boolean R(View view, int i10, int i11) {
        this.f2525t = view;
        this.f2508c = -1;
        boolean v7 = v(i10, i11, 0, 0);
        if (!v7 && this.f2506a == 0 && this.f2525t != null) {
            this.f2525t = null;
        }
        return v7;
    }

    boolean S(View view, int i10) {
        if (view == this.f2525t && this.f2508c == i10) {
            return true;
        }
        if (view == null || !this.f2524s.m(view, i10)) {
            return false;
        }
        this.f2508c = i10;
        c(view, i10);
        return true;
    }

    public void a() {
        b();
        if (this.f2506a == 2) {
            int currX = this.f2523r.getCurrX();
            int currY = this.f2523r.getCurrY();
            this.f2523r.abortAnimation();
            int currX2 = this.f2523r.getCurrX();
            int currY2 = this.f2523r.getCurrY();
            this.f2524s.k(this.f2525t, currX2, currY2, currX2 - currX, currY2 - currY);
        }
        L(0);
    }

    public void b() {
        this.f2508c = -1;
        j();
        VelocityTracker velocityTracker = this.f2517l;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.f2517l = null;
        }
    }

    public void c(View view, int i10) {
        if (view.getParent() == this.f2527v) {
            this.f2525t = view;
            this.f2508c = i10;
            this.f2524s.i(view, i10);
            L(1);
            return;
        }
        throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (" + this.f2527v + ")");
    }

    public boolean e(int i10) {
        int length = this.f2509d.length;
        for (int i11 = 0; i11 < length; i11++) {
            if (f(i10, i11)) {
                return true;
            }
        }
        return false;
    }

    public boolean f(int i10, int i11) {
        if (!D(i11)) {
            return false;
        }
        boolean z10 = (i10 & 1) == 1;
        boolean z11 = (i10 & 2) == 2;
        float f10 = this.f2511f[i11] - this.f2509d[i11];
        float f11 = this.f2512g[i11] - this.f2510e[i11];
        if (!z10 || !z11) {
            return z10 ? Math.abs(f10) > ((float) this.f2507b) : z11 && Math.abs(f11) > ((float) this.f2507b);
        }
        float f12 = (f10 * f10) + (f11 * f11);
        int i12 = this.f2507b;
        return f12 > ((float) (i12 * i12));
    }

    public boolean n(boolean z10) {
        if (this.f2506a == 2) {
            boolean computeScrollOffset = this.f2523r.computeScrollOffset();
            int currX = this.f2523r.getCurrX();
            int currY = this.f2523r.getCurrY();
            int left = currX - this.f2525t.getLeft();
            int top = currY - this.f2525t.getTop();
            if (left != 0) {
                ViewCompat.V(this.f2525t, left);
            }
            if (top != 0) {
                ViewCompat.W(this.f2525t, top);
            }
            if (left != 0 || top != 0) {
                this.f2524s.k(this.f2525t, currX, currY, left, top);
            }
            if (computeScrollOffset && currX == this.f2523r.getFinalX() && currY == this.f2523r.getFinalY()) {
                this.f2523r.abortAnimation();
                computeScrollOffset = false;
            }
            if (!computeScrollOffset) {
                if (z10) {
                    this.f2527v.post(this.f2528w);
                } else {
                    L(0);
                }
            }
        }
        return this.f2506a == 2;
    }

    public View u(int i10, int i11) {
        for (int childCount = this.f2527v.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = this.f2527v.getChildAt(this.f2524s.c(childCount));
            if (i10 >= childAt.getLeft() && i10 < childAt.getRight() && i11 >= childAt.getTop() && i11 < childAt.getBottom()) {
                return childAt;
            }
        }
        return null;
    }

    public View w() {
        return this.f2525t;
    }

    public int x() {
        return this.f2521p;
    }

    public int y() {
        return this.f2520o;
    }
}
