package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.motion.widget.SplineSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.R$styleable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/* compiled from: KeyAttributes.java */
/* renamed from: androidx.constraintlayout.motion.widget.d, reason: use source file name */
/* loaded from: classes.dex */
public class KeyAttributes extends c {

    /* renamed from: g, reason: collision with root package name */
    private String f1487g;

    /* renamed from: h, reason: collision with root package name */
    private int f1488h = -1;

    /* renamed from: i, reason: collision with root package name */
    private boolean f1489i = false;

    /* renamed from: j, reason: collision with root package name */
    private float f1490j = Float.NaN;

    /* renamed from: k, reason: collision with root package name */
    private float f1491k = Float.NaN;

    /* renamed from: l, reason: collision with root package name */
    private float f1492l = Float.NaN;

    /* renamed from: m, reason: collision with root package name */
    private float f1493m = Float.NaN;

    /* renamed from: n, reason: collision with root package name */
    private float f1494n = Float.NaN;

    /* renamed from: o, reason: collision with root package name */
    private float f1495o = Float.NaN;

    /* renamed from: p, reason: collision with root package name */
    private float f1496p = Float.NaN;

    /* renamed from: q, reason: collision with root package name */
    private float f1497q = Float.NaN;

    /* renamed from: r, reason: collision with root package name */
    private float f1498r = Float.NaN;

    /* renamed from: s, reason: collision with root package name */
    private float f1499s = Float.NaN;

    /* renamed from: t, reason: collision with root package name */
    private float f1500t = Float.NaN;

    /* renamed from: u, reason: collision with root package name */
    private float f1501u = Float.NaN;

    /* renamed from: v, reason: collision with root package name */
    private float f1502v = Float.NaN;

    /* renamed from: w, reason: collision with root package name */
    private float f1503w = Float.NaN;

    /* compiled from: KeyAttributes.java */
    /* renamed from: androidx.constraintlayout.motion.widget.d$a */
    /* loaded from: classes.dex */
    private static class a {

        /* renamed from: a, reason: collision with root package name */
        private static SparseIntArray f1504a;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f1504a = sparseIntArray;
            sparseIntArray.append(R$styleable.KeyAttribute_android_alpha, 1);
            f1504a.append(R$styleable.KeyAttribute_android_elevation, 2);
            f1504a.append(R$styleable.KeyAttribute_android_rotation, 4);
            f1504a.append(R$styleable.KeyAttribute_android_rotationX, 5);
            f1504a.append(R$styleable.KeyAttribute_android_rotationY, 6);
            f1504a.append(R$styleable.KeyAttribute_android_transformPivotX, 19);
            f1504a.append(R$styleable.KeyAttribute_android_transformPivotY, 20);
            f1504a.append(R$styleable.KeyAttribute_android_scaleX, 7);
            f1504a.append(R$styleable.KeyAttribute_transitionPathRotate, 8);
            f1504a.append(R$styleable.KeyAttribute_transitionEasing, 9);
            f1504a.append(R$styleable.KeyAttribute_motionTarget, 10);
            f1504a.append(R$styleable.KeyAttribute_framePosition, 12);
            f1504a.append(R$styleable.KeyAttribute_curveFit, 13);
            f1504a.append(R$styleable.KeyAttribute_android_scaleY, 14);
            f1504a.append(R$styleable.KeyAttribute_android_translationX, 15);
            f1504a.append(R$styleable.KeyAttribute_android_translationY, 16);
            f1504a.append(R$styleable.KeyAttribute_android_translationZ, 17);
            f1504a.append(R$styleable.KeyAttribute_motionProgress, 18);
        }

        public static void a(KeyAttributes keyAttributes, TypedArray typedArray) {
            int indexCount = typedArray.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = typedArray.getIndex(i10);
                switch (f1504a.get(index)) {
                    case 1:
                        keyAttributes.f1490j = typedArray.getFloat(index, keyAttributes.f1490j);
                        break;
                    case 2:
                        keyAttributes.f1491k = typedArray.getDimension(index, keyAttributes.f1491k);
                        break;
                    case 3:
                    case 11:
                    default:
                        Log.e("KeyAttribute", "unused attribute 0x" + Integer.toHexString(index) + "   " + f1504a.get(index));
                        break;
                    case 4:
                        keyAttributes.f1492l = typedArray.getFloat(index, keyAttributes.f1492l);
                        break;
                    case 5:
                        keyAttributes.f1493m = typedArray.getFloat(index, keyAttributes.f1493m);
                        break;
                    case 6:
                        keyAttributes.f1494n = typedArray.getFloat(index, keyAttributes.f1494n);
                        break;
                    case 7:
                        keyAttributes.f1498r = typedArray.getFloat(index, keyAttributes.f1498r);
                        break;
                    case 8:
                        keyAttributes.f1497q = typedArray.getFloat(index, keyAttributes.f1497q);
                        break;
                    case 9:
                        keyAttributes.f1487g = typedArray.getString(index);
                        break;
                    case 10:
                        if (MotionLayout.R0) {
                            int resourceId = typedArray.getResourceId(index, keyAttributes.f1483b);
                            keyAttributes.f1483b = resourceId;
                            if (resourceId == -1) {
                                keyAttributes.f1484c = typedArray.getString(index);
                                break;
                            } else {
                                break;
                            }
                        } else if (typedArray.peekValue(index).type == 3) {
                            keyAttributes.f1484c = typedArray.getString(index);
                            break;
                        } else {
                            keyAttributes.f1483b = typedArray.getResourceId(index, keyAttributes.f1483b);
                            break;
                        }
                    case 12:
                        keyAttributes.f1482a = typedArray.getInt(index, keyAttributes.f1482a);
                        break;
                    case 13:
                        keyAttributes.f1488h = typedArray.getInteger(index, keyAttributes.f1488h);
                        break;
                    case 14:
                        keyAttributes.f1499s = typedArray.getFloat(index, keyAttributes.f1499s);
                        break;
                    case 15:
                        keyAttributes.f1500t = typedArray.getDimension(index, keyAttributes.f1500t);
                        break;
                    case 16:
                        keyAttributes.f1501u = typedArray.getDimension(index, keyAttributes.f1501u);
                        break;
                    case 17:
                        keyAttributes.f1502v = typedArray.getDimension(index, keyAttributes.f1502v);
                        break;
                    case 18:
                        keyAttributes.f1503w = typedArray.getFloat(index, keyAttributes.f1503w);
                        break;
                    case 19:
                        keyAttributes.f1495o = typedArray.getDimension(index, keyAttributes.f1495o);
                        break;
                    case 20:
                        keyAttributes.f1496p = typedArray.getDimension(index, keyAttributes.f1496p);
                        break;
                }
            }
        }
    }

    public KeyAttributes() {
        this.f1485d = 1;
        this.f1486e = new HashMap<>();
    }

    /* JADX WARN: Code restructure failed: missing block: B:132:0x0097, code lost:
    
        if (r1.equals("scaleY") == false) goto L12;
     */
    @Override // androidx.constraintlayout.motion.widget.c
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void a(HashMap<String, SplineSet> hashMap) {
        Iterator<String> it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String next = it.next();
            SplineSet splineSet = hashMap.get(next);
            char c10 = 7;
            if (next.startsWith("CUSTOM")) {
                ConstraintAttribute constraintAttribute = this.f1486e.get(next.substring(7));
                if (constraintAttribute != null) {
                    ((SplineSet.b) splineSet).i(this.f1482a, constraintAttribute);
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
                    case -760884510:
                        if (next.equals("transformPivotX")) {
                            c10 = '\b';
                            break;
                        }
                        break;
                    case -760884509:
                        if (next.equals("transformPivotY")) {
                            c10 = '\t';
                            break;
                        }
                        break;
                    case -40300674:
                        if (next.equals("rotation")) {
                            c10 = '\n';
                            break;
                        }
                        break;
                    case -4379043:
                        if (next.equals("elevation")) {
                            c10 = 11;
                            break;
                        }
                        break;
                    case 37232917:
                        if (next.equals("transitionPathRotate")) {
                            c10 = '\f';
                            break;
                        }
                        break;
                    case 92909918:
                        if (next.equals("alpha")) {
                            c10 = '\r';
                            break;
                        }
                        break;
                }
                c10 = 65535;
                switch (c10) {
                    case 0:
                        if (!Float.isNaN(this.f1493m)) {
                            splineSet.e(this.f1482a, this.f1493m);
                            break;
                        } else {
                            break;
                        }
                    case 1:
                        if (!Float.isNaN(this.f1494n)) {
                            splineSet.e(this.f1482a, this.f1494n);
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        if (!Float.isNaN(this.f1500t)) {
                            splineSet.e(this.f1482a, this.f1500t);
                            break;
                        } else {
                            break;
                        }
                    case 3:
                        if (!Float.isNaN(this.f1501u)) {
                            splineSet.e(this.f1482a, this.f1501u);
                            break;
                        } else {
                            break;
                        }
                    case 4:
                        if (!Float.isNaN(this.f1502v)) {
                            splineSet.e(this.f1482a, this.f1502v);
                            break;
                        } else {
                            break;
                        }
                    case 5:
                        if (!Float.isNaN(this.f1503w)) {
                            splineSet.e(this.f1482a, this.f1503w);
                            break;
                        } else {
                            break;
                        }
                    case 6:
                        if (!Float.isNaN(this.f1498r)) {
                            splineSet.e(this.f1482a, this.f1498r);
                            break;
                        } else {
                            break;
                        }
                    case 7:
                        if (!Float.isNaN(this.f1499s)) {
                            splineSet.e(this.f1482a, this.f1499s);
                            break;
                        } else {
                            break;
                        }
                    case '\b':
                        if (!Float.isNaN(this.f1493m)) {
                            splineSet.e(this.f1482a, this.f1495o);
                            break;
                        } else {
                            break;
                        }
                    case '\t':
                        if (!Float.isNaN(this.f1494n)) {
                            splineSet.e(this.f1482a, this.f1496p);
                            break;
                        } else {
                            break;
                        }
                    case '\n':
                        if (!Float.isNaN(this.f1492l)) {
                            splineSet.e(this.f1482a, this.f1492l);
                            break;
                        } else {
                            break;
                        }
                    case 11:
                        if (!Float.isNaN(this.f1491k)) {
                            splineSet.e(this.f1482a, this.f1491k);
                            break;
                        } else {
                            break;
                        }
                    case '\f':
                        if (!Float.isNaN(this.f1497q)) {
                            splineSet.e(this.f1482a, this.f1497q);
                            break;
                        } else {
                            break;
                        }
                    case '\r':
                        if (!Float.isNaN(this.f1490j)) {
                            splineSet.e(this.f1482a, this.f1490j);
                            break;
                        } else {
                            break;
                        }
                    default:
                        Log.v("KeyAttributes", "UNKNOWN addValues \"" + next + "\"");
                        break;
                }
            }
        }
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void b(HashSet<String> hashSet) {
        if (!Float.isNaN(this.f1490j)) {
            hashSet.add("alpha");
        }
        if (!Float.isNaN(this.f1491k)) {
            hashSet.add("elevation");
        }
        if (!Float.isNaN(this.f1492l)) {
            hashSet.add("rotation");
        }
        if (!Float.isNaN(this.f1493m)) {
            hashSet.add("rotationX");
        }
        if (!Float.isNaN(this.f1494n)) {
            hashSet.add("rotationY");
        }
        if (!Float.isNaN(this.f1495o)) {
            hashSet.add("transformPivotX");
        }
        if (!Float.isNaN(this.f1496p)) {
            hashSet.add("transformPivotY");
        }
        if (!Float.isNaN(this.f1500t)) {
            hashSet.add("translationX");
        }
        if (!Float.isNaN(this.f1501u)) {
            hashSet.add("translationY");
        }
        if (!Float.isNaN(this.f1502v)) {
            hashSet.add("translationZ");
        }
        if (!Float.isNaN(this.f1497q)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.f1498r)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.f1498r)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.f1503w)) {
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
        a.a(this, context.obtainStyledAttributes(attributeSet, R$styleable.KeyAttribute));
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void e(HashMap<String, Integer> hashMap) {
        if (this.f1488h == -1) {
            return;
        }
        if (!Float.isNaN(this.f1490j)) {
            hashMap.put("alpha", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1491k)) {
            hashMap.put("elevation", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1492l)) {
            hashMap.put("rotation", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1493m)) {
            hashMap.put("rotationX", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1494n)) {
            hashMap.put("rotationY", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1495o)) {
            hashMap.put("transformPivotX", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1496p)) {
            hashMap.put("transformPivotY", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1500t)) {
            hashMap.put("translationX", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1501u)) {
            hashMap.put("translationY", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1502v)) {
            hashMap.put("translationZ", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1497q)) {
            hashMap.put("transitionPathRotate", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1498r)) {
            hashMap.put("scaleX", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1499s)) {
            hashMap.put("scaleY", Integer.valueOf(this.f1488h));
        }
        if (!Float.isNaN(this.f1503w)) {
            hashMap.put("progress", Integer.valueOf(this.f1488h));
        }
        if (this.f1486e.size() > 0) {
            Iterator<String> it = this.f1486e.keySet().iterator();
            while (it.hasNext()) {
                hashMap.put("CUSTOM," + it.next(), Integer.valueOf(this.f1488h));
            }
        }
    }
}
