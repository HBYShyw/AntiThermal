package l;

import java.util.Arrays;
import java.util.HashSet;

/* compiled from: SolverVariable.java */
/* renamed from: l.i, reason: use source file name */
/* loaded from: classes.dex */
public class SolverVariable {

    /* renamed from: o, reason: collision with root package name */
    private static int f14532o = 1;

    /* renamed from: a, reason: collision with root package name */
    public boolean f14533a;

    /* renamed from: b, reason: collision with root package name */
    private String f14534b;

    /* renamed from: f, reason: collision with root package name */
    public float f14538f;

    /* renamed from: j, reason: collision with root package name */
    a f14542j;

    /* renamed from: c, reason: collision with root package name */
    public int f14535c = -1;

    /* renamed from: d, reason: collision with root package name */
    int f14536d = -1;

    /* renamed from: e, reason: collision with root package name */
    public int f14537e = 0;

    /* renamed from: g, reason: collision with root package name */
    public boolean f14539g = false;

    /* renamed from: h, reason: collision with root package name */
    float[] f14540h = new float[9];

    /* renamed from: i, reason: collision with root package name */
    float[] f14541i = new float[9];

    /* renamed from: k, reason: collision with root package name */
    ArrayRow[] f14543k = new ArrayRow[16];

    /* renamed from: l, reason: collision with root package name */
    int f14544l = 0;

    /* renamed from: m, reason: collision with root package name */
    public int f14545m = 0;

    /* renamed from: n, reason: collision with root package name */
    HashSet<ArrayRow> f14546n = null;

    /* compiled from: SolverVariable.java */
    /* renamed from: l.i$a */
    /* loaded from: classes.dex */
    public enum a {
        UNRESTRICTED,
        CONSTANT,
        SLACK,
        ERROR,
        UNKNOWN
    }

    public SolverVariable(a aVar, String str) {
        this.f14542j = aVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b() {
        f14532o++;
    }

    public final void a(ArrayRow arrayRow) {
        int i10 = 0;
        while (true) {
            int i11 = this.f14544l;
            if (i10 < i11) {
                if (this.f14543k[i10] == arrayRow) {
                    return;
                } else {
                    i10++;
                }
            } else {
                ArrayRow[] arrayRowArr = this.f14543k;
                if (i11 >= arrayRowArr.length) {
                    this.f14543k = (ArrayRow[]) Arrays.copyOf(arrayRowArr, arrayRowArr.length * 2);
                }
                ArrayRow[] arrayRowArr2 = this.f14543k;
                int i12 = this.f14544l;
                arrayRowArr2[i12] = arrayRow;
                this.f14544l = i12 + 1;
                return;
            }
        }
    }

    public final void c(ArrayRow arrayRow) {
        int i10 = this.f14544l;
        int i11 = 0;
        while (i11 < i10) {
            if (this.f14543k[i11] == arrayRow) {
                while (i11 < i10 - 1) {
                    ArrayRow[] arrayRowArr = this.f14543k;
                    int i12 = i11 + 1;
                    arrayRowArr[i11] = arrayRowArr[i12];
                    i11 = i12;
                }
                this.f14544l--;
                return;
            }
            i11++;
        }
    }

    public void d() {
        this.f14534b = null;
        this.f14542j = a.UNKNOWN;
        this.f14537e = 0;
        this.f14535c = -1;
        this.f14536d = -1;
        this.f14538f = 0.0f;
        this.f14539g = false;
        int i10 = this.f14544l;
        for (int i11 = 0; i11 < i10; i11++) {
            this.f14543k[i11] = null;
        }
        this.f14544l = 0;
        this.f14545m = 0;
        this.f14533a = false;
        Arrays.fill(this.f14541i, 0.0f);
    }

    public void e(LinearSystem linearSystem, float f10) {
        this.f14538f = f10;
        this.f14539g = true;
        int i10 = this.f14544l;
        for (int i11 = 0; i11 < i10; i11++) {
            this.f14543k[i11].B(linearSystem, this, false);
        }
        this.f14544l = 0;
    }

    public void f(a aVar, String str) {
        this.f14542j = aVar;
    }

    public final void g(ArrayRow arrayRow) {
        int i10 = this.f14544l;
        for (int i11 = 0; i11 < i10; i11++) {
            this.f14543k[i11].C(arrayRow, false);
        }
        this.f14544l = 0;
    }

    public String toString() {
        if (this.f14534b != null) {
            return "" + this.f14534b;
        }
        return "" + this.f14535c;
    }
}
