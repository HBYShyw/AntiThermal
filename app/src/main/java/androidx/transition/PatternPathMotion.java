package androidx.transition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes.dex */
public class PatternPathMotion extends PathMotion {

    /* renamed from: a, reason: collision with root package name */
    private Path f4044a;

    /* renamed from: b, reason: collision with root package name */
    private final Path f4045b;

    /* renamed from: c, reason: collision with root package name */
    private final Matrix f4046c;

    public PatternPathMotion() {
        Path path = new Path();
        this.f4045b = path;
        this.f4046c = new Matrix();
        path.lineTo(1.0f, 0.0f);
        this.f4044a = path;
    }

    private static float b(float f10, float f11) {
        return (float) Math.sqrt((f10 * f10) + (f11 * f11));
    }

    @Override // androidx.transition.PathMotion
    public Path a(float f10, float f11, float f12, float f13) {
        float f14 = f12 - f10;
        float f15 = f13 - f11;
        float b10 = b(f14, f15);
        double atan2 = Math.atan2(f15, f14);
        this.f4046c.setScale(b10, b10);
        this.f4046c.postRotate((float) Math.toDegrees(atan2));
        this.f4046c.postTranslate(f10, f11);
        Path path = new Path();
        this.f4045b.transform(this.f4046c, path);
        return path;
    }

    public void c(Path path) {
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float[] fArr = new float[2];
        pathMeasure.getPosTan(pathMeasure.getLength(), fArr, null);
        float f10 = fArr[0];
        float f11 = fArr[1];
        pathMeasure.getPosTan(0.0f, fArr, null);
        float f12 = fArr[0];
        float f13 = fArr[1];
        if (f12 == f10 && f13 == f11) {
            throw new IllegalArgumentException("pattern must not end at the starting point");
        }
        this.f4046c.setTranslate(-f12, -f13);
        float f14 = f10 - f12;
        float f15 = f11 - f13;
        float b10 = 1.0f / b(f14, f15);
        this.f4046c.postScale(b10, b10);
        this.f4046c.postRotate((float) Math.toDegrees(-Math.atan2(f15, f14)));
        path.transform(this.f4046c, this.f4045b);
        this.f4044a = path;
    }

    @SuppressLint({"RestrictedApi"})
    public PatternPathMotion(Context context, AttributeSet attributeSet) {
        this.f4045b = new Path();
        this.f4046c = new Matrix();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.f4138k);
        try {
            String i10 = TypedArrayUtils.i(obtainStyledAttributes, (XmlPullParser) attributeSet, "patternPathData", 0);
            if (i10 != null) {
                c(androidx.core.graphics.d.d(i10));
                return;
            }
            throw new RuntimeException("pathData must be supplied for patternPathMotion");
        } finally {
            obtainStyledAttributes.recycle();
        }
    }
}
