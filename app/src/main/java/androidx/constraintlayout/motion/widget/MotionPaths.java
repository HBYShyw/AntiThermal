package androidx.constraintlayout.motion.widget;

import android.view.View;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintSet;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.Arrays;
import java.util.LinkedHashMap;
import k.Easing;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MotionPaths.java */
/* renamed from: androidx.constraintlayout.motion.widget.p, reason: use source file name */
/* loaded from: classes.dex */
public class MotionPaths implements Comparable<MotionPaths> {

    /* renamed from: t, reason: collision with root package name */
    static String[] f1655t = {"position", "x", "y", "width", "height", "pathRotate"};

    /* renamed from: e, reason: collision with root package name */
    Easing f1656e;

    /* renamed from: f, reason: collision with root package name */
    int f1657f;

    /* renamed from: g, reason: collision with root package name */
    float f1658g;

    /* renamed from: h, reason: collision with root package name */
    float f1659h;

    /* renamed from: i, reason: collision with root package name */
    float f1660i;

    /* renamed from: j, reason: collision with root package name */
    float f1661j;

    /* renamed from: k, reason: collision with root package name */
    float f1662k;

    /* renamed from: l, reason: collision with root package name */
    float f1663l;

    /* renamed from: m, reason: collision with root package name */
    float f1664m;

    /* renamed from: n, reason: collision with root package name */
    float f1665n;

    /* renamed from: o, reason: collision with root package name */
    int f1666o;

    /* renamed from: p, reason: collision with root package name */
    LinkedHashMap<String, ConstraintAttribute> f1667p;

    /* renamed from: q, reason: collision with root package name */
    int f1668q;

    /* renamed from: r, reason: collision with root package name */
    double[] f1669r;

    /* renamed from: s, reason: collision with root package name */
    double[] f1670s;

    public MotionPaths() {
        this.f1657f = 0;
        this.f1664m = Float.NaN;
        this.f1665n = Float.NaN;
        this.f1666o = c.f1481f;
        this.f1667p = new LinkedHashMap<>();
        this.f1668q = 0;
        this.f1669r = new double[18];
        this.f1670s = new double[18];
    }

    private boolean c(float f10, float f11) {
        return (Float.isNaN(f10) || Float.isNaN(f11)) ? Float.isNaN(f10) != Float.isNaN(f11) : Math.abs(f10 - f11) > 1.0E-6f;
    }

    public void a(ConstraintSet.a aVar) {
        this.f1656e = Easing.c(aVar.f1954c.f1998c);
        ConstraintSet.c cVar = aVar.f1954c;
        this.f1666o = cVar.f1999d;
        this.f1664m = cVar.f2002g;
        this.f1657f = cVar.f2000e;
        this.f1665n = aVar.f1953b.f2007e;
        for (String str : aVar.f1957f.keySet()) {
            ConstraintAttribute constraintAttribute = aVar.f1957f.get(str);
            if (constraintAttribute.c() != ConstraintAttribute.b.STRING_TYPE) {
                this.f1667p.put(str, constraintAttribute);
            }
        }
    }

    @Override // java.lang.Comparable
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public int compareTo(MotionPaths motionPaths) {
        return Float.compare(this.f1659h, motionPaths.f1659h);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(MotionPaths motionPaths, boolean[] zArr, String[] strArr, boolean z10) {
        zArr[0] = zArr[0] | c(this.f1659h, motionPaths.f1659h);
        zArr[1] = zArr[1] | c(this.f1660i, motionPaths.f1660i) | z10;
        zArr[2] = z10 | c(this.f1661j, motionPaths.f1661j) | zArr[2];
        zArr[3] = zArr[3] | c(this.f1662k, motionPaths.f1662k);
        zArr[4] = c(this.f1663l, motionPaths.f1663l) | zArr[4];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(double[] dArr, int[] iArr) {
        float[] fArr = {this.f1659h, this.f1660i, this.f1661j, this.f1662k, this.f1663l, this.f1664m};
        int i10 = 0;
        for (int i11 = 0; i11 < iArr.length; i11++) {
            if (iArr[i11] < 6) {
                dArr[i10] = fArr[iArr[i11]];
                i10++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(int[] iArr, double[] dArr, float[] fArr, int i10) {
        float f10 = this.f1660i;
        float f11 = this.f1661j;
        float f12 = this.f1662k;
        float f13 = this.f1663l;
        for (int i11 = 0; i11 < iArr.length; i11++) {
            float f14 = (float) dArr[i11];
            int i12 = iArr[i11];
            if (i12 == 1) {
                f10 = f14;
            } else if (i12 == 2) {
                f11 = f14;
            } else if (i12 == 3) {
                f12 = f14;
            } else if (i12 == 4) {
                f13 = f14;
            }
        }
        fArr[i10] = f10 + (f12 / 2.0f) + 0.0f;
        fArr[i10 + 1] = f11 + (f13 / 2.0f) + 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int g(String str, double[] dArr, int i10) {
        ConstraintAttribute constraintAttribute = this.f1667p.get(str);
        if (constraintAttribute.f() == 1) {
            dArr[i10] = constraintAttribute.d();
            return 1;
        }
        int f10 = constraintAttribute.f();
        constraintAttribute.e(new float[f10]);
        int i11 = 0;
        while (i11 < f10) {
            dArr[i10] = r0[i11];
            i11++;
            i10++;
        }
        return f10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int h(String str) {
        return this.f1667p.get(str).f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(int[] iArr, double[] dArr, float[] fArr, int i10) {
        float f10 = this.f1660i;
        float f11 = this.f1661j;
        float f12 = this.f1662k;
        float f13 = this.f1663l;
        for (int i11 = 0; i11 < iArr.length; i11++) {
            float f14 = (float) dArr[i11];
            int i12 = iArr[i11];
            if (i12 == 1) {
                f10 = f14;
            } else if (i12 == 2) {
                f11 = f14;
            } else if (i12 == 3) {
                f12 = f14;
            } else if (i12 == 4) {
                f13 = f14;
            }
        }
        float f15 = f12 + f10;
        float f16 = f13 + f11;
        Float.isNaN(Float.NaN);
        Float.isNaN(Float.NaN);
        int i13 = i10 + 1;
        fArr[i10] = f10 + 0.0f;
        int i14 = i13 + 1;
        fArr[i13] = f11 + 0.0f;
        int i15 = i14 + 1;
        fArr[i14] = f15 + 0.0f;
        int i16 = i15 + 1;
        fArr[i15] = f11 + 0.0f;
        int i17 = i16 + 1;
        fArr[i16] = f15 + 0.0f;
        int i18 = i17 + 1;
        fArr[i17] = f16 + 0.0f;
        fArr[i18] = f10 + 0.0f;
        fArr[i18 + 1] = f16 + 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean j(String str) {
        return this.f1667p.containsKey(str);
    }

    void l(KeyPosition keyPosition, MotionPaths motionPaths, MotionPaths motionPaths2) {
        float f10 = keyPosition.f1482a / 100.0f;
        this.f1658g = f10;
        this.f1657f = keyPosition.f1556j;
        float f11 = Float.isNaN(keyPosition.f1557k) ? f10 : keyPosition.f1557k;
        float f12 = Float.isNaN(keyPosition.f1558l) ? f10 : keyPosition.f1558l;
        float f13 = motionPaths2.f1662k;
        float f14 = motionPaths.f1662k;
        float f15 = motionPaths2.f1663l;
        float f16 = motionPaths.f1663l;
        this.f1659h = this.f1658g;
        float f17 = motionPaths.f1660i;
        float f18 = motionPaths.f1661j;
        float f19 = (motionPaths2.f1660i + (f13 / 2.0f)) - ((f14 / 2.0f) + f17);
        float f20 = (motionPaths2.f1661j + (f15 / 2.0f)) - (f18 + (f16 / 2.0f));
        float f21 = ((f13 - f14) * f11) / 2.0f;
        this.f1660i = (int) ((f17 + (f19 * f10)) - f21);
        float f22 = ((f15 - f16) * f12) / 2.0f;
        this.f1661j = (int) ((f18 + (f20 * f10)) - f22);
        this.f1662k = (int) (f14 + r9);
        this.f1663l = (int) (f16 + r12);
        float f23 = Float.isNaN(keyPosition.f1559m) ? f10 : keyPosition.f1559m;
        float f24 = Float.isNaN(keyPosition.f1562p) ? 0.0f : keyPosition.f1562p;
        if (!Float.isNaN(keyPosition.f1560n)) {
            f10 = keyPosition.f1560n;
        }
        float f25 = Float.isNaN(keyPosition.f1561o) ? 0.0f : keyPosition.f1561o;
        this.f1668q = 2;
        this.f1660i = (int) (((motionPaths.f1660i + (f23 * f19)) + (f25 * f20)) - f21);
        this.f1661j = (int) (((motionPaths.f1661j + (f19 * f24)) + (f20 * f10)) - f22);
        this.f1656e = Easing.c(keyPosition.f1554h);
        this.f1666o = keyPosition.f1555i;
    }

    void m(KeyPosition keyPosition, MotionPaths motionPaths, MotionPaths motionPaths2) {
        float f10 = keyPosition.f1482a / 100.0f;
        this.f1658g = f10;
        this.f1657f = keyPosition.f1556j;
        float f11 = Float.isNaN(keyPosition.f1557k) ? f10 : keyPosition.f1557k;
        float f12 = Float.isNaN(keyPosition.f1558l) ? f10 : keyPosition.f1558l;
        float f13 = motionPaths2.f1662k - motionPaths.f1662k;
        float f14 = motionPaths2.f1663l - motionPaths.f1663l;
        this.f1659h = this.f1658g;
        if (!Float.isNaN(keyPosition.f1559m)) {
            f10 = keyPosition.f1559m;
        }
        float f15 = motionPaths.f1660i;
        float f16 = motionPaths.f1662k;
        float f17 = motionPaths.f1661j;
        float f18 = motionPaths.f1663l;
        float f19 = (motionPaths2.f1660i + (motionPaths2.f1662k / 2.0f)) - ((f16 / 2.0f) + f15);
        float f20 = (motionPaths2.f1661j + (motionPaths2.f1663l / 2.0f)) - ((f18 / 2.0f) + f17);
        float f21 = f19 * f10;
        float f22 = (f13 * f11) / 2.0f;
        this.f1660i = (int) ((f15 + f21) - f22);
        float f23 = f10 * f20;
        float f24 = (f14 * f12) / 2.0f;
        this.f1661j = (int) ((f17 + f23) - f24);
        this.f1662k = (int) (f16 + r7);
        this.f1663l = (int) (f18 + r8);
        float f25 = Float.isNaN(keyPosition.f1560n) ? 0.0f : keyPosition.f1560n;
        this.f1668q = 1;
        float f26 = (int) ((motionPaths.f1660i + f21) - f22);
        float f27 = (int) ((motionPaths.f1661j + f23) - f24);
        this.f1660i = f26 + ((-f20) * f25);
        this.f1661j = f27 + (f19 * f25);
        this.f1656e = Easing.c(keyPosition.f1554h);
        this.f1666o = keyPosition.f1555i;
    }

    void n(int i10, int i11, KeyPosition keyPosition, MotionPaths motionPaths, MotionPaths motionPaths2) {
        float f10 = keyPosition.f1482a / 100.0f;
        this.f1658g = f10;
        this.f1657f = keyPosition.f1556j;
        float f11 = Float.isNaN(keyPosition.f1557k) ? f10 : keyPosition.f1557k;
        float f12 = Float.isNaN(keyPosition.f1558l) ? f10 : keyPosition.f1558l;
        float f13 = motionPaths2.f1662k;
        float f14 = motionPaths.f1662k;
        float f15 = motionPaths2.f1663l;
        float f16 = motionPaths.f1663l;
        this.f1659h = this.f1658g;
        float f17 = motionPaths.f1660i;
        float f18 = motionPaths.f1661j;
        float f19 = motionPaths2.f1660i + (f13 / 2.0f);
        float f20 = motionPaths2.f1661j + (f15 / 2.0f);
        float f21 = (f13 - f14) * f11;
        this.f1660i = (int) ((f17 + ((f19 - ((f14 / 2.0f) + f17)) * f10)) - (f21 / 2.0f));
        float f22 = (f15 - f16) * f12;
        this.f1661j = (int) ((f18 + ((f20 - (f18 + (f16 / 2.0f))) * f10)) - (f22 / 2.0f));
        this.f1662k = (int) (f14 + f21);
        this.f1663l = (int) (f16 + f22);
        this.f1668q = 3;
        if (!Float.isNaN(keyPosition.f1559m)) {
            this.f1660i = (int) (keyPosition.f1559m * ((int) (i10 - this.f1662k)));
        }
        if (!Float.isNaN(keyPosition.f1560n)) {
            this.f1661j = (int) (keyPosition.f1560n * ((int) (i11 - this.f1663l)));
        }
        this.f1656e = Easing.c(keyPosition.f1554h);
        this.f1666o = keyPosition.f1555i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(float f10, float f11, float f12, float f13) {
        this.f1660i = f10;
        this.f1661j = f11;
        this.f1662k = f12;
        this.f1663l = f13;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p(float f10, float f11, float[] fArr, int[] iArr, double[] dArr, double[] dArr2) {
        float f12 = 0.0f;
        float f13 = 0.0f;
        float f14 = 0.0f;
        float f15 = 0.0f;
        for (int i10 = 0; i10 < iArr.length; i10++) {
            float f16 = (float) dArr[i10];
            double d10 = dArr2[i10];
            int i11 = iArr[i10];
            if (i11 == 1) {
                f12 = f16;
            } else if (i11 == 2) {
                f14 = f16;
            } else if (i11 == 3) {
                f13 = f16;
            } else if (i11 == 4) {
                f15 = f16;
            }
        }
        float f17 = f12 - ((0.0f * f13) / 2.0f);
        float f18 = f14 - ((0.0f * f15) / 2.0f);
        fArr[0] = (f17 * (1.0f - f10)) + (((f13 * 1.0f) + f17) * f10) + 0.0f;
        fArr[1] = (f18 * (1.0f - f11)) + (((f15 * 1.0f) + f18) * f11) + 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(View view, int[] iArr, double[] dArr, double[] dArr2, double[] dArr3) {
        float f10;
        boolean z10;
        float f11 = this.f1660i;
        float f12 = this.f1661j;
        float f13 = this.f1662k;
        float f14 = this.f1663l;
        boolean z11 = true;
        if (iArr.length != 0 && this.f1669r.length <= iArr[iArr.length - 1]) {
            int i10 = iArr[iArr.length - 1] + 1;
            this.f1669r = new double[i10];
            this.f1670s = new double[i10];
        }
        Arrays.fill(this.f1669r, Double.NaN);
        for (int i11 = 0; i11 < iArr.length; i11++) {
            this.f1669r[iArr[i11]] = dArr[i11];
            this.f1670s[iArr[i11]] = dArr2[i11];
        }
        int i12 = 0;
        float f15 = Float.NaN;
        float f16 = 0.0f;
        float f17 = 0.0f;
        float f18 = 0.0f;
        float f19 = 0.0f;
        while (true) {
            double[] dArr4 = this.f1669r;
            if (i12 >= dArr4.length) {
                break;
            }
            boolean isNaN = Double.isNaN(dArr4[i12]);
            double d10 = UserProfileInfo.Constant.NA_LAT_LON;
            if (isNaN && (dArr3 == null || dArr3[i12] == UserProfileInfo.Constant.NA_LAT_LON)) {
                f10 = f11;
                z10 = z11;
            } else {
                if (dArr3 != null) {
                    d10 = dArr3[i12];
                }
                if (!Double.isNaN(this.f1669r[i12])) {
                    d10 = this.f1669r[i12] + d10;
                }
                f10 = f11;
                float f20 = (float) d10;
                float f21 = (float) this.f1670s[i12];
                z10 = true;
                if (i12 == 1) {
                    f16 = f21;
                    f11 = f20;
                } else if (i12 == 2) {
                    f12 = f20;
                    f18 = f21;
                } else if (i12 == 3) {
                    f13 = f20;
                    f17 = f21;
                } else if (i12 == 4) {
                    f14 = f20;
                    f19 = f21;
                } else if (i12 == 5) {
                    f11 = f10;
                    f15 = f20;
                }
                i12++;
                z11 = z10;
            }
            f11 = f10;
            i12++;
            z11 = z10;
        }
        float f22 = f11;
        boolean z12 = z11;
        if (Float.isNaN(f15)) {
            if (!Float.isNaN(Float.NaN)) {
                view.setRotation(Float.NaN);
            }
        } else {
            view.setRotation((float) ((Float.isNaN(Float.NaN) ? 0.0f : Float.NaN) + f15 + Math.toDegrees(Math.atan2(f18 + (f19 / 2.0f), f16 + (f17 / 2.0f)))));
        }
        float f23 = f22 + 0.5f;
        int i13 = (int) f23;
        float f24 = f12 + 0.5f;
        int i14 = (int) f24;
        int i15 = (int) (f23 + f13);
        int i16 = (int) (f24 + f14);
        int i17 = i15 - i13;
        int i18 = i16 - i14;
        if ((i17 == view.getMeasuredWidth() && i18 == view.getMeasuredHeight()) ? false : z12) {
            view.measure(View.MeasureSpec.makeMeasureSpec(i17, 1073741824), View.MeasureSpec.makeMeasureSpec(i18, 1073741824));
        }
        view.layout(i13, i14, i15, i16);
    }

    public MotionPaths(int i10, int i11, KeyPosition keyPosition, MotionPaths motionPaths, MotionPaths motionPaths2) {
        this.f1657f = 0;
        this.f1664m = Float.NaN;
        this.f1665n = Float.NaN;
        this.f1666o = c.f1481f;
        this.f1667p = new LinkedHashMap<>();
        this.f1668q = 0;
        this.f1669r = new double[18];
        this.f1670s = new double[18];
        int i12 = keyPosition.f1563q;
        if (i12 == 1) {
            m(keyPosition, motionPaths, motionPaths2);
        } else if (i12 != 2) {
            l(keyPosition, motionPaths, motionPaths2);
        } else {
            n(i10, i11, keyPosition, motionPaths, motionPaths2);
        }
    }
}
