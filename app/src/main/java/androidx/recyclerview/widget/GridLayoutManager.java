package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;

/* loaded from: classes.dex */
public class GridLayoutManager extends LinearLayoutManager {
    boolean I;
    int J;
    int[] K;
    View[] L;
    final SparseIntArray M;
    final SparseIntArray N;
    b O;
    final Rect P;
    private boolean Q;

    /* loaded from: classes.dex */
    public static final class a extends b {
        @Override // androidx.recyclerview.widget.GridLayoutManager.b
        public int e(int i10, int i11) {
            return i10 % i11;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.b
        public int f(int i10) {
            return 1;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class b {

        /* renamed from: a, reason: collision with root package name */
        final SparseIntArray f3416a = new SparseIntArray();

        /* renamed from: b, reason: collision with root package name */
        final SparseIntArray f3417b = new SparseIntArray();

        /* renamed from: c, reason: collision with root package name */
        private boolean f3418c = false;

        /* renamed from: d, reason: collision with root package name */
        private boolean f3419d = false;

        static int a(SparseIntArray sparseIntArray, int i10) {
            int size = sparseIntArray.size() - 1;
            int i11 = 0;
            while (i11 <= size) {
                int i12 = (i11 + size) >>> 1;
                if (sparseIntArray.keyAt(i12) < i10) {
                    i11 = i12 + 1;
                } else {
                    size = i12 - 1;
                }
            }
            int i13 = i11 - 1;
            if (i13 < 0 || i13 >= sparseIntArray.size()) {
                return -1;
            }
            return sparseIntArray.keyAt(i13);
        }

        int b(int i10, int i11) {
            if (!this.f3419d) {
                return d(i10, i11);
            }
            int i12 = this.f3417b.get(i10, -1);
            if (i12 != -1) {
                return i12;
            }
            int d10 = d(i10, i11);
            this.f3417b.put(i10, d10);
            return d10;
        }

        int c(int i10, int i11) {
            if (!this.f3418c) {
                return e(i10, i11);
            }
            int i12 = this.f3416a.get(i10, -1);
            if (i12 != -1) {
                return i12;
            }
            int e10 = e(i10, i11);
            this.f3416a.put(i10, e10);
            return e10;
        }

        public int d(int i10, int i11) {
            int i12;
            int i13;
            int i14;
            int a10;
            if (!this.f3419d || (a10 = a(this.f3417b, i10)) == -1) {
                i12 = 0;
                i13 = 0;
                i14 = 0;
            } else {
                i12 = this.f3417b.get(a10);
                i13 = a10 + 1;
                i14 = c(a10, i11) + f(a10);
                if (i14 == i11) {
                    i12++;
                    i14 = 0;
                }
            }
            int f10 = f(i10);
            while (i13 < i10) {
                int f11 = f(i13);
                i14 += f11;
                if (i14 == i11) {
                    i12++;
                    i14 = 0;
                } else if (i14 > i11) {
                    i12++;
                    i14 = f11;
                }
                i13++;
            }
            return i14 + f10 > i11 ? i12 + 1 : i12;
        }

        public abstract int e(int i10, int i11);

        public abstract int f(int i10);

        public void g() {
            this.f3417b.clear();
        }

        public void h() {
            this.f3416a.clear();
        }
    }

    public GridLayoutManager(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.I = false;
        this.J = -1;
        this.M = new SparseIntArray();
        this.N = new SparseIntArray();
        this.O = new a();
        this.P = new Rect();
        e3(RecyclerView.p.k0(context, attributeSet, i10, i11).f3507b);
    }

    private void N2(RecyclerView.v vVar, RecyclerView.z zVar, int i10, boolean z10) {
        int i11;
        int i12;
        int i13 = 0;
        int i14 = -1;
        if (z10) {
            i12 = 1;
            i14 = i10;
            i11 = 0;
        } else {
            i11 = i10 - 1;
            i12 = -1;
        }
        while (i11 != i14) {
            View view = this.L[i11];
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int a32 = a3(vVar, zVar, j0(view));
            layoutParams.f3415f = a32;
            layoutParams.f3414e = i13;
            i13 += a32;
            i11 += i12;
        }
    }

    private void O2() {
        int J = J();
        for (int i10 = 0; i10 < J; i10++) {
            LayoutParams layoutParams = (LayoutParams) I(i10).getLayoutParams();
            int a10 = layoutParams.a();
            this.M.put(a10, layoutParams.f());
            this.N.put(a10, layoutParams.e());
        }
    }

    private void P2(int i10) {
        this.K = Q2(this.K, this.J, i10);
    }

    static int[] Q2(int[] iArr, int i10, int i11) {
        int i12;
        if (iArr == null || iArr.length != i10 + 1 || iArr[iArr.length - 1] != i11) {
            iArr = new int[i10 + 1];
        }
        int i13 = 0;
        iArr[0] = 0;
        int i14 = i11 / i10;
        int i15 = i11 % i10;
        int i16 = 0;
        for (int i17 = 1; i17 <= i10; i17++) {
            i13 += i15;
            if (i13 <= 0 || i10 - i13 >= i15) {
                i12 = i14;
            } else {
                i12 = i14 + 1;
                i13 -= i10;
            }
            i16 += i12;
            iArr[i17] = i16;
        }
        return iArr;
    }

    private void R2() {
        this.M.clear();
        this.N.clear();
    }

    private int S2(RecyclerView.z zVar) {
        int max;
        if (J() != 0 && zVar.b() != 0) {
            V1();
            boolean r22 = r2();
            View a22 = a2(!r22, true);
            View Z1 = Z1(!r22, true);
            if (a22 != null && Z1 != null) {
                int b10 = this.O.b(j0(a22), this.J);
                int b11 = this.O.b(j0(Z1), this.J);
                int min = Math.min(b10, b11);
                int max2 = Math.max(b10, b11);
                int b12 = this.O.b(zVar.b() - 1, this.J) + 1;
                if (this.f3425x) {
                    max = Math.max(0, (b12 - max2) - 1);
                } else {
                    max = Math.max(0, min);
                }
                if (!r22) {
                    return max;
                }
                return Math.round((max * (Math.abs(this.f3422u.d(Z1) - this.f3422u.g(a22)) / ((this.O.b(j0(Z1), this.J) - this.O.b(j0(a22), this.J)) + 1))) + (this.f3422u.n() - this.f3422u.g(a22)));
            }
        }
        return 0;
    }

    private int T2(RecyclerView.z zVar) {
        if (J() != 0 && zVar.b() != 0) {
            V1();
            View a22 = a2(!r2(), true);
            View Z1 = Z1(!r2(), true);
            if (a22 != null && Z1 != null) {
                if (!r2()) {
                    return this.O.b(zVar.b() - 1, this.J) + 1;
                }
                int d10 = this.f3422u.d(Z1) - this.f3422u.g(a22);
                int b10 = this.O.b(j0(a22), this.J);
                return (int) ((d10 / ((this.O.b(j0(Z1), this.J) - b10) + 1)) * (this.O.b(zVar.b() - 1, this.J) + 1));
            }
        }
        return 0;
    }

    private void U2(RecyclerView.v vVar, RecyclerView.z zVar, LinearLayoutManager.a aVar, int i10) {
        boolean z10 = i10 == 1;
        int Z2 = Z2(vVar, zVar, aVar.f3432b);
        if (z10) {
            while (Z2 > 0) {
                int i11 = aVar.f3432b;
                if (i11 <= 0) {
                    return;
                }
                int i12 = i11 - 1;
                aVar.f3432b = i12;
                Z2 = Z2(vVar, zVar, i12);
            }
            return;
        }
        int b10 = zVar.b() - 1;
        int i13 = aVar.f3432b;
        while (i13 < b10) {
            int i14 = i13 + 1;
            int Z22 = Z2(vVar, zVar, i14);
            if (Z22 <= Z2) {
                break;
            }
            i13 = i14;
            Z2 = Z22;
        }
        aVar.f3432b = i13;
    }

    private void V2() {
        View[] viewArr = this.L;
        if (viewArr == null || viewArr.length != this.J) {
            this.L = new View[this.J];
        }
    }

    private int Y2(RecyclerView.v vVar, RecyclerView.z zVar, int i10) {
        if (!zVar.e()) {
            return this.O.b(i10, this.J);
        }
        int f10 = vVar.f(i10);
        if (f10 == -1) {
            Log.w("GridLayoutManager", "Cannot find span size for pre layout position. " + i10);
            return 0;
        }
        return this.O.b(f10, this.J);
    }

    private int Z2(RecyclerView.v vVar, RecyclerView.z zVar, int i10) {
        if (!zVar.e()) {
            return this.O.c(i10, this.J);
        }
        int i11 = this.N.get(i10, -1);
        if (i11 != -1) {
            return i11;
        }
        int f10 = vVar.f(i10);
        if (f10 == -1) {
            Log.w("GridLayoutManager", "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + i10);
            return 0;
        }
        return this.O.c(f10, this.J);
    }

    private int a3(RecyclerView.v vVar, RecyclerView.z zVar, int i10) {
        if (!zVar.e()) {
            return this.O.f(i10);
        }
        int i11 = this.M.get(i10, -1);
        if (i11 != -1) {
            return i11;
        }
        int f10 = vVar.f(i10);
        if (f10 == -1) {
            Log.w("GridLayoutManager", "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + i10);
            return 1;
        }
        return this.O.f(f10);
    }

    private void b3(float f10, int i10) {
        P2(Math.max(Math.round(f10 * this.J), i10));
    }

    private void c3(View view, int i10, boolean z10) {
        int i11;
        int i12;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Rect rect = layoutParams.f3454b;
        int i13 = rect.top + rect.bottom + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        int i14 = rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
        int W2 = W2(layoutParams.f3414e, layoutParams.f3415f);
        if (this.f3420s == 1) {
            i12 = RecyclerView.p.K(W2, i10, i14, ((ViewGroup.MarginLayoutParams) layoutParams).width, false);
            i11 = RecyclerView.p.K(this.f3422u.o(), X(), i13, ((ViewGroup.MarginLayoutParams) layoutParams).height, true);
        } else {
            int K = RecyclerView.p.K(W2, i10, i13, ((ViewGroup.MarginLayoutParams) layoutParams).height, false);
            int K2 = RecyclerView.p.K(this.f3422u.o(), r0(), i14, ((ViewGroup.MarginLayoutParams) layoutParams).width, true);
            i11 = K;
            i12 = K2;
        }
        d3(view, i12, i11, z10);
    }

    private void d3(View view, int i10, int i11, boolean z10) {
        boolean H1;
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (z10) {
            H1 = J1(view, i10, i11, layoutParams);
        } else {
            H1 = H1(view, i10, i11, layoutParams);
        }
        if (H1) {
            view.measure(i10, i11);
        }
    }

    private void f3() {
        int W;
        int i02;
        if (p2() == 1) {
            W = q0() - g0();
            i02 = f0();
        } else {
            W = W() - d0();
            i02 = i0();
        }
        P2(W - i02);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    public int A1(int i10, RecyclerView.v vVar, RecyclerView.z zVar) {
        f3();
        V2();
        return super.A1(i10, vVar, zVar);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    public RecyclerView.LayoutParams D() {
        if (this.f3420s == 0) {
            return new LayoutParams(-2, -1);
        }
        return new LayoutParams(-1, -2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public RecyclerView.LayoutParams E(Context context, AttributeSet attributeSet) {
        return new LayoutParams(context, attributeSet);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void E1(Rect rect, int i10, int i11) {
        int n10;
        int n11;
        if (this.K == null) {
            super.E1(rect, i10, i11);
        }
        int f02 = f0() + g0();
        int i02 = i0() + d0();
        if (this.f3420s == 1) {
            n11 = RecyclerView.p.n(i11, rect.height() + i02, b0());
            int[] iArr = this.K;
            n10 = RecyclerView.p.n(i10, iArr[iArr.length - 1] + f02, c0());
        } else {
            n10 = RecyclerView.p.n(i10, rect.width() + f02, c0());
            int[] iArr2 = this.K;
            n11 = RecyclerView.p.n(i11, iArr2[iArr2.length - 1] + i02, b0());
        }
        D1(n10, n11);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    public void E2(boolean z10) {
        if (!z10) {
            super.E2(false);
            return;
        }
        throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public RecyclerView.LayoutParams F(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x00d6, code lost:
    
        if (r13 == (r2 > r15)) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x00f6, code lost:
    
        if (r13 == (r2 > r7)) goto L50;
     */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0107  */
    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View L0(View view, int i10, RecyclerView.v vVar, RecyclerView.z zVar) {
        int J;
        int i11;
        int i12;
        View view2;
        View view3;
        int i13;
        int i14;
        boolean z10;
        int i15;
        int i16;
        RecyclerView.v vVar2 = vVar;
        RecyclerView.z zVar2 = zVar;
        View B = B(view);
        View view4 = null;
        if (B == null) {
            return null;
        }
        LayoutParams layoutParams = (LayoutParams) B.getLayoutParams();
        int i17 = layoutParams.f3414e;
        int i18 = layoutParams.f3415f + i17;
        if (super.L0(view, i10, vVar, zVar) == null) {
            return null;
        }
        if ((T1(i10) == 1) != this.f3425x) {
            i12 = J() - 1;
            J = -1;
            i11 = -1;
        } else {
            J = J();
            i11 = 1;
            i12 = 0;
        }
        boolean z11 = this.f3420s == 1 && q2();
        int Y2 = Y2(vVar2, zVar2, i12);
        int i19 = -1;
        int i20 = -1;
        int i21 = 0;
        int i22 = 0;
        int i23 = i12;
        View view5 = null;
        while (i23 != J) {
            int Y22 = Y2(vVar2, zVar2, i23);
            View I = I(i23);
            if (I == B) {
                break;
            }
            if (!I.hasFocusable() || Y22 == Y2) {
                LayoutParams layoutParams2 = (LayoutParams) I.getLayoutParams();
                int i24 = layoutParams2.f3414e;
                view2 = B;
                int i25 = layoutParams2.f3415f + i24;
                if (I.hasFocusable() && i24 == i17 && i25 == i18) {
                    return I;
                }
                if (!(I.hasFocusable() && view4 == null) && (I.hasFocusable() || view5 != null)) {
                    view3 = view5;
                    int min = Math.min(i25, i18) - Math.max(i24, i17);
                    if (I.hasFocusable()) {
                        if (min <= i21) {
                            if (min == i21) {
                            }
                        }
                    } else if (view4 == null) {
                        i13 = i21;
                        i14 = J;
                        if (A0(I, false, true)) {
                            i15 = i22;
                            if (min > i15) {
                                i16 = i20;
                                if (z10) {
                                    if (I.hasFocusable()) {
                                        i19 = layoutParams2.f3414e;
                                        i20 = i16;
                                        i22 = i15;
                                        view5 = view3;
                                        view4 = I;
                                        i21 = Math.min(i25, i18) - Math.max(i24, i17);
                                    } else {
                                        int i26 = layoutParams2.f3414e;
                                        i22 = Math.min(i25, i18) - Math.max(i24, i17);
                                        i20 = i26;
                                        i21 = i13;
                                        view5 = I;
                                    }
                                    i23 += i11;
                                    vVar2 = vVar;
                                    zVar2 = zVar;
                                    B = view2;
                                    J = i14;
                                }
                            } else {
                                if (min == i15) {
                                    i16 = i20;
                                } else {
                                    i16 = i20;
                                }
                                z10 = false;
                                if (z10) {
                                }
                            }
                        }
                        i16 = i20;
                        i15 = i22;
                        z10 = false;
                        if (z10) {
                        }
                    }
                    i13 = i21;
                    i14 = J;
                    i16 = i20;
                    i15 = i22;
                    z10 = false;
                    if (z10) {
                    }
                } else {
                    view3 = view5;
                }
                i13 = i21;
                i14 = J;
                i16 = i20;
                i15 = i22;
                z10 = true;
                if (z10) {
                }
            } else {
                if (view4 != null) {
                    break;
                }
                view2 = B;
                view3 = view5;
                i13 = i21;
                i14 = J;
                i16 = i20;
                i15 = i22;
            }
            i20 = i16;
            i22 = i15;
            i21 = i13;
            view5 = view3;
            i23 += i11;
            vVar2 = vVar;
            zVar2 = zVar;
            B = view2;
            J = i14;
        }
        return view4 != null ? view4 : view5;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int N(RecyclerView.v vVar, RecyclerView.z zVar) {
        if (this.f3420s == 1) {
            return this.J;
        }
        if (zVar.b() < 1) {
            return 0;
        }
        return Y2(vVar, zVar, zVar.b() - 1) + 1;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    public boolean N1() {
        return this.D == null && !this.I;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    void P1(RecyclerView.z zVar, LinearLayoutManager.c cVar, RecyclerView.p.c cVar2) {
        int i10 = this.J;
        for (int i11 = 0; i11 < this.J && cVar.c(zVar) && i10 > 0; i11++) {
            int i12 = cVar.f3443d;
            cVar2.a(i12, Math.max(0, cVar.f3446g));
            i10 -= this.O.f(i12);
            cVar.f3443d += cVar.f3444e;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void R0(RecyclerView.v vVar, RecyclerView.z zVar, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.Q0(view, accessibilityNodeInfoCompat);
            return;
        }
        LayoutParams layoutParams2 = (LayoutParams) layoutParams;
        int Y2 = Y2(vVar, zVar, layoutParams2.a());
        if (this.f3420s == 0) {
            accessibilityNodeInfoCompat.Y(AccessibilityNodeInfoCompat.c.a(layoutParams2.e(), layoutParams2.f(), Y2, 1, false, false));
        } else {
            accessibilityNodeInfoCompat.Y(AccessibilityNodeInfoCompat.c.a(Y2, 1, layoutParams2.e(), layoutParams2.f(), false, false));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void T0(RecyclerView recyclerView, int i10, int i11) {
        this.O.h();
        this.O.g();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void U0(RecyclerView recyclerView) {
        this.O.h();
        this.O.g();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void V0(RecyclerView recyclerView, int i10, int i11, int i12) {
        this.O.h();
        this.O.g();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void W0(RecyclerView recyclerView, int i10, int i11) {
        this.O.h();
        this.O.g();
    }

    int W2(int i10, int i11) {
        if (this.f3420s == 1 && q2()) {
            int[] iArr = this.K;
            int i12 = this.J;
            return iArr[i12 - i10] - iArr[(i12 - i10) - i11];
        }
        int[] iArr2 = this.K;
        return iArr2[i11 + i10] - iArr2[i10];
    }

    public int X2() {
        return this.J;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void Y0(RecyclerView recyclerView, int i10, int i11, Object obj) {
        this.O.h();
        this.O.g();
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    public void Z0(RecyclerView.v vVar, RecyclerView.z zVar) {
        if (zVar.e()) {
            O2();
        }
        super.Z0(vVar, zVar);
        R2();
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    public void a1(RecyclerView.z zVar) {
        super.a1(zVar);
        this.I = false;
    }

    public void e3(int i10) {
        if (i10 == this.J) {
            return;
        }
        this.I = true;
        if (i10 >= 1) {
            this.J = i10;
            this.O.h();
            v1();
        } else {
            throw new IllegalArgumentException("Span count should be at least 1. Provided " + i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.recyclerview.widget.LinearLayoutManager
    public View j2(RecyclerView.v vVar, RecyclerView.z zVar, boolean z10, boolean z11) {
        int i10;
        int J = J();
        int i11 = -1;
        int i12 = 1;
        if (z11) {
            i10 = J() - 1;
            i12 = -1;
        } else {
            i11 = J;
            i10 = 0;
        }
        int b10 = zVar.b();
        V1();
        int n10 = this.f3422u.n();
        int i13 = this.f3422u.i();
        View view = null;
        View view2 = null;
        while (i10 != i11) {
            View I = I(i10);
            int j02 = j0(I);
            if (j02 >= 0 && j02 < b10 && Z2(vVar, zVar, j02) == 0) {
                if (((RecyclerView.LayoutParams) I.getLayoutParams()).c()) {
                    if (view2 == null) {
                        view2 = I;
                    }
                } else {
                    if (this.f3422u.g(I) < i13 && this.f3422u.d(I) >= n10) {
                        return I;
                    }
                    if (view == null) {
                        view = I;
                    }
                }
            }
            i10 += i12;
        }
        return view != null ? view : view2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public boolean m(RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int m0(RecyclerView.v vVar, RecyclerView.z zVar) {
        if (this.f3420s == 0) {
            return this.J;
        }
        if (zVar.b() < 1) {
            return 0;
        }
        return Y2(vVar, zVar, zVar.b() - 1) + 1;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    public int r(RecyclerView.z zVar) {
        if (this.Q) {
            return S2(zVar);
        }
        return super.r(zVar);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    public int s(RecyclerView.z zVar) {
        if (this.Q) {
            return T2(zVar);
        }
        return super.s(zVar);
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x009f, code lost:
    
        r21.f3437b = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a1, code lost:
    
        return;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARN: Type inference failed for: r5v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r5v19 */
    @Override // androidx.recyclerview.widget.LinearLayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void s2(RecyclerView.v vVar, RecyclerView.z zVar, LinearLayoutManager.c cVar, LinearLayoutManager.b bVar) {
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int f10;
        int K;
        int i17;
        View d10;
        int m10 = this.f3422u.m();
        ?? r52 = 0;
        boolean z10 = m10 != 1073741824;
        int i18 = J() > 0 ? this.K[this.J] : 0;
        if (z10) {
            f3();
        }
        boolean z11 = cVar.f3444e == 1;
        int i19 = this.J;
        if (!z11) {
            i19 = Z2(vVar, zVar, cVar.f3443d) + a3(vVar, zVar, cVar.f3443d);
        }
        int i20 = 0;
        while (i20 < this.J && cVar.c(zVar) && i19 > 0) {
            int i21 = cVar.f3443d;
            int a32 = a3(vVar, zVar, i21);
            if (a32 > this.J) {
                throw new IllegalArgumentException("Item at position " + i21 + " requires " + a32 + " spans but GridLayoutManager has only " + this.J + " spans.");
            }
            i19 -= a32;
            if (i19 < 0 || (d10 = cVar.d(vVar)) == null) {
                break;
            }
            this.L[i20] = d10;
            i20++;
        }
        float f11 = 0.0f;
        N2(vVar, zVar, i20, z11);
        int i22 = 0;
        int i23 = 0;
        while (i22 < i20) {
            View view = this.L[i22];
            if (cVar.f3451l == null) {
                if (z11) {
                    d(view);
                } else {
                    e(view, r52);
                }
            } else if (z11) {
                b(view);
            } else {
                c(view, r52);
            }
            j(view, this.P);
            c3(view, m10, r52);
            int e10 = this.f3422u.e(view);
            if (e10 > i23) {
                i23 = e10;
            }
            float f12 = (this.f3422u.f(view) * 1.0f) / ((LayoutParams) view.getLayoutParams()).f3415f;
            if (f12 > f11) {
                f11 = f12;
            }
            i22++;
            r52 = 0;
        }
        if (z10) {
            b3(f11, i18);
            i23 = 0;
            for (int i24 = 0; i24 < i20; i24++) {
                View view2 = this.L[i24];
                c3(view2, 1073741824, true);
                int e11 = this.f3422u.e(view2);
                if (e11 > i23) {
                    i23 = e11;
                }
            }
        }
        for (int i25 = 0; i25 < i20; i25++) {
            View view3 = this.L[i25];
            if (this.f3422u.e(view3) != i23) {
                LayoutParams layoutParams = (LayoutParams) view3.getLayoutParams();
                Rect rect = layoutParams.f3454b;
                int i26 = rect.top + rect.bottom + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                int i27 = rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                int W2 = W2(layoutParams.f3414e, layoutParams.f3415f);
                if (this.f3420s == 1) {
                    i17 = RecyclerView.p.K(W2, 1073741824, i27, ((ViewGroup.MarginLayoutParams) layoutParams).width, false);
                    K = View.MeasureSpec.makeMeasureSpec(i23 - i26, 1073741824);
                } else {
                    int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i23 - i27, 1073741824);
                    K = RecyclerView.p.K(W2, 1073741824, i26, ((ViewGroup.MarginLayoutParams) layoutParams).height, false);
                    i17 = makeMeasureSpec;
                }
                d3(view3, i17, K, true);
            }
        }
        int i28 = 0;
        bVar.f3436a = i23;
        if (this.f3420s == 1) {
            if (cVar.f3445f == -1) {
                i12 = cVar.f3441b;
                i13 = i12 - i23;
            } else {
                int i29 = cVar.f3441b;
                i13 = i29;
                i12 = i23 + i29;
            }
            i10 = 0;
            i11 = 0;
        } else if (cVar.f3445f == -1) {
            int i30 = cVar.f3441b;
            i11 = i30 - i23;
            i13 = 0;
            i10 = i30;
            i12 = 0;
        } else {
            int i31 = cVar.f3441b;
            i10 = i23 + i31;
            i11 = i31;
            i12 = 0;
            i13 = 0;
        }
        while (i28 < i20) {
            View view4 = this.L[i28];
            LayoutParams layoutParams2 = (LayoutParams) view4.getLayoutParams();
            if (this.f3420s == 1) {
                if (q2()) {
                    int f02 = f0() + this.K[this.J - layoutParams2.f3414e];
                    f10 = i12;
                    i15 = f02;
                    i16 = f02 - this.f3422u.f(view4);
                } else {
                    int f03 = f0() + this.K[layoutParams2.f3414e];
                    f10 = i12;
                    i16 = f03;
                    i15 = this.f3422u.f(view4) + f03;
                }
                i14 = i13;
            } else {
                int i02 = i0() + this.K[layoutParams2.f3414e];
                i14 = i02;
                i15 = i10;
                i16 = i11;
                f10 = this.f3422u.f(view4) + i02;
            }
            B0(view4, i16, i14, i15, f10);
            if (layoutParams2.c() || layoutParams2.b()) {
                bVar.f3438c = true;
            }
            bVar.f3439d |= view4.hasFocusable();
            i28++;
            i12 = f10;
            i10 = i15;
            i11 = i16;
            i13 = i14;
        }
        Arrays.fill(this.L, (Object) null);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    public int u(RecyclerView.z zVar) {
        if (this.Q) {
            return S2(zVar);
        }
        return super.u(zVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.recyclerview.widget.LinearLayoutManager
    public void u2(RecyclerView.v vVar, RecyclerView.z zVar, LinearLayoutManager.a aVar, int i10) {
        super.u2(vVar, zVar, aVar, i10);
        f3();
        if (zVar.b() > 0 && !zVar.e()) {
            U2(vVar, zVar, aVar, i10);
        }
        V2();
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    public int v(RecyclerView.z zVar) {
        if (this.Q) {
            return T2(zVar);
        }
        return super.v(zVar);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
    public int y1(int i10, RecyclerView.v vVar, RecyclerView.z zVar) {
        f3();
        V2();
        return super.y1(i10, vVar, zVar);
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends RecyclerView.LayoutParams {

        /* renamed from: e, reason: collision with root package name */
        int f3414e;

        /* renamed from: f, reason: collision with root package name */
        int f3415f;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f3414e = -1;
            this.f3415f = 0;
        }

        public int e() {
            return this.f3414e;
        }

        public int f() {
            return this.f3415f;
        }

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
            this.f3414e = -1;
            this.f3415f = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f3414e = -1;
            this.f3415f = 0;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f3414e = -1;
            this.f3415f = 0;
        }
    }

    public GridLayoutManager(Context context, int i10) {
        super(context);
        this.I = false;
        this.J = -1;
        this.M = new SparseIntArray();
        this.N = new SparseIntArray();
        this.O = new a();
        this.P = new Rect();
        e3(i10);
    }

    public GridLayoutManager(Context context, int i10, int i11, boolean z10) {
        super(context, i11, z10);
        this.I = false;
        this.J = -1;
        this.M = new SparseIntArray();
        this.N = new SparseIntArray();
        this.O = new a();
        this.P = new Rect();
        e3(i10);
    }
}
