package c4;

import android.graphics.RectF;
import java.util.Arrays;

/* compiled from: AbsoluteCornerSize.java */
/* renamed from: c4.a, reason: use source file name */
/* loaded from: classes.dex */
public final class AbsoluteCornerSize implements CornerSize {

    /* renamed from: a, reason: collision with root package name */
    private final float f4759a;

    public AbsoluteCornerSize(float f10) {
        this.f4759a = f10;
    }

    @Override // c4.CornerSize
    public float a(RectF rectF) {
        return this.f4759a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof AbsoluteCornerSize) && this.f4759a == ((AbsoluteCornerSize) obj).f4759a;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Float.valueOf(this.f4759a)});
    }
}
