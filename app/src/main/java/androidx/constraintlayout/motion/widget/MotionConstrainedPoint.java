package androidx.constraintlayout.motion.widget;

import android.util.Log;
import android.view.View;
import androidx.constraintlayout.motion.widget.SplineSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import k.Easing;
import m.ConstraintWidget;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MotionConstrainedPoint.java */
/* renamed from: androidx.constraintlayout.motion.widget.m, reason: use source file name */
/* loaded from: classes.dex */
public class MotionConstrainedPoint implements Comparable<MotionConstrainedPoint> {
    static String[] G = {"position", "x", "y", "width", "height", "pathRotate"};

    /* renamed from: g, reason: collision with root package name */
    int f1609g;

    /* renamed from: t, reason: collision with root package name */
    private Easing f1622t;

    /* renamed from: v, reason: collision with root package name */
    private float f1624v;

    /* renamed from: w, reason: collision with root package name */
    private float f1625w;

    /* renamed from: x, reason: collision with root package name */
    private float f1626x;

    /* renamed from: y, reason: collision with root package name */
    private float f1627y;

    /* renamed from: z, reason: collision with root package name */
    private float f1628z;

    /* renamed from: e, reason: collision with root package name */
    private float f1607e = 1.0f;

    /* renamed from: f, reason: collision with root package name */
    int f1608f = 0;

    /* renamed from: h, reason: collision with root package name */
    private boolean f1610h = false;

    /* renamed from: i, reason: collision with root package name */
    private float f1611i = 0.0f;

    /* renamed from: j, reason: collision with root package name */
    private float f1612j = 0.0f;

    /* renamed from: k, reason: collision with root package name */
    private float f1613k = 0.0f;

    /* renamed from: l, reason: collision with root package name */
    public float f1614l = 0.0f;

    /* renamed from: m, reason: collision with root package name */
    private float f1615m = 1.0f;

    /* renamed from: n, reason: collision with root package name */
    private float f1616n = 1.0f;

    /* renamed from: o, reason: collision with root package name */
    private float f1617o = Float.NaN;

    /* renamed from: p, reason: collision with root package name */
    private float f1618p = Float.NaN;

    /* renamed from: q, reason: collision with root package name */
    private float f1619q = 0.0f;

    /* renamed from: r, reason: collision with root package name */
    private float f1620r = 0.0f;

    /* renamed from: s, reason: collision with root package name */
    private float f1621s = 0.0f;

    /* renamed from: u, reason: collision with root package name */
    private int f1623u = 0;
    private float A = Float.NaN;
    private float B = Float.NaN;
    LinkedHashMap<String, ConstraintAttribute> C = new LinkedHashMap<>();
    int D = 0;
    double[] E = new double[18];
    double[] F = new double[18];

    private boolean e(float f10, float f11) {
        return (Float.isNaN(f10) || Float.isNaN(f11)) ? Float.isNaN(f10) != Float.isNaN(f11) : Math.abs(f10 - f11) > 1.0E-6f;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x0023. Please report as an issue. */
    public void a(HashMap<String, SplineSet> hashMap, int i10) {
        for (String str : hashMap.keySet()) {
            SplineSet splineSet = hashMap.get(str);
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
                case -760884510:
                    if (str.equals("transformPivotX")) {
                        c10 = '\b';
                        break;
                    }
                    break;
                case -760884509:
                    if (str.equals("transformPivotY")) {
                        c10 = '\t';
                        break;
                    }
                    break;
                case -40300674:
                    if (str.equals("rotation")) {
                        c10 = '\n';
                        break;
                    }
                    break;
                case -4379043:
                    if (str.equals("elevation")) {
                        c10 = 11;
                        break;
                    }
                    break;
                case 37232917:
                    if (str.equals("transitionPathRotate")) {
                        c10 = '\f';
                        break;
                    }
                    break;
                case 92909918:
                    if (str.equals("alpha")) {
                        c10 = '\r';
                        break;
                    }
                    break;
            }
            switch (c10) {
                case 0:
                    splineSet.e(i10, Float.isNaN(this.f1613k) ? 0.0f : this.f1613k);
                    break;
                case 1:
                    splineSet.e(i10, Float.isNaN(this.f1614l) ? 0.0f : this.f1614l);
                    break;
                case 2:
                    splineSet.e(i10, Float.isNaN(this.f1619q) ? 0.0f : this.f1619q);
                    break;
                case 3:
                    splineSet.e(i10, Float.isNaN(this.f1620r) ? 0.0f : this.f1620r);
                    break;
                case 4:
                    splineSet.e(i10, Float.isNaN(this.f1621s) ? 0.0f : this.f1621s);
                    break;
                case 5:
                    splineSet.e(i10, Float.isNaN(this.B) ? 0.0f : this.B);
                    break;
                case 6:
                    splineSet.e(i10, Float.isNaN(this.f1615m) ? 1.0f : this.f1615m);
                    break;
                case 7:
                    splineSet.e(i10, Float.isNaN(this.f1616n) ? 1.0f : this.f1616n);
                    break;
                case '\b':
                    splineSet.e(i10, Float.isNaN(this.f1617o) ? 0.0f : this.f1617o);
                    break;
                case '\t':
                    splineSet.e(i10, Float.isNaN(this.f1618p) ? 0.0f : this.f1618p);
                    break;
                case '\n':
                    splineSet.e(i10, Float.isNaN(this.f1612j) ? 0.0f : this.f1612j);
                    break;
                case 11:
                    splineSet.e(i10, Float.isNaN(this.f1611i) ? 0.0f : this.f1611i);
                    break;
                case '\f':
                    splineSet.e(i10, Float.isNaN(this.A) ? 0.0f : this.A);
                    break;
                case '\r':
                    splineSet.e(i10, Float.isNaN(this.f1607e) ? 1.0f : this.f1607e);
                    break;
                default:
                    if (!str.startsWith("CUSTOM")) {
                        Log.e("MotionPaths", "UNKNOWN spline " + str);
                        break;
                    } else {
                        String str2 = str.split(",")[1];
                        if (!this.C.containsKey(str2)) {
                            Log.e("MotionPaths", "UNKNOWN customName " + str2);
                            break;
                        } else {
                            ConstraintAttribute constraintAttribute = this.C.get(str2);
                            if (splineSet instanceof SplineSet.b) {
                                ((SplineSet.b) splineSet).i(i10, constraintAttribute);
                                break;
                            } else {
                                Log.e("MotionPaths", str + " splineSet not a CustomSet frame = " + i10 + ", value" + constraintAttribute.d() + splineSet);
                                break;
                            }
                        }
                    }
            }
        }
    }

    public void b(View view) {
        this.f1609g = view.getVisibility();
        this.f1607e = view.getVisibility() != 0 ? 0.0f : view.getAlpha();
        this.f1610h = false;
        this.f1611i = view.getElevation();
        this.f1612j = view.getRotation();
        this.f1613k = view.getRotationX();
        this.f1614l = view.getRotationY();
        this.f1615m = view.getScaleX();
        this.f1616n = view.getScaleY();
        this.f1617o = view.getPivotX();
        this.f1618p = view.getPivotY();
        this.f1619q = view.getTranslationX();
        this.f1620r = view.getTranslationY();
        this.f1621s = view.getTranslationZ();
    }

    public void c(ConstraintSet.a aVar) {
        ConstraintSet.d dVar = aVar.f1953b;
        int i10 = dVar.f2005c;
        this.f1608f = i10;
        int i11 = dVar.f2004b;
        this.f1609g = i11;
        this.f1607e = (i11 == 0 || i10 != 0) ? dVar.f2006d : 0.0f;
        ConstraintSet.e eVar = aVar.f1956e;
        this.f1610h = eVar.f2020l;
        this.f1611i = eVar.f2021m;
        this.f1612j = eVar.f2010b;
        this.f1613k = eVar.f2011c;
        this.f1614l = eVar.f2012d;
        this.f1615m = eVar.f2013e;
        this.f1616n = eVar.f2014f;
        this.f1617o = eVar.f2015g;
        this.f1618p = eVar.f2016h;
        this.f1619q = eVar.f2017i;
        this.f1620r = eVar.f2018j;
        this.f1621s = eVar.f2019k;
        this.f1622t = Easing.c(aVar.f1954c.f1998c);
        ConstraintSet.c cVar = aVar.f1954c;
        this.A = cVar.f2002g;
        this.f1623u = cVar.f2000e;
        this.B = aVar.f1953b.f2007e;
        for (String str : aVar.f1957f.keySet()) {
            ConstraintAttribute constraintAttribute = aVar.f1957f.get(str);
            if (constraintAttribute.c() != ConstraintAttribute.b.STRING_TYPE) {
                this.C.put(str, constraintAttribute);
            }
        }
    }

    @Override // java.lang.Comparable
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public int compareTo(MotionConstrainedPoint motionConstrainedPoint) {
        return Float.compare(this.f1624v, motionConstrainedPoint.f1624v);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(MotionConstrainedPoint motionConstrainedPoint, HashSet<String> hashSet) {
        if (e(this.f1607e, motionConstrainedPoint.f1607e)) {
            hashSet.add("alpha");
        }
        if (e(this.f1611i, motionConstrainedPoint.f1611i)) {
            hashSet.add("elevation");
        }
        int i10 = this.f1609g;
        int i11 = motionConstrainedPoint.f1609g;
        if (i10 != i11 && this.f1608f == 0 && (i10 == 0 || i11 == 0)) {
            hashSet.add("alpha");
        }
        if (e(this.f1612j, motionConstrainedPoint.f1612j)) {
            hashSet.add("rotation");
        }
        if (!Float.isNaN(this.A) || !Float.isNaN(motionConstrainedPoint.A)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.B) || !Float.isNaN(motionConstrainedPoint.B)) {
            hashSet.add("progress");
        }
        if (e(this.f1613k, motionConstrainedPoint.f1613k)) {
            hashSet.add("rotationX");
        }
        if (e(this.f1614l, motionConstrainedPoint.f1614l)) {
            hashSet.add("rotationY");
        }
        if (e(this.f1617o, motionConstrainedPoint.f1617o)) {
            hashSet.add("transformPivotX");
        }
        if (e(this.f1618p, motionConstrainedPoint.f1618p)) {
            hashSet.add("transformPivotY");
        }
        if (e(this.f1615m, motionConstrainedPoint.f1615m)) {
            hashSet.add("scaleX");
        }
        if (e(this.f1616n, motionConstrainedPoint.f1616n)) {
            hashSet.add("scaleY");
        }
        if (e(this.f1619q, motionConstrainedPoint.f1619q)) {
            hashSet.add("translationX");
        }
        if (e(this.f1620r, motionConstrainedPoint.f1620r)) {
            hashSet.add("translationY");
        }
        if (e(this.f1621s, motionConstrainedPoint.f1621s)) {
            hashSet.add("translationZ");
        }
    }

    void g(float f10, float f11, float f12, float f13) {
        this.f1625w = f10;
        this.f1626x = f11;
        this.f1627y = f12;
        this.f1628z = f13;
    }

    public void h(View view) {
        g(view.getX(), view.getY(), view.getWidth(), view.getHeight());
        b(view);
    }

    public void i(ConstraintWidget constraintWidget, ConstraintSet constraintSet, int i10) {
        g(constraintWidget.R(), constraintWidget.S(), constraintWidget.Q(), constraintWidget.w());
        c(constraintSet.t(i10));
    }
}
