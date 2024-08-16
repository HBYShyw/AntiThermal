package androidx.core.view.animation;

import android.graphics.Path;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

/* compiled from: PathInterpolatorCompat.java */
/* renamed from: androidx.core.view.animation.a, reason: use source file name */
/* loaded from: classes.dex */
public final class PathInterpolatorCompat {

    /* compiled from: PathInterpolatorCompat.java */
    /* renamed from: androidx.core.view.animation.a$a */
    /* loaded from: classes.dex */
    static class a {
        static PathInterpolator a(float f10, float f11) {
            return new PathInterpolator(f10, f11);
        }

        static PathInterpolator b(float f10, float f11, float f12, float f13) {
            return new PathInterpolator(f10, f11, f12, f13);
        }

        static PathInterpolator c(Path path) {
            return new PathInterpolator(path);
        }
    }

    public static Interpolator a(float f10, float f11, float f12, float f13) {
        return a.b(f10, f11, f12, f13);
    }

    public static Interpolator b(Path path) {
        return a.c(path);
    }
}
