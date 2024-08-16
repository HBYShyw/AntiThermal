package m2;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/* compiled from: COUIRoundRectUtil.java */
/* renamed from: m2.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUIRoundRectUtil {

    /* renamed from: b, reason: collision with root package name */
    private static COUIRoundRectUtil f14908b;

    /* renamed from: a, reason: collision with root package name */
    private Path f14909a = new Path();

    private COUIRoundRectUtil() {
    }

    public static COUIRoundRectUtil a() {
        if (f14908b == null) {
            f14908b = new COUIRoundRectUtil();
        }
        return f14908b;
    }

    public Path b(Rect rect, float f10) {
        return c(new RectF(rect), f10);
    }

    public Path c(RectF rectF, float f10) {
        return COUIShapePath.a(this.f14909a, rectF, f10);
    }
}
