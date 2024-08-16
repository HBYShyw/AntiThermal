package l;

import java.util.Arrays;
import java.util.HashMap;
import l.SolverVariable;
import m.ConstraintAnchor;
import m.ConstraintWidget;

/* compiled from: LinearSystem.java */
/* renamed from: l.d, reason: use source file name */
/* loaded from: classes.dex */
public class LinearSystem {

    /* renamed from: q, reason: collision with root package name */
    private static int f14499q = 1000;

    /* renamed from: r, reason: collision with root package name */
    public static boolean f14500r = true;

    /* renamed from: s, reason: collision with root package name */
    public static long f14501s;

    /* renamed from: t, reason: collision with root package name */
    public static long f14502t;

    /* renamed from: c, reason: collision with root package name */
    private a f14505c;

    /* renamed from: f, reason: collision with root package name */
    ArrayRow[] f14508f;

    /* renamed from: m, reason: collision with root package name */
    final c f14515m;

    /* renamed from: p, reason: collision with root package name */
    private a f14518p;

    /* renamed from: a, reason: collision with root package name */
    int f14503a = 0;

    /* renamed from: b, reason: collision with root package name */
    private HashMap<String, SolverVariable> f14504b = null;

    /* renamed from: d, reason: collision with root package name */
    private int f14506d = 32;

    /* renamed from: e, reason: collision with root package name */
    private int f14507e = 32;

    /* renamed from: g, reason: collision with root package name */
    public boolean f14509g = false;

    /* renamed from: h, reason: collision with root package name */
    public boolean f14510h = false;

    /* renamed from: i, reason: collision with root package name */
    private boolean[] f14511i = new boolean[32];

    /* renamed from: j, reason: collision with root package name */
    int f14512j = 1;

    /* renamed from: k, reason: collision with root package name */
    int f14513k = 0;

    /* renamed from: l, reason: collision with root package name */
    private int f14514l = 32;

    /* renamed from: n, reason: collision with root package name */
    private SolverVariable[] f14516n = new SolverVariable[f14499q];

    /* renamed from: o, reason: collision with root package name */
    private int f14517o = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LinearSystem.java */
    /* renamed from: l.d$a */
    /* loaded from: classes.dex */
    public interface a {
        SolverVariable a(LinearSystem linearSystem, boolean[] zArr);

        void b(a aVar);

        void c(SolverVariable solverVariable);

        void clear();

        SolverVariable getKey();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LinearSystem.java */
    /* renamed from: l.d$b */
    /* loaded from: classes.dex */
    public class b extends ArrayRow {
        public b(c cVar) {
            this.f14493e = new SolverVariableValues(this, cVar);
        }
    }

    public LinearSystem() {
        this.f14508f = null;
        this.f14508f = new ArrayRow[32];
        C();
        c cVar = new c();
        this.f14515m = cVar;
        this.f14505c = new PriorityGoalRow(cVar);
        if (f14500r) {
            this.f14518p = new b(cVar);
        } else {
            this.f14518p = new ArrayRow(cVar);
        }
    }

    private final int B(a aVar, boolean z10) {
        for (int i10 = 0; i10 < this.f14512j; i10++) {
            this.f14511i[i10] = false;
        }
        boolean z11 = false;
        int i11 = 0;
        while (!z11) {
            i11++;
            if (i11 >= this.f14512j * 2) {
                return i11;
            }
            if (aVar.getKey() != null) {
                this.f14511i[aVar.getKey().f14535c] = true;
            }
            SolverVariable a10 = aVar.a(this, this.f14511i);
            if (a10 != null) {
                boolean[] zArr = this.f14511i;
                int i12 = a10.f14535c;
                if (zArr[i12]) {
                    return i11;
                }
                zArr[i12] = true;
            }
            if (a10 != null) {
                float f10 = Float.MAX_VALUE;
                int i13 = -1;
                for (int i14 = 0; i14 < this.f14513k; i14++) {
                    ArrayRow arrayRow = this.f14508f[i14];
                    if (arrayRow.f14489a.f14542j != SolverVariable.a.UNRESTRICTED && !arrayRow.f14494f && arrayRow.t(a10)) {
                        float i15 = arrayRow.f14493e.i(a10);
                        if (i15 < 0.0f) {
                            float f11 = (-arrayRow.f14490b) / i15;
                            if (f11 < f10) {
                                i13 = i14;
                                f10 = f11;
                            }
                        }
                    }
                }
                if (i13 > -1) {
                    ArrayRow arrayRow2 = this.f14508f[i13];
                    arrayRow2.f14489a.f14536d = -1;
                    arrayRow2.y(a10);
                    SolverVariable solverVariable = arrayRow2.f14489a;
                    solverVariable.f14536d = i13;
                    solverVariable.g(arrayRow2);
                }
            } else {
                z11 = true;
            }
        }
        return i11;
    }

    private void C() {
        int i10 = 0;
        if (f14500r) {
            while (true) {
                ArrayRow[] arrayRowArr = this.f14508f;
                if (i10 >= arrayRowArr.length) {
                    return;
                }
                ArrayRow arrayRow = arrayRowArr[i10];
                if (arrayRow != null) {
                    this.f14515m.f14495a.a(arrayRow);
                }
                this.f14508f[i10] = null;
                i10++;
            }
        } else {
            while (true) {
                ArrayRow[] arrayRowArr2 = this.f14508f;
                if (i10 >= arrayRowArr2.length) {
                    return;
                }
                ArrayRow arrayRow2 = arrayRowArr2[i10];
                if (arrayRow2 != null) {
                    this.f14515m.f14496b.a(arrayRow2);
                }
                this.f14508f[i10] = null;
                i10++;
            }
        }
    }

    private SolverVariable a(SolverVariable.a aVar, String str) {
        SolverVariable b10 = this.f14515m.f14497c.b();
        if (b10 == null) {
            b10 = new SolverVariable(aVar, str);
            b10.f(aVar, str);
        } else {
            b10.d();
            b10.f(aVar, str);
        }
        int i10 = this.f14517o;
        int i11 = f14499q;
        if (i10 >= i11) {
            int i12 = i11 * 2;
            f14499q = i12;
            this.f14516n = (SolverVariable[]) Arrays.copyOf(this.f14516n, i12);
        }
        SolverVariable[] solverVariableArr = this.f14516n;
        int i13 = this.f14517o;
        this.f14517o = i13 + 1;
        solverVariableArr[i13] = b10;
        return b10;
    }

    private final void l(ArrayRow arrayRow) {
        if (f14500r) {
            ArrayRow[] arrayRowArr = this.f14508f;
            int i10 = this.f14513k;
            if (arrayRowArr[i10] != null) {
                this.f14515m.f14495a.a(arrayRowArr[i10]);
            }
        } else {
            ArrayRow[] arrayRowArr2 = this.f14508f;
            int i11 = this.f14513k;
            if (arrayRowArr2[i11] != null) {
                this.f14515m.f14496b.a(arrayRowArr2[i11]);
            }
        }
        ArrayRow[] arrayRowArr3 = this.f14508f;
        int i12 = this.f14513k;
        arrayRowArr3[i12] = arrayRow;
        SolverVariable solverVariable = arrayRow.f14489a;
        solverVariable.f14536d = i12;
        this.f14513k = i12 + 1;
        solverVariable.g(arrayRow);
    }

    private void n() {
        for (int i10 = 0; i10 < this.f14513k; i10++) {
            ArrayRow arrayRow = this.f14508f[i10];
            arrayRow.f14489a.f14538f = arrayRow.f14490b;
        }
    }

    public static ArrayRow s(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, float f10) {
        return linearSystem.r().j(solverVariable, solverVariable2, f10);
    }

    private int u(a aVar) {
        float f10;
        boolean z10;
        int i10 = 0;
        while (true) {
            f10 = 0.0f;
            if (i10 >= this.f14513k) {
                z10 = false;
                break;
            }
            ArrayRow[] arrayRowArr = this.f14508f;
            if (arrayRowArr[i10].f14489a.f14542j != SolverVariable.a.UNRESTRICTED && arrayRowArr[i10].f14490b < 0.0f) {
                z10 = true;
                break;
            }
            i10++;
        }
        if (!z10) {
            return 0;
        }
        boolean z11 = false;
        int i11 = 0;
        while (!z11) {
            i11++;
            float f11 = Float.MAX_VALUE;
            int i12 = -1;
            int i13 = -1;
            int i14 = 0;
            int i15 = 0;
            while (i14 < this.f14513k) {
                ArrayRow arrayRow = this.f14508f[i14];
                if (arrayRow.f14489a.f14542j != SolverVariable.a.UNRESTRICTED && !arrayRow.f14494f && arrayRow.f14490b < f10) {
                    int i16 = 1;
                    while (i16 < this.f14512j) {
                        SolverVariable solverVariable = this.f14515m.f14498d[i16];
                        float i17 = arrayRow.f14493e.i(solverVariable);
                        if (i17 > f10) {
                            for (int i18 = 0; i18 < 9; i18++) {
                                float f12 = solverVariable.f14540h[i18] / i17;
                                if ((f12 < f11 && i18 == i15) || i18 > i15) {
                                    i15 = i18;
                                    f11 = f12;
                                    i12 = i14;
                                    i13 = i16;
                                }
                            }
                        }
                        i16++;
                        f10 = 0.0f;
                    }
                }
                i14++;
                f10 = 0.0f;
            }
            if (i12 != -1) {
                ArrayRow arrayRow2 = this.f14508f[i12];
                arrayRow2.f14489a.f14536d = -1;
                arrayRow2.y(this.f14515m.f14498d[i13]);
                SolverVariable solverVariable2 = arrayRow2.f14489a;
                solverVariable2.f14536d = i12;
                solverVariable2.g(arrayRow2);
            } else {
                z11 = true;
            }
            if (i11 > this.f14512j / 2) {
                z11 = true;
            }
            f10 = 0.0f;
        }
        return i11;
    }

    public static Metrics w() {
        return null;
    }

    private void y() {
        int i10 = this.f14506d * 2;
        this.f14506d = i10;
        this.f14508f = (ArrayRow[]) Arrays.copyOf(this.f14508f, i10);
        c cVar = this.f14515m;
        cVar.f14498d = (SolverVariable[]) Arrays.copyOf(cVar.f14498d, this.f14506d);
        int i11 = this.f14506d;
        this.f14511i = new boolean[i11];
        this.f14507e = i11;
        this.f14514l = i11;
    }

    void A(a aVar) {
        u(aVar);
        B(aVar, false);
        n();
    }

    public void D() {
        c cVar;
        int i10 = 0;
        while (true) {
            cVar = this.f14515m;
            SolverVariable[] solverVariableArr = cVar.f14498d;
            if (i10 >= solverVariableArr.length) {
                break;
            }
            SolverVariable solverVariable = solverVariableArr[i10];
            if (solverVariable != null) {
                solverVariable.d();
            }
            i10++;
        }
        cVar.f14497c.c(this.f14516n, this.f14517o);
        this.f14517o = 0;
        Arrays.fill(this.f14515m.f14498d, (Object) null);
        HashMap<String, SolverVariable> hashMap = this.f14504b;
        if (hashMap != null) {
            hashMap.clear();
        }
        this.f14503a = 0;
        this.f14505c.clear();
        this.f14512j = 1;
        for (int i11 = 0; i11 < this.f14513k; i11++) {
            this.f14508f[i11].f14491c = false;
        }
        C();
        this.f14513k = 0;
        if (f14500r) {
            this.f14518p = new b(this.f14515m);
        } else {
            this.f14518p = new ArrayRow(this.f14515m);
        }
    }

    public void b(ConstraintWidget constraintWidget, ConstraintWidget constraintWidget2, float f10, int i10) {
        ConstraintAnchor.b bVar = ConstraintAnchor.b.LEFT;
        SolverVariable q10 = q(constraintWidget.n(bVar));
        ConstraintAnchor.b bVar2 = ConstraintAnchor.b.TOP;
        SolverVariable q11 = q(constraintWidget.n(bVar2));
        ConstraintAnchor.b bVar3 = ConstraintAnchor.b.RIGHT;
        SolverVariable q12 = q(constraintWidget.n(bVar3));
        ConstraintAnchor.b bVar4 = ConstraintAnchor.b.BOTTOM;
        SolverVariable q13 = q(constraintWidget.n(bVar4));
        SolverVariable q14 = q(constraintWidget2.n(bVar));
        SolverVariable q15 = q(constraintWidget2.n(bVar2));
        SolverVariable q16 = q(constraintWidget2.n(bVar3));
        SolverVariable q17 = q(constraintWidget2.n(bVar4));
        ArrayRow r10 = r();
        double d10 = f10;
        double d11 = i10;
        r10.q(q11, q13, q15, q17, (float) (Math.sin(d10) * d11));
        d(r10);
        ArrayRow r11 = r();
        r11.q(q10, q12, q14, q16, (float) (Math.cos(d10) * d11));
        d(r11);
    }

    public void c(SolverVariable solverVariable, SolverVariable solverVariable2, int i10, float f10, SolverVariable solverVariable3, SolverVariable solverVariable4, int i11, int i12) {
        ArrayRow r10 = r();
        r10.h(solverVariable, solverVariable2, i10, f10, solverVariable3, solverVariable4, i11);
        if (i12 != 8) {
            r10.d(this, i12);
        }
        d(r10);
    }

    public void d(ArrayRow arrayRow) {
        SolverVariable w10;
        if (arrayRow == null) {
            return;
        }
        boolean z10 = true;
        if (this.f14513k + 1 >= this.f14514l || this.f14512j + 1 >= this.f14507e) {
            y();
        }
        boolean z11 = false;
        if (!arrayRow.f14494f) {
            arrayRow.D(this);
            if (arrayRow.u()) {
                return;
            }
            arrayRow.r();
            if (arrayRow.f(this)) {
                SolverVariable p10 = p();
                arrayRow.f14489a = p10;
                l(arrayRow);
                this.f14518p.b(arrayRow);
                B(this.f14518p, true);
                if (p10.f14536d == -1) {
                    if (arrayRow.f14489a == p10 && (w10 = arrayRow.w(p10)) != null) {
                        arrayRow.y(w10);
                    }
                    if (!arrayRow.f14494f) {
                        arrayRow.f14489a.g(arrayRow);
                    }
                    this.f14513k--;
                }
            } else {
                z10 = false;
            }
            if (!arrayRow.s()) {
                return;
            } else {
                z11 = z10;
            }
        }
        if (z11) {
            return;
        }
        l(arrayRow);
    }

    public ArrayRow e(SolverVariable solverVariable, SolverVariable solverVariable2, int i10, int i11) {
        if (i11 == 8 && solverVariable2.f14539g && solverVariable.f14536d == -1) {
            solverVariable.e(this, solverVariable2.f14538f + i10);
            return null;
        }
        ArrayRow r10 = r();
        r10.n(solverVariable, solverVariable2, i10);
        if (i11 != 8) {
            r10.d(this, i11);
        }
        d(r10);
        return r10;
    }

    public void f(SolverVariable solverVariable, int i10) {
        int i11 = solverVariable.f14536d;
        if (i11 == -1) {
            solverVariable.e(this, i10);
            return;
        }
        if (i11 != -1) {
            ArrayRow arrayRow = this.f14508f[i11];
            if (arrayRow.f14494f) {
                arrayRow.f14490b = i10;
                return;
            }
            if (arrayRow.f14493e.a() == 0) {
                arrayRow.f14494f = true;
                arrayRow.f14490b = i10;
                return;
            } else {
                ArrayRow r10 = r();
                r10.m(solverVariable, i10);
                d(r10);
                return;
            }
        }
        ArrayRow r11 = r();
        r11.i(solverVariable, i10);
        d(r11);
    }

    public void g(SolverVariable solverVariable, SolverVariable solverVariable2, int i10, boolean z10) {
        ArrayRow r10 = r();
        SolverVariable t7 = t();
        t7.f14537e = 0;
        r10.o(solverVariable, solverVariable2, t7, i10);
        d(r10);
    }

    public void h(SolverVariable solverVariable, SolverVariable solverVariable2, int i10, int i11) {
        ArrayRow r10 = r();
        SolverVariable t7 = t();
        t7.f14537e = 0;
        r10.o(solverVariable, solverVariable2, t7, i10);
        if (i11 != 8) {
            m(r10, (int) (r10.f14493e.i(t7) * (-1.0f)), i11);
        }
        d(r10);
    }

    public void i(SolverVariable solverVariable, SolverVariable solverVariable2, int i10, boolean z10) {
        ArrayRow r10 = r();
        SolverVariable t7 = t();
        t7.f14537e = 0;
        r10.p(solverVariable, solverVariable2, t7, i10);
        d(r10);
    }

    public void j(SolverVariable solverVariable, SolverVariable solverVariable2, int i10, int i11) {
        ArrayRow r10 = r();
        SolverVariable t7 = t();
        t7.f14537e = 0;
        r10.p(solverVariable, solverVariable2, t7, i10);
        if (i11 != 8) {
            m(r10, (int) (r10.f14493e.i(t7) * (-1.0f)), i11);
        }
        d(r10);
    }

    public void k(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f10, int i10) {
        ArrayRow r10 = r();
        r10.k(solverVariable, solverVariable2, solverVariable3, solverVariable4, f10);
        if (i10 != 8) {
            r10.d(this, i10);
        }
        d(r10);
    }

    void m(ArrayRow arrayRow, int i10, int i11) {
        arrayRow.e(o(i11, null), i10);
    }

    public SolverVariable o(int i10, String str) {
        if (this.f14512j + 1 >= this.f14507e) {
            y();
        }
        SolverVariable a10 = a(SolverVariable.a.ERROR, str);
        int i11 = this.f14503a + 1;
        this.f14503a = i11;
        this.f14512j++;
        a10.f14535c = i11;
        a10.f14537e = i10;
        this.f14515m.f14498d[i11] = a10;
        this.f14505c.c(a10);
        return a10;
    }

    public SolverVariable p() {
        if (this.f14512j + 1 >= this.f14507e) {
            y();
        }
        SolverVariable a10 = a(SolverVariable.a.SLACK, null);
        int i10 = this.f14503a + 1;
        this.f14503a = i10;
        this.f14512j++;
        a10.f14535c = i10;
        this.f14515m.f14498d[i10] = a10;
        return a10;
    }

    public SolverVariable q(Object obj) {
        SolverVariable solverVariable = null;
        if (obj == null) {
            return null;
        }
        if (this.f14512j + 1 >= this.f14507e) {
            y();
        }
        if (obj instanceof ConstraintAnchor) {
            ConstraintAnchor constraintAnchor = (ConstraintAnchor) obj;
            solverVariable = constraintAnchor.f();
            if (solverVariable == null) {
                constraintAnchor.m(this.f14515m);
                solverVariable = constraintAnchor.f();
            }
            int i10 = solverVariable.f14535c;
            if (i10 == -1 || i10 > this.f14503a || this.f14515m.f14498d[i10] == null) {
                if (i10 != -1) {
                    solverVariable.d();
                }
                int i11 = this.f14503a + 1;
                this.f14503a = i11;
                this.f14512j++;
                solverVariable.f14535c = i11;
                solverVariable.f14542j = SolverVariable.a.UNRESTRICTED;
                this.f14515m.f14498d[i11] = solverVariable;
            }
        }
        return solverVariable;
    }

    public ArrayRow r() {
        ArrayRow b10;
        if (f14500r) {
            b10 = this.f14515m.f14495a.b();
            if (b10 == null) {
                b10 = new b(this.f14515m);
                f14502t++;
            } else {
                b10.z();
            }
        } else {
            b10 = this.f14515m.f14496b.b();
            if (b10 == null) {
                b10 = new ArrayRow(this.f14515m);
                f14501s++;
            } else {
                b10.z();
            }
        }
        SolverVariable.b();
        return b10;
    }

    public SolverVariable t() {
        if (this.f14512j + 1 >= this.f14507e) {
            y();
        }
        SolverVariable a10 = a(SolverVariable.a.SLACK, null);
        int i10 = this.f14503a + 1;
        this.f14503a = i10;
        this.f14512j++;
        a10.f14535c = i10;
        this.f14515m.f14498d[i10] = a10;
        return a10;
    }

    public c v() {
        return this.f14515m;
    }

    public int x(Object obj) {
        SolverVariable f10 = ((ConstraintAnchor) obj).f();
        if (f10 != null) {
            return (int) (f10.f14538f + 0.5f);
        }
        return 0;
    }

    public void z() {
        if (!this.f14509g && !this.f14510h) {
            A(this.f14505c);
            return;
        }
        boolean z10 = false;
        int i10 = 0;
        while (true) {
            if (i10 >= this.f14513k) {
                z10 = true;
                break;
            } else if (!this.f14508f[i10].f14494f) {
                break;
            } else {
                i10++;
            }
        }
        if (!z10) {
            A(this.f14505c);
        } else {
            n();
        }
    }
}
