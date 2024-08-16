package androidx.constraintlayout.motion.widget;

import android.annotation.TargetApi;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintAttribute;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import k.CurveFit;
import k.Oscillator;

/* compiled from: KeyCycleOscillator.java */
/* renamed from: androidx.constraintlayout.motion.widget.g, reason: use source file name */
/* loaded from: classes.dex */
public abstract class KeyCycleOscillator {

    /* renamed from: a, reason: collision with root package name */
    private CurveFit f1525a;

    /* renamed from: b, reason: collision with root package name */
    private d f1526b;

    /* renamed from: c, reason: collision with root package name */
    protected ConstraintAttribute f1527c;

    /* renamed from: d, reason: collision with root package name */
    private String f1528d;

    /* renamed from: e, reason: collision with root package name */
    private int f1529e = 0;

    /* renamed from: f, reason: collision with root package name */
    public int f1530f = 0;

    /* renamed from: g, reason: collision with root package name */
    ArrayList<p> f1531g = new ArrayList<>();

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$a */
    /* loaded from: classes.dex */
    class a implements Comparator<p> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(p pVar, p pVar2) {
            return Integer.compare(pVar.f1548a, pVar2.f1548a);
        }
    }

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$b */
    /* loaded from: classes.dex */
    static class b extends KeyCycleOscillator {
        b() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
            view.setAlpha(a(f10));
        }
    }

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$c */
    /* loaded from: classes.dex */
    static class c extends KeyCycleOscillator {

        /* renamed from: h, reason: collision with root package name */
        float[] f1533h = new float[1];

        c() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
            this.f1533h[0] = a(f10);
            this.f1527c.k(view, this.f1533h);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$d */
    /* loaded from: classes.dex */
    public static class d {

        /* renamed from: a, reason: collision with root package name */
        private final int f1534a;

        /* renamed from: c, reason: collision with root package name */
        float[] f1536c;

        /* renamed from: d, reason: collision with root package name */
        double[] f1537d;

        /* renamed from: e, reason: collision with root package name */
        float[] f1538e;

        /* renamed from: f, reason: collision with root package name */
        float[] f1539f;

        /* renamed from: g, reason: collision with root package name */
        float[] f1540g;

        /* renamed from: h, reason: collision with root package name */
        int f1541h;

        /* renamed from: i, reason: collision with root package name */
        CurveFit f1542i;

        /* renamed from: j, reason: collision with root package name */
        double[] f1543j;

        /* renamed from: k, reason: collision with root package name */
        double[] f1544k;

        /* renamed from: l, reason: collision with root package name */
        float f1545l;

        /* renamed from: b, reason: collision with root package name */
        Oscillator f1535b = new Oscillator();

        /* renamed from: m, reason: collision with root package name */
        public HashMap<String, ConstraintAttribute> f1546m = new HashMap<>();

        d(int i10, int i11, int i12) {
            this.f1541h = i10;
            this.f1534a = i11;
            this.f1535b.g(i10);
            this.f1536c = new float[i12];
            this.f1537d = new double[i12];
            this.f1538e = new float[i12];
            this.f1539f = new float[i12];
            this.f1540g = new float[i12];
        }

        public double a(float f10) {
            CurveFit curveFit = this.f1542i;
            if (curveFit != null) {
                double d10 = f10;
                curveFit.g(d10, this.f1544k);
                this.f1542i.d(d10, this.f1543j);
            } else {
                double[] dArr = this.f1544k;
                dArr[0] = 0.0d;
                dArr[1] = 0.0d;
            }
            double d11 = f10;
            double e10 = this.f1535b.e(d11);
            double d12 = this.f1535b.d(d11);
            double[] dArr2 = this.f1544k;
            return dArr2[0] + (e10 * dArr2[1]) + (d12 * this.f1543j[1]);
        }

        public double b(float f10) {
            CurveFit curveFit = this.f1542i;
            if (curveFit != null) {
                curveFit.d(f10, this.f1543j);
            } else {
                double[] dArr = this.f1543j;
                dArr[0] = this.f1539f[0];
                dArr[1] = this.f1536c[0];
            }
            return this.f1543j[0] + (this.f1535b.e(f10) * this.f1543j[1]);
        }

        public void c(int i10, int i11, float f10, float f11, float f12) {
            this.f1537d[i10] = i11 / 100.0d;
            this.f1538e[i10] = f10;
            this.f1539f[i10] = f11;
            this.f1536c[i10] = f12;
        }

        public void d(float f10) {
            this.f1545l = f10;
            double[][] dArr = (double[][]) Array.newInstance((Class<?>) double.class, this.f1537d.length, 2);
            float[] fArr = this.f1536c;
            this.f1543j = new double[fArr.length + 1];
            this.f1544k = new double[fArr.length + 1];
            if (this.f1537d[0] > UserProfileInfo.Constant.NA_LAT_LON) {
                this.f1535b.a(UserProfileInfo.Constant.NA_LAT_LON, this.f1538e[0]);
            }
            double[] dArr2 = this.f1537d;
            int length = dArr2.length - 1;
            if (dArr2[length] < 1.0d) {
                this.f1535b.a(1.0d, this.f1538e[length]);
            }
            for (int i10 = 0; i10 < dArr.length; i10++) {
                dArr[i10][0] = this.f1539f[i10];
                int i11 = 0;
                while (true) {
                    if (i11 < this.f1536c.length) {
                        dArr[i11][1] = r4[i11];
                        i11++;
                    }
                }
                this.f1535b.a(this.f1537d[i10], this.f1538e[i10]);
            }
            this.f1535b.f();
            double[] dArr3 = this.f1537d;
            if (dArr3.length > 1) {
                this.f1542i = CurveFit.a(0, dArr3, dArr);
            } else {
                this.f1542i = null;
            }
        }
    }

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$e */
    /* loaded from: classes.dex */
    static class e extends KeyCycleOscillator {
        e() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
            view.setElevation(a(f10));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$f */
    /* loaded from: classes.dex */
    public static class f extends KeyCycleOscillator {
        f() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
        }

        public void j(View view, float f10, double d10, double d11) {
            view.setRotation(a(f10) + ((float) Math.toDegrees(Math.atan2(d11, d10))));
        }
    }

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$g */
    /* loaded from: classes.dex */
    static class g extends KeyCycleOscillator {

        /* renamed from: h, reason: collision with root package name */
        boolean f1547h = false;

        g() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
            if (view instanceof MotionLayout) {
                ((MotionLayout) view).setProgress(a(f10));
                return;
            }
            if (this.f1547h) {
                return;
            }
            Method method = null;
            try {
                method = view.getClass().getMethod("setProgress", Float.TYPE);
            } catch (NoSuchMethodException unused) {
                this.f1547h = true;
            }
            if (method != null) {
                try {
                    method.invoke(view, Float.valueOf(a(f10)));
                } catch (IllegalAccessException e10) {
                    Log.e("KeyCycleOscillator", "unable to setProgress", e10);
                } catch (InvocationTargetException e11) {
                    Log.e("KeyCycleOscillator", "unable to setProgress", e11);
                }
            }
        }
    }

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$h */
    /* loaded from: classes.dex */
    static class h extends KeyCycleOscillator {
        h() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
            view.setRotation(a(f10));
        }
    }

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$i */
    /* loaded from: classes.dex */
    static class i extends KeyCycleOscillator {
        i() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
            view.setRotationX(a(f10));
        }
    }

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$j */
    /* loaded from: classes.dex */
    static class j extends KeyCycleOscillator {
        j() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
            view.setRotationY(a(f10));
        }
    }

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$k */
    /* loaded from: classes.dex */
    static class k extends KeyCycleOscillator {
        k() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
            view.setScaleX(a(f10));
        }
    }

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$l */
    /* loaded from: classes.dex */
    static class l extends KeyCycleOscillator {
        l() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
            view.setScaleY(a(f10));
        }
    }

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$m */
    /* loaded from: classes.dex */
    static class m extends KeyCycleOscillator {
        m() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
            view.setTranslationX(a(f10));
        }
    }

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$n */
    /* loaded from: classes.dex */
    static class n extends KeyCycleOscillator {
        n() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
            view.setTranslationY(a(f10));
        }
    }

    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$o */
    /* loaded from: classes.dex */
    static class o extends KeyCycleOscillator {
        o() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void f(View view, float f10) {
            view.setTranslationZ(a(f10));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: KeyCycleOscillator.java */
    /* renamed from: androidx.constraintlayout.motion.widget.g$p */
    /* loaded from: classes.dex */
    public static class p {

        /* renamed from: a, reason: collision with root package name */
        int f1548a;

        /* renamed from: b, reason: collision with root package name */
        float f1549b;

        /* renamed from: c, reason: collision with root package name */
        float f1550c;

        /* renamed from: d, reason: collision with root package name */
        float f1551d;

        public p(int i10, float f10, float f11, float f12) {
            this.f1548a = i10;
            this.f1549b = f12;
            this.f1550c = f11;
            this.f1551d = f10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static KeyCycleOscillator c(String str) {
        if (str.startsWith("CUSTOM")) {
            return new c();
        }
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
            case -40300674:
                if (str.equals("rotation")) {
                    c10 = '\t';
                    break;
                }
                break;
            case -4379043:
                if (str.equals("elevation")) {
                    c10 = '\n';
                    break;
                }
                break;
            case 37232917:
                if (str.equals("transitionPathRotate")) {
                    c10 = 11;
                    break;
                }
                break;
            case 92909918:
                if (str.equals("alpha")) {
                    c10 = '\f';
                    break;
                }
                break;
            case 156108012:
                if (str.equals("waveOffset")) {
                    c10 = '\r';
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
                return new m();
            case 3:
                return new n();
            case 4:
                return new o();
            case 5:
                return new g();
            case 6:
                return new k();
            case 7:
                return new l();
            case '\b':
                return new b();
            case '\t':
                return new h();
            case '\n':
                return new e();
            case 11:
                return new f();
            case '\f':
                return new b();
            case '\r':
                return new b();
            default:
                return null;
        }
    }

    public float a(float f10) {
        return (float) this.f1526b.b(f10);
    }

    public float b(float f10) {
        return (float) this.f1526b.a(f10);
    }

    public void d(int i10, int i11, int i12, float f10, float f11, float f12) {
        this.f1531g.add(new p(i10, f10, f11, f12));
        if (i12 != -1) {
            this.f1530f = i12;
        }
        this.f1529e = i11;
    }

    public void e(int i10, int i11, int i12, float f10, float f11, float f12, ConstraintAttribute constraintAttribute) {
        this.f1531g.add(new p(i10, f10, f11, f12));
        if (i12 != -1) {
            this.f1530f = i12;
        }
        this.f1529e = i11;
        this.f1527c = constraintAttribute;
    }

    public abstract void f(View view, float f10);

    public void g(String str) {
        this.f1528d = str;
    }

    @TargetApi(19)
    public void h(float f10) {
        int size = this.f1531g.size();
        if (size == 0) {
            return;
        }
        Collections.sort(this.f1531g, new a());
        double[] dArr = new double[size];
        double[][] dArr2 = (double[][]) Array.newInstance((Class<?>) double.class, size, 2);
        this.f1526b = new d(this.f1529e, this.f1530f, size);
        Iterator<p> it = this.f1531g.iterator();
        int i10 = 0;
        while (it.hasNext()) {
            p next = it.next();
            float f11 = next.f1551d;
            dArr[i10] = f11 * 0.01d;
            double[] dArr3 = dArr2[i10];
            float f12 = next.f1549b;
            dArr3[0] = f12;
            double[] dArr4 = dArr2[i10];
            float f13 = next.f1550c;
            dArr4[1] = f13;
            this.f1526b.c(i10, next.f1548a, f11, f13, f12);
            i10++;
        }
        this.f1526b.d(f10);
        this.f1525a = CurveFit.a(0, dArr, dArr2);
    }

    public boolean i() {
        return this.f1530f == 1;
    }

    public String toString() {
        String str = this.f1528d;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        Iterator<p> it = this.f1531g.iterator();
        while (it.hasNext()) {
            str = str + "[" + it.next().f1548a + " , " + decimalFormat.format(r2.f1549b) + "] ";
        }
        return str;
    }
}
