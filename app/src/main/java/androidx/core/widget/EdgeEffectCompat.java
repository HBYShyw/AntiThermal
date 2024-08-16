package androidx.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EdgeEffect;

/* compiled from: EdgeEffectCompat.java */
/* renamed from: androidx.core.widget.f, reason: use source file name */
/* loaded from: classes.dex */
public final class EdgeEffectCompat {

    /* compiled from: EdgeEffectCompat.java */
    /* renamed from: androidx.core.widget.f$a */
    /* loaded from: classes.dex */
    static class a {
        static void a(EdgeEffect edgeEffect, float f10, float f11) {
            edgeEffect.onPull(f10, f11);
        }
    }

    /* compiled from: EdgeEffectCompat.java */
    /* renamed from: androidx.core.widget.f$b */
    /* loaded from: classes.dex */
    private static class b {
        public static EdgeEffect a(Context context, AttributeSet attributeSet) {
            try {
                return new EdgeEffect(context, attributeSet);
            } catch (Throwable unused) {
                return new EdgeEffect(context);
            }
        }

        public static float b(EdgeEffect edgeEffect) {
            try {
                return edgeEffect.getDistance();
            } catch (Throwable unused) {
                return 0.0f;
            }
        }

        public static float c(EdgeEffect edgeEffect, float f10, float f11) {
            try {
                return edgeEffect.onPullDistance(f10, f11);
            } catch (Throwable unused) {
                edgeEffect.onPull(f10, f11);
                return 0.0f;
            }
        }
    }

    public static EdgeEffect a(Context context, AttributeSet attributeSet) {
        return b.a(context, attributeSet);
    }

    public static float b(EdgeEffect edgeEffect) {
        return b.b(edgeEffect);
    }

    public static void c(EdgeEffect edgeEffect, float f10, float f11) {
        a.a(edgeEffect, f10, f11);
    }

    public static float d(EdgeEffect edgeEffect, float f10, float f11) {
        return b.c(edgeEffect, f10, f11);
    }
}
