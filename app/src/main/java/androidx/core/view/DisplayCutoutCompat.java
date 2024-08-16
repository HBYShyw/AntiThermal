package androidx.core.view;

import android.graphics.Rect;
import android.view.DisplayCutout;
import androidx.core.util.ObjectsCompat;
import java.util.List;

/* compiled from: DisplayCutoutCompat.java */
/* renamed from: androidx.core.view.c, reason: use source file name */
/* loaded from: classes.dex */
public final class DisplayCutoutCompat {

    /* renamed from: a, reason: collision with root package name */
    private final DisplayCutout f2353a;

    /* compiled from: DisplayCutoutCompat.java */
    /* renamed from: androidx.core.view.c$a */
    /* loaded from: classes.dex */
    static class a {
        static DisplayCutout a(Rect rect, List<Rect> list) {
            return new DisplayCutout(rect, list);
        }

        static List<Rect> b(DisplayCutout displayCutout) {
            return displayCutout.getBoundingRects();
        }

        static int c(DisplayCutout displayCutout) {
            return displayCutout.getSafeInsetBottom();
        }

        static int d(DisplayCutout displayCutout) {
            return displayCutout.getSafeInsetLeft();
        }

        static int e(DisplayCutout displayCutout) {
            return displayCutout.getSafeInsetRight();
        }

        static int f(DisplayCutout displayCutout) {
            return displayCutout.getSafeInsetTop();
        }
    }

    private DisplayCutoutCompat(DisplayCutout displayCutout) {
        this.f2353a = displayCutout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DisplayCutoutCompat e(DisplayCutout displayCutout) {
        if (displayCutout == null) {
            return null;
        }
        return new DisplayCutoutCompat(displayCutout);
    }

    public int a() {
        return a.c(this.f2353a);
    }

    public int b() {
        return a.d(this.f2353a);
    }

    public int c() {
        return a.e(this.f2353a);
    }

    public int d() {
        return a.f(this.f2353a);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || DisplayCutoutCompat.class != obj.getClass()) {
            return false;
        }
        return ObjectsCompat.a(this.f2353a, ((DisplayCutoutCompat) obj).f2353a);
    }

    public int hashCode() {
        DisplayCutout displayCutout = this.f2353a;
        if (displayCutout == null) {
            return 0;
        }
        return displayCutout.hashCode();
    }

    public String toString() {
        return "DisplayCutoutCompat{" + this.f2353a + "}";
    }
}
