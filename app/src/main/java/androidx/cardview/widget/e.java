package androidx.cardview.widget;

import android.graphics.drawable.Drawable;

/* compiled from: RoundRectDrawableWithShadow.java */
/* loaded from: classes.dex */
class e extends Drawable {

    /* renamed from: a, reason: collision with root package name */
    private static final double f1377a = Math.cos(Math.toRadians(45.0d));

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(float f10, float f11, boolean z10) {
        return z10 ? (float) (f10 + ((1.0d - f1377a) * f11)) : f10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(float f10, float f11, boolean z10) {
        return z10 ? (float) ((f10 * 1.5f) + ((1.0d - f1377a) * f11)) : f10 * 1.5f;
    }
}
