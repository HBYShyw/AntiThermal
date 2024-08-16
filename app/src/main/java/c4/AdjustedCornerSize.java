package c4;

import android.graphics.RectF;
import java.util.Arrays;

/* compiled from: AdjustedCornerSize.java */
/* renamed from: c4.b, reason: use source file name */
/* loaded from: classes.dex */
public final class AdjustedCornerSize implements CornerSize {

    /* renamed from: a, reason: collision with root package name */
    private final CornerSize f4760a;

    /* renamed from: b, reason: collision with root package name */
    private final float f4761b;

    public AdjustedCornerSize(float f10, CornerSize cornerSize) {
        while (cornerSize instanceof AdjustedCornerSize) {
            cornerSize = ((AdjustedCornerSize) cornerSize).f4760a;
            f10 += ((AdjustedCornerSize) cornerSize).f4761b;
        }
        this.f4760a = cornerSize;
        this.f4761b = f10;
    }

    @Override // c4.CornerSize
    public float a(RectF rectF) {
        return Math.max(0.0f, this.f4760a.a(rectF) + this.f4761b);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AdjustedCornerSize)) {
            return false;
        }
        AdjustedCornerSize adjustedCornerSize = (AdjustedCornerSize) obj;
        return this.f4760a.equals(adjustedCornerSize.f4760a) && this.f4761b == adjustedCornerSize.f4761b;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.f4760a, Float.valueOf(this.f4761b)});
    }
}
