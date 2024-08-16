package l;

import java.util.Arrays;
import l.ArrayRow;

/* compiled from: ArrayLinkedVariables.java */
/* renamed from: l.a, reason: use source file name */
/* loaded from: classes.dex */
public class ArrayLinkedVariables implements ArrayRow.a {

    /* renamed from: l, reason: collision with root package name */
    private static float f14477l = 0.001f;

    /* renamed from: b, reason: collision with root package name */
    private final ArrayRow f14479b;

    /* renamed from: c, reason: collision with root package name */
    protected final c f14480c;

    /* renamed from: a, reason: collision with root package name */
    int f14478a = 0;

    /* renamed from: d, reason: collision with root package name */
    private int f14481d = 8;

    /* renamed from: e, reason: collision with root package name */
    private SolverVariable f14482e = null;

    /* renamed from: f, reason: collision with root package name */
    private int[] f14483f = new int[8];

    /* renamed from: g, reason: collision with root package name */
    private int[] f14484g = new int[8];

    /* renamed from: h, reason: collision with root package name */
    private float[] f14485h = new float[8];

    /* renamed from: i, reason: collision with root package name */
    private int f14486i = -1;

    /* renamed from: j, reason: collision with root package name */
    private int f14487j = -1;

    /* renamed from: k, reason: collision with root package name */
    private boolean f14488k = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayLinkedVariables(ArrayRow arrayRow, c cVar) {
        this.f14479b = arrayRow;
        this.f14480c = cVar;
    }

    @Override // l.ArrayRow.a
    public int a() {
        return this.f14478a;
    }

    @Override // l.ArrayRow.a
    public void b(SolverVariable solverVariable, float f10, boolean z10) {
        float f11 = f14477l;
        if (f10 <= (-f11) || f10 >= f11) {
            int i10 = this.f14486i;
            if (i10 == -1) {
                this.f14486i = 0;
                this.f14485h[0] = f10;
                this.f14483f[0] = solverVariable.f14535c;
                this.f14484g[0] = -1;
                solverVariable.f14545m++;
                solverVariable.a(this.f14479b);
                this.f14478a++;
                if (this.f14488k) {
                    return;
                }
                int i11 = this.f14487j + 1;
                this.f14487j = i11;
                int[] iArr = this.f14483f;
                if (i11 >= iArr.length) {
                    this.f14488k = true;
                    this.f14487j = iArr.length - 1;
                    return;
                }
                return;
            }
            int i12 = -1;
            for (int i13 = 0; i10 != -1 && i13 < this.f14478a; i13++) {
                int[] iArr2 = this.f14483f;
                int i14 = iArr2[i10];
                int i15 = solverVariable.f14535c;
                if (i14 == i15) {
                    float[] fArr = this.f14485h;
                    float f12 = fArr[i10] + f10;
                    float f13 = f14477l;
                    if (f12 > (-f13) && f12 < f13) {
                        f12 = 0.0f;
                    }
                    fArr[i10] = f12;
                    if (f12 == 0.0f) {
                        if (i10 == this.f14486i) {
                            this.f14486i = this.f14484g[i10];
                        } else {
                            int[] iArr3 = this.f14484g;
                            iArr3[i12] = iArr3[i10];
                        }
                        if (z10) {
                            solverVariable.c(this.f14479b);
                        }
                        if (this.f14488k) {
                            this.f14487j = i10;
                        }
                        solverVariable.f14545m--;
                        this.f14478a--;
                        return;
                    }
                    return;
                }
                if (iArr2[i10] < i15) {
                    i12 = i10;
                }
                i10 = this.f14484g[i10];
            }
            int i16 = this.f14487j;
            int i17 = i16 + 1;
            if (this.f14488k) {
                int[] iArr4 = this.f14483f;
                if (iArr4[i16] != -1) {
                    i16 = iArr4.length;
                }
            } else {
                i16 = i17;
            }
            int[] iArr5 = this.f14483f;
            if (i16 >= iArr5.length && this.f14478a < iArr5.length) {
                int i18 = 0;
                while (true) {
                    int[] iArr6 = this.f14483f;
                    if (i18 >= iArr6.length) {
                        break;
                    }
                    if (iArr6[i18] == -1) {
                        i16 = i18;
                        break;
                    }
                    i18++;
                }
            }
            int[] iArr7 = this.f14483f;
            if (i16 >= iArr7.length) {
                i16 = iArr7.length;
                int i19 = this.f14481d * 2;
                this.f14481d = i19;
                this.f14488k = false;
                this.f14487j = i16 - 1;
                this.f14485h = Arrays.copyOf(this.f14485h, i19);
                this.f14483f = Arrays.copyOf(this.f14483f, this.f14481d);
                this.f14484g = Arrays.copyOf(this.f14484g, this.f14481d);
            }
            this.f14483f[i16] = solverVariable.f14535c;
            this.f14485h[i16] = f10;
            if (i12 != -1) {
                int[] iArr8 = this.f14484g;
                iArr8[i16] = iArr8[i12];
                iArr8[i12] = i16;
            } else {
                this.f14484g[i16] = this.f14486i;
                this.f14486i = i16;
            }
            solverVariable.f14545m++;
            solverVariable.a(this.f14479b);
            this.f14478a++;
            if (!this.f14488k) {
                this.f14487j++;
            }
            int i20 = this.f14487j;
            int[] iArr9 = this.f14483f;
            if (i20 >= iArr9.length) {
                this.f14488k = true;
                this.f14487j = iArr9.length - 1;
            }
        }
    }

    @Override // l.ArrayRow.a
    public SolverVariable c(int i10) {
        int i11 = this.f14486i;
        for (int i12 = 0; i11 != -1 && i12 < this.f14478a; i12++) {
            if (i12 == i10) {
                return this.f14480c.f14498d[this.f14483f[i11]];
            }
            i11 = this.f14484g[i11];
        }
        return null;
    }

    @Override // l.ArrayRow.a
    public final void clear() {
        int i10 = this.f14486i;
        for (int i11 = 0; i10 != -1 && i11 < this.f14478a; i11++) {
            SolverVariable solverVariable = this.f14480c.f14498d[this.f14483f[i10]];
            if (solverVariable != null) {
                solverVariable.c(this.f14479b);
            }
            i10 = this.f14484g[i10];
        }
        this.f14486i = -1;
        this.f14487j = -1;
        this.f14488k = false;
        this.f14478a = 0;
    }

    @Override // l.ArrayRow.a
    public boolean d(SolverVariable solverVariable) {
        int i10 = this.f14486i;
        if (i10 == -1) {
            return false;
        }
        for (int i11 = 0; i10 != -1 && i11 < this.f14478a; i11++) {
            if (this.f14483f[i10] == solverVariable.f14535c) {
                return true;
            }
            i10 = this.f14484g[i10];
        }
        return false;
    }

    @Override // l.ArrayRow.a
    public void e() {
        int i10 = this.f14486i;
        for (int i11 = 0; i10 != -1 && i11 < this.f14478a; i11++) {
            float[] fArr = this.f14485h;
            fArr[i10] = fArr[i10] * (-1.0f);
            i10 = this.f14484g[i10];
        }
    }

    @Override // l.ArrayRow.a
    public float f(int i10) {
        int i11 = this.f14486i;
        for (int i12 = 0; i11 != -1 && i12 < this.f14478a; i12++) {
            if (i12 == i10) {
                return this.f14485h[i11];
            }
            i11 = this.f14484g[i11];
        }
        return 0.0f;
    }

    @Override // l.ArrayRow.a
    public float g(ArrayRow arrayRow, boolean z10) {
        float i10 = i(arrayRow.f14489a);
        j(arrayRow.f14489a, z10);
        ArrayRow.a aVar = arrayRow.f14493e;
        int a10 = aVar.a();
        for (int i11 = 0; i11 < a10; i11++) {
            SolverVariable c10 = aVar.c(i11);
            b(c10, aVar.i(c10) * i10, z10);
        }
        return i10;
    }

    @Override // l.ArrayRow.a
    public final void h(SolverVariable solverVariable, float f10) {
        if (f10 == 0.0f) {
            j(solverVariable, true);
            return;
        }
        int i10 = this.f14486i;
        if (i10 == -1) {
            this.f14486i = 0;
            this.f14485h[0] = f10;
            this.f14483f[0] = solverVariable.f14535c;
            this.f14484g[0] = -1;
            solverVariable.f14545m++;
            solverVariable.a(this.f14479b);
            this.f14478a++;
            if (this.f14488k) {
                return;
            }
            int i11 = this.f14487j + 1;
            this.f14487j = i11;
            int[] iArr = this.f14483f;
            if (i11 >= iArr.length) {
                this.f14488k = true;
                this.f14487j = iArr.length - 1;
                return;
            }
            return;
        }
        int i12 = -1;
        for (int i13 = 0; i10 != -1 && i13 < this.f14478a; i13++) {
            int[] iArr2 = this.f14483f;
            int i14 = iArr2[i10];
            int i15 = solverVariable.f14535c;
            if (i14 == i15) {
                this.f14485h[i10] = f10;
                return;
            }
            if (iArr2[i10] < i15) {
                i12 = i10;
            }
            i10 = this.f14484g[i10];
        }
        int i16 = this.f14487j;
        int i17 = i16 + 1;
        if (this.f14488k) {
            int[] iArr3 = this.f14483f;
            if (iArr3[i16] != -1) {
                i16 = iArr3.length;
            }
        } else {
            i16 = i17;
        }
        int[] iArr4 = this.f14483f;
        if (i16 >= iArr4.length && this.f14478a < iArr4.length) {
            int i18 = 0;
            while (true) {
                int[] iArr5 = this.f14483f;
                if (i18 >= iArr5.length) {
                    break;
                }
                if (iArr5[i18] == -1) {
                    i16 = i18;
                    break;
                }
                i18++;
            }
        }
        int[] iArr6 = this.f14483f;
        if (i16 >= iArr6.length) {
            i16 = iArr6.length;
            int i19 = this.f14481d * 2;
            this.f14481d = i19;
            this.f14488k = false;
            this.f14487j = i16 - 1;
            this.f14485h = Arrays.copyOf(this.f14485h, i19);
            this.f14483f = Arrays.copyOf(this.f14483f, this.f14481d);
            this.f14484g = Arrays.copyOf(this.f14484g, this.f14481d);
        }
        this.f14483f[i16] = solverVariable.f14535c;
        this.f14485h[i16] = f10;
        if (i12 != -1) {
            int[] iArr7 = this.f14484g;
            iArr7[i16] = iArr7[i12];
            iArr7[i12] = i16;
        } else {
            this.f14484g[i16] = this.f14486i;
            this.f14486i = i16;
        }
        solverVariable.f14545m++;
        solverVariable.a(this.f14479b);
        int i20 = this.f14478a + 1;
        this.f14478a = i20;
        if (!this.f14488k) {
            this.f14487j++;
        }
        int[] iArr8 = this.f14483f;
        if (i20 >= iArr8.length) {
            this.f14488k = true;
        }
        if (this.f14487j >= iArr8.length) {
            this.f14488k = true;
            this.f14487j = iArr8.length - 1;
        }
    }

    @Override // l.ArrayRow.a
    public final float i(SolverVariable solverVariable) {
        int i10 = this.f14486i;
        for (int i11 = 0; i10 != -1 && i11 < this.f14478a; i11++) {
            if (this.f14483f[i10] == solverVariable.f14535c) {
                return this.f14485h[i10];
            }
            i10 = this.f14484g[i10];
        }
        return 0.0f;
    }

    @Override // l.ArrayRow.a
    public final float j(SolverVariable solverVariable, boolean z10) {
        if (this.f14482e == solverVariable) {
            this.f14482e = null;
        }
        int i10 = this.f14486i;
        if (i10 == -1) {
            return 0.0f;
        }
        int i11 = 0;
        int i12 = -1;
        while (i10 != -1 && i11 < this.f14478a) {
            if (this.f14483f[i10] == solverVariable.f14535c) {
                if (i10 == this.f14486i) {
                    this.f14486i = this.f14484g[i10];
                } else {
                    int[] iArr = this.f14484g;
                    iArr[i12] = iArr[i10];
                }
                if (z10) {
                    solverVariable.c(this.f14479b);
                }
                solverVariable.f14545m--;
                this.f14478a--;
                this.f14483f[i10] = -1;
                if (this.f14488k) {
                    this.f14487j = i10;
                }
                return this.f14485h[i10];
            }
            i11++;
            i12 = i10;
            i10 = this.f14484g[i10];
        }
        return 0.0f;
    }

    @Override // l.ArrayRow.a
    public void k(float f10) {
        int i10 = this.f14486i;
        for (int i11 = 0; i10 != -1 && i11 < this.f14478a; i11++) {
            float[] fArr = this.f14485h;
            fArr[i10] = fArr[i10] / f10;
            i10 = this.f14484g[i10];
        }
    }

    public String toString() {
        int i10 = this.f14486i;
        String str = "";
        for (int i11 = 0; i10 != -1 && i11 < this.f14478a; i11++) {
            str = ((str + " -> ") + this.f14485h[i10] + " : ") + this.f14480c.f14498d[this.f14483f[i10]];
            i10 = this.f14484g[i10];
        }
        return str;
    }
}
