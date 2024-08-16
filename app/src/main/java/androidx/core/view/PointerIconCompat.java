package androidx.core.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.PointerIcon;

/* compiled from: PointerIconCompat.java */
/* renamed from: androidx.core.view.w, reason: use source file name */
/* loaded from: classes.dex */
public final class PointerIconCompat {

    /* renamed from: a, reason: collision with root package name */
    private final PointerIcon f2415a;

    /* compiled from: PointerIconCompat.java */
    /* renamed from: androidx.core.view.w$a */
    /* loaded from: classes.dex */
    static class a {
        static PointerIcon a(Bitmap bitmap, float f10, float f11) {
            return PointerIcon.create(bitmap, f10, f11);
        }

        static PointerIcon b(Context context, int i10) {
            return PointerIcon.getSystemIcon(context, i10);
        }

        static PointerIcon c(Resources resources, int i10) {
            return PointerIcon.load(resources, i10);
        }
    }

    private PointerIconCompat(PointerIcon pointerIcon) {
        this.f2415a = pointerIcon;
    }

    public static PointerIconCompat b(Context context, int i10) {
        return new PointerIconCompat(a.b(context, i10));
    }

    public Object a() {
        return this.f2415a;
    }
}
