package androidx.constraintlayout.motion.widget;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.KeyCycleOscillator;
import androidx.constraintlayout.motion.widget.SplineSet;
import androidx.constraintlayout.motion.widget.TimeCycleSplineSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import k.CurveFit;
import k.Easing;
import k.VelocityMatrix;
import m.ConstraintWidget;

/* compiled from: MotionController.java */
/* renamed from: androidx.constraintlayout.motion.widget.n, reason: use source file name */
/* loaded from: classes.dex */
public class MotionController {
    private KeyTrigger[] A;

    /* renamed from: a, reason: collision with root package name */
    View f1629a;

    /* renamed from: b, reason: collision with root package name */
    int f1630b;

    /* renamed from: c, reason: collision with root package name */
    String f1631c;

    /* renamed from: i, reason: collision with root package name */
    private CurveFit[] f1637i;

    /* renamed from: j, reason: collision with root package name */
    private CurveFit f1638j;

    /* renamed from: n, reason: collision with root package name */
    private int[] f1642n;

    /* renamed from: o, reason: collision with root package name */
    private double[] f1643o;

    /* renamed from: p, reason: collision with root package name */
    private double[] f1644p;

    /* renamed from: q, reason: collision with root package name */
    private String[] f1645q;

    /* renamed from: r, reason: collision with root package name */
    private int[] f1646r;

    /* renamed from: x, reason: collision with root package name */
    private HashMap<String, TimeCycleSplineSet> f1652x;

    /* renamed from: y, reason: collision with root package name */
    private HashMap<String, SplineSet> f1653y;

    /* renamed from: z, reason: collision with root package name */
    private HashMap<String, KeyCycleOscillator> f1654z;

    /* renamed from: d, reason: collision with root package name */
    private int f1632d = -1;

    /* renamed from: e, reason: collision with root package name */
    private MotionPaths f1633e = new MotionPaths();

    /* renamed from: f, reason: collision with root package name */
    private MotionPaths f1634f = new MotionPaths();

    /* renamed from: g, reason: collision with root package name */
    private MotionConstrainedPoint f1635g = new MotionConstrainedPoint();

    /* renamed from: h, reason: collision with root package name */
    private MotionConstrainedPoint f1636h = new MotionConstrainedPoint();

    /* renamed from: k, reason: collision with root package name */
    float f1639k = Float.NaN;

    /* renamed from: l, reason: collision with root package name */
    float f1640l = 0.0f;

    /* renamed from: m, reason: collision with root package name */
    float f1641m = 1.0f;

    /* renamed from: s, reason: collision with root package name */
    private int f1647s = 4;

    /* renamed from: t, reason: collision with root package name */
    private float[] f1648t = new float[4];

    /* renamed from: u, reason: collision with root package name */
    private ArrayList<MotionPaths> f1649u = new ArrayList<>();

    /* renamed from: v, reason: collision with root package name */
    private float[] f1650v = new float[1];

    /* renamed from: w, reason: collision with root package name */
    private ArrayList<c> f1651w = new ArrayList<>();
    private int B = c.f1481f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MotionController(View view) {
        u(view);
    }

    private float f(float f10, float[] fArr) {
        float f11 = 0.0f;
        if (fArr != null) {
            fArr[0] = 1.0f;
        } else {
            float f12 = this.f1641m;
            if (f12 != 1.0d) {
                float f13 = this.f1640l;
                if (f10 < f13) {
                    f10 = 0.0f;
                }
                if (f10 > f13 && f10 < 1.0d) {
                    f10 = (f10 - f13) * f12;
                }
            }
        }
        Easing easing = this.f1633e.f1656e;
        float f14 = Float.NaN;
        Iterator<MotionPaths> it = this.f1649u.iterator();
        while (it.hasNext()) {
            MotionPaths next = it.next();
            Easing easing2 = next.f1656e;
            if (easing2 != null) {
                float f15 = next.f1658g;
                if (f15 < f10) {
                    easing = easing2;
                    f11 = f15;
                } else if (Float.isNaN(f14)) {
                    f14 = next.f1658g;
                }
            }
        }
        if (easing == null) {
            return f10;
        }
        float f16 = (Float.isNaN(f14) ? 1.0f : f14) - f11;
        double d10 = (f10 - f11) / f16;
        float a10 = f11 + (((float) easing.a(d10)) * f16);
        if (fArr != null) {
            fArr[0] = (float) easing.b(d10);
        }
        return a10;
    }

    private float m() {
        float[] fArr = new float[2];
        float f10 = 1.0f / 99;
        double d10 = UserProfileInfo.Constant.NA_LAT_LON;
        double d11 = 0.0d;
        int i10 = 0;
        float f11 = 0.0f;
        while (i10 < 100) {
            float f12 = i10 * f10;
            double d12 = f12;
            Easing easing = this.f1633e.f1656e;
            float f13 = Float.NaN;
            Iterator<MotionPaths> it = this.f1649u.iterator();
            float f14 = 0.0f;
            while (it.hasNext()) {
                MotionPaths next = it.next();
                Easing easing2 = next.f1656e;
                float f15 = f10;
                if (easing2 != null) {
                    float f16 = next.f1658g;
                    if (f16 < f12) {
                        f14 = f16;
                        easing = easing2;
                    } else if (Float.isNaN(f13)) {
                        f13 = next.f1658g;
                    }
                }
                f10 = f15;
            }
            float f17 = f10;
            if (easing != null) {
                if (Float.isNaN(f13)) {
                    f13 = 1.0f;
                }
                d12 = (((float) easing.a((f12 - f14) / r16)) * (f13 - f14)) + f14;
            }
            this.f1637i[0].d(d12, this.f1643o);
            this.f1633e.f(this.f1642n, this.f1643o, fArr, 0);
            if (i10 > 0) {
                f11 = (float) (f11 + Math.hypot(d11 - fArr[1], d10 - fArr[0]));
            }
            d10 = fArr[0];
            d11 = fArr[1];
            i10++;
            f10 = f17;
        }
        return f11;
    }

    private void n(MotionPaths motionPaths) {
        if (Collections.binarySearch(this.f1649u, motionPaths) == 0) {
            Log.e("MotionController", " KeyPath positon \"" + motionPaths.f1659h + "\" outside of range");
        }
        this.f1649u.add((-r0) - 1, motionPaths);
    }

    private void p(MotionPaths motionPaths) {
        motionPaths.o((int) this.f1629a.getX(), (int) this.f1629a.getY(), this.f1629a.getWidth(), this.f1629a.getHeight());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(c cVar) {
        this.f1651w.add(cVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(ArrayList<c> arrayList) {
        this.f1651w.addAll(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int c(float[] fArr, int[] iArr) {
        if (fArr == null) {
            return 0;
        }
        double[] h10 = this.f1637i[0].h();
        if (iArr != null) {
            Iterator<MotionPaths> it = this.f1649u.iterator();
            int i10 = 0;
            while (it.hasNext()) {
                iArr[i10] = it.next().f1668q;
                i10++;
            }
        }
        int i11 = 0;
        for (double d10 : h10) {
            this.f1637i[0].d(d10, this.f1643o);
            this.f1633e.f(this.f1642n, this.f1643o, fArr, i11);
            i11 += 2;
        }
        return i11 / 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(float[] fArr, int i10) {
        int i11 = i10;
        float f10 = 1.0f;
        float f11 = 1.0f / (i11 - 1);
        HashMap<String, SplineSet> hashMap = this.f1653y;
        SplineSet splineSet = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, SplineSet> hashMap2 = this.f1653y;
        SplineSet splineSet2 = hashMap2 == null ? null : hashMap2.get("translationY");
        HashMap<String, KeyCycleOscillator> hashMap3 = this.f1654z;
        KeyCycleOscillator keyCycleOscillator = hashMap3 == null ? null : hashMap3.get("translationX");
        HashMap<String, KeyCycleOscillator> hashMap4 = this.f1654z;
        KeyCycleOscillator keyCycleOscillator2 = hashMap4 != null ? hashMap4.get("translationY") : null;
        int i12 = 0;
        while (i12 < i11) {
            float f12 = i12 * f11;
            float f13 = this.f1641m;
            if (f13 != f10) {
                float f14 = this.f1640l;
                if (f12 < f14) {
                    f12 = 0.0f;
                }
                if (f12 > f14 && f12 < 1.0d) {
                    f12 = (f12 - f14) * f13;
                }
            }
            double d10 = f12;
            Easing easing = this.f1633e.f1656e;
            float f15 = Float.NaN;
            Iterator<MotionPaths> it = this.f1649u.iterator();
            float f16 = 0.0f;
            while (it.hasNext()) {
                MotionPaths next = it.next();
                Easing easing2 = next.f1656e;
                if (easing2 != null) {
                    float f17 = next.f1658g;
                    if (f17 < f12) {
                        f16 = f17;
                        easing = easing2;
                    } else if (Float.isNaN(f15)) {
                        f15 = next.f1658g;
                    }
                }
            }
            if (easing != null) {
                if (Float.isNaN(f15)) {
                    f15 = 1.0f;
                }
                d10 = (((float) easing.a((f12 - f16) / r15)) * (f15 - f16)) + f16;
            }
            this.f1637i[0].d(d10, this.f1643o);
            CurveFit curveFit = this.f1638j;
            if (curveFit != null) {
                double[] dArr = this.f1643o;
                if (dArr.length > 0) {
                    curveFit.d(d10, dArr);
                }
            }
            int i13 = i12 * 2;
            this.f1633e.f(this.f1642n, this.f1643o, fArr, i13);
            if (keyCycleOscillator != null) {
                fArr[i13] = fArr[i13] + keyCycleOscillator.a(f12);
            } else if (splineSet != null) {
                fArr[i13] = fArr[i13] + splineSet.a(f12);
            }
            if (keyCycleOscillator2 != null) {
                int i14 = i13 + 1;
                fArr[i14] = fArr[i14] + keyCycleOscillator2.a(f12);
            } else if (splineSet2 != null) {
                int i15 = i13 + 1;
                fArr[i15] = fArr[i15] + splineSet2.a(f12);
            }
            i12++;
            i11 = i10;
            f10 = 1.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(float f10, float[] fArr, int i10) {
        this.f1637i[0].d(f(f10, null), this.f1643o);
        this.f1633e.i(this.f1642n, this.f1643o, fArr, i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(float f10, float f11, float f12, float[] fArr) {
        double[] dArr;
        float f13 = f(f10, this.f1650v);
        CurveFit[] curveFitArr = this.f1637i;
        int i10 = 0;
        if (curveFitArr != null) {
            double d10 = f13;
            curveFitArr[0].g(d10, this.f1644p);
            this.f1637i[0].d(d10, this.f1643o);
            float f14 = this.f1650v[0];
            while (true) {
                dArr = this.f1644p;
                if (i10 >= dArr.length) {
                    break;
                }
                dArr[i10] = dArr[i10] * f14;
                i10++;
            }
            CurveFit curveFit = this.f1638j;
            if (curveFit != null) {
                double[] dArr2 = this.f1643o;
                if (dArr2.length > 0) {
                    curveFit.d(d10, dArr2);
                    this.f1638j.g(d10, this.f1644p);
                    this.f1633e.p(f11, f12, fArr, this.f1642n, this.f1644p, this.f1643o);
                    return;
                }
                return;
            }
            this.f1633e.p(f11, f12, fArr, this.f1642n, dArr, this.f1643o);
            return;
        }
        MotionPaths motionPaths = this.f1634f;
        float f15 = motionPaths.f1660i;
        MotionPaths motionPaths2 = this.f1633e;
        float f16 = f15 - motionPaths2.f1660i;
        float f17 = motionPaths.f1661j - motionPaths2.f1661j;
        float f18 = (motionPaths.f1662k - motionPaths2.f1662k) + f16;
        float f19 = (motionPaths.f1663l - motionPaths2.f1663l) + f17;
        fArr[0] = (f16 * (1.0f - f11)) + (f18 * f11);
        fArr[1] = (f17 * (1.0f - f12)) + (f19 * f12);
    }

    public int h() {
        int i10 = this.f1633e.f1657f;
        Iterator<MotionPaths> it = this.f1649u.iterator();
        while (it.hasNext()) {
            i10 = Math.max(i10, it.next().f1657f);
        }
        return Math.max(i10, this.f1634f.f1657f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float i() {
        return this.f1634f.f1660i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float j() {
        return this.f1634f.f1661j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MotionPaths k(int i10) {
        return this.f1649u.get(i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(float f10, int i10, int i11, float f11, float f12, float[] fArr) {
        float f13 = f(f10, this.f1650v);
        HashMap<String, SplineSet> hashMap = this.f1653y;
        SplineSet splineSet = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, SplineSet> hashMap2 = this.f1653y;
        SplineSet splineSet2 = hashMap2 == null ? null : hashMap2.get("translationY");
        HashMap<String, SplineSet> hashMap3 = this.f1653y;
        SplineSet splineSet3 = hashMap3 == null ? null : hashMap3.get("rotation");
        HashMap<String, SplineSet> hashMap4 = this.f1653y;
        SplineSet splineSet4 = hashMap4 == null ? null : hashMap4.get("scaleX");
        HashMap<String, SplineSet> hashMap5 = this.f1653y;
        SplineSet splineSet5 = hashMap5 == null ? null : hashMap5.get("scaleY");
        HashMap<String, KeyCycleOscillator> hashMap6 = this.f1654z;
        KeyCycleOscillator keyCycleOscillator = hashMap6 == null ? null : hashMap6.get("translationX");
        HashMap<String, KeyCycleOscillator> hashMap7 = this.f1654z;
        KeyCycleOscillator keyCycleOscillator2 = hashMap7 == null ? null : hashMap7.get("translationY");
        HashMap<String, KeyCycleOscillator> hashMap8 = this.f1654z;
        KeyCycleOscillator keyCycleOscillator3 = hashMap8 == null ? null : hashMap8.get("rotation");
        HashMap<String, KeyCycleOscillator> hashMap9 = this.f1654z;
        KeyCycleOscillator keyCycleOscillator4 = hashMap9 == null ? null : hashMap9.get("scaleX");
        HashMap<String, KeyCycleOscillator> hashMap10 = this.f1654z;
        KeyCycleOscillator keyCycleOscillator5 = hashMap10 != null ? hashMap10.get("scaleY") : null;
        VelocityMatrix velocityMatrix = new VelocityMatrix();
        velocityMatrix.b();
        velocityMatrix.d(splineSet3, f13);
        velocityMatrix.h(splineSet, splineSet2, f13);
        velocityMatrix.f(splineSet4, splineSet5, f13);
        velocityMatrix.c(keyCycleOscillator3, f13);
        velocityMatrix.g(keyCycleOscillator, keyCycleOscillator2, f13);
        velocityMatrix.e(keyCycleOscillator4, keyCycleOscillator5, f13);
        CurveFit curveFit = this.f1638j;
        if (curveFit != null) {
            double[] dArr = this.f1643o;
            if (dArr.length > 0) {
                double d10 = f13;
                curveFit.d(d10, dArr);
                this.f1638j.g(d10, this.f1644p);
                this.f1633e.p(f11, f12, fArr, this.f1642n, this.f1644p, this.f1643o);
            }
            velocityMatrix.a(f11, f12, i10, i11, fArr);
            return;
        }
        int i12 = 0;
        if (this.f1637i != null) {
            double f14 = f(f13, this.f1650v);
            this.f1637i[0].g(f14, this.f1644p);
            this.f1637i[0].d(f14, this.f1643o);
            float f15 = this.f1650v[0];
            while (true) {
                double[] dArr2 = this.f1644p;
                if (i12 < dArr2.length) {
                    dArr2[i12] = dArr2[i12] * f15;
                    i12++;
                } else {
                    this.f1633e.p(f11, f12, fArr, this.f1642n, dArr2, this.f1643o);
                    velocityMatrix.a(f11, f12, i10, i11, fArr);
                    return;
                }
            }
        } else {
            MotionPaths motionPaths = this.f1634f;
            float f16 = motionPaths.f1660i;
            MotionPaths motionPaths2 = this.f1633e;
            float f17 = f16 - motionPaths2.f1660i;
            KeyCycleOscillator keyCycleOscillator6 = keyCycleOscillator5;
            float f18 = motionPaths.f1661j - motionPaths2.f1661j;
            KeyCycleOscillator keyCycleOscillator7 = keyCycleOscillator4;
            float f19 = (motionPaths.f1662k - motionPaths2.f1662k) + f17;
            float f20 = (motionPaths.f1663l - motionPaths2.f1663l) + f18;
            fArr[0] = (f17 * (1.0f - f11)) + (f19 * f11);
            fArr[1] = (f18 * (1.0f - f12)) + (f20 * f12);
            velocityMatrix.b();
            velocityMatrix.d(splineSet3, f13);
            velocityMatrix.h(splineSet, splineSet2, f13);
            velocityMatrix.f(splineSet4, splineSet5, f13);
            velocityMatrix.c(keyCycleOscillator3, f13);
            velocityMatrix.g(keyCycleOscillator, keyCycleOscillator2, f13);
            velocityMatrix.e(keyCycleOscillator7, keyCycleOscillator6, f13);
            velocityMatrix.a(f11, f12, i10, i11, fArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean o(View view, float f10, long j10, KeyCache keyCache) {
        TimeCycleSplineSet.d dVar;
        boolean z10;
        double d10;
        float f11 = f(f10, null);
        HashMap<String, SplineSet> hashMap = this.f1653y;
        if (hashMap != null) {
            Iterator<SplineSet> it = hashMap.values().iterator();
            while (it.hasNext()) {
                it.next().f(view, f11);
            }
        }
        HashMap<String, TimeCycleSplineSet> hashMap2 = this.f1652x;
        if (hashMap2 != null) {
            dVar = null;
            boolean z11 = false;
            for (TimeCycleSplineSet timeCycleSplineSet : hashMap2.values()) {
                if (timeCycleSplineSet instanceof TimeCycleSplineSet.d) {
                    dVar = (TimeCycleSplineSet.d) timeCycleSplineSet;
                } else {
                    z11 |= timeCycleSplineSet.f(view, f11, j10, keyCache);
                }
            }
            z10 = z11;
        } else {
            dVar = null;
            z10 = false;
        }
        CurveFit[] curveFitArr = this.f1637i;
        if (curveFitArr != null) {
            double d11 = f11;
            curveFitArr[0].d(d11, this.f1643o);
            this.f1637i[0].g(d11, this.f1644p);
            CurveFit curveFit = this.f1638j;
            if (curveFit != null) {
                double[] dArr = this.f1643o;
                if (dArr.length > 0) {
                    curveFit.d(d11, dArr);
                    this.f1638j.g(d11, this.f1644p);
                }
            }
            this.f1633e.q(view, this.f1642n, this.f1643o, this.f1644p, null);
            HashMap<String, SplineSet> hashMap3 = this.f1653y;
            if (hashMap3 != null) {
                for (SplineSet splineSet : hashMap3.values()) {
                    if (splineSet instanceof SplineSet.d) {
                        double[] dArr2 = this.f1644p;
                        ((SplineSet.d) splineSet).i(view, f11, dArr2[0], dArr2[1]);
                    }
                }
            }
            if (dVar != null) {
                double[] dArr3 = this.f1644p;
                d10 = d11;
                z10 = dVar.j(view, keyCache, f11, j10, dArr3[0], dArr3[1]) | z10;
            } else {
                d10 = d11;
            }
            int i10 = 1;
            while (true) {
                CurveFit[] curveFitArr2 = this.f1637i;
                if (i10 >= curveFitArr2.length) {
                    break;
                }
                curveFitArr2[i10].e(d10, this.f1648t);
                this.f1633e.f1667p.get(this.f1645q[i10 - 1]).k(view, this.f1648t);
                i10++;
            }
            MotionConstrainedPoint motionConstrainedPoint = this.f1635g;
            if (motionConstrainedPoint.f1608f == 0) {
                if (f11 <= 0.0f) {
                    view.setVisibility(motionConstrainedPoint.f1609g);
                } else if (f11 >= 1.0f) {
                    view.setVisibility(this.f1636h.f1609g);
                } else if (this.f1636h.f1609g != motionConstrainedPoint.f1609g) {
                    view.setVisibility(0);
                }
            }
            if (this.A != null) {
                int i11 = 0;
                while (true) {
                    KeyTrigger[] keyTriggerArr = this.A;
                    if (i11 >= keyTriggerArr.length) {
                        break;
                    }
                    keyTriggerArr[i11].r(f11, view);
                    i11++;
                }
            }
        } else {
            MotionPaths motionPaths = this.f1633e;
            float f12 = motionPaths.f1660i;
            MotionPaths motionPaths2 = this.f1634f;
            float f13 = f12 + ((motionPaths2.f1660i - f12) * f11);
            float f14 = motionPaths.f1661j;
            float f15 = f14 + ((motionPaths2.f1661j - f14) * f11);
            float f16 = motionPaths.f1662k;
            float f17 = motionPaths2.f1662k;
            float f18 = motionPaths.f1663l;
            float f19 = motionPaths2.f1663l;
            float f20 = f13 + 0.5f;
            int i12 = (int) f20;
            float f21 = f15 + 0.5f;
            int i13 = (int) f21;
            int i14 = (int) (f20 + ((f17 - f16) * f11) + f16);
            int i15 = (int) (f21 + ((f19 - f18) * f11) + f18);
            int i16 = i14 - i12;
            int i17 = i15 - i13;
            if (f17 != f16 || f19 != f18) {
                view.measure(View.MeasureSpec.makeMeasureSpec(i16, 1073741824), View.MeasureSpec.makeMeasureSpec(i17, 1073741824));
            }
            view.layout(i12, i13, i14, i15);
        }
        HashMap<String, KeyCycleOscillator> hashMap4 = this.f1654z;
        if (hashMap4 != null) {
            for (KeyCycleOscillator keyCycleOscillator : hashMap4.values()) {
                if (keyCycleOscillator instanceof KeyCycleOscillator.f) {
                    double[] dArr4 = this.f1644p;
                    ((KeyCycleOscillator.f) keyCycleOscillator).j(view, f11, dArr4[0], dArr4[1]);
                } else {
                    keyCycleOscillator.f(view, f11);
                }
            }
        }
        return z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(ConstraintWidget constraintWidget, ConstraintSet constraintSet) {
        MotionPaths motionPaths = this.f1634f;
        motionPaths.f1658g = 1.0f;
        motionPaths.f1659h = 1.0f;
        p(motionPaths);
        this.f1634f.o(constraintWidget.R(), constraintWidget.S(), constraintWidget.Q(), constraintWidget.w());
        this.f1634f.a(constraintSet.t(this.f1630b));
        this.f1636h.i(constraintWidget, constraintSet, this.f1630b);
    }

    public void r(int i10) {
        this.B = i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(View view) {
        MotionPaths motionPaths = this.f1633e;
        motionPaths.f1658g = 0.0f;
        motionPaths.f1659h = 0.0f;
        motionPaths.o(view.getX(), view.getY(), view.getWidth(), view.getHeight());
        this.f1635g.h(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(ConstraintWidget constraintWidget, ConstraintSet constraintSet) {
        MotionPaths motionPaths = this.f1633e;
        motionPaths.f1658g = 0.0f;
        motionPaths.f1659h = 0.0f;
        p(motionPaths);
        this.f1633e.o(constraintWidget.R(), constraintWidget.S(), constraintWidget.Q(), constraintWidget.w());
        ConstraintSet.a t7 = constraintSet.t(this.f1630b);
        this.f1633e.a(t7);
        this.f1639k = t7.f1954c.f2001f;
        this.f1635g.i(constraintWidget, constraintSet, this.f1630b);
    }

    public String toString() {
        return " start: x: " + this.f1633e.f1660i + " y: " + this.f1633e.f1661j + " end: x: " + this.f1634f.f1660i + " y: " + this.f1634f.f1661j;
    }

    public void u(View view) {
        this.f1629a = view;
        this.f1630b = view.getId();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            this.f1631c = ((ConstraintLayout.LayoutParams) layoutParams).a();
        }
    }

    public void v(int i10, int i11, float f10, long j10) {
        ArrayList arrayList;
        String[] strArr;
        TimeCycleSplineSet d10;
        ConstraintAttribute constraintAttribute;
        SplineSet d11;
        ConstraintAttribute constraintAttribute2;
        new HashSet();
        HashSet<String> hashSet = new HashSet<>();
        HashSet<String> hashSet2 = new HashSet<>();
        HashSet<String> hashSet3 = new HashSet<>();
        HashMap<String, Integer> hashMap = new HashMap<>();
        int i12 = this.B;
        if (i12 != c.f1481f) {
            this.f1633e.f1666o = i12;
        }
        this.f1635g.f(this.f1636h, hashSet2);
        ArrayList<c> arrayList2 = this.f1651w;
        if (arrayList2 != null) {
            Iterator<c> it = arrayList2.iterator();
            arrayList = null;
            while (it.hasNext()) {
                c next = it.next();
                if (next instanceof KeyPosition) {
                    KeyPosition keyPosition = (KeyPosition) next;
                    n(new MotionPaths(i10, i11, keyPosition, this.f1633e, this.f1634f));
                    int i13 = keyPosition.f1567g;
                    if (i13 != c.f1481f) {
                        this.f1632d = i13;
                    }
                } else if (next instanceof KeyCycle) {
                    next.b(hashSet3);
                } else if (next instanceof KeyTimeCycle) {
                    next.b(hashSet);
                } else if (next instanceof KeyTrigger) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add((KeyTrigger) next);
                } else {
                    next.e(hashMap);
                    next.b(hashSet2);
                }
            }
        } else {
            arrayList = null;
        }
        int i14 = 0;
        if (arrayList != null) {
            this.A = (KeyTrigger[]) arrayList.toArray(new KeyTrigger[0]);
        }
        char c10 = 1;
        if (!hashSet2.isEmpty()) {
            this.f1653y = new HashMap<>();
            Iterator<String> it2 = hashSet2.iterator();
            while (it2.hasNext()) {
                String next2 = it2.next();
                if (next2.startsWith("CUSTOM,")) {
                    SparseArray sparseArray = new SparseArray();
                    String str = next2.split(",")[c10];
                    Iterator<c> it3 = this.f1651w.iterator();
                    while (it3.hasNext()) {
                        c next3 = it3.next();
                        HashMap<String, ConstraintAttribute> hashMap2 = next3.f1486e;
                        if (hashMap2 != null && (constraintAttribute2 = hashMap2.get(str)) != null) {
                            sparseArray.append(next3.f1482a, constraintAttribute2);
                        }
                    }
                    d11 = SplineSet.c(next2, sparseArray);
                } else {
                    d11 = SplineSet.d(next2);
                }
                if (d11 != null) {
                    d11.g(next2);
                    this.f1653y.put(next2, d11);
                }
                c10 = 1;
            }
            ArrayList<c> arrayList3 = this.f1651w;
            if (arrayList3 != null) {
                Iterator<c> it4 = arrayList3.iterator();
                while (it4.hasNext()) {
                    c next4 = it4.next();
                    if (next4 instanceof KeyAttributes) {
                        next4.a(this.f1653y);
                    }
                }
            }
            this.f1635g.a(this.f1653y, 0);
            this.f1636h.a(this.f1653y, 100);
            for (String str2 : this.f1653y.keySet()) {
                this.f1653y.get(str2).h(hashMap.containsKey(str2) ? hashMap.get(str2).intValue() : 0);
            }
        }
        if (!hashSet.isEmpty()) {
            if (this.f1652x == null) {
                this.f1652x = new HashMap<>();
            }
            Iterator<String> it5 = hashSet.iterator();
            while (it5.hasNext()) {
                String next5 = it5.next();
                if (!this.f1652x.containsKey(next5)) {
                    if (next5.startsWith("CUSTOM,")) {
                        SparseArray sparseArray2 = new SparseArray();
                        String str3 = next5.split(",")[1];
                        Iterator<c> it6 = this.f1651w.iterator();
                        while (it6.hasNext()) {
                            c next6 = it6.next();
                            HashMap<String, ConstraintAttribute> hashMap3 = next6.f1486e;
                            if (hashMap3 != null && (constraintAttribute = hashMap3.get(str3)) != null) {
                                sparseArray2.append(next6.f1482a, constraintAttribute);
                            }
                        }
                        d10 = TimeCycleSplineSet.c(next5, sparseArray2);
                    } else {
                        d10 = TimeCycleSplineSet.d(next5, j10);
                    }
                    if (d10 != null) {
                        d10.h(next5);
                        this.f1652x.put(next5, d10);
                    }
                }
            }
            ArrayList<c> arrayList4 = this.f1651w;
            if (arrayList4 != null) {
                Iterator<c> it7 = arrayList4.iterator();
                while (it7.hasNext()) {
                    c next7 = it7.next();
                    if (next7 instanceof KeyTimeCycle) {
                        ((KeyTimeCycle) next7).M(this.f1652x);
                    }
                }
            }
            for (String str4 : this.f1652x.keySet()) {
                this.f1652x.get(str4).i(hashMap.containsKey(str4) ? hashMap.get(str4).intValue() : 0);
            }
        }
        int i15 = 2;
        int size = this.f1649u.size() + 2;
        MotionPaths[] motionPathsArr = new MotionPaths[size];
        motionPathsArr[0] = this.f1633e;
        motionPathsArr[size - 1] = this.f1634f;
        if (this.f1649u.size() > 0 && this.f1632d == -1) {
            this.f1632d = 0;
        }
        Iterator<MotionPaths> it8 = this.f1649u.iterator();
        int i16 = 1;
        while (it8.hasNext()) {
            motionPathsArr[i16] = it8.next();
            i16++;
        }
        HashSet hashSet4 = new HashSet();
        for (String str5 : this.f1634f.f1667p.keySet()) {
            if (this.f1633e.f1667p.containsKey(str5)) {
                if (!hashSet2.contains("CUSTOM," + str5)) {
                    hashSet4.add(str5);
                }
            }
        }
        String[] strArr2 = (String[]) hashSet4.toArray(new String[0]);
        this.f1645q = strArr2;
        this.f1646r = new int[strArr2.length];
        int i17 = 0;
        while (true) {
            strArr = this.f1645q;
            if (i17 >= strArr.length) {
                break;
            }
            String str6 = strArr[i17];
            this.f1646r[i17] = 0;
            int i18 = 0;
            while (true) {
                if (i18 >= size) {
                    break;
                }
                if (motionPathsArr[i18].f1667p.containsKey(str6)) {
                    int[] iArr = this.f1646r;
                    iArr[i17] = iArr[i17] + motionPathsArr[i18].f1667p.get(str6).f();
                    break;
                }
                i18++;
            }
            i17++;
        }
        boolean z10 = motionPathsArr[0].f1666o != c.f1481f;
        int length = 18 + strArr.length;
        boolean[] zArr = new boolean[length];
        for (int i19 = 1; i19 < size; i19++) {
            motionPathsArr[i19].d(motionPathsArr[i19 - 1], zArr, this.f1645q, z10);
        }
        int i20 = 0;
        for (int i21 = 1; i21 < length; i21++) {
            if (zArr[i21]) {
                i20++;
            }
        }
        int[] iArr2 = new int[i20];
        this.f1642n = iArr2;
        this.f1643o = new double[iArr2.length];
        this.f1644p = new double[iArr2.length];
        int i22 = 0;
        for (int i23 = 1; i23 < length; i23++) {
            if (zArr[i23]) {
                this.f1642n[i22] = i23;
                i22++;
            }
        }
        double[][] dArr = (double[][]) Array.newInstance((Class<?>) double.class, size, this.f1642n.length);
        double[] dArr2 = new double[size];
        for (int i24 = 0; i24 < size; i24++) {
            motionPathsArr[i24].e(dArr[i24], this.f1642n);
            dArr2[i24] = motionPathsArr[i24].f1658g;
        }
        int i25 = 0;
        while (true) {
            int[] iArr3 = this.f1642n;
            if (i25 >= iArr3.length) {
                break;
            }
            if (iArr3[i25] < MotionPaths.f1655t.length) {
                String str7 = MotionPaths.f1655t[this.f1642n[i25]] + " [";
                for (int i26 = 0; i26 < size; i26++) {
                    str7 = str7 + dArr[i26][i25];
                }
            }
            i25++;
        }
        this.f1637i = new CurveFit[this.f1645q.length + 1];
        int i27 = 0;
        while (true) {
            String[] strArr3 = this.f1645q;
            if (i27 >= strArr3.length) {
                break;
            }
            String str8 = strArr3[i27];
            int i28 = i14;
            int i29 = i28;
            double[] dArr3 = null;
            double[][] dArr4 = null;
            while (i28 < size) {
                if (motionPathsArr[i28].j(str8)) {
                    if (dArr4 == null) {
                        dArr3 = new double[size];
                        int[] iArr4 = new int[i15];
                        iArr4[1] = motionPathsArr[i28].h(str8);
                        iArr4[i14] = size;
                        dArr4 = (double[][]) Array.newInstance((Class<?>) double.class, iArr4);
                    }
                    dArr3[i29] = motionPathsArr[i28].f1658g;
                    motionPathsArr[i28].g(str8, dArr4[i29], 0);
                    i29++;
                }
                i28++;
                i15 = 2;
                i14 = 0;
            }
            i27++;
            this.f1637i[i27] = CurveFit.a(this.f1632d, Arrays.copyOf(dArr3, i29), (double[][]) Arrays.copyOf(dArr4, i29));
            i15 = 2;
            i14 = 0;
        }
        this.f1637i[0] = CurveFit.a(this.f1632d, dArr2, dArr);
        if (motionPathsArr[0].f1666o != c.f1481f) {
            int[] iArr5 = new int[size];
            double[] dArr5 = new double[size];
            double[][] dArr6 = (double[][]) Array.newInstance((Class<?>) double.class, size, 2);
            for (int i30 = 0; i30 < size; i30++) {
                iArr5[i30] = motionPathsArr[i30].f1666o;
                dArr5[i30] = motionPathsArr[i30].f1658g;
                dArr6[i30][0] = motionPathsArr[i30].f1660i;
                dArr6[i30][1] = motionPathsArr[i30].f1661j;
            }
            this.f1638j = CurveFit.b(iArr5, dArr5, dArr6);
        }
        float f11 = Float.NaN;
        this.f1654z = new HashMap<>();
        if (this.f1651w != null) {
            Iterator<String> it9 = hashSet3.iterator();
            while (it9.hasNext()) {
                String next8 = it9.next();
                KeyCycleOscillator c11 = KeyCycleOscillator.c(next8);
                if (c11 != null) {
                    if (c11.i() && Float.isNaN(f11)) {
                        f11 = m();
                    }
                    c11.g(next8);
                    this.f1654z.put(next8, c11);
                }
            }
            Iterator<c> it10 = this.f1651w.iterator();
            while (it10.hasNext()) {
                c next9 = it10.next();
                if (next9 instanceof KeyCycle) {
                    ((KeyCycle) next9).O(this.f1654z);
                }
            }
            Iterator<KeyCycleOscillator> it11 = this.f1654z.values().iterator();
            while (it11.hasNext()) {
                it11.next().h(f11);
            }
        }
    }
}
