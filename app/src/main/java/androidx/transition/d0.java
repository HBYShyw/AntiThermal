package androidx.transition;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Property;
import android.view.View;
import androidx.core.view.ViewCompat;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ViewUtils.java */
/* loaded from: classes.dex */
public class d0 {

    /* renamed from: a, reason: collision with root package name */
    private static final ViewUtilsBase f4094a = new ViewUtilsApi29();

    /* renamed from: b, reason: collision with root package name */
    static final Property<View, Float> f4095b = new a(Float.class, "translationAlpha");

    /* renamed from: c, reason: collision with root package name */
    static final Property<View, Rect> f4096c = new b(Rect.class, "clipBounds");

    /* compiled from: ViewUtils.java */
    /* loaded from: classes.dex */
    static class a extends Property<View, Float> {
        a(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(View view) {
            return Float.valueOf(d0.c(view));
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(View view, Float f10) {
            d0.h(view, f10.floatValue());
        }
    }

    /* compiled from: ViewUtils.java */
    /* loaded from: classes.dex */
    static class b extends Property<View, Rect> {
        b(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Rect get(View view) {
            return ViewCompat.r(view);
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(View view, Rect rect) {
            ViewCompat.s0(view, rect);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(View view) {
        f4094a.a(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static c0 b(View view) {
        return new b0(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float c(View view) {
        return f4094a.b(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static WindowIdImpl d(View view) {
        return new WindowIdApi18(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void e(View view) {
        f4094a.c(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void f(View view, Matrix matrix) {
        f4094a.d(view, matrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void g(View view, int i10, int i11, int i12, int i13) {
        f4094a.e(view, i10, i11, i12, i13);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void h(View view, float f10) {
        f4094a.f(view, f10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void i(View view, int i10) {
        f4094a.g(view, i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void j(View view, Matrix matrix) {
        f4094a.h(view, matrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void k(View view, Matrix matrix) {
        f4094a.i(view, matrix);
    }
}
