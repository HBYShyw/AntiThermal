package androidx.constraintlayout.motion.widget;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
import k.CurveFit;

/* compiled from: SplineSet.java */
/* renamed from: androidx.constraintlayout.motion.widget.r, reason: use source file name */
/* loaded from: classes.dex */
public abstract class SplineSet {

    /* renamed from: a, reason: collision with root package name */
    protected CurveFit f1713a;

    /* renamed from: b, reason: collision with root package name */
    protected int[] f1714b = new int[10];

    /* renamed from: c, reason: collision with root package name */
    protected float[] f1715c = new float[10];

    /* renamed from: d, reason: collision with root package name */
    private int f1716d;

    /* renamed from: e, reason: collision with root package name */
    private String f1717e;

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$a */
    /* loaded from: classes.dex */
    static class a extends SplineSet {
        a() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            view.setAlpha(a(f10));
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$b */
    /* loaded from: classes.dex */
    static class b extends SplineSet {

        /* renamed from: f, reason: collision with root package name */
        String f1718f;

        /* renamed from: g, reason: collision with root package name */
        SparseArray<ConstraintAttribute> f1719g;

        /* renamed from: h, reason: collision with root package name */
        float[] f1720h;

        public b(String str, SparseArray<ConstraintAttribute> sparseArray) {
            this.f1718f = str.split(",")[1];
            this.f1719g = sparseArray;
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void e(int i10, float f10) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute)");
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            this.f1713a.e(f10, this.f1720h);
            this.f1719g.valueAt(0).k(view, this.f1720h);
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void h(int i10) {
            int size = this.f1719g.size();
            int f10 = this.f1719g.valueAt(0).f();
            double[] dArr = new double[size];
            this.f1720h = new float[f10];
            double[][] dArr2 = (double[][]) Array.newInstance((Class<?>) double.class, size, f10);
            for (int i11 = 0; i11 < size; i11++) {
                int keyAt = this.f1719g.keyAt(i11);
                ConstraintAttribute valueAt = this.f1719g.valueAt(i11);
                dArr[i11] = keyAt * 0.01d;
                valueAt.e(this.f1720h);
                int i12 = 0;
                while (true) {
                    if (i12 < this.f1720h.length) {
                        dArr2[i11][i12] = r6[i12];
                        i12++;
                    }
                }
            }
            this.f1713a = CurveFit.a(i10, dArr, dArr2);
        }

        public void i(int i10, ConstraintAttribute constraintAttribute) {
            this.f1719g.append(i10, constraintAttribute);
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$c */
    /* loaded from: classes.dex */
    static class c extends SplineSet {
        c() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            view.setElevation(a(f10));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$d */
    /* loaded from: classes.dex */
    public static class d extends SplineSet {
        d() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
        }

        public void i(View view, float f10, double d10, double d11) {
            view.setRotation(a(f10) + ((float) Math.toDegrees(Math.atan2(d11, d10))));
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$e */
    /* loaded from: classes.dex */
    static class e extends SplineSet {
        e() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            view.setPivotX(a(f10));
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$f */
    /* loaded from: classes.dex */
    static class f extends SplineSet {
        f() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            view.setPivotY(a(f10));
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$g */
    /* loaded from: classes.dex */
    static class g extends SplineSet {

        /* renamed from: f, reason: collision with root package name */
        boolean f1721f = false;

        g() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            if (view instanceof MotionLayout) {
                ((MotionLayout) view).setProgress(a(f10));
                return;
            }
            if (this.f1721f) {
                return;
            }
            Method method = null;
            try {
                method = view.getClass().getMethod("setProgress", Float.TYPE);
            } catch (NoSuchMethodException unused) {
                this.f1721f = true;
            }
            if (method != null) {
                try {
                    method.invoke(view, Float.valueOf(a(f10)));
                } catch (IllegalAccessException e10) {
                    Log.e("SplineSet", "unable to setProgress", e10);
                } catch (InvocationTargetException e11) {
                    Log.e("SplineSet", "unable to setProgress", e11);
                }
            }
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$h */
    /* loaded from: classes.dex */
    static class h extends SplineSet {
        h() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            view.setRotation(a(f10));
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$i */
    /* loaded from: classes.dex */
    static class i extends SplineSet {
        i() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            view.setRotationX(a(f10));
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$j */
    /* loaded from: classes.dex */
    static class j extends SplineSet {
        j() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            view.setRotationY(a(f10));
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$k */
    /* loaded from: classes.dex */
    static class k extends SplineSet {
        k() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            view.setScaleX(a(f10));
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$l */
    /* loaded from: classes.dex */
    static class l extends SplineSet {
        l() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            view.setScaleY(a(f10));
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$m */
    /* loaded from: classes.dex */
    private static class m {
        static void a(int[] iArr, float[] fArr, int i10, int i11) {
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

        private static int b(int[] iArr, float[] fArr, int i10, int i11) {
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

        private static void c(int[] iArr, float[] fArr, int i10, int i11) {
            int i12 = iArr[i10];
            iArr[i10] = iArr[i11];
            iArr[i11] = i12;
            float f10 = fArr[i10];
            fArr[i10] = fArr[i11];
            fArr[i11] = f10;
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$n */
    /* loaded from: classes.dex */
    static class n extends SplineSet {
        n() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            view.setTranslationX(a(f10));
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$o */
    /* loaded from: classes.dex */
    static class o extends SplineSet {
        o() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            view.setTranslationY(a(f10));
        }
    }

    /* compiled from: SplineSet.java */
    /* renamed from: androidx.constraintlayout.motion.widget.r$p */
    /* loaded from: classes.dex */
    static class p extends SplineSet {
        p() {
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public void f(View view, float f10) {
            view.setTranslationZ(a(f10));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SplineSet c(String str, SparseArray<ConstraintAttribute> sparseArray) {
        return new b(str, sparseArray);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SplineSet d(String str) {
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
            case -797520672:
                if (str.equals("waveVariesBy")) {
                    c10 = '\b';
                    break;
                }
                break;
            case -760884510:
                if (str.equals("transformPivotX")) {
                    c10 = '\t';
                    break;
                }
                break;
            case -760884509:
                if (str.equals("transformPivotY")) {
                    c10 = '\n';
                    break;
                }
                break;
            case -40300674:
                if (str.equals("rotation")) {
                    c10 = 11;
                    break;
                }
                break;
            case -4379043:
                if (str.equals("elevation")) {
                    c10 = '\f';
                    break;
                }
                break;
            case 37232917:
                if (str.equals("transitionPathRotate")) {
                    c10 = '\r';
                    break;
                }
                break;
            case 92909918:
                if (str.equals("alpha")) {
                    c10 = 14;
                    break;
                }
                break;
            case 156108012:
                if (str.equals("waveOffset")) {
                    c10 = 15;
                    break;
                }
                break;
        }
        switch (c10) {
            case 0:
                return new i();
            case 1:
                return new j();
            case 2:
                return new n();
            case 3:
                return new o();
            case 4:
                return new p();
            case 5:
                return new g();
            case 6:
                return new k();
            case 7:
                return new l();
            case '\b':
                return new a();
            case '\t':
                return new e();
            case '\n':
                return new f();
            case 11:
                return new h();
            case '\f':
                return new c();
            case '\r':
                return new d();
            case 14:
                return new a();
            case 15:
                return new a();
            default:
                return null;
        }
    }

    public float a(float f10) {
        return (float) this.f1713a.c(f10, 0);
    }

    public float b(float f10) {
        return (float) this.f1713a.f(f10, 0);
    }

    public void e(int i10, float f10) {
        int[] iArr = this.f1714b;
        if (iArr.length < this.f1716d + 1) {
            this.f1714b = Arrays.copyOf(iArr, iArr.length * 2);
            float[] fArr = this.f1715c;
            this.f1715c = Arrays.copyOf(fArr, fArr.length * 2);
        }
        int[] iArr2 = this.f1714b;
        int i11 = this.f1716d;
        iArr2[i11] = i10;
        this.f1715c[i11] = f10;
        this.f1716d = i11 + 1;
    }

    public abstract void f(View view, float f10);

    public void g(String str) {
        this.f1717e = str;
    }

    public void h(int i10) {
        int i11;
        int i12 = this.f1716d;
        if (i12 == 0) {
            return;
        }
        m.a(this.f1714b, this.f1715c, 0, i12 - 1);
        int i13 = 1;
        for (int i14 = 1; i14 < this.f1716d; i14++) {
            int[] iArr = this.f1714b;
            if (iArr[i14 - 1] != iArr[i14]) {
                i13++;
            }
        }
        double[] dArr = new double[i13];
        double[][] dArr2 = (double[][]) Array.newInstance((Class<?>) double.class, i13, 1);
        int i15 = 0;
        for (0; i11 < this.f1716d; i11 + 1) {
            if (i11 > 0) {
                int[] iArr2 = this.f1714b;
                i11 = iArr2[i11] == iArr2[i11 - 1] ? i11 + 1 : 0;
            }
            dArr[i15] = this.f1714b[i11] * 0.01d;
            dArr2[i15][0] = this.f1715c[i11];
            i15++;
        }
        this.f1713a = CurveFit.a(i10, dArr, dArr2);
    }

    public String toString() {
        String str = this.f1717e;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        for (int i10 = 0; i10 < this.f1716d; i10++) {
            str = str + "[" + this.f1714b[i10] + " , " + decimalFormat.format(this.f1715c[i10]) + "] ";
        }
        return str;
    }
}
