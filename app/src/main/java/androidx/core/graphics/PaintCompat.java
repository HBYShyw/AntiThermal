package androidx.core.graphics;

import android.graphics.Paint;
import android.graphics.Rect;
import androidx.core.util.Pair;

/* compiled from: PaintCompat.java */
/* renamed from: androidx.core.graphics.c, reason: use source file name */
/* loaded from: classes.dex */
public final class PaintCompat {

    /* renamed from: a, reason: collision with root package name */
    private static final ThreadLocal<Pair<Rect, Rect>> f2189a = new ThreadLocal<>();

    /* compiled from: PaintCompat.java */
    /* renamed from: androidx.core.graphics.c$a */
    /* loaded from: classes.dex */
    static class a {
        static boolean a(Paint paint, String str) {
            return paint.hasGlyph(str);
        }
    }

    public static boolean a(Paint paint, String str) {
        return a.a(paint, str);
    }
}
