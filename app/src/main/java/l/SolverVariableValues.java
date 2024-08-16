package l;

import java.util.Arrays;
import l.ArrayRow;

/* compiled from: SolverVariableValues.java */
/* renamed from: l.j, reason: use source file name */
/* loaded from: classes.dex */
public class SolverVariableValues implements ArrayRow.a {

    /* renamed from: n, reason: collision with root package name */
    private static float f14553n = 0.001f;

    /* renamed from: a, reason: collision with root package name */
    private final int f14554a = -1;

    /* renamed from: b, reason: collision with root package name */
    private int f14555b = 16;

    /* renamed from: c, reason: collision with root package name */
    private int f14556c = 16;

    /* renamed from: d, reason: collision with root package name */
    int[] f14557d = new int[16];

    /* renamed from: e, reason: collision with root package name */
    int[] f14558e = new int[16];

    /* renamed from: f, reason: collision with root package name */
    int[] f14559f = new int[16];

    /* renamed from: g, reason: collision with root package name */
    float[] f14560g = new float[16];

    /* renamed from: h, reason: collision with root package name */
    int[] f14561h = new int[16];

    /* renamed from: i, reason: collision with root package name */
    int[] f14562i = new int[16];

    /* renamed from: j, reason: collision with root package name */
    int f14563j = 0;

    /* renamed from: k, reason: collision with root package name */
    int f14564k = -1;

    /* renamed from: l, reason: collision with root package name */
    private final ArrayRow f14565l;

    /* renamed from: m, reason: collision with root package name */
    protected final c f14566m;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SolverVariableValues(ArrayRow arrayRow, c cVar) {
        this.f14565l = arrayRow;
        this.f14566m = cVar;
        clear();
    }

    private void l(SolverVariable solverVariable, int i10) {
        int[] iArr;
        int i11 = solverVariable.f14535c % this.f14556c;
        int[] iArr2 = this.f14557d;
        int i12 = iArr2[i11];
        if (i12 == -1) {
            iArr2[i11] = i10;
        } else {
            while (true) {
                iArr = this.f14558e;
                if (iArr[i12] == -1) {
                    break;
                } else {
                    i12 = iArr[i12];
                }
            }
            iArr[i12] = i10;
        }
        this.f14558e[i10] = -1;
    }

    private void m(int i10, SolverVariable solverVariable, float f10) {
        this.f14559f[i10] = solverVariable.f14535c;
        this.f14560g[i10] = f10;
        this.f14561h[i10] = -1;
        this.f14562i[i10] = -1;
        solverVariable.a(this.f14565l);
        solverVariable.f14545m++;
        this.f14563j++;
    }

    private int n() {
        for (int i10 = 0; i10 < this.f14555b; i10++) {
            if (this.f14559f[i10] == -1) {
                return i10;
            }
        }
        return -1;
    }

    private void o() {
        int i10 = this.f14555b * 2;
        this.f14559f = Arrays.copyOf(this.f14559f, i10);
        this.f14560g = Arrays.copyOf(this.f14560g, i10);
        this.f14561h = Arrays.copyOf(this.f14561h, i10);
        this.f14562i = Arrays.copyOf(this.f14562i, i10);
        this.f14558e = Arrays.copyOf(this.f14558e, i10);
        for (int i11 = this.f14555b; i11 < i10; i11++) {
            this.f14559f[i11] = -1;
            this.f14558e[i11] = -1;
        }
        this.f14555b = i10;
    }

    private void q(int i10, SolverVariable solverVariable, float f10) {
        int n10 = n();
        m(n10, solverVariable, f10);
        if (i10 != -1) {
            this.f14561h[n10] = i10;
            int[] iArr = this.f14562i;
            iArr[n10] = iArr[i10];
            iArr[i10] = n10;
        } else {
            this.f14561h[n10] = -1;
            if (this.f14563j > 0) {
                this.f14562i[n10] = this.f14564k;
                this.f14564k = n10;
            } else {
                this.f14562i[n10] = -1;
            }
        }
        int[] iArr2 = this.f14562i;
        if (iArr2[n10] != -1) {
            this.f14561h[iArr2[n10]] = n10;
        }
        l(solverVariable, n10);
    }

    private void r(SolverVariable solverVariable) {
        int[] iArr;
        int i10 = solverVariable.f14535c;
        int i11 = i10 % this.f14556c;
        int[] iArr2 = this.f14557d;
        int i12 = iArr2[i11];
        if (i12 == -1) {
            return;
        }
        if (this.f14559f[i12] == i10) {
            int[] iArr3 = this.f14558e;
            iArr2[i11] = iArr3[i12];
            iArr3[i12] = -1;
            return;
        }
        while (true) {
            iArr = this.f14558e;
            if (iArr[i12] == -1 || this.f14559f[iArr[i12]] == i10) {
                break;
            } else {
                i12 = iArr[i12];
            }
        }
        int i13 = iArr[i12];
        if (i13 == -1 || this.f14559f[i13] != i10) {
            return;
        }
        iArr[i12] = iArr[i13];
        iArr[i13] = -1;
    }

    @Override // l.ArrayRow.a
    public int a() {
        return this.f14563j;
    }

    @Override // l.ArrayRow.a
    public void b(SolverVariable solverVariable, float f10, boolean z10) {
        float f11 = f14553n;
        if (f10 <= (-f11) || f10 >= f11) {
            int p10 = p(solverVariable);
            if (p10 == -1) {
                h(solverVariable, f10);
                return;
            }
            float[] fArr = this.f14560g;
            fArr[p10] = fArr[p10] + f10;
            float f12 = fArr[p10];
            float f13 = f14553n;
            if (f12 <= (-f13) || fArr[p10] >= f13) {
                return;
            }
            fArr[p10] = 0.0f;
            j(solverVariable, z10);
        }
    }

    @Override // l.ArrayRow.a
    public SolverVariable c(int i10) {
        int i11 = this.f14563j;
        if (i11 == 0) {
            return null;
        }
        int i12 = this.f14564k;
        for (int i13 = 0; i13 < i11; i13++) {
            if (i13 == i10 && i12 != -1) {
                return this.f14566m.f14498d[this.f14559f[i12]];
            }
            i12 = this.f14562i[i12];
            if (i12 == -1) {
                break;
            }
        }
        return null;
    }

    @Override // l.ArrayRow.a
    public void clear() {
        int i10 = this.f14563j;
        for (int i11 = 0; i11 < i10; i11++) {
            SolverVariable c10 = c(i11);
            if (c10 != null) {
                c10.c(this.f14565l);
            }
        }
        for (int i12 = 0; i12 < this.f14555b; i12++) {
            this.f14559f[i12] = -1;
            this.f14558e[i12] = -1;
        }
        for (int i13 = 0; i13 < this.f14556c; i13++) {
            this.f14557d[i13] = -1;
        }
        this.f14563j = 0;
        this.f14564k = -1;
    }

    @Override // l.ArrayRow.a
    public boolean d(SolverVariable solverVariable) {
        return p(solverVariable) != -1;
    }

    @Override // l.ArrayRow.a
    public void e() {
        int i10 = this.f14563j;
        int i11 = this.f14564k;
        for (int i12 = 0; i12 < i10; i12++) {
            float[] fArr = this.f14560g;
            fArr[i11] = fArr[i11] * (-1.0f);
            i11 = this.f14562i[i11];
            if (i11 == -1) {
                return;
            }
        }
    }

    @Override // l.ArrayRow.a
    public float f(int i10) {
        int i11 = this.f14563j;
        int i12 = this.f14564k;
        for (int i13 = 0; i13 < i11; i13++) {
            if (i13 == i10) {
                return this.f14560g[i12];
            }
            i12 = this.f14562i[i12];
            if (i12 == -1) {
                return 0.0f;
            }
        }
        return 0.0f;
    }

    @Override // l.ArrayRow.a
    public float g(ArrayRow arrayRow, boolean z10) {
        float i10 = i(arrayRow.f14489a);
        j(arrayRow.f14489a, z10);
        SolverVariableValues solverVariableValues = (SolverVariableValues) arrayRow.f14493e;
        int a10 = solverVariableValues.a();
        int i11 = 0;
        int i12 = 0;
        while (i11 < a10) {
            int[] iArr = solverVariableValues.f14559f;
            if (iArr[i12] != -1) {
                b(this.f14566m.f14498d[iArr[i12]], solverVariableValues.f14560g[i12] * i10, z10);
                i11++;
            }
            i12++;
        }
        return i10;
    }

    @Override // l.ArrayRow.a
    public void h(SolverVariable solverVariable, float f10) {
        float f11 = f14553n;
        if (f10 > (-f11) && f10 < f11) {
            j(solverVariable, true);
            return;
        }
        if (this.f14563j == 0) {
            m(0, solverVariable, f10);
            l(solverVariable, 0);
            this.f14564k = 0;
            return;
        }
        int p10 = p(solverVariable);
        if (p10 != -1) {
            this.f14560g[p10] = f10;
            return;
        }
        if (this.f14563j + 1 >= this.f14555b) {
            o();
        }
        int i10 = this.f14563j;
        int i11 = this.f14564k;
        int i12 = -1;
        for (int i13 = 0; i13 < i10; i13++) {
            int[] iArr = this.f14559f;
            int i14 = iArr[i11];
            int i15 = solverVariable.f14535c;
            if (i14 == i15) {
                this.f14560g[i11] = f10;
                return;
            }
            if (iArr[i11] < i15) {
                i12 = i11;
            }
            i11 = this.f14562i[i11];
            if (i11 == -1) {
                break;
            }
        }
        q(i12, solverVariable, f10);
    }

    @Override // l.ArrayRow.a
    public float i(SolverVariable solverVariable) {
        int p10 = p(solverVariable);
        if (p10 != -1) {
            return this.f14560g[p10];
        }
        return 0.0f;
    }

    @Override // l.ArrayRow.a
    public float j(SolverVariable solverVariable, boolean z10) {
        int p10 = p(solverVariable);
        if (p10 == -1) {
            return 0.0f;
        }
        r(solverVariable);
        float f10 = this.f14560g[p10];
        if (this.f14564k == p10) {
            this.f14564k = this.f14562i[p10];
        }
        this.f14559f[p10] = -1;
        int[] iArr = this.f14561h;
        if (iArr[p10] != -1) {
            int[] iArr2 = this.f14562i;
            iArr2[iArr[p10]] = iArr2[p10];
        }
        int[] iArr3 = this.f14562i;
        if (iArr3[p10] != -1) {
            iArr[iArr3[p10]] = iArr[p10];
        }
        this.f14563j--;
        solverVariable.f14545m--;
        if (z10) {
            solverVariable.c(this.f14565l);
        }
        return f10;
    }

    @Override // l.ArrayRow.a
    public void k(float f10) {
        int i10 = this.f14563j;
        int i11 = this.f14564k;
        for (int i12 = 0; i12 < i10; i12++) {
            float[] fArr = this.f14560g;
            fArr[i11] = fArr[i11] / f10;
            i11 = this.f14562i[i11];
            if (i11 == -1) {
                return;
            }
        }
    }

    public int p(SolverVariable solverVariable) {
        int[] iArr;
        if (this.f14563j == 0) {
            return -1;
        }
        int i10 = solverVariable.f14535c;
        int i11 = this.f14557d[i10 % this.f14556c];
        if (i11 == -1) {
            return -1;
        }
        if (this.f14559f[i11] == i10) {
            return i11;
        }
        while (true) {
            iArr = this.f14558e;
            if (iArr[i11] == -1 || this.f14559f[iArr[i11]] == i10) {
                break;
            }
            i11 = iArr[i11];
        }
        if (iArr[i11] != -1 && this.f14559f[iArr[i11]] == i10) {
            return iArr[i11];
        }
        return -1;
    }

    public String toString() {
        String str = hashCode() + " { ";
        int i10 = this.f14563j;
        for (int i11 = 0; i11 < i10; i11++) {
            SolverVariable c10 = c(i11);
            if (c10 != null) {
                String str2 = str + c10 + " = " + f(i11) + " ";
                int p10 = p(c10);
                String str3 = str2 + "[p: ";
                String str4 = (this.f14561h[p10] != -1 ? str3 + this.f14566m.f14498d[this.f14559f[this.f14561h[p10]]] : str3 + "none") + ", n: ";
                str = (this.f14562i[p10] != -1 ? str4 + this.f14566m.f14498d[this.f14559f[this.f14562i[p10]]] : str4 + "none") + "]";
            }
        }
        return str + " }";
    }
}
