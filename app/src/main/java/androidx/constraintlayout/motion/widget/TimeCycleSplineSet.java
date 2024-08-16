package androidx.constraintlayout.motion.widget;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintAttribute;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import k.CurveFit;

/* compiled from: TimeCycleSplineSet.java */
/* renamed from: androidx.constraintlayout.motion.widget.s, reason: use source file name */
/* loaded from: classes.dex */
public abstract class TimeCycleSplineSet {

    /* renamed from: k, reason: collision with root package name */
    private static float f1722k = 6.2831855f;

    /* renamed from: a, reason: collision with root package name */
    protected CurveFit f1723a;

    /* renamed from: e, reason: collision with root package name */
    private int f1727e;

    /* renamed from: f, reason: collision with root package name */
    private String f1728f;

    /* renamed from: i, reason: collision with root package name */
    long f1731i;

    /* renamed from: b, reason: collision with root package name */
    protected int f1724b = 0;

    /* renamed from: c, reason: collision with root package name */
    protected int[] f1725c = new int[10];

    /* renamed from: d, reason: collision with root package name */
    protected float[][] f1726d = (float[][]) Array.newInstance((Class<?>) float.class, 10, 3);

    /* renamed from: g, reason: collision with root package name */
    private float[] f1729g = new float[3];

    /* renamed from: h, reason: collision with root package name */
    protected boolean f1730h = false;

    /* renamed from: j, reason: collision with root package name */
    float f1732j = Float.NaN;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$a */
    /* loaded from: classes.dex */
    public static class a extends TimeCycleSplineSet {
        a() {
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            view.setAlpha(b(f10, j10, view, keyCache));
            return this.f1730h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$b */
    /* loaded from: classes.dex */
    public static class b extends TimeCycleSplineSet {

        /* renamed from: l, reason: collision with root package name */
        String f1733l;

        /* renamed from: m, reason: collision with root package name */
        SparseArray<ConstraintAttribute> f1734m;

        /* renamed from: n, reason: collision with root package name */
        SparseArray<float[]> f1735n = new SparseArray<>();

        /* renamed from: o, reason: collision with root package name */
        float[] f1736o;

        /* renamed from: p, reason: collision with root package name */
        float[] f1737p;

        public b(String str, SparseArray<ConstraintAttribute> sparseArray) {
            this.f1733l = str.split(",")[1];
            this.f1734m = sparseArray;
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public void e(int i10, float f10, float f11, int i11, float f12) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute,...)");
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            this.f1723a.e(f10, this.f1736o);
            float[] fArr = this.f1736o;
            float f11 = fArr[fArr.length - 2];
            float f12 = fArr[fArr.length - 1];
            float f13 = (float) ((this.f1732j + (((j10 - this.f1731i) * 1.0E-9d) * f11)) % 1.0d);
            this.f1732j = f13;
            this.f1731i = j10;
            float a10 = a(f13);
            this.f1730h = false;
            int i10 = 0;
            while (true) {
                float[] fArr2 = this.f1737p;
                if (i10 >= fArr2.length) {
                    break;
                }
                boolean z10 = this.f1730h;
                float[] fArr3 = this.f1736o;
                this.f1730h = z10 | (((double) fArr3[i10]) != UserProfileInfo.Constant.NA_LAT_LON);
                fArr2[i10] = (fArr3[i10] * a10) + f12;
                i10++;
            }
            this.f1734m.valueAt(0).k(view, this.f1737p);
            if (f11 != 0.0f) {
                this.f1730h = true;
            }
            return this.f1730h;
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public void i(int i10) {
            int size = this.f1734m.size();
            int f10 = this.f1734m.valueAt(0).f();
            double[] dArr = new double[size];
            int i11 = f10 + 2;
            this.f1736o = new float[i11];
            this.f1737p = new float[f10];
            double[][] dArr2 = (double[][]) Array.newInstance((Class<?>) double.class, size, i11);
            for (int i12 = 0; i12 < size; i12++) {
                int keyAt = this.f1734m.keyAt(i12);
                ConstraintAttribute valueAt = this.f1734m.valueAt(i12);
                float[] valueAt2 = this.f1735n.valueAt(i12);
                dArr[i12] = keyAt * 0.01d;
                valueAt.e(this.f1736o);
                int i13 = 0;
                while (true) {
                    if (i13 < this.f1736o.length) {
                        dArr2[i12][i13] = r8[i13];
                        i13++;
                    }
                }
                dArr2[i12][f10] = valueAt2[0];
                dArr2[i12][f10 + 1] = valueAt2[1];
            }
            this.f1723a = CurveFit.a(i10, dArr, dArr2);
        }

        public void j(int i10, ConstraintAttribute constraintAttribute, float f10, int i11, float f11) {
            this.f1734m.append(i10, constraintAttribute);
            this.f1735n.append(i10, new float[]{f10, f11});
            this.f1724b = Math.max(this.f1724b, i11);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$c */
    /* loaded from: classes.dex */
    public static class c extends TimeCycleSplineSet {
        c() {
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            view.setElevation(b(f10, j10, view, keyCache));
            return this.f1730h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$d */
    /* loaded from: classes.dex */
    public static class d extends TimeCycleSplineSet {
        d() {
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            return this.f1730h;
        }

        public boolean j(View view, KeyCache keyCache, float f10, long j10, double d10, double d11) {
            view.setRotation(b(f10, j10, view, keyCache) + ((float) Math.toDegrees(Math.atan2(d11, d10))));
            return this.f1730h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$e */
    /* loaded from: classes.dex */
    public static class e extends TimeCycleSplineSet {

        /* renamed from: l, reason: collision with root package name */
        boolean f1738l = false;

        e() {
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            if (view instanceof MotionLayout) {
                ((MotionLayout) view).setProgress(b(f10, j10, view, keyCache));
            } else {
                if (this.f1738l) {
                    return false;
                }
                Method method = null;
                try {
                    method = view.getClass().getMethod("setProgress", Float.TYPE);
                } catch (NoSuchMethodException unused) {
                    this.f1738l = true;
                }
                Method method2 = method;
                if (method2 != null) {
                    try {
                        method2.invoke(view, Float.valueOf(b(f10, j10, view, keyCache)));
                    } catch (IllegalAccessException e10) {
                        Log.e("SplineSet", "unable to setProgress", e10);
                    } catch (InvocationTargetException e11) {
                        Log.e("SplineSet", "unable to setProgress", e11);
                    }
                }
            }
            return this.f1730h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$f */
    /* loaded from: classes.dex */
    public static class f extends TimeCycleSplineSet {
        f() {
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            view.setRotation(b(f10, j10, view, keyCache));
            return this.f1730h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$g */
    /* loaded from: classes.dex */
    public static class g extends TimeCycleSplineSet {
        g() {
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            view.setRotationX(b(f10, j10, view, keyCache));
            return this.f1730h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$h */
    /* loaded from: classes.dex */
    public static class h extends TimeCycleSplineSet {
        h() {
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            view.setRotationY(b(f10, j10, view, keyCache));
            return this.f1730h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$i */
    /* loaded from: classes.dex */
    public static class i extends TimeCycleSplineSet {
        i() {
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            view.setScaleX(b(f10, j10, view, keyCache));
            return this.f1730h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$j */
    /* loaded from: classes.dex */
    public static class j extends TimeCycleSplineSet {
        j() {
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            view.setScaleY(b(f10, j10, view, keyCache));
            return this.f1730h;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$k */
    /* loaded from: classes.dex */
    public static class k {
        static void a(int[] iArr, float[][] fArr, int i10, int i11) {
            int[] iArr2 = new int[iArr.length + 10];
            iArr2[0] = i11;
            iArr2[1] = i10;
            int i12 = 2;
            while (i12 > 0) {
                int i13 = i12 - 1;
                int i14 = iArr2[i13];
                i12 = i13 - 1;
                int i15 = iArr2[i12];
                if (i14 < i15) {
                    int b10 = b(iArr, fArr, i14, i15);
                    int i16 = i12 + 1;
                    iArr2[i12] = b10 - 1;
                    int i17 = i16 + 1;
                    iArr2[i16] = i14;
                    int i18 = i17 + 1;
                    iArr2[i17] = i15;
                    i12 = i18 + 1;
                    iArr2[i18] = b10 + 1;
                }
            }
        }

        private static int b(int[] iArr, float[][] fArr, int i10, int i11) {
            int i12 = iArr[i11];
            int i13 = i10;
            while (i10 < i11) {
                if (iArr[i10] <= i12) {
                    c(iArr, fArr, i13, i10);
                    i13++;
                }
                i10++;
            }
            c(iArr, fArr, i13, i11);
            return i13;
        }

        private static void c(int[] iArr, float[][] fArr, int i10, int i11) {
            int i12 = iArr[i10];
            iArr[i10] = iArr[i11];
            iArr[i11] = i12;
            float[] fArr2 = fArr[i10];
            fArr[i10] = fArr[i11];
            fArr[i11] = fArr2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$l */
    /* loaded from: classes.dex */
    public static class l extends TimeCycleSplineSet {
        l() {
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            view.setTranslationX(b(f10, j10, view, keyCache));
            return this.f1730h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$m */
    /* loaded from: classes.dex */
    public static class m extends TimeCycleSplineSet {
        m() {
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            view.setTranslationY(b(f10, j10, view, keyCache));
            return this.f1730h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TimeCycleSplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.s$n */
    /* loaded from: classes.dex */
    public static class n extends TimeCycleSplineSet {
        n() {
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public boolean f(View view, float f10, long j10, KeyCache keyCache) {
            view.setTranslationZ(b(f10, j10, view, keyCache));
            return this.f1730h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TimeCycleSplineSet c(String str, SparseArray<ConstraintAttribute> sparseArray) {
        return new b(str, sparseArray);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Failed to find 'out' block for switch in B:39:0x009c. Please report as an issue. */
    public static TimeCycleSplineSet d(String str, long j10) {
        TimeCycleSplineSet gVar;
        str.hashCode();
        char c10 = 65535;
        switch (str.hashCode()) {
            case -1249320806:
                if (str.equals("rotationX")) {
                    c10 = 0;
                    break;
                }
                break;
            case -1249320805:
                if (str.equals("rotationY")) {
                    c10 = 1;
                    break;
                }
                break;
            case -1225497657:
                if (str.equals("translationX")) {
                    c10 = 2;
                    break;
                }
                break;
            case -1225497656:
                if (str.equals("translationY")) {
                    c10 = 3;
                    break;
                }
                break;
            case -1225497655:
                if (str.equals("translationZ")) {
                    c10 = 4;
                    break;
                }
                break;
            case -1001078227:
                if (str.equals("progress")) {
                    c10 = 5;
                    break;
                }
                break;
            case -908189618:
                if (str.equals("scaleX")) {
                    c10 = 6;
                    break;
                }
                break;
            case -908189617:
                if (str.equals("scaleY")) {
                    c10 = 7;
                    break;
                }
                break;
            case -40300674:
                if (str.equals("rotation")) {
                    c10 = '\b';
                    break;
                }
                break;
            case -4379043:
                if (str.equals("elevation")) {
                    c10 = '\t';
                    break;
                }
                break;
            case 37232917:
                if (str.equals("transitionPathRotate")) {
                    c10 = '\n';
                    break;
                }
                break;
            case 92909918:
                if (str.equals("alpha")) {
                    c10 = 11;
                    break;
                }
                break;
        }
        switch (c10) {
            case 0:
                gVar = new g();
                gVar.g(j10);
                return gVar;
            case 1:
                gVar = new h();
                gVar.g(j10);
                return gVar;
            case 2:
                gVar = new l();
                gVar.g(j10);
                return gVar;
            case 3:
                gVar = new m();
                gVar.g(j10);
                return gVar;
            case 4:
                gVar = new n();
                gVar.g(j10);
                return gVar;
            case 5:
                gVar = new e();
                gVar.g(j10);
                return gVar;
            case 6:
                gVar = new i();
                gVar.g(j10);
                return gVar;
            case 7:
                gVar = new j();
                gVar.g(j10);
                return gVar;
            case '\b':
                gVar = new f();
                gVar.g(j10);
                return gVar;
            case '\t':
                gVar = new c();
                gVar.g(j10);
                return gVar;
            case '\n':
                gVar = new d();
                gVar.g(j10);
                return gVar;
            case 11:
                gVar = new a();
                gVar.g(j10);
                return gVar;
            default:
                return null;
        }
    }

    protected float a(float f10) {
        switch (this.f1724b) {
            case 1:
                return Math.signum(f10 * f1722k);
            case 2:
                return 1.0f - Math.abs(f10);
            case 3:
                return (((f10 * 2.0f) + 1.0f) % 2.0f) - 1.0f;
            case 4:
                return 1.0f - (((f10 * 2.0f) + 1.0f) % 2.0f);
            case 5:
                return (float) Math.cos(f10 * f1722k);
            case 6:
                float abs = 1.0f - Math.abs(((f10 * 4.0f) % 4.0f) - 2.0f);
                return 1.0f - (abs * abs);
            default:
                return (float) Math.sin(f10 * f1722k);
        }
    }

    public float b(float f10, long j10, View view, KeyCache keyCache) {
        this.f1723a.e(f10, this.f1729g);
        float[] fArr = this.f1729g;
        float f11 = fArr[1];
        if (f11 == 0.0f) {
            this.f1730h = false;
            return fArr[2];
        }
        if (Float.isNaN(this.f1732j)) {
            float a10 = keyCache.a(view, this.f1728f, 0);
            this.f1732j = a10;
            if (Float.isNaN(a10)) {
                this.f1732j = 0.0f;
            }
        }
        float f12 = (float) ((this.f1732j + (((j10 - this.f1731i) * 1.0E-9d) * f11)) % 1.0d);
        this.f1732j = f12;
        keyCache.b(view, this.f1728f, 0, f12);
        this.f1731i = j10;
        float f13 = this.f1729g[0];
        float a11 = (a(this.f1732j) * f13) + this.f1729g[2];
        this.f1730h = (f13 == 0.0f && f11 == 0.0f) ? false : true;
        return a11;
    }

    public void e(int i10, float f10, float f11, int i11, float f12) {
        int[] iArr = this.f1725c;
        int i12 = this.f1727e;
        iArr[i12] = i10;
        float[][] fArr = this.f1726d;
        fArr[i12][0] = f10;
        fArr[i12][1] = f11;
        fArr[i12][2] = f12;
        this.f1724b = Math.max(this.f1724b, i11);
        this.f1727e++;
    }

    public abstract boolean f(View view, float f10, long j10, KeyCache keyCache);

    protected void g(long j10) {
        this.f1731i = j10;
    }

    public void h(String str) {
        this.f1728f = str;
    }

    public void i(int i10) {
        int i11;
        int i12 = this.f1727e;
        if (i12 == 0) {
            Log.e("SplineSet", "Error no points added to " + this.f1728f);
            return;
        }
        k.a(this.f1725c, this.f1726d, 0, i12 - 1);
        int i13 = 1;
        int i14 = 0;
        while (true) {
            int[] iArr = this.f1725c;
            if (i13 >= iArr.length) {
                break;
            }
            if (iArr[i13] != iArr[i13 - 1]) {
                i14++;
            }
            i13++;
        }
        if (i14 == 0) {
            i14 = 1;
        }
        double[] dArr = new double[i14];
        double[][] dArr2 = (double[][]) Array.newInstance((Class<?>) double.class, i14, 3);
        int i15 = 0;
        for (0; i11 < this.f1727e; i11 + 1) {
            if (i11 > 0) {
                int[] iArr2 = this.f1725c;
                i11 = iArr2[i11] == iArr2[i11 - 1] ? i11 + 1 : 0;
            }
            dArr[i15] = this.f1725c[i11] * 0.01d;
            double[] dArr3 = dArr2[i15];
            float[][] fArr = this.f1726d;
            dArr3[0] = fArr[i11][0];
            dArr2[i15][1] = fArr[i11][1];
            dArr2[i15][2] = fArr[i11][2];
            i15++;
        }
        this.f1723a = CurveFit.a(i10, dArr, dArr2);
    }

    public String toString() {
        String str = this.f1728f;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        for (int i10 = 0; i10 < this.f1727e; i10++) {
            str = str + "[" + this.f1725c[i10] + " , " + decimalFormat.format(this.f1726d[i10]) + "] ";
        }
        return str;
    }
}
