package androidx.recyclerview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

/* loaded from: classes.dex */
public class StaggeredGridLayoutManager extends RecyclerView.p implements RecyclerView.y.b {
    private BitSet B;
    private boolean G;
    private boolean H;
    private SavedState I;
    private int J;
    private int[] O;

    /* renamed from: t, reason: collision with root package name */
    c[] f3558t;

    /* renamed from: u, reason: collision with root package name */
    OrientationHelper f3559u;

    /* renamed from: v, reason: collision with root package name */
    OrientationHelper f3560v;

    /* renamed from: w, reason: collision with root package name */
    private int f3561w;

    /* renamed from: x, reason: collision with root package name */
    private int f3562x;

    /* renamed from: y, reason: collision with root package name */
    private final LayoutState f3563y;

    /* renamed from: s, reason: collision with root package name */
    private int f3557s = -1;

    /* renamed from: z, reason: collision with root package name */
    boolean f3564z = false;
    boolean A = false;
    int C = -1;
    int D = Integer.MIN_VALUE;
    LazySpanLookup E = new LazySpanLookup();
    private int F = 2;
    private final Rect K = new Rect();
    private final b L = new b();
    private boolean M = false;
    private boolean N = true;
    private final Runnable P = new a();

    /* loaded from: classes.dex */
    public static class LayoutParams extends RecyclerView.LayoutParams {

        /* renamed from: e, reason: collision with root package name */
        c f3565e;

        /* renamed from: f, reason: collision with root package name */
        boolean f3566f;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public boolean e() {
            return this.f3566f;
        }

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f3573e;

        /* renamed from: f, reason: collision with root package name */
        int f3574f;

        /* renamed from: g, reason: collision with root package name */
        int f3575g;

        /* renamed from: h, reason: collision with root package name */
        int[] f3576h;

        /* renamed from: i, reason: collision with root package name */
        int f3577i;

        /* renamed from: j, reason: collision with root package name */
        int[] f3578j;

        /* renamed from: k, reason: collision with root package name */
        List<LazySpanLookup.FullSpanItem> f3579k;

        /* renamed from: l, reason: collision with root package name */
        boolean f3580l;

        /* renamed from: m, reason: collision with root package name */
        boolean f3581m;

        /* renamed from: n, reason: collision with root package name */
        boolean f3582n;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<SavedState> {
            a() {
            }

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
        }

        public SavedState() {
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        void j() {
            this.f3576h = null;
            this.f3575g = 0;
            this.f3573e = -1;
            this.f3574f = -1;
        }

        void k() {
            this.f3576h = null;
            this.f3575g = 0;
            this.f3577i = 0;
            this.f3578j = null;
            this.f3579k = null;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            parcel.writeInt(this.f3573e);
            parcel.writeInt(this.f3574f);
            parcel.writeInt(this.f3575g);
            if (this.f3575g > 0) {
                parcel.writeIntArray(this.f3576h);
            }
            parcel.writeInt(this.f3577i);
            if (this.f3577i > 0) {
                parcel.writeIntArray(this.f3578j);
            }
            parcel.writeInt(this.f3580l ? 1 : 0);
            parcel.writeInt(this.f3581m ? 1 : 0);
            parcel.writeInt(this.f3582n ? 1 : 0);
            parcel.writeList(this.f3579k);
        }

        SavedState(Parcel parcel) {
            this.f3573e = parcel.readInt();
            this.f3574f = parcel.readInt();
            int readInt = parcel.readInt();
            this.f3575g = readInt;
            if (readInt > 0) {
                int[] iArr = new int[readInt];
                this.f3576h = iArr;
                parcel.readIntArray(iArr);
            }
            int readInt2 = parcel.readInt();
            this.f3577i = readInt2;
            if (readInt2 > 0) {
                int[] iArr2 = new int[readInt2];
                this.f3578j = iArr2;
                parcel.readIntArray(iArr2);
            }
            this.f3580l = parcel.readInt() == 1;
            this.f3581m = parcel.readInt() == 1;
            this.f3582n = parcel.readInt() == 1;
            this.f3579k = parcel.readArrayList(LazySpanLookup.FullSpanItem.class.getClassLoader());
        }

        public SavedState(SavedState savedState) {
            this.f3575g = savedState.f3575g;
            this.f3573e = savedState.f3573e;
            this.f3574f = savedState.f3574f;
            this.f3576h = savedState.f3576h;
            this.f3577i = savedState.f3577i;
            this.f3578j = savedState.f3578j;
            this.f3580l = savedState.f3580l;
            this.f3581m = savedState.f3581m;
            this.f3582n = savedState.f3582n;
            this.f3579k = savedState.f3579k;
        }
    }

    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            StaggeredGridLayoutManager.this.U1();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b {

        /* renamed from: a, reason: collision with root package name */
        int f3584a;

        /* renamed from: b, reason: collision with root package name */
        int f3585b;

        /* renamed from: c, reason: collision with root package name */
        boolean f3586c;

        /* renamed from: d, reason: collision with root package name */
        boolean f3587d;

        /* renamed from: e, reason: collision with root package name */
        boolean f3588e;

        /* renamed from: f, reason: collision with root package name */
        int[] f3589f;

        b() {
            c();
        }

        void a() {
            this.f3585b = this.f3586c ? StaggeredGridLayoutManager.this.f3559u.i() : StaggeredGridLayoutManager.this.f3559u.n();
        }

        void b(int i10) {
            if (this.f3586c) {
                this.f3585b = StaggeredGridLayoutManager.this.f3559u.i() - i10;
            } else {
                this.f3585b = StaggeredGridLayoutManager.this.f3559u.n() + i10;
            }
        }

        void c() {
            this.f3584a = -1;
            this.f3585b = Integer.MIN_VALUE;
            this.f3586c = false;
            this.f3587d = false;
            this.f3588e = false;
            int[] iArr = this.f3589f;
            if (iArr != null) {
                Arrays.fill(iArr, -1);
            }
        }

        void d(c[] cVarArr) {
            int length = cVarArr.length;
            int[] iArr = this.f3589f;
            if (iArr == null || iArr.length < length) {
                this.f3589f = new int[StaggeredGridLayoutManager.this.f3558t.length];
            }
            for (int i10 = 0; i10 < length; i10++) {
                this.f3589f[i10] = cVarArr[i10].p(Integer.MIN_VALUE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c {

        /* renamed from: a, reason: collision with root package name */
        ArrayList<View> f3591a = new ArrayList<>();

        /* renamed from: b, reason: collision with root package name */
        int f3592b = Integer.MIN_VALUE;

        /* renamed from: c, reason: collision with root package name */
        int f3593c = Integer.MIN_VALUE;

        /* renamed from: d, reason: collision with root package name */
        int f3594d = 0;

        /* renamed from: e, reason: collision with root package name */
        final int f3595e;

        c(int i10) {
            this.f3595e = i10;
        }

        void a(View view) {
            LayoutParams n10 = n(view);
            n10.f3565e = this;
            this.f3591a.add(view);
            this.f3593c = Integer.MIN_VALUE;
            if (this.f3591a.size() == 1) {
                this.f3592b = Integer.MIN_VALUE;
            }
            if (n10.c() || n10.b()) {
                this.f3594d += StaggeredGridLayoutManager.this.f3559u.e(view);
            }
        }

        void b(boolean z10, int i10) {
            int p10;
            if (z10) {
                p10 = l(Integer.MIN_VALUE);
            } else {
                p10 = p(Integer.MIN_VALUE);
            }
            e();
            if (p10 == Integer.MIN_VALUE) {
                return;
            }
            if (!z10 || p10 >= StaggeredGridLayoutManager.this.f3559u.i()) {
                if (z10 || p10 <= StaggeredGridLayoutManager.this.f3559u.n()) {
                    if (i10 != Integer.MIN_VALUE) {
                        p10 += i10;
                    }
                    this.f3593c = p10;
                    this.f3592b = p10;
                }
            }
        }

        void c() {
            LazySpanLookup.FullSpanItem f10;
            ArrayList<View> arrayList = this.f3591a;
            View view = arrayList.get(arrayList.size() - 1);
            LayoutParams n10 = n(view);
            this.f3593c = StaggeredGridLayoutManager.this.f3559u.d(view);
            if (n10.f3566f && (f10 = StaggeredGridLayoutManager.this.E.f(n10.a())) != null && f10.f3570f == 1) {
                this.f3593c += f10.j(this.f3595e);
            }
        }

        void d() {
            LazySpanLookup.FullSpanItem f10;
            View view = this.f3591a.get(0);
            LayoutParams n10 = n(view);
            this.f3592b = StaggeredGridLayoutManager.this.f3559u.g(view);
            if (n10.f3566f && (f10 = StaggeredGridLayoutManager.this.E.f(n10.a())) != null && f10.f3570f == -1) {
                this.f3592b -= f10.j(this.f3595e);
            }
        }

        void e() {
            this.f3591a.clear();
            q();
            this.f3594d = 0;
        }

        public int f() {
            if (StaggeredGridLayoutManager.this.f3564z) {
                return i(this.f3591a.size() - 1, -1, true);
            }
            return i(0, this.f3591a.size(), true);
        }

        public int g() {
            if (StaggeredGridLayoutManager.this.f3564z) {
                return i(0, this.f3591a.size(), true);
            }
            return i(this.f3591a.size() - 1, -1, true);
        }

        int h(int i10, int i11, boolean z10, boolean z11, boolean z12) {
            int n10 = StaggeredGridLayoutManager.this.f3559u.n();
            int i12 = StaggeredGridLayoutManager.this.f3559u.i();
            int i13 = i11 > i10 ? 1 : -1;
            while (i10 != i11) {
                View view = this.f3591a.get(i10);
                int g6 = StaggeredGridLayoutManager.this.f3559u.g(view);
                int d10 = StaggeredGridLayoutManager.this.f3559u.d(view);
                boolean z13 = false;
                boolean z14 = !z12 ? g6 >= i12 : g6 > i12;
                if (!z12 ? d10 > n10 : d10 >= n10) {
                    z13 = true;
                }
                if (z14 && z13) {
                    if (z10 && z11) {
                        if (g6 >= n10 && d10 <= i12) {
                            return StaggeredGridLayoutManager.this.j0(view);
                        }
                    } else {
                        if (z11) {
                            return StaggeredGridLayoutManager.this.j0(view);
                        }
                        if (g6 < n10 || d10 > i12) {
                            return StaggeredGridLayoutManager.this.j0(view);
                        }
                    }
                }
                i10 += i13;
            }
            return -1;
        }

        int i(int i10, int i11, boolean z10) {
            return h(i10, i11, false, false, z10);
        }

        public int j() {
            return this.f3594d;
        }

        int k() {
            int i10 = this.f3593c;
            if (i10 != Integer.MIN_VALUE) {
                return i10;
            }
            c();
            return this.f3593c;
        }

        int l(int i10) {
            int i11 = this.f3593c;
            if (i11 != Integer.MIN_VALUE) {
                return i11;
            }
            if (this.f3591a.size() == 0) {
                return i10;
            }
            c();
            return this.f3593c;
        }

        public View m(int i10, int i11) {
            View view = null;
            if (i11 == -1) {
                int size = this.f3591a.size();
                int i12 = 0;
                while (i12 < size) {
                    View view2 = this.f3591a.get(i12);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = StaggeredGridLayoutManager.this;
                    if (staggeredGridLayoutManager.f3564z && staggeredGridLayoutManager.j0(view2) <= i10) {
                        break;
                    }
                    StaggeredGridLayoutManager staggeredGridLayoutManager2 = StaggeredGridLayoutManager.this;
                    if ((!staggeredGridLayoutManager2.f3564z && staggeredGridLayoutManager2.j0(view2) >= i10) || !view2.hasFocusable()) {
                        break;
                    }
                    i12++;
                    view = view2;
                }
            } else {
                int size2 = this.f3591a.size() - 1;
                while (size2 >= 0) {
                    View view3 = this.f3591a.get(size2);
                    StaggeredGridLayoutManager staggeredGridLayoutManager3 = StaggeredGridLayoutManager.this;
                    if (staggeredGridLayoutManager3.f3564z && staggeredGridLayoutManager3.j0(view3) >= i10) {
                        break;
                    }
                    StaggeredGridLayoutManager staggeredGridLayoutManager4 = StaggeredGridLayoutManager.this;
                    if ((!staggeredGridLayoutManager4.f3564z && staggeredGridLayoutManager4.j0(view3) <= i10) || !view3.hasFocusable()) {
                        break;
                    }
                    size2--;
                    view = view3;
                }
            }
            return view;
        }

        LayoutParams n(View view) {
            return (LayoutParams) view.getLayoutParams();
        }

        int o() {
            int i10 = this.f3592b;
            if (i10 != Integer.MIN_VALUE) {
                return i10;
            }
            d();
            return this.f3592b;
        }

        int p(int i10) {
            int i11 = this.f3592b;
            if (i11 != Integer.MIN_VALUE) {
                return i11;
            }
            if (this.f3591a.size() == 0) {
                return i10;
            }
            d();
            return this.f3592b;
        }

        void q() {
            this.f3592b = Integer.MIN_VALUE;
            this.f3593c = Integer.MIN_VALUE;
        }

        void r(int i10) {
            int i11 = this.f3592b;
            if (i11 != Integer.MIN_VALUE) {
                this.f3592b = i11 + i10;
            }
            int i12 = this.f3593c;
            if (i12 != Integer.MIN_VALUE) {
                this.f3593c = i12 + i10;
            }
        }

        void s() {
            int size = this.f3591a.size();
            View remove = this.f3591a.remove(size - 1);
            LayoutParams n10 = n(remove);
            n10.f3565e = null;
            if (n10.c() || n10.b()) {
                this.f3594d -= StaggeredGridLayoutManager.this.f3559u.e(remove);
            }
            if (size == 1) {
                this.f3592b = Integer.MIN_VALUE;
            }
            this.f3593c = Integer.MIN_VALUE;
        }

        void t() {
            View remove = this.f3591a.remove(0);
            LayoutParams n10 = n(remove);
            n10.f3565e = null;
            if (this.f3591a.size() == 0) {
                this.f3593c = Integer.MIN_VALUE;
            }
            if (n10.c() || n10.b()) {
                this.f3594d -= StaggeredGridLayoutManager.this.f3559u.e(remove);
            }
            this.f3592b = Integer.MIN_VALUE;
        }

        void u(View view) {
            LayoutParams n10 = n(view);
            n10.f3565e = this;
            this.f3591a.add(0, view);
            this.f3592b = Integer.MIN_VALUE;
            if (this.f3591a.size() == 1) {
                this.f3593c = Integer.MIN_VALUE;
            }
            if (n10.c() || n10.b()) {
                this.f3594d += StaggeredGridLayoutManager.this.f3559u.e(view);
            }
        }

        void v(int i10) {
            this.f3592b = i10;
            this.f3593c = i10;
        }
    }

    public StaggeredGridLayoutManager(Context context, AttributeSet attributeSet, int i10, int i11) {
        RecyclerView.p.d k02 = RecyclerView.p.k0(context, attributeSet, i10, i11);
        J2(k02.f3506a);
        L2(k02.f3507b);
        K2(k02.f3508c);
        this.f3563y = new LayoutState();
        c2();
    }

    private void B2(View view) {
        for (int i10 = this.f3557s - 1; i10 >= 0; i10--) {
            this.f3558t[i10].u(view);
        }
    }

    private void C2(RecyclerView.v vVar, LayoutState layoutState) {
        int min;
        int min2;
        if (!layoutState.f3778a || layoutState.f3786i) {
            return;
        }
        if (layoutState.f3779b == 0) {
            if (layoutState.f3782e == -1) {
                D2(vVar, layoutState.f3784g);
                return;
            } else {
                E2(vVar, layoutState.f3783f);
                return;
            }
        }
        if (layoutState.f3782e == -1) {
            int i10 = layoutState.f3783f;
            int o22 = i10 - o2(i10);
            if (o22 < 0) {
                min2 = layoutState.f3784g;
            } else {
                min2 = layoutState.f3784g - Math.min(o22, layoutState.f3779b);
            }
            D2(vVar, min2);
            return;
        }
        int p22 = p2(layoutState.f3784g) - layoutState.f3784g;
        if (p22 < 0) {
            min = layoutState.f3783f;
        } else {
            min = Math.min(p22, layoutState.f3779b) + layoutState.f3783f;
        }
        E2(vVar, min);
    }

    private void D2(RecyclerView.v vVar, int i10) {
        for (int J = J() - 1; J >= 0; J--) {
            View I = I(J);
            if (this.f3559u.g(I) < i10 || this.f3559u.r(I) < i10) {
                return;
            }
            LayoutParams layoutParams = (LayoutParams) I.getLayoutParams();
            if (layoutParams.f3566f) {
                for (int i11 = 0; i11 < this.f3557s; i11++) {
                    if (this.f3558t[i11].f3591a.size() == 1) {
                        return;
                    }
                }
                for (int i12 = 0; i12 < this.f3557s; i12++) {
                    this.f3558t[i12].s();
                }
            } else if (layoutParams.f3565e.f3591a.size() == 1) {
                return;
            } else {
                layoutParams.f3565e.s();
            }
            o1(I, vVar);
        }
    }

    private void E2(RecyclerView.v vVar, int i10) {
        while (J() > 0) {
            View I = I(0);
            if (this.f3559u.d(I) > i10 || this.f3559u.q(I) > i10) {
                return;
            }
            LayoutParams layoutParams = (LayoutParams) I.getLayoutParams();
            if (layoutParams.f3566f) {
                for (int i11 = 0; i11 < this.f3557s; i11++) {
                    if (this.f3558t[i11].f3591a.size() == 1) {
                        return;
                    }
                }
                for (int i12 = 0; i12 < this.f3557s; i12++) {
                    this.f3558t[i12].t();
                }
            } else if (layoutParams.f3565e.f3591a.size() == 1) {
                return;
            } else {
                layoutParams.f3565e.t();
            }
            o1(I, vVar);
        }
    }

    private void F2() {
        if (this.f3560v.l() == 1073741824) {
            return;
        }
        float f10 = 0.0f;
        int J = J();
        for (int i10 = 0; i10 < J; i10++) {
            View I = I(i10);
            float e10 = this.f3560v.e(I);
            if (e10 >= f10) {
                if (((LayoutParams) I.getLayoutParams()).e()) {
                    e10 = (e10 * 1.0f) / this.f3557s;
                }
                f10 = Math.max(f10, e10);
            }
        }
        int i11 = this.f3562x;
        int round = Math.round(f10 * this.f3557s);
        if (this.f3560v.l() == Integer.MIN_VALUE) {
            round = Math.min(round, this.f3560v.o());
        }
        R2(round);
        if (this.f3562x == i11) {
            return;
        }
        for (int i12 = 0; i12 < J; i12++) {
            View I2 = I(i12);
            LayoutParams layoutParams = (LayoutParams) I2.getLayoutParams();
            if (!layoutParams.f3566f) {
                if (v2() && this.f3561w == 1) {
                    int i13 = this.f3557s;
                    int i14 = layoutParams.f3565e.f3595e;
                    I2.offsetLeftAndRight(((-((i13 - 1) - i14)) * this.f3562x) - ((-((i13 - 1) - i14)) * i11));
                } else {
                    int i15 = layoutParams.f3565e.f3595e;
                    int i16 = this.f3562x * i15;
                    int i17 = i15 * i11;
                    if (this.f3561w == 1) {
                        I2.offsetLeftAndRight(i16 - i17);
                    } else {
                        I2.offsetTopAndBottom(i16 - i17);
                    }
                }
            }
        }
    }

    private void G2() {
        if (this.f3561w != 1 && v2()) {
            this.A = !this.f3564z;
        } else {
            this.A = this.f3564z;
        }
    }

    private void I2(int i10) {
        LayoutState layoutState = this.f3563y;
        layoutState.f3782e = i10;
        layoutState.f3781d = this.A != (i10 == -1) ? -1 : 1;
    }

    private void M2(int i10, int i11) {
        for (int i12 = 0; i12 < this.f3557s; i12++) {
            if (!this.f3558t[i12].f3591a.isEmpty()) {
                S2(this.f3558t[i12], i10, i11);
            }
        }
    }

    private boolean N2(RecyclerView.z zVar, b bVar) {
        int e22;
        if (this.G) {
            e22 = i2(zVar.b());
        } else {
            e22 = e2(zVar.b());
        }
        bVar.f3584a = e22;
        bVar.f3585b = Integer.MIN_VALUE;
        return true;
    }

    private void O1(View view) {
        for (int i10 = this.f3557s - 1; i10 >= 0; i10--) {
            this.f3558t[i10].a(view);
        }
    }

    private void P1(b bVar) {
        int n10;
        SavedState savedState = this.I;
        int i10 = savedState.f3575g;
        if (i10 > 0) {
            if (i10 == this.f3557s) {
                for (int i11 = 0; i11 < this.f3557s; i11++) {
                    this.f3558t[i11].e();
                    SavedState savedState2 = this.I;
                    int i12 = savedState2.f3576h[i11];
                    if (i12 != Integer.MIN_VALUE) {
                        if (savedState2.f3581m) {
                            n10 = this.f3559u.i();
                        } else {
                            n10 = this.f3559u.n();
                        }
                        i12 += n10;
                    }
                    this.f3558t[i11].v(i12);
                }
            } else {
                savedState.k();
                SavedState savedState3 = this.I;
                savedState3.f3573e = savedState3.f3574f;
            }
        }
        SavedState savedState4 = this.I;
        this.H = savedState4.f3582n;
        K2(savedState4.f3580l);
        G2();
        SavedState savedState5 = this.I;
        int i13 = savedState5.f3573e;
        if (i13 != -1) {
            this.C = i13;
            bVar.f3586c = savedState5.f3581m;
        } else {
            bVar.f3586c = this.A;
        }
        if (savedState5.f3577i > 1) {
            LazySpanLookup lazySpanLookup = this.E;
            lazySpanLookup.f3567a = savedState5.f3578j;
            lazySpanLookup.f3568b = savedState5.f3579k;
        }
    }

    private void Q2(int i10, RecyclerView.z zVar) {
        int i11;
        int i12;
        int c10;
        LayoutState layoutState = this.f3563y;
        boolean z10 = false;
        layoutState.f3779b = 0;
        layoutState.f3780c = i10;
        if (!z0() || (c10 = zVar.c()) == -1) {
            i11 = 0;
            i12 = 0;
        } else {
            if (this.A == (c10 < i10)) {
                i11 = this.f3559u.o();
                i12 = 0;
            } else {
                i12 = this.f3559u.o();
                i11 = 0;
            }
        }
        if (M()) {
            this.f3563y.f3783f = this.f3559u.n() - i12;
            this.f3563y.f3784g = this.f3559u.i() + i11;
        } else {
            this.f3563y.f3784g = this.f3559u.h() + i11;
            this.f3563y.f3783f = -i12;
        }
        LayoutState layoutState2 = this.f3563y;
        layoutState2.f3785h = false;
        layoutState2.f3778a = true;
        if (this.f3559u.l() == 0 && this.f3559u.h() == 0) {
            z10 = true;
        }
        layoutState2.f3786i = z10;
    }

    private void S1(View view, LayoutParams layoutParams, LayoutState layoutState) {
        if (layoutState.f3782e == 1) {
            if (layoutParams.f3566f) {
                O1(view);
                return;
            } else {
                layoutParams.f3565e.a(view);
                return;
            }
        }
        if (layoutParams.f3566f) {
            B2(view);
        } else {
            layoutParams.f3565e.u(view);
        }
    }

    private void S2(c cVar, int i10, int i11) {
        int j10 = cVar.j();
        if (i10 == -1) {
            if (cVar.o() + j10 <= i11) {
                this.B.set(cVar.f3595e, false);
            }
        } else if (cVar.k() - j10 >= i11) {
            this.B.set(cVar.f3595e, false);
        }
    }

    private int T1(int i10) {
        if (J() == 0) {
            return this.A ? 1 : -1;
        }
        return (i10 < l2()) != this.A ? -1 : 1;
    }

    private int T2(int i10, int i11, int i12) {
        if (i11 == 0 && i12 == 0) {
            return i10;
        }
        int mode = View.MeasureSpec.getMode(i10);
        return (mode == Integer.MIN_VALUE || mode == 1073741824) ? View.MeasureSpec.makeMeasureSpec(Math.max(0, (View.MeasureSpec.getSize(i10) - i11) - i12), mode) : i10;
    }

    private boolean V1(c cVar) {
        if (this.A) {
            if (cVar.k() < this.f3559u.i()) {
                ArrayList<View> arrayList = cVar.f3591a;
                return !cVar.n(arrayList.get(arrayList.size() - 1)).f3566f;
            }
        } else if (cVar.o() > this.f3559u.n()) {
            return !cVar.n(cVar.f3591a.get(0)).f3566f;
        }
        return false;
    }

    private int W1(RecyclerView.z zVar) {
        if (J() == 0) {
            return 0;
        }
        return ScrollbarHelper.a(zVar, this.f3559u, g2(!this.N), f2(!this.N), this, this.N);
    }

    private int X1(RecyclerView.z zVar) {
        if (J() == 0) {
            return 0;
        }
        return ScrollbarHelper.b(zVar, this.f3559u, g2(!this.N), f2(!this.N), this, this.N, this.A);
    }

    private int Y1(RecyclerView.z zVar) {
        if (J() == 0) {
            return 0;
        }
        return ScrollbarHelper.c(zVar, this.f3559u, g2(!this.N), f2(!this.N), this, this.N);
    }

    private int Z1(int i10) {
        return i10 != 1 ? i10 != 2 ? i10 != 17 ? i10 != 33 ? i10 != 66 ? (i10 == 130 && this.f3561w == 1) ? 1 : Integer.MIN_VALUE : this.f3561w == 0 ? 1 : Integer.MIN_VALUE : this.f3561w == 1 ? -1 : Integer.MIN_VALUE : this.f3561w == 0 ? -1 : Integer.MIN_VALUE : (this.f3561w != 1 && v2()) ? -1 : 1 : (this.f3561w != 1 && v2()) ? 1 : -1;
    }

    private LazySpanLookup.FullSpanItem a2(int i10) {
        LazySpanLookup.FullSpanItem fullSpanItem = new LazySpanLookup.FullSpanItem();
        fullSpanItem.f3571g = new int[this.f3557s];
        for (int i11 = 0; i11 < this.f3557s; i11++) {
            fullSpanItem.f3571g[i11] = i10 - this.f3558t[i11].l(i10);
        }
        return fullSpanItem;
    }

    private LazySpanLookup.FullSpanItem b2(int i10) {
        LazySpanLookup.FullSpanItem fullSpanItem = new LazySpanLookup.FullSpanItem();
        fullSpanItem.f3571g = new int[this.f3557s];
        for (int i11 = 0; i11 < this.f3557s; i11++) {
            fullSpanItem.f3571g[i11] = this.f3558t[i11].p(i10) - i10;
        }
        return fullSpanItem;
    }

    private void c2() {
        this.f3559u = OrientationHelper.b(this, this.f3561w);
        this.f3560v = OrientationHelper.b(this, 1 - this.f3561w);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v0 */
    /* JADX WARN: Type inference failed for: r9v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r9v7 */
    private int d2(RecyclerView.v vVar, LayoutState layoutState, RecyclerView.z zVar) {
        int i10;
        int n10;
        int n22;
        c cVar;
        int e10;
        int i11;
        int i12;
        int e11;
        boolean z10;
        boolean R1;
        ?? r92 = 0;
        this.B.set(0, this.f3557s, true);
        if (this.f3563y.f3786i) {
            i10 = layoutState.f3782e == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        } else if (layoutState.f3782e == 1) {
            i10 = layoutState.f3784g + layoutState.f3779b;
        } else {
            i10 = layoutState.f3783f - layoutState.f3779b;
        }
        int i13 = i10;
        M2(layoutState.f3782e, i13);
        if (this.A) {
            n10 = this.f3559u.i();
        } else {
            n10 = this.f3559u.n();
        }
        int i14 = n10;
        boolean z11 = false;
        while (layoutState.a(zVar) && (this.f3563y.f3786i || !this.B.isEmpty())) {
            View b10 = layoutState.b(vVar);
            LayoutParams layoutParams = (LayoutParams) b10.getLayoutParams();
            int a10 = layoutParams.a();
            int g6 = this.E.g(a10);
            boolean z12 = g6 == -1 ? true : r92;
            if (z12) {
                cVar = layoutParams.f3566f ? this.f3558t[r92] : r2(layoutState);
                this.E.n(a10, cVar);
            } else {
                cVar = this.f3558t[g6];
            }
            c cVar2 = cVar;
            layoutParams.f3565e = cVar2;
            if (layoutState.f3782e == 1) {
                d(b10);
            } else {
                e(b10, r92);
            }
            x2(b10, layoutParams, r92);
            if (layoutState.f3782e == 1) {
                int n23 = layoutParams.f3566f ? n2(i14) : cVar2.l(i14);
                int e12 = this.f3559u.e(b10) + n23;
                if (z12 && layoutParams.f3566f) {
                    LazySpanLookup.FullSpanItem a22 = a2(n23);
                    a22.f3570f = -1;
                    a22.f3569e = a10;
                    this.E.a(a22);
                }
                i11 = e12;
                e10 = n23;
            } else {
                int q22 = layoutParams.f3566f ? q2(i14) : cVar2.p(i14);
                e10 = q22 - this.f3559u.e(b10);
                if (z12 && layoutParams.f3566f) {
                    LazySpanLookup.FullSpanItem b22 = b2(q22);
                    b22.f3570f = 1;
                    b22.f3569e = a10;
                    this.E.a(b22);
                }
                i11 = q22;
            }
            if (layoutParams.f3566f && layoutState.f3781d == -1) {
                if (z12) {
                    this.M = true;
                } else {
                    if (layoutState.f3782e == 1) {
                        R1 = Q1();
                    } else {
                        R1 = R1();
                    }
                    if (!R1) {
                        LazySpanLookup.FullSpanItem f10 = this.E.f(a10);
                        if (f10 != null) {
                            f10.f3572h = true;
                        }
                        this.M = true;
                    }
                }
            }
            S1(b10, layoutParams, layoutState);
            if (v2() && this.f3561w == 1) {
                int i15 = layoutParams.f3566f ? this.f3560v.i() : this.f3560v.i() - (((this.f3557s - 1) - cVar2.f3595e) * this.f3562x);
                e11 = i15;
                i12 = i15 - this.f3560v.e(b10);
            } else {
                int n11 = layoutParams.f3566f ? this.f3560v.n() : (cVar2.f3595e * this.f3562x) + this.f3560v.n();
                i12 = n11;
                e11 = this.f3560v.e(b10) + n11;
            }
            if (this.f3561w == 1) {
                B0(b10, i12, e10, e11, i11);
            } else {
                B0(b10, e10, i12, i11, e11);
            }
            if (layoutParams.f3566f) {
                M2(this.f3563y.f3782e, i13);
            } else {
                S2(cVar2, this.f3563y.f3782e, i13);
            }
            C2(vVar, this.f3563y);
            if (this.f3563y.f3785h && b10.hasFocusable()) {
                if (layoutParams.f3566f) {
                    this.B.clear();
                } else {
                    z10 = false;
                    this.B.set(cVar2.f3595e, false);
                    r92 = z10;
                    z11 = true;
                }
            }
            z10 = false;
            r92 = z10;
            z11 = true;
        }
        int i16 = r92;
        if (!z11) {
            C2(vVar, this.f3563y);
        }
        if (this.f3563y.f3782e == -1) {
            n22 = this.f3559u.n() - q2(this.f3559u.n());
        } else {
            n22 = n2(this.f3559u.i()) - this.f3559u.i();
        }
        return n22 > 0 ? Math.min(layoutState.f3779b, n22) : i16;
    }

    private int e2(int i10) {
        int J = J();
        for (int i11 = 0; i11 < J; i11++) {
            int j02 = j0(I(i11));
            if (j02 >= 0 && j02 < i10) {
                return j02;
            }
        }
        return 0;
    }

    private int i2(int i10) {
        for (int J = J() - 1; J >= 0; J--) {
            int j02 = j0(I(J));
            if (j02 >= 0 && j02 < i10) {
                return j02;
            }
        }
        return 0;
    }

    private void j2(RecyclerView.v vVar, RecyclerView.z zVar, boolean z10) {
        int i10;
        int n22 = n2(Integer.MIN_VALUE);
        if (n22 != Integer.MIN_VALUE && (i10 = this.f3559u.i() - n22) > 0) {
            int i11 = i10 - (-H2(-i10, vVar, zVar));
            if (!z10 || i11 <= 0) {
                return;
            }
            this.f3559u.s(i11);
        }
    }

    private void k2(RecyclerView.v vVar, RecyclerView.z zVar, boolean z10) {
        int n10;
        int q22 = q2(Integer.MAX_VALUE);
        if (q22 != Integer.MAX_VALUE && (n10 = q22 - this.f3559u.n()) > 0) {
            int H2 = n10 - H2(n10, vVar, zVar);
            if (!z10 || H2 <= 0) {
                return;
            }
            this.f3559u.s(-H2);
        }
    }

    private int n2(int i10) {
        int l10 = this.f3558t[0].l(i10);
        for (int i11 = 1; i11 < this.f3557s; i11++) {
            int l11 = this.f3558t[i11].l(i10);
            if (l11 > l10) {
                l10 = l11;
            }
        }
        return l10;
    }

    private int o2(int i10) {
        int p10 = this.f3558t[0].p(i10);
        for (int i11 = 1; i11 < this.f3557s; i11++) {
            int p11 = this.f3558t[i11].p(i10);
            if (p11 > p10) {
                p10 = p11;
            }
        }
        return p10;
    }

    private int p2(int i10) {
        int l10 = this.f3558t[0].l(i10);
        for (int i11 = 1; i11 < this.f3557s; i11++) {
            int l11 = this.f3558t[i11].l(i10);
            if (l11 < l10) {
                l10 = l11;
            }
        }
        return l10;
    }

    private int q2(int i10) {
        int p10 = this.f3558t[0].p(i10);
        for (int i11 = 1; i11 < this.f3557s; i11++) {
            int p11 = this.f3558t[i11].p(i10);
            if (p11 < p10) {
                p10 = p11;
            }
        }
        return p10;
    }

    private c r2(LayoutState layoutState) {
        int i10;
        int i11;
        int i12 = -1;
        if (z2(layoutState.f3782e)) {
            i10 = this.f3557s - 1;
            i11 = -1;
        } else {
            i10 = 0;
            i12 = this.f3557s;
            i11 = 1;
        }
        c cVar = null;
        if (layoutState.f3782e == 1) {
            int i13 = Integer.MAX_VALUE;
            int n10 = this.f3559u.n();
            while (i10 != i12) {
                c cVar2 = this.f3558t[i10];
                int l10 = cVar2.l(n10);
                if (l10 < i13) {
                    cVar = cVar2;
                    i13 = l10;
                }
                i10 += i11;
            }
            return cVar;
        }
        int i14 = Integer.MIN_VALUE;
        int i15 = this.f3559u.i();
        while (i10 != i12) {
            c cVar3 = this.f3558t[i10];
            int p10 = cVar3.p(i15);
            if (p10 > i14) {
                cVar = cVar3;
                i14 = p10;
            }
            i10 += i11;
        }
        return cVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0043 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x003c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void s2(int i10, int i11, int i12) {
        int i13;
        int i14;
        int m22 = this.A ? m2() : l2();
        if (i12 != 8) {
            i13 = i10 + i11;
        } else {
            if (i10 >= i11) {
                i13 = i10 + 1;
                i14 = i11;
                this.E.h(i14);
                if (i12 != 1) {
                    this.E.j(i10, i11);
                } else if (i12 == 2) {
                    this.E.k(i10, i11);
                } else if (i12 == 8) {
                    this.E.k(i10, 1);
                    this.E.j(i11, 1);
                }
                if (i13 > m22) {
                    return;
                }
                if (i14 <= (this.A ? l2() : m2())) {
                    v1();
                    return;
                }
                return;
            }
            i13 = i11 + 1;
        }
        i14 = i10;
        this.E.h(i14);
        if (i12 != 1) {
        }
        if (i13 > m22) {
        }
    }

    private void w2(View view, int i10, int i11, boolean z10) {
        boolean H1;
        j(view, this.K);
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i12 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
        Rect rect = this.K;
        int T2 = T2(i10, i12 + rect.left, ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + rect.right);
        int i13 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
        Rect rect2 = this.K;
        int T22 = T2(i11, i13 + rect2.top, ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + rect2.bottom);
        if (z10) {
            H1 = J1(view, T2, T22, layoutParams);
        } else {
            H1 = H1(view, T2, T22, layoutParams);
        }
        if (H1) {
            view.measure(T2, T22);
        }
    }

    private void x2(View view, LayoutParams layoutParams, boolean z10) {
        if (layoutParams.f3566f) {
            if (this.f3561w == 1) {
                w2(view, this.J, RecyclerView.p.K(W(), X(), i0() + d0(), ((ViewGroup.MarginLayoutParams) layoutParams).height, true), z10);
                return;
            } else {
                w2(view, RecyclerView.p.K(q0(), r0(), f0() + g0(), ((ViewGroup.MarginLayoutParams) layoutParams).width, true), this.J, z10);
                return;
            }
        }
        if (this.f3561w == 1) {
            w2(view, RecyclerView.p.K(this.f3562x, r0(), 0, ((ViewGroup.MarginLayoutParams) layoutParams).width, false), RecyclerView.p.K(W(), X(), i0() + d0(), ((ViewGroup.MarginLayoutParams) layoutParams).height, true), z10);
        } else {
            w2(view, RecyclerView.p.K(q0(), r0(), f0() + g0(), ((ViewGroup.MarginLayoutParams) layoutParams).width, true), RecyclerView.p.K(this.f3562x, X(), 0, ((ViewGroup.MarginLayoutParams) layoutParams).height, false), z10);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:82:0x0157, code lost:
    
        if (U1() != false) goto L90;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void y2(RecyclerView.v vVar, RecyclerView.z zVar, boolean z10) {
        SavedState savedState;
        b bVar = this.L;
        if ((this.I != null || this.C != -1) && zVar.b() == 0) {
            m1(vVar);
            bVar.c();
            return;
        }
        boolean z11 = true;
        boolean z12 = (bVar.f3588e && this.C == -1 && this.I == null) ? false : true;
        if (z12) {
            bVar.c();
            if (this.I != null) {
                P1(bVar);
            } else {
                G2();
                bVar.f3586c = this.A;
            }
            P2(zVar, bVar);
            bVar.f3588e = true;
        }
        if (this.I == null && this.C == -1 && (bVar.f3586c != this.G || v2() != this.H)) {
            this.E.b();
            bVar.f3587d = true;
        }
        if (J() > 0 && ((savedState = this.I) == null || savedState.f3575g < 1)) {
            if (bVar.f3587d) {
                for (int i10 = 0; i10 < this.f3557s; i10++) {
                    this.f3558t[i10].e();
                    int i11 = bVar.f3585b;
                    if (i11 != Integer.MIN_VALUE) {
                        this.f3558t[i10].v(i11);
                    }
                }
            } else if (!z12 && this.L.f3589f != null) {
                for (int i12 = 0; i12 < this.f3557s; i12++) {
                    c cVar = this.f3558t[i12];
                    cVar.e();
                    cVar.v(this.L.f3589f[i12]);
                }
            } else {
                for (int i13 = 0; i13 < this.f3557s; i13++) {
                    this.f3558t[i13].b(this.A, bVar.f3585b);
                }
                this.L.d(this.f3558t);
            }
        }
        w(vVar);
        this.f3563y.f3778a = false;
        this.M = false;
        R2(this.f3560v.o());
        Q2(bVar.f3584a, zVar);
        if (bVar.f3586c) {
            I2(-1);
            d2(vVar, this.f3563y, zVar);
            I2(1);
            LayoutState layoutState = this.f3563y;
            layoutState.f3780c = bVar.f3584a + layoutState.f3781d;
            d2(vVar, layoutState, zVar);
        } else {
            I2(1);
            d2(vVar, this.f3563y, zVar);
            I2(-1);
            LayoutState layoutState2 = this.f3563y;
            layoutState2.f3780c = bVar.f3584a + layoutState2.f3781d;
            d2(vVar, layoutState2, zVar);
        }
        F2();
        if (J() > 0) {
            if (this.A) {
                j2(vVar, zVar, true);
                k2(vVar, zVar, false);
            } else {
                k2(vVar, zVar, true);
                j2(vVar, zVar, false);
            }
        }
        if (z10 && !zVar.e()) {
            if (this.F != 0 && J() > 0 && (this.M || t2() != null)) {
                q1(this.P);
            }
        }
        z11 = false;
        if (zVar.e()) {
            this.L.c();
        }
        this.G = bVar.f3586c;
        this.H = v2();
        if (z11) {
            this.L.c();
            y2(vVar, zVar, false);
        }
    }

    private boolean z2(int i10) {
        if (this.f3561w == 0) {
            return (i10 == -1) != this.A;
        }
        return ((i10 == -1) == this.A) == v2();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int A1(int i10, RecyclerView.v vVar, RecyclerView.z zVar) {
        return H2(i10, vVar, zVar);
    }

    void A2(int i10, RecyclerView.z zVar) {
        int i11;
        int l22;
        if (i10 > 0) {
            l22 = m2();
            i11 = 1;
        } else {
            i11 = -1;
            l22 = l2();
        }
        this.f3563y.f3778a = true;
        Q2(l22, zVar);
        I2(i11);
        LayoutState layoutState = this.f3563y;
        layoutState.f3780c = l22 + layoutState.f3781d;
        layoutState.f3779b = Math.abs(i10);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public RecyclerView.LayoutParams D() {
        if (this.f3561w == 0) {
            return new LayoutParams(-2, -1);
        }
        return new LayoutParams(-1, -2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public RecyclerView.LayoutParams E(Context context, AttributeSet attributeSet) {
        return new LayoutParams(context, attributeSet);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void E0(int i10) {
        super.E0(i10);
        for (int i11 = 0; i11 < this.f3557s; i11++) {
            this.f3558t[i11].r(i10);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void E1(Rect rect, int i10, int i11) {
        int n10;
        int n11;
        int f02 = f0() + g0();
        int i02 = i0() + d0();
        if (this.f3561w == 1) {
            n11 = RecyclerView.p.n(i11, rect.height() + i02, b0());
            n10 = RecyclerView.p.n(i10, (this.f3562x * this.f3557s) + f02, c0());
        } else {
            n10 = RecyclerView.p.n(i10, rect.width() + f02, c0());
            n11 = RecyclerView.p.n(i11, (this.f3562x * this.f3557s) + i02, b0());
        }
        D1(n10, n11);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public RecyclerView.LayoutParams F(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void F0(int i10) {
        super.F0(i10);
        for (int i11 = 0; i11 < this.f3557s; i11++) {
            this.f3558t[i11].r(i10);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void G0(RecyclerView.h hVar, RecyclerView.h hVar2) {
        this.E.b();
        for (int i10 = 0; i10 < this.f3557s; i10++) {
            this.f3558t[i10].e();
        }
    }

    int H2(int i10, RecyclerView.v vVar, RecyclerView.z zVar) {
        if (J() == 0 || i10 == 0) {
            return 0;
        }
        A2(i10, zVar);
        int d22 = d2(vVar, this.f3563y, zVar);
        if (this.f3563y.f3779b >= d22) {
            i10 = i10 < 0 ? -d22 : d22;
        }
        this.f3559u.s(-i10);
        this.G = this.A;
        LayoutState layoutState = this.f3563y;
        layoutState.f3779b = 0;
        C2(vVar, layoutState);
        return i10;
    }

    public void J2(int i10) {
        if (i10 != 0 && i10 != 1) {
            throw new IllegalArgumentException("invalid orientation.");
        }
        g(null);
        if (i10 == this.f3561w) {
            return;
        }
        this.f3561w = i10;
        OrientationHelper orientationHelper = this.f3559u;
        this.f3559u = this.f3560v;
        this.f3560v = orientationHelper;
        v1();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void K0(RecyclerView recyclerView, RecyclerView.v vVar) {
        super.K0(recyclerView, vVar);
        q1(this.P);
        for (int i10 = 0; i10 < this.f3557s; i10++) {
            this.f3558t[i10].e();
        }
        recyclerView.requestLayout();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void K1(RecyclerView recyclerView, RecyclerView.z zVar, int i10) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.p(i10);
        L1(linearSmoothScroller);
    }

    public void K2(boolean z10) {
        g(null);
        SavedState savedState = this.I;
        if (savedState != null && savedState.f3580l != z10) {
            savedState.f3580l = z10;
        }
        this.f3564z = z10;
        v1();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public View L0(View view, int i10, RecyclerView.v vVar, RecyclerView.z zVar) {
        View B;
        int l22;
        int g6;
        int g10;
        int g11;
        View m10;
        if (J() == 0 || (B = B(view)) == null) {
            return null;
        }
        G2();
        int Z1 = Z1(i10);
        if (Z1 == Integer.MIN_VALUE) {
            return null;
        }
        LayoutParams layoutParams = (LayoutParams) B.getLayoutParams();
        boolean z10 = layoutParams.f3566f;
        c cVar = layoutParams.f3565e;
        if (Z1 == 1) {
            l22 = m2();
        } else {
            l22 = l2();
        }
        Q2(l22, zVar);
        I2(Z1);
        LayoutState layoutState = this.f3563y;
        layoutState.f3780c = layoutState.f3781d + l22;
        layoutState.f3779b = (int) (this.f3559u.o() * 0.33333334f);
        LayoutState layoutState2 = this.f3563y;
        layoutState2.f3785h = true;
        layoutState2.f3778a = false;
        d2(vVar, layoutState2, zVar);
        this.G = this.A;
        if (!z10 && (m10 = cVar.m(l22, Z1)) != null && m10 != B) {
            return m10;
        }
        if (z2(Z1)) {
            for (int i11 = this.f3557s - 1; i11 >= 0; i11--) {
                View m11 = this.f3558t[i11].m(l22, Z1);
                if (m11 != null && m11 != B) {
                    return m11;
                }
            }
        } else {
            for (int i12 = 0; i12 < this.f3557s; i12++) {
                View m12 = this.f3558t[i12].m(l22, Z1);
                if (m12 != null && m12 != B) {
                    return m12;
                }
            }
        }
        boolean z11 = (this.f3564z ^ true) == (Z1 == -1);
        if (!z10) {
            if (z11) {
                g11 = cVar.f();
            } else {
                g11 = cVar.g();
            }
            View C = C(g11);
            if (C != null && C != B) {
                return C;
            }
        }
        if (z2(Z1)) {
            for (int i13 = this.f3557s - 1; i13 >= 0; i13--) {
                if (i13 != cVar.f3595e) {
                    if (z11) {
                        g10 = this.f3558t[i13].f();
                    } else {
                        g10 = this.f3558t[i13].g();
                    }
                    View C2 = C(g10);
                    if (C2 != null && C2 != B) {
                        return C2;
                    }
                }
            }
        } else {
            for (int i14 = 0; i14 < this.f3557s; i14++) {
                if (z11) {
                    g6 = this.f3558t[i14].f();
                } else {
                    g6 = this.f3558t[i14].g();
                }
                View C3 = C(g6);
                if (C3 != null && C3 != B) {
                    return C3;
                }
            }
        }
        return null;
    }

    public void L2(int i10) {
        g(null);
        if (i10 != this.f3557s) {
            u2();
            this.f3557s = i10;
            this.B = new BitSet(this.f3557s);
            this.f3558t = new c[this.f3557s];
            for (int i11 = 0; i11 < this.f3557s; i11++) {
                this.f3558t[i11] = new c(i11);
            }
            v1();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void M0(AccessibilityEvent accessibilityEvent) {
        super.M0(accessibilityEvent);
        if (J() > 0) {
            View g22 = g2(false);
            View f22 = f2(false);
            if (g22 == null || f22 == null) {
                return;
            }
            int j02 = j0(g22);
            int j03 = j0(f22);
            if (j02 < j03) {
                accessibilityEvent.setFromIndex(j02);
                accessibilityEvent.setToIndex(j03);
            } else {
                accessibilityEvent.setFromIndex(j03);
                accessibilityEvent.setToIndex(j02);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public boolean N1() {
        return this.I == null;
    }

    boolean O2(RecyclerView.z zVar, b bVar) {
        int i10;
        int n10;
        if (!zVar.e() && (i10 = this.C) != -1) {
            if (i10 >= 0 && i10 < zVar.b()) {
                SavedState savedState = this.I;
                if (savedState != null && savedState.f3573e != -1 && savedState.f3575g >= 1) {
                    bVar.f3585b = Integer.MIN_VALUE;
                    bVar.f3584a = this.C;
                } else {
                    View C = C(this.C);
                    if (C != null) {
                        bVar.f3584a = this.A ? m2() : l2();
                        if (this.D != Integer.MIN_VALUE) {
                            if (bVar.f3586c) {
                                bVar.f3585b = (this.f3559u.i() - this.D) - this.f3559u.d(C);
                            } else {
                                bVar.f3585b = (this.f3559u.n() + this.D) - this.f3559u.g(C);
                            }
                            return true;
                        }
                        if (this.f3559u.e(C) > this.f3559u.o()) {
                            if (bVar.f3586c) {
                                n10 = this.f3559u.i();
                            } else {
                                n10 = this.f3559u.n();
                            }
                            bVar.f3585b = n10;
                            return true;
                        }
                        int g6 = this.f3559u.g(C) - this.f3559u.n();
                        if (g6 < 0) {
                            bVar.f3585b = -g6;
                            return true;
                        }
                        int i11 = this.f3559u.i() - this.f3559u.d(C);
                        if (i11 < 0) {
                            bVar.f3585b = i11;
                            return true;
                        }
                        bVar.f3585b = Integer.MIN_VALUE;
                    } else {
                        int i12 = this.C;
                        bVar.f3584a = i12;
                        int i13 = this.D;
                        if (i13 == Integer.MIN_VALUE) {
                            bVar.f3586c = T1(i12) == 1;
                            bVar.a();
                        } else {
                            bVar.b(i13);
                        }
                        bVar.f3587d = true;
                    }
                }
                return true;
            }
            this.C = -1;
            this.D = Integer.MIN_VALUE;
        }
        return false;
    }

    void P2(RecyclerView.z zVar, b bVar) {
        if (O2(zVar, bVar) || N2(zVar, bVar)) {
            return;
        }
        bVar.a();
        bVar.f3584a = 0;
    }

    boolean Q1() {
        int l10 = this.f3558t[0].l(Integer.MIN_VALUE);
        for (int i10 = 1; i10 < this.f3557s; i10++) {
            if (this.f3558t[i10].l(Integer.MIN_VALUE) != l10) {
                return false;
            }
        }
        return true;
    }

    boolean R1() {
        int p10 = this.f3558t[0].p(Integer.MIN_VALUE);
        for (int i10 = 1; i10 < this.f3557s; i10++) {
            if (this.f3558t[i10].p(Integer.MIN_VALUE) != p10) {
                return false;
            }
        }
        return true;
    }

    void R2(int i10) {
        this.f3562x = i10 / this.f3557s;
        this.J = View.MeasureSpec.makeMeasureSpec(i10, this.f3560v.l());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void T0(RecyclerView recyclerView, int i10, int i11) {
        s2(i10, i11, 1);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void U0(RecyclerView recyclerView) {
        this.E.b();
        v1();
    }

    boolean U1() {
        int l22;
        int m22;
        if (J() == 0 || this.F == 0 || !t0()) {
            return false;
        }
        if (this.A) {
            l22 = m2();
            m22 = l2();
        } else {
            l22 = l2();
            m22 = m2();
        }
        if (l22 == 0 && t2() != null) {
            this.E.b();
            w1();
            v1();
            return true;
        }
        if (!this.M) {
            return false;
        }
        int i10 = this.A ? -1 : 1;
        int i11 = m22 + 1;
        LazySpanLookup.FullSpanItem e10 = this.E.e(l22, i11, i10, true);
        if (e10 == null) {
            this.M = false;
            this.E.d(i11);
            return false;
        }
        LazySpanLookup.FullSpanItem e11 = this.E.e(l22, e10.f3569e, i10 * (-1), true);
        if (e11 == null) {
            this.E.d(e10.f3569e);
        } else {
            this.E.d(e11.f3569e + 1);
        }
        w1();
        v1();
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void V0(RecyclerView recyclerView, int i10, int i11, int i12) {
        s2(i10, i11, 8);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void W0(RecyclerView recyclerView, int i10, int i11) {
        s2(i10, i11, 2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void Y0(RecyclerView recyclerView, int i10, int i11, Object obj) {
        s2(i10, i11, 4);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void Z0(RecyclerView.v vVar, RecyclerView.z zVar) {
        y2(vVar, zVar, true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.y.b
    public PointF a(int i10) {
        int T1 = T1(i10);
        PointF pointF = new PointF();
        if (T1 == 0) {
            return null;
        }
        if (this.f3561w == 0) {
            pointF.x = T1;
            pointF.y = 0.0f;
        } else {
            pointF.x = 0.0f;
            pointF.y = T1;
        }
        return pointF;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void a1(RecyclerView.z zVar) {
        super.a1(zVar);
        this.C = -1;
        this.D = Integer.MIN_VALUE;
        this.I = null;
        this.L.c();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void e1(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            this.I = savedState;
            if (this.C != -1) {
                savedState.j();
                this.I.k();
            }
            v1();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public Parcelable f1() {
        int p10;
        int n10;
        int[] iArr;
        if (this.I != null) {
            return new SavedState(this.I);
        }
        SavedState savedState = new SavedState();
        savedState.f3580l = this.f3564z;
        savedState.f3581m = this.G;
        savedState.f3582n = this.H;
        LazySpanLookup lazySpanLookup = this.E;
        if (lazySpanLookup != null && (iArr = lazySpanLookup.f3567a) != null) {
            savedState.f3578j = iArr;
            savedState.f3577i = iArr.length;
            savedState.f3579k = lazySpanLookup.f3568b;
        } else {
            savedState.f3577i = 0;
        }
        if (J() > 0) {
            savedState.f3573e = this.G ? m2() : l2();
            savedState.f3574f = h2();
            int i10 = this.f3557s;
            savedState.f3575g = i10;
            savedState.f3576h = new int[i10];
            for (int i11 = 0; i11 < this.f3557s; i11++) {
                if (this.G) {
                    p10 = this.f3558t[i11].l(Integer.MIN_VALUE);
                    if (p10 != Integer.MIN_VALUE) {
                        n10 = this.f3559u.i();
                        p10 -= n10;
                        savedState.f3576h[i11] = p10;
                    } else {
                        savedState.f3576h[i11] = p10;
                    }
                } else {
                    p10 = this.f3558t[i11].p(Integer.MIN_VALUE);
                    if (p10 != Integer.MIN_VALUE) {
                        n10 = this.f3559u.n();
                        p10 -= n10;
                        savedState.f3576h[i11] = p10;
                    } else {
                        savedState.f3576h[i11] = p10;
                    }
                }
            }
        } else {
            savedState.f3573e = -1;
            savedState.f3574f = -1;
            savedState.f3575g = 0;
        }
        return savedState;
    }

    View f2(boolean z10) {
        int n10 = this.f3559u.n();
        int i10 = this.f3559u.i();
        View view = null;
        for (int J = J() - 1; J >= 0; J--) {
            View I = I(J);
            int g6 = this.f3559u.g(I);
            int d10 = this.f3559u.d(I);
            if (d10 > n10 && g6 < i10) {
                if (d10 <= i10 || !z10) {
                    return I;
                }
                if (view == null) {
                    view = I;
                }
            }
        }
        return view;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void g(String str) {
        if (this.I == null) {
            super.g(str);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void g1(int i10) {
        if (i10 == 0) {
            U1();
        }
    }

    View g2(boolean z10) {
        int n10 = this.f3559u.n();
        int i10 = this.f3559u.i();
        int J = J();
        View view = null;
        for (int i11 = 0; i11 < J; i11++) {
            View I = I(i11);
            int g6 = this.f3559u.g(I);
            if (this.f3559u.d(I) > n10 && g6 < i10) {
                if (g6 >= n10 || !z10) {
                    return I;
                }
                if (view == null) {
                    view = I;
                }
            }
        }
        return view;
    }

    int h2() {
        View f22 = this.A ? f2(true) : g2(true);
        if (f22 == null) {
            return -1;
        }
        return j0(f22);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public boolean k() {
        return this.f3561w == 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public boolean l() {
        return this.f3561w == 1;
    }

    int l2() {
        if (J() == 0) {
            return 0;
        }
        return j0(I(0));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public boolean m(RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    int m2() {
        int J = J();
        if (J == 0) {
            return 0;
        }
        return j0(I(J - 1));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void o(int i10, int i11, RecyclerView.z zVar, RecyclerView.p.c cVar) {
        int l10;
        int i12;
        if (this.f3561w != 0) {
            i10 = i11;
        }
        if (J() == 0 || i10 == 0) {
            return;
        }
        A2(i10, zVar);
        int[] iArr = this.O;
        if (iArr == null || iArr.length < this.f3557s) {
            this.O = new int[this.f3557s];
        }
        int i13 = 0;
        for (int i14 = 0; i14 < this.f3557s; i14++) {
            LayoutState layoutState = this.f3563y;
            if (layoutState.f3781d == -1) {
                l10 = layoutState.f3783f;
                i12 = this.f3558t[i14].p(l10);
            } else {
                l10 = this.f3558t[i14].l(layoutState.f3784g);
                i12 = this.f3563y.f3784g;
            }
            int i15 = l10 - i12;
            if (i15 >= 0) {
                this.O[i13] = i15;
                i13++;
            }
        }
        Arrays.sort(this.O, 0, i13);
        for (int i16 = 0; i16 < i13 && this.f3563y.a(zVar); i16++) {
            cVar.a(this.f3563y.f3780c, this.O[i16]);
            LayoutState layoutState2 = this.f3563y;
            layoutState2.f3780c += layoutState2.f3781d;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int q(RecyclerView.z zVar) {
        return W1(zVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int r(RecyclerView.z zVar) {
        return X1(zVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int s(RecyclerView.z zVar) {
        return Y1(zVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int t(RecyclerView.z zVar) {
        return W1(zVar);
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0074, code lost:
    
        if (r10 == r11) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x008a, code lost:
    
        r10 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0088, code lost:
    
        r10 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0086, code lost:
    
        if (r10 == r11) goto L37;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    View t2() {
        int i10;
        int i11;
        boolean z10;
        int J = J() - 1;
        BitSet bitSet = new BitSet(this.f3557s);
        bitSet.set(0, this.f3557s, true);
        char c10 = (this.f3561w == 1 && v2()) ? (char) 1 : (char) 65535;
        if (this.A) {
            i10 = -1;
        } else {
            i10 = J + 1;
            J = 0;
        }
        int i12 = J < i10 ? 1 : -1;
        while (J != i10) {
            View I = I(J);
            LayoutParams layoutParams = (LayoutParams) I.getLayoutParams();
            if (bitSet.get(layoutParams.f3565e.f3595e)) {
                if (V1(layoutParams.f3565e)) {
                    return I;
                }
                bitSet.clear(layoutParams.f3565e.f3595e);
            }
            if (!layoutParams.f3566f && (i11 = J + i12) != i10) {
                View I2 = I(i11);
                if (this.A) {
                    int d10 = this.f3559u.d(I);
                    int d11 = this.f3559u.d(I2);
                    if (d10 < d11) {
                        return I;
                    }
                } else {
                    int g6 = this.f3559u.g(I);
                    int g10 = this.f3559u.g(I2);
                    if (g6 > g10) {
                        return I;
                    }
                }
                if (z10) {
                    if ((layoutParams.f3565e.f3595e - ((LayoutParams) I2.getLayoutParams()).f3565e.f3595e < 0) != (c10 < 0)) {
                        return I;
                    }
                } else {
                    continue;
                }
            }
            J += i12;
        }
        return null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int u(RecyclerView.z zVar) {
        return X1(zVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public boolean u0() {
        return this.F != 0;
    }

    public void u2() {
        this.E.b();
        v1();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int v(RecyclerView.z zVar) {
        return Y1(zVar);
    }

    boolean v2() {
        return Z() == 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int y1(int i10, RecyclerView.v vVar, RecyclerView.z zVar) {
        return H2(i10, vVar, zVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void z1(int i10) {
        SavedState savedState = this.I;
        if (savedState != null && savedState.f3573e != i10) {
            savedState.j();
        }
        this.C = i10;
        this.D = Integer.MIN_VALUE;
        v1();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class LazySpanLookup {

        /* renamed from: a, reason: collision with root package name */
        int[] f3567a;

        /* renamed from: b, reason: collision with root package name */
        List<FullSpanItem> f3568b;

        LazySpanLookup() {
        }

        private int i(int i10) {
            if (this.f3568b == null) {
                return -1;
            }
            FullSpanItem f10 = f(i10);
            if (f10 != null) {
                this.f3568b.remove(f10);
            }
            int size = this.f3568b.size();
            int i11 = 0;
            while (true) {
                if (i11 >= size) {
                    i11 = -1;
                    break;
                }
                if (this.f3568b.get(i11).f3569e >= i10) {
                    break;
                }
                i11++;
            }
            if (i11 == -1) {
                return -1;
            }
            FullSpanItem fullSpanItem = this.f3568b.get(i11);
            this.f3568b.remove(i11);
            return fullSpanItem.f3569e;
        }

        private void l(int i10, int i11) {
            List<FullSpanItem> list = this.f3568b;
            if (list == null) {
                return;
            }
            for (int size = list.size() - 1; size >= 0; size--) {
                FullSpanItem fullSpanItem = this.f3568b.get(size);
                int i12 = fullSpanItem.f3569e;
                if (i12 >= i10) {
                    fullSpanItem.f3569e = i12 + i11;
                }
            }
        }

        private void m(int i10, int i11) {
            List<FullSpanItem> list = this.f3568b;
            if (list == null) {
                return;
            }
            int i12 = i10 + i11;
            for (int size = list.size() - 1; size >= 0; size--) {
                FullSpanItem fullSpanItem = this.f3568b.get(size);
                int i13 = fullSpanItem.f3569e;
                if (i13 >= i10) {
                    if (i13 < i12) {
                        this.f3568b.remove(size);
                    } else {
                        fullSpanItem.f3569e = i13 - i11;
                    }
                }
            }
        }

        public void a(FullSpanItem fullSpanItem) {
            if (this.f3568b == null) {
                this.f3568b = new ArrayList();
            }
            int size = this.f3568b.size();
            for (int i10 = 0; i10 < size; i10++) {
                FullSpanItem fullSpanItem2 = this.f3568b.get(i10);
                if (fullSpanItem2.f3569e == fullSpanItem.f3569e) {
                    this.f3568b.remove(i10);
                }
                if (fullSpanItem2.f3569e >= fullSpanItem.f3569e) {
                    this.f3568b.add(i10, fullSpanItem);
                    return;
                }
            }
            this.f3568b.add(fullSpanItem);
        }

        void b() {
            int[] iArr = this.f3567a;
            if (iArr != null) {
                Arrays.fill(iArr, -1);
            }
            this.f3568b = null;
        }

        void c(int i10) {
            int[] iArr = this.f3567a;
            if (iArr == null) {
                int[] iArr2 = new int[Math.max(i10, 10) + 1];
                this.f3567a = iArr2;
                Arrays.fill(iArr2, -1);
            } else if (i10 >= iArr.length) {
                int[] iArr3 = new int[o(i10)];
                this.f3567a = iArr3;
                System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
                int[] iArr4 = this.f3567a;
                Arrays.fill(iArr4, iArr.length, iArr4.length, -1);
            }
        }

        int d(int i10) {
            List<FullSpanItem> list = this.f3568b;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    if (this.f3568b.get(size).f3569e >= i10) {
                        this.f3568b.remove(size);
                    }
                }
            }
            return h(i10);
        }

        public FullSpanItem e(int i10, int i11, int i12, boolean z10) {
            List<FullSpanItem> list = this.f3568b;
            if (list == null) {
                return null;
            }
            int size = list.size();
            for (int i13 = 0; i13 < size; i13++) {
                FullSpanItem fullSpanItem = this.f3568b.get(i13);
                int i14 = fullSpanItem.f3569e;
                if (i14 >= i11) {
                    return null;
                }
                if (i14 >= i10 && (i12 == 0 || fullSpanItem.f3570f == i12 || (z10 && fullSpanItem.f3572h))) {
                    return fullSpanItem;
                }
            }
            return null;
        }

        public FullSpanItem f(int i10) {
            List<FullSpanItem> list = this.f3568b;
            if (list == null) {
                return null;
            }
            for (int size = list.size() - 1; size >= 0; size--) {
                FullSpanItem fullSpanItem = this.f3568b.get(size);
                if (fullSpanItem.f3569e == i10) {
                    return fullSpanItem;
                }
            }
            return null;
        }

        int g(int i10) {
            int[] iArr = this.f3567a;
            if (iArr == null || i10 >= iArr.length) {
                return -1;
            }
            return iArr[i10];
        }

        int h(int i10) {
            int[] iArr = this.f3567a;
            if (iArr == null || i10 >= iArr.length) {
                return -1;
            }
            int i11 = i(i10);
            if (i11 == -1) {
                int[] iArr2 = this.f3567a;
                Arrays.fill(iArr2, i10, iArr2.length, -1);
                return this.f3567a.length;
            }
            int min = Math.min(i11 + 1, this.f3567a.length);
            Arrays.fill(this.f3567a, i10, min, -1);
            return min;
        }

        void j(int i10, int i11) {
            int[] iArr = this.f3567a;
            if (iArr == null || i10 >= iArr.length) {
                return;
            }
            int i12 = i10 + i11;
            c(i12);
            int[] iArr2 = this.f3567a;
            System.arraycopy(iArr2, i10, iArr2, i12, (iArr2.length - i10) - i11);
            Arrays.fill(this.f3567a, i10, i12, -1);
            l(i10, i11);
        }

        void k(int i10, int i11) {
            int[] iArr = this.f3567a;
            if (iArr == null || i10 >= iArr.length) {
                return;
            }
            int i12 = i10 + i11;
            c(i12);
            int[] iArr2 = this.f3567a;
            System.arraycopy(iArr2, i12, iArr2, i10, (iArr2.length - i10) - i11);
            int[] iArr3 = this.f3567a;
            Arrays.fill(iArr3, iArr3.length - i11, iArr3.length, -1);
            m(i10, i11);
        }

        void n(int i10, c cVar) {
            c(i10);
            this.f3567a[i10] = cVar.f3595e;
        }

        int o(int i10) {
            int length = this.f3567a.length;
            while (length <= i10) {
                length *= 2;
            }
            return length;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @SuppressLint({"BanParcelableUsage"})
        /* loaded from: classes.dex */
        public static class FullSpanItem implements Parcelable {
            public static final Parcelable.Creator<FullSpanItem> CREATOR = new a();

            /* renamed from: e, reason: collision with root package name */
            int f3569e;

            /* renamed from: f, reason: collision with root package name */
            int f3570f;

            /* renamed from: g, reason: collision with root package name */
            int[] f3571g;

            /* renamed from: h, reason: collision with root package name */
            boolean f3572h;

            /* loaded from: classes.dex */
            class a implements Parcelable.Creator<FullSpanItem> {
                a() {
                }

                @Override // android.os.Parcelable.Creator
                /* renamed from: a, reason: merged with bridge method [inline-methods] */
                public FullSpanItem createFromParcel(Parcel parcel) {
                    return new FullSpanItem(parcel);
                }

                @Override // android.os.Parcelable.Creator
                /* renamed from: b, reason: merged with bridge method [inline-methods] */
                public FullSpanItem[] newArray(int i10) {
                    return new FullSpanItem[i10];
                }
            }

            FullSpanItem(Parcel parcel) {
                this.f3569e = parcel.readInt();
                this.f3570f = parcel.readInt();
                this.f3572h = parcel.readInt() == 1;
                int readInt = parcel.readInt();
                if (readInt > 0) {
                    int[] iArr = new int[readInt];
                    this.f3571g = iArr;
                    parcel.readIntArray(iArr);
                }
            }

            @Override // android.os.Parcelable
            public int describeContents() {
                return 0;
            }

            int j(int i10) {
                int[] iArr = this.f3571g;
                if (iArr == null) {
                    return 0;
                }
                return iArr[i10];
            }

            public String toString() {
                return "FullSpanItem{mPosition=" + this.f3569e + ", mGapDir=" + this.f3570f + ", mHasUnwantedGapAfter=" + this.f3572h + ", mGapPerSpan=" + Arrays.toString(this.f3571g) + '}';
            }

            @Override // android.os.Parcelable
            public void writeToParcel(Parcel parcel, int i10) {
                parcel.writeInt(this.f3569e);
                parcel.writeInt(this.f3570f);
                parcel.writeInt(this.f3572h ? 1 : 0);
                int[] iArr = this.f3571g;
                if (iArr != null && iArr.length > 0) {
                    parcel.writeInt(iArr.length);
                    parcel.writeIntArray(this.f3571g);
                } else {
                    parcel.writeInt(0);
                }
            }

            FullSpanItem() {
            }
        }
    }
}
