package androidx.recyclerview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* loaded from: classes.dex */
public class LinearLayoutManager extends RecyclerView.p implements RecyclerView.y.b {
    int A;
    int B;
    private boolean C;
    SavedState D;
    final a E;
    private final b F;
    private int G;
    private int[] H;

    /* renamed from: s, reason: collision with root package name */
    int f3420s;

    /* renamed from: t, reason: collision with root package name */
    private c f3421t;

    /* renamed from: u, reason: collision with root package name */
    OrientationHelper f3422u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f3423v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f3424w;

    /* renamed from: x, reason: collision with root package name */
    boolean f3425x;

    /* renamed from: y, reason: collision with root package name */
    private boolean f3426y;

    /* renamed from: z, reason: collision with root package name */
    private boolean f3427z;

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f3428e;

        /* renamed from: f, reason: collision with root package name */
        int f3429f;

        /* renamed from: g, reason: collision with root package name */
        boolean f3430g;

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

        boolean j() {
            return this.f3428e >= 0;
        }

        void k() {
            this.f3428e = -1;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            parcel.writeInt(this.f3428e);
            parcel.writeInt(this.f3429f);
            parcel.writeInt(this.f3430g ? 1 : 0);
        }

        SavedState(Parcel parcel) {
            this.f3428e = parcel.readInt();
            this.f3429f = parcel.readInt();
            this.f3430g = parcel.readInt() == 1;
        }

        public SavedState(SavedState savedState) {
            this.f3428e = savedState.f3428e;
            this.f3429f = savedState.f3429f;
            this.f3430g = savedState.f3430g;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        OrientationHelper f3431a;

        /* renamed from: b, reason: collision with root package name */
        int f3432b;

        /* renamed from: c, reason: collision with root package name */
        int f3433c;

        /* renamed from: d, reason: collision with root package name */
        boolean f3434d;

        /* renamed from: e, reason: collision with root package name */
        boolean f3435e;

        a() {
            e();
        }

        void a() {
            int n10;
            if (this.f3434d) {
                n10 = this.f3431a.i();
            } else {
                n10 = this.f3431a.n();
            }
            this.f3433c = n10;
        }

        public void b(View view, int i10) {
            if (this.f3434d) {
                this.f3433c = this.f3431a.d(view) + this.f3431a.p();
            } else {
                this.f3433c = this.f3431a.g(view);
            }
            this.f3432b = i10;
        }

        public void c(View view, int i10) {
            int p10 = this.f3431a.p();
            if (p10 >= 0) {
                b(view, i10);
                return;
            }
            this.f3432b = i10;
            if (this.f3434d) {
                int i11 = (this.f3431a.i() - p10) - this.f3431a.d(view);
                this.f3433c = this.f3431a.i() - i11;
                if (i11 > 0) {
                    int e10 = this.f3433c - this.f3431a.e(view);
                    int n10 = this.f3431a.n();
                    int min = e10 - (n10 + Math.min(this.f3431a.g(view) - n10, 0));
                    if (min < 0) {
                        this.f3433c += Math.min(i11, -min);
                        return;
                    }
                    return;
                }
                return;
            }
            int g6 = this.f3431a.g(view);
            int n11 = g6 - this.f3431a.n();
            this.f3433c = g6;
            if (n11 > 0) {
                int i12 = (this.f3431a.i() - Math.min(0, (this.f3431a.i() - p10) - this.f3431a.d(view))) - (g6 + this.f3431a.e(view));
                if (i12 < 0) {
                    this.f3433c -= Math.min(n11, -i12);
                }
            }
        }

        boolean d(View view, RecyclerView.z zVar) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            return !layoutParams.c() && layoutParams.a() >= 0 && layoutParams.a() < zVar.b();
        }

        void e() {
            this.f3432b = -1;
            this.f3433c = Integer.MIN_VALUE;
            this.f3434d = false;
            this.f3435e = false;
        }

        public String toString() {
            return "AnchorInfo{mPosition=" + this.f3432b + ", mCoordinate=" + this.f3433c + ", mLayoutFromEnd=" + this.f3434d + ", mValid=" + this.f3435e + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        public int f3436a;

        /* renamed from: b, reason: collision with root package name */
        public boolean f3437b;

        /* renamed from: c, reason: collision with root package name */
        public boolean f3438c;

        /* renamed from: d, reason: collision with root package name */
        public boolean f3439d;

        protected b() {
        }

        void a() {
            this.f3436a = 0;
            this.f3437b = false;
            this.f3438c = false;
            this.f3439d = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: b, reason: collision with root package name */
        int f3441b;

        /* renamed from: c, reason: collision with root package name */
        int f3442c;

        /* renamed from: d, reason: collision with root package name */
        int f3443d;

        /* renamed from: e, reason: collision with root package name */
        int f3444e;

        /* renamed from: f, reason: collision with root package name */
        int f3445f;

        /* renamed from: g, reason: collision with root package name */
        int f3446g;

        /* renamed from: k, reason: collision with root package name */
        int f3450k;

        /* renamed from: m, reason: collision with root package name */
        boolean f3452m;

        /* renamed from: a, reason: collision with root package name */
        boolean f3440a = true;

        /* renamed from: h, reason: collision with root package name */
        int f3447h = 0;

        /* renamed from: i, reason: collision with root package name */
        int f3448i = 0;

        /* renamed from: j, reason: collision with root package name */
        boolean f3449j = false;

        /* renamed from: l, reason: collision with root package name */
        List<RecyclerView.c0> f3451l = null;

        c() {
        }

        private View e() {
            int size = this.f3451l.size();
            for (int i10 = 0; i10 < size; i10++) {
                View view = this.f3451l.get(i10).itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (!layoutParams.c() && this.f3443d == layoutParams.a()) {
                    b(view);
                    return view;
                }
            }
            return null;
        }

        public void a() {
            b(null);
        }

        public void b(View view) {
            View f10 = f(view);
            if (f10 == null) {
                this.f3443d = -1;
            } else {
                this.f3443d = ((RecyclerView.LayoutParams) f10.getLayoutParams()).a();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean c(RecyclerView.z zVar) {
            int i10 = this.f3443d;
            return i10 >= 0 && i10 < zVar.b();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public View d(RecyclerView.v vVar) {
            if (this.f3451l != null) {
                return e();
            }
            View o10 = vVar.o(this.f3443d);
            this.f3443d += this.f3444e;
            return o10;
        }

        public View f(View view) {
            int a10;
            int size = this.f3451l.size();
            View view2 = null;
            int i10 = Integer.MAX_VALUE;
            for (int i11 = 0; i11 < size; i11++) {
                View view3 = this.f3451l.get(i11).itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view3.getLayoutParams();
                if (view3 != view && !layoutParams.c() && (a10 = (layoutParams.a() - this.f3443d) * this.f3444e) >= 0 && a10 < i10) {
                    view2 = view3;
                    if (a10 == 0) {
                        break;
                    }
                    i10 = a10;
                }
            }
            return view2;
        }
    }

    public LinearLayoutManager(Context context) {
        this(context, 1, false);
    }

    private void A2() {
        if (this.f3420s != 1 && q2()) {
            this.f3425x = !this.f3424w;
        } else {
            this.f3425x = this.f3424w;
        }
    }

    private boolean F2(RecyclerView.v vVar, RecyclerView.z zVar, a aVar) {
        View j22;
        boolean z10 = false;
        if (J() == 0) {
            return false;
        }
        View V = V();
        if (V != null && aVar.d(V, zVar)) {
            aVar.c(V, j0(V));
            return true;
        }
        boolean z11 = this.f3423v;
        boolean z12 = this.f3426y;
        if (z11 != z12 || (j22 = j2(vVar, zVar, aVar.f3434d, z12)) == null) {
            return false;
        }
        aVar.b(j22, j0(j22));
        if (!zVar.e() && N1()) {
            int g6 = this.f3422u.g(j22);
            int d10 = this.f3422u.d(j22);
            int n10 = this.f3422u.n();
            int i10 = this.f3422u.i();
            boolean z13 = d10 <= n10 && g6 < n10;
            if (g6 >= i10 && d10 > i10) {
                z10 = true;
            }
            if (z13 || z10) {
                if (aVar.f3434d) {
                    n10 = i10;
                }
                aVar.f3433c = n10;
            }
        }
        return true;
    }

    private boolean G2(RecyclerView.z zVar, a aVar) {
        int i10;
        int g6;
        if (!zVar.e() && (i10 = this.A) != -1) {
            if (i10 >= 0 && i10 < zVar.b()) {
                aVar.f3432b = this.A;
                SavedState savedState = this.D;
                if (savedState != null && savedState.j()) {
                    boolean z10 = this.D.f3430g;
                    aVar.f3434d = z10;
                    if (z10) {
                        aVar.f3433c = this.f3422u.i() - this.D.f3429f;
                    } else {
                        aVar.f3433c = this.f3422u.n() + this.D.f3429f;
                    }
                    return true;
                }
                if (this.B == Integer.MIN_VALUE) {
                    View C = C(this.A);
                    if (C != null) {
                        if (this.f3422u.e(C) > this.f3422u.o()) {
                            aVar.a();
                            return true;
                        }
                        if (this.f3422u.g(C) - this.f3422u.n() < 0) {
                            aVar.f3433c = this.f3422u.n();
                            aVar.f3434d = false;
                            return true;
                        }
                        if (this.f3422u.i() - this.f3422u.d(C) < 0) {
                            aVar.f3433c = this.f3422u.i();
                            aVar.f3434d = true;
                            return true;
                        }
                        if (aVar.f3434d) {
                            g6 = this.f3422u.d(C) + this.f3422u.p();
                        } else {
                            g6 = this.f3422u.g(C);
                        }
                        aVar.f3433c = g6;
                    } else {
                        if (J() > 0) {
                            aVar.f3434d = (this.A < j0(I(0))) == this.f3425x;
                        }
                        aVar.a();
                    }
                    return true;
                }
                boolean z11 = this.f3425x;
                aVar.f3434d = z11;
                if (z11) {
                    aVar.f3433c = this.f3422u.i() - this.B;
                } else {
                    aVar.f3433c = this.f3422u.n() + this.B;
                }
                return true;
            }
            this.A = -1;
            this.B = Integer.MIN_VALUE;
        }
        return false;
    }

    private void H2(RecyclerView.v vVar, RecyclerView.z zVar, a aVar) {
        if (G2(zVar, aVar) || F2(vVar, zVar, aVar)) {
            return;
        }
        aVar.a();
        aVar.f3432b = this.f3426y ? zVar.b() - 1 : 0;
    }

    private void I2(int i10, int i11, boolean z10, RecyclerView.z zVar) {
        int n10;
        this.f3421t.f3452m = z2();
        this.f3421t.f3445f = i10;
        int[] iArr = this.H;
        iArr[0] = 0;
        iArr[1] = 0;
        O1(zVar, iArr);
        int max = Math.max(0, this.H[0]);
        int max2 = Math.max(0, this.H[1]);
        boolean z11 = i10 == 1;
        c cVar = this.f3421t;
        int i12 = z11 ? max2 : max;
        cVar.f3447h = i12;
        if (!z11) {
            max = max2;
        }
        cVar.f3448i = max;
        if (z11) {
            cVar.f3447h = i12 + this.f3422u.j();
            View m22 = m2();
            c cVar2 = this.f3421t;
            cVar2.f3444e = this.f3425x ? -1 : 1;
            int j02 = j0(m22);
            c cVar3 = this.f3421t;
            cVar2.f3443d = j02 + cVar3.f3444e;
            cVar3.f3441b = this.f3422u.d(m22);
            n10 = this.f3422u.d(m22) - this.f3422u.i();
        } else {
            View n22 = n2();
            this.f3421t.f3447h += this.f3422u.n();
            c cVar4 = this.f3421t;
            cVar4.f3444e = this.f3425x ? 1 : -1;
            int j03 = j0(n22);
            c cVar5 = this.f3421t;
            cVar4.f3443d = j03 + cVar5.f3444e;
            cVar5.f3441b = this.f3422u.g(n22);
            n10 = (-this.f3422u.g(n22)) + this.f3422u.n();
        }
        c cVar6 = this.f3421t;
        cVar6.f3442c = i11;
        if (z10) {
            cVar6.f3442c = i11 - n10;
        }
        cVar6.f3446g = n10;
    }

    private void J2(int i10, int i11) {
        this.f3421t.f3442c = this.f3422u.i() - i11;
        c cVar = this.f3421t;
        cVar.f3444e = this.f3425x ? -1 : 1;
        cVar.f3443d = i10;
        cVar.f3445f = 1;
        cVar.f3441b = i11;
        cVar.f3446g = Integer.MIN_VALUE;
    }

    private void K2(a aVar) {
        J2(aVar.f3432b, aVar.f3433c);
    }

    private void L2(int i10, int i11) {
        this.f3421t.f3442c = i11 - this.f3422u.n();
        c cVar = this.f3421t;
        cVar.f3443d = i10;
        cVar.f3444e = this.f3425x ? 1 : -1;
        cVar.f3445f = -1;
        cVar.f3441b = i11;
        cVar.f3446g = Integer.MIN_VALUE;
    }

    private void M2(a aVar) {
        L2(aVar.f3432b, aVar.f3433c);
    }

    private int Q1(RecyclerView.z zVar) {
        if (J() == 0) {
            return 0;
        }
        V1();
        return ScrollbarHelper.a(zVar, this.f3422u, a2(!this.f3427z, true), Z1(!this.f3427z, true), this, this.f3427z);
    }

    private int R1(RecyclerView.z zVar) {
        if (J() == 0) {
            return 0;
        }
        V1();
        return ScrollbarHelper.b(zVar, this.f3422u, a2(!this.f3427z, true), Z1(!this.f3427z, true), this, this.f3427z, this.f3425x);
    }

    private int S1(RecyclerView.z zVar) {
        if (J() == 0) {
            return 0;
        }
        V1();
        return ScrollbarHelper.c(zVar, this.f3422u, a2(!this.f3427z, true), Z1(!this.f3427z, true), this, this.f3427z);
    }

    private View Y1() {
        return f2(0, J());
    }

    private View d2() {
        return f2(J() - 1, -1);
    }

    private View h2() {
        return this.f3425x ? Y1() : d2();
    }

    private View i2() {
        return this.f3425x ? d2() : Y1();
    }

    private int k2(int i10, RecyclerView.v vVar, RecyclerView.z zVar, boolean z10) {
        int i11;
        int i12 = this.f3422u.i() - i10;
        if (i12 <= 0) {
            return 0;
        }
        int i13 = -B2(-i12, vVar, zVar);
        int i14 = i10 + i13;
        if (!z10 || (i11 = this.f3422u.i() - i14) <= 0) {
            return i13;
        }
        this.f3422u.s(i11);
        return i11 + i13;
    }

    private int l2(int i10, RecyclerView.v vVar, RecyclerView.z zVar, boolean z10) {
        int n10;
        int n11 = i10 - this.f3422u.n();
        if (n11 <= 0) {
            return 0;
        }
        int i11 = -B2(n11, vVar, zVar);
        int i12 = i10 + i11;
        if (!z10 || (n10 = i12 - this.f3422u.n()) <= 0) {
            return i11;
        }
        this.f3422u.s(-n10);
        return i11 - n10;
    }

    private View m2() {
        return I(this.f3425x ? 0 : J() - 1);
    }

    private View n2() {
        return I(this.f3425x ? J() - 1 : 0);
    }

    private void t2(RecyclerView.v vVar, RecyclerView.z zVar, int i10, int i11) {
        if (!zVar.g() || J() == 0 || zVar.e() || !N1()) {
            return;
        }
        List<RecyclerView.c0> k10 = vVar.k();
        int size = k10.size();
        int j02 = j0(I(0));
        int i12 = 0;
        int i13 = 0;
        for (int i14 = 0; i14 < size; i14++) {
            RecyclerView.c0 c0Var = k10.get(i14);
            if (!c0Var.isRemoved()) {
                if (((c0Var.getLayoutPosition() < j02) != this.f3425x ? (char) 65535 : (char) 1) == 65535) {
                    i12 += this.f3422u.e(c0Var.itemView);
                } else {
                    i13 += this.f3422u.e(c0Var.itemView);
                }
            }
        }
        this.f3421t.f3451l = k10;
        if (i12 > 0) {
            L2(j0(n2()), i10);
            c cVar = this.f3421t;
            cVar.f3447h = i12;
            cVar.f3442c = 0;
            cVar.a();
            W1(vVar, this.f3421t, zVar, false);
        }
        if (i13 > 0) {
            J2(j0(m2()), i11);
            c cVar2 = this.f3421t;
            cVar2.f3447h = i13;
            cVar2.f3442c = 0;
            cVar2.a();
            W1(vVar, this.f3421t, zVar, false);
        }
        this.f3421t.f3451l = null;
    }

    private void v2(RecyclerView.v vVar, c cVar) {
        if (!cVar.f3440a || cVar.f3452m) {
            return;
        }
        int i10 = cVar.f3446g;
        int i11 = cVar.f3448i;
        if (cVar.f3445f == -1) {
            x2(vVar, i10, i11);
        } else {
            y2(vVar, i10, i11);
        }
    }

    private void w2(RecyclerView.v vVar, int i10, int i11) {
        if (i10 == i11) {
            return;
        }
        if (i11 <= i10) {
            while (i10 > i11) {
                p1(i10, vVar);
                i10--;
            }
        } else {
            for (int i12 = i11 - 1; i12 >= i10; i12--) {
                p1(i12, vVar);
            }
        }
    }

    private void x2(RecyclerView.v vVar, int i10, int i11) {
        int J = J();
        if (i10 < 0) {
            return;
        }
        int h10 = (this.f3422u.h() - i10) + i11;
        if (this.f3425x) {
            for (int i12 = 0; i12 < J; i12++) {
                View I = I(i12);
                if (this.f3422u.g(I) < h10 || this.f3422u.r(I) < h10) {
                    w2(vVar, 0, i12);
                    return;
                }
            }
            return;
        }
        int i13 = J - 1;
        for (int i14 = i13; i14 >= 0; i14--) {
            View I2 = I(i14);
            if (this.f3422u.g(I2) < h10 || this.f3422u.r(I2) < h10) {
                w2(vVar, i13, i14);
                return;
            }
        }
    }

    private void y2(RecyclerView.v vVar, int i10, int i11) {
        if (i10 < 0) {
            return;
        }
        int i12 = i10 - i11;
        int J = J();
        if (!this.f3425x) {
            for (int i13 = 0; i13 < J; i13++) {
                View I = I(i13);
                if (this.f3422u.d(I) > i12 || this.f3422u.q(I) > i12) {
                    w2(vVar, 0, i13);
                    return;
                }
            }
            return;
        }
        int i14 = J - 1;
        for (int i15 = i14; i15 >= 0; i15--) {
            View I2 = I(i15);
            if (this.f3422u.d(I2) > i12 || this.f3422u.q(I2) > i12) {
                w2(vVar, i14, i15);
                return;
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int A1(int i10, RecyclerView.v vVar, RecyclerView.z zVar) {
        if (this.f3420s == 0) {
            return 0;
        }
        return B2(i10, vVar, zVar);
    }

    int B2(int i10, RecyclerView.v vVar, RecyclerView.z zVar) {
        if (J() == 0 || i10 == 0) {
            return 0;
        }
        V1();
        this.f3421t.f3440a = true;
        int i11 = i10 > 0 ? 1 : -1;
        int abs = Math.abs(i10);
        I2(i11, abs, true, zVar);
        c cVar = this.f3421t;
        int W1 = cVar.f3446g + W1(vVar, cVar, zVar, false);
        if (W1 < 0) {
            return 0;
        }
        if (abs > W1) {
            i10 = i11 * W1;
        }
        this.f3422u.s(-i10);
        this.f3421t.f3450k = i10;
        return i10;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public View C(int i10) {
        int J = J();
        if (J == 0) {
            return null;
        }
        int j02 = i10 - j0(I(0));
        if (j02 >= 0 && j02 < J) {
            View I = I(j02);
            if (j0(I) == i10) {
                return I;
            }
        }
        return super.C(i10);
    }

    public void C2(int i10) {
        if (i10 != 0 && i10 != 1) {
            throw new IllegalArgumentException("invalid orientation:" + i10);
        }
        g(null);
        if (i10 != this.f3420s || this.f3422u == null) {
            OrientationHelper b10 = OrientationHelper.b(this, i10);
            this.f3422u = b10;
            this.E.f3431a = b10;
            this.f3420s = i10;
            v1();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public RecyclerView.LayoutParams D() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    public void D2(boolean z10) {
        g(null);
        if (z10 == this.f3424w) {
            return;
        }
        this.f3424w = z10;
        v1();
    }

    public void E2(boolean z10) {
        g(null);
        if (this.f3426y == z10) {
            return;
        }
        this.f3426y = z10;
        v1();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    boolean I1() {
        return (X() == 1073741824 || r0() == 1073741824 || !s0()) ? false : true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void K0(RecyclerView recyclerView, RecyclerView.v vVar) {
        super.K0(recyclerView, vVar);
        if (this.C) {
            m1(vVar);
            vVar.c();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void K1(RecyclerView recyclerView, RecyclerView.z zVar, int i10) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.p(i10);
        L1(linearSmoothScroller);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public View L0(View view, int i10, RecyclerView.v vVar, RecyclerView.z zVar) {
        int T1;
        View h22;
        View m22;
        A2();
        if (J() == 0 || (T1 = T1(i10)) == Integer.MIN_VALUE) {
            return null;
        }
        V1();
        I2(T1, (int) (this.f3422u.o() * 0.33333334f), false, zVar);
        c cVar = this.f3421t;
        cVar.f3446g = Integer.MIN_VALUE;
        cVar.f3440a = false;
        W1(vVar, cVar, zVar, true);
        if (T1 == -1) {
            h22 = i2();
        } else {
            h22 = h2();
        }
        if (T1 == -1) {
            m22 = n2();
        } else {
            m22 = m2();
        }
        if (!m22.hasFocusable()) {
            return h22;
        }
        if (h22 == null) {
            return null;
        }
        return m22;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void M0(AccessibilityEvent accessibilityEvent) {
        super.M0(accessibilityEvent);
        if (J() > 0) {
            accessibilityEvent.setFromIndex(b2());
            accessibilityEvent.setToIndex(e2());
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public boolean N1() {
        return this.D == null && this.f3423v == this.f3426y;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void O1(RecyclerView.z zVar, int[] iArr) {
        int i10;
        int o22 = o2(zVar);
        if (this.f3421t.f3445f == -1) {
            i10 = 0;
        } else {
            i10 = o22;
            o22 = 0;
        }
        iArr[0] = o22;
        iArr[1] = i10;
    }

    void P1(RecyclerView.z zVar, c cVar, RecyclerView.p.c cVar2) {
        int i10 = cVar.f3443d;
        if (i10 < 0 || i10 >= zVar.b()) {
            return;
        }
        cVar2.a(i10, Math.max(0, cVar.f3446g));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int T1(int i10) {
        return i10 != 1 ? i10 != 2 ? i10 != 17 ? i10 != 33 ? i10 != 66 ? (i10 == 130 && this.f3420s == 1) ? 1 : Integer.MIN_VALUE : this.f3420s == 0 ? 1 : Integer.MIN_VALUE : this.f3420s == 1 ? -1 : Integer.MIN_VALUE : this.f3420s == 0 ? -1 : Integer.MIN_VALUE : (this.f3420s != 1 && q2()) ? -1 : 1 : (this.f3420s != 1 && q2()) ? 1 : -1;
    }

    c U1() {
        return new c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void V1() {
        if (this.f3421t == null) {
            this.f3421t = U1();
        }
    }

    int W1(RecyclerView.v vVar, c cVar, RecyclerView.z zVar, boolean z10) {
        int i10 = cVar.f3442c;
        int i11 = cVar.f3446g;
        if (i11 != Integer.MIN_VALUE) {
            if (i10 < 0) {
                cVar.f3446g = i11 + i10;
            }
            v2(vVar, cVar);
        }
        int i12 = cVar.f3442c + cVar.f3447h;
        b bVar = this.F;
        while (true) {
            if ((!cVar.f3452m && i12 <= 0) || !cVar.c(zVar)) {
                break;
            }
            bVar.a();
            s2(vVar, zVar, cVar, bVar);
            if (!bVar.f3437b) {
                cVar.f3441b += bVar.f3436a * cVar.f3445f;
                if (!bVar.f3438c || cVar.f3451l != null || !zVar.e()) {
                    int i13 = cVar.f3442c;
                    int i14 = bVar.f3436a;
                    cVar.f3442c = i13 - i14;
                    i12 -= i14;
                }
                int i15 = cVar.f3446g;
                if (i15 != Integer.MIN_VALUE) {
                    int i16 = i15 + bVar.f3436a;
                    cVar.f3446g = i16;
                    int i17 = cVar.f3442c;
                    if (i17 < 0) {
                        cVar.f3446g = i16 + i17;
                    }
                    v2(vVar, cVar);
                }
                if (z10 && bVar.f3439d) {
                    break;
                }
            } else {
                break;
            }
        }
        return i10 - cVar.f3442c;
    }

    public int X1() {
        View g22 = g2(0, J(), true, false);
        if (g22 == null) {
            return -1;
        }
        return j0(g22);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void Z0(RecyclerView.v vVar, RecyclerView.z zVar) {
        int i10;
        int i11;
        int i12;
        int i13;
        int k22;
        int i14;
        View C;
        int g6;
        int i15;
        int i16 = -1;
        if ((this.D != null || this.A != -1) && zVar.b() == 0) {
            m1(vVar);
            return;
        }
        SavedState savedState = this.D;
        if (savedState != null && savedState.j()) {
            this.A = this.D.f3428e;
        }
        V1();
        this.f3421t.f3440a = false;
        A2();
        View V = V();
        a aVar = this.E;
        if (aVar.f3435e && this.A == -1 && this.D == null) {
            if (V != null && (this.f3422u.g(V) >= this.f3422u.i() || this.f3422u.d(V) <= this.f3422u.n())) {
                this.E.c(V, j0(V));
            }
        } else {
            aVar.e();
            a aVar2 = this.E;
            aVar2.f3434d = this.f3425x ^ this.f3426y;
            H2(vVar, zVar, aVar2);
            this.E.f3435e = true;
        }
        c cVar = this.f3421t;
        cVar.f3445f = cVar.f3450k >= 0 ? 1 : -1;
        int[] iArr = this.H;
        iArr[0] = 0;
        iArr[1] = 0;
        O1(zVar, iArr);
        int max = Math.max(0, this.H[0]) + this.f3422u.n();
        int max2 = Math.max(0, this.H[1]) + this.f3422u.j();
        if (zVar.e() && (i14 = this.A) != -1 && this.B != Integer.MIN_VALUE && (C = C(i14)) != null) {
            if (this.f3425x) {
                i15 = this.f3422u.i() - this.f3422u.d(C);
                g6 = this.B;
            } else {
                g6 = this.f3422u.g(C) - this.f3422u.n();
                i15 = this.B;
            }
            int i17 = i15 - g6;
            if (i17 > 0) {
                max += i17;
            } else {
                max2 -= i17;
            }
        }
        a aVar3 = this.E;
        if (!aVar3.f3434d ? !this.f3425x : this.f3425x) {
            i16 = 1;
        }
        u2(vVar, zVar, aVar3, i16);
        w(vVar);
        this.f3421t.f3452m = z2();
        this.f3421t.f3449j = zVar.e();
        this.f3421t.f3448i = 0;
        a aVar4 = this.E;
        if (aVar4.f3434d) {
            M2(aVar4);
            c cVar2 = this.f3421t;
            cVar2.f3447h = max;
            W1(vVar, cVar2, zVar, false);
            c cVar3 = this.f3421t;
            i11 = cVar3.f3441b;
            int i18 = cVar3.f3443d;
            int i19 = cVar3.f3442c;
            if (i19 > 0) {
                max2 += i19;
            }
            K2(this.E);
            c cVar4 = this.f3421t;
            cVar4.f3447h = max2;
            cVar4.f3443d += cVar4.f3444e;
            W1(vVar, cVar4, zVar, false);
            c cVar5 = this.f3421t;
            i10 = cVar5.f3441b;
            int i20 = cVar5.f3442c;
            if (i20 > 0) {
                L2(i18, i11);
                c cVar6 = this.f3421t;
                cVar6.f3447h = i20;
                W1(vVar, cVar6, zVar, false);
                i11 = this.f3421t.f3441b;
            }
        } else {
            K2(aVar4);
            c cVar7 = this.f3421t;
            cVar7.f3447h = max2;
            W1(vVar, cVar7, zVar, false);
            c cVar8 = this.f3421t;
            i10 = cVar8.f3441b;
            int i21 = cVar8.f3443d;
            int i22 = cVar8.f3442c;
            if (i22 > 0) {
                max += i22;
            }
            M2(this.E);
            c cVar9 = this.f3421t;
            cVar9.f3447h = max;
            cVar9.f3443d += cVar9.f3444e;
            W1(vVar, cVar9, zVar, false);
            c cVar10 = this.f3421t;
            i11 = cVar10.f3441b;
            int i23 = cVar10.f3442c;
            if (i23 > 0) {
                J2(i21, i10);
                c cVar11 = this.f3421t;
                cVar11.f3447h = i23;
                W1(vVar, cVar11, zVar, false);
                i10 = this.f3421t.f3441b;
            }
        }
        if (J() > 0) {
            if (this.f3425x ^ this.f3426y) {
                int k23 = k2(i10, vVar, zVar, true);
                i12 = i11 + k23;
                i13 = i10 + k23;
                k22 = l2(i12, vVar, zVar, false);
            } else {
                int l22 = l2(i11, vVar, zVar, true);
                i12 = i11 + l22;
                i13 = i10 + l22;
                k22 = k2(i13, vVar, zVar, false);
            }
            i11 = i12 + k22;
            i10 = i13 + k22;
        }
        t2(vVar, zVar, i11, i10);
        if (!zVar.e()) {
            this.f3422u.t();
        } else {
            this.E.e();
        }
        this.f3423v = this.f3426y;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View Z1(boolean z10, boolean z11) {
        if (this.f3425x) {
            return g2(0, J(), z10, z11);
        }
        return g2(J() - 1, -1, z10, z11);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.y.b
    public PointF a(int i10) {
        if (J() == 0) {
            return null;
        }
        int i11 = (i10 < j0(I(0))) != this.f3425x ? -1 : 1;
        if (this.f3420s == 0) {
            return new PointF(i11, 0.0f);
        }
        return new PointF(0.0f, i11);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void a1(RecyclerView.z zVar) {
        super.a1(zVar);
        this.D = null;
        this.A = -1;
        this.B = Integer.MIN_VALUE;
        this.E.e();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View a2(boolean z10, boolean z11) {
        if (this.f3425x) {
            return g2(J() - 1, -1, z10, z11);
        }
        return g2(0, J(), z10, z11);
    }

    public int b2() {
        View g22 = g2(0, J(), false, true);
        if (g22 == null) {
            return -1;
        }
        return j0(g22);
    }

    public int c2() {
        View g22 = g2(J() - 1, -1, true, false);
        if (g22 == null) {
            return -1;
        }
        return j0(g22);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void e1(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            this.D = savedState;
            if (this.A != -1) {
                savedState.k();
            }
            v1();
        }
    }

    public int e2() {
        View g22 = g2(J() - 1, -1, false, true);
        if (g22 == null) {
            return -1;
        }
        return j0(g22);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public Parcelable f1() {
        if (this.D != null) {
            return new SavedState(this.D);
        }
        SavedState savedState = new SavedState();
        if (J() > 0) {
            V1();
            boolean z10 = this.f3423v ^ this.f3425x;
            savedState.f3430g = z10;
            if (z10) {
                View m22 = m2();
                savedState.f3429f = this.f3422u.i() - this.f3422u.d(m22);
                savedState.f3428e = j0(m22);
            } else {
                View n22 = n2();
                savedState.f3428e = j0(n22);
                savedState.f3429f = this.f3422u.g(n22) - this.f3422u.n();
            }
        } else {
            savedState.k();
        }
        return savedState;
    }

    View f2(int i10, int i11) {
        int i12;
        int i13;
        V1();
        if ((i11 > i10 ? (char) 1 : i11 < i10 ? (char) 65535 : (char) 0) == 0) {
            return I(i10);
        }
        if (this.f3422u.g(I(i10)) < this.f3422u.n()) {
            i12 = 16644;
            i13 = 16388;
        } else {
            i12 = 4161;
            i13 = 4097;
        }
        if (this.f3420s == 0) {
            return this.f3490e.a(i10, i11, i12, i13);
        }
        return this.f3491f.a(i10, i11, i12, i13);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void g(String str) {
        if (this.D == null) {
            super.g(str);
        }
    }

    View g2(int i10, int i11, boolean z10, boolean z11) {
        V1();
        int i12 = z10 ? 24579 : 320;
        int i13 = z11 ? 320 : 0;
        if (this.f3420s == 0) {
            return this.f3490e.a(i10, i11, i12, i13);
        }
        return this.f3491f.a(i10, i11, i12, i13);
    }

    View j2(RecyclerView.v vVar, RecyclerView.z zVar, boolean z10, boolean z11) {
        int i10;
        int i11;
        V1();
        int J = J();
        int i12 = -1;
        if (z11) {
            i10 = J() - 1;
            i11 = -1;
        } else {
            i12 = J;
            i10 = 0;
            i11 = 1;
        }
        int b10 = zVar.b();
        int n10 = this.f3422u.n();
        int i13 = this.f3422u.i();
        View view = null;
        View view2 = null;
        View view3 = null;
        while (i10 != i12) {
            View I = I(i10);
            int j02 = j0(I);
            int g6 = this.f3422u.g(I);
            int d10 = this.f3422u.d(I);
            if (j02 >= 0 && j02 < b10) {
                if (!((RecyclerView.LayoutParams) I.getLayoutParams()).c()) {
                    boolean z12 = d10 <= n10 && g6 < n10;
                    boolean z13 = g6 >= i13 && d10 > i13;
                    if (!z12 && !z13) {
                        return I;
                    }
                    if (z10) {
                        if (!z13) {
                            if (view != null) {
                            }
                            view = I;
                        }
                        view2 = I;
                    } else {
                        if (!z12) {
                            if (view != null) {
                            }
                            view = I;
                        }
                        view2 = I;
                    }
                } else if (view3 == null) {
                    view3 = I;
                }
            }
            i10 += i11;
        }
        return view != null ? view : view2 != null ? view2 : view3;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public boolean k() {
        return this.f3420s == 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public boolean l() {
        return this.f3420s == 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void o(int i10, int i11, RecyclerView.z zVar, RecyclerView.p.c cVar) {
        if (this.f3420s != 0) {
            i10 = i11;
        }
        if (J() == 0 || i10 == 0) {
            return;
        }
        V1();
        I2(i10 > 0 ? 1 : -1, Math.abs(i10), true, zVar);
        P1(zVar, this.f3421t, cVar);
    }

    @Deprecated
    protected int o2(RecyclerView.z zVar) {
        if (zVar.d()) {
            return this.f3422u.o();
        }
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void p(int i10, RecyclerView.p.c cVar) {
        boolean z10;
        int i11;
        SavedState savedState = this.D;
        if (savedState != null && savedState.j()) {
            SavedState savedState2 = this.D;
            z10 = savedState2.f3430g;
            i11 = savedState2.f3428e;
        } else {
            A2();
            z10 = this.f3425x;
            i11 = this.A;
            if (i11 == -1) {
                i11 = z10 ? i10 - 1 : 0;
            }
        }
        int i12 = z10 ? -1 : 1;
        for (int i13 = 0; i13 < this.G && i11 >= 0 && i11 < i10; i13++) {
            cVar.a(i11, 0);
            i11 += i12;
        }
    }

    public int p2() {
        return this.f3420s;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int q(RecyclerView.z zVar) {
        return Q1(zVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean q2() {
        return Z() == 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int r(RecyclerView.z zVar) {
        return R1(zVar);
    }

    public boolean r2() {
        return this.f3427z;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int s(RecyclerView.z zVar) {
        return S1(zVar);
    }

    void s2(RecyclerView.v vVar, RecyclerView.z zVar, c cVar, b bVar) {
        int i10;
        int i11;
        int i12;
        int i13;
        int f10;
        View d10 = cVar.d(vVar);
        if (d10 == null) {
            bVar.f3437b = true;
            return;
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) d10.getLayoutParams();
        if (cVar.f3451l == null) {
            if (this.f3425x == (cVar.f3445f == -1)) {
                d(d10);
            } else {
                e(d10, 0);
            }
        } else {
            if (this.f3425x == (cVar.f3445f == -1)) {
                b(d10);
            } else {
                c(d10, 0);
            }
        }
        C0(d10, 0, 0);
        bVar.f3436a = this.f3422u.e(d10);
        if (this.f3420s == 1) {
            if (q2()) {
                f10 = q0() - g0();
                i13 = f10 - this.f3422u.f(d10);
            } else {
                i13 = f0();
                f10 = this.f3422u.f(d10) + i13;
            }
            if (cVar.f3445f == -1) {
                int i14 = cVar.f3441b;
                i12 = i14;
                i11 = f10;
                i10 = i14 - bVar.f3436a;
            } else {
                int i15 = cVar.f3441b;
                i10 = i15;
                i11 = f10;
                i12 = bVar.f3436a + i15;
            }
        } else {
            int i02 = i0();
            int f11 = this.f3422u.f(d10) + i02;
            if (cVar.f3445f == -1) {
                int i16 = cVar.f3441b;
                i11 = i16;
                i10 = i02;
                i12 = f11;
                i13 = i16 - bVar.f3436a;
            } else {
                int i17 = cVar.f3441b;
                i10 = i02;
                i11 = bVar.f3436a + i17;
                i12 = f11;
                i13 = i17;
            }
        }
        B0(d10, i13, i10, i11, i12);
        if (layoutParams.c() || layoutParams.b()) {
            bVar.f3438c = true;
        }
        bVar.f3439d = d10.hasFocusable();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int t(RecyclerView.z zVar) {
        return Q1(zVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int u(RecyclerView.z zVar) {
        return R1(zVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public boolean u0() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u2(RecyclerView.v vVar, RecyclerView.z zVar, a aVar, int i10) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int v(RecyclerView.z zVar) {
        return S1(zVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public int y1(int i10, RecyclerView.v vVar, RecyclerView.z zVar) {
        if (this.f3420s == 1) {
            return 0;
        }
        return B2(i10, vVar, zVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.p
    public void z1(int i10) {
        this.A = i10;
        this.B = Integer.MIN_VALUE;
        SavedState savedState = this.D;
        if (savedState != null) {
            savedState.k();
        }
        v1();
    }

    boolean z2() {
        return this.f3422u.l() == 0 && this.f3422u.h() == 0;
    }

    public LinearLayoutManager(Context context, int i10, boolean z10) {
        this.f3420s = 1;
        this.f3424w = false;
        this.f3425x = false;
        this.f3426y = false;
        this.f3427z = true;
        this.A = -1;
        this.B = Integer.MIN_VALUE;
        this.D = null;
        this.E = new a();
        this.F = new b();
        this.G = 2;
        this.H = new int[2];
        C2(i10);
        D2(z10);
    }

    public LinearLayoutManager(Context context, AttributeSet attributeSet, int i10, int i11) {
        this.f3420s = 1;
        this.f3424w = false;
        this.f3425x = false;
        this.f3426y = false;
        this.f3427z = true;
        this.A = -1;
        this.B = Integer.MIN_VALUE;
        this.D = null;
        this.E = new a();
        this.F = new b();
        this.G = 2;
        this.H = new int[2];
        RecyclerView.p.d k02 = RecyclerView.p.k0(context, attributeSet, i10, i11);
        C2(k02.f3506a);
        D2(k02.f3508c);
        E2(k02.f3509d);
    }
}
