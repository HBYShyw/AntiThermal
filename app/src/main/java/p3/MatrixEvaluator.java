package p3;

import android.animation.TypeEvaluator;
import android.graphics.Matrix;

/* compiled from: MatrixEvaluator.java */
/* renamed from: p3.g, reason: use source file name */
/* loaded from: classes.dex */
public class MatrixEvaluator implements TypeEvaluator<Matrix> {

    /* renamed from: a, reason: collision with root package name */
    private final float[] f16565a = new float[9];

    /* renamed from: b, reason: collision with root package name */
    private final float[] f16566b = new float[9];

    /* renamed from: c, reason: collision with root package name */
    private final Matrix f16567c = new Matrix();

    public Matrix a(float f10, Matrix matrix, Matrix matrix2) {
        matrix.getValues(this.f16565a);
        matrix2.getValues(this.f16566b);
        for (int i10 = 0; i10 < 9; i10++) {
            float[] fArr = this.f16566b;
            float f11 = fArr[i10];
            float[] fArr2 = this.f16565a;
            fArr[i10] = fArr2[i10] + ((f11 - fArr2[i10]) * f10);
        }
        this.f16567c.setValues(this.f16566b);
        return this.f16567c;
    }
}
