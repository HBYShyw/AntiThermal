package c4;

import android.graphics.RectF;
import java.util.Arrays;

/* compiled from: RelativeCornerSize.java */
/* renamed from: c4.k, reason: use source file name */
/* loaded from: classes.dex */
public final class RelativeCornerSize implements CornerSize {

    /* renamed from: a, reason: collision with root package name */
    private final float f4813a;

    public RelativeCornerSize(float f10) {
        this.f4813a = f10;
    }

    @Override // c4.CornerSize
    public float a(RectF rectF) {
        return this.f4813a * rectF.height();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof RelativeCornerSize) && this.f4813a == ((RelativeCornerSize) obj).f4813a;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Float.valueOf(this.f4813a)});
    }
}
