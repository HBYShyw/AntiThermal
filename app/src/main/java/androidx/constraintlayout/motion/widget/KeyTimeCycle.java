package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.motion.widget.TimeCycleSplineSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.R$styleable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/* compiled from: KeyTimeCycle.java */
/* renamed from: androidx.constraintlayout.motion.widget.k, reason: use source file name */
/* loaded from: classes.dex */
public class KeyTimeCycle extends c {

    /* renamed from: g, reason: collision with root package name */
    private String f1568g;

    /* renamed from: h, reason: collision with root package name */
    private int f1569h = -1;

    /* renamed from: i, reason: collision with root package name */
    private float f1570i = Float.NaN;

    /* renamed from: j, reason: collision with root package name */
    private float f1571j = Float.NaN;

    /* renamed from: k, reason: collision with root package name */
    private float f1572k = Float.NaN;

    /* renamed from: l, reason: collision with root package name */
    private float f1573l = Float.NaN;

    /* renamed from: m, reason: collision with root package name */
    private float f1574m = Float.NaN;

    /* renamed from: n, reason: collision with root package name */
    private float f1575n = Float.NaN;

    /* renamed from: o, reason: collision with root package name */
    private float f1576o = Float.NaN;

    /* renamed from: p, reason: collision with root package name */
    private float f1577p = Float.NaN;

    /* renamed from: q, reason: collision with root package name */
    private float f1578q = Float.NaN;

    /* renamed from: r, reason: collision with root package name */
    private float f1579r = Float.NaN;

    /* renamed from: s, reason: collision with root package name */
    private float f1580s = Float.NaN;

    /* renamed from: t, reason: collision with root package name */
    private float f1581t = Float.NaN;

    /* renamed from: u, reason: collision with root package name */
    private int f1582u = 0;

    /* renamed from: v, reason: collision with root package name */
    private float f1583v = Float.NaN;

    /* renamed from: w, reason: collision with root package name */
    private float f1584w = 0.0f;

    /* compiled from: KeyTimeCycle.java */
    /* renamed from: androidx.constraintlayout.motion.widget.k$a */
    /* loaded from: classes.dex */
    private static class a {

        /* renamed from: a, reason: collision with root package name */
        private static SparseIntArray f1585a;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f1585a = sparseIntArray;
            sparseIntArray.append(R$styleable.KeyTimeCycle_android_alpha, 1);
            f1585a.append(R$styleable.KeyTimeCycle_android_elevation, 2);
            f1585a.append(R$styleable.KeyTimeCycle_android_rotation, 4);
            f1585a.append(R$styleable.KeyTimeCycle_android_rotationX, 5);
            f1585a.append(R$styleable.KeyTimeCycle_android_rotationY, 6);
            f1585a.append(R$styleable.KeyTimeCycle_android_scaleX, 7);
            f1585a.append(R$styleable.KeyTimeCycle_transitionPathRotate, 8);
            f1585a.append(R$styleable.KeyTimeCycle_transitionEasing, 9);
            f1585a.append(R$styleable.KeyTimeCycle_motionTarget, 10);
            f1585a.append(R$styleable.KeyTimeCycle_framePosition, 12);
            f1585a.append(R$styleable.KeyTimeCycle_curveFit, 13);
            f1585a.append(R$styleable.KeyTimeCycle_android_scaleY, 14);
            f1585a.append(R$styleable.KeyTimeCycle_android_translationX, 15);
            f1585a.append(R$styleable.KeyTimeCycle_android_translationY, 16);
            f1585a.append(R$styleable.KeyTimeCycle_android_translationZ, 17);
            f1585a.append(R$styleable.KeyTimeCycle_motionProgress, 18);
            f1585a.append(R$styleable.KeyTimeCycle_wavePeriod, 20);
            f1585a.append(R$styleable.KeyTimeCycle_waveOffset, 21);
            f1585a.append(R$styleable.KeyTimeCycle_waveShape, 19);
        }

        public static void a(KeyTimeCycle keyTimeCycle, TypedArray typedArray) {
            int indexCount = typedArray.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = typedArray.getIndex(i10);
                switch (f1585a.get(index)) {
                    case 1:
                        keyTimeCycle.f1570i = typedArray.getFloat(index, keyTimeCycle.f1570i);
                        break;
                    case 2:
                        keyTimeCycle.f1571j = typedArray.getDimension(index, keyTimeCycle.f1571j);
                        break;
                    case 3:
                    case 11:
                    default:
                        Log.e("KeyTimeCycle", "unused attribute 0x" + Integer.toHexString(index) + "   " + f1585a.get(index));
                        break;
                    case 4:
                        keyTimeCycle.f1572k = typedArray.getFloat(index, keyTimeCycle.f1572k);
                        break;
                    case 5:
                        keyTimeCycle.f1573l = typedArray.getFloat(index, keyTimeCycle.f1573l);
                        break;
                    case 6:
                        keyTimeCycle.f1574m = typedArray.getFloat(index, keyTimeCycle.f1574m);
                        break;
                    case 7:
                        keyTimeCycle.f1576o = typedArray.getFloat(index, keyTimeCycle.f1576o);
                        break;
                    case 8:
                        keyTimeCycle.f1575n = typedArray.getFloat(index, keyTimeCycle.f1575n);
                        break;
                    case 9:
                        keyTimeCycle.f1568g = typedArray.getString(index);
                        break;
                    case 10:
                        if (MotionLayout.R0) {
                            int resourceId = typedArray.getResourceId(index, keyTimeCycle.f1483b);
                            keyTimeCycle.f1483b = resourceId;
                            if (resourceId == -1) {
                                keyTimeCycle.f1484c = typedArray.getString(index);
                                break;
                            } else {
                                break;
                            }
                        } else if (typedArray.peekValue(index).type == 3) {
                            keyTimeCycle.f1484c = typedArray.getString(index);
                            break;
                        } else {
                            keyTimeCycle.f1483b = typedArray.getResourceId(index, keyTimeCycle.f1483b);
                            break;
                        }
                    case 12:
                        keyTimeCycle.f1482a = typedArray.getInt(index, keyTimeCycle.f1482a);
                        break;
                    case 13:
                        keyTimeCycle.f1569h = typedArray.getInteger(index, keyTimeCycle.f1569h);
                        break;
                    case 14:
                        keyTimeCycle.f1577p = typedArray.getFloat(index, keyTimeCycle.f1577p);
                        break;
                    case 15:
                        keyTimeCycle.f1578q = typedArray.getDimension(index, keyTimeCycle.f1578q);
                        break;
                    case 16:
                        keyTimeCycle.f1579r = typedArray.getDimension(index, keyTimeCycle.f1579r);
                        break;
                    case 17:
                        keyTimeCycle.f1580s = typedArray.getDimension(index, keyTimeCycle.f1580s);
                        break;
                    case 18:
                        keyTimeCycle.f1581t = typedArray.getFloat(index, keyTimeCycle.f1581t);
                        break;
                    case 19:
                        keyTimeCycle.f1582u = typedArray.getInt(index, keyTimeCycle.f1582u);
                        break;
                    case 20:
                        keyTimeCycle.f1583v = typedArray.getFloat(index, keyTimeCycle.f1583v);
                        break;
                    case 21:
                        if (typedArray.peekValue(index).type == 5) {
                            keyTimeCycle.f1584w = typedArray.getDimension(index, keyTimeCycle.f1584w);
                            break;
                        } else {
                            keyTimeCycle.f1584w = typedArray.getFloat(index, keyTimeCycle.f1584w);
                            break;
                        }
                }
            }
        }
    }

    public KeyTimeCycle() {
        this.f1485d = 3;
        this.f1486e = new HashMap<>();
    }

    /* JADX WARN: Code restructure failed: missing block: B:112:0x0086, code lost:
    
        if (r1.equals("scaleY") == false) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void M(HashMap<String, TimeCycleSplineSet> hashMap) {
        Iterator<String> it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String next = it.next();
            TimeCycleSplineSet timeCycleSplineSet = hashMap.get(next);
            char c10 = 7;
            if (next.startsWith("CUSTOM")) {
                ConstraintAttribute constraintAttribute = this.f1486e.get(next.substring(7));
                if (constraintAttribute != null) {
                    ((TimeCycleSplineSet.b) timeCycleSplineSet).j(this.f1482a, constraintAttribute, this.f1583v, this.f1582u, this.f1584w);
                }
            } else {
                switch (next.hashCode()) {
                    case -1249320806:
                        if (next.equals("rotationX")) {
                            c10 = 0;
                            break;
                        }
                        break;
                    case -1249320805:
                        if (next.equals("rotationY")) {
                            c10 = 1;
                            break;
                        }
                        break;
                    case -1225497657:
                        if (next.equals("translationX")) {
                            c10 = 2;
                            break;
                        }
                        break;
                    case -1225497656:
                        if (next.equals("translationY")) {
                            c10 = 3;
                            break;
                        }
                        break;
                    case -1225497655:
                        if (next.equals("translationZ")) {
                            c10 = 4;
                            break;
                        }
                        break;
                    case -1001078227:
                        if (next.equals("progress")) {
                            c10 = 5;
                            break;
                        }
                        break;
                    case -908189618:
                        if (next.equals("scaleX")) {
                            c10 = 6;
                            break;
                        }
                        break;
                    case -908189617:
                        break;
                    case -40300674:
                        if (next.equals("rotation")) {
                            c10 = '\b';
                            break;
                        }
                        break;
                    case -4379043:
                        if (next.equals("elevation")) {
                            c10 = '\t';
                            break;
                        }
                        break;
                    case 37232917:
                        if (next.equals("transitionPathRotate")) {
                            c10 = '\n';
                            break;
                        }
                        break;
                    case 92909918:
                        if (next.equals("alpha")) {
                            c10 = 11;
                            break;
                        }
                        break;
                }
                c10 = 65535;
                switch (c10) {
                    case 0:
                        if (!Float.isNaN(this.f1573l)) {
                            timeCycleSplineSet.e(this.f1482a, this.f1573l, this.f1583v, this.f1582u, this.f1584w);
                            break;
                        } else {
                            break;
                        }
                    case 1:
                        if (!Float.isNaN(this.f1574m)) {
                            timeCycleSplineSet.e(this.f1482a, this.f1574m, this.f1583v, this.f1582u, this.f1584w);
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        if (!Float.isNaN(this.f1578q)) {
                            timeCycleSplineSet.e(this.f1482a, this.f1578q, this.f1583v, this.f1582u, this.f1584w);
                            break;
                        } else {
                            break;
                        }
                    case 3:
                        if (!Float.isNaN(this.f1579r)) {
                            timeCycleSplineSet.e(this.f1482a, this.f1579r, this.f1583v, this.f1582u, this.f1584w);
                            break;
                        } else {
                            break;
                        }
                    case 4:
                        if (!Float.isNaN(this.f1580s)) {
                            timeCycleSplineSet.e(this.f1482a, this.f1580s, this.f1583v, this.f1582u, this.f1584w);
                            break;
                        } else {
                            break;
                        }
                    case 5:
                        if (!Float.isNaN(this.f1581t)) {
                            timeCycleSplineSet.e(this.f1482a, this.f1581t, this.f1583v, this.f1582u, this.f1584w);
                            break;
                        } else {
                            break;
                        }
                    case 6:
                        if (!Float.isNaN(this.f1576o)) {
                            timeCycleSplineSet.e(this.f1482a, this.f1576o, this.f1583v, this.f1582u, this.f1584w);
                            break;
                        } else {
                            break;
                        }
                    case 7:
                        if (!Float.isNaN(this.f1577p)) {
                            timeCycleSplineSet.e(this.f1482a, this.f1577p, this.f1583v, this.f1582u, this.f1584w);
                            break;
                        } else {
                            break;
                        }
                    case '\b':
                        if (!Float.isNaN(this.f1572k)) {
                            timeCycleSplineSet.e(this.f1482a, this.f1572k, this.f1583v, this.f1582u, this.f1584w);
                            break;
                        } else {
                            break;
                        }
                    case '\t':
                        if (!Float.isNaN(this.f1571j)) {
                            timeCycleSplineSet.e(this.f1482a, this.f1571j, this.f1583v, this.f1582u, this.f1584w);
                            break;
                        } else {
                            break;
                        }
                    case '\n':
                        if (!Float.isNaN(this.f1575n)) {
                            timeCycleSplineSet.e(this.f1482a, this.f1575n, this.f1583v, this.f1582u, this.f1584w);
                            break;
                        } else {
                            break;
                        }
                    case 11:
                        if (!Float.isNaN(this.f1570i)) {
                            timeCycleSplineSet.e(this.f1482a, this.f1570i, this.f1583v, this.f1582u, this.f1584w);
                            break;
                        } else {
                            break;
                        }
                    default:
                        Log.e("KeyTimeCycles", "UNKNOWN addValues \"" + next + "\"");
                        break;
                }
            }
        }
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void a(HashMap<String, SplineSet> hashMap) {
        throw new IllegalArgumentException(" KeyTimeCycles do not support SplineSet");
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void b(HashSet<String> hashSet) {
        if (!Float.isNaN(this.f1570i)) {
            hashSet.add("alpha");
        }
        if (!Float.isNaN(this.f1571j)) {
            hashSet.add("elevation");
        }
        if (!Float.isNaN(this.f1572k)) {
            hashSet.add("rotation");
        }
        if (!Float.isNaN(this.f1573l)) {
            hashSet.add("rotationX");
        }
        if (!Float.isNaN(this.f1574m)) {
            hashSet.add("rotationY");
        }
        if (!Float.isNaN(this.f1578q)) {
            hashSet.add("translationX");
        }
        if (!Float.isNaN(this.f1579r)) {
            hashSet.add("translationY");
        }
        if (!Float.isNaN(this.f1580s)) {
            hashSet.add("translationZ");
        }
        if (!Float.isNaN(this.f1575n)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.f1576o)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.f1577p)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.f1581t)) {
            hashSet.add("progress");
        }
        if (this.f1486e.size() > 0) {
            Iterator<String> it = this.f1486e.keySet().iterator();
            while (it.hasNext()) {
                hashSet.add("CUSTOM," + it.next());
            }
        }
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void c(Context context, AttributeSet attributeSet) {
        a.a(this, context.obtainStyledAttributes(attributeSet, R$styleable.KeyTimeCycle));
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void e(HashMap<String, Integer> hashMap) {
        if (this.f1569h == -1) {
            return;
        }
        if (!Float.isNaN(this.f1570i)) {
            hashMap.put("alpha", Integer.valueOf(this.f1569h));
        }
        if (!Float.isNaN(this.f1571j)) {
            hashMap.put("elevation", Integer.valueOf(this.f1569h));
        }
        if (!Float.isNaN(this.f1572k)) {
            hashMap.put("rotation", Integer.valueOf(this.f1569h));
        }
        if (!Float.isNaN(this.f1573l)) {
            hashMap.put("rotationX", Integer.valueOf(this.f1569h));
        }
        if (!Float.isNaN(this.f1574m)) {
            hashMap.put("rotationY", Integer.valueOf(this.f1569h));
        }
        if (!Float.isNaN(this.f1578q)) {
            hashMap.put("translationX", Integer.valueOf(this.f1569h));
        }
        if (!Float.isNaN(this.f1579r)) {
            hashMap.put("translationY", Integer.valueOf(this.f1569h));
        }
        if (!Float.isNaN(this.f1580s)) {
            hashMap.put("translationZ", Integer.valueOf(this.f1569h));
        }
        if (!Float.isNaN(this.f1575n)) {
            hashMap.put("transitionPathRotate", Integer.valueOf(this.f1569h));
        }
        if (!Float.isNaN(this.f1576o)) {
            hashMap.put("scaleX", Integer.valueOf(this.f1569h));
        }
        if (!Float.isNaN(this.f1576o)) {
            hashMap.put("scaleY", Integer.valueOf(this.f1569h));
        }
        if (!Float.isNaN(this.f1581t)) {
            hashMap.put("progress", Integer.valueOf(this.f1569h));
        }
        if (this.f1486e.size() > 0) {
            Iterator<String> it = this.f1486e.keySet().iterator();
            while (it.hasNext()) {
                hashMap.put("CUSTOM," + it.next(), Integer.valueOf(this.f1569h));
            }
        }
    }
}
