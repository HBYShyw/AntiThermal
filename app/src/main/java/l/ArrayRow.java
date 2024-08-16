package l;

import java.util.ArrayList;
import java.util.Iterator;
import l.LinearSystem;
import l.SolverVariable;

/* compiled from: ArrayRow.java */
/* renamed from: l.b, reason: use source file name */
/* loaded from: classes.dex */
public class ArrayRow implements LinearSystem.a {

    /* renamed from: e, reason: collision with root package name */
    public a f14493e;

    /* renamed from: a, reason: collision with root package name */
    SolverVariable f14489a = null;

    /* renamed from: b, reason: collision with root package name */
    float f14490b = 0.0f;

    /* renamed from: c, reason: collision with root package name */
    boolean f14491c = false;

    /* renamed from: d, reason: collision with root package name */
    ArrayList<SolverVariable> f14492d = new ArrayList<>();

    /* renamed from: f, reason: collision with root package name */
    boolean f14494f = false;

    /* compiled from: ArrayRow.java */
    /* renamed from: l.b$a */
    /* loaded from: classes.dex */
    public interface a {
        int a();

        void b(SolverVariable solverVariable, float f10, boolean z10);

        SolverVariable c(int i10);

        void clear();

        boolean d(SolverVariable solverVariable);

        void e();

        float f(int i10);

        float g(ArrayRow arrayRow, boolean z10);

        void h(SolverVariable solverVariable, float f10);

        float i(SolverVariable solverVariable);

        float j(SolverVariable solverVariable, boolean z10);

        void k(float f10);
    }

    public ArrayRow() {
    }

    private boolean v(SolverVariable solverVariable, LinearSystem linearSystem) {
        return solverVariable.f14545m <= 1;
    }

    private SolverVariable x(boolean[] zArr, SolverVariable solverVariable) {
        SolverVariable.a aVar;
        int a10 = this.f14493e.a();
        SolverVariable solverVariable2 = null;
        float f10 = 0.0f;
        for (int i10 = 0; i10 < a10; i10++) {
            float f11 = this.f14493e.f(i10);
            if (f11 < 0.0f) {
                SolverVariable c10 = this.f14493e.c(i10);
                if ((zArr == null || !zArr[c10.f14535c]) && c10 != solverVariable && (((aVar = c10.f14542j) == SolverVariable.a.SLACK || aVar == SolverVariable.a.ERROR) && f11 < f10)) {
                    f10 = f11;
                    solverVariable2 = c10;
                }
            }
        }
        return solverVariable2;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00cf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    String A() {
        boolean z10;
        String str = (this.f14489a == null ? "0" : "" + this.f14489a) + " = ";
        if (this.f14490b != 0.0f) {
            str = str + this.f14490b;
            z10 = true;
        } else {
            z10 = false;
        }
        int a10 = this.f14493e.a();
        for (int i10 = 0; i10 < a10; i10++) {
            SolverVariable c10 = this.f14493e.c(i10);
            if (c10 != null) {
                float f10 = this.f14493e.f(i10);
                if (f10 != 0.0f) {
                    String solverVariable = c10.toString();
                    if (!z10) {
                        if (f10 < 0.0f) {
                            str = str + "- ";
                            f10 *= -1.0f;
                        }
                        str = f10 == 1.0f ? str + solverVariable : str + f10 + " " + solverVariable;
                        z10 = true;
                    } else if (f10 > 0.0f) {
                        str = str + " + ";
                        if (f10 == 1.0f) {
                        }
                        z10 = true;
                    } else {
                        str = str + " - ";
                        f10 *= -1.0f;
                        if (f10 == 1.0f) {
                        }
                        z10 = true;
                    }
                }
            }
        }
        if (z10) {
            return str;
        }
        return str + "0.0";
    }

    public void B(LinearSystem linearSystem, SolverVariable solverVariable, boolean z10) {
        if (solverVariable.f14539g) {
            this.f14490b += solverVariable.f14538f * this.f14493e.i(solverVariable);
            this.f14493e.j(solverVariable, z10);
            if (z10) {
                solverVariable.c(this);
            }
        }
    }

    public void C(ArrayRow arrayRow, boolean z10) {
        this.f14490b += arrayRow.f14490b * this.f14493e.g(arrayRow, z10);
        if (z10) {
            arrayRow.f14489a.c(this);
        }
    }

    public void D(LinearSystem linearSystem) {
        if (linearSystem.f14508f.length == 0) {
            return;
        }
        boolean z10 = false;
        while (!z10) {
            int a10 = this.f14493e.a();
            for (int i10 = 0; i10 < a10; i10++) {
                SolverVariable c10 = this.f14493e.c(i10);
                if (c10.f14536d != -1 || c10.f14539g) {
                    this.f14492d.add(c10);
                }
            }
            if (this.f14492d.size() > 0) {
                Iterator<SolverVariable> it = this.f14492d.iterator();
                while (it.hasNext()) {
                    SolverVariable next = it.next();
                    if (next.f14539g) {
                        B(linearSystem, next, true);
                    } else {
                        C(linearSystem.f14508f[next.f14536d], true);
                    }
                }
                this.f14492d.clear();
            } else {
                z10 = true;
            }
        }
    }

    @Override // l.LinearSystem.a
    public SolverVariable a(LinearSystem linearSystem, boolean[] zArr) {
        return x(zArr, null);
    }

    @Override // l.LinearSystem.a
    public void b(LinearSystem.a aVar) {
        if (aVar instanceof ArrayRow) {
            ArrayRow arrayRow = (ArrayRow) aVar;
            this.f14489a = null;
            this.f14493e.clear();
            for (int i10 = 0; i10 < arrayRow.f14493e.a(); i10++) {
                this.f14493e.b(arrayRow.f14493e.c(i10), arrayRow.f14493e.f(i10), true);
            }
        }
    }

    @Override // l.LinearSystem.a
    public void c(SolverVariable solverVariable) {
        int i10 = solverVariable.f14537e;
        float f10 = 1.0f;
        if (i10 != 1) {
            if (i10 == 2) {
                f10 = 1000.0f;
            } else if (i10 == 3) {
                f10 = 1000000.0f;
            } else if (i10 == 4) {
                f10 = 1.0E9f;
            } else if (i10 == 5) {
                f10 = 1.0E12f;
            }
        }
        this.f14493e.h(solverVariable, f10);
    }

    @Override // l.LinearSystem.a
    public void clear() {
        this.f14493e.clear();
        this.f14489a = null;
        this.f14490b = 0.0f;
    }

    public ArrayRow d(LinearSystem linearSystem, int i10) {
        this.f14493e.h(linearSystem.o(i10, "ep"), 1.0f);
        this.f14493e.h(linearSystem.o(i10, "em"), -1.0f);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayRow e(SolverVariable solverVariable, int i10) {
        this.f14493e.h(solverVariable, i10);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean f(LinearSystem linearSystem) {
        boolean z10;
        SolverVariable g6 = g(linearSystem);
        if (g6 == null) {
            z10 = true;
        } else {
            y(g6);
            z10 = false;
        }
        if (this.f14493e.a() == 0) {
            this.f14494f = true;
        }
        return z10;
    }

    SolverVariable g(LinearSystem linearSystem) {
        boolean v7;
        boolean v10;
        int a10 = this.f14493e.a();
        SolverVariable solverVariable = null;
        boolean z10 = false;
        boolean z11 = false;
        float f10 = 0.0f;
        float f11 = 0.0f;
        SolverVariable solverVariable2 = null;
        for (int i10 = 0; i10 < a10; i10++) {
            float f12 = this.f14493e.f(i10);
            SolverVariable c10 = this.f14493e.c(i10);
            if (c10.f14542j == SolverVariable.a.UNRESTRICTED) {
                if (solverVariable == null) {
                    v10 = v(c10, linearSystem);
                } else if (f10 > f12) {
                    v10 = v(c10, linearSystem);
                } else if (!z10 && v(c10, linearSystem)) {
                    f10 = f12;
                    solverVariable = c10;
                    z10 = true;
                }
                z10 = v10;
                f10 = f12;
                solverVariable = c10;
            } else if (solverVariable == null && f12 < 0.0f) {
                if (solverVariable2 == null) {
                    v7 = v(c10, linearSystem);
                } else if (f11 > f12) {
                    v7 = v(c10, linearSystem);
                } else if (!z11 && v(c10, linearSystem)) {
                    f11 = f12;
                    solverVariable2 = c10;
                    z11 = true;
                }
                z11 = v7;
                f11 = f12;
                solverVariable2 = c10;
            }
        }
        return solverVariable != null ? solverVariable : solverVariable2;
    }

    @Override // l.LinearSystem.a
    public SolverVariable getKey() {
        return this.f14489a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayRow h(SolverVariable solverVariable, SolverVariable solverVariable2, int i10, float f10, SolverVariable solverVariable3, SolverVariable solverVariable4, int i11) {
        if (solverVariable2 == solverVariable3) {
            this.f14493e.h(solverVariable, 1.0f);
            this.f14493e.h(solverVariable4, 1.0f);
            this.f14493e.h(solverVariable2, -2.0f);
            return this;
        }
        if (f10 == 0.5f) {
            this.f14493e.h(solverVariable, 1.0f);
            this.f14493e.h(solverVariable2, -1.0f);
            this.f14493e.h(solverVariable3, -1.0f);
            this.f14493e.h(solverVariable4, 1.0f);
            if (i10 > 0 || i11 > 0) {
                this.f14490b = (-i10) + i11;
            }
        } else if (f10 <= 0.0f) {
            this.f14493e.h(solverVariable, -1.0f);
            this.f14493e.h(solverVariable2, 1.0f);
            this.f14490b = i10;
        } else if (f10 >= 1.0f) {
            this.f14493e.h(solverVariable4, -1.0f);
            this.f14493e.h(solverVariable3, 1.0f);
            this.f14490b = -i11;
        } else {
            float f11 = 1.0f - f10;
            this.f14493e.h(solverVariable, f11 * 1.0f);
            this.f14493e.h(solverVariable2, f11 * (-1.0f));
            this.f14493e.h(solverVariable3, (-1.0f) * f10);
            this.f14493e.h(solverVariable4, 1.0f * f10);
            if (i10 > 0 || i11 > 0) {
                this.f14490b = ((-i10) * f11) + (i11 * f10);
            }
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayRow i(SolverVariable solverVariable, int i10) {
        this.f14489a = solverVariable;
        float f10 = i10;
        solverVariable.f14538f = f10;
        this.f14490b = f10;
        this.f14494f = true;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayRow j(SolverVariable solverVariable, SolverVariable solverVariable2, float f10) {
        this.f14493e.h(solverVariable, -1.0f);
        this.f14493e.h(solverVariable2, f10);
        return this;
    }

    public ArrayRow k(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f10) {
        this.f14493e.h(solverVariable, -1.0f);
        this.f14493e.h(solverVariable2, 1.0f);
        this.f14493e.h(solverVariable3, f10);
        this.f14493e.h(solverVariable4, -f10);
        return this;
    }

    public ArrayRow l(float f10, float f11, float f12, SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4) {
        this.f14490b = 0.0f;
        if (f11 == 0.0f || f10 == f12) {
            this.f14493e.h(solverVariable, 1.0f);
            this.f14493e.h(solverVariable2, -1.0f);
            this.f14493e.h(solverVariable4, 1.0f);
            this.f14493e.h(solverVariable3, -1.0f);
        } else if (f10 == 0.0f) {
            this.f14493e.h(solverVariable, 1.0f);
            this.f14493e.h(solverVariable2, -1.0f);
        } else if (f12 == 0.0f) {
            this.f14493e.h(solverVariable3, 1.0f);
            this.f14493e.h(solverVariable4, -1.0f);
        } else {
            float f13 = (f10 / f11) / (f12 / f11);
            this.f14493e.h(solverVariable, 1.0f);
            this.f14493e.h(solverVariable2, -1.0f);
            this.f14493e.h(solverVariable4, f13);
            this.f14493e.h(solverVariable3, -f13);
        }
        return this;
    }

    public ArrayRow m(SolverVariable solverVariable, int i10) {
        if (i10 < 0) {
            this.f14490b = i10 * (-1);
            this.f14493e.h(solverVariable, 1.0f);
        } else {
            this.f14490b = i10;
            this.f14493e.h(solverVariable, -1.0f);
        }
        return this;
    }

    public ArrayRow n(SolverVariable solverVariable, SolverVariable solverVariable2, int i10) {
        boolean z10 = false;
        if (i10 != 0) {
            if (i10 < 0) {
                i10 *= -1;
                z10 = true;
            }
            this.f14490b = i10;
        }
        if (!z10) {
            this.f14493e.h(solverVariable, -1.0f);
            this.f14493e.h(solverVariable2, 1.0f);
        } else {
            this.f14493e.h(solverVariable, 1.0f);
            this.f14493e.h(solverVariable2, -1.0f);
        }
        return this;
    }

    public ArrayRow o(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, int i10) {
        boolean z10 = false;
        if (i10 != 0) {
            if (i10 < 0) {
                i10 *= -1;
                z10 = true;
            }
            this.f14490b = i10;
        }
        if (!z10) {
            this.f14493e.h(solverVariable, -1.0f);
            this.f14493e.h(solverVariable2, 1.0f);
            this.f14493e.h(solverVariable3, 1.0f);
        } else {
            this.f14493e.h(solverVariable, 1.0f);
            this.f14493e.h(solverVariable2, -1.0f);
            this.f14493e.h(solverVariable3, -1.0f);
        }
        return this;
    }

    public ArrayRow p(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, int i10) {
        boolean z10 = false;
        if (i10 != 0) {
            if (i10 < 0) {
                i10 *= -1;
                z10 = true;
            }
            this.f14490b = i10;
        }
        if (!z10) {
            this.f14493e.h(solverVariable, -1.0f);
            this.f14493e.h(solverVariable2, 1.0f);
            this.f14493e.h(solverVariable3, -1.0f);
        } else {
            this.f14493e.h(solverVariable, 1.0f);
            this.f14493e.h(solverVariable2, -1.0f);
            this.f14493e.h(solverVariable3, 1.0f);
        }
        return this;
    }

    public ArrayRow q(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f10) {
        this.f14493e.h(solverVariable3, 0.5f);
        this.f14493e.h(solverVariable4, 0.5f);
        this.f14493e.h(solverVariable, -0.5f);
        this.f14493e.h(solverVariable2, -0.5f);
        this.f14490b = -f10;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r() {
        float f10 = this.f14490b;
        if (f10 < 0.0f) {
            this.f14490b = f10 * (-1.0f);
            this.f14493e.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean s() {
        SolverVariable solverVariable = this.f14489a;
        return solverVariable != null && (solverVariable.f14542j == SolverVariable.a.UNRESTRICTED || this.f14490b >= 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean t(SolverVariable solverVariable) {
        return this.f14493e.d(solverVariable);
    }

    public String toString() {
        return A();
    }

    public boolean u() {
        return this.f14489a == null && this.f14490b == 0.0f && this.f14493e.a() == 0;
    }

    public SolverVariable w(SolverVariable solverVariable) {
        return x(null, solverVariable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y(SolverVariable solverVariable) {
        SolverVariable solverVariable2 = this.f14489a;
        if (solverVariable2 != null) {
            this.f14493e.h(solverVariable2, -1.0f);
            this.f14489a = null;
        }
        float j10 = this.f14493e.j(solverVariable, true) * (-1.0f);
        this.f14489a = solverVariable;
        if (j10 == 1.0f) {
            return;
        }
        this.f14490b /= j10;
        this.f14493e.k(j10);
    }

    public void z() {
        this.f14489a = null;
        this.f14493e.clear();
        this.f14490b = 0.0f;
        this.f14494f = false;
    }

    public ArrayRow(c cVar) {
        this.f14493e = new ArrayLinkedVariables(this, cVar);
    }
}
