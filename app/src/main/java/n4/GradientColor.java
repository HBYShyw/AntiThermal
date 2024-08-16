package n4;

import s4.GammaEvaluator;
import s4.MiscUtils;

/* compiled from: GradientColor.java */
/* renamed from: n4.d, reason: use source file name */
/* loaded from: classes.dex */
public class GradientColor {

    /* renamed from: a, reason: collision with root package name */
    private final float[] f15758a;

    /* renamed from: b, reason: collision with root package name */
    private final int[] f15759b;

    public GradientColor(float[] fArr, int[] iArr) {
        this.f15758a = fArr;
        this.f15759b = iArr;
    }

    public int[] a() {
        return this.f15759b;
    }

    public float[] b() {
        return this.f15758a;
    }

    public int c() {
        return this.f15759b.length;
    }

    public void d(GradientColor gradientColor, GradientColor gradientColor2, float f10) {
        if (gradientColor.f15759b.length == gradientColor2.f15759b.length) {
            for (int i10 = 0; i10 < gradientColor.f15759b.length; i10++) {
                this.f15758a[i10] = MiscUtils.k(gradientColor.f15758a[i10], gradientColor2.f15758a[i10], f10);
                this.f15759b[i10] = GammaEvaluator.c(f10, gradientColor.f15759b[i10], gradientColor2.f15759b[i10]);
            }
            return;
        }
        throw new IllegalArgumentException("Cannot interpolate between gradients. Lengths vary (" + gradientColor.f15759b.length + " vs " + gradientColor2.f15759b.length + ")");
    }
}
