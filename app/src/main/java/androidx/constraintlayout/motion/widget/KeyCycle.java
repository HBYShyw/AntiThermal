package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.R$styleable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/* compiled from: KeyCycle.java */
/* renamed from: androidx.constraintlayout.motion.widget.f, reason: use source file name */
/* loaded from: classes.dex */
public class KeyCycle extends c {

    /* renamed from: g, reason: collision with root package name */
    private String f1506g = null;

    /* renamed from: h, reason: collision with root package name */
    private int f1507h = 0;

    /* renamed from: i, reason: collision with root package name */
    private int f1508i = -1;

    /* renamed from: j, reason: collision with root package name */
    private float f1509j = Float.NaN;

    /* renamed from: k, reason: collision with root package name */
    private float f1510k = 0.0f;

    /* renamed from: l, reason: collision with root package name */
    private float f1511l = Float.NaN;

    /* renamed from: m, reason: collision with root package name */
    private int f1512m = -1;

    /* renamed from: n, reason: collision with root package name */
    private float f1513n = Float.NaN;

    /* renamed from: o, reason: collision with root package name */
    private float f1514o = Float.NaN;

    /* renamed from: p, reason: collision with root package name */
    private float f1515p = Float.NaN;

    /* renamed from: q, reason: collision with root package name */
    private float f1516q = Float.NaN;

    /* renamed from: r, reason: collision with root package name */
    private float f1517r = Float.NaN;

    /* renamed from: s, reason: collision with root package name */
    private float f1518s = Float.NaN;

    /* renamed from: t, reason: collision with root package name */
    private float f1519t = Float.NaN;

    /* renamed from: u, reason: collision with root package name */
    private float f1520u = Float.NaN;

    /* renamed from: v, reason: collision with root package name */
    private float f1521v = Float.NaN;

    /* renamed from: w, reason: collision with root package name */
    private float f1522w = Float.NaN;

    /* renamed from: x, reason: collision with root package name */
    private float f1523x = Float.NaN;

    /* compiled from: KeyCycle.java */
    /* renamed from: androidx.constraintlayout.motion.widget.f$a */
    /* loaded from: classes.dex */
    private static class a {

        /* renamed from: a, reason: collision with root package name */
        private static SparseIntArray f1524a;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            f1524a = sparseIntArray;
            sparseIntArray.append(R$styleable.KeyCycle_motionTarget, 1);
            f1524a.append(R$styleable.KeyCycle_framePosition, 2);
            f1524a.append(R$styleable.KeyCycle_transitionEasing, 3);
            f1524a.append(R$styleable.KeyCycle_curveFit, 4);
            f1524a.append(R$styleable.KeyCycle_waveShape, 5);
            f1524a.append(R$styleable.KeyCycle_wavePeriod, 6);
            f1524a.append(R$styleable.KeyCycle_waveOffset, 7);
            f1524a.append(R$styleable.KeyCycle_waveVariesBy, 8);
            f1524a.append(R$styleable.KeyCycle_android_alpha, 9);
            f1524a.append(R$styleable.KeyCycle_android_elevation, 10);
            f1524a.append(R$styleable.KeyCycle_android_rotation, 11);
            f1524a.append(R$styleable.KeyCycle_android_rotationX, 12);
            f1524a.append(R$styleable.KeyCycle_android_rotationY, 13);
            f1524a.append(R$styleable.KeyCycle_transitionPathRotate, 14);
            f1524a.append(R$styleable.KeyCycle_android_scaleX, 15);
            f1524a.append(R$styleable.KeyCycle_android_scaleY, 16);
            f1524a.append(R$styleable.KeyCycle_android_translationX, 17);
            f1524a.append(R$styleable.KeyCycle_android_translationY, 18);
            f1524a.append(R$styleable.KeyCycle_android_translationZ, 19);
            f1524a.append(R$styleable.KeyCycle_motionProgress, 20);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void b(KeyCycle keyCycle, TypedArray typedArray) {
            int indexCount = typedArray.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = typedArray.getIndex(i10);
                switch (f1524a.get(index)) {
                    case 1:
                        if (MotionLayout.R0) {
                            int resourceId = typedArray.getResourceId(index, keyCycle.f1483b);
                            keyCycle.f1483b = resourceId;
                            if (resourceId == -1) {
                                keyCycle.f1484c = typedArray.getString(index);
                                break;
                            } else {
                                break;
                            }
                        } else if (typedArray.peekValue(index).type == 3) {
                            keyCycle.f1484c = typedArray.getString(index);
                            break;
                        } else {
                            keyCycle.f1483b = typedArray.getResourceId(index, keyCycle.f1483b);
                            break;
                        }
                    case 2:
                        keyCycle.f1482a = typedArray.getInt(index, keyCycle.f1482a);
                        break;
                    case 3:
                        keyCycle.f1506g = typedArray.getString(index);
                        break;
                    case 4:
                        keyCycle.f1507h = typedArray.getInteger(index, keyCycle.f1507h);
                        break;
                    case 5:
                        keyCycle.f1508i = typedArray.getInt(index, keyCycle.f1508i);
                        break;
                    case 6:
                        keyCycle.f1509j = typedArray.getFloat(index, keyCycle.f1509j);
                        break;
                    case 7:
                        if (typedArray.peekValue(index).type == 5) {
                            keyCycle.f1510k = typedArray.getDimension(index, keyCycle.f1510k);
                            break;
                        } else {
                            keyCycle.f1510k = typedArray.getFloat(index, keyCycle.f1510k);
                            break;
                        }
                    case 8:
                        keyCycle.f1512m = typedArray.getInt(index, keyCycle.f1512m);
                        break;
                    case 9:
                        keyCycle.f1513n = typedArray.getFloat(index, keyCycle.f1513n);
                        break;
                    case 10:
                        keyCycle.f1514o = typedArray.getDimension(index, keyCycle.f1514o);
                        break;
                    case 11:
                        keyCycle.f1515p = typedArray.getFloat(index, keyCycle.f1515p);
                        break;
                    case 12:
                        keyCycle.f1517r = typedArray.getFloat(index, keyCycle.f1517r);
                        break;
                    case 13:
                        keyCycle.f1518s = typedArray.getFloat(index, keyCycle.f1518s);
                        break;
                    case 14:
                        keyCycle.f1516q = typedArray.getFloat(index, keyCycle.f1516q);
                        break;
                    case 15:
                        keyCycle.f1519t = typedArray.getFloat(index, keyCycle.f1519t);
                        break;
                    case 16:
                        keyCycle.f1520u = typedArray.getFloat(index, keyCycle.f1520u);
                        break;
                    case 17:
                        keyCycle.f1521v = typedArray.getDimension(index, keyCycle.f1521v);
                        break;
                    case 18:
                        keyCycle.f1522w = typedArray.getDimension(index, keyCycle.f1522w);
                        break;
                    case 19:
                        keyCycle.f1523x = typedArray.getDimension(index, keyCycle.f1523x);
                        break;
                    case 20:
                        keyCycle.f1511l = typedArray.getFloat(index, keyCycle.f1511l);
                        break;
                    default:
                        Log.e("KeyCycle", "unused attribute 0x" + Integer.toHexString(index) + "   " + f1524a.get(index));
                        break;
                }
            }
        }
    }

    public KeyCycle() {
        this.f1485d = 4;
        this.f1486e = new HashMap<>();
    }

    public void O(HashMap<String, KeyCycleOscillator> hashMap) {
        for (String str : hashMap.keySet()) {
            if (str.startsWith("CUSTOM")) {
                ConstraintAttribute constraintAttribute = this.f1486e.get(str.substring(7));
                if (constraintAttribute != null && constraintAttribute.c() == ConstraintAttribute.b.FLOAT_TYPE) {
                    hashMap.get(str).e(this.f1482a, this.f1508i, this.f1512m, this.f1509j, this.f1510k, constraintAttribute.d(), constraintAttribute);
                }
            }
            float P = P(str);
            if (!Float.isNaN(P)) {
                hashMap.get(str).d(this.f1482a, this.f1508i, this.f1512m, this.f1509j, this.f1510k, P);
            }
        }
    }

    public float P(String str) {
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
            case 156108012:
                if (str.equals("waveOffset")) {
                    c10 = '\f';
                    break;
                }
                break;
        }
        switch (c10) {
            case 0:
                return this.f1517r;
            case 1:
                return this.f1518s;
            case 2:
                return this.f1521v;
            case 3:
                return this.f1522w;
            case 4:
                return this.f1523x;
            case 5:
                return this.f1511l;
            case 6:
                return this.f1519t;
            case 7:
                return this.f1520u;
            case '\b':
                return this.f1515p;
            case '\t':
                return this.f1514o;
            case '\n':
                return this.f1516q;
            case 11:
                return this.f1513n;
            case '\f':
                return this.f1510k;
            default:
                Log.v("WARNING! KeyCycle", "  UNKNOWN  " + str);
                return Float.NaN;
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x0042. Please report as an issue. */
    @Override // androidx.constraintlayout.motion.widget.c
    public void a(HashMap<String, SplineSet> hashMap) {
        androidx.constraintlayout.motion.widget.a.e("KeyCycle", "add " + hashMap.size() + " values", 2);
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
                case 156108012:
                    if (str.equals("waveOffset")) {
                        c10 = '\f';
                        break;
                    }
                    break;
            }
            switch (c10) {
                case 0:
                    splineSet.e(this.f1482a, this.f1517r);
                    break;
                case 1:
                    splineSet.e(this.f1482a, this.f1518s);
                    break;
                case 2:
                    splineSet.e(this.f1482a, this.f1521v);
                    break;
                case 3:
                    splineSet.e(this.f1482a, this.f1522w);
                    break;
                case 4:
                    splineSet.e(this.f1482a, this.f1523x);
                    break;
                case 5:
                    splineSet.e(this.f1482a, this.f1511l);
                    break;
                case 6:
                    splineSet.e(this.f1482a, this.f1519t);
                    break;
                case 7:
                    splineSet.e(this.f1482a, this.f1520u);
                    break;
                case '\b':
                    splineSet.e(this.f1482a, this.f1515p);
                    break;
                case '\t':
                    splineSet.e(this.f1482a, this.f1514o);
                    break;
                case '\n':
                    splineSet.e(this.f1482a, this.f1516q);
                    break;
                case 11:
                    splineSet.e(this.f1482a, this.f1513n);
                    break;
                case '\f':
                    splineSet.e(this.f1482a, this.f1510k);
                    break;
                default:
                    Log.v("WARNING KeyCycle", "  UNKNOWN  " + str);
                    break;
            }
        }
    }

    @Override // androidx.constraintlayout.motion.widget.c
    public void b(HashSet<String> hashSet) {
        if (!Float.isNaN(this.f1513n)) {
            hashSet.add("alpha");
        }
        if (!Float.isNaN(this.f1514o)) {
            hashSet.add("elevation");
        }
        if (!Float.isNaN(this.f1515p)) {
            hashSet.add("rotation");
        }
        if (!Float.isNaN(this.f1517r)) {
            hashSet.add("rotationX");
        }
        if (!Float.isNaN(this.f1518s)) {
            hashSet.add("rotationY");
        }
        if (!Float.isNaN(this.f1519t)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.f1520u)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.f1516q)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.f1521v)) {
            hashSet.add("translationX");
        }
        if (!Float.isNaN(this.f1522w)) {
            hashSet.add("translationY");
        }
        if (!Float.isNaN(this.f1523x)) {
            hashSet.add("translationZ");
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
        a.b(this, context.obtainStyledAttributes(attributeSet, R$styleable.KeyCycle));
    }
}
