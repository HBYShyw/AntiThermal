package q4;

import android.graphics.Color;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;
import r4.c;

/* compiled from: JsonUtils.java */
/* renamed from: q4.t, reason: use source file name */
/* loaded from: classes.dex */
class JsonUtils {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16886a = c.a.a("x", "y");

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: JsonUtils.java */
    /* renamed from: q4.t$a */
    /* loaded from: classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f16887a;

        static {
            int[] iArr = new int[c.b.values().length];
            f16887a = iArr;
            try {
                iArr[c.b.NUMBER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f16887a[c.b.BEGIN_ARRAY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f16887a[c.b.BEGIN_OBJECT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    private static PointF a(r4.c cVar, float f10) {
        cVar.c();
        float N = (float) cVar.N();
        float N2 = (float) cVar.N();
        while (cVar.e0() != c.b.END_ARRAY) {
            cVar.m0();
        }
        cVar.u();
        return new PointF(N * f10, N2 * f10);
    }

    private static PointF b(r4.c cVar, float f10) {
        float N = (float) cVar.N();
        float N2 = (float) cVar.N();
        while (cVar.w()) {
            cVar.m0();
        }
        return new PointF(N * f10, N2 * f10);
    }

    private static PointF c(r4.c cVar, float f10) {
        cVar.m();
        float f11 = 0.0f;
        float f12 = 0.0f;
        while (cVar.w()) {
            int i02 = cVar.i0(f16886a);
            if (i02 == 0) {
                f11 = g(cVar);
            } else if (i02 != 1) {
                cVar.j0();
                cVar.m0();
            } else {
                f12 = g(cVar);
            }
        }
        cVar.v();
        return new PointF(f11 * f10, f12 * f10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int d(r4.c cVar) {
        cVar.c();
        int N = (int) (cVar.N() * 255.0d);
        int N2 = (int) (cVar.N() * 255.0d);
        int N3 = (int) (cVar.N() * 255.0d);
        while (cVar.w()) {
            cVar.m0();
        }
        cVar.u();
        return Color.argb(255, N, N2, N3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PointF e(r4.c cVar, float f10) {
        int i10 = a.f16887a[cVar.e0().ordinal()];
        if (i10 == 1) {
            return b(cVar, f10);
        }
        if (i10 == 2) {
            return a(cVar, f10);
        }
        if (i10 == 3) {
            return c(cVar, f10);
        }
        throw new IllegalArgumentException("Unknown point starts with " + cVar.e0());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<PointF> f(r4.c cVar, float f10) {
        ArrayList arrayList = new ArrayList();
        cVar.c();
        while (cVar.e0() == c.b.BEGIN_ARRAY) {
            cVar.c();
            arrayList.add(e(cVar, f10));
            cVar.u();
        }
        cVar.u();
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float g(r4.c cVar) {
        c.b e02 = cVar.e0();
        int i10 = a.f16887a[e02.ordinal()];
        if (i10 == 1) {
            return (float) cVar.N();
        }
        if (i10 == 2) {
            cVar.c();
            float N = (float) cVar.N();
            while (cVar.w()) {
                cVar.m0();
            }
            cVar.u();
            return N;
        }
        throw new IllegalArgumentException("Unknown value for token of type " + e02);
    }
}
