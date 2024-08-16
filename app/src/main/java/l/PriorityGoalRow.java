package l;

import java.util.Arrays;
import java.util.Comparator;
import l.ArrayRow;

/* compiled from: PriorityGoalRow.java */
/* renamed from: l.h, reason: use source file name */
/* loaded from: classes.dex */
public class PriorityGoalRow extends ArrayRow {

    /* renamed from: g, reason: collision with root package name */
    private int f14522g;

    /* renamed from: h, reason: collision with root package name */
    private SolverVariable[] f14523h;

    /* renamed from: i, reason: collision with root package name */
    private SolverVariable[] f14524i;

    /* renamed from: j, reason: collision with root package name */
    private int f14525j;

    /* renamed from: k, reason: collision with root package name */
    b f14526k;

    /* renamed from: l, reason: collision with root package name */
    c f14527l;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PriorityGoalRow.java */
    /* renamed from: l.h$a */
    /* loaded from: classes.dex */
    public class a implements Comparator<SolverVariable> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(SolverVariable solverVariable, SolverVariable solverVariable2) {
            return solverVariable.f14535c - solverVariable2.f14535c;
        }
    }

    /* compiled from: PriorityGoalRow.java */
    /* renamed from: l.h$b */
    /* loaded from: classes.dex */
    class b implements Comparable {

        /* renamed from: e, reason: collision with root package name */
        SolverVariable f14529e;

        /* renamed from: f, reason: collision with root package name */
        PriorityGoalRow f14530f;

        public b(PriorityGoalRow priorityGoalRow) {
            this.f14530f = priorityGoalRow;
        }

        public boolean a(SolverVariable solverVariable, float f10) {
            boolean z10 = true;
            if (!this.f14529e.f14533a) {
                for (int i10 = 0; i10 < 9; i10++) {
                    float f11 = solverVariable.f14541i[i10];
                    if (f11 != 0.0f) {
                        float f12 = f11 * f10;
                        if (Math.abs(f12) < 1.0E-4f) {
                            f12 = 0.0f;
                        }
                        this.f14529e.f14541i[i10] = f12;
                    } else {
                        this.f14529e.f14541i[i10] = 0.0f;
                    }
                }
                return true;
            }
            for (int i11 = 0; i11 < 9; i11++) {
                float[] fArr = this.f14529e.f14541i;
                fArr[i11] = fArr[i11] + (solverVariable.f14541i[i11] * f10);
                if (Math.abs(fArr[i11]) < 1.0E-4f) {
                    this.f14529e.f14541i[i11] = 0.0f;
                } else {
                    z10 = false;
                }
            }
            if (z10) {
                PriorityGoalRow.this.G(this.f14529e);
            }
            return false;
        }

        public void b(SolverVariable solverVariable) {
            this.f14529e = solverVariable;
        }

        public final boolean c() {
            for (int i10 = 8; i10 >= 0; i10--) {
                float f10 = this.f14529e.f14541i[i10];
                if (f10 > 0.0f) {
                    return false;
                }
                if (f10 < 0.0f) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.lang.Comparable
        public int compareTo(Object obj) {
            return this.f14529e.f14535c - ((SolverVariable) obj).f14535c;
        }

        public final boolean d(SolverVariable solverVariable) {
            int i10 = 8;
            while (true) {
                if (i10 < 0) {
                    break;
                }
                float f10 = solverVariable.f14541i[i10];
                float f11 = this.f14529e.f14541i[i10];
                if (f11 == f10) {
                    i10--;
                } else if (f11 < f10) {
                    return true;
                }
            }
            return false;
        }

        public void e() {
            Arrays.fill(this.f14529e.f14541i, 0.0f);
        }

        public String toString() {
            String str = "[ ";
            if (this.f14529e != null) {
                for (int i10 = 0; i10 < 9; i10++) {
                    str = str + this.f14529e.f14541i[i10] + " ";
                }
            }
            return str + "] " + this.f14529e;
        }
    }

    public PriorityGoalRow(c cVar) {
        super(cVar);
        this.f14522g = 128;
        this.f14523h = new SolverVariable[128];
        this.f14524i = new SolverVariable[128];
        this.f14525j = 0;
        this.f14526k = new b(this);
        this.f14527l = cVar;
    }

    private final void F(SolverVariable solverVariable) {
        int i10;
        int i11 = this.f14525j + 1;
        SolverVariable[] solverVariableArr = this.f14523h;
        if (i11 > solverVariableArr.length) {
            SolverVariable[] solverVariableArr2 = (SolverVariable[]) Arrays.copyOf(solverVariableArr, solverVariableArr.length * 2);
            this.f14523h = solverVariableArr2;
            this.f14524i = (SolverVariable[]) Arrays.copyOf(solverVariableArr2, solverVariableArr2.length * 2);
        }
        SolverVariable[] solverVariableArr3 = this.f14523h;
        int i12 = this.f14525j;
        solverVariableArr3[i12] = solverVariable;
        int i13 = i12 + 1;
        this.f14525j = i13;
        if (i13 > 1 && solverVariableArr3[i13 - 1].f14535c > solverVariable.f14535c) {
            int i14 = 0;
            while (true) {
                i10 = this.f14525j;
                if (i14 >= i10) {
                    break;
                }
                this.f14524i[i14] = this.f14523h[i14];
                i14++;
            }
            Arrays.sort(this.f14524i, 0, i10, new a());
            for (int i15 = 0; i15 < this.f14525j; i15++) {
                this.f14523h[i15] = this.f14524i[i15];
            }
        }
        solverVariable.f14533a = true;
        solverVariable.a(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void G(SolverVariable solverVariable) {
        int i10 = 0;
        while (i10 < this.f14525j) {
            if (this.f14523h[i10] == solverVariable) {
                while (true) {
                    int i11 = this.f14525j;
                    if (i10 < i11 - 1) {
                        SolverVariable[] solverVariableArr = this.f14523h;
                        int i12 = i10 + 1;
                        solverVariableArr[i10] = solverVariableArr[i12];
                        i10 = i12;
                    } else {
                        this.f14525j = i11 - 1;
                        solverVariable.f14533a = false;
                        return;
                    }
                }
            } else {
                i10++;
            }
        }
    }

    @Override // l.ArrayRow
    public void C(ArrayRow arrayRow, boolean z10) {
        SolverVariable solverVariable = arrayRow.f14489a;
        if (solverVariable == null) {
            return;
        }
        ArrayRow.a aVar = arrayRow.f14493e;
        int a10 = aVar.a();
        for (int i10 = 0; i10 < a10; i10++) {
            SolverVariable c10 = aVar.c(i10);
            float f10 = aVar.f(i10);
            this.f14526k.b(c10);
            if (this.f14526k.a(solverVariable, f10)) {
                F(c10);
            }
            this.f14490b += arrayRow.f14490b * f10;
        }
        G(solverVariable);
    }

    @Override // l.ArrayRow, l.LinearSystem.a
    public SolverVariable a(LinearSystem linearSystem, boolean[] zArr) {
        int i10 = -1;
        for (int i11 = 0; i11 < this.f14525j; i11++) {
            SolverVariable solverVariable = this.f14523h[i11];
            if (!zArr[solverVariable.f14535c]) {
                this.f14526k.b(solverVariable);
                if (i10 == -1) {
                    if (!this.f14526k.c()) {
                    }
                    i10 = i11;
                } else {
                    if (!this.f14526k.d(this.f14523h[i10])) {
                    }
                    i10 = i11;
                }
            }
        }
        if (i10 == -1) {
            return null;
        }
        return this.f14523h[i10];
    }

    @Override // l.ArrayRow, l.LinearSystem.a
    public void c(SolverVariable solverVariable) {
        this.f14526k.b(solverVariable);
        this.f14526k.e();
        solverVariable.f14541i[solverVariable.f14537e] = 1.0f;
        F(solverVariable);
    }

    @Override // l.ArrayRow, l.LinearSystem.a
    public void clear() {
        this.f14525j = 0;
        this.f14490b = 0.0f;
    }

    @Override // l.ArrayRow
    public String toString() {
        String str = " goal -> (" + this.f14490b + ") : ";
        for (int i10 = 0; i10 < this.f14525j; i10++) {
            this.f14526k.b(this.f14523h[i10]);
            str = str + this.f14526k + " ";
        }
        return str;
    }
}
