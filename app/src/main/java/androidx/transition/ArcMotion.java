package androidx.transition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes.dex */
public class ArcMotion extends PathMotion {

    /* renamed from: g, reason: collision with root package name */
    private static final float f3955g = (float) Math.tan(Math.toRadians(35.0d));

    /* renamed from: a, reason: collision with root package name */
    private float f3956a;

    /* renamed from: b, reason: collision with root package name */
    private float f3957b;

    /* renamed from: c, reason: collision with root package name */
    private float f3958c;

    /* renamed from: d, reason: collision with root package name */
    private float f3959d;

    /* renamed from: e, reason: collision with root package name */
    private float f3960e;

    /* renamed from: f, reason: collision with root package name */
    private float f3961f;

    public ArcMotion() {
        this.f3956a = 0.0f;
        this.f3957b = 0.0f;
        this.f3958c = 70.0f;
        this.f3959d = 0.0f;
        this.f3960e = 0.0f;
        this.f3961f = f3955g;
    }

    private static float e(float f10) {
        if (f10 >= 0.0f && f10 <= 90.0f) {
            return (float) Math.tan(Math.toRadians(f10 / 2.0f));
        }
        throw new IllegalArgumentException("Arc must be between 0 and 90 degrees");
    }

    @Override // androidx.transition.PathMotion
    public Path a(float f10, float f11, float f12, float f13) {
        float f14;
        float f15;
        float f16;
        Path path = new Path();
        path.moveTo(f10, f11);
        float f17 = f12 - f10;
        float f18 = f13 - f11;
        float f19 = (f17 * f17) + (f18 * f18);
        float f20 = (f10 + f12) / 2.0f;
        float f21 = (f11 + f13) / 2.0f;
        float f22 = 0.25f * f19;
        boolean z10 = f11 > f13;
        if (Math.abs(f17) < Math.abs(f18)) {
            float abs = Math.abs(f19 / (f18 * 2.0f));
            if (z10) {
                f15 = abs + f13;
                f14 = f12;
            } else {
                f15 = abs + f11;
                f14 = f10;
            }
            f16 = this.f3960e;
        } else {
            float f23 = f19 / (f17 * 2.0f);
            if (z10) {
                f15 = f11;
                f14 = f23 + f10;
            } else {
                f14 = f12 - f23;
                f15 = f13;
            }
            f16 = this.f3959d;
        }
        float f24 = f22 * f16 * f16;
        float f25 = f20 - f14;
        float f26 = f21 - f15;
        float f27 = (f25 * f25) + (f26 * f26);
        float f28 = this.f3961f;
        float f29 = f22 * f28 * f28;
        if (f27 >= f24) {
            f24 = f27 > f29 ? f29 : 0.0f;
        }
        if (f24 != 0.0f) {
            float sqrt = (float) Math.sqrt(f24 / f27);
            f14 = ((f14 - f20) * sqrt) + f20;
            f15 = f21 + (sqrt * (f15 - f21));
        }
        path.cubicTo((f10 + f14) / 2.0f, (f11 + f15) / 2.0f, (f14 + f12) / 2.0f, (f15 + f13) / 2.0f, f12, f13);
        return path;
    }

    public void b(float f10) {
        this.f3958c = f10;
        this.f3961f = e(f10);
    }

    public void c(float f10) {
        this.f3956a = f10;
        this.f3959d = e(f10);
    }

    public void d(float f10) {
        this.f3957b = f10;
        this.f3960e = e(f10);
    }

    @SuppressLint({"RestrictedApi"})
    public ArcMotion(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f3956a = 0.0f;
        this.f3957b = 0.0f;
        this.f3958c = 70.0f;
        this.f3959d = 0.0f;
        this.f3960e = 0.0f;
        this.f3961f = f3955g;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.f4137j);
        XmlPullParser xmlPullParser = (XmlPullParser) attributeSet;
        d(TypedArrayUtils.f(obtainStyledAttributes, xmlPullParser, "minimumVerticalAngle", 1, 0.0f));
        c(TypedArrayUtils.f(obtainStyledAttributes, xmlPullParser, "minimumHorizontalAngle", 0, 0.0f));
        b(TypedArrayUtils.f(obtainStyledAttributes, xmlPullParser, "maximumAngle", 2, 70.0f));
        obtainStyledAttributes.recycle();
    }
}
