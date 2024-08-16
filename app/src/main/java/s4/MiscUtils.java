package s4;

import android.graphics.Path;
import android.graphics.PointF;
import i4.KeyPathElementContent;
import java.util.List;
import l4.CubicCurveData;
import l4.KeyPath;
import n4.ShapeData;

/* compiled from: MiscUtils.java */
/* renamed from: s4.g, reason: use source file name */
/* loaded from: classes.dex */
public class MiscUtils {

    /* renamed from: a, reason: collision with root package name */
    private static final PointF f18058a = new PointF();

    public static PointF a(PointF pointF, PointF pointF2) {
        return new PointF(pointF.x + pointF2.x, pointF.y + pointF2.y);
    }

    public static double b(double d10, double d11, double d12) {
        return Math.max(d11, Math.min(d12, d10));
    }

    public static float c(float f10, float f11, float f12) {
        return Math.max(f11, Math.min(f12, f10));
    }

    public static int d(int i10, int i11, int i12) {
        return Math.max(i11, Math.min(i12, i10));
    }

    public static boolean e(float f10, float f11, float f12) {
        return f10 >= f11 && f10 <= f12;
    }

    private static int f(int i10, int i11) {
        int i12 = i10 / i11;
        return (((i10 ^ i11) >= 0) || i10 % i11 == 0) ? i12 : i12 - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int g(float f10, float f11) {
        return h((int) f10, (int) f11);
    }

    private static int h(int i10, int i11) {
        return i10 - (i11 * f(i10, i11));
    }

    public static void i(ShapeData shapeData, Path path) {
        path.reset();
        PointF b10 = shapeData.b();
        path.moveTo(b10.x, b10.y);
        f18058a.set(b10.x, b10.y);
        for (int i10 = 0; i10 < shapeData.a().size(); i10++) {
            CubicCurveData cubicCurveData = shapeData.a().get(i10);
            PointF a10 = cubicCurveData.a();
            PointF b11 = cubicCurveData.b();
            PointF c10 = cubicCurveData.c();
            PointF pointF = f18058a;
            if (a10.equals(pointF) && b11.equals(c10)) {
                path.lineTo(c10.x, c10.y);
            } else {
                path.cubicTo(a10.x, a10.y, b11.x, b11.y, c10.x, c10.y);
            }
            pointF.set(c10.x, c10.y);
        }
        if (shapeData.d()) {
            path.close();
        }
    }

    public static double j(double d10, double d11, double d12) {
        return d10 + (d12 * (d11 - d10));
    }

    public static float k(float f10, float f11, float f12) {
        return f10 + (f12 * (f11 - f10));
    }

    public static int l(int i10, int i11, float f10) {
        return (int) (i10 + (f10 * (i11 - i10)));
    }

    public static void m(KeyPath keyPath, int i10, List<KeyPath> list, KeyPath keyPath2, KeyPathElementContent keyPathElementContent) {
        if (keyPath.c(keyPathElementContent.getName(), i10)) {
            list.add(keyPath2.a(keyPathElementContent.getName()).i(keyPathElementContent));
        }
    }
}
