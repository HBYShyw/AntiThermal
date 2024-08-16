package androidx.appcompat.widget;

import android.graphics.Insets;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* compiled from: DrawableUtils.java */
/* loaded from: classes.dex */
public class t {

    /* renamed from: a, reason: collision with root package name */
    private static final int[] f1310a = {16842912};

    /* renamed from: b, reason: collision with root package name */
    private static final int[] f1311b = new int[0];

    /* renamed from: c, reason: collision with root package name */
    public static final Rect f1312c = new Rect();

    /* compiled from: DrawableUtils.java */
    /* loaded from: classes.dex */
    static class a {
        static Insets a(Drawable drawable) {
            return drawable.getOpticalInsets();
        }
    }

    public static boolean a(Drawable drawable) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(Drawable drawable) {
        drawable.getClass();
    }

    public static Rect c(Drawable drawable) {
        Insets a10 = a.a(drawable);
        return new Rect(a10.left, a10.top, a10.right, a10.bottom);
    }

    public static PorterDuff.Mode d(int i10, PorterDuff.Mode mode) {
        if (i10 == 3) {
            return PorterDuff.Mode.SRC_OVER;
        }
        if (i10 == 5) {
            return PorterDuff.Mode.SRC_IN;
        }
        if (i10 != 9) {
            switch (i10) {
                case 14:
                    return PorterDuff.Mode.MULTIPLY;
                case 15:
                    return PorterDuff.Mode.SCREEN;
                case 16:
                    return PorterDuff.Mode.ADD;
                default:
                    return mode;
            }
        }
        return PorterDuff.Mode.SRC_ATOP;
    }
}
